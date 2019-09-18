/*******************************************************************************
 * Copyright (c) 2019 Composent, Inc. and others. All rights reserved. This
 * program and the accompanying materials are made available under the terms of
 * the Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Composent, Inc. - initial API and implementation
 ******************************************************************************/
package org.eclipse.ecf.provider.internal.dubbo;

import org.apache.dubbo.common.extension.ExtensionLoader;
import org.apache.dubbo.config.DubboShutdownHook;
import org.apache.dubbo.registry.support.AbstractRegistryFactory;
import org.apache.dubbo.rpc.Protocol;
import org.eclipse.ecf.core.identity.IDFactory;
import org.eclipse.ecf.provider.dubbo.identity.DubboNamespace;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class Activator implements BundleActivator {

	@Override
	public void start(BundleContext context) throws Exception {
		IDFactory.getDefault().addNamespace(DubboNamespace.INSTANCE);
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		AbstractRegistryFactory.destroyAll();
		ExtensionLoader<Protocol> loader = ExtensionLoader.getExtensionLoader(Protocol.class);
		for (String protocolName : loader.getLoadedExtensions()) {
			try {
				Protocol protocol = loader.getLoadedExtension(protocolName);
				if (protocol != null) {
					protocol.destroy();
				}
			} catch (Throwable t) {
			}
		}

		DubboShutdownHook.getDubboShutdownHook().unregister();

	}

}
