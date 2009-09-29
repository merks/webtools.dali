/*******************************************************************************
 * Copyright (c) 2006, 2009 Oracle. All rights reserved. This
 * program and the accompanying materials are made available under the terms of
 * the Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors: Oracle. - initial API and implementation
 *******************************************************************************/
package org.eclipse.jpt.ui.internal.views;

import java.util.HashMap;
import java.util.Map;
import org.eclipse.jpt.core.JpaResourceType;
import org.eclipse.jpt.core.JpaStructureNode;
import org.eclipse.jpt.ui.JpaPlatformUi;
import org.eclipse.jpt.ui.JptUiPlugin;
import org.eclipse.jpt.ui.details.JpaDetailsPage;
import org.eclipse.jpt.ui.internal.JptUiMessages;
import org.eclipse.jpt.ui.internal.Tracing;
import org.eclipse.jpt.ui.internal.platform.JpaPlatformUiRegistry;
import org.eclipse.jpt.ui.internal.selection.JpaSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;

/**
 * The JPA view that shows the details of a structure node
 *
 * @version 2.2
 * @since 1.0
 */
public class JpaDetailsView extends AbstractJpaView
{
	/**
	 * The current <code>JpaDetailsPage</code> that was retrieve based on the
	 * current selection.
	 */
	private JpaDetailsPage<JpaStructureNode> currentPage;

	/**
	 * The current selection used to show the right <code>IJpaDetailsPage</code>.
	 */
	private JpaSelection currentSelection;

	//TODO this is crap, a Map of Maps of Maps. Needs to be done differently, the factory/platform should handle caching instead
	// key1 platform id
	// key2 JpaResourceType
	// key3 structure node type
	// value Composite page
	private Map<String, Map<JpaResourceType, Map<String, JpaDetailsPage<? extends JpaStructureNode>>>> detailsPages;

	/**
	 * Creates a new <code>JpaDetailsView</code>.
	 */
	public JpaDetailsView() {
		super(JptUiMessages.JpaDetailsView_viewNotAvailable);
	}

	@Override
	protected void initialize() {
		super.initialize();

		this.currentSelection = JpaSelection.NULL_SELECTION;
		this.detailsPages = new HashMap<String, Map<JpaResourceType, Map<String, JpaDetailsPage<? extends JpaStructureNode>>>>();
	}

	private JpaPlatformUi getJpaPlatformUi(JpaStructureNode structureNode) {
		String platformId = structureNode.getJpaProject().getJpaPlatform().getId();
		return JpaPlatformUiRegistry.instance().getJpaPlatformUi(platformId);
	}

	public JpaSelection getSelection() {
		return this.currentSelection;
	}
	
	private JpaDetailsPage<? extends JpaStructureNode> getDetailsPage(JpaStructureNode structureNode) {
		String platformId = structureNode.getJpaProject().getJpaPlatform().getId();
		if (this.detailsPages.containsKey(platformId)) {
			Map<JpaResourceType, Map<String, JpaDetailsPage<? extends JpaStructureNode>>> platformDetailsPages = this.detailsPages.get(platformId);
			Map<String, JpaDetailsPage<? extends JpaStructureNode>> contentTypeDetailsPages = platformDetailsPages.get(structureNode.getResourceType());
			if (contentTypeDetailsPages != null) {
				JpaDetailsPage<? extends JpaStructureNode> page =  contentTypeDetailsPages.get(structureNode.getId());
				if (page != null) {
					if (page.getControl().isDisposed()) {
						platformDetailsPages.remove(structureNode.getId());
					} else {
						return page;
					}
				}
			}
		}
		return buildDetailsPage(structureNode);
	}

	private JpaDetailsPage<? extends JpaStructureNode> buildDetailsPage(JpaStructureNode structureNode) {
		JpaPlatformUi jpaPlatformUi = getJpaPlatformUi(structureNode);
		
		Composite container = getWidgetFactory().createComposite(getPageBook());
		container.setLayout(new FillLayout(SWT.HORIZONTAL));

		JpaDetailsPage<? extends JpaStructureNode> page = jpaPlatformUi.buildJpaDetailsPage(container, structureNode, getWidgetFactory());
		if (page == null) {
			return null;
		}

		String platformId = structureNode.getJpaProject().getJpaPlatform().getId();
		Map<JpaResourceType, Map<String, JpaDetailsPage<? extends JpaStructureNode>>> platformDetailsPages = this.detailsPages.get(platformId);
		if (platformDetailsPages == null) {
			platformDetailsPages = new HashMap<JpaResourceType, Map<String, JpaDetailsPage<? extends JpaStructureNode>>>();
			this.detailsPages.put(platformId, platformDetailsPages);
		}
		JpaResourceType resourceType = structureNode.getResourceType();
		Map<String, JpaDetailsPage<? extends JpaStructureNode>> contentTypeDetailsPages = platformDetailsPages.get(resourceType);
		if (contentTypeDetailsPages == null) {
			contentTypeDetailsPages = new HashMap<String, JpaDetailsPage<? extends JpaStructureNode>>();
			platformDetailsPages.put(resourceType, contentTypeDetailsPages);
		}
		contentTypeDetailsPages.put(structureNode.getId(), page);

		return page;
	}

	@Override
	public void select(JpaSelection jpaSelection) {
		if (jpaSelection.equals(this.currentSelection)) {
			return;
		}

		this.currentSelection = jpaSelection;

		if (jpaSelection != JpaSelection.NULL_SELECTION) {
			JpaStructureNode newNode = jpaSelection.getSelectedNode();
			JpaDetailsPage<? extends JpaStructureNode> newPage = getDetailsPage(newNode);
			setCurrentPage(newPage);
		}
		else {
			setCurrentPage(null);
		}
	}

	/**
	 * Changes the current page and shows the given page.
	 *
	 * @param newPage The new page to display
	 */
	@SuppressWarnings("unchecked")
	private void setCurrentPage(JpaDetailsPage<? extends JpaStructureNode> page) {

		// Unpopulate old page
		if (this.currentPage != null) {
			try {
				log("JpaDetailsView.setCurrentPage() : disposing of current page"); //$NON-NLS-1$

				this.currentPage.setSubject(null);
			}
			catch (Exception e) {
				JptUiPlugin.log(e);
			}
		}

		JpaDetailsPage<JpaStructureNode> newPage = (JpaDetailsPage<JpaStructureNode>) page;

		// Populate new page
		if (page != null) {
			try {
				log("JpaDetailsView.setCurrentPage() : populating new page"); //$NON-NLS-1$
				newPage.setSubject(this.currentSelection.getSelectedNode());
			}
			catch (Exception e) {
				// Show error page
				page = null;
				JptUiPlugin.log(e);
			}
		}
		else {
			log("JpaDetailsView.setCurrentPage() : No page to populate"); //$NON-NLS-1$
		}

		//no need to show the page again if it is still the same
		if (newPage != null && this.currentPage == newPage) {
			return;
		}
		this.currentPage = newPage;

		// Show new page
		if (page == null) {
			showDefaultPage();
		}
		else {
			showPage(page.getControl());
		}
	}

	@Override
	public void dispose() {

		this.detailsPages.clear();

		this.currentSelection = JpaSelection.NULL_SELECTION;
		this.currentPage = null;

		super.dispose();
	}

	private void log(String message) {
		if (Tracing.booleanDebugOption(Tracing.UI_DETAILS_VIEW)) {
			Tracing.log(message);
		}
	}

}