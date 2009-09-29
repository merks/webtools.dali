/*******************************************************************************
 *  Copyright (c) 2009  Oracle. 
 *  All rights reserved.  This program and the accompanying materials are 
 *  made available under the terms of the Eclipse Public License v1.0 which 
 *  accompanies this distribution, and is available at 
 *  http://www.eclipse.org/legal/epl-v10.html
 *  
 *  Contributors: 
 *  	Oracle - initial API and implementation
 *******************************************************************************/
package org.eclipse.jpt.eclipselink.ui.internal.v2_0.persistence;

import org.eclipse.jpt.core.JpaResourceType;
import org.eclipse.jpt.core.JptCorePlugin;
import org.eclipse.jpt.eclipselink.ui.internal.structure.EclipseLinkPersistenceResourceModelStructureProvider;
import org.eclipse.jpt.ui.ResourceUiDefinition;
import org.eclipse.jpt.ui.internal.persistence.details.AbstractPersistenceXmlResourceUiDefinition;
import org.eclipse.jpt.ui.internal.persistence.details.PersistenceXmlUiFactory;
import org.eclipse.jpt.ui.structure.JpaStructureProvider;

public class EclipseLinkPersistenceXml2_0UiDefinition extends AbstractPersistenceXmlResourceUiDefinition
{
	// singleton
	private static final ResourceUiDefinition INSTANCE = new EclipseLinkPersistenceXml2_0UiDefinition();
	
	
	/**
	 * Return the singleton
	 */
	public static ResourceUiDefinition instance() {
		return INSTANCE;
	}
	
	
	/**
	 * Enforce singleton usage
	 */
	private EclipseLinkPersistenceXml2_0UiDefinition() {
		super();
	}
	
	
	@Override
	protected PersistenceXmlUiFactory buildPersistenceXmlUiFactory() {
		return new EclipseLink2_0PersistenceXmlUiFactory();
	}
	
	public boolean providesUi(JpaResourceType resourceType) {
		return resourceType.equals(JptCorePlugin.PERSISTENCE_XML_2_0_RESOURCE_TYPE);
	}
	
	public JpaStructureProvider getStructureProvider() {
		return EclipseLinkPersistenceResourceModelStructureProvider.instance();
		//TODO do we need an EclipseLinkPersistence2_0ResourceModelStructureProvider??
//		return Persistence2_0ResourceModelStructureProvider.instance();
	}
}
