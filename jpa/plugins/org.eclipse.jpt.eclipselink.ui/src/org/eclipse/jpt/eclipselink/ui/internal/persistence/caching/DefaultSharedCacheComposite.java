/*******************************************************************************
 * Copyright (c) 2008, 2009 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 *
 * Contributors:
 *     Oracle - initial API and implementation
 *******************************************************************************/
package org.eclipse.jpt.eclipselink.ui.internal.persistence.caching;

import org.eclipse.jpt.eclipselink.core.internal.context.persistence.caching.Caching;
import org.eclipse.jpt.eclipselink.ui.internal.EclipseLinkHelpContextIds;
import org.eclipse.jpt.eclipselink.ui.internal.EclipseLinkUiMessages;
import org.eclipse.jpt.ui.internal.widgets.FormPane;
import org.eclipse.jpt.utility.internal.model.value.PropertyAspectAdapter;
import org.eclipse.jpt.utility.internal.model.value.TransformationPropertyValueModel;
import org.eclipse.jpt.utility.model.value.PropertyValueModel;
import org.eclipse.jpt.utility.model.value.WritablePropertyValueModel;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.widgets.Composite;

/**
 * DefaultShareCacheComposite
 */
public class DefaultSharedCacheComposite extends FormPane<Caching>
{
	/**
	 * Creates a new <code>DefaultShareCacheComposite</code>.
	 *
	 * @param parentController
	 *            The parent container of this one
	 * @param parent
	 *            The parent container
	 */
	public DefaultSharedCacheComposite(
					FormPane<? extends Caching> parentComposite,
					Composite parent) {

		super(parentComposite, parent);
	}

	@Override
	protected void initializeLayout(Composite container) {

		this.addTriStateCheckBoxWithDefault(
			container,
			EclipseLinkUiMessages.PersistenceXmlCachingTab_sharedCacheDefaultLabel,
			this.buildDefaultSharedCacheHolder(),
			this.buildDefaultSharedCacheStringHolder(),
			EclipseLinkHelpContextIds.PERSISTENCE_CACHING_DEFAULT_SHARED
		);
	}
	
	private WritablePropertyValueModel<Boolean> buildDefaultSharedCacheHolder() {
		return new PropertyAspectAdapter<Caching, Boolean>(getSubjectHolder(), Caching.SHARED_CACHE_DEFAULT_PROPERTY) {
			@Override
			protected Boolean buildValue_() {
				return this.subject.getSharedCacheDefault();
			}

			@Override
			protected void setValue_(Boolean value) {
				this.subject.setSharedCacheDefault(value);
			}
		};
	}

	private PropertyValueModel<String> buildDefaultSharedCacheStringHolder() {
		return new TransformationPropertyValueModel<Boolean, String>(buildDefaultDefaultSharedCacheHolder()) {
			@Override
			protected String transform(Boolean value) {
				if (value != null) {
					String defaultStringValue = value.booleanValue() ? EclipseLinkUiMessages.Boolean_True : EclipseLinkUiMessages.Boolean_False;
					return NLS.bind(EclipseLinkUiMessages.PersistenceXmlCachingTab_defaultSharedCacheDefaultLabel, defaultStringValue);
				}
				return EclipseLinkUiMessages.PersistenceXmlCachingTab_sharedCacheDefaultLabel;
			}
		};
	}
	private PropertyValueModel<Boolean> buildDefaultDefaultSharedCacheHolder() {
		return new PropertyAspectAdapter<Caching, Boolean>(
			getSubjectHolder(),
			Caching.SHARED_CACHE_DEFAULT_PROPERTY)
		{
			@Override
			protected Boolean buildValue_() {
				if (this.subject.getSharedCacheDefault() != null) {
					return null;
				}
				return this.subject.getDefaultSharedCacheDefault();
			}
		};
	}
}
