/*******************************************************************************
 * Copyright (c) 2006, 2007 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 * 
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.core.internal.content.orm;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.jpt.core.internal.IJpaContentNode;
import org.eclipse.jpt.core.internal.IPersistentAttribute;
import org.eclipse.jpt.core.internal.ITextRange;
import org.eclipse.jpt.core.internal.JpaCorePackage;
import org.eclipse.jpt.core.internal.XmlEObject;
import org.eclipse.jpt.core.internal.content.java.JavaPersistentAttribute;
import org.eclipse.jpt.core.internal.content.java.JavaPersistentType;
import org.eclipse.jpt.core.internal.jdtutility.Attribute;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Xml Persistent Attribute</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.jpt.core.internal.content.orm.XmlPersistentAttribute#getName <em>Name</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.jpt.core.internal.content.orm.OrmPackage#getXmlPersistentAttribute()
 * @model kind="class"
 * @generated
 */
public class XmlPersistentAttribute extends XmlEObject
	implements IPersistentAttribute
{
	/**
	 * The default value of the '{@link #getName() <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getName()
	 * @generated
	 * @ordered
	 */
	protected static final String NAME_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getName() <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getName()
	 * @generated
	 * @ordered
	 */
	protected String name = NAME_EDEFAULT;

	private List<IXmlAttributeMappingProvider> attributeMappingProviders;

	protected XmlPersistentAttribute() {
		super();
		this.attributeMappingProviders = buildAttributeMappingProviders();
	}

	protected List<IXmlAttributeMappingProvider> buildAttributeMappingProviders() {
		List<IXmlAttributeMappingProvider> list = new ArrayList<IXmlAttributeMappingProvider>();
		list.add(XmlEmbeddedProvider.instance()); //bug 190344 need to test default embedded before basic
		list.add(XmlBasicProvider.instance());
		list.add(XmlTransientProvider.instance());
		list.add(XmlIdProvider.instance());
		list.add(XmlManyToManyProvider.instance());
		list.add(XmlOneToManyProvider.instance());
		list.add(XmlManyToOneProvider.instance());
		list.add(XmlOneToOneProvider.instance());
		list.add(XmlVersionProvider.instance());
		list.add(XmlEmbeddedIdProvider.instance());
		return list;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return OrmPackage.Literals.XML_PERSISTENT_ATTRIBUTE;
	}

	/**
	 * Returns the value of the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Name</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Name</em>' attribute.
	 * @see #setName(String)
	 * @see org.eclipse.jpt.core.internal.content.orm.OrmPackage#getXmlPersistentAttribute_Name()
	 * @model
	 * @generated
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the value of the '{@link org.eclipse.jpt.core.internal.content.orm.XmlPersistentAttribute#getName <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Name</em>' attribute.
	 * @see #getName()
	 * @generated
	 */
	public void setName(String newName) {
		String oldName = name;
		name = newName;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, OrmPackage.XML_PERSISTENT_ATTRIBUTE__NAME, oldName, name));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case OrmPackage.XML_PERSISTENT_ATTRIBUTE__MAPPING :
				return getMapping();
			case OrmPackage.XML_PERSISTENT_ATTRIBUTE__NAME :
				return getName();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case OrmPackage.XML_PERSISTENT_ATTRIBUTE__NAME :
				setName((String) newValue);
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
	public void eUnset(int featureID) {
		switch (featureID) {
			case OrmPackage.XML_PERSISTENT_ATTRIBUTE__NAME :
				setName(NAME_EDEFAULT);
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
	public boolean eIsSet(int featureID) {
		switch (featureID) {
			case OrmPackage.XML_PERSISTENT_ATTRIBUTE__MAPPING :
				return getMapping() != null;
			case OrmPackage.XML_PERSISTENT_ATTRIBUTE__NAME :
				return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT.equals(name);
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int eBaseStructuralFeatureID(int derivedFeatureID, Class<?> baseClass) {
		if (baseClass == IJpaContentNode.class) {
			switch (derivedFeatureID) {
				default :
					return -1;
			}
		}
		if (baseClass == IPersistentAttribute.class) {
			switch (derivedFeatureID) {
				case OrmPackage.XML_PERSISTENT_ATTRIBUTE__MAPPING :
					return JpaCorePackage.IPERSISTENT_ATTRIBUTE__MAPPING;
				default :
					return -1;
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
	public int eDerivedStructuralFeatureID(int baseFeatureID, Class<?> baseClass) {
		if (baseClass == IJpaContentNode.class) {
			switch (baseFeatureID) {
				default :
					return -1;
			}
		}
		if (baseClass == IPersistentAttribute.class) {
			switch (baseFeatureID) {
				case JpaCorePackage.IPERSISTENT_ATTRIBUTE__MAPPING :
					return OrmPackage.XML_PERSISTENT_ATTRIBUTE__MAPPING;
				default :
					return -1;
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
	public String toString() {
		if (eIsProxy())
			return super.toString();
		StringBuffer result = new StringBuffer(super.toString());
		result.append(" (name: ");
		result.append(name);
		result.append(')');
		return result.toString();
	}

	public XmlAttributeMapping getMapping() {
		return (XmlAttributeMapping) eContainer();
	}

	public String mappingKey() {
		return this.getMapping().getKey();
	}

	public String defaultMappingKey() {
		return null;
	}

	public void setSpecifiedMappingKey(String newKey) {
		String oldKey = this.mappingKey();
		if (newKey == oldKey) {
			return;
		}
		XmlAttributeMapping oldMapping = getMapping();
		this.persistentType().changeMapping(this.getMapping(), newKey);
		XmlAttributeMapping newMapping = getMapping();
		if (eNotificationRequired()) {
			eNotify(new ENotificationImpl(this, Notification.SET, OrmPackage.XML_PERSISTENT_ATTRIBUTE__MAPPING, oldMapping, newMapping));
		}
	}

	public Object getId() {
		return IXmlContentNodes.PERSISTENT_ATTRIBUTE_ID;
	}

	public Collection<IXmlAttributeMappingProvider> attributeMappingProviders() {
		return this.attributeMappingProviders;
	}

	public XmlPersistentType persistentType() {
		return getMapping().getPersistentType();
	}

	public XmlTypeMapping typeMapping() {
		return persistentType().getMapping();
	}

	public boolean isVirtual() {
		return getMapping().isVirtual();
	}

	public void setVirtual(boolean virtual) {
		getMapping().setVirtual(virtual);
	}

	public Attribute getAttribute() {
		JavaPersistentType javaPersistentType = typeMapping().getPersistentType().findJavaPersistentType();
		if (javaPersistentType == null) {
			return null;
		}
		for (Iterator<JavaPersistentAttribute> i = javaPersistentType.attributes(); i.hasNext();) {
			JavaPersistentAttribute persistentAttribute = i.next();
			if (persistentAttribute.getName().equals(getName())) {
				return persistentAttribute.getAttribute();
			}
		}
		return null;
	}

	public String primaryKeyColumnName() {
		return getMapping().primaryKeyColumnName();
	}

	@Override
	public ITextRange fullTextRange() {
		return (this.isVirtual()) ? null : super.fullTextRange();
	}

	@Override
	public ITextRange validationTextRange() {
		return (this.isVirtual()) ? this.persistentType().attributesTextRange() : this.getMapping().validationTextRange();
	}

	@Override
	public ITextRange selectionTextRange() {
		return (isVirtual()) ? null : this.getMapping().selectionTextRange();
	}

	public ITextRange nameTextRange() {
		return getMapping().nameTextRange();
	}

	public boolean isOverridableAttribute() {
		return this.getMapping().isOverridableAttributeMapping();
	}

	public boolean isOverridableAssociation() {
		return this.getMapping().isOverridableAssociationMapping();
	}

	public boolean isIdAttribute() {
		return this.getMapping().isIdMapping();
	}
}
