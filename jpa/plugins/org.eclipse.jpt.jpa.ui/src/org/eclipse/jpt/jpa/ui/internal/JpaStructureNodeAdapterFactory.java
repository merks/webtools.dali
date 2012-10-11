/*******************************************************************************
 * Copyright (c) 2012 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 * 
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.jpa.ui.internal;

import org.eclipse.core.runtime.IAdapterFactory;
import org.eclipse.jpt.common.ui.WidgetFactory;
import org.eclipse.jpt.common.utility.internal.ObjectTools;
import org.eclipse.jpt.jpa.core.JpaStructureNode;
import org.eclipse.jpt.jpa.ui.JpaPlatformUi;
import org.eclipse.jpt.jpa.ui.details.JpaDetailsPageManager;
import org.eclipse.swt.widgets.Composite;

/**
 * Factory to build adapters for a {@link JpaStructureNode}:<ul>
 * <li>{@link org.eclipse.jpt.jpa.ui.details.JpaDetailsPageManager.Factory}
 * </ul>
 * See <code>org.eclipse.jpt.jpa.ui/plugin.xml:org.eclipse.core.runtime.adapters</code>.
 */
public class JpaStructureNodeAdapterFactory
	implements IAdapterFactory
{
	private static final Class<?>[] ADAPTER_LIST = new Class[] { JpaDetailsPageManager.Factory.class };

	public Class<?>[] getAdapterList() {
		return ADAPTER_LIST;
	}

	public Object getAdapter(Object adaptableObject, @SuppressWarnings("rawtypes") Class adapterType) {
		if (adaptableObject instanceof JpaStructureNode) {
			return this.getAdapter((JpaStructureNode) adaptableObject, adapterType);
		}
		return null;
	}

	private Object getAdapter(JpaStructureNode jpaStructureNode, Class<?> adapterType) {
		if (adapterType == JpaDetailsPageManager.Factory.class) {
			return this.getJpaDetailsPageManagerFactory(jpaStructureNode);
		}
		return null;
	}

	private JpaDetailsPageManager.Factory getJpaDetailsPageManagerFactory(JpaStructureNode jpaStructureNode) {
		return new JpaDetailsPageManagerFactory(jpaStructureNode);
	}

	/* CU private */ static class JpaDetailsPageManagerFactory
		implements JpaDetailsPageManager.Factory
	{
		private final JpaStructureNode jpaStructureNode;

		JpaDetailsPageManagerFactory(JpaStructureNode jpaStructureNode) {
			super();
			this.jpaStructureNode = jpaStructureNode;
		}

		@SuppressWarnings("unchecked")
		public <T extends JpaStructureNode> JpaDetailsPageManager<T> buildPageManager(Composite parent, WidgetFactory widgetFactory) {
			return (JpaDetailsPageManager<T>) this.getJpaPlatformUi().buildJpaDetailsPageManager(parent, this.jpaStructureNode, widgetFactory);
		}

		private JpaPlatformUi getJpaPlatformUi() {
			return (JpaPlatformUi) this.jpaStructureNode.getJpaPlatform().getAdapter(JpaPlatformUi.class);
		}
		@Override
		public String toString() {
			return ObjectTools.toString(this, this.jpaStructureNode);
		}
	}
}
