/*******************************************************************************
 * Copyright (c) 2008, 2012 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 *
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.jpa.ui.internal.persistence;

import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.search.IJavaSearchScope;
import org.eclipse.jdt.core.search.SearchEngine;
import org.eclipse.jdt.ui.IJavaElementSearchConstants;
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.window.Window;
import org.eclipse.jpt.common.ui.internal.JptCommonUiMessages;
import org.eclipse.jpt.common.ui.internal.widgets.AddRemoveListPane;
import org.eclipse.jpt.common.ui.internal.widgets.AddRemovePane;
import org.eclipse.jpt.common.ui.internal.widgets.AddRemovePane.Adapter;
import org.eclipse.jpt.common.ui.internal.widgets.Pane;
import org.eclipse.jpt.common.utility.internal.model.value.CollectionPropertyValueModelAdapter;
import org.eclipse.jpt.common.utility.internal.model.value.ItemPropertyListValueModelAdapter;
import org.eclipse.jpt.common.utility.internal.model.value.ListAspectAdapter;
import org.eclipse.jpt.common.utility.internal.model.value.PropertyAspectAdapter;
import org.eclipse.jpt.common.utility.internal.model.value.SimpleCollectionValueModel;
import org.eclipse.jpt.common.utility.internal.model.value.TransformationPropertyValueModel;
import org.eclipse.jpt.common.utility.iterable.ListIterable;
import org.eclipse.jpt.common.utility.model.value.CollectionValueModel;
import org.eclipse.jpt.common.utility.model.value.ListValueModel;
import org.eclipse.jpt.common.utility.model.value.ModifiableCollectionValueModel;
import org.eclipse.jpt.common.utility.model.value.ModifiablePropertyValueModel;
import org.eclipse.jpt.common.utility.model.value.PropertyValueModel;
import org.eclipse.jpt.jpa.core.context.PersistentType;
import org.eclipse.jpt.jpa.core.context.TypeMapping;
import org.eclipse.jpt.jpa.core.context.java.JavaPersistentType;
import org.eclipse.jpt.jpa.core.context.persistence.ClassRef;
import org.eclipse.jpt.jpa.core.context.persistence.PersistenceUnit;
import org.eclipse.jpt.jpa.ui.JpaPlatformUi;
import org.eclipse.jpt.jpa.ui.details.MappingUiDefinition;
import org.eclipse.jpt.jpa.ui.internal.JpaHelpContextIds;
import org.eclipse.jpt.jpa.ui.internal.JptUiIcons;
import org.eclipse.jpt.jpa.ui.internal.plugin.JptJpaUiPlugin;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.SelectionDialog;
import org.eclipse.ui.progress.IProgressService;

/**
 * Here the layout of this pane:
 * <pre>
 * -----------------------------------------------------------------------------
 * |                                                                           |
 * | Description                                                               |
 * |                                                                           |
 * | ------------------------------------------------------------------------- |
 * | |                                                                       | |
 * | | AddRemoveListPane                                                     | |
 * | |                                                                       | |
 * | ------------------------------------------------------------------------- |
 * |                                                                           |
 * | x Exclude Unlisted Mapped Classes                                         |
 * |                                                                           |
 * -----------------------------------------------------------------------------</pre>
 *
 * @see PersistenceUnit
 * @see PersistenceUnitGeneralTab - The parent container
 * @see AddRemoveListPane
 *
 * @version 2.3
 * @since 2.0
 */
@SuppressWarnings("nls")
public class PersistenceUnitClassesComposite extends Pane<PersistenceUnit>
{
	/**
	 * Creates a new <code>PersistenceUnitMappedClassesComposite</code>.
	 *
	 * @param parentPane The parent pane of this one
	 * @param parent The parent container
	 */
	public PersistenceUnitClassesComposite(Pane<? extends PersistenceUnit> parentPane,
	                                             Composite parent) {

		super(parentPane, parent);
	}

	@Override
	protected void initializeLayout(Composite container) {
		// List pane
		new AddRemoveListPane<PersistenceUnit, ClassRef>(
			this,
			container,
			this.buildAdapter(),
			this.buildItemListHolder(),
			this.buildSelectedItemsModel(),
			this.buildLabelProvider(),
			JpaHelpContextIds.PERSISTENCE_XML_GENERAL
		);

		this.addTriStateCheckBoxWithDefault(
			container,
			JptUiPersistenceMessages.PersistenceUnitClassesComposite_excludeUnlistedMappedClasses,
			buildExcludeUnlistedMappedClassesHolder(),
			buildExcludeUnlistedMappedClassesStringHolder(),
			JpaHelpContextIds.PERSISTENCE_XML_GENERAL
		);
	}


	private ClassRef addMappedClass() {

		IType type = chooseType();

		if (type != null) {
			String className = type.getFullyQualifiedName('$');
			if(classRefExists(className)) {
				return null;
			}
			return getSubject().addSpecifiedClassRef(className);
		}
		return null;
	}
	
	private boolean classRefExists(String className) {
		for (ClassRef classRef : getSubject().getSpecifiedClassRefs()) {
			if( classRef.getClassName().equals(className)) {
				return true;
			}
		}
		return false;
	}

	private Adapter<ClassRef> buildAdapter() {
		return new AddRemovePane.AbstractAdapter<ClassRef>() {
			public ClassRef addNewItem() {
				return addMappedClass();
			}

			@Override
			public PropertyValueModel<Boolean> buildOptionalButtonEnabledModel(CollectionValueModel<ClassRef> selectedItemsModel) {
				return new CollectionPropertyValueModelAdapter<Boolean, ClassRef>(selectedItemsModel) {
					@Override
					protected Boolean buildValue() {
						if (this.collectionModel.size() == 1) {
							ClassRef classRef = this.collectionModel.iterator().next();
							return Boolean.valueOf(findType(classRef) != null);				
						}
						return Boolean.FALSE;
					}
				};
			}

			@Override
			public boolean hasOptionalButton() {
				return true;
			}

			@Override
			public String optionalButtonText() {
				return JptUiPersistenceMessages.PersistenceUnitClassesComposite_open;
			}

			@Override
			public void optionOnSelection(CollectionValueModel<ClassRef> selectedItemsModel) {
				openMappedClass(selectedItemsModel.iterator().next());
			}

			public void removeSelectedItems(CollectionValueModel<ClassRef> selectedItemsModel) {
				getSubject().removeSpecifiedClassRefs(selectedItemsModel);
			}
		};
	}

	private ModifiablePropertyValueModel<Boolean> buildExcludeUnlistedMappedClassesHolder() {
		return new PropertyAspectAdapter<PersistenceUnit, Boolean>(
			getSubjectHolder(),
			PersistenceUnit.SPECIFIED_EXCLUDE_UNLISTED_CLASSES_PROPERTY)
		{
			@Override
			protected Boolean buildValue_() {
				return this.subject.getSpecifiedExcludeUnlistedClasses();
			}

			@Override
			protected void setValue_(Boolean value) {
				this.subject.setSpecifiedExcludeUnlistedClasses(value);
			}
		};
	}

	private PropertyValueModel<String> buildExcludeUnlistedMappedClassesStringHolder() {
		return new TransformationPropertyValueModel<Boolean, String>(buildDefaultExcludeUnlistedMappedClassesHolder()) {
			@Override
			protected String transform(Boolean value) {
				if (value != null) {
					String defaultStringValue = value.booleanValue() ? JptCommonUiMessages.Boolean_True : JptCommonUiMessages.Boolean_False;
					return NLS.bind(JptUiPersistenceMessages.PersistenceUnitClassesComposite_excludeUnlistedMappedClassesWithDefault, defaultStringValue);
				}
				return JptUiPersistenceMessages.PersistenceUnitClassesComposite_excludeUnlistedMappedClasses;
			}
		};
	}
	
	private PropertyValueModel<Boolean> buildDefaultExcludeUnlistedMappedClassesHolder() {
		return new PropertyAspectAdapter<PersistenceUnit, Boolean>(
			getSubjectHolder(),
			PersistenceUnit.SPECIFIED_EXCLUDE_UNLISTED_CLASSES_PROPERTY,
			PersistenceUnit.DEFAULT_EXCLUDE_UNLISTED_CLASSES_PROPERTY)
		{
			@Override
			protected Boolean buildValue_() {
				if (this.subject.getSpecifiedExcludeUnlistedClasses() != null) {
					return null;
				}
				return Boolean.valueOf(this.subject.getDefaultExcludeUnlistedClasses());
			}
		};
	}
	private ILabelProvider buildLabelProvider() {
		return new LabelProvider() {
			@Override
			public Image getImage(Object element) {
				ClassRef classRef = (ClassRef) element;
				JavaPersistentType persistentType = classRef.getJavaPersistentType();
				if (persistentType != null) {
					return this.getImage(persistentType);
				}
				return JptJpaUiPlugin.instance().getImage(JptUiIcons.WARNING);
			}

			private Image getImage(JavaPersistentType persistentType) {
				return this.getTypeMappingUiDefinition(persistentType).getImage();
			}

			private MappingUiDefinition<PersistentType, ? extends TypeMapping> getTypeMappingUiDefinition(JavaPersistentType persistentType) {
				return this.getJpaPlatformUi(persistentType).getTypeMappingUiDefinition(persistentType.getResourceType(), persistentType.getMappingKey());
			}

			private JpaPlatformUi getJpaPlatformUi(JavaPersistentType persistentType) {
				return (JpaPlatformUi) persistentType.getJpaPlatform().getAdapter(JpaPlatformUi.class);
			}

			@Override
			public String getText(Object element) {
				ClassRef classRef = (ClassRef) element;
				String name = classRef.getClassName();

				if (name == null) {
					name = JptUiPersistenceMessages.PersistenceUnitClassesComposite_mappedClassesNoName;
				}

				return name;
			}
		};
	}

	private ListValueModel<ClassRef> buildItemListHolder() {
		return new ItemPropertyListValueModelAdapter<ClassRef>(
			buildListHolder(),
			ClassRef.JAVA_PERSISTENT_TYPE_PROPERTY,
			ClassRef.CLASS_NAME_PROPERTY
		);
	}

	private ListValueModel<ClassRef> buildListHolder() {
		return new ListAspectAdapter<PersistenceUnit, ClassRef>(getSubjectHolder(), PersistenceUnit.SPECIFIED_CLASS_REFS_LIST) {
			@Override
			protected ListIterable<ClassRef> getListIterable() {
				return subject.getSpecifiedClassRefs();
			}

			@Override
			protected int size_() {
				return subject.getSpecifiedClassRefsSize();
			}
		};
	}

	private ModifiableCollectionValueModel<ClassRef> buildSelectedItemsModel() {
		return new SimpleCollectionValueModel<ClassRef>();
	}

	/**
	 * Prompts the user the Open Type dialog.
	 *
	 * @return Either the selected type or <code>null</code> if the user
	 * canceled the dialog
	 */
	private IType chooseType() {
		IJavaProject javaProject = getJavaProject();
		IJavaElement[] elements = new IJavaElement[] { javaProject };
		IJavaSearchScope scope = SearchEngine.createJavaSearchScope(elements);
		IProgressService service = PlatformUI.getWorkbench().getProgressService();
		SelectionDialog typeSelectionDialog;

		try {
			typeSelectionDialog = JavaUI.createTypeDialog(
				getShell(),
				service,
				scope,
				IJavaElementSearchConstants.CONSIDER_CLASSES,
				false,
				""
			);
		}
		catch (JavaModelException e) {
			JptJpaUiPlugin.instance().logError(e);
			return null;
		}

		typeSelectionDialog.setTitle(JptCommonUiMessages.ClassChooserPane_dialogTitle);
		typeSelectionDialog.setMessage(JptCommonUiMessages.ClassChooserPane_dialogMessage);

		if (typeSelectionDialog.open() == Window.OK) {
			return (IType) typeSelectionDialog.getResult()[0];
		}

		return null;
	}

	private IType findType(ClassRef classRef) {
		String className = classRef.getClassName();

		if (className != null) {
			try {
				return getSubject().getJpaProject().getJavaProject().findType(className.replace('$', '.'));
			}
			catch (JavaModelException e) {
				JptJpaUiPlugin.instance().logError(e);
			}
		}

		return null;
	}

	private void openMappedClass(ClassRef classRef) {

		IType type = findType(classRef);

		if (type != null) {
			try {
				IJavaElement javaElement = type.getParent();
				JavaUI.openInEditor(javaElement, true, true);
			}
			catch (PartInitException e) {
				JptJpaUiPlugin.instance().logError(e);
			}
			catch (JavaModelException e) {
				JptJpaUiPlugin.instance().logError(e);
			}
		}
	}

	private IJavaProject getJavaProject() {
		return getSubject().getJpaProject().getJavaProject();
	}
}
