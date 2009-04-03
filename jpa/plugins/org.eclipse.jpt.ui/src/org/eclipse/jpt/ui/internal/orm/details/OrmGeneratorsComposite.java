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

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.window.Window;
import org.eclipse.jpt.core.context.Generator;
import org.eclipse.jpt.core.context.orm.EntityMappings;
import org.eclipse.jpt.core.context.orm.OrmGenerator;
import org.eclipse.jpt.core.context.orm.OrmSequenceGenerator;
import org.eclipse.jpt.core.context.orm.OrmTableGenerator;
import org.eclipse.jpt.ui.internal.orm.JptUiOrmMessages;
import org.eclipse.jpt.ui.internal.util.ControlSwitcher;
import org.eclipse.jpt.ui.internal.util.PaneEnabler;
import org.eclipse.jpt.ui.internal.widgets.AddRemoveListPane;
import org.eclipse.jpt.ui.internal.widgets.Pane;
import org.eclipse.jpt.ui.internal.widgets.AddRemovePane.Adapter;
import org.eclipse.jpt.utility.internal.CollectionTools;
import org.eclipse.jpt.utility.internal.Transformer;
import org.eclipse.jpt.utility.internal.model.value.CompositeListValueModel;
import org.eclipse.jpt.utility.internal.model.value.ItemPropertyListValueModelAdapter;
import org.eclipse.jpt.utility.internal.model.value.ListAspectAdapter;
import org.eclipse.jpt.utility.internal.model.value.SimplePropertyValueModel;
import org.eclipse.jpt.utility.internal.model.value.TransformationPropertyValueModel;
import org.eclipse.jpt.utility.internal.model.value.swing.ObjectListSelectionModel;
import org.eclipse.jpt.utility.model.value.ListValueModel;
import org.eclipse.jpt.utility.model.value.PropertyValueModel;
import org.eclipse.jpt.utility.model.value.WritablePropertyValueModel;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.part.PageBook;

/**
 * This pane shows the list of named queries and named native queries.
 * <p>
 * Here the layout of this pane:
 * <pre>
 * -----------------------------------------------------------------------------
 * | ------------------------------------------------------------------------- |
 * | |                                                                       | |
 * | | AddRemoveListPane                                                     | |
 * | |                                                                       | |
 * | ------------------------------------------------------------------------- |
 * | ------------------------------------------------------------------------- |
 * | |                                                                       | |
 * | | OrmSequenceGeneratorComposite or OrmTableGeneratorComposite           | |
 * | |                                                                       | |
 * | ------------------------------------------------------------------------- |
 * -----------------------------------------------------------------------------</pre>
 *
 * @see EntityMappings
 * @see OrmGenerator
 * @see OrmSequenceGenerator
 * @see OrmTableGenerator
 * @see EntityComposite - The parent container
 * @see OrmSequenceGeneratorComposite
 * @see OrmTableGeneratorComposite
 *
 * @version 2.0
 * @since 2.0
 */
public class OrmGeneratorsComposite extends Pane<EntityMappings>
{
	private WritablePropertyValueModel<OrmGenerator> generatorHolder;
	private OrmSequenceGeneratorComposite sequenceGeneratorPane;
	private OrmTableGeneratorComposite tableGeneratorPane;
	private AddRemoveListPane<EntityMappings> listPane;

	/**
	 * Creates a new <code>OrmGeneratorsComposite</code>.
	 *
	 * @param parentPane The parent container of this one
	 * @param parent The parent container
	 */
	public OrmGeneratorsComposite(Pane<? extends EntityMappings> parentPane,
	                              Composite parent) {

		super(parentPane, parent, false);
	}
	
	private void addGenerator(ObjectListSelectionModel listSelectionModel) {
		addGeneratorFromDialog(listSelectionModel, buildAddGeneratorDialog());
	}
	
	protected AddGeneratorDialog buildAddGeneratorDialog() {
		return new AddGeneratorDialog(getControl().getShell());
	}

	protected void addGeneratorFromDialog(ObjectListSelectionModel listSelectionModel, AddGeneratorDialog dialog) {
		if (dialog.open() != Window.OK) {
			return;
		}
		String generatorType = dialog.getGeneratorType();
		OrmGenerator generator;
		if (generatorType == Generator.TABLE_GENERATOR) {
			generator = this.getSubject().addTableGenerator(getSubject().tableGeneratorsSize());
		}
		else if (generatorType == Generator.SEQUENCE_GENERATOR) {
			generator = this.getSubject().addSequenceGenerator(getSubject().sequenceGeneratorsSize());
		}
		else {
			throw new IllegalArgumentException();
		}
		generator.setName(dialog.getName());
		this.generatorHolder.setValue(generator);//so that it gets selected in the List for the user to edit
		listSelectionModel.setSelectedValue(generator);
	}

	private ListValueModel<OrmGenerator> buildDisplayableGeneratorListHolder() {
		return new ItemPropertyListValueModelAdapter<OrmGenerator>(
			buildGeneratorsListHolder(),
			Generator.NAME_PROPERTY
		);
	}

	private Adapter buildGeneratorAdapter() {

		return new AddRemoveListPane.AbstractAdapter() {

			public void addNewItem(ObjectListSelectionModel listSelectionModel) {
				addGenerator(listSelectionModel);
			}


			public void removeSelectedItems(ObjectListSelectionModel listSelectionModel) {
				for (Object item : listSelectionModel.selectedValues()) {
					if (item instanceof OrmSequenceGenerator) {
						getSubject().removeSequenceGenerator((OrmSequenceGenerator) item);
					}
					else {
						getSubject().removeTableGenerator((OrmTableGenerator) item);
					}
				}
			}
		};
	}

	private WritablePropertyValueModel<OrmGenerator> buildGeneratorHolder() {
		return new SimplePropertyValueModel<OrmGenerator>();
	}

	private ILabelProvider buildGeneratorLabelProvider() {
		return new LabelProvider() {
			@Override
			public String getText(Object element) {
				OrmGenerator generator = (OrmGenerator) element;
				String name = generator.getName();

				if (name == null) {
					int index = -1;

					if (generator instanceof OrmSequenceGenerator) {
						index = CollectionTools.indexOf(getSubject().sequenceGenerators(), generator);
					}
					else {
						index = CollectionTools.indexOf(getSubject().tableGenerators(), generator);
					}

					name = NLS.bind(JptUiOrmMessages.OrmGeneratorsComposite_displayString, index);
				}

				return name;
			}
		};
	}

	private ListValueModel<OrmGenerator> buildGeneratorsListHolder() {
		List<ListValueModel<? extends OrmGenerator>> list = new ArrayList<ListValueModel<? extends OrmGenerator>>();
		list.add(buildSequenceGeneratorListHolder());
		list.add(buildTableGeneratorListHolder());
		return new CompositeListValueModel<ListValueModel<? extends OrmGenerator>, OrmGenerator>(list);
	}

	private PropertyValueModel<Boolean> buildPaneEnablerHolder() {
		return new TransformationPropertyValueModel<EntityMappings, Boolean>(getSubjectHolder()) {
			@Override
			protected Boolean transform(EntityMappings value) {
				return (value != null);
			}
		};
	}

	private Transformer<OrmGenerator, Control> buildPaneTransformer() {
		return new Transformer<OrmGenerator, Control>() {
			public Control transform(OrmGenerator generator) {

				if (generator == null) {
					return null;
				}

				if (generator instanceof OrmSequenceGenerator) {
					return sequenceGeneratorPane.getControl();
				}

				return tableGeneratorPane.getControl();
			}
		};
	}

	private PropertyValueModel<OrmSequenceGenerator> buildSequenceGeneratorHolder() {
		return new TransformationPropertyValueModel<OrmGenerator, OrmSequenceGenerator>(this.generatorHolder) {
			@Override
			protected OrmSequenceGenerator transform_(OrmGenerator value) {
				return (value instanceof OrmSequenceGenerator) ? (OrmSequenceGenerator) value : null;
			}
		};
	}

	private ListValueModel<OrmSequenceGenerator> buildSequenceGeneratorListHolder() {
		return new ListAspectAdapter<EntityMappings, OrmSequenceGenerator>(
			getSubjectHolder(),
			EntityMappings.SEQUENCE_GENERATORS_LIST)
		{
			@Override
			protected ListIterator<OrmSequenceGenerator> listIterator_() {
				return this.subject.sequenceGenerators();
			}

			@Override
			protected int size_() {
				return this.subject.sequenceGeneratorsSize();
			}
		};
	}

	private PropertyValueModel<OrmTableGenerator> buildTableGeneratorHolder() {
		return new TransformationPropertyValueModel<OrmGenerator, OrmTableGenerator>(generatorHolder) {
			@Override
			protected OrmTableGenerator transform_(OrmGenerator value) {
				return (value instanceof OrmTableGenerator) ? (OrmTableGenerator) value : null;
			}
		};
	}

	private ListValueModel<OrmTableGenerator> buildTableGeneratorListHolder() {
		return new ListAspectAdapter<EntityMappings, OrmTableGenerator>(
			getSubjectHolder(),
			EntityMappings.TABLE_GENERATORS_LIST)
		{
			@Override
			protected ListIterator<OrmTableGenerator> listIterator_() {
				return this.subject.tableGenerators();
			}

			@Override
			protected int size_() {
				return this.subject.tableGeneratorsSize();
			}
		};
	}


	@Override
	protected void initialize() {
		super.initialize();
		this.generatorHolder = buildGeneratorHolder();
	}

	@Override
	protected void initializeLayout(Composite container) {

		container = addCollapsableSection(
			container,
			JptUiOrmMessages.OrmGeneratorsComposite_groupBox
		);

		// List pane
		this.listPane = addListPane(container);
		installPaneEnabler();

		// Property pane
		PropertyValueModel<OrmSequenceGenerator> sequenceGeneratorHolder =
			buildSequenceGeneratorHolder();
		PropertyValueModel<OrmTableGenerator> tableGeneratorHolder =
			buildTableGeneratorHolder();

		PageBook pageBook = new PageBook(container, SWT.NULL);
		pageBook.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		// Sequence Generator property pane
		this.sequenceGeneratorPane = new OrmSequenceGeneratorComposite(
			this,
			sequenceGeneratorHolder,
			pageBook
		);

		// Table Generator property pane
		this.tableGeneratorPane = new OrmTableGeneratorComposite(
			this,
			tableGeneratorHolder,
			pageBook
		);

		addAlignRight(this.sequenceGeneratorPane);
		addAlignRight(this.tableGeneratorPane);
		installPaneSwitcher(pageBook);
	}

	private AddRemoveListPane<EntityMappings> addListPane(Composite container) {

		return new AddRemoveListPane<EntityMappings>(
			this,
			container,
			buildGeneratorAdapter(),
			buildDisplayableGeneratorListHolder(),
			this.generatorHolder,
			buildGeneratorLabelProvider()
		);
	}

	private void installPaneEnabler() {
		new PaneEnabler(
			buildPaneEnablerHolder(),
			this.listPane
		);
	}

	private void installPaneSwitcher(PageBook pageBook) {
		new ControlSwitcher(this.generatorHolder, buildPaneTransformer(), pageBook);
	}
}
