/*******************************************************************************
 * Copyright (c) 2006, 2012 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 * 
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.jpa.core.internal.prefs;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;

/**
 * Class used to initialize default preference values.
 * See <code>org.eclipse.core.runtime.preferences</code> extension point.
 */
public class JpaPreferenceInitializer
	extends AbstractPreferenceInitializer 
{
	@Override
	public void initializeDefaultPreferences() {
		JpaPreferencesManager.initializeDefaultPreferences();
	}
}
