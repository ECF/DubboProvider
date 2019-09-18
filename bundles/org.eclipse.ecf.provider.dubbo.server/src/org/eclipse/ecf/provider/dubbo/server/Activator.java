package org.eclipse.ecf.provider.dubbo.server;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.ecf.core.ContainerCreateException;
import org.eclipse.ecf.core.ContainerTypeDescription;
import org.eclipse.ecf.core.IContainer;
import org.eclipse.ecf.core.identity.IDFactory;
import org.eclipse.ecf.core.identity.URIID;
import org.eclipse.ecf.provider.dubbo.common.DubboConstants;
import org.eclipse.ecf.provider.dubbo.identity.DubboNamespace;
import org.eclipse.ecf.provider.dubbo.server.container.DubboServerContainer;
import org.eclipse.ecf.remoteservice.provider.IRemoteServiceDistributionProvider;
import org.eclipse.ecf.remoteservice.provider.RemoteServiceContainerInstantiator;
import org.eclipse.ecf.remoteservice.provider.RemoteServiceDistributionProvider;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class Activator implements BundleActivator {

	public static final String[] dubboIntents = new String[] { "dubbo" };

	private DubboNamespace ns;
	
	@Override
	public void start(BundleContext context) throws Exception {
		// Make sure common bundle is loaded/started
		ns = DubboNamespace.INSTANCE;
		context.registerService(IRemoteServiceDistributionProvider.class,
				new RemoteServiceDistributionProvider.Builder().setName(DubboConstants.SERVER_PROVIDER_CONFIG_TYPE)
						.setInstantiator(
								new RemoteServiceContainerInstantiator(DubboConstants.SERVER_PROVIDER_CONFIG_TYPE,
										DubboConstants.CLIENT_PROVIDER_CONFIG_TYPE) {
									@Override
									public IContainer createInstance(ContainerTypeDescription description,
											Map<String, ?> parameters) throws ContainerCreateException {
										String hostName = getParameterValue(parameters, DubboConstants.HOSTNAME_PROP,
												DubboConstants.HOSTNAME_DEFAULT);

										String applicationName = getParameterValue(parameters,
												DubboConstants.APPLICATION_NAME_PROP,
												DubboConstants.APPLICATION_NAME_DEFAULT);
										URI uri = null;
										try {
											uri = new URI(
													DubboNamespace.SCHEME + "://" + hostName + "/" + applicationName);
										} catch (URISyntaxException e) {
											throw new ContainerCreateException(
													"Could not create uri.  applicationName=" + applicationName);
										}
										checkOSGIIntents(description, uri, parameters);
										URIID containerId = (URIID) IDFactory.getDefault()
												.createID(DubboNamespace.INSTANCE, new Object[] { uri });
										return new DubboServerContainer(containerId);
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
		this.ns = null;
	}

}
