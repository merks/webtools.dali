/*******************************************************************************
 * Copyright (c) 2007, 2008 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 * 
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.core.resource.java;

/**
 * Corresponds to the javax.persistence.AttributeOverride annotation
 * 
 * Provisional API: This interface is part of an interim API that is still
 * under development and expected to change significantly before reaching
 * stability. It is available at this early stage to solicit feedback from
 * pioneering adopters on the understanding that any code that uses this API
 * will almost certainly be broken (repeatedly) as the API evolves.
 */
public interface AttributeOverrideAnnotation extends OverrideAnnotation
{
	String ANNOTATION_NAME = JPA.ATTRIBUTE_OVERRIDE;
		
	/**
	 * Corresponds to the column element of the AttributeOverride annotation.
	 * Returns null if the column element does not exist in java.
	 */
	ColumnAnnotation getColumn();
	
	/**
	 * Add the column element to the AttributeOverride annotation.
	 */
	ColumnAnnotation addColumn();
	
	/**
	 * Remove the column element from the AttributeOverride annotation.
	 */
	void removeColumn();
	
	ColumnAnnotation getNonNullColumn();
	
	String COLUMN_PROPERTY = "column"; //$NON-NLS-1$

}
