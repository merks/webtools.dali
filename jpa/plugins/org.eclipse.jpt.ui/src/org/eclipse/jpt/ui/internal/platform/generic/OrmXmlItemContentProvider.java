/*******************************************************************************
 *  Copyright (c) 2008  Oracle. 
 *  All rights reserved.  This program and the accompanying materials are 
 *  made available under the terms of the Eclipse Public License v1.0 which 
 *  accompanies this distribution, and is available at 
 *  http://www.eclipse.org/legal/epl-v10.html
 *  
 *  Contributors: 
 *  	Oracle - initial API and implementation
 *******************************************************************************/
package org.eclipse.jpt.ui.internal.platform.generic;

import java.util.ListIterator;
import org.eclipse.jpt.core.context.orm.EntityMappings;
import org.eclipse.jpt.core.context.orm.OrmPersistentType;
import org.eclipse.jpt.core.context.orm.OrmXml;
import org.eclipse.jpt.core.context.persistence.PersistenceUnit;
import org.eclipse.jpt.ui.internal.jface.AbstractTreeItemContentProvider;
import org.eclipse.jpt.ui.internal.jface.DelegatingTreeContentAndLabelProvider;
import org.eclipse.jpt.utility.internal.model.value.ListAspectAdapter;
import org.eclipse.jpt.utility.internal.model.value.PropertyAspectAdapter;
import org.eclipse.jpt.utility.model.value.ListValueModel;

public class OrmXmlItemContentProvider
	extends AbstractTreeItemContentProvider<OrmPersistentType>
{
	public OrmXmlItemContentProvider(
			OrmXml ormXml, DelegatingTreeContentAndLabelProvider contentProvider) {
		super(ormXml, contentProvider);
	}
	
	
	@Override
	public PersistenceUnit getParent() {
		return ((OrmXml) model()).persistenceUnit();
	}
	
	@Override
	protected ListValueModel<OrmPersistentType> buildChildrenModel() {
		return new ListAspectAdapter<EntityMappings, OrmPersistentType>(
				new PropertyAspectAdapter<OrmXml, EntityMappings>(
						OrmXml.ENTITY_MAPPINGS_PROPERTY, (OrmXml) model()) {
					@Override
					protected EntityMappings buildValue_() {
						return subject.getEntityMappings();
					}
				},
				EntityMappings.PERSISTENT_TYPES_LIST) {
			@Override
			protected ListIterator<OrmPersistentType> listIterator_() {
				return subject.ormPersistentTypes();
			}
		};
	}
}
