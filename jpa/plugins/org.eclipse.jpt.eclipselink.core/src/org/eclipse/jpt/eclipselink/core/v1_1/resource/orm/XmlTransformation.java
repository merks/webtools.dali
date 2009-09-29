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

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.jpt.core.internal.resource.xml.translators.SimpleTranslator;
import org.eclipse.jpt.core.resource.orm.OrmPackage;
import org.eclipse.wst.common.internal.emf.resource.Translator;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Xml Transformation Impl</b></em>'.
 * <!-- end-user-doc -->
 *
 *
 * @see org.eclipse.jpt.eclipselink.core.v1_1.resource.orm.EclipseLink1_1OrmPackage#getXmlTransformation()
 * @model kind="class"
 * @generated
 */
public class XmlTransformation extends org.eclipse.jpt.eclipselink.core.resource.orm.XmlTransformation implements XmlAttributeMapping
{
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected XmlTransformation()
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
		return EclipseLink1_1OrmPackage.Literals.XML_TRANSFORMATION;
	}

	// ********** translators **********

	public static Translator buildTranslator(String elementName, EStructuralFeature structuralFeature) {
		return new SimpleTranslator(
			elementName, 
			structuralFeature, 
			EclipseLink1_1OrmPackage.eINSTANCE.getXmlTransformation(), 
			buildTranslatorChildren());
	}

	private static Translator[] buildTranslatorChildren() {
		return new Translator[] {
			buildNameTranslator(),
//			buildFetchTranslator(),
//			buildOptionalTranslator(),
			buildAccessTranslator(),
//			buildMutableTranslator(),
//			buildReadTransformerTranslator(),
//			buildWriteTransformerTranslator(),
			buildPropertyTranslator(),
			buildAccessMethodsTranslator()
		};
	}
	
	protected static Translator buildAccessTranslator() {
		return new Translator(EclipseLink1_1.ACCESS, OrmPackage.eINSTANCE.getXmlAccessHolder_Access(), Translator.DOM_ATTRIBUTE);
	}

} // XmlTransformationImpl
