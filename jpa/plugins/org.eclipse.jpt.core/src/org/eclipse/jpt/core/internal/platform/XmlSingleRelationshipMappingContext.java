/*******************************************************************************
 * Copyright (c) 2007 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 * 
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.core.internal.platform;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import org.eclipse.jpt.core.internal.ITypeMapping;
import org.eclipse.jpt.core.internal.content.orm.XmlSingleRelationshipMapping;
import org.eclipse.jpt.core.internal.mappings.IJoinColumn;
import org.eclipse.jpt.core.internal.validation.IJpaValidationMessages;
import org.eclipse.jpt.core.internal.validation.JpaValidationMessages;
import org.eclipse.wst.validation.internal.provisional.core.IMessage;

public abstract class XmlSingleRelationshipMappingContext
	extends XmlRelationshipMappingContext
{
	private Collection<JoinColumnContext> joinColumnContexts;

	protected XmlSingleRelationshipMappingContext(
			IContext parentContext, XmlSingleRelationshipMapping mapping) {
		super(parentContext, mapping);
		this.joinColumnContexts = buildJoinColumnContexts();
	}
	
	protected Collection<JoinColumnContext> buildJoinColumnContexts() {
		Collection<JoinColumnContext> contexts = new ArrayList<JoinColumnContext>();
		for (Iterator<IJoinColumn> i = singleRelationshipMapping().getJoinColumns().iterator(); i.hasNext(); ) {
			contexts.add(new JoinColumnContext(this, i.next()));
		}
		return contexts;
	}

	protected XmlSingleRelationshipMapping singleRelationshipMapping() {
		return (XmlSingleRelationshipMapping) relationshipMapping();
	}

	
	public void refreshDefaults(DefaultsContext defaultsContext) {
		super.refreshDefaults(defaultsContext);
		for (JoinColumnContext context : this.joinColumnContexts) {
			context.refreshDefaults(defaultsContext);
		}
	}
	
	@Override
	protected Object getDefault(String key, DefaultsContext defaultsContext) {
		/**
		 * by default, the join column is in the type mapping's primary table
		 */
		if (key.equals(BaseJpaPlatform.DEFAULT_JOIN_COLUMN_TABLE_KEY)) {
			return singleRelationshipMapping().typeMapping().getTableName();
		}
		return super.getDefault(key, defaultsContext);
	}
	
	@Override
	public void addToMessages(List<IMessage> messages) {
		super.addToMessages(messages);
		
		addJoinColumnMessages(messages);
	}
	
	protected void addJoinColumnMessages(List<IMessage> messages) {
		XmlSingleRelationshipMapping mapping = singleRelationshipMapping();
		ITypeMapping typeMapping = mapping.typeMapping();
		
		for (IJoinColumn joinColumn : mapping.getJoinColumns()) {
			String table = joinColumn.getTable();
			boolean doContinue = joinColumn.isConnected();
			
			if (doContinue && typeMapping.tableNameIsInvalid(table)) {
				if (mapping.isVirtual()) {
					messages.add(
						JpaValidationMessages.buildMessage(
							IMessage.HIGH_SEVERITY,
							IJpaValidationMessages.VIRTUAL_ATTRIBUTE_JOIN_COLUMN_UNRESOLVED_TABLE,
							new String[] {mapping.getPersistentAttribute().getName(), table, joinColumn.getName()},
							joinColumn, joinColumn.getTableTextRange())
					);
				}
				else {
					messages.add(
						JpaValidationMessages.buildMessage(
							IMessage.HIGH_SEVERITY,
							IJpaValidationMessages.JOIN_COLUMN_UNRESOLVED_TABLE,
							new String[] {table, joinColumn.getName()}, 
							joinColumn, joinColumn.getTableTextRange())
					);
				}
				doContinue = false;
			}
			
			if (doContinue && ! joinColumn.isResolved()) {
				if (mapping.isVirtual()) {
					messages.add(
						JpaValidationMessages.buildMessage(
							IMessage.HIGH_SEVERITY,
							IJpaValidationMessages.VIRTUAL_ATTRIBUTE_JOIN_COLUMN_UNRESOLVED_NAME,
							new String[] {mapping.getPersistentAttribute().getName(), joinColumn.getName()}, 
							joinColumn, joinColumn.getNameTextRange())
					);
				}
				else {
					messages.add(
						JpaValidationMessages.buildMessage(
							IMessage.HIGH_SEVERITY,
							IJpaValidationMessages.JOIN_COLUMN_UNRESOLVED_NAME,
							new String[] {joinColumn.getName()}, 
							joinColumn, joinColumn.getNameTextRange())
					);
				}
			}
			
			if (doContinue && ! joinColumn.isReferencedColumnResolved()) {
				if (mapping.isVirtual()) {
					messages.add(
						JpaValidationMessages.buildMessage(
							IMessage.HIGH_SEVERITY,
							IJpaValidationMessages.VIRTUAL_ATTRIBUTE_JOIN_COLUMN_REFERENCED_COLUMN_UNRESOLVED_NAME,
							new String[] {mapping.getPersistentAttribute().getName(), joinColumn.getReferencedColumnName(), joinColumn.getName()}, 
							joinColumn, joinColumn.getReferencedColumnNameTextRange())
					);
				}
				else {
					messages.add(
						JpaValidationMessages.buildMessage(
							IMessage.HIGH_SEVERITY,
							IJpaValidationMessages.JOIN_COLUMN_REFERENCED_COLUMN_UNRESOLVED_NAME,
							new String[] {joinColumn.getReferencedColumnName(), joinColumn.getName()}, 
							joinColumn, joinColumn.getReferencedColumnNameTextRange())
					);
				}
			}
		}
	}
}
