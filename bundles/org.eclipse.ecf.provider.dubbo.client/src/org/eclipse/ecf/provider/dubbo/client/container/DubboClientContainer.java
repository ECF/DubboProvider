/*******************************************************************************
 * Copyright (c) 2019 Composent, Inc. and others. All rights reserved. This
 * program and the accompanying materials are made available under the terms of
 * the Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Composent, Inc. - initial API and implementation
 ******************************************************************************/
package org.eclipse.ecf.provider.dubbo.client.container;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Callable;

import org.apache.dubbo.config.ReferenceConfig;
import org.eclipse.ecf.provider.dubbo.client.Activator;
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

	private final Map<IRemoteServiceReference, DubboRSAClientService> reftoServiceMap;

	public DubboClientContainer(String clientApplicationName) {
		super(DubboNamespace.createClientID(clientApplicationName + UUID.randomUUID().toString()));
		this.reftoServiceMap = Collections
				.synchronizedMap(new HashMap<IRemoteServiceReference, DubboRSAClientService>());
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
	public void disconnect() {
		super.disconnect();
		Collection<DubboRSAClientService> removed = null;
		synchronized (reftoServiceMap) {
			removed = this.reftoServiceMap.values();
			this.reftoServiceMap.clear();
		}
		removed.forEach(s -> s.dispose());
	}

	@Override
	public boolean ungetRemoteService(IRemoteServiceReference reference) {
		DubboRSAClientService svc = this.reftoServiceMap.remove(reference);
		if (svc != null) {
			svc.dispose();
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
			Activator a = Activator.getInstance();
			if (a == null) {
				throw new NullPointerException("Dubbo Client Bundle has bene deactivated");
			}
			reference.setApplication(a.getApplicationConfig());
			reference.setUrl(getConnectedID().getName());
			reference.setInterface(registration.getClazzes()[0]);
			DubboRSAClientService svc = new DubboRSAClientService(this, registration, reference);
			this.reftoServiceMap.put(registration.getReference(), svc);
			return svc;
		} finally {
			currentThread.setContextClassLoader(ccl);
		}
	}
}
