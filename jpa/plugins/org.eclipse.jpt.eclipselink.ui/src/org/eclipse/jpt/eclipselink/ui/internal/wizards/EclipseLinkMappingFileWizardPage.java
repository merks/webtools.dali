/*******************************************************************************
 *  Copyright (c) 2008  Oracle. 
 *  All rights reserved.  This program and the accompanying materials are 
 *  made available under the terms of the Eclipse Public License v1.0 which 
 *  accompanies this distribution, and is available at 
 *  http://www.eclipse.org/legal/epl-v10.html
 *  
 *  Contributors: 
 *  	Oracle - initial API and implementation
 *******************************************************************************/
package org.eclipse.jpt.eclipselink.ui.internal.wizards;

import org.eclipse.jpt.eclipselink.ui.internal.EclipseLinkUiMessages;
import org.eclipse.jpt.ui.internal.wizards.orm.MappingFileWizardPage;
import org.eclipse.wst.common.frameworks.datamodel.IDataModel;

public class EclipseLinkMappingFileWizardPage extends MappingFileWizardPage
{
	public EclipseLinkMappingFileWizardPage(IDataModel dataModel, String pageName) {
		super(dataModel, pageName);
		setTitle(EclipseLinkUiMessages.MappingFileWizardPage_title);
		setDescription(EclipseLinkUiMessages.MappingFileWizardPage_desc);
		setPageComplete(false);
	}
}
