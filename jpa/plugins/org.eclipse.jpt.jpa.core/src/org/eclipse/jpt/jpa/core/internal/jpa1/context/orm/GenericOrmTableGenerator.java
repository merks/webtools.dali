/*******************************************************************************
 * Copyright (c) 2007, 2011 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 *
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.jpa.core.internal.jpa1.context.orm;

import org.eclipse.jpt.common.utility.internal.CollectionTools;
import org.eclipse.jpt.common.utility.internal.StringTools;
import org.eclipse.jpt.common.utility.internal.iterables.EmptyIterable;
import org.eclipse.jpt.common.utility.internal.iterables.ListIterable;
import org.eclipse.jpt.common.utility.internal.iterables.LiveCloneListIterable;
import org.eclipse.jpt.jpa.core.context.Generator;
import org.eclipse.jpt.jpa.core.context.TableGenerator;
import org.eclipse.jpt.jpa.core.context.UniqueConstraint;
import org.eclipse.jpt.jpa.core.context.XmlContextNode;
import org.eclipse.jpt.jpa.core.context.orm.OrmTableGenerator;
import org.eclipse.jpt.jpa.core.context.orm.OrmUniqueConstraint;
import org.eclipse.jpt.jpa.core.internal.context.orm.AbstractOrmGenerator;
import org.eclipse.jpt.jpa.core.resource.orm.OrmFactory;
import org.eclipse.jpt.jpa.core.resource.orm.XmlTableGenerator;
import org.eclipse.jpt.jpa.core.resource.orm.XmlUniqueConstraint;
import org.eclipse.jpt.jpa.db.Schema;
import org.eclipse.jpt.jpa.db.Table;

/**
 * <code>orm.xml</code> table generator
 */
public class GenericOrmTableGenerator
	extends AbstractOrmGenerator<XmlTableGenerator>
	implements OrmTableGenerator, UniqueConstraint.Owner
{
	protected String specifiedTable;
	protected String defaultTable;

	protected String specifiedSchema;
	protected String defaultSchema;

	protected String specifiedCatalog;
	protected String defaultCatalog;

	protected String specifiedPkColumnName;
	protected String defaultPkColumnName;

	protected String specifiedValueColumnName;
	protected String defaultValueColumnName;

	protected String specifiedPkColumnValue;
	protected String defaultPkColumnValue;

	protected final ContextListContainer<OrmUniqueConstraint, XmlUniqueConstraint> uniqueConstraintContainer;


	// ********** constructor **********

	public GenericOrmTableGenerator(XmlContextNode parent, XmlTableGenerator xmlTableGenerator) {
		super(parent, xmlTableGenerator);
		this.specifiedTable = xmlTableGenerator.getTable();
		this.specifiedSchema = xmlTableGenerator.getSchema();
		this.specifiedCatalog = xmlTableGenerator.getCatalog();
		this.specifiedPkColumnName = xmlTableGenerator.getPkColumnName();
		this.specifiedValueColumnName = xmlTableGenerator.getValueColumnName();
		this.specifiedPkColumnValue = xmlTableGenerator.getPkColumnValue();
		this.uniqueConstraintContainer = this.buildUniqueConstraintContainer();
	}


	// ********** synchronize/update **********

	@Override
	public void synchronizeWithResourceModel() {
		super.synchronizeWithResourceModel();
		this.setSpecifiedTable_(this.xmlGenerator.getTable());
		this.setSpecifiedSchema_(this.xmlGenerator.getSchema());
		this.setSpecifiedCatalog_(this.xmlGenerator.getCatalog());
		this.setSpecifiedPkColumnName_(this.xmlGenerator.getPkColumnName());
		this.setSpecifiedValueColumnName_(this.xmlGenerator.getValueColumnName());
		this.setSpecifiedPkColumnValue_(this.xmlGenerator.getPkColumnValue());
		this.syncUniqueConstraints();
	}

	@Override
	public void update() {
		super.update();
		this.setDefaultTable(this.buildDefaultTable());
		this.setDefaultSchema(this.buildDefaultSchema());
		this.setDefaultCatalog(this.buildDefaultCatalog());
		this.setDefaultPkColumnName(this.buildDefaultPkColumnName());
		this.setDefaultValueColumnName(this.buildDefaultValueColumnName());
		this.setDefaultPkColumnValue(this.buildDefaultPkColumnValue());
		this.updateNodes(this.getUniqueConstraints());
	}


	// ********** initial value **********

	@Override
	protected int buildDefaultInitialValue() {
		return DEFAULT_INITIAL_VALUE;
	}
	

	// ********** table **********

	public String getTable() {
		return (this.specifiedTable != null) ? this.specifiedTable : this.defaultTable;
	}

	public String getSpecifiedTable() {
		return this.specifiedTable;
	}

	public void setSpecifiedTable(String table) {
		this.setSpecifiedTable_(table);
		this.xmlGenerator.setTable(table);
	}

	protected void setSpecifiedTable_(String table) {
		String old = this.specifiedTable;
		this.specifiedTable = table;
		this.firePropertyChanged(SPECIFIED_TABLE_PROPERTY, old, table);
	}

	public String getDefaultTable() {
		return this.defaultTable;
	}

	protected void setDefaultTable(String table) {
		String old = this.defaultTable;
		this.defaultTable = table;
		this.firePropertyChanged(DEFAULT_TABLE_PROPERTY, old, table);
	}

	protected String buildDefaultTable() {
		return null; // TODO the default table is determined by the runtime provider...
	}

	public Table getDbTable() {
		Schema dbSchema = this.getDbSchema();
		return (dbSchema == null) ? null : dbSchema.getTableForIdentifier(this.getTable());
	}


	// ********** schema **********

	@Override
	public String getSchema() {
		return (this.specifiedSchema != null) ? this.specifiedSchema : this.defaultSchema;
	}

	public String getSpecifiedSchema() {
		return this.specifiedSchema;
	}

	public void setSpecifiedSchema(String schema) {
		this.setSpecifiedSchema_(schema);
		this.xmlGenerator.setSchema(schema);
	}

	protected void setSpecifiedSchema_(String schema) {
		String old = this.specifiedSchema;
		this.specifiedSchema = schema;
		this.firePropertyChanged(SPECIFIED_SCHEMA_PROPERTY, old, schema);
	}

	public String getDefaultSchema() {
		return this.defaultSchema;
	}

	protected void setDefaultSchema(String schema) {
		String old = this.defaultSchema;
		this.defaultSchema = schema;
		this.firePropertyChanged(DEFAULT_SCHEMA_PROPERTY, old, schema);
	}

	protected String buildDefaultSchema() {
		return this.getContextDefaultSchema();
	}


	// ********** catalog **********

	@Override
	public String getCatalog() {
		return (this.specifiedCatalog != null) ? this.specifiedCatalog : this.defaultCatalog;
	}

	public String getSpecifiedCatalog() {
		return this.specifiedCatalog;
	}

	public void setSpecifiedCatalog(String catalog) {
		this.setSpecifiedCatalog_(catalog);
		this.xmlGenerator.setCatalog(catalog);
	}

	protected void setSpecifiedCatalog_(String catalog) {
		String old = this.specifiedCatalog;
		this.specifiedCatalog = catalog;
		this.firePropertyChanged(SPECIFIED_CATALOG_PROPERTY, old, catalog);
	}

	public String getDefaultCatalog() {
		return this.defaultCatalog;
	}

	protected void setDefaultCatalog(String catalog) {
		String old = this.defaultCatalog;
		this.defaultCatalog = catalog;
		this.firePropertyChanged(DEFAULT_CATALOG_PROPERTY, old, catalog);
	}

	protected String buildDefaultCatalog() {
		return this.getContextDefaultCatalog();
	}


	// ********** primary key column name **********

	public String getPkColumnName() {
		return (this.specifiedPkColumnName != null) ? this.specifiedPkColumnName : this.defaultPkColumnName;
	}

	public String getSpecifiedPkColumnName() {
		return this.specifiedPkColumnName;
	}

	public void setSpecifiedPkColumnName(String name) {
		this.setSpecifiedPkColumnName_(name);
		this.xmlGenerator.setPkColumnName(name);
	}

	protected void setSpecifiedPkColumnName_(String name) {
		String old = this.specifiedPkColumnName;
		this.specifiedPkColumnName = name;
		this.firePropertyChanged(SPECIFIED_PK_COLUMN_NAME_PROPERTY, old, name);
	}

	public String getDefaultPkColumnName() {
		return this.defaultPkColumnName;
	}

	protected void setDefaultPkColumnName(String name) {
		String old = this.defaultPkColumnName;
		this.defaultPkColumnName = name;
		this.firePropertyChanged(DEFAULT_PK_COLUMN_NAME_PROPERTY, old, name);
	}

	protected String buildDefaultPkColumnName() {
		return null; // TODO the default pk column name is determined by the runtime provider...
	}


	// ********** value column name **********

	public String getValueColumnName() {
		return (this.specifiedValueColumnName != null) ? this.specifiedValueColumnName : this.defaultValueColumnName;
	}

	public String getSpecifiedValueColumnName() {
		return this.specifiedValueColumnName;
	}

	public void setSpecifiedValueColumnName(String name) {
		this.setSpecifiedValueColumnName_(name);
		this.xmlGenerator.setValueColumnName(name);
	}

	protected void setSpecifiedValueColumnName_(String name) {
		String old = this.specifiedValueColumnName;
		this.specifiedValueColumnName = name;
		this.firePropertyChanged(SPECIFIED_VALUE_COLUMN_NAME_PROPERTY, old, name);
	}

	public String getDefaultValueColumnName() {
		return this.defaultValueColumnName;
	}

	protected void setDefaultValueColumnName(String name) {
		String old = this.defaultValueColumnName;
		this.defaultValueColumnName = name;
		this.firePropertyChanged(DEFAULT_VALUE_COLUMN_NAME_PROPERTY, old, name);
	}

	protected String buildDefaultValueColumnName() {
		return null; // TODO the default value column name is determined by the runtime provider...
	}


	// ********** primary key column value **********

	public String getPkColumnValue() {
		return (this.specifiedPkColumnValue != null) ? this.specifiedPkColumnValue : this.defaultPkColumnValue;
	}

	public String getSpecifiedPkColumnValue() {
		return this.specifiedPkColumnValue;
	}

	public void setSpecifiedPkColumnValue(String value) {
		this.setSpecifiedPkColumnValue_(value);
		this.xmlGenerator.setPkColumnValue(value);
	}

	protected void setSpecifiedPkColumnValue_(String value) {
		String old = this.specifiedPkColumnValue;
		this.specifiedPkColumnValue = value;
		this.firePropertyChanged(SPECIFIED_PK_COLUMN_VALUE_PROPERTY, old, value);
	}

	public String getDefaultPkColumnValue() {
		return this.defaultPkColumnValue;
	}

	protected void setDefaultPkColumnValue(String value) {
		String old = this.defaultPkColumnValue;
		this.defaultPkColumnValue = value;
		this.firePropertyChanged(DEFAULT_PK_COLUMN_VALUE_PROPERTY, old, value);
	}

	protected String buildDefaultPkColumnValue() {
		return null; // TODO the default pk column value is determined by the runtime provider...
	}


	// ********** unique constraints **********

	public ListIterable<OrmUniqueConstraint> getUniqueConstraints() {
		return this.uniqueConstraintContainer.getContextElements();
	}

	public int getUniqueConstraintsSize() {
		return this.uniqueConstraintContainer.getContextElementsSize();
	}

	public OrmUniqueConstraint addUniqueConstraint() {
		return this.addUniqueConstraint(this.getUniqueConstraintsSize());
	}

	public OrmUniqueConstraint addUniqueConstraint(int index) {
		XmlUniqueConstraint xmlConstraint = this.buildXmlUniqueConstraint();
		OrmUniqueConstraint constraint = this.uniqueConstraintContainer.addContextElement(index, xmlConstraint);
		this.xmlGenerator.getUniqueConstraints().add(index, xmlConstraint);
		return constraint;
	}

	protected XmlUniqueConstraint buildXmlUniqueConstraint() {
		return OrmFactory.eINSTANCE.createXmlUniqueConstraint();
	}

	public void removeUniqueConstraint(UniqueConstraint uniqueConstraint) {
		this.removeUniqueConstraint(this.uniqueConstraintContainer.indexOfContextElement((OrmUniqueConstraint) uniqueConstraint));
	}

	public void removeUniqueConstraint(int index) {
		this.uniqueConstraintContainer.removeContextElement(index);
		this.xmlGenerator.getUniqueConstraints().remove(index);
	}

	public void moveUniqueConstraint(int targetIndex, int sourceIndex) {
		this.uniqueConstraintContainer.moveContextElement(targetIndex, sourceIndex);
		this.xmlGenerator.getUniqueConstraints().move(targetIndex, sourceIndex);
	}

	protected OrmUniqueConstraint buildUniqueConstraint(XmlUniqueConstraint resourceUniqueConstraint) {
		return this.getContextNodeFactory().buildOrmUniqueConstraint(this, this, resourceUniqueConstraint);
	}

	protected void syncUniqueConstraints() {
		this.uniqueConstraintContainer.synchronizeWithResourceModel();
	}

	protected ListIterable<XmlUniqueConstraint> getXmlUniqueConstraints() {
		// clone to reduce chance of concurrency problems
		return new LiveCloneListIterable<XmlUniqueConstraint>(this.xmlGenerator.getUniqueConstraints());
	}

	protected ContextListContainer<OrmUniqueConstraint, XmlUniqueConstraint> buildUniqueConstraintContainer() {
		UniqueConstraintContainer container = new UniqueConstraintContainer();
		container.initialize();
		return container;
	}

	/**
	 * unique constraint container
	 */
	protected class UniqueConstraintContainer
		extends ContextListContainer<OrmUniqueConstraint, XmlUniqueConstraint>
	{
		@Override
		protected String getContextElementsPropertyName() {
			return UNIQUE_CONSTRAINTS_LIST;
		}
		@Override
		protected OrmUniqueConstraint buildContextElement(XmlUniqueConstraint resourceElement) {
			return GenericOrmTableGenerator.this.buildUniqueConstraint(resourceElement);
		}
		@Override
		protected ListIterable<XmlUniqueConstraint> getResourceElements() {
			return GenericOrmTableGenerator.this.getXmlUniqueConstraints();
		}
		@Override
		protected XmlUniqueConstraint getResourceElement(OrmUniqueConstraint contextElement) {
			return contextElement.getXmlUniqueConstraint();
		}
	}


	// ********** UniqueConstraint.Owner implementation **********

	public Iterable<String> getCandidateUniqueConstraintColumnNames() {
		org.eclipse.jpt.jpa.db.Table dbTable = this.getDbTable();
		return (dbTable != null) ? dbTable.getSortedColumnIdentifiers() : EmptyIterable.<String>instance();
	}

	// ********** misc **********

	public Class<TableGenerator> getType() {
		return TableGenerator.class;
	}

	// ********** validation **********

	@Override
	public boolean isIdentical(Generator generator) {
		return super.isIdentical(generator) &&
				StringTools.stringsAreEqual(this.getSpecifiedTable(), (((GenericOrmTableGenerator)generator).getSpecifiedTable())) &&
				StringTools.stringsAreEqual(this.getSpecifiedSchema(), (((GenericOrmTableGenerator)generator).getSpecifiedSchema())) &&
				StringTools.stringsAreEqual(this.getSpecifiedCatalog(), (((GenericOrmTableGenerator)generator).getSpecifiedCatalog())) &&
				StringTools.stringsAreEqual(this.getSpecifiedPkColumnName(), (((GenericOrmTableGenerator)generator).getSpecifiedPkColumnName())) &&
				StringTools.stringsAreEqual(this.getSpecifiedPkColumnValue(), (((GenericOrmTableGenerator)generator).getSpecifiedPkColumnValue())) &&
				StringTools.stringsAreEqual(this.getSpecifiedValueColumnName(), (((GenericOrmTableGenerator)generator).getSpecifiedValueColumnName())) &&
				conversionValuesAreIdentical(((GenericOrmTableGenerator)generator).getUniqueConstraints());
	}

	private boolean conversionValuesAreIdentical(ListIterable<OrmUniqueConstraint> conversionValues) {
		boolean isIdentical = true;
		if (this.getUniqueConstraintsSize() != CollectionTools.size(conversionValues)) {
			return false;
		} else {
			for (int i=0; i<this.getUniqueConstraintsSize(); i++) {
					if (!(CollectionTools.get(this.getUniqueConstraints(), i)).isIdentical(CollectionTools.get(conversionValues, i))) {
						isIdentical = false;
					}
			}
		}
		return isIdentical;
	}
}
