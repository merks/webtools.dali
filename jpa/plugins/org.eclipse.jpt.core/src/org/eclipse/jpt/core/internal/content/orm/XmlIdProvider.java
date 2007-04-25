/*******************************************************************************
 * Copyright (c) 2006 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License v1.0, which accompanies this distribution and is available at
 * http://www.eclipse.org/legal/epl-v10.html.
 * 
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.core.internal.content.orm;

import org.eclipse.jpt.core.internal.IMappingKeys;

public class XmlIdProvider implements IXmlAttributeMappingProvider
{
	// singleton
	private static final XmlIdProvider INSTANCE = new XmlIdProvider();

	/**
	 * Return the singleton.
	 */
	public static IXmlAttributeMappingProvider instance() {
		return INSTANCE;
	}

	/**
	 * Ensure non-instantiability.
	 */
	private XmlIdProvider() {
		super();
	}

	public XmlAttributeMapping buildAttributeMapping() {
		return OrmFactory.eINSTANCE.createXmlId();
	}

	public String key() {
		return IMappingKeys.ID_ATTRIBUTE_MAPPING_KEY;
	}
}
