/*******************************************************************************
 * Copyright (c) 2006 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 * 
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.core.internal.jdtutility;

/**
 * Adapt a member and a declaration annotation adapter.
 */
public class MemberAnnotationAdapter extends AbstractAnnotationAdapter {


	// ********** constructor **********

	public MemberAnnotationAdapter(Member member, DeclarationAnnotationAdapter daa) {
		super(member, daa);
	}

}
