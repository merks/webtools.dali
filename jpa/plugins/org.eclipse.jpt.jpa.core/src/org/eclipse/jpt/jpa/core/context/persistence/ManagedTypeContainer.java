/*******************************************************************************
 * Copyright (c) 2013 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 * 
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.jpa.core.context.persistence;

import org.eclipse.jpt.common.utility.transformer.Transformer;
import org.eclipse.jpt.jpa.core.context.ManagedType;

/**
 * Interface used by persistence unit to gather up managed types.
 * <p>
 * Provisional API: This interface is part of an interim API that is still
 * under development and expected to change significantly before reaching
 * stability. It is available at this early stage to solicit feedback from
 * pioneering adopters on the understanding that any code that uses this API
 * will almost certainly be broken (repeatedly) as the API evolves.
 * 
 * @version 3.3
 * @since 3.3
 */
public interface ManagedTypeContainer {

	/**
	 * Return the container's managed types. The returned list may contain
	 * multiple managed types for the same Java class; e.g.<ul>
	 * <li>the same type is specified in both the <code>persistence.xml</code> and
	 *     <code>orm.xml</code> files
	 * <Li>the same type is specified multiple times in the same
	 *     <code>persistence.xml</code> or <code>orm.xml</code> file
	 * <li>the same type is in a jar file specified in the
	 *     <code>persistence.xml</code> file and is specified in the
	 *     <code>persistence.xml</code> file and/or an <code>orm.xml</code> file
	 * </ul>
	 */
	Iterable<? extends ManagedType> getManagedTypes();

	/**
	 * Return the managed type with the specified name.
	 * Return <code>null</code> if the managed type is not found.
	 * If the persistent unit has more than one managed type with the
	 * specified name, return the first one found, using the following
	 * search order:<ul>
	 * <li>mapping files
	 * <li>classes
	 * <li>jar files
	 * </ul>
	 */
	ManagedType getManagedType(String typeName);


	Transformer<ManagedTypeContainer, Iterable<? extends ManagedType>> TRANSFORMER =
		new Transformer<ManagedTypeContainer, Iterable<? extends ManagedType>>() {
			public Iterable<? extends ManagedType> transform(ManagedTypeContainer container) {
				return container.getManagedTypes();
			}
			@Override
			public String toString() {
				return "ManagedTypeContainer.TRANSFORMER"; //$NON-NLS-1$
			}
		};
}
