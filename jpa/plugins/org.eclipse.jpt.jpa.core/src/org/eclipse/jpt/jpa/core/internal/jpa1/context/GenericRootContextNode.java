/*******************************************************************************
 * Copyright (c) 2007, 2012 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 *
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.jpa.core.internal.jpa1.context;

import java.util.List;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jpt.common.core.JptResourceType;
import org.eclipse.jpt.common.core.resource.java.JavaResourceAbstractType;
import org.eclipse.jpt.common.core.resource.java.JavaResourceCompilationUnit;
import org.eclipse.jpt.common.utility.internal.CollectionTools;
import org.eclipse.jpt.common.utility.internal.HashBag;
import org.eclipse.jpt.jpa.core.JpaProject;
import org.eclipse.jpt.jpa.core.JptJpaCorePlugin;
import org.eclipse.jpt.jpa.core.context.MappingFileRoot;
import org.eclipse.jpt.jpa.core.context.persistence.Persistence;
import org.eclipse.jpt.jpa.core.context.persistence.PersistenceUnit;
import org.eclipse.jpt.jpa.core.context.persistence.PersistenceXml;
import org.eclipse.jpt.jpa.core.internal.context.AbstractJpaContextNode;
import org.eclipse.jpt.jpa.core.internal.validation.DefaultJpaValidationMessages;
import org.eclipse.jpt.jpa.core.internal.validation.JpaValidationMessages;
import org.eclipse.jpt.jpa.core.jpa2.MetamodelSynchronizer;
import org.eclipse.jpt.jpa.core.jpa2.context.JpaRootContextNode2_0;
import org.eclipse.jpt.jpa.core.jpa2.context.persistence.PersistenceXml2_0;
import org.eclipse.jpt.jpa.core.resource.xml.JpaXmlResource;
import org.eclipse.jst.j2ee.model.internal.validation.ValidationCancelledException;
import org.eclipse.wst.validation.internal.provisional.core.IMessage;
import org.eclipse.wst.validation.internal.provisional.core.IReporter;

/**
 * the context model root
 */
public class GenericRootContextNode
	extends AbstractJpaContextNode
	implements JpaRootContextNode2_0
{
	/* This object has no parent, so it must point to the JPA project explicitly. */
	protected final JpaProject jpaProject;

	/* Main context object. */
	protected PersistenceXml persistenceXml;


	public GenericRootContextNode(JpaProject jpaProject) {
		super(null);  // the JPA project is not really a "parent"...

		if (jpaProject == null) {
			throw new NullPointerException();
		}
		this.jpaProject = jpaProject;
		this.persistenceXml = this.buildPersistenceXml();
	}


	// ********** synchronize/update **********

	@Override
	public void synchronizeWithResourceModel() {
		super.synchronizeWithResourceModel();
		this.syncPersistenceXml();
	}

	@Override
	public void update() {
		super.update();
		this.updatePersistenceXml();
	}


	// ********** persistence.xml **********

	public PersistenceXml getPersistenceXml() {
		return this.persistenceXml;
	}

	protected void setPersistenceXml(PersistenceXml persistenceXml) {
		PersistenceXml old = this.persistenceXml;
		this.persistenceXml = persistenceXml;
		this.firePropertyChanged(PERSISTENCE_XML_PROPERTY, old, persistenceXml);
	}

	protected PersistenceXml buildPersistenceXml() {
		JpaXmlResource xmlResource = this.resolvePersistenceXmlResource();
		return (xmlResource == null) ? null : this.buildPersistenceXml(xmlResource);
	}

	protected void syncPersistenceXml() {
		this.syncPersistenceXml(true);
	}

	/**
	 * We call this method from both {@link #syncPersistenceXml()} and
	 * {@link #updatePersistenceXml()} because<ul>
	 * <li>a <em>sync</em> will occur when the file is edited directly;
	 *     and the user could modify something (e.g. the version number) that
	 *     triggers the file being "resolved" or not;
	 *     see {@link #resolvePersistenceXmlResource()}
	 * <li>an <em>update</em> will occur whenever the entire file is added or
	 *     removed
	 * </ul>
	 */
	protected void syncPersistenceXml(boolean sync) {
		JpaXmlResource xmlResource = this.resolvePersistenceXmlResource();
		if (xmlResource == null) {
			if (this.persistenceXml != null) {
				this.persistenceXml.dispose();
				this.setPersistenceXml(null);
			}
		} else {
			if (this.persistenceXml == null) {
				this.setPersistenceXml(this.buildPersistenceXml(xmlResource));
			} else {
				if (sync) {
					this.persistenceXml.synchronizeWithResourceModel();
				} else {
					this.persistenceXml.update();
				}
			}
		}
	}

	protected JpaXmlResource resolvePersistenceXmlResource() {
		JpaXmlResource xmlResource = this.jpaProject.getPersistenceXmlResource();
		if (xmlResource == null) {
			return null;
		}
		if (xmlResource.isReverting()) {
			// 308254 - this can happen when persistence.xml is closed without saving;
			// the model is completely whacked in another thread - so wipe our model(?)
			return null;
		}
		JptResourceType resourceType = xmlResource.getResourceType();
		if (resourceType == null) {
			return null;
		}
		if ( ! this.getJpaPlatform().supportsResourceType(resourceType)) {
			return null;
		}
		return xmlResource;
	}

	/**
	 * pre-condition: 'xmlResource' is not <code>null</code>
	 */
	protected PersistenceXml buildPersistenceXml(JpaXmlResource xmlResource) {
		return this.getJpaFactory().buildPersistenceXml(this, xmlResource);
	}

	protected void updatePersistenceXml() {
		this.syncPersistenceXml(false);
	}


	// ********** misc **********

	@Override
	protected boolean requiresParent() {
		return false;
	}

	@Override
	public void stateChanged() {
		super.stateChanged();
		// forward to JPA project
		this.jpaProject.stateChanged();
	}

	@Override
	public JpaProject getJpaProject() {
		return this.jpaProject;
	}

	@Override
	public IResource getResource() {
		return this.getProject();
	}

	protected IProject getProject() {
		return this.jpaProject.getProject();
	}

	@Override
	public PersistenceUnit getPersistenceUnit() {
		return null;
	}

	@Override
	public MappingFileRoot getMappingFileRoot() {
		return null;
	}

	@Override
	public void toString(StringBuilder sb) {
		sb.append(this.jpaProject.getName());
	}


	// ********** metamodel **********

	public void initializeMetamodel() {
		if (this.persistenceXml != null) {
			((PersistenceXml2_0) this.persistenceXml).initializeMetamodel();
		}
	}

	public IStatus synchronizeMetamodel(IProgressMonitor monitor) {
		return (this.persistenceXml != null) ?
				((PersistenceXml2_0) this.persistenceXml).synchronizeMetamodel(monitor) :
				Status.OK_STATUS;
	}

	public void disposeMetamodel() {
		if (this.persistenceXml != null) {
			((PersistenceXml2_0) this.persistenceXml).disposeMetamodel();
		}
	}


	// ********** validation **********

	public void validate(List<IMessage> messages, IReporter reporter) {
		if (reporter.isCancelled()) {
			throw new ValidationCancelledException();
		}

		if (this.persistenceXml == null) {
			messages.add(buildPersistenceXmlValidationMessage());
			return;
		}
		if ( ! this.jpaProject.discoversAnnotatedClasses()) {
			this.validateOrphanClasses(messages);
		}
		this.persistenceXml.validate(messages, reporter);
	}

	protected IMessage buildPersistenceXmlValidationMessage() {
		int severity = IMessage.HIGH_SEVERITY;
		IFile file = getPlatformFile();
		if (file != null && file.exists()) {
			JpaXmlResource xmlResource = this.jpaProject.getPersistenceXmlResource();
			if (xmlResource != null 
					&& ! getJpaPlatform().supportsResourceType(xmlResource.getResourceType())) {
				return DefaultJpaValidationMessages.buildMessage(
					severity,
					JpaValidationMessages.PERSISTENCE_XML_UNSUPPORTED_CONTENT,
					file);
			}
			return DefaultJpaValidationMessages.buildMessage(
				severity,
				JpaValidationMessages.PERSISTENCE_XML_INVALID_CONTENT,
				file);
		}
		return DefaultJpaValidationMessages.buildMessage(
			severity,
			JpaValidationMessages.PROJECT_NO_PERSISTENCE_XML,
			this);
	}

	protected IFile getPlatformFile() {
		return this.jpaProject.getPlatformFile(JptJpaCorePlugin.DEFAULT_PERSISTENCE_XML_RUNTIME_PATH);
	}

	protected void validateOrphanClasses(List<IMessage> messages) {
		Persistence persistence = this.persistenceXml.getPersistence();
		if (persistence == null) {
			return;  // handled with other validation
		}
		if (persistence.getPersistenceUnitsSize() != 1) {
			return;  // the context model currently only supports 1 persistence unit
		}

		PersistenceUnit persistenceUnit = persistence.getPersistenceUnits().iterator().next();
		HashBag<String> annotatedClassNames = CollectionTools.bag(this.jpaProject.getAnnotatedJavaSourceClassNames());
		HashBag<String> orphans = annotatedClassNames.clone();
		for (String annotatedClassName : annotatedClassNames) {
			if (persistenceUnit.specifiesPersistentType(annotatedClassName)) {
				orphans.remove(annotatedClassName);
			}
			else if (MetamodelSynchronizer.MetamodelTools.isMetamodel(getJpaProject().getJavaResourceType(annotatedClassName))) {
				orphans.remove(annotatedClassName);
			}
		}

		// TODO remove 'jrcu'
		// replace jrcu.getFile() with jrpt.getFile()
		// replace jrpt.getMappingAnnotation().getTextRange(jrcu.buildASTRoot())
		//    with jrpt.getMappingAnnotation().getTextRange()
		//    (new method #getTextRange() ?)
		Iterable<String> typeMappingAnnotationNames = this.jpaProject.getTypeMappingAnnotationNames();
		for (String orphan : orphans) {
			JavaResourceAbstractType jrt = this.jpaProject.getJavaResourceType(orphan);
			JavaResourceCompilationUnit jrcu = jrt.getJavaResourceCompilationUnit();
			if (jrt.isAnnotatedWithAnyOf(typeMappingAnnotationNames)) {
				messages.add(
					DefaultJpaValidationMessages.buildMessage(
						IMessage.HIGH_SEVERITY,
						JpaValidationMessages.PERSISTENT_TYPE_MAPPED_BUT_NOT_INCLUDED_IN_PERSISTENCE_UNIT,
						new String[] {jrt.getTypeBinding().getQualifiedName()},
						jrt.getFile(),
						jrt.getNameTextRange(jrcu.buildASTRoot())
					)
				);
			}
			else {
				messages.add(
					DefaultJpaValidationMessages.buildMessage(
						IMessage.NORMAL_SEVERITY,
						JpaValidationMessages.PERSISTENT_TYPE_ANNOTATED_BUT_NOT_INCLUDED_IN_PERSISTENCE_UNIT,
						new String[] {jrt.getName()},
						jrt.getFile(),
						jrt.getNameTextRange(jrcu.buildASTRoot())
					)
				);
			}
		}
	}
}
