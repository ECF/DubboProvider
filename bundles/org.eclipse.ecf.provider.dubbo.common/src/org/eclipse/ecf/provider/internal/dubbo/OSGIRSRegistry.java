/*******************************************************************************
 * Copyright (c) 2019 Composent, Inc. and others. All rights reserved. This
 * program and the accompanying materials are made available under the terms of
 * the Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Composent, Inc. - initial API and implementation
 ******************************************************************************/
package org.eclipse.ecf.provider.internal.dubbo;

import java.util.List;

import org.apache.dubbo.common.URL;
import org.apache.dubbo.registry.NotifyListener;
import org.apache.dubbo.registry.Registry;

public class OSGIRSRegistry implements Registry {

	private final URL url;

	public OSGIRSRegistry(URL url) {
		this.url = url;
	}

	@Override
	public URL getUrl() {
		return url;
	}

	@Override
	public boolean isAvailable() {
		return true;
	}

	@Override
	public void destroy() {
	}

	@Override
	public void register(URL url) {
	}

	@Override
	public void unregister(URL url) {
	}

	@Override
	public void subscribe(URL url, NotifyListener listener) {
	}

	@Override
	public void unsubscribe(URL url, NotifyListener listener) {
	}

	@Override
	public List<URL> lookup(URL url) {
		return null;
	}

}
