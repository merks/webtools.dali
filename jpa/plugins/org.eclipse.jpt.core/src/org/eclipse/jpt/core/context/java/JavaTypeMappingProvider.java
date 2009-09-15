/*******************************************************************************
 * Copyright (c) 2006, 2008 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 * 
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.core.context.java;

import org.eclipse.jpt.core.JpaFactory;

/**
 * Map a string key to a type mapping and its corresponding
 * Java annotation.
 * 
 * Provisional API: This interface is part of an interim API that is still
 * under development and expected to change significantly before reaching
 * stability. It is available at this early stage to solicit feedback from
 * pioneering adopters on the understanding that any code that uses this API
 * will almost certainly be broken (repeatedly) as the API evolves.
 */
public interface JavaTypeMappingProvider 
{
	/**
	 * Return the type mapping's key.
	 */
	String getKey();

	/**
	 * Return the type mapping's Java annotation name.
	 */
	String getAnnotationName();

	/**
	 * Build a Java type mapping for the specified type. Use the specified
	 * factory for creation so extenders can simply override the appropriate
	 * creation method instead of building a provider for the same key.
	 */
	public JavaTypeMapping buildMapping(JavaPersistentType type, JpaFactory factory);
	
	/**
	 * Return whether this mapping provider should be used for the given {@link JavaPersistentType}, 
	 * considering all annotations.
	 */
	boolean test(JavaPersistentType persistentType);
}
