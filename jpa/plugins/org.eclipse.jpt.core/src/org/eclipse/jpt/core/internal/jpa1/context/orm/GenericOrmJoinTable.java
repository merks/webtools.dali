/*******************************************************************************
 * Copyright (c) 2007, 2010 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 * 
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.core.internal.jpa1.context.orm;

import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Vector;

import org.eclipse.jpt.core.context.BaseColumn;
import org.eclipse.jpt.core.context.BaseJoinColumn;
import org.eclipse.jpt.core.context.Entity;
import org.eclipse.jpt.core.context.JoinColumn;
import org.eclipse.jpt.core.context.JoinTable;
import org.eclipse.jpt.core.context.NamedColumn;
import org.eclipse.jpt.core.context.PersistentAttribute;
import org.eclipse.jpt.core.context.RelationshipMapping;
import org.eclipse.jpt.core.context.TypeMapping;
import org.eclipse.jpt.core.context.orm.OrmJoinColumn;
import org.eclipse.jpt.core.context.orm.OrmJoinTable;
import org.eclipse.jpt.core.context.orm.OrmJoinTableJoiningStrategy;
import org.eclipse.jpt.core.internal.context.MappingTools;
import org.eclipse.jpt.core.internal.validation.DefaultJpaValidationMessages;
import org.eclipse.jpt.core.internal.validation.JpaValidationDescriptionMessages;
import org.eclipse.jpt.core.internal.validation.JpaValidationMessages;
import org.eclipse.jpt.core.resource.orm.AbstractXmlReferenceTable;
import org.eclipse.jpt.core.resource.orm.OrmFactory;
import org.eclipse.jpt.core.resource.orm.XmlJoinColumn;
import org.eclipse.jpt.core.resource.orm.XmlJoinTable;
import org.eclipse.jpt.core.utility.TextRange;
import org.eclipse.jpt.utility.internal.CollectionTools;
import org.eclipse.jpt.utility.internal.StringTools;
import org.eclipse.jpt.utility.internal.iterators.CloneIterator;
import org.eclipse.jpt.utility.internal.iterators.CloneListIterator;
import org.eclipse.jpt.utility.internal.iterators.EmptyIterator;
import org.eclipse.jpt.utility.internal.iterators.EmptyListIterator;
import org.eclipse.jpt.utility.internal.iterators.SingleElementListIterator;
import org.eclipse.wst.validation.internal.provisional.core.IMessage;
import org.eclipse.wst.validation.internal.provisional.core.IReporter;

/**
 * orm.xml join table
 */
public class GenericOrmJoinTable
	extends GenericOrmReferenceTable
	implements OrmJoinTable
{

	protected OrmJoinColumn defaultInverseJoinColumn;

	protected final Vector<OrmJoinColumn> specifiedInverseJoinColumns = new Vector<OrmJoinColumn>();
	protected final OrmJoinColumn.Owner inverseJoinColumnOwner;


	public GenericOrmJoinTable(OrmJoinTableJoiningStrategy parent, XmlJoinTable resourceJoinTable) {
		super(parent);
		this.inverseJoinColumnOwner = this.buildInverseJoinColumnOwner();
		this.initialize(resourceJoinTable);
	}

	@Override
	protected OrmJoinColumn.Owner buildJoinColumnOwner() {
		return new JoinColumnOwner();
	}

	protected OrmJoinColumn.Owner buildInverseJoinColumnOwner() {
		return new InverseJoinColumnOwner();
	}

	public RelationshipMapping getRelationshipMapping() {
		return getParent().getRelationshipReference().getRelationshipMapping();
	}

	public void initializeFrom(JoinTable oldJoinTable) {
		super.initializeFrom(oldJoinTable);
		for (Iterator<OrmJoinColumn> stream = oldJoinTable.specifiedInverseJoinColumns(); stream.hasNext(); ) {
			this.addSpecifiedInverseJoinColumnFrom(stream.next());
		}
	}

	protected void initialize(XmlJoinTable joinTable) {
		super.initialize(joinTable);
		this.initializeSpecifiedInverseJoinColumns(joinTable);
		this.initializeDefaultInverseJoinColumn();
	}

	@Override
	protected void update(AbstractXmlReferenceTable joinTable) {
		super.update(joinTable);
		this.updateSpecifiedInverseJoinColumns((XmlJoinTable) joinTable);
		this.updateDefaultInverseJoinColumn();
	}

	public PersistentAttribute getPersistentAttribute() {
		return getRelationshipMapping().getPersistentAttribute();
	}


	// ********** AbstractOrmTable implementation **********

	@Override
	public OrmJoinTableJoiningStrategy getParent() {
		return (OrmJoinTableJoiningStrategy) super.getParent();
	}

	@Override
	protected String buildDefaultName() {
		return this.getParent().getJoinTableDefaultName();
	}

	@Override
	protected XmlJoinTable getResourceTable() {
		return this.getParent().getResourceJoinTable();
	}

	@Override
	protected XmlJoinTable addResourceTable() {
		return getParent().addResourceJoinTable();
	}

	@Override
	protected void removeResourceTable() {
		getParent().removeResourceJoinTable();
	}


	// ********** inverse join columns **********

	public ListIterator<OrmJoinColumn> inverseJoinColumns() {
		return this.hasSpecifiedInverseJoinColumns() ? this.specifiedInverseJoinColumns() : this.defaultInverseJoinColumns();
	}

	public int inverseJoinColumnsSize() {
		return this.hasSpecifiedInverseJoinColumns() ? this.specifiedInverseJoinColumnsSize() : this.defaultInverseJoinColumnsSize();
	}

	public void convertDefaultToSpecifiedInverseJoinColumn() {
		MappingTools.convertJoinTableDefaultToSpecifiedInverseJoinColumn(this);
	}


	// ********** default inverse join column **********

	public OrmJoinColumn getDefaultInverseJoinColumn() {
		return this.defaultInverseJoinColumn;
	}

	protected void setDefaultInverseJoinColumn(OrmJoinColumn defaultInverseJoinColumn) {
		OrmJoinColumn old = this.defaultInverseJoinColumn;
		this.defaultInverseJoinColumn = defaultInverseJoinColumn;
		this.firePropertyChanged(DEFAULT_INVERSE_JOIN_COLUMN, old, defaultInverseJoinColumn);
	}

	protected ListIterator<OrmJoinColumn> defaultInverseJoinColumns() {
		if (this.defaultInverseJoinColumn != null) {
			return new SingleElementListIterator<OrmJoinColumn>(this.defaultInverseJoinColumn);
		}
		return EmptyListIterator.instance();
	}

	protected int defaultInverseJoinColumnsSize() {
		return (this.defaultInverseJoinColumn == null) ? 0 : 1;
	}

	protected void initializeDefaultInverseJoinColumn() {
		if (this.shouldBuildDefaultInverseJoinColumn()) {
			this.defaultInverseJoinColumn = this.buildInverseJoinColumn(null);
		}
	}

	protected void updateDefaultInverseJoinColumn() {
		if (this.shouldBuildDefaultInverseJoinColumn()) {
			if (this.defaultInverseJoinColumn == null) {
				this.setDefaultInverseJoinColumn(this.buildInverseJoinColumn(null));
			} else {
				this.defaultInverseJoinColumn.update(null);
			}
		} else {
			this.setDefaultInverseJoinColumn(null);
		}
	}

	protected boolean shouldBuildDefaultInverseJoinColumn() {
		return ! this.hasSpecifiedInverseJoinColumns();
	}


	// ********** specified inverse join columns **********

	public ListIterator<OrmJoinColumn> specifiedInverseJoinColumns() {
		return new CloneListIterator<OrmJoinColumn>(this.specifiedInverseJoinColumns);
	}

	public int specifiedInverseJoinColumnsSize() {
		return this.specifiedInverseJoinColumns.size();
	}

	public boolean hasSpecifiedInverseJoinColumns() {
		return !this.specifiedInverseJoinColumns.isEmpty();
	}

	protected void addSpecifiedInverseJoinColumnFrom(OrmJoinColumn oldJoinColumn) {
		OrmJoinColumn newJoinColumn = this.addSpecifiedInverseJoinColumn(this.specifiedInverseJoinColumns.size());
		newJoinColumn.initializeFrom(oldJoinColumn);
	}

	public OrmJoinColumn addSpecifiedInverseJoinColumn(int index) {
		if (this.getResourceTable() == null) {
			this.addResourceTable();
		}
		XmlJoinColumn xmlJoinColumn = OrmFactory.eINSTANCE.createXmlJoinColumn();
		OrmJoinColumn joinColumn = this.buildInverseJoinColumn(xmlJoinColumn);
		this.specifiedInverseJoinColumns.add(index, joinColumn);
		this.getResourceTable().getInverseJoinColumns().add(index, xmlJoinColumn);
		this.fireItemAdded(SPECIFIED_INVERSE_JOIN_COLUMNS_LIST, index, joinColumn);
		return joinColumn;
	}

	protected void addSpecifiedInverseJoinColumn(int index, OrmJoinColumn joinColumn) {
		this.addItemToList(index, joinColumn, this.specifiedInverseJoinColumns, SPECIFIED_INVERSE_JOIN_COLUMNS_LIST);
	}

	protected void addSpecifiedInverseJoinColumn(OrmJoinColumn joinColumn) {
		this.addSpecifiedInverseJoinColumn(this.specifiedInverseJoinColumns.size(), joinColumn);
	}

	public void removeSpecifiedInverseJoinColumn(JoinColumn joinColumn) {
		this.removeSpecifiedInverseJoinColumn(this.specifiedInverseJoinColumns.indexOf(joinColumn));
	}

	public void removeSpecifiedInverseJoinColumn(int index) {
		OrmJoinColumn removedJoinColumn = this.specifiedInverseJoinColumns.remove(index);
		if ( ! this.hasSpecifiedInverseJoinColumns()) {
			//create the defaultInverseJoinColumn now or this will happen during project update 
			//after removing the join column from the resource model. That causes problems 
			//in the UI because the change notifications end up in the wrong order.
			this.defaultInverseJoinColumn = this.buildInverseJoinColumn(null);
		}
		this.getResourceTable().getInverseJoinColumns().remove(index);
		this.fireItemRemoved(SPECIFIED_INVERSE_JOIN_COLUMNS_LIST, index, removedJoinColumn);
		if (this.defaultInverseJoinColumn != null) {
			//fire change notification if a defaultJoinColumn was created above
			this.firePropertyChanged(DEFAULT_INVERSE_JOIN_COLUMN, null, this.defaultInverseJoinColumn);
		}
	}

	protected void removeSpecifiedInverseJoinColumn_(OrmJoinColumn joinColumn) {
		this.removeItemFromList(joinColumn, this.specifiedInverseJoinColumns, SPECIFIED_INVERSE_JOIN_COLUMNS_LIST);
	}

	public void moveSpecifiedInverseJoinColumn(int targetIndex, int sourceIndex) {
		CollectionTools.move(this.specifiedInverseJoinColumns, targetIndex, sourceIndex);
		this.getResourceTable().getInverseJoinColumns().move(targetIndex, sourceIndex);
		this.fireItemMoved(SPECIFIED_INVERSE_JOIN_COLUMNS_LIST, targetIndex, sourceIndex);
	}

	public void clearSpecifiedInverseJoinColumns() {
		this.specifiedInverseJoinColumns.clear();
		this.defaultInverseJoinColumn = this.buildInverseJoinColumn(null);
		this.getResourceTable().getInverseJoinColumns().clear();
		this.fireListCleared(SPECIFIED_INVERSE_JOIN_COLUMNS_LIST);
		this.firePropertyChanged(DEFAULT_INVERSE_JOIN_COLUMN, null, this.defaultInverseJoinColumn);
	}

	protected OrmJoinColumn buildInverseJoinColumn(XmlJoinColumn resourceJoinColumn) {
		return this.buildJoinColumn(resourceJoinColumn, this.inverseJoinColumnOwner);
	}

	protected void initializeSpecifiedInverseJoinColumns(XmlJoinTable xmlJoinTable) {
		if (xmlJoinTable != null) {
			for (XmlJoinColumn xmlJoinColumn : xmlJoinTable.getInverseJoinColumns()) {
				this.specifiedInverseJoinColumns.add(this.buildInverseJoinColumn(xmlJoinColumn));
			}
		}
	}

	protected void updateSpecifiedInverseJoinColumns(XmlJoinTable xmlJoinTable) {
		Iterator<XmlJoinColumn> xmlJoinColumns = this.xmlInverseJoinColumns(xmlJoinTable);

		for (ListIterator<OrmJoinColumn> contextJoinColumns = this.specifiedInverseJoinColumns(); contextJoinColumns.hasNext(); ) {
			OrmJoinColumn contextColumn = contextJoinColumns.next();
			if (xmlJoinColumns.hasNext()) {
				contextColumn.update(xmlJoinColumns.next());
			} else {
				this.removeSpecifiedInverseJoinColumn_(contextColumn);
			}
		}

		while (xmlJoinColumns.hasNext()) {
			this.addSpecifiedInverseJoinColumn(this.buildInverseJoinColumn(xmlJoinColumns.next()));
		}
	}

	protected Iterator<XmlJoinColumn> xmlInverseJoinColumns(XmlJoinTable xmlJoinTable) {
		// make a copy of the XML join columns (to prevent ConcurrentModificationException)
		return (xmlJoinTable == null) ? EmptyIterator.<XmlJoinColumn>instance()
			: new CloneIterator<XmlJoinColumn>(xmlJoinTable.getInverseJoinColumns());
	}


	// ********** validation **********

	@Override
	protected void validateJoinColumns(List<IMessage> messages, IReporter reporter) {
		super.validateJoinColumns(messages, reporter);
		this.validateJoinColumns(this.inverseJoinColumns(), messages, reporter);
	}

	@Override
	protected boolean shouldValidateAgainstDatabase() {
		return getParent().shouldValidateAgainstDatabase();
	}

	@Override
	protected String getUnresolvedCatalogMessageId() {
		return JpaValidationMessages.JOIN_TABLE_UNRESOLVED_CATALOG;
	}

	@Override
	protected String getUnresolvedNameMessageId() {
		return JpaValidationMessages.JOIN_TABLE_UNRESOLVED_NAME;
	}

	@Override
	protected String getUnresolvedSchemaMessageId() {
		return JpaValidationMessages.JOIN_TABLE_UNRESOLVED_SCHEMA;
	}

	@Override
	protected String getVirtualAttributeUnresolvedCatalogMessageId() {
		return JpaValidationMessages.VIRTUAL_ATTRIBUTE_JOIN_TABLE_UNRESOLVED_CATALOG;
	}

	@Override
	protected String getVirtualAttributeUnresolvedNameMessageId() {
		return JpaValidationMessages.VIRTUAL_ATTRIBUTE_JOIN_TABLE_UNRESOLVED_NAME;
	}

	@Override
	protected String getVirtualAttributeUnresolvedSchemaMessageId() {
		return JpaValidationMessages.VIRTUAL_ATTRIBUTE_JOIN_TABLE_UNRESOLVED_SCHEMA;
	}

	// ********** join column owner adapters **********

	/**
	 * just a little common behavior
	 */
	protected abstract class AbstractJoinColumnOwner
		implements OrmJoinColumn.Owner
	{
		protected AbstractJoinColumnOwner() {
			super();
		}

		public TypeMapping getTypeMapping() {
			return GenericOrmJoinTable.this.getParent().getRelationshipReference().getTypeMapping();
		}

		public PersistentAttribute getPersistentAttribute() {
			return GenericOrmJoinTable.this.getRelationshipMapping().getPersistentAttribute();
		}

		/**
		 * If there is a specified table name it needs to be the same
		 * the default table name.  the table is always the join table
		 */
		public boolean tableNameIsInvalid(String tableName) {
			return !StringTools.stringsAreEqual(getDefaultTableName(), tableName);
		}

		/**
		 * the join column can only be on the join table itself
		 */
		public boolean tableIsAllowed() {
			return false;
		}

		public Iterator<String> candidateTableNames() {
			return EmptyIterator.instance();
		}

		public org.eclipse.jpt.db.Table getDbTable(String tableName) {
			String joinTableName = GenericOrmJoinTable.this.getName();
			return (joinTableName == null) ? null : (joinTableName.equals(tableName)) ? GenericOrmJoinTable.this.getDbTable() : null;
		}

		/**
		 * by default, the join column is, obviously, in the join table;
		 * not sure whether it can be anywhere else...
		 */
		public String getDefaultTableName() {
			return GenericOrmJoinTable.this.getName();
		}

		public TextRange getValidationTextRange() {
			return GenericOrmJoinTable.this.getValidationTextRange();
		}

		protected boolean isPersistentAttributeVirtual() {
			return getPersistentAttribute().isVirtual();
		}

		protected String getPersistentAttributeName() {
			return getPersistentAttribute().getName();
		}

		public IMessage buildTableNotValidMessage(BaseColumn column, TextRange textRange) {
			if (isPersistentAttributeVirtual()) {
				return this.buildVirtualTableNotValidMessage(column, textRange);
			}
			return DefaultJpaValidationMessages.buildMessage(
				IMessage.HIGH_SEVERITY,
				this.getTableNotValidMessage(),
				new String[] {
					column.getTable(),
					column.getName(),
					JpaValidationDescriptionMessages.DOES_NOT_MATCH_JOIN_TABLE}, 
				column, 
				textRange
			);
		}

		protected IMessage buildVirtualTableNotValidMessage(BaseColumn column, TextRange textRange) {
			return DefaultJpaValidationMessages.buildMessage(
				IMessage.HIGH_SEVERITY,
				this.getVirtualTableNotValidMessage(),
				new String[] {
					getName(),
					column.getTable(),
					column.getName(),
					JpaValidationDescriptionMessages.DOES_NOT_MATCH_JOIN_TABLE},
				column, 
				textRange
			);
		}

		protected abstract String getTableNotValidMessage();

		protected abstract String getVirtualTableNotValidMessage();

		public IMessage buildUnresolvedNameMessage(NamedColumn column, TextRange textRange) {
			if (isPersistentAttributeVirtual()) {
				return this.buildVirtualUnresolvedNameMessage(column, textRange);
			}
			return DefaultJpaValidationMessages.buildMessage(
				IMessage.HIGH_SEVERITY,
				this.getUnresolvedNameMessage(),
				new String[] {column.getName(), column.getDbTable().getName()}, 
				column, 
				textRange
			);
		}

		protected IMessage buildVirtualUnresolvedNameMessage(NamedColumn column, TextRange textRange) {
			return DefaultJpaValidationMessages.buildMessage(
				IMessage.HIGH_SEVERITY,
				this.getVirtualUnresolvedNameMessage(),
				new String[] {getPersistentAttributeName(), column.getName(), column.getDbTable().getName()},
				column, 
				textRange
			);
		}

		protected abstract String getUnresolvedNameMessage();

		protected abstract String getVirtualUnresolvedNameMessage();

		public IMessage buildUnresolvedReferencedColumnNameMessage(BaseJoinColumn column, TextRange textRange) {
			if (isPersistentAttributeVirtual()) {
				return this.buildVirtualUnresolvedReferencedColumnNameMessage(column, textRange);
			}
			return DefaultJpaValidationMessages.buildMessage(
				IMessage.HIGH_SEVERITY,
				this.getUnresolvedReferencedColumnNameMessage(),
				new String[] {column.getReferencedColumnName(), column.getReferencedColumnDbTable().getName()},
				column, 
				textRange
			);
		}

		protected IMessage buildVirtualUnresolvedReferencedColumnNameMessage(BaseJoinColumn column, TextRange textRange) {
			return DefaultJpaValidationMessages.buildMessage(
				IMessage.HIGH_SEVERITY,
				this.getVirtualUnresolvedReferencedColumnNameMessage(),
				new String[] {this.getPersistentAttributeName(), column.getReferencedColumnName(), column.getReferencedColumnDbTable().getName()},
				column, 
				textRange
			);
		}

		protected abstract String getUnresolvedReferencedColumnNameMessage();

		protected abstract String getVirtualUnresolvedReferencedColumnNameMessage();

		public IMessage buildUnspecifiedNameMultipleJoinColumnsMessage(BaseJoinColumn column, TextRange textRange) {
			if (this.isPersistentAttributeVirtual()) {
				return this.buildVirtualUnspecifiedNameMultipleJoinColumnsMessage(column, textRange);
			}
			return DefaultJpaValidationMessages.buildMessage(
				IMessage.HIGH_SEVERITY,
				getUnspecifiedNameMultipleJoinColumnsMessage(),
				new String[0],
				column,
				textRange
			);
		}

		protected IMessage buildVirtualUnspecifiedNameMultipleJoinColumnsMessage(BaseJoinColumn column, TextRange textRange) {
			return DefaultJpaValidationMessages.buildMessage(
				IMessage.HIGH_SEVERITY,
				getVirtualUnspecifiedNameMultipleJoinColumnsMessage(),
				new String[] {this.getPersistentAttributeName()},
				column, 
				textRange
			);
		}

		protected abstract String getUnspecifiedNameMultipleJoinColumnsMessage();

		protected abstract String getVirtualUnspecifiedNameMultipleJoinColumnsMessage();

		public IMessage buildUnspecifiedReferencedColumnNameMultipleJoinColumnsMessage(BaseJoinColumn column, TextRange textRange) {
			if (this.isPersistentAttributeVirtual()) {
				return this.buildVirtualUnspecifiedReferencedColumnNameMultipleJoinColumnsMessage(column, textRange);
			}
			return DefaultJpaValidationMessages.buildMessage(
				IMessage.HIGH_SEVERITY,
				getUnspecifiedReferencedColumnNameMultipleJoinColumnsMessage(),
				new String[0],
				column,
				textRange
			);
		}

		protected IMessage buildVirtualUnspecifiedReferencedColumnNameMultipleJoinColumnsMessage(BaseJoinColumn column, TextRange textRange) {
			return DefaultJpaValidationMessages.buildMessage(
				IMessage.HIGH_SEVERITY,
				getVirtualUnspecifiedReferencedColumnNameMultipleJoinColumnsMessage(),
				new String[] {this.getPersistentAttributeName()},
				column, 
				textRange
			);
		}

		protected abstract String getUnspecifiedReferencedColumnNameMultipleJoinColumnsMessage();

		protected abstract String getVirtualUnspecifiedReferencedColumnNameMultipleJoinColumnsMessage();
	}


	/**
	 * owner for "back-pointer" JoinColumns;
	 * these point at the source/owning entity
	 */
	protected class JoinColumnOwner
		extends AbstractJoinColumnOwner
	{
		protected JoinColumnOwner() {
			super();
		}

		public Entity getTargetEntity() {
			return GenericOrmJoinTable.this.getParent().getRelationshipReference().getEntity();
		}

		public String getAttributeName() {
			RelationshipMapping relationshipMapping = GenericOrmJoinTable.this.getRelationshipMapping();
			if (relationshipMapping == null) {
				return null;
			}
			Entity targetEntity = relationshipMapping.getResolvedTargetEntity();
			if (targetEntity == null) {
				return null;
			}
			for (PersistentAttribute each : CollectionTools.iterable(targetEntity.getPersistentType().allAttributes())) {
				if (each.getMapping().isOwnedBy(relationshipMapping)) {
					return each.getName();
				}
			}
			return null;
		}

		@Override
		public org.eclipse.jpt.db.Table getDbTable(String tableName) {
			org.eclipse.jpt.db.Table dbTable = super.getDbTable(tableName);
			return (dbTable != null) ? dbTable : this.getTypeMapping().getDbTable(tableName);
		}

		public org.eclipse.jpt.db.Table getReferencedColumnDbTable() {
			return getTypeMapping().getPrimaryDbTable();
		}

		public boolean isVirtual(BaseJoinColumn joinColumn) {
			return GenericOrmJoinTable.this.defaultJoinColumn == joinColumn;
		}

		public String getDefaultColumnName() {
			//built in MappingTools.buildJoinColumnDefaultName()
			return null;
		}

		public int joinColumnsSize() {
			return GenericOrmJoinTable.this.joinColumnsSize();
		}

		@Override
		protected String getTableNotValidMessage() {
			return JpaValidationMessages.JOIN_COLUMN_TABLE_NOT_VALID;
		}

		@Override
		public String getVirtualTableNotValidMessage() {
			return JpaValidationMessages.VIRTUAL_ATTRIBUTE_JOIN_COLUMN_TABLE_NOT_VALID;
		}

		@Override
		protected String getUnresolvedNameMessage() {
			return JpaValidationMessages.JOIN_COLUMN_UNRESOLVED_NAME;
		}

		@Override
		protected String getVirtualUnresolvedNameMessage() {
			return JpaValidationMessages.VIRTUAL_ATTRIBUTE_JOIN_COLUMN_UNRESOLVED_NAME;
		}

		@Override
		public String getUnresolvedReferencedColumnNameMessage() {
			return JpaValidationMessages.JOIN_COLUMN_UNRESOLVED_REFERENCED_COLUMN_NAME;
		}

		@Override
		public String getVirtualUnresolvedReferencedColumnNameMessage() {
			return JpaValidationMessages.VIRTUAL_ATTRIBUTE_JOIN_COLUMN_UNRESOLVED_REFERENCED_COLUMN_NAME;
		}

		@Override
		public String getUnspecifiedNameMultipleJoinColumnsMessage() {
			return JpaValidationMessages.JOIN_COLUMN_NAME_MUST_BE_SPECIFIED_MULTIPLE_JOIN_COLUMNS;
		}

		@Override
		public String getVirtualUnspecifiedNameMultipleJoinColumnsMessage() {
			return JpaValidationMessages.VIRTUAL_ATTRIBUTE_JOIN_COLUMN_NAME_MUST_BE_SPECIFIED_MULTIPLE_JOIN_COLUMNS;
		}

		@Override
		public String getUnspecifiedReferencedColumnNameMultipleJoinColumnsMessage() {
			return JpaValidationMessages.JOIN_COLUMN_REFERENCED_COLUMN_NAME_MUST_BE_SPECIFIED_MULTIPLE_JOIN_COLUMNS;
		}

		@Override
		public String getVirtualUnspecifiedReferencedColumnNameMultipleJoinColumnsMessage() {
			return JpaValidationMessages.VIRTUAL_ATTRIBUTE_JOIN_COLUMN_REFERENCED_COLUMN_NAME_MUST_BE_SPECIFIED_MULTIPLE_JOIN_COLUMNS;
		}
	}


	/**
	 * owner for "forward-pointer" JoinColumns;
	 * these point at the target/inverse entity
	 */
	protected class InverseJoinColumnOwner
		extends AbstractJoinColumnOwner
	{
		protected InverseJoinColumnOwner() {
			super();
		}

		public Entity getTargetEntity() {
			RelationshipMapping relationshipMapping = GenericOrmJoinTable.this.getRelationshipMapping();
			return relationshipMapping == null ? null : relationshipMapping.getResolvedTargetEntity();
		}

		public String getAttributeName() {
			RelationshipMapping relationshipMapping = GenericOrmJoinTable.this.getRelationshipMapping();
			return relationshipMapping == null ? null : relationshipMapping.getName();
		}

		@Override
		public org.eclipse.jpt.db.Table getDbTable(String tableName) {
			org.eclipse.jpt.db.Table dbTable = super.getDbTable(tableName);
			if (dbTable != null) {
				return dbTable;
			}
			Entity targetEntity = this.getTargetEntity();
			return (targetEntity == null) ? null : targetEntity.getDbTable(tableName);
		}

		public org.eclipse.jpt.db.Table getReferencedColumnDbTable() {
			Entity targetEntity = this.getTargetEntity();
			return (targetEntity == null) ? null : targetEntity.getPrimaryDbTable();
		}

		public boolean isVirtual(BaseJoinColumn joinColumn) {
			return GenericOrmJoinTable.this.defaultInverseJoinColumn == joinColumn;
		}

		public String getDefaultColumnName() {
			//built in MappingTools.buildJoinColumnDefaultName()
			return null;
		}

		public int joinColumnsSize() {
			return GenericOrmJoinTable.this.inverseJoinColumnsSize();
		}

		@Override
		protected String getTableNotValidMessage() {
			return JpaValidationMessages.INVERSE_JOIN_COLUMN_TABLE_NOT_VALID;
		}

		@Override
		public String getVirtualTableNotValidMessage() {
			return JpaValidationMessages.VIRTUAL_ATTRIBUTE_INVERSE_JOIN_COLUMN_TABLE_NOT_VALID;
		}

		@Override
		public String getUnresolvedNameMessage() {
			return JpaValidationMessages.INVERSE_JOIN_COLUMN_UNRESOLVED_NAME;
		}

		@Override
		public String getVirtualUnresolvedNameMessage() {
			return JpaValidationMessages.VIRTUAL_ATTRIBUTE_INVERSE_JOIN_COLUMN_UNRESOLVED_NAME;
		}

		@Override
		public String getUnresolvedReferencedColumnNameMessage() {
			return JpaValidationMessages.INVERSE_JOIN_COLUMN_UNRESOLVED_REFERENCED_COLUMN_NAME;
		}

		@Override
		public String getVirtualUnresolvedReferencedColumnNameMessage() {
			return JpaValidationMessages.VIRTUAL_ATTRIBUTE_INVERSE_JOIN_COLUMN_UNRESOLVED_REFERENCED_COLUMN_NAME;
		}

		@Override
		public String getUnspecifiedNameMultipleJoinColumnsMessage() {
			return JpaValidationMessages.INVERSE_JOIN_COLUMN_NAME_MUST_BE_SPECIFIED_MULTIPLE_JOIN_COLUMNS;
		}

		@Override
		public String getVirtualUnspecifiedNameMultipleJoinColumnsMessage() {
			return JpaValidationMessages.VIRTUAL_ATTRIBUTE_INVERSE_JOIN_COLUMN_NAME_MUST_BE_SPECIFIED_MULTIPLE_JOIN_COLUMNS;
		}

		@Override
		public String getUnspecifiedReferencedColumnNameMultipleJoinColumnsMessage() {
			return JpaValidationMessages.INVERSE_JOIN_COLUMN_REFERENCED_COLUMN_NAME_MUST_BE_SPECIFIED_MULTIPLE_JOIN_COLUMNS;
		}

		@Override
		public String getVirtualUnspecifiedReferencedColumnNameMultipleJoinColumnsMessage() {
			return JpaValidationMessages.VIRTUAL_ATTRIBUTE_INVERSE_JOIN_COLUMN_REFERENCED_COLUMN_NAME_MUST_BE_SPECIFIED_MULTIPLE_JOIN_COLUMNS;
		}
	}

}
