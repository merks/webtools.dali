/*******************************************************************************
 * Copyright (c) 2009 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 * 
 * Contributors:
 *     Oracle - initial API and implementation
 *******************************************************************************/
package org.eclipse.jpt.eclipselink.core.v1_1.resource.orm;

import java.util.Iterator;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.jpt.core.internal.resource.xml.translators.EnumeratedValueTranslator;
import org.eclipse.jpt.core.internal.resource.xml.translators.SimpleRootTranslator;
import org.eclipse.jpt.core.resource.orm.JPA;
import org.eclipse.jpt.core.resource.orm.OrmPackage;
import org.eclipse.jpt.core.resource.orm.SqlResultSetMapping;
import org.eclipse.jpt.core.resource.orm.XmlNamedNativeQuery;
import org.eclipse.jpt.core.resource.orm.XmlNamedQuery;
import org.eclipse.jpt.core.resource.orm.XmlSequenceGenerator;
import org.eclipse.jpt.core.resource.orm.XmlTableGenerator;
import org.eclipse.jpt.core.resource.xml.CommonPackage;
import org.eclipse.jpt.core.resource.xml.XML;
import org.eclipse.jpt.eclipselink.core.resource.orm.EclipseLinkOrmPackage;
import org.eclipse.jpt.eclipselink.core.resource.orm.XmlConverter;
import org.eclipse.jpt.eclipselink.core.resource.orm.XmlNamedStoredProcedureQuery;
import org.eclipse.jpt.eclipselink.core.resource.orm.XmlObjectTypeConverter;
import org.eclipse.jpt.eclipselink.core.resource.orm.XmlPersistenceUnitMetadata;
import org.eclipse.jpt.eclipselink.core.resource.orm.XmlStructConverter;
import org.eclipse.jpt.eclipselink.core.resource.orm.XmlTypeConverter;
import org.eclipse.jpt.utility.internal.iterators.ArrayIterator;
import org.eclipse.wst.common.internal.emf.resource.ConstantAttributeTranslator;
import org.eclipse.wst.common.internal.emf.resource.Translator;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Xml Entity Mappings</b></em>'.
 * <!-- end-user-doc -->
 *
 *
 * @see org.eclipse.jpt.eclipselink.core.v1_1.resource.orm.EclipseLink1_1OrmPackage#getXmlEntityMappings()
 * @model kind="class"
 * @generated
 */
public class XmlEntityMappings extends org.eclipse.jpt.eclipselink.core.resource.orm.XmlEntityMappings
{
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected XmlEntityMappings()
	{
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass()
	{
		return EclipseLink1_1OrmPackage.Literals.XML_ENTITY_MAPPINGS;
	}
	
	// ********** translators **********

	public static Translator getRootTranslator() {
		return ROOT_TRANSLATOR;
	}
	private static final Translator ROOT_TRANSLATOR = buildRootTranslator();

	private static Translator buildRootTranslator() {
		return new SimpleRootTranslator(
				EclipseLink1_1.ENTITY_MAPPINGS,
				EclipseLink1_1OrmPackage.eINSTANCE.getXmlEntityMappings(),
				buildTranslatorChildren()
			);
	}

	private static Translator[] buildTranslatorChildren() {
		return new Translator[] {
			buildVersionTranslator(),
			buildNamespaceTranslator(),
			buildSchemaNamespaceTranslator(),
			buildSchemaLocationTranslator(),
			buildDescriptionTranslator(),
			XmlPersistenceUnitMetadata.buildTranslator(EclipseLink1_1.PERSISTENCE_UNIT_METADATA, OrmPackage.eINSTANCE.getXmlEntityMappings_PersistenceUnitMetadata()),
			buildPackageTranslator(),
			buildSchemaTranslator(),
			buildCatalogTranslator(),
			buildAccessTranslator(),
			XmlConverter.buildTranslator(EclipseLink1_1.CONVERTER, EclipseLinkOrmPackage.eINSTANCE.getXmlConvertersHolder_Converters()),
			XmlTypeConverter.buildTranslator(EclipseLink1_1.TYPE_CONVERTER, EclipseLinkOrmPackage.eINSTANCE.getXmlConvertersHolder_TypeConverters()),
			XmlObjectTypeConverter.buildTranslator(EclipseLink1_1.OBJECT_TYPE_CONVERTER, EclipseLinkOrmPackage.eINSTANCE.getXmlConvertersHolder_ObjectTypeConverters()),
			XmlStructConverter.buildTranslator(EclipseLink1_1.STRUCT_CONVERTER, EclipseLinkOrmPackage.eINSTANCE.getXmlConvertersHolder_StructConverters()),
			XmlSequenceGenerator.buildTranslator(EclipseLink1_1.SEQUENCE_GENERATOR, OrmPackage.eINSTANCE.getXmlEntityMappings_SequenceGenerators()),
			XmlTableGenerator.buildTranslator(EclipseLink1_1.TABLE_GENERATOR, OrmPackage.eINSTANCE.getXmlEntityMappings_TableGenerators()),
			XmlNamedQuery.buildTranslator(EclipseLink1_1.NAMED_QUERY, OrmPackage.eINSTANCE.getXmlQueryContainer_NamedQueries()),
			XmlNamedNativeQuery.buildTranslator(EclipseLink1_1.NAMED_NATIVE_QUERY, OrmPackage.eINSTANCE.getXmlQueryContainer_NamedNativeQueries()),
			XmlNamedStoredProcedureQuery.buildTranslator(EclipseLink1_1.NAMED_STORED_PROCEDURE_QUERY, EclipseLinkOrmPackage.eINSTANCE.getXmlQueryContainer_NamedStoredProcedureQueries()),
			SqlResultSetMapping.buildTranslator(EclipseLink1_1.SQL_RESULT_SET_MAPPING, OrmPackage.eINSTANCE.getXmlEntityMappings_SqlResultSetMappings()),
			XmlMappedSuperclass.buildTranslator(EclipseLink1_1.MAPPED_SUPERCLASS, OrmPackage.eINSTANCE.getXmlEntityMappings_MappedSuperclasses()),
			XmlEntity.buildTranslator(EclipseLink1_1.ENTITY, OrmPackage.eINSTANCE.getXmlEntityMappings_Entities()),
			XmlEmbeddable.buildTranslator(EclipseLink1_1.EMBEDDABLE, OrmPackage.eINSTANCE.getXmlEntityMappings_Embeddables()),
		};
	}
	
	protected static Translator buildVersionTranslator() {
		return new EnumeratedValueTranslator(
				JPA.ENTITY_MAPPINGS__VERSION, 
				CommonPackage.eINSTANCE.getJpaRootEObject_Version(),
				Translator.DOM_ATTRIBUTE) {
			
			@Override
			protected Iterator enumeratedObjectValues() {
				return new ArrayIterator(new Object[] { EclipseLink1_1.SCHEMA_VERSION });
			}
		};
	}
	
	private static Translator buildNamespaceTranslator() {
		return new ConstantAttributeTranslator(XML.NAMESPACE, EclipseLink1_1.SCHEMA_NAMESPACE);
	}
	
	private static Translator buildSchemaLocationTranslator() {
		return new ConstantAttributeTranslator(XML.XSI_SCHEMA_LOCATION, EclipseLink1_1.SCHEMA_NAMESPACE + ' ' + EclipseLink1_1.SCHEMA_LOCATION);
	}
}
