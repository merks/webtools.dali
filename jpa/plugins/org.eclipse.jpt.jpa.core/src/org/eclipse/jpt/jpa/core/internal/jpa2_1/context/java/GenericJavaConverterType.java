/*******************************************************************************
 * Copyright (c) 2013 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 * 
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.jpa.core.internal.jpa2_1.context.java;

import org.eclipse.jpt.common.core.resource.java.JavaResourceType;
import org.eclipse.jpt.jpa.core.context.JpaContextNode;
import org.eclipse.jpt.jpa.core.internal.context.java.AbstractJavaManagedType;
import org.eclipse.jpt.jpa.core.jpa2_1.context.java.JavaConverterType2_1;
import org.eclipse.jpt.jpa.core.jpa2_1.resource.java.Converter2_1Annotation;

public class GenericJavaConverterType extends AbstractJavaManagedType
	implements JavaConverterType2_1
{

	protected boolean autoApply;

	protected Boolean specifiedAutoApply;

	public GenericJavaConverterType(JpaContextNode parent, JavaResourceType resourceType) {
		super(parent, resourceType);
		this.specifiedAutoApply = this.buildSpecifiedAutoApply();
		this.autoApply = this.buildAutoApply();
	}

	@Override
	public void synchronizeWithResourceModel() {
		super.synchronizeWithResourceModel();
		this.setSpecifiedAutoApply_(this.buildSpecifiedAutoApply());
		this.setAutoApply(this.buildAutoApply());
	}

	@Override
	public void update() {
		super.update();
	}


	// ********** auto apply **********

	public boolean isAutoApply() {
		return this.autoApply;
	}

	protected void setAutoApply(boolean autoApply) {
		boolean old = this.autoApply;
		this.autoApply = autoApply;
		firePropertyChanged(AUTO_APPLY_PROPERTY, old, autoApply);
	}

	protected boolean buildAutoApply() {
		return this.specifiedAutoApply == null ? this.isDefaultAutoApply() : this.specifiedAutoApply.booleanValue();
	}

	public boolean isDefaultAutoApply() {
		return DEFAULT_AUTO_APPLY;
	}

	public Boolean getSpecifiedAutoApply() {
		return this.specifiedAutoApply;
	}

	public void setSpecifiedAutoApply(Boolean autoApply) {
		this.getConverterAnnotation().setAutoApply(autoApply);
		this.setSpecifiedAutoApply_(autoApply);
	}

	protected void setSpecifiedAutoApply_(Boolean autoApply) {
		Boolean old = this.specifiedAutoApply;
		this.specifiedAutoApply = autoApply;
		this.firePropertyChanged(SPECIFIED_AUTO_APPLY_PROPERTY, old, autoApply);
	}

	protected Boolean buildSpecifiedAutoApply() {
		Converter2_1Annotation converterAnnotation = this.getConverterAnnotation();
		return converterAnnotation != null ? converterAnnotation.getAutoApply() : null;
	}


	// ********** converter annotation **********

	protected Converter2_1Annotation getConverterAnnotation() {
		return (Converter2_1Annotation) this.resourceType.getAnnotation(Converter2_1Annotation.ANNOTATION_NAME);
	}


	// ********** ManagedType implementation **********

	public Class<JavaConverterType2_1> getType() {
		return JavaConverterType2_1.class;
	}

}
