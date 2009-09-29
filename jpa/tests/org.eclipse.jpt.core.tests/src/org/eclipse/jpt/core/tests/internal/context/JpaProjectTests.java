/*******************************************************************************
 * Copyright (c) 2009 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 * 
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.core.tests.internal.context;

import junit.framework.TestCase;
import org.eclipse.jpt.core.JpaProject;
import org.eclipse.jpt.core.JptCorePlugin;
import org.eclipse.jpt.core.internal.operations.OrmFileCreationDataModelProperties;
import org.eclipse.jpt.core.internal.operations.OrmFileCreationDataModelProvider;
import org.eclipse.jpt.core.internal.operations.PersistenceFileCreationDataModelProperties;
import org.eclipse.jpt.core.internal.operations.PersistenceFileCreationDataModelProvider;
import org.eclipse.jpt.core.resource.xml.JpaXmlResource;
import org.eclipse.jpt.core.tests.internal.projects.TestJpaProject;
import org.eclipse.wst.common.frameworks.datamodel.DataModelFactory;
import org.eclipse.wst.common.frameworks.datamodel.IDataModel;

@SuppressWarnings("nls")
public class JpaProjectTests extends TestCase
{
	static final String BASE_PROJECT_NAME = JpaProjectTests.class.getSimpleName();
	
	TestJpaProject jpaProject;

	
	public JpaProjectTests(String name) {
		super(name);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		this.jpaProject = TestJpaProject.buildJpaProject(BASE_PROJECT_NAME, false); // false = no auto-build
	}
	
	@Override
	protected void tearDown() throws Exception {
		this.jpaProject.getProject().delete(true, true, null);
		this.jpaProject = null;
		super.tearDown();
	}
	
	protected JpaProject getJpaProject() {
		return this.jpaProject.getJpaProject();
	}
	
	public void testGetPersistenceXmlResource() throws Exception {
		JpaXmlResource resource = this.getJpaProject().getPersistenceXmlResource();
		assertNotNull(resource);
		assertEquals(JptCorePlugin.PERSISTENCE_XML_CONTENT_TYPE, resource.getContentType());
		assertEquals("src/META-INF/persistence.xml", resource.getFile().getProjectRelativePath().toString());
		
		//delete the persistence.xml file and verify it is not returned from getPersistenceXmlResource()
		resource.delete(null);
		resource = this.getJpaProject().getPersistenceXmlResource();
		assertNull(resource);
		
		//add the persistence.xml file back
		createPersistenceXmlFile();
		resource = this.getJpaProject().getPersistenceXmlResource();
		assertNotNull(resource);
		assertEquals(JptCorePlugin.PERSISTENCE_XML_CONTENT_TYPE, resource.getContentType());
		assertEquals("src/META-INF/persistence.xml", resource.getFile().getProjectRelativePath().toString());
	}
	
	private void createPersistenceXmlFile() throws Exception {
		IDataModel config =
			DataModelFactory.createDataModel(new PersistenceFileCreationDataModelProvider());
		config.setProperty(PersistenceFileCreationDataModelProperties.PROJECT_NAME, getJpaProject().getProject().getName());
		config.getDefaultOperation().execute(null, null);
	}
	
	public void testGetDefaultOrmXmlResource() throws Exception {
		JpaXmlResource resource = this.getJpaProject().getDefaultOrmXmlResource();
		assertNotNull(resource);
		assertEquals(JptCorePlugin.ORM_XML_CONTENT_TYPE, resource.getContentType());
		assertEquals("src/META-INF/orm.xml", resource.getFile().getProjectRelativePath().toString());
		
		//delete the orm.xml file and verify it is not returned from getDefaultOrmXmlResource()
		resource.delete(null);
		resource = this.getJpaProject().getDefaultOrmXmlResource();
		assertNull(resource);
		
		//add the default orm.xml file back
		createDefaultOrmXmlFile();
		resource = this.getJpaProject().getDefaultOrmXmlResource();
		assertNotNull(resource);
		assertEquals(JptCorePlugin.ORM_XML_CONTENT_TYPE, resource.getContentType());
		assertEquals("src/META-INF/orm.xml", resource.getFile().getProjectRelativePath().toString());
	}
	
	private void createDefaultOrmXmlFile() throws Exception {
		IDataModel config =
			DataModelFactory.createDataModel(new OrmFileCreationDataModelProvider());
		config.setProperty(OrmFileCreationDataModelProperties.PROJECT_NAME, getJpaProject().getProject().getName());
		config.getDefaultOperation().execute(null, null);
	}
	
	private void createOrmXmlFile(String filePath) throws Exception {
		IDataModel config =
			DataModelFactory.createDataModel(new OrmFileCreationDataModelProvider());
		config.setProperty(OrmFileCreationDataModelProperties.PROJECT_NAME, getJpaProject().getProject().getName());
		config.setProperty(OrmFileCreationDataModelProperties.FILE_PATH, filePath);
		config.getDefaultOperation().execute(null, null);
	}

	public void testGetMappingFileResource() throws Exception {
		JpaXmlResource resource = this.getJpaProject().getMappingFileXmlResource(JptCorePlugin.DEFAULT_ORM_XML_FILE_PATH);
		assertNotNull(resource);
		assertEquals(JptCorePlugin.ORM_XML_CONTENT_TYPE, resource.getContentType());
		assertEquals("src/META-INF/orm.xml", resource.getFile().getProjectRelativePath().toString());
		
		//delete the orm.xml file and verify it is not returned from getMappingFileResource()
		resource.delete(null);
		resource = this.getJpaProject().getMappingFileXmlResource(JptCorePlugin.DEFAULT_ORM_XML_FILE_PATH);
		assertNull(resource);
		
		//add the  orm.xml file back
		createDefaultOrmXmlFile();
		resource = this.getJpaProject().getMappingFileXmlResource(JptCorePlugin.DEFAULT_ORM_XML_FILE_PATH);
		assertNotNull(resource);
		assertEquals(JptCorePlugin.ORM_XML_CONTENT_TYPE, resource.getContentType());
		assertEquals("src/META-INF/orm.xml", resource.getFile().getProjectRelativePath().toString());
	}
	
	public void testGetMappingFileResourceDifferentlyName() throws Exception {
		JpaXmlResource resource = this.getJpaProject().getMappingFileXmlResource("META-INF/orm2.xml");
		assertNull(resource);

		//create the orm2.xml file
		createOrmXmlFile("META-INF/orm2.xml");
		resource = this.getJpaProject().getMappingFileXmlResource("META-INF/orm2.xml");
		assertNotNull(resource);
		assertEquals(JptCorePlugin.ORM_XML_CONTENT_TYPE, resource.getContentType());
		assertEquals("src/META-INF/orm2.xml", resource.getFile().getProjectRelativePath().toString());
		
		//delete the orm2.xml file and verify it is not returned from getMappingFileResource()
		resource.delete(null);
		resource = this.getJpaProject().getMappingFileXmlResource("META-INF/orm2.xml");
		assertNull(resource);
		
		//add the orm2.xml file back
		createOrmXmlFile("META-INF/orm2.xml");
		resource = this.getJpaProject().getMappingFileXmlResource("META-INF/orm2.xml");
		assertNotNull(resource);
		assertEquals(JptCorePlugin.ORM_XML_CONTENT_TYPE, resource.getContentType());
		assertEquals("src/META-INF/orm2.xml", resource.getFile().getProjectRelativePath().toString());
	}
}
