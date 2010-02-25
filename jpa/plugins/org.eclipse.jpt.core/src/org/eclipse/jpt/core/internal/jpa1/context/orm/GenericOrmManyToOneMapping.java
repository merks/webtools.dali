/*******************************************************************************
 * Copyright (c) 2006, 2010 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 * 
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.core.internal.jpa1.context.orm;

import org.eclipse.jpt.core.context.orm.OrmPersistentAttribute;
import org.eclipse.jpt.core.internal.context.orm.AbstractOrmManyToOneMapping;
import org.eclipse.jpt.core.internal.context.orm.GenericOrmManyToOneRelationshipReference;
import org.eclipse.jpt.core.jpa2.context.orm.OrmManyToOneRelationshipReference2_0;
import org.eclipse.jpt.core.resource.orm.XmlManyToOne;

public class GenericOrmManyToOneMapping 
	extends AbstractOrmManyToOneMapping<XmlManyToOne>
{
	public GenericOrmManyToOneMapping(OrmPersistentAttribute parent, XmlManyToOne resourceMapping) {
		super(parent, resourceMapping);
	}
	
	@Override
	protected OrmManyToOneRelationshipReference2_0 buildRelationshipReference() {
		return new GenericOrmManyToOneRelationshipReference(this, this.resourceAttributeMapping);
	}
}
