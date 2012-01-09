/*******************************************************************************
 *  Copyright (c) 2012  Oracle. All rights reserved.
 *  This program and the accompanying materials are made available under the
 *  terms of the Eclipse Public License v1.0, which accompanies this distribution
 *  and is available at http://www.eclipse.org/legal/epl-v10.html
 *  
 *  Contributors: 
 *  	Oracle - initial API and implementation
 *******************************************************************************/
package org.eclipse.jpt.jaxb.eclipselink.core.tests.internal.context.java;

import java.util.Iterator;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jpt.common.core.resource.java.JavaResourceAttribute;
import org.eclipse.jpt.common.core.utility.jdt.AnnotatedElement;
import org.eclipse.jpt.common.core.utility.jdt.Member;
import org.eclipse.jpt.common.core.utility.jdt.ModifiedDeclaration;
import org.eclipse.jpt.common.utility.internal.CollectionTools;
import org.eclipse.jpt.common.utility.internal.iterators.ArrayIterator;
import org.eclipse.jpt.jaxb.core.MappingKeys;
import org.eclipse.jpt.jaxb.core.context.JaxbClass;
import org.eclipse.jpt.jaxb.core.context.JaxbClassMapping;
import org.eclipse.jpt.jaxb.core.context.JaxbPersistentAttribute;
import org.eclipse.jpt.jaxb.core.resource.java.JAXB;
import org.eclipse.jpt.jaxb.eclipselink.core.internal.context.java.ELJavaXmlElementMapping;
import org.eclipse.jpt.jaxb.eclipselink.core.resource.java.ELJaxb;
import org.eclipse.jpt.jaxb.eclipselink.core.resource.java.XmlPathAnnotation;
import org.eclipse.jpt.jaxb.eclipselink.core.tests.internal.context.ELJaxbContextModelTestCase;


public class ELJavaXmlElementMappingTests
		extends ELJaxbContextModelTestCase {
	
	public ELJavaXmlElementMappingTests(String name) {
		super(name);
	}
	
	
	private ICompilationUnit createTypeWithXmlElement() throws Exception {
		return this.createTestType(new DefaultAnnotationWriter() {
			@Override
			public Iterator<String> imports() {
				return new ArrayIterator<String>(JAXB.XML_TYPE, JAXB.XML_ELEMENT);
			}
			
			@Override
			public void appendTypeAnnotationTo(StringBuilder sb) {
				sb.append("@XmlType");
			}
			
			@Override
			public void appendIdFieldAnnotationTo(StringBuilder sb) {
				sb.append("@XmlElement");
			}
		});
	}
	
	private ICompilationUnit createTypeWithXmlPath() throws Exception {
		return this.createTestType(new DefaultAnnotationWriter() {
			@Override
			public Iterator<String> imports() {
				return new ArrayIterator<String>(JAXB.XML_TYPE, ELJaxb.XML_PATH);
			}
			
			@Override
			public void appendTypeAnnotationTo(StringBuilder sb) {
				sb.append("@XmlType");
			}
			
			@Override
			public void appendIdFieldAnnotationTo(StringBuilder sb) {
				sb.append("@XmlPath");
			}
		});
	}
	
	
	public void testModifyXmlPath() throws Exception {
		createTypeWithXmlElement();
		
		JaxbClass jaxbClass = (JaxbClass) CollectionTools.get(getContextRoot().getTypes(), 0);
		JaxbClassMapping classMapping = jaxbClass.getMapping();
		JaxbPersistentAttribute persistentAttribute = CollectionTools.get(classMapping.getAttributes(), 0);
		ELJavaXmlElementMapping mapping = (ELJavaXmlElementMapping) persistentAttribute.getMapping();
		JavaResourceAttribute resourceAttribute = mapping.getPersistentAttribute().getJavaResourceAttribute();
		
		assertEquals(0, CollectionTools.size(resourceAttribute.getAnnotations(ELJaxb.XML_PATH)));
		assertNull(mapping.getXmlPath());
		
		mapping.addXmlPath();
		
		assertEquals(1, CollectionTools.size(resourceAttribute.getAnnotations(ELJaxb.XML_PATH)));
		assertNotNull(mapping.getXmlPath());
		
		mapping.removeXmlPath();
		
		assertEquals(0, CollectionTools.size(resourceAttribute.getAnnotations(ELJaxb.XML_PATH)));
		assertNull(mapping.getXmlPath());
	}

	public void testUpdateXmlPath() throws Exception {
		createTypeWithXmlElement();
		
		JaxbClass jaxbClass = (JaxbClass) CollectionTools.get(getContextRoot().getTypes(), 0);
		JaxbClassMapping classMapping = jaxbClass.getMapping();
		JaxbPersistentAttribute persistentAttribute = CollectionTools.get(classMapping.getAttributes(), 0);
		ELJavaXmlElementMapping mapping = (ELJavaXmlElementMapping) persistentAttribute.getMapping();
		JavaResourceAttribute resourceAttribute = mapping.getPersistentAttribute().getJavaResourceAttribute();
		
		assertEquals(0, CollectionTools.size(resourceAttribute.getAnnotations(ELJaxb.XML_PATH)));
		assertNull(mapping.getXmlPath());
		
		AnnotatedElement annotatedElement = this.annotatedElement(resourceAttribute);
		annotatedElement.edit(new Member.Editor() {
			public void edit(ModifiedDeclaration declaration) {
				ELJavaXmlElementMappingTests.this.addMarkerAnnotation(
						declaration.getDeclaration(), ELJaxb.XML_PATH);
			}
		});
		
		assertEquals(1, CollectionTools.size(resourceAttribute.getAnnotations(ELJaxb.XML_PATH)));
		assertNotNull(mapping.getXmlPath());
		
		annotatedElement.edit(new Member.Editor() {
			public void edit(ModifiedDeclaration declaration) {
				ELJavaXmlElementMappingTests.this.removeAnnotation(declaration, ELJaxb.XML_PATH);
			}
		});
		
		assertEquals(0, CollectionTools.size(resourceAttribute.getAnnotations(ELJaxb.XML_PATH)));
		assertNull(mapping.getXmlPath());
	}
	
	public void testDefault() throws Exception {
		createTypeWithXmlPath();
		
		JaxbClass jaxbClass = (JaxbClass) CollectionTools.get(getContextRoot().getTypes(), 0);
		JaxbClassMapping classMapping = jaxbClass.getMapping();
		JaxbPersistentAttribute persistentAttribute = CollectionTools.get(classMapping.getAttributes(), 0);
		JavaResourceAttribute resourceAttribute = persistentAttribute.getJavaResourceAttribute();
		XmlPathAnnotation xmlPathAnnotation = (XmlPathAnnotation) resourceAttribute.getAnnotation(0, ELJaxb.XML_PATH);
		
		assertNotNull(xmlPathAnnotation);
		assertTrue(persistentAttribute.getMapping().getKey() == MappingKeys.XML_ELEMENT_ATTRIBUTE_MAPPING_KEY);
		
		AnnotatedElement annotatedElement = this.annotatedElement(resourceAttribute);
		annotatedElement.edit(new Member.Editor() {
			public void edit(ModifiedDeclaration declaration) {
				ELJavaXmlElementMappingTests.this.setMemberValuePair(
						declaration, ELJaxb.XML_PATH, "element/@attribute");
			}
		});
		
		assertNotNull(xmlPathAnnotation.getValue());
		assertFalse(persistentAttribute.getMapping().getKey() == MappingKeys.XML_ELEMENT_ATTRIBUTE_MAPPING_KEY);
		
		annotatedElement.edit(new Member.Editor() {
			public void edit(ModifiedDeclaration declaration) {
				ELJavaXmlElementMappingTests.this.setMemberValuePair(
						declaration, ELJaxb.XML_PATH, "./element[@name=\"foo\"]/element[1]/text()");
			}
		});
		
		assertNotNull(xmlPathAnnotation.getValue());
		assertTrue(persistentAttribute.getMapping().getKey() == MappingKeys.XML_ELEMENT_ATTRIBUTE_MAPPING_KEY);
		
		annotatedElement.edit(new Member.Editor() {
			public void edit(ModifiedDeclaration declaration) {
				ELJavaXmlElementMappingTests.this.removeMemberValuePair(
						declaration, ELJaxb.XML_PATH);
			}
		});
		
		assertNull(xmlPathAnnotation.getValue());
		assertTrue(persistentAttribute.getMapping().getKey() == MappingKeys.XML_ELEMENT_ATTRIBUTE_MAPPING_KEY);
	}
}
