/*******************************************************************************
 * Copyright (c) 2019 Composent, Inc. and others. All rights reserved. This
 * program and the accompanying materials are made available under the terms of
 * the Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Composent, Inc. - initial API and implementation
 ******************************************************************************/
package org.eclipse.ecf.provider.dubbo.client;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.dubbo.config.ApplicationConfig;
import org.eclipse.ecf.core.ContainerTypeDescription;
import org.eclipse.ecf.core.IContainer;
import org.eclipse.ecf.provider.dubbo.client.container.DubboClientContainer;
import org.eclipse.ecf.provider.dubbo.common.DubboConstants;
import org.eclipse.ecf.remoteservice.provider.IRemoteServiceDistributionProvider;
import org.eclipse.ecf.remoteservice.provider.RemoteServiceContainerInstantiator;
import org.eclipse.ecf.remoteservice.provider.RemoteServiceDistributionProvider;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class Activator implements BundleActivator {

	public static final String[] dubboIntents = new String[] { "dubbo" };

	private static Activator instance;
	private ApplicationConfig appConfig;
	
	public ApplicationConfig getApplicationConfig() {
		return appConfig;
	}
	
	public static Activator getInstance() {
		return instance;
	}
	
	@Override
	public void start(BundleContext context) throws Exception {
		this.instance = this;
		this.appConfig = new ApplicationConfig(UUID.randomUUID().toString());
		context.registerService(IRemoteServiceDistributionProvider.class,
				new RemoteServiceDistributionProvider.Builder().setName(DubboConstants.CLIENT_PROVIDER_CONFIG_TYPE)
						.setInstantiator(
								new RemoteServiceContainerInstantiator(DubboConstants.SERVER_PROVIDER_CONFIG_TYPE,
										DubboConstants.CLIENT_PROVIDER_CONFIG_TYPE) {
									@Override
									public IContainer createInstance(ContainerTypeDescription description,
											Map<String, ?> parameters) {
										String applicationName = getParameterValue(parameters,
												DubboConstants.CLIENT_APPLICATION_NAME_PROP,
												DubboConstants.CLIENT_APPLICATION_NAME_DEFAULT);
										return new DubboClientContainer(applicationName);
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
						.setServer(false).setHidden(false).build(),
				null);

	}

	@Override
	public void stop(BundleContext context) throws Exception {
		this.appConfig = null;
		this.instance = null;
	}

}
