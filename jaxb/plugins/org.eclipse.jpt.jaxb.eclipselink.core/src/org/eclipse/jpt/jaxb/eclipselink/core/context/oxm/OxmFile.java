/*******************************************************************************
 *  Copyright (c) 2012  Oracle. All rights reserved.
 *  This program and the accompanying materials are made available under the
 *  terms of the Eclipse Public License v1.0, which accompanies this distribution
 *  and is available at http://www.eclipse.org/legal/epl-v10.html
 *  
 *  Contributors: 
 *  	Oracle - initial API and implementation
 *******************************************************************************/
package org.eclipse.jpt.jaxb.eclipselink.core.context.oxm;

import org.eclipse.jpt.common.core.resource.xml.JptXmlResource;
import org.eclipse.jpt.jaxb.core.context.JaxbContextNode;
import org.eclipse.jpt.jaxb.eclipselink.core.context.ELJaxbPackage;

/**
 * Provisional API: This interface is part of an interim API that is still
 * under development and expected to change significantly before reaching
 * stability. It is available at this early stage to solicit feedback from
 * pioneering adopters on the understanding that any code that uses this API
 * will almost certainly be broken (repeatedly) as the API evolves.
 * 
 * @version 3.3
 * @since 3.3
 */
public interface OxmFile 
		extends JaxbContextNode {
	
	JptXmlResource getOxmResource();
	
	String getPackageName();
	
	/** Convenience: will return the package associated with the package name  */
	ELJaxbPackage getPackage();
	
	
	// ***** xml bindings *****
	
	static final String XML_BINDINGS_PROPERTY = "xmlBindings"; //$NON-NLS-1$
	
	OxmXmlBindings getXmlBindings();
}
