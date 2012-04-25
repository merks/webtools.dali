/*******************************************************************************
 * Copyright (c) 2010, 2012 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 * 
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.jaxb.core.context;

import org.eclipse.jpt.jaxb.core.context.java.JavaContextNode;
import org.eclipse.jpt.jaxb.core.resource.java.XmlNsAnnotation;

/**
 * 
 * <p>
 * Provisional API: This interface is part of an interim API that is still
 * under development and expected to change significantly before reaching
 * stability. It is available at this early stage to solicit feedback from
 * pioneering adopters on the understanding that any code that uses this API
 * will almost certainly be broken (repeatedly) as the API evolves.
 * 
 * @version 3.0
 * @since 3.0
 */
public interface XmlNs
		extends JavaContextNode {
	
	
	XmlNsAnnotation getResourceXmlNs();
	
	
	// ***** namespaceURI *****
	
	String NAMESPACE_URI_PROPERTY = "namespaceURI"; //$NON-NLS-1$
	
	String getNamespaceURI();
	
	void setNamespaceURI(String namespaceURI);
	
	
	// ***** prefix *****
	
	String PREFIX_PROPERTY = "prefix"; //$NON-NLS-1$
	
	String getPrefix();
	
	void setPrefix(String prefix);
}
