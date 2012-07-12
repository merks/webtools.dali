/*******************************************************************************
 * Copyright (c) 2011, 2012 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 * 
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.common.core.resource.java;

import org.eclipse.jpt.common.core.utility.jdt.TypeBinding;


/**
 * Java source code or binary attribute (field/method)
 * 
 * Provisional API: This interface is part of an interim API that is still
 * under development and expected to change significantly before reaching
 * stability. It is available at this early stage to solicit feedback from
 * pioneering adopters on the understanding that any code that uses this API
 * will almost certainly be broken (repeatedly) as the API evolves.
 * 
 * @version 3.3
 * @since 3.0
 */
public interface JavaResourceAttribute
		extends JavaResourceMember {
	
	JavaResourceType getParent();
	
	JavaResourceType getResourceType();
	
	
	// ***** modifiers *****
	
	String MODIFIERS_PROPERTY = "modifiers"; //$NON-NLS-1$
	
	/**
	 * @see java.lang.reflect.Modifier
	 */
	int getModifiers();
	
	
	// ***** type binding *****
	
	String TYPE_BINDING_PROPERTY = "typeBinding"; //$//$NON-NLS-1$
	
	TypeBinding getTypeBinding();
	
	/**
	 * Return the type binding for this attribute within the context of the given type.
	 * (This may very well be different if this attribute has a generic type.)
	 */
	TypeBinding getTypeBinding(JavaResourceType contextType);
}
