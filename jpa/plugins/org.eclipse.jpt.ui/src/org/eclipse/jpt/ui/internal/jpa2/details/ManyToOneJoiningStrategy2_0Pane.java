/*******************************************************************************
 * Copyright (c) 2010 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 * 
 * Contributors:
 *     Oracle - initial API and implementation
 *******************************************************************************/
package org.eclipse.jpt.ui.internal.jpa2.details;

import org.eclipse.jpt.core.context.ManyToOneRelationshipReference;
import org.eclipse.jpt.core.jpa2.context.ManyToOneRelationshipReference2_0;
import org.eclipse.jpt.ui.internal.details.JoinColumnJoiningStrategyPane;
import org.eclipse.jpt.ui.internal.details.JoinTableJoiningStrategyPane;
import org.eclipse.jpt.ui.internal.details.JptUiDetailsMessages;
import org.eclipse.jpt.ui.internal.widgets.Pane;
import org.eclipse.jpt.utility.internal.model.value.SimplePropertyValueModel;
import org.eclipse.jpt.utility.model.value.PropertyValueModel;
import org.eclipse.swt.widgets.Composite;

/**
 * Here is the layout of this pane:  
 * <pre>
 * -----------------------------------------------------------------------------
 * | - Joining Strategy ------------------------------------------------------ |
 * | |                                                                       | |
 * | | o JoinColumnStrategyPane ____________________________________________ | |
 * | | |                                                                   | | |
 * | | |                                                                   | | |
 * | | --------------------------------------------------------------------- | |
 * | | o JoinTableJoiningStrategyPane_______________________________________ | |
 * | | |                                                                   | | |
 * | | |                                                                   | | |
 * | | --------------------------------------------------------------------- | |
 * | ------------------------------------------------------------------------- |
 * -----------------------------------------------------------------------------</pre>
 *
 * @see {@link ManyToOneMapping}
 * @see {@link ManyToOneRelationshipReference}
 * @see {@link ManyToOneMappingComposite}
 * @see {@link JoinColumnStrategyPane}
 *
 * @version 2.1
 * @since 2.1
 */
public class ManyToOneJoiningStrategy2_0Pane extends Pane<ManyToOneRelationshipReference2_0>
{
	public ManyToOneJoiningStrategy2_0Pane(
			Pane<?> parentPane, 
			PropertyValueModel<? extends ManyToOneRelationshipReference2_0> subjectHolder, 
			Composite parent) {
		
		super(parentPane, subjectHolder, parent);
	}
	
	
	@Override
	protected void initializeLayout(Composite container) {
		Composite composite = addCollapsibleSection(
				container,
				JptUiDetailsMessages.Joining_title,
				new SimplePropertyValueModel<Boolean>(Boolean.TRUE));
		
		JoinColumnJoiningStrategyPane.
			buildJoinColumnJoiningStrategyPaneWithIncludeOverrideCheckBox(this, composite);
		
		new JoinTableJoiningStrategyPane(this, composite);

		addSubPane(composite, 5);
	}
}
