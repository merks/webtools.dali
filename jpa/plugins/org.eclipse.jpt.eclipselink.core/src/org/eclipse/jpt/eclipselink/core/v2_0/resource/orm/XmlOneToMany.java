/*******************************************************************************
 * Copyright (c) 2009 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 * 
 * Contributors:
 *     Oracle - initial API and implementation
 *******************************************************************************/
package org.eclipse.jpt.eclipselink.core.v2_0.resource.orm;

import java.util.Collection;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;
import org.eclipse.jpt.core.internal.resource.xml.translators.EmptyTagBooleanTranslator;
import org.eclipse.jpt.core.internal.resource.xml.translators.SimpleTranslator;
import org.eclipse.jpt.core.resource.orm.XmlAssociationOverride;
import org.eclipse.jpt.eclipselink.core.resource.orm.EclipseLinkOrmPackage;
import org.eclipse.jpt.eclipselink.core.resource.orm.XmlAccessMethods;
import org.eclipse.jpt.eclipselink.core.resource.orm.XmlConverter;
import org.eclipse.jpt.eclipselink.core.resource.orm.XmlConverterHolder;
import org.eclipse.jpt.eclipselink.core.resource.orm.XmlObjectTypeConverter;
import org.eclipse.jpt.eclipselink.core.resource.orm.XmlProperty;
import org.eclipse.jpt.eclipselink.core.resource.orm.XmlStructConverter;
import org.eclipse.jpt.eclipselink.core.resource.orm.XmlTypeConverter;
import org.eclipse.wst.common.internal.emf.resource.Translator;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Xml One To Many</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.jpt.eclipselink.core.v2_0.resource.orm.XmlOneToMany#getMapKeyConvert <em>Map Key Convert</em>}</li>
 *   <li>{@link org.eclipse.jpt.eclipselink.core.v2_0.resource.orm.XmlOneToMany#getMapKeyAssociationOverrides <em>Map Key Association Overrides</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.jpt.eclipselink.core.v2_0.resource.orm.EclipseLink2_0OrmPackage#getXmlOneToMany()
 * @model kind="class"
 * @generated
 */
public class XmlOneToMany extends org.eclipse.jpt.eclipselink.core.v1_1.resource.orm.XmlOneToMany implements XmlConverterHolder
{
	/**
	 * The cached value of the '{@link #getConverter() <em>Converter</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getConverter()
	 * @generated
	 * @ordered
	 */
	protected XmlConverter converter;

	/**
	 * The cached value of the '{@link #getTypeConverter() <em>Type Converter</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTypeConverter()
	 * @generated
	 * @ordered
	 */
	protected XmlTypeConverter typeConverter;

	/**
	 * The cached value of the '{@link #getObjectTypeConverter() <em>Object Type Converter</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getObjectTypeConverter()
	 * @generated
	 * @ordered
	 */
	protected XmlObjectTypeConverter objectTypeConverter;

	/**
	 * The cached value of the '{@link #getStructConverter() <em>Struct Converter</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getStructConverter()
	 * @generated
	 * @ordered
	 */
	protected XmlStructConverter structConverter;

	/**
	 * The default value of the '{@link #getMapKeyConvert() <em>Map Key Convert</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMapKeyConvert()
	 * @generated
	 * @ordered
	 */
	protected static final String MAP_KEY_CONVERT_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getMapKeyConvert() <em>Map Key Convert</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMapKeyConvert()
	 * @generated
	 * @ordered
	 */
	protected String mapKeyConvert = MAP_KEY_CONVERT_EDEFAULT;

	/**
	 * The cached value of the '{@link #getMapKeyAssociationOverrides() <em>Map Key Association Overrides</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMapKeyAssociationOverrides()
	 * @generated
	 * @ordered
	 */
	protected EList<XmlAssociationOverride> mapKeyAssociationOverrides;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected XmlOneToMany()
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
		return EclipseLink2_0OrmPackage.Literals.XML_ONE_TO_MANY;
	}

	/**
	 * Returns the value of the '<em><b>Converter</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Converter</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Converter</em>' containment reference.
	 * @see #setConverter(XmlConverter)
	 * @see org.eclipse.jpt.eclipselink.core.v2_0.resource.orm.EclipseLink2_0OrmPackage#getXmlConverterHolder_Converter()
	 * @model containment="true"
	 * @generated
	 */
	public XmlConverter getConverter()
	{
		return converter;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetConverter(XmlConverter newConverter, NotificationChain msgs)
	{
		XmlConverter oldConverter = converter;
		converter = newConverter;
		if (eNotificationRequired())
		{
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, EclipseLink2_0OrmPackage.XML_ONE_TO_MANY__CONVERTER, oldConverter, newConverter);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * Sets the value of the '{@link org.eclipse.jpt.eclipselink.core.v2_0.resource.orm.XmlOneToMany#getConverter <em>Converter</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Converter</em>' containment reference.
	 * @see #getConverter()
	 * @generated
	 */
	public void setConverter(XmlConverter newConverter)
	{
		if (newConverter != converter)
		{
			NotificationChain msgs = null;
			if (converter != null)
				msgs = ((InternalEObject)converter).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - EclipseLink2_0OrmPackage.XML_ONE_TO_MANY__CONVERTER, null, msgs);
			if (newConverter != null)
				msgs = ((InternalEObject)newConverter).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - EclipseLink2_0OrmPackage.XML_ONE_TO_MANY__CONVERTER, null, msgs);
			msgs = basicSetConverter(newConverter, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, EclipseLink2_0OrmPackage.XML_ONE_TO_MANY__CONVERTER, newConverter, newConverter));
	}

	/**
	 * Returns the value of the '<em><b>Type Converter</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Type Converter</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Type Converter</em>' containment reference.
	 * @see #setTypeConverter(XmlTypeConverter)
	 * @see org.eclipse.jpt.eclipselink.core.v2_0.resource.orm.EclipseLink2_0OrmPackage#getXmlConverterHolder_TypeConverter()
	 * @model containment="true"
	 * @generated
	 */
	public XmlTypeConverter getTypeConverter()
	{
		return typeConverter;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetTypeConverter(XmlTypeConverter newTypeConverter, NotificationChain msgs)
	{
		XmlTypeConverter oldTypeConverter = typeConverter;
		typeConverter = newTypeConverter;
		if (eNotificationRequired())
		{
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, EclipseLink2_0OrmPackage.XML_ONE_TO_MANY__TYPE_CONVERTER, oldTypeConverter, newTypeConverter);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * Sets the value of the '{@link org.eclipse.jpt.eclipselink.core.v2_0.resource.orm.XmlOneToMany#getTypeConverter <em>Type Converter</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Type Converter</em>' containment reference.
	 * @see #getTypeConverter()
	 * @generated
	 */
	public void setTypeConverter(XmlTypeConverter newTypeConverter)
	{
		if (newTypeConverter != typeConverter)
		{
			NotificationChain msgs = null;
			if (typeConverter != null)
				msgs = ((InternalEObject)typeConverter).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - EclipseLink2_0OrmPackage.XML_ONE_TO_MANY__TYPE_CONVERTER, null, msgs);
			if (newTypeConverter != null)
				msgs = ((InternalEObject)newTypeConverter).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - EclipseLink2_0OrmPackage.XML_ONE_TO_MANY__TYPE_CONVERTER, null, msgs);
			msgs = basicSetTypeConverter(newTypeConverter, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, EclipseLink2_0OrmPackage.XML_ONE_TO_MANY__TYPE_CONVERTER, newTypeConverter, newTypeConverter));
	}

	/**
	 * Returns the value of the '<em><b>Object Type Converter</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Object Type Converter</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Object Type Converter</em>' containment reference.
	 * @see #setObjectTypeConverter(XmlObjectTypeConverter)
	 * @see org.eclipse.jpt.eclipselink.core.v2_0.resource.orm.EclipseLink2_0OrmPackage#getXmlConverterHolder_ObjectTypeConverter()
	 * @model containment="true"
	 * @generated
	 */
	public XmlObjectTypeConverter getObjectTypeConverter()
	{
		return objectTypeConverter;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetObjectTypeConverter(XmlObjectTypeConverter newObjectTypeConverter, NotificationChain msgs)
	{
		XmlObjectTypeConverter oldObjectTypeConverter = objectTypeConverter;
		objectTypeConverter = newObjectTypeConverter;
		if (eNotificationRequired())
		{
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, EclipseLink2_0OrmPackage.XML_ONE_TO_MANY__OBJECT_TYPE_CONVERTER, oldObjectTypeConverter, newObjectTypeConverter);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * Sets the value of the '{@link org.eclipse.jpt.eclipselink.core.v2_0.resource.orm.XmlOneToMany#getObjectTypeConverter <em>Object Type Converter</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Object Type Converter</em>' containment reference.
	 * @see #getObjectTypeConverter()
	 * @generated
	 */
	public void setObjectTypeConverter(XmlObjectTypeConverter newObjectTypeConverter)
	{
		if (newObjectTypeConverter != objectTypeConverter)
		{
			NotificationChain msgs = null;
			if (objectTypeConverter != null)
				msgs = ((InternalEObject)objectTypeConverter).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - EclipseLink2_0OrmPackage.XML_ONE_TO_MANY__OBJECT_TYPE_CONVERTER, null, msgs);
			if (newObjectTypeConverter != null)
				msgs = ((InternalEObject)newObjectTypeConverter).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - EclipseLink2_0OrmPackage.XML_ONE_TO_MANY__OBJECT_TYPE_CONVERTER, null, msgs);
			msgs = basicSetObjectTypeConverter(newObjectTypeConverter, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, EclipseLink2_0OrmPackage.XML_ONE_TO_MANY__OBJECT_TYPE_CONVERTER, newObjectTypeConverter, newObjectTypeConverter));
	}

	/**
	 * Returns the value of the '<em><b>Struct Converter</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Struct Converter</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Struct Converter</em>' containment reference.
	 * @see #setStructConverter(XmlStructConverter)
	 * @see org.eclipse.jpt.eclipselink.core.v2_0.resource.orm.EclipseLink2_0OrmPackage#getXmlConverterHolder_StructConverter()
	 * @model containment="true"
	 * @generated
	 */
	public XmlStructConverter getStructConverter()
	{
		return structConverter;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetStructConverter(XmlStructConverter newStructConverter, NotificationChain msgs)
	{
		XmlStructConverter oldStructConverter = structConverter;
		structConverter = newStructConverter;
		if (eNotificationRequired())
		{
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, EclipseLink2_0OrmPackage.XML_ONE_TO_MANY__STRUCT_CONVERTER, oldStructConverter, newStructConverter);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * Sets the value of the '{@link org.eclipse.jpt.eclipselink.core.v2_0.resource.orm.XmlOneToMany#getStructConverter <em>Struct Converter</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Struct Converter</em>' containment reference.
	 * @see #getStructConverter()
	 * @generated
	 */
	public void setStructConverter(XmlStructConverter newStructConverter)
	{
		if (newStructConverter != structConverter)
		{
			NotificationChain msgs = null;
			if (structConverter != null)
				msgs = ((InternalEObject)structConverter).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - EclipseLink2_0OrmPackage.XML_ONE_TO_MANY__STRUCT_CONVERTER, null, msgs);
			if (newStructConverter != null)
				msgs = ((InternalEObject)newStructConverter).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - EclipseLink2_0OrmPackage.XML_ONE_TO_MANY__STRUCT_CONVERTER, null, msgs);
			msgs = basicSetStructConverter(newStructConverter, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, EclipseLink2_0OrmPackage.XML_ONE_TO_MANY__STRUCT_CONVERTER, newStructConverter, newStructConverter));
	}

	/**
	 * Returns the value of the '<em><b>Map Key Convert</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Map Key Convert</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Map Key Convert</em>' attribute.
	 * @see #setMapKeyConvert(String)
	 * @see org.eclipse.jpt.eclipselink.core.v2_0.resource.orm.EclipseLink2_0OrmPackage#getXmlOneToMany_MapKeyConvert()
	 * @model
	 * @generated
	 */
	public String getMapKeyConvert()
	{
		return mapKeyConvert;
	}

	/**
	 * Sets the value of the '{@link org.eclipse.jpt.eclipselink.core.v2_0.resource.orm.XmlOneToMany#getMapKeyConvert <em>Map Key Convert</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Map Key Convert</em>' attribute.
	 * @see #getMapKeyConvert()
	 * @generated
	 */
	public void setMapKeyConvert(String newMapKeyConvert)
	{
		String oldMapKeyConvert = mapKeyConvert;
		mapKeyConvert = newMapKeyConvert;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, EclipseLink2_0OrmPackage.XML_ONE_TO_MANY__MAP_KEY_CONVERT, oldMapKeyConvert, mapKeyConvert));
	}

	/**
	 * Returns the value of the '<em><b>Map Key Association Overrides</b></em>' containment reference list.
	 * The list contents are of type {@link org.eclipse.jpt.core.resource.orm.XmlAssociationOverride}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Map Key Association Overrides</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Map Key Association Overrides</em>' containment reference list.
	 * @see org.eclipse.jpt.eclipselink.core.v2_0.resource.orm.EclipseLink2_0OrmPackage#getXmlOneToMany_MapKeyAssociationOverrides()
	 * @model containment="true"
	 * @generated
	 */
	public EList<XmlAssociationOverride> getMapKeyAssociationOverrides()
	{
		if (mapKeyAssociationOverrides == null)
		{
			mapKeyAssociationOverrides = new EObjectContainmentEList<XmlAssociationOverride>(XmlAssociationOverride.class, this, EclipseLink2_0OrmPackage.XML_ONE_TO_MANY__MAP_KEY_ASSOCIATION_OVERRIDES);
		}
		return mapKeyAssociationOverrides;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs)
	{
		switch (featureID)
		{
			case EclipseLink2_0OrmPackage.XML_ONE_TO_MANY__CONVERTER:
				return basicSetConverter(null, msgs);
			case EclipseLink2_0OrmPackage.XML_ONE_TO_MANY__TYPE_CONVERTER:
				return basicSetTypeConverter(null, msgs);
			case EclipseLink2_0OrmPackage.XML_ONE_TO_MANY__OBJECT_TYPE_CONVERTER:
				return basicSetObjectTypeConverter(null, msgs);
			case EclipseLink2_0OrmPackage.XML_ONE_TO_MANY__STRUCT_CONVERTER:
				return basicSetStructConverter(null, msgs);
			case EclipseLink2_0OrmPackage.XML_ONE_TO_MANY__MAP_KEY_ASSOCIATION_OVERRIDES:
				return ((InternalEList<?>)getMapKeyAssociationOverrides()).basicRemove(otherEnd, msgs);
		}
		return super.eInverseRemove(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType)
	{
		switch (featureID)
		{
			case EclipseLink2_0OrmPackage.XML_ONE_TO_MANY__CONVERTER:
				return getConverter();
			case EclipseLink2_0OrmPackage.XML_ONE_TO_MANY__TYPE_CONVERTER:
				return getTypeConverter();
			case EclipseLink2_0OrmPackage.XML_ONE_TO_MANY__OBJECT_TYPE_CONVERTER:
				return getObjectTypeConverter();
			case EclipseLink2_0OrmPackage.XML_ONE_TO_MANY__STRUCT_CONVERTER:
				return getStructConverter();
			case EclipseLink2_0OrmPackage.XML_ONE_TO_MANY__MAP_KEY_CONVERT:
				return getMapKeyConvert();
			case EclipseLink2_0OrmPackage.XML_ONE_TO_MANY__MAP_KEY_ASSOCIATION_OVERRIDES:
				return getMapKeyAssociationOverrides();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void eSet(int featureID, Object newValue)
	{
		switch (featureID)
		{
			case EclipseLink2_0OrmPackage.XML_ONE_TO_MANY__CONVERTER:
				setConverter((XmlConverter)newValue);
				return;
			case EclipseLink2_0OrmPackage.XML_ONE_TO_MANY__TYPE_CONVERTER:
				setTypeConverter((XmlTypeConverter)newValue);
				return;
			case EclipseLink2_0OrmPackage.XML_ONE_TO_MANY__OBJECT_TYPE_CONVERTER:
				setObjectTypeConverter((XmlObjectTypeConverter)newValue);
				return;
			case EclipseLink2_0OrmPackage.XML_ONE_TO_MANY__STRUCT_CONVERTER:
				setStructConverter((XmlStructConverter)newValue);
				return;
			case EclipseLink2_0OrmPackage.XML_ONE_TO_MANY__MAP_KEY_CONVERT:
				setMapKeyConvert((String)newValue);
				return;
			case EclipseLink2_0OrmPackage.XML_ONE_TO_MANY__MAP_KEY_ASSOCIATION_OVERRIDES:
				getMapKeyAssociationOverrides().clear();
				getMapKeyAssociationOverrides().addAll((Collection<? extends XmlAssociationOverride>)newValue);
				return;
		}
		super.eSet(featureID, newValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eUnset(int featureID)
	{
		switch (featureID)
		{
			case EclipseLink2_0OrmPackage.XML_ONE_TO_MANY__CONVERTER:
				setConverter((XmlConverter)null);
				return;
			case EclipseLink2_0OrmPackage.XML_ONE_TO_MANY__TYPE_CONVERTER:
				setTypeConverter((XmlTypeConverter)null);
				return;
			case EclipseLink2_0OrmPackage.XML_ONE_TO_MANY__OBJECT_TYPE_CONVERTER:
				setObjectTypeConverter((XmlObjectTypeConverter)null);
				return;
			case EclipseLink2_0OrmPackage.XML_ONE_TO_MANY__STRUCT_CONVERTER:
				setStructConverter((XmlStructConverter)null);
				return;
			case EclipseLink2_0OrmPackage.XML_ONE_TO_MANY__MAP_KEY_CONVERT:
				setMapKeyConvert(MAP_KEY_CONVERT_EDEFAULT);
				return;
			case EclipseLink2_0OrmPackage.XML_ONE_TO_MANY__MAP_KEY_ASSOCIATION_OVERRIDES:
				getMapKeyAssociationOverrides().clear();
				return;
		}
		super.eUnset(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean eIsSet(int featureID)
	{
		switch (featureID)
		{
			case EclipseLink2_0OrmPackage.XML_ONE_TO_MANY__CONVERTER:
				return converter != null;
			case EclipseLink2_0OrmPackage.XML_ONE_TO_MANY__TYPE_CONVERTER:
				return typeConverter != null;
			case EclipseLink2_0OrmPackage.XML_ONE_TO_MANY__OBJECT_TYPE_CONVERTER:
				return objectTypeConverter != null;
			case EclipseLink2_0OrmPackage.XML_ONE_TO_MANY__STRUCT_CONVERTER:
				return structConverter != null;
			case EclipseLink2_0OrmPackage.XML_ONE_TO_MANY__MAP_KEY_CONVERT:
				return MAP_KEY_CONVERT_EDEFAULT == null ? mapKeyConvert != null : !MAP_KEY_CONVERT_EDEFAULT.equals(mapKeyConvert);
			case EclipseLink2_0OrmPackage.XML_ONE_TO_MANY__MAP_KEY_ASSOCIATION_OVERRIDES:
				return mapKeyAssociationOverrides != null && !mapKeyAssociationOverrides.isEmpty();
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int eBaseStructuralFeatureID(int derivedFeatureID, Class<?> baseClass)
	{
		if (baseClass == XmlConverterHolder.class)
		{
			switch (derivedFeatureID)
			{
				case EclipseLink2_0OrmPackage.XML_ONE_TO_MANY__CONVERTER: return EclipseLinkOrmPackage.XML_CONVERTER_HOLDER__CONVERTER;
				case EclipseLink2_0OrmPackage.XML_ONE_TO_MANY__TYPE_CONVERTER: return EclipseLinkOrmPackage.XML_CONVERTER_HOLDER__TYPE_CONVERTER;
				case EclipseLink2_0OrmPackage.XML_ONE_TO_MANY__OBJECT_TYPE_CONVERTER: return EclipseLinkOrmPackage.XML_CONVERTER_HOLDER__OBJECT_TYPE_CONVERTER;
				case EclipseLink2_0OrmPackage.XML_ONE_TO_MANY__STRUCT_CONVERTER: return EclipseLinkOrmPackage.XML_CONVERTER_HOLDER__STRUCT_CONVERTER;
				default: return -1;
			}
		}
		return super.eBaseStructuralFeatureID(derivedFeatureID, baseClass);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int eDerivedStructuralFeatureID(int baseFeatureID, Class<?> baseClass)
	{
		if (baseClass == XmlConverterHolder.class)
		{
			switch (baseFeatureID)
			{
				case EclipseLinkOrmPackage.XML_CONVERTER_HOLDER__CONVERTER: return EclipseLink2_0OrmPackage.XML_ONE_TO_MANY__CONVERTER;
				case EclipseLinkOrmPackage.XML_CONVERTER_HOLDER__TYPE_CONVERTER: return EclipseLink2_0OrmPackage.XML_ONE_TO_MANY__TYPE_CONVERTER;
				case EclipseLinkOrmPackage.XML_CONVERTER_HOLDER__OBJECT_TYPE_CONVERTER: return EclipseLink2_0OrmPackage.XML_ONE_TO_MANY__OBJECT_TYPE_CONVERTER;
				case EclipseLinkOrmPackage.XML_CONVERTER_HOLDER__STRUCT_CONVERTER: return EclipseLink2_0OrmPackage.XML_ONE_TO_MANY__STRUCT_CONVERTER;
				default: return -1;
			}
		}
		return super.eDerivedStructuralFeatureID(baseFeatureID, baseClass);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String toString()
	{
		if (eIsProxy()) return super.toString();

		StringBuffer result = new StringBuffer(super.toString());
		result.append(" (mapKeyConvert: ");
		result.append(mapKeyConvert);
		result.append(')');
		return result.toString();
	}
	
	// ********** translators **********

	public static Translator buildTranslator(String elementName, EStructuralFeature structuralFeature) {
		return new SimpleTranslator(
			elementName, 
			structuralFeature, 
			EclipseLink2_0OrmPackage.eINSTANCE.getXmlOneToMany(), 
			buildTranslatorChildren());
	}

	private static Translator[] buildTranslatorChildren() {
		return new Translator[] {
			buildNameTranslator(),
			buildTargetEntityTranslator(),
			buildFetchTranslator(),
			buildAccessTranslator(),
			buildMappedByTranslator(),
			buildOrphanRemovalTranslator(),
			buildOrderByTranslator(),
			buildOrderColumnTranslator(),
			buildMapKeyTranslator(),
			buildMapKeyClassTranslator(),
			buildMapKeyTemporalTranslator(),
			buildMapKeyEnumeratedTranslator(),
			buildMapKeyConvertTranslator(),
			buildMapKeyAttributeOverrideTranslator(),
			buildMapKeyAssociationOverrideTranslator(),
			buildMapKeyColumnTranslator(),
			buildMapKeyJoinColumnTranslator(),
			buildConverterTranslator(),
			buildTypeConverterTranslator(),
			buildObjectTypeConverterTranslator(),
			buildStructConverterTranslator(),
			buildJoinTableTranslator(),
			buildJoinColumnTranslator(),
			buildCascadeTranslator(),
			buildPrivateOwnedTranslator(),
			buildJoinFetchTranslator(),
			buildPropertyTranslator(),
			buildAccessMethodsTranslator()
		};
	}

	protected static Translator buildMapKeyAssociationOverrideTranslator() {
		return XmlAssociationOverride.buildTranslator(EclipseLink2_0.MAP_KEY_ASSOCIATION_OVERRIDE, EclipseLink2_0OrmPackage.eINSTANCE.getXmlOneToMany_MapKeyAssociationOverrides());
	}
	
	protected static Translator buildMapKeyConvertTranslator() {
		return new Translator(EclipseLink2_0.MAP_KEY_CONVERT, EclipseLink2_0OrmPackage.eINSTANCE.getXmlOneToMany_MapKeyConvert());
	}
	
	protected static Translator buildConverterTranslator() {
		return XmlConverter.buildTranslator(EclipseLink2_0.CONVERTER, EclipseLinkOrmPackage.eINSTANCE.getXmlConverterHolder_Converter());
	}
	
	protected static Translator buildTypeConverterTranslator() {
		return XmlTypeConverter.buildTranslator(EclipseLink2_0.TYPE_CONVERTER, EclipseLinkOrmPackage.eINSTANCE.getXmlConverterHolder_TypeConverter());
	}
	
	protected static Translator buildObjectTypeConverterTranslator() {
		return XmlObjectTypeConverter.buildTranslator(EclipseLink2_0.OBJECT_TYPE_CONVERTER, EclipseLinkOrmPackage.eINSTANCE.getXmlConverterHolder_ObjectTypeConverter());
	}
	
	protected static Translator buildStructConverterTranslator() {
		return XmlStructConverter.buildTranslator(EclipseLink2_0.STRUCT_CONVERTER, EclipseLinkOrmPackage.eINSTANCE.getXmlConverterHolder_StructConverter());
	}
	
	public static Translator buildPrivateOwnedTranslator() {
		return new EmptyTagBooleanTranslator(EclipseLink2_0.PRIVATE_OWNED, EclipseLinkOrmPackage.eINSTANCE.getXmlPrivateOwned_PrivateOwned());
	}
	
	public static Translator buildJoinFetchTranslator() {
		return new Translator(EclipseLink2_0.JOIN_FETCH, EclipseLinkOrmPackage.eINSTANCE.getXmlJoinFetch_JoinFetch());
	}
	
	public static Translator buildPropertyTranslator() {
		return XmlProperty.buildTranslator(EclipseLink2_0.PROPERTY, EclipseLinkOrmPackage.eINSTANCE.getXmlPropertyContainer_Properties());
	}
	
	public static Translator buildAccessMethodsTranslator() {
		return XmlAccessMethods.buildTranslator(EclipseLink2_0.ACCESS_METHODS, EclipseLinkOrmPackage.eINSTANCE.getXmlAccessMethodsHolder_AccessMethods());
	}

} // XmlOneToMany
