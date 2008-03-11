/*******************************************************************************
 * Copyright (c) 2006, 2008 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 * 
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.gen.internal;

import java.util.Collection;
import java.util.Iterator;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jpt.db.Table;
import org.eclipse.jpt.gen.internal.EntityGenerator.OverwriteConfirmer;
import org.eclipse.jpt.utility.internal.CollectionTools;

/**
 * This generator will generate a package of entities for a set of tables.
 */
public class PackageGenerator {
	private final Config config;
	private final EntityGenerator.Config entityConfig;
	private final GenScope scope;
	private final OverwriteConfirmer overwriteConfirmer;
	private final IProgressMonitor monitor;


	// ********** public API **********

	public static void generateEntities(Config config, EntityGenerator.Config entityConfig, Collection<Table> tables, OverwriteConfirmer overwriteConfirmer, IProgressMonitor monitor) {
		if ((config == null) || (entityConfig == null) || (tables == null)) {
			throw new NullPointerException();
		}
		new PackageGenerator(config, entityConfig, tables, overwriteConfirmer, monitor).generateEntities();
	}


	// ********** construction/initialization **********

	private PackageGenerator(Config config, EntityGenerator.Config entityConfig, Collection<Table> tables, OverwriteConfirmer overwriteConfirmer, IProgressMonitor monitor) {
		super();
		this.config = config;
		this.entityConfig = entityConfig;
		this.scope = new GenScope(tables, entityConfig, monitor);
		this.overwriteConfirmer = overwriteConfirmer;
		this.monitor = monitor;
	}


	// ********** generation **********

	private void generateEntities() {
		int size = CollectionTools.size(this.scope.entityTables());
		for (Iterator<GenTable> stream = this.scope.entityTables(); stream.hasNext(); ) {
			checkCanceled();
			this.buildEntity(stream.next());
			this.monitor.worked(50/size);
		}
	}

	private void checkCanceled() {
		if (this.monitor.isCanceled()) {
			throw new OperationCanceledException();
		}		
	}
	

	private void buildEntity(GenTable genTable) {
		EntityGenerator.generateEntity(this.entityConfig, this.config.getPackageFragment(), genTable, overwriteConfirmer, this.monitor);
	}


	// ********** config **********

	public static class Config {
		private IPackageFragment packageFragment;

		public IPackageFragment getPackageFragment() {
			return this.packageFragment;
		}
		public void setPackageFragment(IPackageFragment packageFragment) {
			this.packageFragment = packageFragment;
		}

	}

}
