package org.eclipse.ecf.provider.dubbo.server.container;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import org.apache.dubbo.config.ApplicationConfig;
import org.apache.dubbo.config.ProtocolConfig;
import org.apache.dubbo.config.RegistryConfig;
import org.apache.dubbo.config.ServiceConfig;
import org.eclipse.ecf.core.identity.URIID;
import org.eclipse.ecf.provider.dubbo.common.DubboConstants;
import org.eclipse.ecf.remoteservice.AbstractRSAContainer;
import org.eclipse.ecf.remoteservice.RSARemoteServiceContainerAdapter.RSARemoteServiceRegistration;

public class DubboServerContainer extends AbstractRSAContainer {

	private final String DUBBO_REGISTRY_URL = "osgirs://127.0.0.1";

	private final ProtocolConfig protocolConfig;
	private final ApplicationConfig applicationConfig;
	private final RegistryConfig registryConfig;
	@SuppressWarnings("rawtypes")
	private final Map<RSARemoteServiceRegistration, ServiceConfig> regToSvcConfigMap;

	@SuppressWarnings("rawtypes")
	public DubboServerContainer(URIID id) {
		super(id);
		regToSvcConfigMap = new HashMap<RSARemoteServiceRegistration, ServiceConfig>();
		this.applicationConfig = new ApplicationConfig(id.toURI().getPath().replaceAll("/", ""));
		this.registryConfig = new RegistryConfig(DUBBO_REGISTRY_URL);
		this.protocolConfig = new ProtocolConfig();
		this.protocolConfig.refresh();
	}

	@SuppressWarnings("unchecked")
	@Override
	protected Map<String, Object> exportRemoteService(RSARemoteServiceRegistration registration) {
		Map<String, Object> properties = new HashMap<String, Object>();
		Thread currentThread = Thread.currentThread();
		// get existing context classloader
		ClassLoader ccl = currentThread.getContextClassLoader();
		// set context classloader to the org.eclipse.ecf.provider.dubbo.common
		// classloader so that dubbo can find necessary classes (uses ccl)
		currentThread.setContextClassLoader(DubboConstants.class.getClassLoader());
		try {
			// Create ServiceConfig object for exported service
			@SuppressWarnings("rawtypes")
			ServiceConfig serviceConfig = new ServiceConfig();
			// Set application config
			serviceConfig.setApplication(applicationConfig);
			// Set registry to OSGiServiceRegistry which is a dummy registry
			serviceConfig.setRegistry(registryConfig);
			// get the service instance from the registration
			Object svc = registration.getService();
			// Set dubbo interface as first interface (getInterfaces[0])
			serviceConfig.setInterface(svc.getClass().getInterfaces()[0]);
			// Set the reference to the service instance
			serviceConfig.setRef(svc);
			URI uri = ((URIID) getID()).toURI();
			int port = uri.getPort();
			if (port == 0) {
				this.protocolConfig.setPort(0);
			} else if (port > 0) {
				this.protocolConfig.setPort(port);
			}
			String hostName = uri.getHost();
			if (hostName != null) {
				this.protocolConfig.setHost(hostName);
			}
			serviceConfig.setProtocol(protocolConfig);
			// export via dubbo here
			synchronized (regToSvcConfigMap) {
				serviceConfig.export();
				if (serviceConfig.isExported()) {
					// set endpoint id to dubbo URL as urlencoded string
					try {
						String url = java.net.URLEncoder.encode(serviceConfig.toUrl().toString(),
								StandardCharsets.UTF_8.name());
						properties.put("ecf.endpoint.id", url);
					} catch (UnsupportedEncodingException e) {
						// Can't happen for utf-8
					}
					// put registration and serviceConfig into map
					regToSvcConfigMap.put(registration, serviceConfig);
				}
			}
			return properties;
		} finally {
			// reset context classloader
			currentThread.setContextClassLoader(ccl);
		}
	}

	private void unexport(RSARemoteServiceRegistration registration) {
		synchronized (regToSvcConfigMap) {
			@SuppressWarnings("rawtypes")
			ServiceConfig config = regToSvcConfigMap.get(registration);
			if (config != null && config.isExported()) {
				try {
					config.unexport();
				} catch (Exception e) {

				}
			}

		}
	}

	@Override
	protected void unexportRemoteService(RSARemoteServiceRegistration registration) {
		unexport(registration);
	}

	@Override
	public void dispose() {
		super.dispose();
		synchronized (regToSvcConfigMap) {
			regToSvcConfigMap.forEach((k, v) -> unexport(k));
		}
	}
}
