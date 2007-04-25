/*******************************************************************************
 * Copyright (c) 2006, 2007 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License v1.0, which accompanies this distribution and is available at
 * http://www.eclipse.org/legal/epl-v10.html.
 * 
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.core.internal.content.orm.resource;

import org.eclipse.jpt.core.internal.content.orm.OrmPackage;
import org.eclipse.wst.common.internal.emf.resource.DependencyTranslator;

public class TypeJavaClassTranslator extends DependencyTranslator
	implements OrmXmlMapper
{
	protected static final OrmPackage JPA_CORE_XML_PKG = 
		OrmPackage.eINSTANCE;
	
	
	public TypeJavaClassTranslator() {
		super(CLASS, JPA_CORE_XML_PKG.getXmlPersistentType_Class(), 
				JPA_CORE_XML_PKG.getXmlTypeMapping_PersistentType());
		fStyle = DOM_ATTRIBUTE;
	}
	
}
