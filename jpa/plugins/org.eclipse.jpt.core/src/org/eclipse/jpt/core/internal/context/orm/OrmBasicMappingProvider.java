/*******************************************************************************
 * Copyright (c) 2006, 2009 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 * 
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.core.internal.context.orm;

import org.eclipse.emf.ecore.EFactory;
import org.eclipse.jpt.core.MappingKeys;
import org.eclipse.jpt.core.context.java.JavaAttributeMapping;
import org.eclipse.jpt.core.context.java.JavaBasicMapping;
import org.eclipse.jpt.core.context.orm.OrmAttributeMappingProvider;
import org.eclipse.jpt.core.context.orm.OrmBasicMapping;
import org.eclipse.jpt.core.context.orm.OrmPersistentAttribute;
import org.eclipse.jpt.core.context.orm.OrmTypeMapping;
import org.eclipse.jpt.core.context.orm.OrmXmlContextNodeFactory;
import org.eclipse.jpt.core.resource.orm.OrmPackage;
import org.eclipse.jpt.core.resource.orm.XmlAttributeMapping;
import org.eclipse.jpt.core.resource.orm.XmlBasic;
import org.eclipse.jpt.core.resource.xml.EmfTools;

public class OrmBasicMappingProvider
	implements OrmAttributeMappingProvider
{
	// singleton
	private static final OrmAttributeMappingProvider INSTANCE = 
			new OrmBasicMappingProvider();
	
	
	/**
	 * Return the singleton
	 */
	public static OrmAttributeMappingProvider instance() {
		return INSTANCE;
	}
	
	
	/**
	 * Enforce singleton usage
	 */
	private OrmBasicMappingProvider() {
		super();
	}
	
	
	public String getKey() {
		return MappingKeys.BASIC_ATTRIBUTE_MAPPING_KEY;
	}
	
	public XmlAttributeMapping buildResourceMapping(EFactory factory) {
		return EmfTools.create(
				factory, 
				OrmPackage.eINSTANCE.getXmlBasic(), 
				XmlBasic.class);
	}
	
	public OrmBasicMapping buildContextMapping(
			OrmPersistentAttribute parent, 
			XmlAttributeMapping resourceMapping, 
			OrmXmlContextNodeFactory factory) {
		return factory.buildOrmBasicMapping(parent, (XmlBasic) resourceMapping);
	}
	
	public XmlAttributeMapping buildVirtualResourceMapping(
			OrmTypeMapping ormTypeMapping, 
			JavaAttributeMapping javaAttributeMapping, 
			OrmXmlContextNodeFactory factory) {
		return factory.buildVirtualXmlBasic(ormTypeMapping, (JavaBasicMapping) javaAttributeMapping);
	}
}
