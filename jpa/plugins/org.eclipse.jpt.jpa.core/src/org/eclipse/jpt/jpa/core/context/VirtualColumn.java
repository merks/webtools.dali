/*******************************************************************************
 * Copyright (c) 2010, 2011 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 * 
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.jpa.core.context;

/**
 * Virtual column
 * <p>
 * Provisional API: This interface is part of an interim API that is still
 * under development and expected to change significantly before reaching
 * stability. It is available at this early stage to solicit feedback from
 * pioneering adopters on the understanding that any code that uses this API
 * will almost certainly be broken (repeatedly) as the API evolves.
 */
public interface VirtualColumn
	extends VirtualBaseColumn, ReadOnlyColumn
{
	ReadOnlyColumn getOverriddenColumn();


	// ********** owner **********

	/**
	 * Interface allowing the virtual column to be get the column it overrides.
	 */
	interface Owner
		extends BaseColumn.Owner
	{
		/**
		 * Return the column overridden by the virtual column.
		 */
		ReadOnlyColumn resolveOverriddenColumn();
	}
}
