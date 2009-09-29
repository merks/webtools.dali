/*******************************************************************************
 *  Copyright (c) 2009  Oracle. 
 *  All rights reserved.  This program and the accompanying materials are 
 *  made available under the terms of the Eclipse Public License v1.0 which 
 *  accompanies this distribution, and is available at 
 *  http://www.eclipse.org/legal/epl-v10.html
 *  
 *  Contributors: 
 *  	Oracle - initial API and implementation
 *******************************************************************************/

package org.eclipse.jpt.core.resource.orm.v2_0;

import org.eclipse.emf.common.util.EList;
import org.eclipse.jpt.core.resource.orm.EnumType;
import org.eclipse.jpt.core.resource.orm.TemporalType;
import org.eclipse.jpt.core.resource.orm.XmlAttributeOverride;
import org.eclipse.jpt.core.resource.orm.XmlColumn;
import org.eclipse.jpt.core.resource.orm.XmlJoinColumn;
import org.eclipse.jpt.core.resource.orm.XmlMapKeyClass;
import org.eclipse.jpt.core.resource.orm.XmlOrderColumn;
import org.eclipse.jpt.core.resource.xml.JpaEObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Xml One To Many 20</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.jpt.core.resource.orm.v2_0.XmlOneToMany_2_0#getOrderColumn <em>Order Column</em>}</li>
 *   <li>{@link org.eclipse.jpt.core.resource.orm.v2_0.XmlOneToMany_2_0#getMapKeyClass <em>Map Key Class</em>}</li>
 *   <li>{@link org.eclipse.jpt.core.resource.orm.v2_0.XmlOneToMany_2_0#getMapKeyTemporal <em>Map Key Temporal</em>}</li>
 *   <li>{@link org.eclipse.jpt.core.resource.orm.v2_0.XmlOneToMany_2_0#getMapKeyEnumerated <em>Map Key Enumerated</em>}</li>
 *   <li>{@link org.eclipse.jpt.core.resource.orm.v2_0.XmlOneToMany_2_0#getMapKeyAttributeOverrides <em>Map Key Attribute Overrides</em>}</li>
 *   <li>{@link org.eclipse.jpt.core.resource.orm.v2_0.XmlOneToMany_2_0#getMapKeyColumn <em>Map Key Column</em>}</li>
 *   <li>{@link org.eclipse.jpt.core.resource.orm.v2_0.XmlOneToMany_2_0#getMapKeyJoinColumns <em>Map Key Join Columns</em>}</li>
 *   <li>{@link org.eclipse.jpt.core.resource.orm.v2_0.XmlOneToMany_2_0#isOrphanRemoval <em>Orphan Removal</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.jpt.core.resource.orm.v2_0.OrmV2_0Package#getXmlOneToMany_2_0()
 * @model kind="class" interface="true" abstract="true"
 * @extends JpaEObject
 * @generated
 */
public interface XmlOneToMany_2_0 extends JpaEObject
{
	/**
	 * Returns the value of the '<em><b>Order Column</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Order Column</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Order Column</em>' containment reference.
	 * @see #setOrderColumn(XmlOrderColumn)
	 * @see org.eclipse.jpt.core.resource.orm.v2_0.OrmV2_0Package#getXmlOneToMany_2_0_OrderColumn()
	 * @model containment="true"
	 * @generated
	 */
	XmlOrderColumn getOrderColumn();

	/**
	 * Sets the value of the '{@link org.eclipse.jpt.core.resource.orm.v2_0.XmlOneToMany_2_0#getOrderColumn <em>Order Column</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Order Column</em>' containment reference.
	 * @see #getOrderColumn()
	 * @generated
	 */
	void setOrderColumn(XmlOrderColumn value);

	/**
	 * Returns the value of the '<em><b>Map Key Class</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Map Key Class</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Map Key Class</em>' containment reference.
	 * @see #setMapKeyClass(XmlMapKeyClass)
	 * @see org.eclipse.jpt.core.resource.orm.v2_0.OrmV2_0Package#getXmlOneToMany_2_0_MapKeyClass()
	 * @model containment="true"
	 * @generated
	 */
	XmlMapKeyClass getMapKeyClass();

	/**
	 * Sets the value of the '{@link org.eclipse.jpt.core.resource.orm.v2_0.XmlOneToMany_2_0#getMapKeyClass <em>Map Key Class</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Map Key Class</em>' containment reference.
	 * @see #getMapKeyClass()
	 * @generated
	 */
	void setMapKeyClass(XmlMapKeyClass value);

	/**
	 * Returns the value of the '<em><b>Map Key Temporal</b></em>' attribute.
	 * The literals are from the enumeration {@link org.eclipse.jpt.core.resource.orm.TemporalType}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Map Key Temporal</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Map Key Temporal</em>' attribute.
	 * @see org.eclipse.jpt.core.resource.orm.TemporalType
	 * @see #setMapKeyTemporal(TemporalType)
	 * @see org.eclipse.jpt.core.resource.orm.v2_0.OrmV2_0Package#getXmlOneToMany_2_0_MapKeyTemporal()
	 * @model
	 * @generated
	 */
	TemporalType getMapKeyTemporal();

	/**
	 * Sets the value of the '{@link org.eclipse.jpt.core.resource.orm.v2_0.XmlOneToMany_2_0#getMapKeyTemporal <em>Map Key Temporal</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Map Key Temporal</em>' attribute.
	 * @see org.eclipse.jpt.core.resource.orm.TemporalType
	 * @see #getMapKeyTemporal()
	 * @generated
	 */
	void setMapKeyTemporal(TemporalType value);

	/**
	 * Returns the value of the '<em><b>Map Key Enumerated</b></em>' attribute.
	 * The literals are from the enumeration {@link org.eclipse.jpt.core.resource.orm.EnumType}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Map Key Enumerated</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Map Key Enumerated</em>' attribute.
	 * @see org.eclipse.jpt.core.resource.orm.EnumType
	 * @see #setMapKeyEnumerated(EnumType)
	 * @see org.eclipse.jpt.core.resource.orm.v2_0.OrmV2_0Package#getXmlOneToMany_2_0_MapKeyEnumerated()
	 * @model
	 * @generated
	 */
	EnumType getMapKeyEnumerated();

	/**
	 * Sets the value of the '{@link org.eclipse.jpt.core.resource.orm.v2_0.XmlOneToMany_2_0#getMapKeyEnumerated <em>Map Key Enumerated</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Map Key Enumerated</em>' attribute.
	 * @see org.eclipse.jpt.core.resource.orm.EnumType
	 * @see #getMapKeyEnumerated()
	 * @generated
	 */
	void setMapKeyEnumerated(EnumType value);

	/**
	 * Returns the value of the '<em><b>Map Key Attribute Overrides</b></em>' containment reference list.
	 * The list contents are of type {@link org.eclipse.jpt.core.resource.orm.XmlAttributeOverride}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Map Key Attribute Overrides</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Map Key Attribute Overrides</em>' containment reference list.
	 * @see org.eclipse.jpt.core.resource.orm.v2_0.OrmV2_0Package#getXmlOneToMany_2_0_MapKeyAttributeOverrides()
	 * @model containment="true"
	 * @generated
	 */
	EList<XmlAttributeOverride> getMapKeyAttributeOverrides();

	/**
	 * Returns the value of the '<em><b>Map Key Column</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Map Key Column</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Map Key Column</em>' containment reference.
	 * @see #setMapKeyColumn(XmlColumn)
	 * @see org.eclipse.jpt.core.resource.orm.v2_0.OrmV2_0Package#getXmlOneToMany_2_0_MapKeyColumn()
	 * @model containment="true"
	 * @generated
	 */
	XmlColumn getMapKeyColumn();

	/**
	 * Sets the value of the '{@link org.eclipse.jpt.core.resource.orm.v2_0.XmlOneToMany_2_0#getMapKeyColumn <em>Map Key Column</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Map Key Column</em>' containment reference.
	 * @see #getMapKeyColumn()
	 * @generated
	 */
	void setMapKeyColumn(XmlColumn value);

	/**
	 * Returns the value of the '<em><b>Map Key Join Columns</b></em>' containment reference list.
	 * The list contents are of type {@link org.eclipse.jpt.core.resource.orm.XmlJoinColumn}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Map Key Join Columns</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Map Key Join Columns</em>' containment reference list.
	 * @see org.eclipse.jpt.core.resource.orm.v2_0.OrmV2_0Package#getXmlOneToMany_2_0_MapKeyJoinColumns()
	 * @model containment="true"
	 * @generated
	 */
	EList<XmlJoinColumn> getMapKeyJoinColumns();

	/**
	 * Returns the value of the '<em><b>Orphan Removal</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Orphan Removal</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Orphan Removal</em>' attribute.
	 * @see #setOrphanRemoval(boolean)
	 * @see org.eclipse.jpt.core.resource.orm.v2_0.OrmV2_0Package#getXmlOneToMany_2_0_OrphanRemoval()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.Boolean"
	 * @generated
	 */
	boolean isOrphanRemoval();

	/**
	 * Sets the value of the '{@link org.eclipse.jpt.core.resource.orm.v2_0.XmlOneToMany_2_0#isOrphanRemoval <em>Orphan Removal</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Orphan Removal</em>' attribute.
	 * @see #isOrphanRemoval()
	 * @generated
	 */
	void setOrphanRemoval(boolean value);

} // XmlOneToMany_2_0
