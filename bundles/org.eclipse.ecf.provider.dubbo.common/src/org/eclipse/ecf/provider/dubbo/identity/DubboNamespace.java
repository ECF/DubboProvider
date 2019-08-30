/*******************************************************************************
 * Copyright (c) 2019 Composent, Inc. and others. All rights reserved. This
 * program and the accompanying materials are made available under the terms of
 * the Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Composent, Inc. - initial API and implementation
 ******************************************************************************/
package org.eclipse.ecf.provider.dubbo.identity;

import java.net.URI;
import java.nio.charset.StandardCharsets;

import org.eclipse.ecf.core.identity.ID;
import org.eclipse.ecf.core.identity.IDCreateException;
import org.eclipse.ecf.core.identity.IDFactory;
import org.eclipse.ecf.core.identity.URIID;
import org.eclipse.ecf.core.identity.URIID.URIIDNamespace;

public class DubboNamespace extends URIIDNamespace {

	private static final long serialVersionUID = 1169210024627883880L;

	public static final String NAME = "ecf.namespace.dubbo";
	public static final String SCHEME = "dubbo";

	public static final DubboNamespace INSTANCE = new DubboNamespace();

	private DubboNamespace() {
		super(NAME, "Dubbo ID Namespace");
	}

	@Override
	public String getScheme() {
		return SCHEME;
	}

	public ID createInstance(Object[] parameters) throws IDCreateException {
		try {
			String init = getInitStringFromExternalForm(parameters);
			if (init != null)
				return new URIID(this, new URI(init));
			if (parameters[0] instanceof URI)
				return new URIID(this, (URI) parameters[0]);
			if (parameters[0] instanceof String) {
				return new URIID(this,
						new URI(java.net.URLDecoder.decode((String) parameters[0], StandardCharsets.UTF_8.name())));
			}
			throw new IDCreateException("Cannot create URIID");
		} catch (Exception e) {
			throw new IDCreateException(getName() + " createInstance()", e); //$NON-NLS-1$
		}
	}

	public static URIID createClientID(String clientApplicationName) {
		return (URIID) IDFactory.getDefault().createID(INSTANCE,
				new Object[] { SCHEME + "://127.0.0.1/" + clientApplicationName });
	}
}
