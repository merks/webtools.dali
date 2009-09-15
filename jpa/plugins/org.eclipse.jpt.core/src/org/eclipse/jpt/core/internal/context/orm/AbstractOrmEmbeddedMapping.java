/*******************************************************************************
 * Copyright (c) 2007, 2009 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 * 
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.core.internal.context.orm;

import org.eclipse.jpt.core.MappingKeys;
import org.eclipse.jpt.core.context.orm.OrmAttributeMapping;
import org.eclipse.jpt.core.context.orm.OrmEmbeddedMapping;
import org.eclipse.jpt.core.context.orm.OrmPersistentAttribute;
import org.eclipse.jpt.core.internal.context.orm.AbstractOrmBaseEmbeddedMapping;
import org.eclipse.jpt.core.resource.orm.Attributes;
import org.eclipse.jpt.core.resource.orm.XmlEmbedded;


public abstract class AbstractOrmEmbeddedMapping<T extends XmlEmbedded>
	extends AbstractOrmBaseEmbeddedMapping<T> 
	implements OrmEmbeddedMapping
{
	
	protected AbstractOrmEmbeddedMapping(OrmPersistentAttribute parent, T resourceMapping) {
		super(parent, resourceMapping);
	}

	public void initializeOn(OrmAttributeMapping newMapping) {
		newMapping.initializeFromOrmEmbeddedMapping(this);
	}

	public int getXmlSequence() {
		return 80;
	}

	public String getKey() {
		return MappingKeys.EMBEDDED_ATTRIBUTE_MAPPING_KEY;
	}

	public void addToResourceModel(Attributes resourceAttributes) {
		resourceAttributes.getEmbeddeds().add(this.resourceAttributeMapping);
	}

	public void removeFromResourceModel(Attributes resourceAttributes) {
		resourceAttributes.getEmbeddeds().remove(this.resourceAttributeMapping);
	}
}
