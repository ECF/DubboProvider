package org.eclipse.ecf.provider.dubbo.server;

import java.net.InetAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.dubbo.common.extension.ExtensionLoader;
import org.apache.dubbo.common.utils.NetUtils;
import org.apache.dubbo.rpc.Protocol;
import org.eclipse.ecf.core.ContainerCreateException;
import org.eclipse.ecf.core.ContainerTypeDescription;
import org.eclipse.ecf.core.IContainer;
import org.eclipse.ecf.core.identity.IDFactory;
import org.eclipse.ecf.core.identity.URIID;
import org.eclipse.ecf.core.util.BundleStarter;
import org.eclipse.ecf.provider.dubbo.common.DubboConstants;
import org.eclipse.ecf.provider.dubbo.identity.DubboNamespace;
import org.eclipse.ecf.provider.dubbo.server.container.DubboServerContainer;
import org.eclipse.ecf.remoteservice.provider.IRemoteServiceDistributionProvider;
import org.eclipse.ecf.remoteservice.provider.RemoteServiceContainerInstantiator;
import org.eclipse.ecf.remoteservice.provider.RemoteServiceDistributionProvider;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class Activator implements BundleActivator {

	public static final String[] dubboIntents = new String[] { DubboConstants.DUBBO_NAME };

	public static final String[] START_DEPENDENTS = new String[] { "org.eclipse.ecf.provider.dubbo.common" };

	private static String getDubboApplicationName(String appName) {
		return (appName == null) ? DubboConstants.APPLICATION_NAME_PREFIX + UUID.randomUUID().toString() : appName;
	}

	private static String getDubboHostname(String hostName) throws ContainerCreateException {
		if (NetUtils.isInvalidLocalHost(hostName)) {
			try {
				return InetAddress.getLocalHost().getHostAddress();
			} catch (UnknownHostException e) {
				throw new ContainerCreateException("Could not get host address for localhost", e);
			}
		}
		return hostName;
	}

	private static int getDubboPort(String port) throws ContainerCreateException {
		try {
			if (port == null || "-1".equals(port)) {
				return ExtensionLoader.getExtensionLoader(Protocol.class).getExtension(DubboConstants.DUBBO_NAME)
						.getDefaultPort();
			} else if ("0".equals(port)) {
				return NetUtils.getAvailablePort();
			} else {
				return Integer.valueOf(port);
			}
		} catch (Exception e) {
			throw new ContainerCreateException("Cannot get port from string=" + port);
		}
	}

	@Override
	public void start(BundleContext context) throws Exception {
		BundleStarter.startDependents(context, START_DEPENDENTS, Bundle.RESOLVED | Bundle.STARTING);
		context.registerService(IRemoteServiceDistributionProvider.class,
				new RemoteServiceDistributionProvider.Builder().setName(DubboConstants.SERVER_PROVIDER_CONFIG_TYPE)
						.setInstantiator(
								new RemoteServiceContainerInstantiator(DubboConstants.SERVER_PROVIDER_CONFIG_TYPE,
										DubboConstants.CLIENT_PROVIDER_CONFIG_TYPE) {
									@Override
									public IContainer createInstance(ContainerTypeDescription description,
											Map<String, ?> parameters) throws ContainerCreateException {

										String id = getParameterValue(parameters, DubboConstants.ID_PROP);

										if (id == null) {
											String hostName = getDubboHostname(getParameterValue(parameters,
													DubboConstants.HOSTNAME_PROP, DubboConstants.HOSTNAME_DEFAULT));

											int port = getDubboPort(getParameterValue(parameters,
													DubboConstants.PORT_PROP, DubboConstants.PORT_DEFAULT));

											String applicationName = getDubboApplicationName(
													getParameterValue(parameters, DubboConstants.APPLICATION_NAME_PROP,
															DubboConstants.APPLICATION_NAME_DEFAULT));

											id = DubboNamespace.SCHEME + "://" + hostName + ":" + String.valueOf(port)
													+ "/" + applicationName;
										}
										// Construct URI with dubbo://<hostname>:<port>/<applicationName>
										URI uri = null;
										try {
											uri = new URI(id);
										} catch (URISyntaxException e) {
											throw new ContainerCreateException(
													"Could not create dubbo uri from id=" + id);
										}
										checkOSGIIntents(description, uri, parameters);
										return new DubboServerContainer((URIID) IDFactory.getDefault()
												.createID(DubboNamespace.INSTANCE, new Object[] { uri }));
									}

									@Override
									protected boolean supportsOSGIPrivateIntent(ContainerTypeDescription description) {
										return true;
									}

									@Override
									protected boolean supportsOSGIAsyncIntent(ContainerTypeDescription description) {
										return true;
									}

									public String[] getSupportedIntents(ContainerTypeDescription description) {
										List<String> results = new ArrayList<String>();
										String[] genericIntents = super.getSupportedIntents(description);
										for (int i = 0; i < genericIntents.length; i++)
											results.add(genericIntents[i]);
										for (int i = 0; i < dubboIntents.length; i++)
											results.add(dubboIntents[i]);
										return (String[]) results.toArray(new String[] {});
									}

								})
						.setServer(true).setHidden(false).build(),
				null);
	}

	@Override
	public void stop(BundleContext context) throws Exception {
	}

}
