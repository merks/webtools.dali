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

import java.util.Iterator;
import org.eclipse.jpt.core.context.ColumnMapping;
import org.eclipse.jpt.core.context.Embeddable;
import org.eclipse.jpt.core.context.PersistentAttribute;
import org.eclipse.jpt.core.context.PersistentType;
import org.eclipse.jpt.core.context.java.JavaBaseEmbeddedMapping;
import org.eclipse.jpt.core.context.java.JavaPersistentAttribute;
import org.eclipse.jpt.core.context.orm.OrmAttributeOverrideContainer;
import org.eclipse.jpt.core.context.orm.OrmBaseEmbeddedMapping;
import org.eclipse.jpt.core.context.orm.OrmPersistentAttribute;
import org.eclipse.jpt.core.resource.orm.AbstractXmlEmbedded;
import org.eclipse.jpt.core.resource.orm.XmlColumn;
import org.eclipse.jpt.utility.internal.iterators.EmptyIterator;
import org.eclipse.jpt.utility.internal.iterators.FilteringIterator;
import org.eclipse.jpt.utility.internal.iterators.TransformationIterator;


public abstract class AbstractOrmBaseEmbeddedMapping<T extends AbstractXmlEmbedded> extends AbstractOrmAttributeMapping<T> implements OrmBaseEmbeddedMapping
{
	protected OrmAttributeOverrideContainer attributeOverrideContainer;

	private Embeddable embeddable;//TODO hmm, why no property change notification for setting this??
	
	protected AbstractOrmBaseEmbeddedMapping(OrmPersistentAttribute parent, T resourceMapping) {
		super(parent, resourceMapping);
		this.embeddable = embeddableFor(this.getJavaPersistentAttribute());
		this.attributeOverrideContainer = getXmlContextNodeFactory().buildOrmAttributeOverrideContainer(this, this, this.resourceAttributeMapping);
	}

	@Override
	public void initializeFromOrmBaseEmbeddedMapping(OrmBaseEmbeddedMapping oldMapping) {
		super.initializeFromOrmBaseEmbeddedMapping(oldMapping);
		this.attributeOverrideContainer.initializeFromAttributeOverrideContainer(oldMapping.getAttributeOverrideContainer());
	}

	public OrmAttributeOverrideContainer getAttributeOverrideContainer() {
		return this.attributeOverrideContainer;
	}
	
	
	//************* AttributeOverrideContainer.Owner implementation ********************
	
	public XmlColumn buildVirtualXmlColumn(ColumnMapping overridableColumnMapping) {
		return new VirtualXmlAttributeOverrideColumn(overridableColumnMapping.getColumn());
	}

	public PersistentType getOverridablePersistentType() {
		Embeddable embeddable = getEmbeddable();
		return embeddable == null ? null : embeddable.getPersistentType();
	}

	public Embeddable getEmbeddable() {
		return this.embeddable;
	}
	
	public Iterator<String> allOverridableAttributeNames() {
		return new TransformationIterator<PersistentAttribute, String>(this.allOverridableAttributes()) {
			@Override
			protected String transform(PersistentAttribute attribute) {
				return attribute.getName();
			}
		};
	}

	public Iterator<PersistentAttribute> allOverridableAttributes() {
		if (this.getEmbeddable() == null) {
			return EmptyIterator.instance();
		}
		return new FilteringIterator<PersistentAttribute, PersistentAttribute>(this.getEmbeddable().getPersistentType().attributes()) {
			@Override
			protected boolean accept(PersistentAttribute o) {
				return o.isOverridableAttribute();
			}
		};
	}

	public JavaBaseEmbeddedMapping getJavaEmbeddedMapping() {
		JavaPersistentAttribute jpa = this.getJavaPersistentAttribute();
		if ((jpa != null) && this.valuesAreEqual(jpa.getMappingKey(), this.getKey())) {
			return (JavaBaseEmbeddedMapping) jpa.getMapping();
		}
		return null;
	}

	
	@Override
	public void update() {
		super.update();
		this.embeddable = embeddableFor(this.getJavaPersistentAttribute());
		getAttributeOverrideContainer().update();
	}
	
	//************ static methods ************
	
	public static Embeddable embeddableFor(JavaPersistentAttribute javaPersistentAttribute) {
		return (javaPersistentAttribute == null) ? null : javaPersistentAttribute.getEmbeddable();
	}
}
