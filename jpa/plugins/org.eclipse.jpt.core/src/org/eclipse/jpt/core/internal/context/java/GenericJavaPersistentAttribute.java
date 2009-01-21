/*******************************************************************************
 * Copyright (c) 2006, 2009 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 * 
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.core.internal.context.java;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jpt.core.JpaStructureNode;
import org.eclipse.jpt.core.MappingKeys;
import org.eclipse.jpt.core.context.PersistentAttribute;
import org.eclipse.jpt.core.context.java.JavaAttributeMapping;
import org.eclipse.jpt.core.context.java.JavaPersistentAttribute;
import org.eclipse.jpt.core.context.java.JavaPersistentType;
import org.eclipse.jpt.core.context.java.JavaStructureNodes;
import org.eclipse.jpt.core.context.java.JavaTypeMapping;
import org.eclipse.jpt.core.internal.validation.DefaultJpaValidationMessages;
import org.eclipse.jpt.core.internal.validation.JpaValidationMessages;
import org.eclipse.jpt.core.resource.java.Annotation;
import org.eclipse.jpt.core.resource.java.JavaResourceNode;
import org.eclipse.jpt.core.resource.java.JavaResourcePersistentAttribute;
import org.eclipse.jpt.core.utility.TextRange;
import org.eclipse.jpt.utility.Filter;
import org.eclipse.jpt.utility.internal.CollectionTools;
import org.eclipse.wst.validation.internal.provisional.core.IMessage;

/**
 * 
 */
public class GenericJavaPersistentAttribute
	extends AbstractJavaJpaContextNode
	implements JavaPersistentAttribute
{
	protected String name;

	protected JavaAttributeMapping defaultMapping;

	protected JavaAttributeMapping specifiedMapping;

	protected JavaResourcePersistentAttribute resourcePersistentAttribute;


	public GenericJavaPersistentAttribute(JavaPersistentType parent, JavaResourcePersistentAttribute jrpa) {
		super(parent);
		this.initialize(jrpa);
	}
	
	public String getId() {
		return JavaStructureNodes.PERSISTENT_ATTRIBUTE_ID;
	}

	protected void initialize(JavaResourcePersistentAttribute jrpa) {
		this.resourcePersistentAttribute = jrpa;
		this.name = this.name();
		initializeDefaultMapping();
		initializeSpecifiedMapping();
	}
	
	protected void initializeDefaultMapping() {
		this.defaultMapping = buildDefaultMapping();
	}
	
	protected JavaAttributeMapping buildDefaultMapping() {
		JavaAttributeMapping defaultMapping = getJpaPlatform().buildDefaultJavaAttributeMapping(this);
		if (defaultMapping.getAnnotationName() != null) {
			JavaResourceNode resourceMapping = this.resourcePersistentAttribute.getNullMappingAnnotation(defaultMapping.getAnnotationName());
			defaultMapping.initialize(resourceMapping);
		}
		return defaultMapping;
	}
	
	protected void initializeSpecifiedMapping() {
		this.specifiedMapping = buildJavaAttributeMappingFromAnnotation(this.getJavaMappingAnnotationName());
	}
	
	public JavaResourcePersistentAttribute getResourcePersistentAttribute() {
		return this.resourcePersistentAttribute;
	}
	
	public JavaPersistentType getPersistentType() {
		return (JavaPersistentType) this.getParent();
	}

	public JavaTypeMapping getTypeMapping() {
		return this.getPersistentType().getMapping();
	}

	public String getPrimaryKeyColumnName() {
		return this.getMapping().getPrimaryKeyColumnName();
	}

	public boolean isOverridableAttribute() {
		return this.getMapping().isOverridableAttributeMapping();
	}

	public boolean isOverridableAssociation() {
		return this.getMapping().isOverridableAssociationMapping();
	}

	public boolean isIdAttribute() {
		return this.getMapping().isIdMapping();
	}
	
	public boolean isVirtual() {
		return false;
	}
	
	public String getName() {
		return this.name;
	}
	
	protected void setName(String newName) {
		String oldName = this.name;
		this.name = newName;
		firePropertyChanged(NAME_PROPERTY, oldName, newName);
	}

	public JavaAttributeMapping getDefaultMapping() {
		return this.defaultMapping;
	}

	/**
	 * clients do not set the "default" mapping
	 */
	protected void setDefaultMapping(JavaAttributeMapping newDefaultMapping) {
		JavaAttributeMapping oldMapping = this.defaultMapping;
		this.defaultMapping = newDefaultMapping;	
		firePropertyChanged(PersistentAttribute.DEFAULT_MAPPING_PROPERTY, oldMapping, newDefaultMapping);
	}

	public JavaAttributeMapping getSpecifiedMapping() {
		return this.specifiedMapping;
	}

	/**
	 * clients do not set the "specified" mapping;
	 * use #setMappingKey(String)
	 */
	protected void setSpecifiedMapping(JavaAttributeMapping newSpecifiedMapping) {
		JavaAttributeMapping oldMapping = this.specifiedMapping;
		this.specifiedMapping = newSpecifiedMapping;	
		firePropertyChanged(PersistentAttribute.SPECIFIED_MAPPING_PROPERTY, oldMapping, newSpecifiedMapping);
	}

	
	public JavaAttributeMapping getMapping() {
		return (this.specifiedMapping != null) ? this.specifiedMapping : this.defaultMapping;
	}

	public String getMappingKey() {
		return this.getMapping().getKey();
	}

	/**
	 * return null if there is no "default" mapping for the attribute
	 */
	public String getDefaultMappingKey() {
		return this.defaultMapping.getKey();
	}

	/**
	 * return null if there is no "specified" mapping for the attribute
	 */
	public String getSpecifiedMappingKey() {
		return (this.specifiedMapping == null) ? null : this.specifiedMapping.getKey();
	}

	// TODO support morphing mappings, i.e. copying common settings over
	// to the new mapping; this can't be done in the same was as XmlAttributeMapping
	// since we don't know all the possible mapping types
	public void setSpecifiedMappingKey(String newKey) {
		if (newKey == getSpecifiedMappingKey()) {
			return;
		}
		JavaAttributeMapping oldMapping = getSpecifiedMapping();
		JavaAttributeMapping newMapping = buildJavaAttributeMappingFromMappingKey(newKey);

		this.specifiedMapping = newMapping;	
		if (newMapping != null) {
			this.resourcePersistentAttribute.setMappingAnnotation(newMapping.getAnnotationName());
		}
		else {
			this.resourcePersistentAttribute.setMappingAnnotation(null);			
		}
		firePropertyChanged(PersistentAttribute.SPECIFIED_MAPPING_PROPERTY, oldMapping, newMapping);
		
		if (oldMapping != null) {
			Collection<String> annotationsToRemove = CollectionTools.collection(oldMapping.correspondingAnnotationNames());
			if (getMapping() != null) {
				CollectionTools.removeAll(annotationsToRemove, getMapping().correspondingAnnotationNames());
			}
			
			for (String annotationName : annotationsToRemove) {
				this.resourcePersistentAttribute.removeSupportingAnnotation(annotationName);
			}
		}
	}
	
	public JpaStructureNode getStructureNode(int textOffset) {
		return this;
	}

	public boolean contains(int offset, CompilationUnit astRoot) {
		TextRange fullTextRange = this.getFullTextRange(astRoot);
		// 'fullTextRange' will be null if the attribute no longer exists in the java;
		// the context model can be out of synch with the resource model
		// when a selection event occurs before the context model has a
		// chance to synch with the resource model via the update thread
		return (fullTextRange == null) ? false : fullTextRange.includes(offset);
	}


	public TextRange getFullTextRange(CompilationUnit astRoot) {
		return this.resourcePersistentAttribute.getTextRange(astRoot);
	}

	public TextRange getValidationTextRange(CompilationUnit astRoot) {
		return this.getSelectionTextRange(astRoot);
	}

	public TextRange getSelectionTextRange(CompilationUnit astRoot) {
		return this.resourcePersistentAttribute.getNameTextRange(astRoot);
	}
	
	public TextRange getSelectionTextRange() {
		return getSelectionTextRange(this.buildASTRoot());
	}

	protected CompilationUnit buildASTRoot() {
		return this.resourcePersistentAttribute.getJavaResourceCompilationUnit().buildASTRoot();
	}

	public void update() {
		this.setName(this.name());
		this.updateDefaultMapping();
		this.updateSpecifiedMapping();
	}
	
	protected String name() {
		return this.resourcePersistentAttribute.getName();	
	}
	
	public String specifiedMappingAnnotationName() {
		return (this.specifiedMapping == null) ? null : this.specifiedMapping.getAnnotationName();
	}
	
	protected void updateSpecifiedMapping() {
		String javaMappingAnnotationName = this.getJavaMappingAnnotationName();
		if (specifiedMappingAnnotationName() != javaMappingAnnotationName) {
			setSpecifiedMapping(buildJavaAttributeMappingFromAnnotation(javaMappingAnnotationName));
		}
		else {
			if (getSpecifiedMapping() != null) {
				getSpecifiedMapping().update(this.resourcePersistentAttribute.getMappingAnnotation(javaMappingAnnotationName));
			}
		}
	}
	
	protected void updateDefaultMapping() {
		String defaultMappingKey = getJpaPlatform().getDefaultJavaAttributeMappingKey(this);
		if (getDefaultMapping().getKey() != defaultMappingKey) {
			JavaAttributeMapping oldDefaultMapping = this.defaultMapping;
			this.defaultMapping = buildDefaultMapping();
			firePropertyChanged(PersistentAttribute.DEFAULT_MAPPING_PROPERTY, oldDefaultMapping, this.defaultMapping);
		}
		else {
			if (this.defaultMapping.getAnnotationName() != null) {
				getDefaultMapping().update(this.resourcePersistentAttribute.getNullMappingAnnotation(this.defaultMapping.getAnnotationName()));
			}
		}
	}
	
	protected String getJavaMappingAnnotationName() {
		Annotation mappingAnnotation = (Annotation) this.resourcePersistentAttribute.getMappingAnnotation();
		if (mappingAnnotation != null) {
			return mappingAnnotation.getAnnotationName();
		}
		return null;
	}
	
	protected JavaAttributeMapping buildJavaAttributeMappingFromMappingKey(String key) {
		if (key == MappingKeys.NULL_ATTRIBUTE_MAPPING_KEY) {
			return null;
		}
		JavaAttributeMapping mapping = getJpaPlatform().buildJavaAttributeMappingFromMappingKey(key, this);
		//no mapping.initialize(JavaResourcePersistentAttribute) call here
		//we do not yet have a mapping annotation so we can't call initialize
		return mapping;
	}

	protected JavaAttributeMapping buildJavaAttributeMappingFromAnnotation(String annotationName) {
		if (annotationName == null) {
			return null;
		}
		JavaAttributeMapping mapping = getJpaPlatform().buildJavaAttributeMappingFromAnnotation(annotationName, this);
		mapping.initialize(this.resourcePersistentAttribute.getMappingAnnotation(annotationName));
		return mapping;
	}

	/**
	 * the mapping might be "default", but it still might be a "null" mapping...
	 */
	public boolean mappingIsDefault(JavaAttributeMapping mapping) {
		return this.defaultMapping == mapping;
	}

	@Override
	public Iterator<String> javaCompletionProposals(int pos, Filter<String> filter, CompilationUnit astRoot) {
		Iterator<String> result = super.javaCompletionProposals(pos, filter, astRoot);
		if (result != null) {
			return result;
		}
		return this.getMapping().javaCompletionProposals(pos, filter, astRoot);
	}


	// ********** validation **********

	@Override
	public void validate(List<IMessage> messages, CompilationUnit astRoot) {
		super.validate(messages, astRoot);
		
		this.validateModifiers(messages, astRoot);
		
		if (this.specifiedMapping != null) {
			this.specifiedMapping.validate(messages, astRoot);
		}
		else if (this.defaultMapping != null) {
			this.defaultMapping.validate(messages, astRoot);
		}
	}
	
	
	protected void validateModifiers(List<IMessage> messages, CompilationUnit astRoot) {
		if (getMappingKey() == MappingKeys.TRANSIENT_ATTRIBUTE_MAPPING_KEY) {
			return;
		}
		
		if (this.resourcePersistentAttribute.isForField()) {
			if (this.resourcePersistentAttribute.isFinal()) {
				messages.add(this.buildAttributeMessage(JpaValidationMessages.PERSISTENT_ATTRIBUTE_FINAL_FIELD, astRoot));
			}
			
			if (this.resourcePersistentAttribute.isPublic()) {
				messages.add(this.buildAttributeMessage(JpaValidationMessages.PERSISTENT_ATTRIBUTE_PUBLIC_FIELD, astRoot));
			}
		}
	}

	protected IMessage buildAttributeMessage(String msgID, CompilationUnit astRoot) {
		return DefaultJpaValidationMessages.buildMessage(
				IMessage.HIGH_SEVERITY,
				msgID,
				new String[] {getName()},
				this,
				getValidationTextRange(astRoot)
			);
	}

	// ********** misc **********

	@Override
	public void toString(StringBuilder sb) {
		super.toString(sb);
		sb.append(this.name);
	}
	
	public void dispose() {
		//nothing to dispose
	}

}
