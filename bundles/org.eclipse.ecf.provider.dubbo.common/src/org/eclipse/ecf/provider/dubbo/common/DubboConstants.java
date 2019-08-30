/*******************************************************************************
 * Copyright (c) 2019 Composent, Inc. and others. All rights reserved. This
 * program and the accompanying materials are made available under the terms of
 * the Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Composent, Inc. - initial API and implementation
 ******************************************************************************/
package org.eclipse.ecf.provider.dubbo.common;

import org.eclipse.ecf.provider.dubbo.identity.DubboNamespace;

public interface DubboConstants {

	public static final String NAMESPACE_NAME = DubboNamespace.NAME;
	public static final String SERVER_PROVIDER_CONFIG_TYPE = "ecf.dubbo.server";
	public static final String CLIENT_PROVIDER_CONFIG_TYPE = "ecf.dubbo.client";
	public static final String HOSTNAME_PROP = "hostName";
	public static final String HOSTNAME_DEFAULT = "127.0.0.1";
	public static final String APPLICATION_NAME_PROP = "applicationName";
	public static final String APPLICATION_NAME_DEFAULT = "ecf-remoteservice-application";
	public static final String CLIENT_APPLICATION_NAME_PROP = "clientApplicationName";
	public static final String CLIENT_APPLICATION_NAME_DEFAULT = "ecf-remoteservice-client-application";

}
