/*******************************************************************************
 *  Copyright (c) 2007 Oracle. 
 *  All rights reserved.  This program and the accompanying materials 
 *  are made available under the terms of the Eclipse Public License v1.0 
 *  which accompanies this distribution, and is available at 
 *  http://www.eclipse.org/legal/epl-v10.html
 *  
 *  Contributors: 
 *  	Oracle - initial API and implementation
 *******************************************************************************/
package org.eclipse.jpt.core.tests.internal.context.orm;

import java.util.Iterator;
import java.util.ListIterator;
import org.eclipse.jdt.core.IType;
import org.eclipse.jpt.core.JptCorePlugin;
import org.eclipse.jpt.core.MappingKeys;
import org.eclipse.jpt.core.context.BasicMapping;
import org.eclipse.jpt.core.context.EmbeddedIdMapping;
import org.eclipse.jpt.core.context.EmbeddedMapping;
import org.eclipse.jpt.core.context.FetchType;
import org.eclipse.jpt.core.context.IdMapping;
import org.eclipse.jpt.core.context.JoinColumn;
import org.eclipse.jpt.core.context.ManyToManyMapping;
import org.eclipse.jpt.core.context.ManyToOneMapping;
import org.eclipse.jpt.core.context.OneToManyMapping;
import org.eclipse.jpt.core.context.OneToOneMapping;
import org.eclipse.jpt.core.context.TransientMapping;
import org.eclipse.jpt.core.context.VersionMapping;
import org.eclipse.jpt.core.context.orm.OrmPersistentAttribute;
import org.eclipse.jpt.core.context.orm.OrmPersistentType;
import org.eclipse.jpt.core.internal.context.orm.GenericOrmJoinColumn;
import org.eclipse.jpt.core.internal.context.orm.GenericOrmOneToOneMapping;
import org.eclipse.jpt.core.internal.context.orm.OrmCascade;
import org.eclipse.jpt.core.resource.java.JPA;
import org.eclipse.jpt.core.resource.orm.XmlOneToOne;
import org.eclipse.jpt.core.resource.persistence.PersistenceFactory;
import org.eclipse.jpt.core.resource.persistence.XmlMappingFileRef;
import org.eclipse.jpt.core.tests.internal.context.ContextModelTestCase;
import org.eclipse.jpt.core.tests.internal.projects.TestJavaProject.SourceWriter;
import org.eclipse.jpt.utility.internal.iterators.ArrayIterator;

public class XmlOneToOneMappingTests extends ContextModelTestCase
{
	public XmlOneToOneMappingTests(String name) {
		super(name);
	}
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		XmlMappingFileRef mappingFileRef = PersistenceFactory.eINSTANCE.createXmlMappingFileRef();
		mappingFileRef.setFileName(JptCorePlugin.DEFAULT_ORM_XML_FILE_PATH);
		xmlPersistenceUnit().getMappingFiles().add(mappingFileRef);
		persistenceResource().save(null);
	}
	
	private void createEntityAnnotation() throws Exception {
		this.createAnnotationAndMembers("Entity", "String name() default \"\";");		
	}
	
	private void createOneToOneAnnotation() throws Exception{
		this.createAnnotationAndMembers("OneToOne", 
			"Class targetEntity() default void.class;" +
			"CascadeType[] cascade() default {};" +
			"FetchType fetch() default EAGER;" +
			"boolean optional() default true;" +		
			"String mappedBy() default \"\";");		
	}
	
	private void createJoinColumnAnnotation() throws Exception{
		this.createAnnotationAndMembers("JoinColumn", 
			"String name() default \"\";" +
			"String referencedColumnName() default \"\";" +
			"boolean unique() default false;" +
			"boolean nullable() default true;" +
			"boolean insertable() default true;" +
			"boolean updatable() default true;" +
			"String columnDefinition() default \"\";" +
			"String table() default \"\";");		
	}
	
	private IType createTestEntityOneToOneMapping() throws Exception {
		createEntityAnnotation();
		createOneToOneAnnotation();
		createJoinColumnAnnotation();
		return this.createTestType(new DefaultAnnotationWriter() {
			@Override
			public Iterator<String> imports() {
				return new ArrayIterator<String>(JPA.ENTITY, JPA.ONE_TO_ONE, JPA.JOIN_COLUMN, JPA.FETCH_TYPE, JPA.CASCADE_TYPE);
			}
			@Override
			public void appendTypeAnnotationTo(StringBuilder sb) {
				sb.append("@Entity");
			}
			
			@Override
			public void appendIdFieldAnnotationTo(StringBuilder sb) {
				sb.append(CR);
				sb.append("    @OneToOne(fetch=FetchType.LAZY, optional=false, targetEntity=Address.class, cascade={CascadeType.ALL, CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE, CascadeType.REFRESH})");
				sb.append(CR);
				sb.append("    @JoinColumn(name=\"MY_COLUMN\", referencedColumnName=\"MY_REFERENCED_COLUMN\", unique=true, nullable=false, insertable=false, updatable=false, columnDefinition=\"COLUMN_DEFINITION\", table=\"MY_TABLE\")");
				sb.append(CR);
				sb.append("    private Address address;").append(CR);
				sb.append(CR);
				sb.append("    @Id");				
			}
		});
	}	
	
	private IType createTestTargetEntityAddress() throws Exception {
		SourceWriter sourceWriter = new SourceWriter() {
			public void appendSourceTo(StringBuilder sb) {
				sb.append(CR);
					sb.append("import ");
					sb.append(JPA.ENTITY);
					sb.append(";");
					sb.append(CR);
					sb.append("import ");
					sb.append(JPA.ID);
					sb.append(";");
					sb.append(CR);
				sb.append(CR);
				sb.append("@Entity");
				sb.append(CR);
				sb.append("public class ").append("Address").append(" ");
				sb.append("{").append(CR);
				sb.append(CR);
				sb.append("    @Id").append(CR);
				sb.append("    private int id;").append(CR);
				sb.append(CR);
				sb.append("    private String city;").append(CR);
				sb.append(CR);
				sb.append("    private String state;").append(CR);
				sb.append(CR);
				sb.append("    private int zip;").append(CR);
				sb.append(CR);
				sb.append("}").append(CR);
		}
		};
		return this.javaProject.createType(PACKAGE_NAME, "Address.java", sourceWriter);
	}	
	public void testUpdateName() throws Exception {
		OrmPersistentType ormPersistentType = entityMappings().addOrmPersistentType(MappingKeys.ENTITY_TYPE_MAPPING_KEY, "model.Foo");
		OrmPersistentAttribute ormPersistentAttribute = ormPersistentType.addSpecifiedPersistentAttribute(MappingKeys.ONE_TO_ONE_ATTRIBUTE_MAPPING_KEY, "oneToOneMapping");
		GenericOrmOneToOneMapping xmlOneToOneMapping = (GenericOrmOneToOneMapping) ormPersistentAttribute.getMapping();
		XmlOneToOne oneToOne = ormResource().getEntityMappings().getEntities().get(0).getAttributes().getOneToOnes().get(0);
		
		assertEquals("oneToOneMapping", xmlOneToOneMapping.getName());
		assertEquals("oneToOneMapping", oneToOne.getName());
				
		//set name in the resource model, verify context model updated
		oneToOne.setName("newName");
		assertEquals("newName", xmlOneToOneMapping.getName());
		assertEquals("newName", oneToOne.getName());
	
		//set name to null in the resource model
		oneToOne.setName(null);
		assertNull(xmlOneToOneMapping.getName());
		assertNull(oneToOne.getName());
	}
	
	public void testModifyName() throws Exception {
		OrmPersistentType ormPersistentType = entityMappings().addOrmPersistentType(MappingKeys.ENTITY_TYPE_MAPPING_KEY, "model.Foo");
		OrmPersistentAttribute ormPersistentAttribute = ormPersistentType.addSpecifiedPersistentAttribute(MappingKeys.ONE_TO_ONE_ATTRIBUTE_MAPPING_KEY, "oneToOneMapping");
		GenericOrmOneToOneMapping xmlOneToOneMapping = (GenericOrmOneToOneMapping) ormPersistentAttribute.getMapping();
		XmlOneToOne oneToOne = ormResource().getEntityMappings().getEntities().get(0).getAttributes().getOneToOnes().get(0);
		
		assertEquals("oneToOneMapping", xmlOneToOneMapping.getName());
		assertEquals("oneToOneMapping", oneToOne.getName());
				
		//set name in the context model, verify resource model updated
		xmlOneToOneMapping.setName("newName");
		assertEquals("newName", xmlOneToOneMapping.getName());
		assertEquals("newName", oneToOne.getName());
	
		//set name to null in the context model
		xmlOneToOneMapping.setName(null);
		assertNull(xmlOneToOneMapping.getName());
		assertNull(oneToOne.getName());
	}
	
	public void testUpdateSpecifiedTargetEntity() throws Exception {
		OrmPersistentType ormPersistentType = entityMappings().addOrmPersistentType(MappingKeys.ENTITY_TYPE_MAPPING_KEY, "model.Foo");
		OrmPersistentAttribute ormPersistentAttribute = ormPersistentType.addSpecifiedPersistentAttribute(MappingKeys.ONE_TO_ONE_ATTRIBUTE_MAPPING_KEY, "oneToOneMapping");
		GenericOrmOneToOneMapping xmlOneToOneMapping = (GenericOrmOneToOneMapping) ormPersistentAttribute.getMapping();
		XmlOneToOne oneToOne = ormResource().getEntityMappings().getEntities().get(0).getAttributes().getOneToOnes().get(0);
		
		assertNull(xmlOneToOneMapping.getSpecifiedTargetEntity());
		assertNull(oneToOne.getTargetEntity());
				
		//set target entity in the resource model, verify context model updated
		oneToOne.setTargetEntity("newTargetEntity");
		assertEquals("newTargetEntity", xmlOneToOneMapping.getSpecifiedTargetEntity());
		assertEquals("newTargetEntity", oneToOne.getTargetEntity());
	
		//set target entity to null in the resource model
		oneToOne.setTargetEntity(null);
		assertNull(xmlOneToOneMapping.getSpecifiedTargetEntity());
		assertNull(oneToOne.getTargetEntity());
	}
	
	public void testModifySpecifiedTargetEntity() throws Exception {
		OrmPersistentType ormPersistentType = entityMappings().addOrmPersistentType(MappingKeys.ENTITY_TYPE_MAPPING_KEY, "model.Foo");
		OrmPersistentAttribute ormPersistentAttribute = ormPersistentType.addSpecifiedPersistentAttribute(MappingKeys.ONE_TO_ONE_ATTRIBUTE_MAPPING_KEY, "oneToOneMapping");
		GenericOrmOneToOneMapping xmlOneToOneMapping = (GenericOrmOneToOneMapping) ormPersistentAttribute.getMapping();
		XmlOneToOne oneToOne = ormResource().getEntityMappings().getEntities().get(0).getAttributes().getOneToOnes().get(0);
		
		assertNull(xmlOneToOneMapping.getSpecifiedTargetEntity());
		assertNull(oneToOne.getTargetEntity());
				
		//set target entity in the context model, verify resource model updated
		xmlOneToOneMapping.setSpecifiedTargetEntity("newTargetEntity");
		assertEquals("newTargetEntity", xmlOneToOneMapping.getSpecifiedTargetEntity());
		assertEquals("newTargetEntity", oneToOne.getTargetEntity());
	
		//set target entity to null in the context model
		xmlOneToOneMapping.setSpecifiedTargetEntity(null);
		assertNull(xmlOneToOneMapping.getSpecifiedTargetEntity());
		assertNull(oneToOne.getTargetEntity());
	}
	
	public void testUpdateSpecifiedFetch() throws Exception {
		OrmPersistentType ormPersistentType = entityMappings().addOrmPersistentType(MappingKeys.ENTITY_TYPE_MAPPING_KEY, "model.Foo");
		OrmPersistentAttribute ormPersistentAttribute = ormPersistentType.addSpecifiedPersistentAttribute(MappingKeys.ONE_TO_ONE_ATTRIBUTE_MAPPING_KEY, "oneToOneMapping");
		GenericOrmOneToOneMapping xmlOneToOneMapping = (GenericOrmOneToOneMapping) ormPersistentAttribute.getMapping();
		XmlOneToOne oneToOneResource = ormResource().getEntityMappings().getEntities().get(0).getAttributes().getOneToOnes().get(0);
		
		assertNull(xmlOneToOneMapping.getSpecifiedFetch());
		assertNull(oneToOneResource.getFetch());
				
		//set fetch in the resource model, verify context model updated
		oneToOneResource.setFetch(org.eclipse.jpt.core.resource.orm.FetchType.EAGER);
		assertEquals(FetchType.EAGER, xmlOneToOneMapping.getSpecifiedFetch());
		assertEquals(org.eclipse.jpt.core.resource.orm.FetchType.EAGER, oneToOneResource.getFetch());
	
		oneToOneResource.setFetch(org.eclipse.jpt.core.resource.orm.FetchType.LAZY);
		assertEquals(FetchType.LAZY, xmlOneToOneMapping.getSpecifiedFetch());
		assertEquals(org.eclipse.jpt.core.resource.orm.FetchType.LAZY, oneToOneResource.getFetch());

		//set fetch to null in the resource model
		oneToOneResource.setFetch(null);
		assertNull(xmlOneToOneMapping.getSpecifiedFetch());
		assertNull(oneToOneResource.getFetch());
	}
	
	public void testModifySpecifiedFetch() throws Exception {
		OrmPersistentType ormPersistentType = entityMappings().addOrmPersistentType(MappingKeys.ENTITY_TYPE_MAPPING_KEY, "model.Foo");
		OrmPersistentAttribute ormPersistentAttribute = ormPersistentType.addSpecifiedPersistentAttribute(MappingKeys.ONE_TO_ONE_ATTRIBUTE_MAPPING_KEY, "oneToOneMapping");
		GenericOrmOneToOneMapping xmlOneToOneMapping = (GenericOrmOneToOneMapping) ormPersistentAttribute.getMapping();
		XmlOneToOne oneToOneResource = ormResource().getEntityMappings().getEntities().get(0).getAttributes().getOneToOnes().get(0);
		
		assertNull(xmlOneToOneMapping.getSpecifiedFetch());
		assertNull(oneToOneResource.getFetch());
				
		//set fetch in the context model, verify resource model updated
		xmlOneToOneMapping.setSpecifiedFetch(FetchType.EAGER);
		assertEquals(org.eclipse.jpt.core.resource.orm.FetchType.EAGER, oneToOneResource.getFetch());
		assertEquals(FetchType.EAGER, xmlOneToOneMapping.getSpecifiedFetch());
	
		xmlOneToOneMapping.setSpecifiedFetch(FetchType.LAZY);
		assertEquals(org.eclipse.jpt.core.resource.orm.FetchType.LAZY, oneToOneResource.getFetch());
		assertEquals(FetchType.LAZY, xmlOneToOneMapping.getSpecifiedFetch());

		//set fetch to null in the context model
		xmlOneToOneMapping.setSpecifiedFetch(null);
		assertNull(oneToOneResource.getFetch());
		assertNull(xmlOneToOneMapping.getSpecifiedFetch());
	}

	public void testUpdateMappedBy() throws Exception {
		OrmPersistentType ormPersistentType = entityMappings().addOrmPersistentType(MappingKeys.ENTITY_TYPE_MAPPING_KEY, "model.Foo");
		OrmPersistentAttribute ormPersistentAttribute = ormPersistentType.addSpecifiedPersistentAttribute(MappingKeys.ONE_TO_ONE_ATTRIBUTE_MAPPING_KEY, "oneToOneMapping");
		GenericOrmOneToOneMapping xmlOneToOneMapping = (GenericOrmOneToOneMapping) ormPersistentAttribute.getMapping();
		XmlOneToOne oneToOne = ormResource().getEntityMappings().getEntities().get(0).getAttributes().getOneToOnes().get(0);
		
		assertNull(xmlOneToOneMapping.getMappedBy());
		assertNull(oneToOne.getMappedBy());
				
		//set mappedBy in the resource model, verify context model updated
		oneToOne.setMappedBy("newMappedBy");
		assertEquals("newMappedBy", xmlOneToOneMapping.getMappedBy());
		assertEquals("newMappedBy", oneToOne.getMappedBy());
	
		//set mappedBy to null in the resource model
		oneToOne.setMappedBy(null);
		assertNull(xmlOneToOneMapping.getMappedBy());
		assertNull(oneToOne.getMappedBy());
	}
	
	public void testModifyMappedBy() throws Exception {
		OrmPersistentType ormPersistentType = entityMappings().addOrmPersistentType(MappingKeys.ENTITY_TYPE_MAPPING_KEY, "model.Foo");
		OrmPersistentAttribute ormPersistentAttribute = ormPersistentType.addSpecifiedPersistentAttribute(MappingKeys.ONE_TO_ONE_ATTRIBUTE_MAPPING_KEY, "oneToOneMapping");
		GenericOrmOneToOneMapping xmlOneToOneMapping = (GenericOrmOneToOneMapping) ormPersistentAttribute.getMapping();
		XmlOneToOne oneToOne = ormResource().getEntityMappings().getEntities().get(0).getAttributes().getOneToOnes().get(0);
		
		assertNull(xmlOneToOneMapping.getMappedBy());
		assertNull(oneToOne.getMappedBy());
				
		//set mappedBy in the context model, verify resource model updated
		xmlOneToOneMapping.setMappedBy("newMappedBy");
		assertEquals("newMappedBy", xmlOneToOneMapping.getMappedBy());
		assertEquals("newMappedBy", oneToOne.getMappedBy());
	
		//set mappedBy to null in the context model
		xmlOneToOneMapping.setMappedBy(null);
		assertNull(xmlOneToOneMapping.getMappedBy());
		assertNull(oneToOne.getMappedBy());
	}
	
	
	public void testUpdateSpecifiedOptional() throws Exception {
		OrmPersistentType ormPersistentType = entityMappings().addOrmPersistentType(MappingKeys.ENTITY_TYPE_MAPPING_KEY, "model.Foo");
		OrmPersistentAttribute ormPersistentAttribute = ormPersistentType.addSpecifiedPersistentAttribute(MappingKeys.ONE_TO_ONE_ATTRIBUTE_MAPPING_KEY, "oneToOneMapping");
		GenericOrmOneToOneMapping xmlOneToOneMapping = (GenericOrmOneToOneMapping) ormPersistentAttribute.getMapping();
		XmlOneToOne oneToOneResource = ormResource().getEntityMappings().getEntities().get(0).getAttributes().getOneToOnes().get(0);
		
		assertNull(xmlOneToOneMapping.getSpecifiedOptional());
		assertNull(oneToOneResource.getOptional());
				
		//set optional in the resource model, verify context model updated
		oneToOneResource.setOptional(Boolean.TRUE);
		assertEquals(Boolean.TRUE, xmlOneToOneMapping.getSpecifiedOptional());
		assertEquals(Boolean.TRUE, oneToOneResource.getOptional());
	
		oneToOneResource.setOptional(Boolean.FALSE);
		assertEquals(Boolean.FALSE, xmlOneToOneMapping.getSpecifiedOptional());
		assertEquals(Boolean.FALSE, oneToOneResource.getOptional());

		//set optional to null in the resource model
		oneToOneResource.setOptional(null);
		assertNull(xmlOneToOneMapping.getSpecifiedOptional());
		assertNull(oneToOneResource.getOptional());
	}
	
	public void testModifySpecifiedOptional() throws Exception {
		OrmPersistentType ormPersistentType = entityMappings().addOrmPersistentType(MappingKeys.ENTITY_TYPE_MAPPING_KEY, "model.Foo");
		OrmPersistentAttribute ormPersistentAttribute = ormPersistentType.addSpecifiedPersistentAttribute(MappingKeys.ONE_TO_ONE_ATTRIBUTE_MAPPING_KEY, "oneToOneMapping");
		GenericOrmOneToOneMapping xmlOneToOneMapping = (GenericOrmOneToOneMapping) ormPersistentAttribute.getMapping();
		XmlOneToOne oneToOneResource = ormResource().getEntityMappings().getEntities().get(0).getAttributes().getOneToOnes().get(0);
		
		assertNull(xmlOneToOneMapping.getSpecifiedOptional());
		assertNull(oneToOneResource.getOptional());
				
		//set optional in the context model, verify resource model updated
		xmlOneToOneMapping.setSpecifiedOptional(Boolean.TRUE);
		assertEquals(Boolean.TRUE, oneToOneResource.getOptional());
		assertEquals(Boolean.TRUE, xmlOneToOneMapping.getSpecifiedOptional());
	
		xmlOneToOneMapping.setSpecifiedOptional(Boolean.FALSE);
		assertEquals(Boolean.FALSE, oneToOneResource.getOptional());
		assertEquals(Boolean.FALSE, xmlOneToOneMapping.getSpecifiedOptional());

		//set optional to null in the context model
		xmlOneToOneMapping.setSpecifiedOptional(null);
		assertNull(oneToOneResource.getOptional());
		assertNull(xmlOneToOneMapping.getSpecifiedOptional());
	}
	
	public void testAddSpecifiedJoinColumn() throws Exception {
		OrmPersistentType ormPersistentType = entityMappings().addOrmPersistentType(MappingKeys.ENTITY_TYPE_MAPPING_KEY, "model.Foo");
		OrmPersistentAttribute ormPersistentAttribute = ormPersistentType.addSpecifiedPersistentAttribute(MappingKeys.ONE_TO_ONE_ATTRIBUTE_MAPPING_KEY, "oneToOneMapping");
		GenericOrmOneToOneMapping xmlOneToOneMapping = (GenericOrmOneToOneMapping) ormPersistentAttribute.getMapping();
		XmlOneToOne oneToOneResource = ormResource().getEntityMappings().getEntities().get(0).getAttributes().getOneToOnes().get(0);
		
		GenericOrmJoinColumn joinColumn = xmlOneToOneMapping.addSpecifiedJoinColumn(0);
		ormResource().save(null);
		joinColumn.setSpecifiedName("FOO");
		ormResource().save(null);
				
		assertEquals("FOO", oneToOneResource.getJoinColumns().get(0).getName());
		
		GenericOrmJoinColumn joinColumn2 = xmlOneToOneMapping.addSpecifiedJoinColumn(0);
		ormResource().save(null);
		joinColumn2.setSpecifiedName("BAR");
		ormResource().save(null);
		
		assertEquals("BAR", oneToOneResource.getJoinColumns().get(0).getName());
		assertEquals("FOO", oneToOneResource.getJoinColumns().get(1).getName());
		
		GenericOrmJoinColumn joinColumn3 = xmlOneToOneMapping.addSpecifiedJoinColumn(1);
		ormResource().save(null);
		joinColumn3.setSpecifiedName("BAZ");
		ormResource().save(null);
		
		assertEquals("BAR", oneToOneResource.getJoinColumns().get(0).getName());
		assertEquals("BAZ", oneToOneResource.getJoinColumns().get(1).getName());
		assertEquals("FOO", oneToOneResource.getJoinColumns().get(2).getName());
		
		ListIterator<GenericOrmJoinColumn> joinColumns = xmlOneToOneMapping.specifiedJoinColumns();
		assertEquals(joinColumn2, joinColumns.next());
		assertEquals(joinColumn3, joinColumns.next());
		assertEquals(joinColumn, joinColumns.next());
		
		joinColumns = xmlOneToOneMapping.specifiedJoinColumns();
		assertEquals("BAR", joinColumns.next().getName());
		assertEquals("BAZ", joinColumns.next().getName());
		assertEquals("FOO", joinColumns.next().getName());
	}
	
	public void testRemoveSpecifiedJoinColumn() throws Exception {
		OrmPersistentType ormPersistentType = entityMappings().addOrmPersistentType(MappingKeys.ENTITY_TYPE_MAPPING_KEY, "model.Foo");
		OrmPersistentAttribute ormPersistentAttribute = ormPersistentType.addSpecifiedPersistentAttribute(MappingKeys.ONE_TO_ONE_ATTRIBUTE_MAPPING_KEY, "oneToOneMapping");
		GenericOrmOneToOneMapping xmlOneToOneMapping = (GenericOrmOneToOneMapping) ormPersistentAttribute.getMapping();
		XmlOneToOne oneToOneResource = ormResource().getEntityMappings().getEntities().get(0).getAttributes().getOneToOnes().get(0);

		xmlOneToOneMapping.addSpecifiedJoinColumn(0).setSpecifiedName("FOO");
		xmlOneToOneMapping.addSpecifiedJoinColumn(1).setSpecifiedName("BAR");
		xmlOneToOneMapping.addSpecifiedJoinColumn(2).setSpecifiedName("BAZ");
		
		assertEquals(3, oneToOneResource.getJoinColumns().size());
		
		xmlOneToOneMapping.removeSpecifiedJoinColumn(0);
		assertEquals(2, oneToOneResource.getJoinColumns().size());
		assertEquals("BAR", oneToOneResource.getJoinColumns().get(0).getName());
		assertEquals("BAZ", oneToOneResource.getJoinColumns().get(1).getName());

		xmlOneToOneMapping.removeSpecifiedJoinColumn(0);
		assertEquals(1, oneToOneResource.getJoinColumns().size());
		assertEquals("BAZ", oneToOneResource.getJoinColumns().get(0).getName());
		
		xmlOneToOneMapping.removeSpecifiedJoinColumn(0);
		assertEquals(0, oneToOneResource.getJoinColumns().size());
	}
	
	public void testMoveSpecifiedJoinColumn() throws Exception {
		OrmPersistentType ormPersistentType = entityMappings().addOrmPersistentType(MappingKeys.ENTITY_TYPE_MAPPING_KEY, "model.Foo");
		OrmPersistentAttribute ormPersistentAttribute = ormPersistentType.addSpecifiedPersistentAttribute(MappingKeys.ONE_TO_ONE_ATTRIBUTE_MAPPING_KEY, "oneToOneMapping");
		GenericOrmOneToOneMapping xmlOneToOneMapping = (GenericOrmOneToOneMapping) ormPersistentAttribute.getMapping();
		XmlOneToOne oneToOneResource = ormResource().getEntityMappings().getEntities().get(0).getAttributes().getOneToOnes().get(0);

		xmlOneToOneMapping.addSpecifiedJoinColumn(0).setSpecifiedName("FOO");
		xmlOneToOneMapping.addSpecifiedJoinColumn(1).setSpecifiedName("BAR");
		xmlOneToOneMapping.addSpecifiedJoinColumn(2).setSpecifiedName("BAZ");
		
		assertEquals(3, oneToOneResource.getJoinColumns().size());
		
		
		xmlOneToOneMapping.moveSpecifiedJoinColumn(2, 0);
		ListIterator<GenericOrmJoinColumn> joinColumns = xmlOneToOneMapping.specifiedJoinColumns();
		assertEquals("BAR", joinColumns.next().getName());
		assertEquals("BAZ", joinColumns.next().getName());
		assertEquals("FOO", joinColumns.next().getName());

		assertEquals("BAR", oneToOneResource.getJoinColumns().get(0).getName());
		assertEquals("BAZ", oneToOneResource.getJoinColumns().get(1).getName());
		assertEquals("FOO", oneToOneResource.getJoinColumns().get(2).getName());


		xmlOneToOneMapping.moveSpecifiedJoinColumn(0, 1);
		joinColumns = xmlOneToOneMapping.specifiedJoinColumns();
		assertEquals("BAZ", joinColumns.next().getName());
		assertEquals("BAR", joinColumns.next().getName());
		assertEquals("FOO", joinColumns.next().getName());

		assertEquals("BAZ", oneToOneResource.getJoinColumns().get(0).getName());
		assertEquals("BAR", oneToOneResource.getJoinColumns().get(1).getName());
		assertEquals("FOO", oneToOneResource.getJoinColumns().get(2).getName());
	}
	
	public void testOneToOneMappingNoUnderylingJavaAttribute() throws Exception {
		createTestEntityOneToOneMapping();
		createTestTargetEntityAddress();

		OrmPersistentType ormPersistentType = entityMappings().addOrmPersistentType(MappingKeys.ENTITY_TYPE_MAPPING_KEY, FULLY_QUALIFIED_TYPE_NAME);
		entityMappings().addOrmPersistentType(MappingKeys.ENTITY_TYPE_MAPPING_KEY, PACKAGE_NAME + ".Address");
		ormPersistentType.addSpecifiedPersistentAttribute(MappingKeys.ONE_TO_ONE_ATTRIBUTE_MAPPING_KEY, "foo");
		assertEquals(3, ormPersistentType.virtualAttributesSize());
		
		OrmPersistentAttribute ormPersistentAttribute = ormPersistentType.specifiedAttributes().next();
		GenericOrmOneToOneMapping xmlOneToOneMapping = (GenericOrmOneToOneMapping) ormPersistentAttribute.getMapping();
		
		assertEquals("foo", xmlOneToOneMapping.getName());

		assertNull(xmlOneToOneMapping.getSpecifiedFetch());
		assertNull(xmlOneToOneMapping.getSpecifiedOptional());
		assertNull(xmlOneToOneMapping.getSpecifiedTargetEntity());
		assertEquals(FetchType.EAGER, xmlOneToOneMapping.getFetch());
		assertEquals(Boolean.TRUE, xmlOneToOneMapping.getOptional());
		assertNull(xmlOneToOneMapping.getTargetEntity());

		
		assertFalse(xmlOneToOneMapping.specifiedJoinColumns().hasNext());
		//TODO default joinColumns
		//assertTrue(xmlOneToOneMapping.defaultJoinColumns().hasNext());
	
	
		OrmCascade xmlCascade = xmlOneToOneMapping.getCascade();
		assertFalse(xmlCascade.isAll());
		assertFalse(xmlCascade.isMerge());
		assertFalse(xmlCascade.isPersist());
		assertFalse(xmlCascade.isRemove());
		assertFalse(xmlCascade.isRefresh());
	}
	
	
	public void testVirtualMappingMetadataCompleteFalse() throws Exception {
		createTestEntityOneToOneMapping();
		createTestTargetEntityAddress();

		OrmPersistentType ormPersistentType = entityMappings().addOrmPersistentType(MappingKeys.ENTITY_TYPE_MAPPING_KEY, FULLY_QUALIFIED_TYPE_NAME);
		entityMappings().addOrmPersistentType(MappingKeys.ENTITY_TYPE_MAPPING_KEY, PACKAGE_NAME + ".Address");
		assertEquals(3, ormPersistentType.virtualAttributesSize());		
		OrmPersistentAttribute ormPersistentAttribute = ormPersistentType.virtualAttributes().next();
		
		GenericOrmOneToOneMapping xmlOneToOneMapping = (GenericOrmOneToOneMapping) ormPersistentAttribute.getMapping();	
		assertEquals("address", xmlOneToOneMapping.getName());
		assertEquals(FetchType.LAZY, xmlOneToOneMapping.getSpecifiedFetch());
		assertEquals(Boolean.FALSE, xmlOneToOneMapping.getSpecifiedOptional());
		assertEquals("Address", xmlOneToOneMapping.getSpecifiedTargetEntity());
		assertNull(xmlOneToOneMapping.getMappedBy());

		GenericOrmJoinColumn xmlJoinColumn = xmlOneToOneMapping.specifiedJoinColumns().next();
		assertEquals("MY_COLUMN", xmlJoinColumn.getSpecifiedName());
		assertEquals("MY_REFERENCED_COLUMN", xmlJoinColumn.getSpecifiedReferencedColumnName());
		assertEquals(Boolean.TRUE, xmlJoinColumn.getSpecifiedUnique());
		assertEquals(Boolean.FALSE, xmlJoinColumn.getSpecifiedNullable());
		assertEquals(Boolean.FALSE, xmlJoinColumn.getSpecifiedInsertable());
		assertEquals(Boolean.FALSE, xmlJoinColumn.getSpecifiedUpdatable());
		assertEquals("COLUMN_DEFINITION", xmlJoinColumn.getColumnDefinition());
		assertEquals("MY_TABLE", xmlJoinColumn.getSpecifiedTable());

		OrmCascade xmlCascade = xmlOneToOneMapping.getCascade();
		assertTrue(xmlCascade.isAll());
		assertTrue(xmlCascade.isMerge());
		assertTrue(xmlCascade.isPersist());
		assertTrue(xmlCascade.isRemove());
		assertTrue(xmlCascade.isRefresh());
	}
	
	public void testVirtualMappingMetadataCompleteTrue() throws Exception {
		createTestEntityOneToOneMapping();
		createTestTargetEntityAddress();

		OrmPersistentType ormPersistentType = entityMappings().addOrmPersistentType(MappingKeys.ENTITY_TYPE_MAPPING_KEY, FULLY_QUALIFIED_TYPE_NAME);
		entityMappings().addOrmPersistentType(MappingKeys.ENTITY_TYPE_MAPPING_KEY, PACKAGE_NAME + ".Address");
		ormPersistentType.getMapping().setSpecifiedMetadataComplete(Boolean.TRUE);
		assertEquals(3, ormPersistentType.virtualAttributesSize());		
		OrmPersistentAttribute ormPersistentAttribute = ormPersistentType.virtualAttributes().next();
		
		GenericOrmOneToOneMapping xmlOneToOneMapping = (GenericOrmOneToOneMapping) ormPersistentAttribute.getMapping();	
		assertEquals("address", xmlOneToOneMapping.getName());
		assertEquals(FetchType.EAGER, xmlOneToOneMapping.getSpecifiedFetch());
		assertEquals(Boolean.TRUE, xmlOneToOneMapping.getSpecifiedOptional());
		//TODO hmm, is this correct?
		assertEquals("test.Address", xmlOneToOneMapping.getSpecifiedTargetEntity());
		assertNull(xmlOneToOneMapping.getMappedBy());

		//TODO default join columns in xml one-to-one
//		XmlJoinColumn xmlJoinColumn = xmlOneToOneMapping.specifiedJoinColumns().next();
//		//TODO java default columns name in JavaSingleRelationshipMapping.JoinColumnOwner
//		//assertEquals("address", xmlJoinColumn.getSpecifiedName());
//		//assertEquals("address", xmlJoinColumn.getSpecifiedReferencedColumnName());
//		assertEquals(Boolean.FALSE, xmlJoinColumn.getSpecifiedUnique());
//		assertEquals(Boolean.TRUE, xmlJoinColumn.getSpecifiedNullable());
//		assertEquals(Boolean.TRUE, xmlJoinColumn.getSpecifiedInsertable());
//		assertEquals(Boolean.TRUE, xmlJoinColumn.getSpecifiedUpdatable());
//		assertNull(xmlJoinColumn.getColumnDefinition());
//		assertEquals(TYPE_NAME, xmlJoinColumn.getSpecifiedTable());

		OrmCascade xmlCascade = xmlOneToOneMapping.getCascade();
		assertFalse(xmlCascade.isAll());
		assertFalse(xmlCascade.isMerge());
		assertFalse(xmlCascade.isPersist());
		assertFalse(xmlCascade.isRemove());
		assertFalse(xmlCascade.isRefresh());
	}
	
	public void testSpecifiedMapping() throws Exception {
		createTestEntityOneToOneMapping();
		createTestTargetEntityAddress();

		OrmPersistentType ormPersistentType = entityMappings().addOrmPersistentType(MappingKeys.ENTITY_TYPE_MAPPING_KEY, FULLY_QUALIFIED_TYPE_NAME);
		entityMappings().addOrmPersistentType(MappingKeys.ENTITY_TYPE_MAPPING_KEY, PACKAGE_NAME + ".Address");

		ormPersistentType.addSpecifiedPersistentAttribute(MappingKeys.ONE_TO_ONE_ATTRIBUTE_MAPPING_KEY, "address");
		assertEquals(2, ormPersistentType.virtualAttributesSize());
		
		OrmPersistentAttribute ormPersistentAttribute = ormPersistentType.specifiedAttributes().next();
		GenericOrmOneToOneMapping xmlOneToOneMapping = (GenericOrmOneToOneMapping) ormPersistentAttribute.getMapping();
		
		assertEquals("address", xmlOneToOneMapping.getName());
		assertNull(xmlOneToOneMapping.getSpecifiedFetch());
		assertNull(xmlOneToOneMapping.getSpecifiedOptional());
		assertNull(xmlOneToOneMapping.getSpecifiedTargetEntity());
		assertNull(xmlOneToOneMapping.getMappedBy());
		assertEquals(FetchType.EAGER, xmlOneToOneMapping.getFetch());
		assertEquals(Boolean.TRUE, xmlOneToOneMapping.getOptional());
		//TODO default target entity in xml
		//assertEquals("test.Address", xmlOneToOneMapping.getDefaultTargetEntity());
		
		assertFalse(xmlOneToOneMapping.specifiedJoinColumns().hasNext());
		
		//TODO default join columns for specified xmlOneToOne mapping
//		XmlJoinColumn xmlJoinColumn = xmlOneToOneMapping.defaultJoinColumns().next();
//		assertNull(xmlJoinColumn.getSpecifiedName());
//		assertNull(xmlJoinColumn.getSpecifiedReferencedColumnName());
//		assertNull(xmlJoinColumn.getSpecifiedUnique());
//		assertNull(xmlJoinColumn.getSpecifiedNullable());
//		assertNull(xmlJoinColumn.getSpecifiedInsertable());
//		assertNull(xmlJoinColumn.getSpecifiedUpdatable());
//		assertNull(xmlJoinColumn.getColumnDefinition());
//		assertNull(xmlJoinColumn.getSpecifiedTable());
//		
//		assertEquals("address", xmlJoinColumn.getDefaultName());
//		assertEquals("address", xmlJoinColumn.getDefaultReferencedColumnName());
//		assertEquals(Boolean.FALSE, xmlJoinColumn.getDefaultUnique());
//		assertEquals(Boolean.TRUE, xmlJoinColumn.getDefaultNullable());
//		assertEquals(Boolean.TRUE, xmlJoinColumn.getDefaultInsertable());
//		assertEquals(Boolean.TRUE, xmlJoinColumn.getDefaultUpdatable());
//		assertEquals(null, xmlJoinColumn.getColumnDefinition());
//		assertEquals(TYPE_NAME, xmlJoinColumn.getDefaultTable());

		OrmCascade xmlCascade = xmlOneToOneMapping.getCascade();
		assertFalse(xmlCascade.isAll());
		assertFalse(xmlCascade.isMerge());
		assertFalse(xmlCascade.isPersist());
		assertFalse(xmlCascade.isRemove());
		assertFalse(xmlCascade.isRefresh());
	}
	
	
	public void testOneToOneMorphToIdMapping() throws Exception {
		OrmPersistentType ormPersistentType = entityMappings().addOrmPersistentType(MappingKeys.ENTITY_TYPE_MAPPING_KEY, FULLY_QUALIFIED_TYPE_NAME);
		OrmPersistentAttribute ormPersistentAttribute = ormPersistentType.addSpecifiedPersistentAttribute(MappingKeys.ONE_TO_ONE_ATTRIBUTE_MAPPING_KEY, "oneToOne");
		
		OneToOneMapping oneToOneMapping = (OneToOneMapping) ormPersistentAttribute.getMapping();
		assertFalse(oneToOneMapping.isDefault());
		oneToOneMapping.setSpecifiedFetch(FetchType.EAGER);
		oneToOneMapping.setSpecifiedTargetEntity("TargetEntity");
		oneToOneMapping.setMappedBy("mappedBy");
		oneToOneMapping.getCascade().setAll(true);
		oneToOneMapping.getCascade().setMerge(true);
		oneToOneMapping.getCascade().setPersist(true);
		oneToOneMapping.getCascade().setRefresh(true);
		oneToOneMapping.getCascade().setRemove(true);
		JoinColumn joinColumn = oneToOneMapping.addSpecifiedJoinColumn(0);
		joinColumn.setSpecifiedName("name");
		joinColumn.setSpecifiedReferencedColumnName("referenceName");
		assertFalse(oneToOneMapping.isDefault());	
		
		ormPersistentAttribute.setSpecifiedMappingKey(MappingKeys.ID_ATTRIBUTE_MAPPING_KEY);
		assertEquals(1, ormPersistentType.specifiedAttributesSize());
		assertEquals(ormPersistentAttribute, ormPersistentType.specifiedAttributes().next());
		assertTrue(ormPersistentAttribute.getMapping() instanceof IdMapping);
		assertEquals("oneToOne", ormPersistentAttribute.getMapping().getName());
	}
	
	public void testOneToOneMorphToVersionMapping() throws Exception {
		OrmPersistentType ormPersistentType = entityMappings().addOrmPersistentType(MappingKeys.ENTITY_TYPE_MAPPING_KEY, FULLY_QUALIFIED_TYPE_NAME);
		OrmPersistentAttribute ormPersistentAttribute = ormPersistentType.addSpecifiedPersistentAttribute(MappingKeys.ONE_TO_ONE_ATTRIBUTE_MAPPING_KEY, "oneToOne");
		
		OneToOneMapping oneToOneMapping = (OneToOneMapping) ormPersistentAttribute.getMapping();
		assertFalse(oneToOneMapping.isDefault());
		oneToOneMapping.setSpecifiedFetch(FetchType.EAGER);
		oneToOneMapping.setSpecifiedTargetEntity("TargetEntity");
		oneToOneMapping.setMappedBy("mappedBy");
		oneToOneMapping.getCascade().setAll(true);
		oneToOneMapping.getCascade().setMerge(true);
		oneToOneMapping.getCascade().setPersist(true);
		oneToOneMapping.getCascade().setRefresh(true);
		oneToOneMapping.getCascade().setRemove(true);
		JoinColumn joinColumn = oneToOneMapping.addSpecifiedJoinColumn(0);
		joinColumn.setSpecifiedName("name");
		joinColumn.setSpecifiedReferencedColumnName("referenceName");
		assertFalse(oneToOneMapping.isDefault());	
		
		ormPersistentAttribute.setSpecifiedMappingKey(MappingKeys.VERSION_ATTRIBUTE_MAPPING_KEY);
		assertEquals(1, ormPersistentType.specifiedAttributesSize());
		assertEquals(ormPersistentAttribute, ormPersistentType.specifiedAttributes().next());
		assertTrue(ormPersistentAttribute.getMapping() instanceof VersionMapping);
		assertEquals("oneToOne", ormPersistentAttribute.getMapping().getName());
	}
	
	public void testOneToOneMorphToTransientMapping() throws Exception {
		OrmPersistentType ormPersistentType = entityMappings().addOrmPersistentType(MappingKeys.ENTITY_TYPE_MAPPING_KEY, FULLY_QUALIFIED_TYPE_NAME);
		OrmPersistentAttribute ormPersistentAttribute = ormPersistentType.addSpecifiedPersistentAttribute(MappingKeys.ONE_TO_ONE_ATTRIBUTE_MAPPING_KEY, "oneToOne");

		OneToOneMapping oneToOneMapping = (OneToOneMapping) ormPersistentAttribute.getMapping();
		assertFalse(oneToOneMapping.isDefault());
		oneToOneMapping.setSpecifiedFetch(FetchType.EAGER);
		oneToOneMapping.setSpecifiedTargetEntity("TargetEntity");
		oneToOneMapping.setMappedBy("mappedBy");
		oneToOneMapping.getCascade().setAll(true);
		oneToOneMapping.getCascade().setMerge(true);
		oneToOneMapping.getCascade().setPersist(true);
		oneToOneMapping.getCascade().setRefresh(true);
		oneToOneMapping.getCascade().setRemove(true);
		JoinColumn joinColumn = oneToOneMapping.addSpecifiedJoinColumn(0);
		joinColumn.setSpecifiedName("name");
		joinColumn.setSpecifiedReferencedColumnName("referenceName");
		assertFalse(oneToOneMapping.isDefault());	
		
		ormPersistentAttribute.setSpecifiedMappingKey(MappingKeys.TRANSIENT_ATTRIBUTE_MAPPING_KEY);
		assertEquals(1, ormPersistentType.specifiedAttributesSize());
		assertEquals(ormPersistentAttribute, ormPersistentType.specifiedAttributes().next());
		assertTrue(ormPersistentAttribute.getMapping() instanceof TransientMapping);
		assertEquals("oneToOne", ormPersistentAttribute.getMapping().getName());
	}
	
	public void testOneToOneMorphToEmbeddedMapping() throws Exception {
		OrmPersistentType ormPersistentType = entityMappings().addOrmPersistentType(MappingKeys.ENTITY_TYPE_MAPPING_KEY, FULLY_QUALIFIED_TYPE_NAME);
		OrmPersistentAttribute ormPersistentAttribute = ormPersistentType.addSpecifiedPersistentAttribute(MappingKeys.ONE_TO_ONE_ATTRIBUTE_MAPPING_KEY, "oneToOne");

		OneToOneMapping oneToOneMapping = (OneToOneMapping) ormPersistentAttribute.getMapping();
		assertFalse(oneToOneMapping.isDefault());
		oneToOneMapping.setSpecifiedFetch(FetchType.EAGER);
		oneToOneMapping.setSpecifiedTargetEntity("TargetEntity");
		oneToOneMapping.setMappedBy("mappedBy");
		oneToOneMapping.getCascade().setAll(true);
		oneToOneMapping.getCascade().setMerge(true);
		oneToOneMapping.getCascade().setPersist(true);
		oneToOneMapping.getCascade().setRefresh(true);
		oneToOneMapping.getCascade().setRemove(true);
		JoinColumn joinColumn = oneToOneMapping.addSpecifiedJoinColumn(0);
		joinColumn.setSpecifiedName("name");
		joinColumn.setSpecifiedReferencedColumnName("referenceName");
		assertFalse(oneToOneMapping.isDefault());	
		
		ormPersistentAttribute.setSpecifiedMappingKey(MappingKeys.EMBEDDED_ATTRIBUTE_MAPPING_KEY);
		assertEquals(1, ormPersistentType.specifiedAttributesSize());
		assertEquals(ormPersistentAttribute, ormPersistentType.specifiedAttributes().next());
		assertTrue(ormPersistentAttribute.getMapping() instanceof EmbeddedMapping);
		assertEquals("oneToOne", ormPersistentAttribute.getMapping().getName());
	}
	
	public void testOneToOneMorphToEmbeddedIdMapping() throws Exception {
		OrmPersistentType ormPersistentType = entityMappings().addOrmPersistentType(MappingKeys.ENTITY_TYPE_MAPPING_KEY, FULLY_QUALIFIED_TYPE_NAME);
		OrmPersistentAttribute ormPersistentAttribute = ormPersistentType.addSpecifiedPersistentAttribute(MappingKeys.ONE_TO_ONE_ATTRIBUTE_MAPPING_KEY, "oneToOne");
		
		OneToOneMapping oneToOneMapping = (OneToOneMapping) ormPersistentAttribute.getMapping();
		assertFalse(oneToOneMapping.isDefault());
		oneToOneMapping.setSpecifiedFetch(FetchType.EAGER);
		oneToOneMapping.setSpecifiedTargetEntity("TargetEntity");
		oneToOneMapping.setMappedBy("mappedBy");
		oneToOneMapping.getCascade().setAll(true);
		oneToOneMapping.getCascade().setMerge(true);
		oneToOneMapping.getCascade().setPersist(true);
		oneToOneMapping.getCascade().setRefresh(true);
		oneToOneMapping.getCascade().setRemove(true);
		JoinColumn joinColumn = oneToOneMapping.addSpecifiedJoinColumn(0);
		joinColumn.setSpecifiedName("name");
		joinColumn.setSpecifiedReferencedColumnName("referenceName");
		assertFalse(oneToOneMapping.isDefault());	
		
		ormPersistentAttribute.setSpecifiedMappingKey(MappingKeys.EMBEDDED_ID_ATTRIBUTE_MAPPING_KEY);
		assertEquals(1, ormPersistentType.specifiedAttributesSize());
		assertEquals(ormPersistentAttribute, ormPersistentType.specifiedAttributes().next());
		assertTrue(ormPersistentAttribute.getMapping() instanceof EmbeddedIdMapping);
		assertEquals("oneToOne", ormPersistentAttribute.getMapping().getName());
	}
	
	public void testOneToOneMorphToManyToManyMapping() throws Exception {
		OrmPersistentType ormPersistentType = entityMappings().addOrmPersistentType(MappingKeys.ENTITY_TYPE_MAPPING_KEY, FULLY_QUALIFIED_TYPE_NAME);
		OrmPersistentAttribute ormPersistentAttribute = ormPersistentType.addSpecifiedPersistentAttribute(MappingKeys.ONE_TO_ONE_ATTRIBUTE_MAPPING_KEY, "oneToOne");
		
		OneToOneMapping oneToOneMapping = (OneToOneMapping) ormPersistentAttribute.getMapping();
		assertFalse(oneToOneMapping.isDefault());
		oneToOneMapping.setSpecifiedFetch(FetchType.EAGER);
		oneToOneMapping.setSpecifiedTargetEntity("TargetEntity");
		oneToOneMapping.setMappedBy("mappedBy");
		oneToOneMapping.getCascade().setAll(true);
		oneToOneMapping.getCascade().setMerge(true);
		oneToOneMapping.getCascade().setPersist(true);
		oneToOneMapping.getCascade().setRefresh(true);
		oneToOneMapping.getCascade().setRemove(true);
		JoinColumn joinColumn = oneToOneMapping.addSpecifiedJoinColumn(0);
		joinColumn.setSpecifiedName("name");
		joinColumn.setSpecifiedReferencedColumnName("referenceName");
		assertFalse(oneToOneMapping.isDefault());	
		
		ormPersistentAttribute.setSpecifiedMappingKey(MappingKeys.MANY_TO_MANY_ATTRIBUTE_MAPPING_KEY);
		assertEquals(1, ormPersistentType.specifiedAttributesSize());
		assertEquals(ormPersistentAttribute, ormPersistentType.specifiedAttributes().next());
		assertTrue(ormPersistentAttribute.getMapping() instanceof ManyToManyMapping);
		assertEquals("oneToOne", ormPersistentAttribute.getMapping().getName());
		assertEquals(FetchType.EAGER, ((ManyToManyMapping) ormPersistentAttribute.getMapping()).getSpecifiedFetch());
		assertEquals("TargetEntity", ((ManyToManyMapping) ormPersistentAttribute.getMapping()).getSpecifiedTargetEntity());
		assertEquals("mappedBy", ((ManyToManyMapping) ormPersistentAttribute.getMapping()).getMappedBy());
		assertTrue(((ManyToManyMapping) ormPersistentAttribute.getMapping()).getCascade().isAll());
		assertTrue(((ManyToManyMapping) ormPersistentAttribute.getMapping()).getCascade().isMerge());
		assertTrue(((ManyToManyMapping) ormPersistentAttribute.getMapping()).getCascade().isPersist());
		assertTrue(((ManyToManyMapping) ormPersistentAttribute.getMapping()).getCascade().isRefresh());
		assertTrue(((ManyToManyMapping) ormPersistentAttribute.getMapping()).getCascade().isRemove());
	}
	
	public void testOneToOneMorphToOneToManyMapping() throws Exception {
		OrmPersistentType ormPersistentType = entityMappings().addOrmPersistentType(MappingKeys.ENTITY_TYPE_MAPPING_KEY, FULLY_QUALIFIED_TYPE_NAME);
		OrmPersistentAttribute ormPersistentAttribute = ormPersistentType.addSpecifiedPersistentAttribute(MappingKeys.ONE_TO_ONE_ATTRIBUTE_MAPPING_KEY, "oneToOne");
		
		OneToOneMapping oneToOneMapping = (OneToOneMapping) ormPersistentAttribute.getMapping();
		assertFalse(oneToOneMapping.isDefault());
		oneToOneMapping.setSpecifiedFetch(FetchType.EAGER);
		oneToOneMapping.setSpecifiedTargetEntity("TargetEntity");
		oneToOneMapping.setMappedBy("mappedBy");
		oneToOneMapping.getCascade().setAll(true);
		oneToOneMapping.getCascade().setMerge(true);
		oneToOneMapping.getCascade().setPersist(true);
		oneToOneMapping.getCascade().setRefresh(true);
		oneToOneMapping.getCascade().setRemove(true);
		JoinColumn joinColumn = oneToOneMapping.addSpecifiedJoinColumn(0);
		joinColumn.setSpecifiedName("name");
		joinColumn.setSpecifiedReferencedColumnName("referenceName");
		assertFalse(oneToOneMapping.isDefault());	
		
		ormPersistentAttribute.setSpecifiedMappingKey(MappingKeys.ONE_TO_MANY_ATTRIBUTE_MAPPING_KEY);
		assertEquals(1, ormPersistentType.specifiedAttributesSize());
		assertEquals(ormPersistentAttribute, ormPersistentType.specifiedAttributes().next());
		assertTrue(ormPersistentAttribute.getMapping() instanceof OneToManyMapping);
		assertEquals("oneToOne", ormPersistentAttribute.getMapping().getName());
		assertEquals(FetchType.EAGER, ((OneToManyMapping) ormPersistentAttribute.getMapping()).getSpecifiedFetch());
		assertEquals("TargetEntity", ((OneToManyMapping) ormPersistentAttribute.getMapping()).getSpecifiedTargetEntity());
		assertEquals("mappedBy", ((OneToManyMapping) ormPersistentAttribute.getMapping()).getMappedBy());
		assertTrue(((OneToManyMapping) ormPersistentAttribute.getMapping()).getCascade().isAll());
		assertTrue(((OneToManyMapping) ormPersistentAttribute.getMapping()).getCascade().isMerge());
		assertTrue(((OneToManyMapping) ormPersistentAttribute.getMapping()).getCascade().isPersist());
		assertTrue(((OneToManyMapping) ormPersistentAttribute.getMapping()).getCascade().isRefresh());
		assertTrue(((OneToManyMapping) ormPersistentAttribute.getMapping()).getCascade().isRemove());
	}
	
	public void testOneToOneMorphToManyToOneMapping() throws Exception {
		OrmPersistentType ormPersistentType = entityMappings().addOrmPersistentType(MappingKeys.ENTITY_TYPE_MAPPING_KEY, FULLY_QUALIFIED_TYPE_NAME);
		OrmPersistentAttribute ormPersistentAttribute = ormPersistentType.addSpecifiedPersistentAttribute(MappingKeys.ONE_TO_ONE_ATTRIBUTE_MAPPING_KEY, "oneToOne");
		
		OneToOneMapping oneToOneMapping = (OneToOneMapping) ormPersistentAttribute.getMapping();
		assertFalse(oneToOneMapping.isDefault());
		oneToOneMapping.setSpecifiedFetch(FetchType.EAGER);
		oneToOneMapping.setSpecifiedTargetEntity("TargetEntity");
		oneToOneMapping.setMappedBy("mappedBy");
		oneToOneMapping.getCascade().setAll(true);
		oneToOneMapping.getCascade().setMerge(true);
		oneToOneMapping.getCascade().setPersist(true);
		oneToOneMapping.getCascade().setRefresh(true);
		oneToOneMapping.getCascade().setRemove(true);
		JoinColumn joinColumn = oneToOneMapping.addSpecifiedJoinColumn(0);
		joinColumn.setSpecifiedName("name");
		joinColumn.setSpecifiedReferencedColumnName("referenceName");
		assertFalse(oneToOneMapping.isDefault());	
		
		ormPersistentAttribute.setSpecifiedMappingKey(MappingKeys.MANY_TO_ONE_ATTRIBUTE_MAPPING_KEY);
		assertEquals(1, ormPersistentType.specifiedAttributesSize());
		assertEquals(ormPersistentAttribute, ormPersistentType.specifiedAttributes().next());
		assertTrue(ormPersistentAttribute.getMapping() instanceof ManyToOneMapping);
		assertEquals("oneToOne", ormPersistentAttribute.getMapping().getName());
		assertEquals(FetchType.EAGER, ((ManyToOneMapping) ormPersistentAttribute.getMapping()).getSpecifiedFetch());
		assertEquals("TargetEntity", ((ManyToOneMapping) ormPersistentAttribute.getMapping()).getSpecifiedTargetEntity());
		assertTrue(((ManyToOneMapping) ormPersistentAttribute.getMapping()).getCascade().isAll());
		assertTrue(((ManyToOneMapping) ormPersistentAttribute.getMapping()).getCascade().isMerge());
		assertTrue(((ManyToOneMapping) ormPersistentAttribute.getMapping()).getCascade().isPersist());
		assertTrue(((ManyToOneMapping) ormPersistentAttribute.getMapping()).getCascade().isRefresh());
		assertTrue(((ManyToOneMapping) ormPersistentAttribute.getMapping()).getCascade().isRemove());
		
		joinColumn = ((ManyToOneMapping) ormPersistentAttribute.getMapping()).specifiedJoinColumns().next();
		assertEquals("name", joinColumn.getName());		
		assertEquals("referenceName", joinColumn.getReferencedColumnName());		
	}
	
	public void testOneToOneMorphToBasicMapping() throws Exception {
		OrmPersistentType ormPersistentType = entityMappings().addOrmPersistentType(MappingKeys.ENTITY_TYPE_MAPPING_KEY, FULLY_QUALIFIED_TYPE_NAME);
		OrmPersistentAttribute ormPersistentAttribute = ormPersistentType.addSpecifiedPersistentAttribute(MappingKeys.ONE_TO_ONE_ATTRIBUTE_MAPPING_KEY, "oneToOne");
		
		OneToOneMapping oneToOneMapping = (OneToOneMapping) ormPersistentAttribute.getMapping();
		assertFalse(oneToOneMapping.isDefault());
		oneToOneMapping.setSpecifiedFetch(FetchType.EAGER);
		oneToOneMapping.setSpecifiedTargetEntity("TargetEntity");
		oneToOneMapping.setMappedBy("mappedBy");
		oneToOneMapping.getCascade().setAll(true);
		oneToOneMapping.getCascade().setMerge(true);
		oneToOneMapping.getCascade().setPersist(true);
		oneToOneMapping.getCascade().setRefresh(true);
		oneToOneMapping.getCascade().setRemove(true);
		JoinColumn joinColumn = oneToOneMapping.addSpecifiedJoinColumn(0);
		joinColumn.setSpecifiedName("name");
		joinColumn.setSpecifiedReferencedColumnName("referenceName");
		assertFalse(oneToOneMapping.isDefault());	
		
		ormPersistentAttribute.setSpecifiedMappingKey(MappingKeys.BASIC_ATTRIBUTE_MAPPING_KEY);
		assertEquals(1, ormPersistentType.specifiedAttributesSize());
		assertEquals(ormPersistentAttribute, ormPersistentType.specifiedAttributes().next());
		assertTrue(ormPersistentAttribute.getMapping() instanceof BasicMapping);
		assertEquals("oneToOne", ormPersistentAttribute.getMapping().getName());
//TODO	assertEquals(FetchType.EAGER, ((IBasicMapping) ormPersistentAttribute.getMapping()).getSpecifiedFetch());
	}
}