/*******************************************************************************
 * Copyright (c) 2019 Composent, Inc. and others. All rights reserved. This
 * program and the accompanying materials are made available under the terms of
 * the Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Composent, Inc. - initial API and implementation
 ******************************************************************************/
package org.eclipse.ecf.provider.internal.dubbo;

import org.apache.dubbo.common.URL;
import org.apache.dubbo.registry.Registry;
import org.apache.dubbo.registry.RegistryFactory;

public class OSGIRSRegistryFactory implements RegistryFactory {

	@Override
	public Registry getRegistry(URL url) {
		return new OSGIRSRegistry(url);
	}

}
