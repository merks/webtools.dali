/*******************************************************************************
 * Copyright (c) 2008, 2013 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 * 
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.jpa.ui.internal.platform.generic;

import org.eclipse.jpt.common.ui.jface.ItemTreeContentProvider;
import org.eclipse.jpt.jpa.ui.internal.platform.base.AbstractNavigatorItemContentProviderFactory;

public class GenericNavigatorItemContentProviderFactory
	extends AbstractNavigatorItemContentProviderFactory
{
	// singleton
	private static final ItemTreeContentProvider.Factory INSTANCE = new GenericNavigatorItemContentProviderFactory();

	/**
	 * Return the singleton
	 */
	public static ItemTreeContentProvider.Factory instance() {
		return INSTANCE;
	}


	private GenericNavigatorItemContentProviderFactory() {
		super();
	}
}
