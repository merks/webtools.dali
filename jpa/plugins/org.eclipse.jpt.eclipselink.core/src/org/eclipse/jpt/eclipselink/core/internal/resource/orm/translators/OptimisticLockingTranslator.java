/*******************************************************************************
 *  Copyright (c) 2009 Oracle. All rights reserved. This
 *  program and the accompanying materials are made available under the terms of
 *  the Eclipse Public License v1.0 which accompanies this distribution, and is
 *  available at http://www.eclipse.org/legal/epl-v10.html
 *  
 *  Contributors: Oracle. - initial API and implementation
 *******************************************************************************/
package org.eclipse.jpt.eclipselink.core.internal.resource.orm.translators;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.jpt.core.internal.resource.common.translators.BooleanTranslator;
import org.eclipse.jpt.core.internal.resource.orm.translators.ColumnTranslator;
import org.eclipse.jpt.eclipselink.core.resource.orm.EclipseLinkOrmFactory;
import org.eclipse.wst.common.internal.emf.resource.Translator;

public class OptimisticLockingTranslator extends Translator
	implements EclipseLinkOrmXmlMapper
{	
	private Translator[] children;	
	
	
	public OptimisticLockingTranslator(String domNameAndPath, EStructuralFeature aFeature) {
		super(domNameAndPath, aFeature);
	}
	
	@Override
	public EObject createEMFObject(@SuppressWarnings("unused") String nodeName, @SuppressWarnings("unused") String readAheadName) {
		return EclipseLinkOrmFactory.eINSTANCE.createXmlOptimisticLocking();
	}
	
	@Override
	protected Translator[] getChildren() {
		if (this.children == null) {
			this.children = createChildren();
		}
		return this.children;
	}
		
	protected Translator[] createChildren() {
		return new Translator[] {
			createTypeTranslator(),
			createCascadeTranslator(),
			createSelectedColumnTranslator(),
		};
	}
	
	protected Translator createTypeTranslator() {
		return new Translator(OPTIMISTIC_LOCKING__TYPE, ECLIPSELINK_ORM_PKG.getXmlOptimisticLocking_Type(), DOM_ATTRIBUTE);
	}
	
	protected Translator createCascadeTranslator() {
		return new BooleanTranslator(OPTIMISTIC_LOCKING__CASCADE, ECLIPSELINK_ORM_PKG.getXmlOptimisticLocking_Cascade(), DOM_ATTRIBUTE);
	}
	
	protected Translator createSelectedColumnTranslator() {
		return new ColumnTranslator(OPTIMISTIC_LOCKING__SELECTED_COLUMN, ECLIPSELINK_ORM_PKG.getXmlOptimisticLocking_SelectedColumns());
	}
}
