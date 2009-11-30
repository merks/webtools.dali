/*******************************************************************************
 * Copyright (c) 2009 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 * 
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.core.internal.jpa2.context.java;

import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jpt.core.context.BaseColumn;
import org.eclipse.jpt.core.context.java.JavaJpaContextNode;
import org.eclipse.jpt.core.context.java.JavaNamedColumn;
import org.eclipse.jpt.core.internal.context.java.AbstractJavaNamedColumn;
import org.eclipse.jpt.core.jpa2.context.java.JavaOrderColumn2_0;
import org.eclipse.jpt.core.jpa2.resource.java.OrderColumn2_0Annotation;
import org.eclipse.jpt.core.resource.java.JavaResourcePersistentMember;
import org.eclipse.jpt.core.utility.TextRange;


public class GenericJavaOrderColumn2_0
	extends AbstractJavaNamedColumn<OrderColumn2_0Annotation>
	implements JavaOrderColumn2_0
{
	protected Boolean specifiedNullable;
		
	protected Boolean specifiedInsertable;
		
	protected Boolean specifiedUpdatable;

	protected JavaResourcePersistentMember persistenceResource;

	
	public GenericJavaOrderColumn2_0(JavaJpaContextNode parent, JavaNamedColumn.Owner owner) {
		super(parent, owner);
	}
	
	@Override
	public void initialize(OrderColumn2_0Annotation column) {
		super.initialize(column);
		this.specifiedNullable = this.getResourceNullable(column);
		this.specifiedInsertable = this.getResourceInsertable(column);
		this.specifiedUpdatable = this.getResourceUpdatable(column);
	}

	public boolean isNullable() {
		return (this.getSpecifiedNullable() == null) ? this.isDefaultNullable() : this.getSpecifiedNullable().booleanValue();
	}
	
	public boolean isDefaultNullable() {
		return BaseColumn.DEFAULT_NULLABLE;
	}
	
	public Boolean getSpecifiedNullable() {
		return this.specifiedNullable;
	}
	
	public void setSpecifiedNullable(Boolean newSpecifiedNullable) {
		Boolean oldSpecifiedNullable = this.specifiedNullable;
		this.specifiedNullable = newSpecifiedNullable;
		this.getResourceColumn().setNullable(newSpecifiedNullable);
		firePropertyChanged(BaseColumn.SPECIFIED_NULLABLE_PROPERTY, oldSpecifiedNullable, newSpecifiedNullable);
	}
	
	/**
	 * internal setter used only for updating from the resource model.
	 * There were problems with InvalidThreadAccess exceptions in the UI
	 * when you set a value from the UI and the annotation doesn't exist yet.
	 * Adding the annotation causes an update to occur and then the exception.
	 */
	protected void setSpecifiedNullable_(Boolean newSpecifiedNullable) {
		Boolean oldSpecifiedNullable = this.specifiedNullable;
		this.specifiedNullable = newSpecifiedNullable;
		firePropertyChanged(BaseColumn.SPECIFIED_NULLABLE_PROPERTY, oldSpecifiedNullable, newSpecifiedNullable);
	}
	
	public boolean isInsertable() {
		return (this.getSpecifiedInsertable() == null) ? this.isDefaultInsertable() : this.getSpecifiedInsertable().booleanValue();
	}
	
	public boolean isDefaultInsertable() {
		return BaseColumn.DEFAULT_INSERTABLE;
	}
	
	public Boolean getSpecifiedInsertable() {
		return this.specifiedInsertable;
	}
	
	public void setSpecifiedInsertable(Boolean newSpecifiedInsertable) {
		Boolean oldSpecifiedInsertable = this.specifiedInsertable;
		this.specifiedInsertable = newSpecifiedInsertable;
		this.getResourceColumn().setInsertable(newSpecifiedInsertable);
		firePropertyChanged(BaseColumn.SPECIFIED_INSERTABLE_PROPERTY, oldSpecifiedInsertable, newSpecifiedInsertable);
	}
	
	/**
	 * internal setter used only for updating from the resource model.
	 * There were problems with InvalidThreadAccess exceptions in the UI
	 * when you set a value from the UI and the annotation doesn't exist yet.
	 * Adding the annotation causes an update to occur and then the exception.
	 */
	protected void setSpecifiedInsertable_(Boolean newSpecifiedInsertable) {
		Boolean oldSpecifiedInsertable = this.specifiedInsertable;
		this.specifiedInsertable = newSpecifiedInsertable;
		firePropertyChanged(BaseColumn.SPECIFIED_INSERTABLE_PROPERTY, oldSpecifiedInsertable, newSpecifiedInsertable);
	}
	
	public boolean isUpdatable() {
		return (this.getSpecifiedUpdatable() == null) ? this.isDefaultUpdatable() : this.getSpecifiedUpdatable().booleanValue();
	}
	
	public boolean isDefaultUpdatable() {
		return BaseColumn.DEFAULT_UPDATABLE;
	}
	
	public Boolean getSpecifiedUpdatable() {
		return this.specifiedUpdatable;
	}
	
	public void setSpecifiedUpdatable(Boolean newSpecifiedUpdatable) {
		Boolean oldSpecifiedUpdatable = this.specifiedUpdatable;
		this.specifiedUpdatable = newSpecifiedUpdatable;
		this.getResourceColumn().setUpdatable(newSpecifiedUpdatable);
		firePropertyChanged(BaseColumn.SPECIFIED_UPDATABLE_PROPERTY, oldSpecifiedUpdatable, newSpecifiedUpdatable);
	}

	/**
	 * internal setter used only for updating from the resource model.
	 * There were problems with InvalidThreadAccess exceptions in the UI
	 * when you set a value from the UI and the annotation doesn't exist yet.
	 * Adding the annotation causes an update to occur and then the exception.
	 */
	protected void setSpecifiedUpdatable_(Boolean newSpecifiedUpdatable) {
		Boolean oldSpecifiedUpdatable = this.specifiedUpdatable;
		this.specifiedUpdatable = newSpecifiedUpdatable;
		firePropertyChanged(BaseColumn.SPECIFIED_UPDATABLE_PROPERTY, oldSpecifiedUpdatable, newSpecifiedUpdatable);
	}

	public TextRange getValidationTextRange(CompilationUnit astRoot) {
		TextRange textRange = getResourceColumn().getTextRange(astRoot);
		return (textRange != null) ? textRange : this.getOwner().getValidationTextRange(astRoot);	
	}
	
	@Override
	public void update(OrderColumn2_0Annotation column) {
		super.update(column);
		this.setSpecifiedNullable_(this.getResourceNullable(column));
		this.setSpecifiedInsertable_(this.getResourceInsertable(column));
		this.setSpecifiedUpdatable_(this.getResourceUpdatable(column));
	}
	
	protected Boolean getResourceNullable(OrderColumn2_0Annotation column) {
		return column.getNullable();
	}
	
	protected Boolean getResourceInsertable(OrderColumn2_0Annotation column) {
		return column.getInsertable();
	}
	
	protected Boolean getResourceUpdatable(OrderColumn2_0Annotation column) {
		return column.getUpdatable();
	}
}
