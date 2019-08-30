/*******************************************************************************
 * Copyright (c) 2019 Composent, Inc. and others. All rights reserved. This
 * program and the accompanying materials are made available under the terms of
 * the Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Composent, Inc. - initial API and implementation
 ******************************************************************************/
package org.eclipse.ecf.provider.dubbo.client.container;

import java.util.concurrent.Callable;

import org.apache.dubbo.config.ApplicationConfig;
import org.apache.dubbo.config.ReferenceConfig;
import org.eclipse.ecf.provider.dubbo.common.DubboConstants;
import org.eclipse.ecf.provider.dubbo.identity.DubboNamespace;
import org.eclipse.ecf.remoteservice.IRemoteService;
import org.eclipse.ecf.remoteservice.IRemoteServiceReference;
import org.eclipse.ecf.remoteservice.client.AbstractClientContainer;
import org.eclipse.ecf.remoteservice.client.AbstractRSAClientContainer;
import org.eclipse.ecf.remoteservice.client.AbstractRSAClientService;
import org.eclipse.ecf.remoteservice.client.RemoteServiceClientRegistration;
import org.eclipse.ecf.remoteservice.events.IRemoteCallCompleteEvent;

public class DubboClientContainer extends AbstractRSAClientContainer {

	private final ApplicationConfig applicationConfig;

	public DubboClientContainer(String clientApplicationName) {
		super(DubboNamespace.createClientID(clientApplicationName));
		this.applicationConfig = new ApplicationConfig(clientApplicationName);
	}

	protected class DubboRSAClientService extends AbstractRSAClientService {

		@SuppressWarnings("rawtypes")
		private ReferenceConfig referenceConfig;
		private Object service;

		public DubboRSAClientService(AbstractClientContainer container, RemoteServiceClientRegistration registration,
				@SuppressWarnings("rawtypes") ReferenceConfig referenceConfig) {
			super(container, registration);
			this.referenceConfig = referenceConfig;
			this.service = this.referenceConfig.get();
		}

		@Override
		public void dispose() {
			super.dispose();
			if (referenceConfig != null && referenceConfig.isValid()) {
				try {
					referenceConfig.destroy();
					referenceConfig = null;
					service = null;
				} catch (Exception e) {

				}
			}
		}
		
		@Override
		protected Callable<Object> getSyncCallable(RSARemoteCall call) {
			return () -> {
				return call.getReflectMethod().invoke(service, call.getParameters());
			};
		}

		@Override
		protected Callable<IRemoteCallCompleteEvent> getAsyncCallable(RSARemoteCall call) {
			return () -> {
				Object result = call.getReflectMethod().invoke(service, call.getParameters());
				if (result instanceof Throwable)
					return createRCCEFailure((Throwable) result);
				return createRCCESuccess(result);
			};
		}

	}

	@Override
	public boolean ungetRemoteService(IRemoteServiceReference reference) {
		IRemoteService svc = getRemoteService(reference);
		if (svc instanceof DubboRSAClientService) {
			DubboRSAClientService service = (DubboRSAClientService) svc;
			service.dispose();
		}
		return super.ungetRemoteService(reference);
	}

	@Override
	protected IRemoteService createRemoteService(RemoteServiceClientRegistration registration) {
		Thread currentThread = Thread.currentThread();
		ClassLoader ccl = currentThread.getContextClassLoader();
		currentThread.setContextClassLoader(DubboConstants.class.getClassLoader());
		try {
			@SuppressWarnings("rawtypes")
			ReferenceConfig reference = new ReferenceConfig<>();
			reference.setApplication(applicationConfig);
			reference.setUrl(getConnectedID().getName());
			reference.setInterface(registration.getClazzes()[0]);
			return new DubboRSAClientService(this, registration, reference);
		} finally {
			currentThread.setContextClassLoader(ccl);
		}
	}
}
