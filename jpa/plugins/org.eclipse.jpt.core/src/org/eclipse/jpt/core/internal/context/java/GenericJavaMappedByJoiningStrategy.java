/*******************************************************************************
 * Copyright (c) 2009, 2010 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 * 
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.core.internal.context.java;

import java.util.Iterator;
import java.util.List;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jpt.core.context.AttributeMapping;
import org.eclipse.jpt.core.context.Entity;
import org.eclipse.jpt.core.context.RelationshipMapping;
import org.eclipse.jpt.core.context.java.JavaMappedByJoiningStrategy;
import org.eclipse.jpt.core.context.java.JavaOwnableRelationshipReference;
import org.eclipse.jpt.core.context.java.JavaRelationshipMapping;
import org.eclipse.jpt.core.internal.validation.DefaultJpaValidationMessages;
import org.eclipse.jpt.core.internal.validation.JpaValidationMessages;
import org.eclipse.jpt.core.resource.java.OwnableRelationshipMappingAnnotation;
import org.eclipse.jpt.core.utility.TextRange;
import org.eclipse.jpt.db.Table;
import org.eclipse.jpt.utility.Filter;
import org.eclipse.jpt.utility.internal.StringTools;
import org.eclipse.jpt.utility.internal.iterators.FilteringIterator;
import org.eclipse.wst.validation.internal.provisional.core.IMessage;
import org.eclipse.wst.validation.internal.provisional.core.IReporter;

public class GenericJavaMappedByJoiningStrategy
	extends AbstractJavaJpaContextNode 
	implements JavaMappedByJoiningStrategy
{
	protected OwnableRelationshipMappingAnnotation mappingAnnotation;
	
	protected String mappedByAttribute;
	
	
	public GenericJavaMappedByJoiningStrategy(JavaOwnableRelationshipReference parent) {
		super(parent);
	}
	
	
	@Override
	public JavaOwnableRelationshipReference getParent() {
		return (JavaOwnableRelationshipReference) super.getParent();
	}
	
	public JavaOwnableRelationshipReference getRelationshipReference() {
		return this.getParent();
	}
	
	public JavaRelationshipMapping getRelationshipMapping() {
		return getParent().getRelationshipMapping();
	}
	
	public String getTableName() {
		RelationshipMapping owner = getRelationshipOwner();
		return owner == null ? null : owner.getRelationshipReference().getPredominantJoiningStrategy().getTableName();
	}

	public Table getDbTable(String tableName) {
		RelationshipMapping owner = getRelationshipOwner();
		return owner == null ? null : owner.getRelationshipReference().getPredominantJoiningStrategy().getDbTable(tableName);
	}

	protected RelationshipMapping getRelationshipOwner() {
		return getRelationshipMapping().getRelationshipOwner();
	}
	
	public boolean isOverridableAssociation() {
		return false;
	}
	
	public boolean relationshipIsOwnedBy(RelationshipMapping otherMapping) {
		String thisEntity = 
			(getRelationshipReference().getEntity()) == null ?
				null : getRelationshipReference().getEntity().getName();
		String otherTargetEntity = 
			(otherMapping.getResolvedTargetEntity() == null) ?
				null : otherMapping.getResolvedTargetEntity().getName();
		return StringTools.stringsAreEqual(
				thisEntity,
				otherTargetEntity)
			&& StringTools.stringsAreEqual(
				getMappedByAttribute(), 
				otherMapping.getName());
	}
	
	public String getMappedByAttribute() {
		return this.mappedByAttribute;
	}
	
	public void setMappedByAttribute(String newMappedByAttribute) {
		String oldMappedByAttribute = this.mappedByAttribute;
		this.mappedByAttribute = newMappedByAttribute;
		this.mappingAnnotation.setMappedBy(newMappedByAttribute);
		firePropertyChanged(MAPPED_BY_ATTRIBUTE_PROPERTY, oldMappedByAttribute, newMappedByAttribute);
	}
	
	protected void setMappedByAttribute_(String newMappedByAttribute) {
		String oldMappedByAttribute = this.mappedByAttribute;
		this.mappedByAttribute = newMappedByAttribute;
		firePropertyChanged(MAPPED_BY_ATTRIBUTE_PROPERTY, oldMappedByAttribute, newMappedByAttribute);
	}
	
	public void addStrategy() {
		if (this.mappedByAttribute == null) {
			setMappedByAttribute("");
		}
	}
	
	public void removeStrategy() {
		if (this.mappedByAttribute != null) {
			setMappedByAttribute(null);
		}
	}
	
	public void initialize() {
		this.mappingAnnotation = this.getRelationshipReference().getMappingAnnotation();
		this.mappedByAttribute = this.mappingAnnotation.getMappedBy();
	}
	
	public void update() {
		this.mappingAnnotation = this.getRelationshipReference().getMappingAnnotation();
		setMappedByAttribute_(this.mappingAnnotation.getMappedBy());
	}
	
	@Override
	public Iterator<String> javaCompletionProposals(int pos, Filter<String> filter, CompilationUnit astRoot) {
		Iterator<String> result = super.javaCompletionProposals(pos, filter, astRoot);
		if (result != null) {
			return result;
		}
		if (this.mappingAnnotation.mappedByTouches(pos, astRoot)) {
			result = javaCandidateMappedByAttributeNames(filter);
		}
		return result;
	}
	
	public Iterator<String> candidateMappedByAttributeNames() {
		return getRelationshipMapping().allTargetEntityAttributeNames();	
	}
	
	public Iterator<String> candidateMappedByAttributeNames(Filter<String> filter) {
		return new FilteringIterator<String>(this.candidateMappedByAttributeNames(), filter);
	}
	
	protected Iterator<String> javaCandidateMappedByAttributeNames(Filter<String> filter) {
		return StringTools.convertToJavaStringLiterals(this.candidateMappedByAttributeNames(filter));
	}
	
	
	// **************** validation *********************************************
	
	@Override
	public void validate(List<IMessage> messages, IReporter reporter, CompilationUnit astRoot) {
		super.validate(messages, reporter, astRoot);
		
		if (getMappedByAttribute() == null) {
			return;
		}
		
		Entity targetEntity = this.getRelationshipMapping().getResolvedTargetEntity();
		if (targetEntity == null) {
			return;  // null target entity is validated elsewhere
		}
		
		AttributeMapping mappedByMapping = targetEntity.resolveAttributeMapping(this.mappedByAttribute);
		
		if (mappedByMapping == null) {
			messages.add(
				DefaultJpaValidationMessages.buildMessage(
					IMessage.HIGH_SEVERITY,
					JpaValidationMessages.MAPPING_UNRESOLVED_MAPPED_BY,
					new String[] {this.mappedByAttribute},
					this,
					this.getValidationTextRange(astRoot)
				)
			);
			return;
		}
		
		if ( ! this.getRelationshipReference().mayBeMappedBy(mappedByMapping)) {
			messages.add(
				DefaultJpaValidationMessages.buildMessage(
					IMessage.HIGH_SEVERITY,
					JpaValidationMessages.MAPPING_INVALID_MAPPED_BY,
					new String[] {this.mappedByAttribute}, 
					this,
					this.getValidationTextRange(astRoot)
				)
			);
			return;
		}
		
		// if mappedByMapping is not a relationship owner, then it should have 
		// been flagged in above rule (mappedByIsValid)
		if (! ((RelationshipMapping) mappedByMapping).isRelationshipOwner()) {
			messages.add(
				DefaultJpaValidationMessages.buildMessage(
					IMessage.HIGH_SEVERITY,
					JpaValidationMessages.MAPPING_MAPPED_BY_ON_BOTH_SIDES,
					this,
					this.getValidationTextRange(astRoot)
				)
			);
		}
	}
	
	public TextRange getValidationTextRange(CompilationUnit astRoot) {
		return this.mappingAnnotation.getMappedByTextRange(astRoot);
	}
}
