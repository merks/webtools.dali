/*******************************************************************************
 * Copyright (c) 2008 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 *
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.ui.internal.orm.details;

import org.eclipse.jpt.core.context.orm.EntityMappings;
import org.eclipse.jpt.ui.internal.mappings.details.QueriesComposite;
import org.eclipse.jpt.ui.internal.orm.JptUiOrmMessages;
import org.eclipse.jpt.ui.internal.util.PaneEnabler;
import org.eclipse.jpt.ui.internal.widgets.AbstractPane;
import org.eclipse.jpt.utility.internal.model.value.TransformationPropertyValueModel;
import org.eclipse.jpt.utility.model.value.PropertyValueModel;
import org.eclipse.swt.widgets.Composite;

/**
 * Here the layout of this pane:
 * <pre>
 * -----------------------------------------------------------------------------
 * | ------------------------------------------------------------------------- |
 * | |                                                                       | |
 * | | QueriesComposite                                                      | |
 * | |                                                                       | |
 * | ------------------------------------------------------------------------- |
 * -----------------------------------------------------------------------------</pre>
 *
 * @see EntityMappings
 * @see EntityMappingsDetailsPage - The parent container
 * @see QueriesComposite
 *
 * @version 2.0
 * @since 2.0
 */
public class OrmQueriesComposite extends AbstractPane<EntityMappings> {

	/**
	 * Creates a new <code>OrmQueriesComposite</code>.
	 *
	 * @param parentPane The parent container of this one
	 * @param parent The parent container
	 */
	public OrmQueriesComposite(AbstractPane<? extends EntityMappings> parentPane,
	                           Composite parent) {

		super(parentPane, parent);
	}

	private PropertyValueModel<Boolean> buildPaneEnablerHolder() {
		return new TransformationPropertyValueModel<EntityMappings, Boolean>(getSubjectHolder()) {
			@Override
			protected Boolean transform(EntityMappings value) {
				return (value != null);
			}
		};
	}

	/*
	 * (non-Javadoc)
	 */
	@Override
	protected void initializeLayout(Composite container) {

		container = buildCollapsableSection(
			container,
			JptUiOrmMessages.OrmQueriesComposite_groupBox
		);

		QueriesComposite queriesComposite = new QueriesComposite(
			this,
			container
		);

		installPaneEnabler(queriesComposite);
	}

	private void installPaneEnabler(QueriesComposite queriesComposite) {
		new PaneEnabler(
			buildPaneEnablerHolder(),
			queriesComposite
		);
	}
}
