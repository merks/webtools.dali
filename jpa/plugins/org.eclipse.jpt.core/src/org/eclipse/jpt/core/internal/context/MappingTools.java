/*******************************************************************************
 * Copyright (c) 2006, 2009 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License v1.0, which accompanies this distribution and is available at
 * http://www.eclipse.org/legal/epl-v10.html.
 * 
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.core.internal.context;

import java.util.Iterator;
import org.eclipse.jpt.core.context.ColumnMapping;
import org.eclipse.jpt.core.context.Entity;
import org.eclipse.jpt.core.context.JoinColumn;
import org.eclipse.jpt.core.context.JoinTable;
import org.eclipse.jpt.core.context.PersistentAttribute;
import org.eclipse.jpt.core.context.PersistentType;
import org.eclipse.jpt.core.context.RelationshipMapping;
import org.eclipse.jpt.core.context.RelationshipReference;
import org.eclipse.jpt.db.Table;

/**
 * Gather some of the behavior common to the Java and XML models. :-(
 */
public class MappingTools {

	/**
	 * Default join table name from the JPA spec:
	 * 	The concatenated names of the two associated primary
	 * 	entity tables, separated by a underscore.
	 * 
	 * [owning table name]_[target table name]
	 * 
	 * NB: The *names* are concatenated, *not* the *identifiers*.
	 * E.g. the join table for "Foo" and "baR" (where the "delimited" identifier
	 * is required) is
	 *     "Foo_baR"
	 * not
	 *     "Foo"_"baR"
	 * As a result, we cannot honestly calculate the default name without a
	 * database connection.
	 */
	public static String buildJoinTableDefaultName(RelationshipReference relationshipReference) {
		if (relationshipReference.getJpaProject().getDataSource().connectionProfileIsActive()) {
			return buildDbJoinTableDefaultName(relationshipReference);
		}
		// continue with a "best effort":
		String owningTableName = relationshipReference.getTypeMapping().getPrimaryTableName();
		if (owningTableName == null) {
			return null;
		}
		RelationshipMapping relationshipMapping = relationshipReference.getRelationshipMapping();
		if (relationshipMapping == null) {
			return null;
		}
		Entity targetEntity = relationshipMapping.getResolvedTargetEntity();
		if (targetEntity == null) {
			return null;
		}
		String targetTableName = targetEntity.getPrimaryTableName();
		if (targetTableName == null) {
			return null;
		}
		return owningTableName + '_' + targetTableName;
	}

	/**
	 * Use the database to build a more accurate default name.
	 */
	protected static String buildDbJoinTableDefaultName(RelationshipReference relationshipReference) {
		Table owningTable = relationshipReference.getTypeMapping().getPrimaryDbTable();
		if (owningTable == null) {
			return null;
		}
		Entity targetEntity = relationshipReference.getRelationshipMapping().getResolvedTargetEntity();
		if (targetEntity == null) {
			return null;
		}
		Table targetTable = targetEntity.getPrimaryDbTable();
		if (targetTable == null) {
			return null;
		}
		String name = owningTable.getName() + '_' + targetTable.getName();
		return owningTable.getDatabase().convertNameToIdentifier(name);
	}

	/**
	 * Return the join column's default name;
	 * which is typically
	 *     [attribute name]_[referenced column name]
	 * But, if we don't have an attribute name (e.g. in a unidirectional
	 * OneToMany or ManyToMany) is
	 *     [target entity name]_[referenced column name]
	 * 
	 * @see #buildJoinTableDefaultName(RelationshipMapping)
	 */
	public static String buildJoinColumnDefaultName(JoinColumn joinColumn) {
		JoinColumn.Owner owner = joinColumn.getOwner();
		RelationshipMapping relationshipMapping = owner.getRelationshipMapping();
		if (relationshipMapping == null) {
			return null;
		}
		if (owner.joinColumnsSize() != 1) {
			return null;
		}
		String prefix = owner.getAttributeName();
		if (prefix == null) {
			Entity targetEntity = owner.getTargetEntity();
			if (targetEntity == null) {
				return null;
			}
			prefix = targetEntity.getName();
		}
		// not sure which of these is correct...
		// (the spec implies that the referenced column is always the
		// primary key column of the target entity)
		// Column targetColumn = joinColumn.getTargetPrimaryKeyDbColumn();
		String targetColumnName = joinColumn.getReferencedColumnName();
		if (targetColumnName == null) {
			return null;
		}
		String name = prefix + '_' + targetColumnName;
		// not sure which of these is correct...
		// converting the name to an identifier will result in the identifier
		// being delimited nearly every time (at least on non-Sybase/MS
		// databases); but that probably is not the intent of the spec...
		// return targetColumn.getDatabase().convertNameToIdentifier(name);
		return name;
	}

	/**
	 * If appropriate, return the name of the single primary key column of the
	 * target entity.
	 */
	public static String buildJoinColumnDefaultReferencedColumnName(JoinColumn.Owner joinColumnOwner) {
		RelationshipMapping relationshipMapping = joinColumnOwner.getRelationshipMapping();
		if (relationshipMapping == null) {
			return null;
		}
		if (joinColumnOwner.joinColumnsSize() != 1) {
			return null;
		}
		Entity targetEntity = joinColumnOwner.getTargetEntity();
		if (targetEntity == null) {
			return null;
		}
		return targetEntity.getPrimaryKeyColumnName();
	}

	public static ColumnMapping getColumnMapping(String attributeName, PersistentType persistentType) {
		if (attributeName == null || persistentType == null) {
			return null;
		}
		for (Iterator<PersistentAttribute> stream = persistentType.allAttributes(); stream.hasNext(); ) {
			PersistentAttribute persAttribute = stream.next();
			if (attributeName.equals(persAttribute.getName())) {
				if (persAttribute.getMapping() instanceof ColumnMapping) {
					return (ColumnMapping) persAttribute.getMapping();
				}
				// keep looking or return null???
			}
		}
		return null;		
	}

	public static void convertJoinTableDefaultToSpecifiedJoinColumn(JoinTable joinTable) {
		JoinColumn defaultJoinColumn = joinTable.getDefaultJoinColumn();
		if (defaultJoinColumn != null) {
			String columnName = defaultJoinColumn.getDefaultName();
			String referencedColumnName = defaultJoinColumn.getDefaultReferencedColumnName();
			JoinColumn joinColumn = joinTable.addSpecifiedJoinColumn(0);
			joinColumn.setSpecifiedName(columnName);
			joinColumn.setSpecifiedReferencedColumnName(referencedColumnName);
		}
	}

	public static void convertJoinTableDefaultToSpecifiedInverseJoinColumn(JoinTable joinTable) {
		JoinColumn defaultInverseJoinColumn = joinTable.getDefaultInverseJoinColumn();
		if (defaultInverseJoinColumn != null) {
			String columnName = defaultInverseJoinColumn.getDefaultName();
			String referencedColumnName = defaultInverseJoinColumn.getDefaultReferencedColumnName();
			JoinColumn joinColumn = joinTable.addSpecifiedInverseJoinColumn(0);
			joinColumn.setSpecifiedName(columnName);
			joinColumn.setSpecifiedReferencedColumnName(referencedColumnName);
		}
	}

}
