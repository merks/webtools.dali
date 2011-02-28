/*******************************************************************************
 * <copyright>
 *
 * Copyright (c) 2005, 2010 SAP AG.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Stefan Dimov - initial API, implementation and documentation
 *
 * </copyright>
 *
 *******************************************************************************/
package org.eclipse.jpt.jpadiagrameditor.ui.internal.feature;

import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.jpt.jpa.core.context.java.JavaPersistentType;
import org.eclipse.jpt.jpadiagrameditor.ui.internal.i18n.JPAEditorMessages;
import org.eclipse.jpt.jpadiagrameditor.ui.internal.provider.JPAEditorImageProvider;
import org.eclipse.jpt.jpadiagrameditor.ui.internal.relations.ManyToOneBiDirRelation;
import org.eclipse.jpt.jpadiagrameditor.ui.internal.util.JPAEditorUtil;
import org.eclipse.jpt.jpadiagrameditor.ui.internal.util.JpaArtifactFactory;


public class CreateManyToOneBiDirRelationFeature extends
		CreateManyToOneRelationFeature {

	public CreateManyToOneBiDirRelationFeature(IFeatureProvider fp) {
		super(fp, JPAEditorMessages.CreateManyToOneBiDirRelationFeature_manyToOneBiDirFeatureName,  
				JPAEditorMessages.CreateManyToOneBiDirRelationFeature_manyToOneBiDirFeatureDescription);
	}
		
	@Override
	public ManyToOneBiDirRelation createRelation(IFeatureProvider fp, PictogramElement source, 
			PictogramElement target) {
		JavaPersistentType owner = (JavaPersistentType)(getBusinessObjectForPictogramElement(source));
		JavaPersistentType inverse = (JavaPersistentType)(getBusinessObjectForPictogramElement(target));		
		
		String ownerAttributeName = JPAEditorUtil.cutFromLastDot(JpaArtifactFactory.instance().getEntityName(inverse));
		String nameWithNonCapitalLetter = ownerAttributeName;
		if (JpaArtifactFactory.instance().isMethodAnnotated(owner))
			nameWithNonCapitalLetter = JPAEditorUtil.produceValidAttributeName(ownerAttributeName);
		String ownerAttributeText = JPAEditorUtil.produceUniqueAttributeName(owner, nameWithNonCapitalLetter);		

		String inverseAttributeName = JPAEditorUtil.cutFromLastDot(JpaArtifactFactory.instance().getEntityName(owner));
		String nameWithNonCapitalLetter2 = inverseAttributeName;
		if (JpaArtifactFactory.instance().isMethodAnnotated(inverse))
			nameWithNonCapitalLetter2 = JPAEditorUtil.produceValidAttributeName(inverseAttributeName);
		String inverseAttributeText = JPAEditorUtil.produceUniqueAttributeName(inverse, ownerAttributeText, nameWithNonCapitalLetter2);		
		
		ManyToOneBiDirRelation rel = new ManyToOneBiDirRelation(fp, owner, inverse, ownerAttributeText, inverseAttributeText, true, 
				getFeatureProvider().getCompilationUnit(owner),
				getFeatureProvider().getCompilationUnit(inverse)); 
		return rel;		
	}
	
    public String getCreateImageId() {
        return JPAEditorImageProvider.ICON_MANY_TO_ONE_2_DIR;
    }			

}