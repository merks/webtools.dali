/*******************************************************************************
 * Copyright (c) 2009 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 *
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.ui.internal.orm.details;

import org.eclipse.jpt.core.context.orm.OrmTypeMapping;
import org.eclipse.jpt.ui.internal.orm.JptUiOrmMessages;
import org.eclipse.jpt.ui.internal.widgets.FormPane;
import org.eclipse.jpt.utility.internal.model.value.PropertyAspectAdapter;
import org.eclipse.jpt.utility.internal.model.value.TransformationPropertyValueModel;
import org.eclipse.jpt.utility.model.value.PropertyValueModel;
import org.eclipse.jpt.utility.model.value.WritablePropertyValueModel;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.widgets.Composite;

public class MetadataCompleteComposite extends FormPane<OrmTypeMapping> {

	public MetadataCompleteComposite(FormPane<?> parentPane,
	                           PropertyValueModel<? extends OrmTypeMapping> subjectHolder,
	                           Composite parent) {

		super(parentPane, subjectHolder, parent);
	}

	@Override
	protected void initializeLayout(Composite container) {

		addTriStateCheckBoxWithDefault(
			container,
			JptUiOrmMessages.MetadataCompleteComposite_metadataComplete,
			buildMetadataCompleteHolder(),
			buildMetadataCompleteStringHolder(),
			null
		);
	}
	
	private WritablePropertyValueModel<Boolean> buildMetadataCompleteHolder() {
		return new PropertyAspectAdapter<OrmTypeMapping, Boolean>(
			getSubjectHolder(),
			OrmTypeMapping.SPECIFIED_METADATA_COMPLETE_PROPERTY)
		{
			@Override
			protected Boolean buildValue_() {
				return this.subject.getSpecifiedMetadataComplete();
			}

			@Override
			protected void setValue_(Boolean value) {
				this.subject.setSpecifiedMetadataComplete(value);
			}
		};
	}

	private PropertyValueModel<String> buildMetadataCompleteStringHolder() {
		return new TransformationPropertyValueModel<Boolean, String>(buildDefaultMetadataCompleteHolder()) {
			@Override
			protected String transform(Boolean value) {
				if (value != null) {
					String defaultStringValue = value.booleanValue() ? JptUiOrmMessages.Boolean_True : JptUiOrmMessages.Boolean_False;
					return NLS.bind(JptUiOrmMessages.MetadataCompleteComposite_metadataCompleteWithDefault, defaultStringValue);
				}
				return JptUiOrmMessages.MetadataCompleteComposite_metadataComplete;
			}
		};
	}
	private PropertyValueModel<Boolean> buildDefaultMetadataCompleteHolder() {
		return new PropertyAspectAdapter<OrmTypeMapping, Boolean>(
			getSubjectHolder(),
			OrmTypeMapping.SPECIFIED_METADATA_COMPLETE_PROPERTY,
			OrmTypeMapping.DEFAULT_METADATA_COMPLETE_PROPERTY)
		{
			@Override
			protected Boolean buildValue_() {
				if (this.subject.getSpecifiedMetadataComplete() != null) {
					return null;
				}
				return Boolean.valueOf(this.subject.isDefaultMetadataComplete());
			}
		};
	}
}