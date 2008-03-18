/*******************************************************************************
 * Copyright (c) 2007 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 * 
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.core.tests.internal.resource.java;

import java.util.Iterator;
import org.eclipse.jdt.core.IType;
import org.eclipse.jpt.core.resource.java.JPA;
import org.eclipse.jpt.core.resource.java.JavaResourceNode;
import org.eclipse.jpt.core.resource.java.JavaResourcePersistentType;
import org.eclipse.jpt.core.resource.java.NamedNativeQueries;
import org.eclipse.jpt.core.resource.java.NamedNativeQueryAnnotation;
import org.eclipse.jpt.utility.internal.CollectionTools;
import org.eclipse.jpt.utility.internal.iterators.ArrayIterator;

public class NamedNativeQueriesTests extends JavaResourceModelTestCase {

	private static final String QUERY_NAME = "myQuery";
	private static final String QUERY_QUERY = "SELECT name FROM Employee";
	private static final String QUERY_RESULT_CLASS = "Result";
	private static final String QUERY_RESULT_SET_MAPPING = "resultSetMapping";
	
	public NamedNativeQueriesTests(String name) {
		super(name);
	}

	private void createNamedNativeQueryAnnotation() throws Exception {
		createQueryHintAnnotation();
		this.createAnnotationAndMembers("NamedNativeQuery", "String name(); " +
			"String query();" + 
			"QueryHint[] hints() default{};");
	}
	
	private void createNamedNativeQueriesAnnotation() throws Exception {
		createNamedNativeQueryAnnotation();
		this.createAnnotationAndMembers("NamedNativeQueries", 
			"NamedNativeQuery[] value();");
	}
	
	private void createQueryHintAnnotation() throws Exception {
		this.createAnnotationAndMembers("QueryHint", "String name(); " +
			"String value();");
	}
	
	private IType createTestNamedNativeQueries() throws Exception {
		createNamedNativeQueriesAnnotation();
		return this.createTestType(new DefaultAnnotationWriter() {
			@Override
			public Iterator<String> imports() {
				return new ArrayIterator<String>(JPA.NAMED_NATIVE_QUERIES, JPA.NAMED_NATIVE_QUERY);
			}
			@Override
			public void appendTypeAnnotationTo(StringBuilder sb) {
				sb.append("@NamedNativeQueries(@NamedNativeQuery)");
			}
		});
	}
	
	private IType createTestNamedNativeQueryWithName() throws Exception {
		return createTestNamedNativeQueryWithStringElement("name", QUERY_NAME);
	}
	
	private IType createTestNamedNativeQueryWithQuery() throws Exception {
		return createTestNamedNativeQueryWithStringElement("query", QUERY_QUERY);
	}
	
	private IType createTestNamedNativeQueryWithResultSetMapping() throws Exception {
		return createTestNamedNativeQueryWithStringElement("resultSetMapping", QUERY_RESULT_SET_MAPPING);
	}
	

	private IType createTestNamedNativeQueryWithStringElement(final String elementName, final String value) throws Exception {
		createNamedNativeQueriesAnnotation();
		return this.createTestType(new DefaultAnnotationWriter() {
			@Override
			public Iterator<String> imports() {
				return new ArrayIterator<String>(JPA.NAMED_NATIVE_QUERIES, JPA.NAMED_NATIVE_QUERY);
			}
			@Override
			public void appendTypeAnnotationTo(StringBuilder sb) {
				sb.append("@NamedNativeQueries(@NamedNativeQuery(" + elementName + "=\"" + value + "\"))");
			}
		});
	}

	private IType createTestNamedNativeQueryWithResultClass() throws Exception {
		createNamedNativeQueriesAnnotation();
		return this.createTestType(new DefaultAnnotationWriter() {
			@Override
			public Iterator<String> imports() {
				return new ArrayIterator<String>(JPA.NAMED_NATIVE_QUERIES, JPA.NAMED_NATIVE_QUERY);
			}
			@Override
			public void appendTypeAnnotationTo(StringBuilder sb) {
				sb.append("@NamedNativeQueries(@NamedNativeQuery(resultClass=" + QUERY_RESULT_CLASS + ".class))");
			}
		});
	}
	private IType createTestNamedNativeQueryWithQueryHints() throws Exception {
		createNamedNativeQueriesAnnotation();
		return this.createTestType(new DefaultAnnotationWriter() {
			@Override
			public Iterator<String> imports() {
				return new ArrayIterator<String>(JPA.NAMED_NATIVE_QUERIES, JPA.NAMED_NATIVE_QUERY, JPA.QUERY_HINT);
			}
			@Override
			public void appendTypeAnnotationTo(StringBuilder sb) {
				sb.append("@NamedNativeQueries(@NamedNativeQuery(hints={@QueryHint(name=\"BAR\", value=\"FOO\"), @QueryHint}))");
			}
		});
	}

	private IType createTestNamedNativeQuery() throws Exception {
		createNamedNativeQueriesAnnotation();
		return this.createTestType(new DefaultAnnotationWriter() {
			@Override
			public Iterator<String> imports() {
				return new ArrayIterator<String>(JPA.NAMED_NATIVE_QUERY, JPA.QUERY_HINT);
			}
			@Override
			public void appendTypeAnnotationTo(StringBuilder sb) {
				sb.append("@NamedNativeQuery(name=\"foo\", query=\"bar\", hints=@QueryHint(name=\"BAR\", value=\"FOO\"), resultClass=Foo.class, resultSetMapping=\"mapping\")");
			}
		});
	}

	public void testNamedNativeQuery() throws Exception {
		IType testType = this.createTestNamedNativeQueries();
		JavaResourcePersistentType typeResource = buildJavaTypeResource(testType);
		
		NamedNativeQueries namedQueries = (NamedNativeQueries) typeResource.annotation(JPA.NAMED_NATIVE_QUERIES);
		NamedNativeQueryAnnotation namedQuery = namedQueries.nestedAnnotations().next();
		assertNotNull(namedQuery);
	}

	public void testGetName() throws Exception {
		IType testType = this.createTestNamedNativeQueryWithName();
		JavaResourcePersistentType typeResource = buildJavaTypeResource(testType); 
		
		NamedNativeQueries namedQueries = (NamedNativeQueries) typeResource.annotation(JPA.NAMED_NATIVE_QUERIES);
		NamedNativeQueryAnnotation namedQuery = namedQueries.nestedAnnotations().next();
		assertEquals(QUERY_NAME, namedQuery.getName());
	}

	public void testSetName() throws Exception {
		IType testType = this.createTestNamedNativeQueryWithName();
		JavaResourcePersistentType typeResource = buildJavaTypeResource(testType); 
		
		NamedNativeQueries namedQueries = (NamedNativeQueries) typeResource.annotation(JPA.NAMED_NATIVE_QUERIES);
		NamedNativeQueryAnnotation namedQuery = namedQueries.nestedAnnotations().next();
		assertEquals(QUERY_NAME, namedQuery.getName());
		
		namedQuery.setName("foo");
		assertEquals("foo", namedQuery.getName());
		
		assertSourceContains("@NamedNativeQuery(name=\"foo\")");
		
		namedQuery.setName(null);
		assertNull(namedQuery.getName());
		
		assertSourceDoesNotContain("@NamedNativeQuery");
	}

	public void testGetQuery() throws Exception {
		IType testType = this.createTestNamedNativeQueryWithQuery();
		JavaResourcePersistentType typeResource = buildJavaTypeResource(testType); 
		
		NamedNativeQueries namedQueries = (NamedNativeQueries) typeResource.annotation(JPA.NAMED_NATIVE_QUERIES);
		NamedNativeQueryAnnotation namedQuery = namedQueries.nestedAnnotations().next();
		assertEquals(QUERY_QUERY, namedQuery.getQuery());
	}

	public void testSetQuery() throws Exception {
		IType testType = this.createTestNamedNativeQueryWithQuery();
		JavaResourcePersistentType typeResource = buildJavaTypeResource(testType); 
		
		NamedNativeQueries namedQueries = (NamedNativeQueries) typeResource.annotation(JPA.NAMED_NATIVE_QUERIES);
		NamedNativeQueryAnnotation namedQuery = namedQueries.nestedAnnotations().next();
		assertEquals(QUERY_QUERY, namedQuery.getQuery());
		
		namedQuery.setQuery("foo");
		assertEquals("foo", namedQuery.getQuery());
		
		assertSourceContains("@NamedNativeQuery(query=\"foo\")");
		
		namedQuery.setQuery(null);
		assertNull(namedQuery.getQuery());
		
		assertSourceDoesNotContain("@NamedNativeQuery");
	}
	
	public void testGetResultClass() throws Exception {
		IType testType = this.createTestNamedNativeQueryWithResultClass();
		JavaResourcePersistentType typeResource = buildJavaTypeResource(testType); 
		
		NamedNativeQueries namedQueries = (NamedNativeQueries) typeResource.annotation(JPA.NAMED_NATIVE_QUERIES);
		NamedNativeQueryAnnotation namedQuery = namedQueries.nestedAnnotations().next();
		assertEquals(QUERY_RESULT_CLASS, namedQuery.getResultClass());
	}

	public void testSetResultClass() throws Exception {
		IType testType = this.createTestNamedNativeQueryWithResultClass();
		JavaResourcePersistentType typeResource = buildJavaTypeResource(testType); 
		
		NamedNativeQueries namedQueries = (NamedNativeQueries) typeResource.annotation(JPA.NAMED_NATIVE_QUERIES);
		NamedNativeQueryAnnotation namedQuery = namedQueries.nestedAnnotations().next();
		assertEquals(QUERY_RESULT_CLASS, namedQuery.getResultClass());
		
		namedQuery.setResultClass("foo");
		assertEquals("foo", namedQuery.getResultClass());
		
		assertSourceContains("@NamedNativeQuery(resultClass=foo.class)");
		
		namedQuery.setResultClass(null);
		assertNull(namedQuery.getResultClass());
		
		assertSourceDoesNotContain("@NamedNativeQuery");
	}

	public void testGetFullyQualifiedClass() throws Exception {
		IType testType = this.createTestNamedNativeQueryWithResultClass();
		JavaResourcePersistentType typeResource = buildJavaTypeResource(testType);

		NamedNativeQueries namedQueries = (NamedNativeQueries) typeResource.annotation(JPA.NAMED_NATIVE_QUERIES);
		NamedNativeQueryAnnotation namedQuery = namedQueries.nestedAnnotations().next();
		assertNotNull(namedQuery.getResultClass());
		assertEquals("Result", namedQuery.getFullyQualifiedResultClass());//bug 196200 changed this

		namedQuery.setResultClass(TYPE_NAME);		
		
		assertEquals(FULLY_QUALIFIED_TYPE_NAME, namedQuery.getFullyQualifiedResultClass());				
		assertSourceContains("@NamedNativeQuery(resultClass=" + TYPE_NAME + ".class)");
	}
	
	public void testGetResultSetMapping() throws Exception {
		IType testType = this.createTestNamedNativeQueryWithResultSetMapping();
		JavaResourcePersistentType typeResource = buildJavaTypeResource(testType); 
		
		NamedNativeQueries namedQueries = (NamedNativeQueries) typeResource.annotation(JPA.NAMED_NATIVE_QUERIES);
		NamedNativeQueryAnnotation namedQuery = namedQueries.nestedAnnotations().next();
		assertEquals(QUERY_RESULT_SET_MAPPING, namedQuery.getResultSetMapping());
	}

	public void testSetResultSetMapping() throws Exception {
		IType testType = this.createTestNamedNativeQueryWithResultSetMapping();
		JavaResourcePersistentType typeResource = buildJavaTypeResource(testType); 
		
		NamedNativeQueries namedQueries = (NamedNativeQueries) typeResource.annotation(JPA.NAMED_NATIVE_QUERIES);
		NamedNativeQueryAnnotation namedQuery = namedQueries.nestedAnnotations().next();
		assertEquals(QUERY_RESULT_SET_MAPPING, namedQuery.getResultSetMapping());
		
		namedQuery.setResultSetMapping("foo");
		assertEquals("foo", namedQuery.getResultSetMapping());
		
		assertSourceContains("@NamedNativeQuery(resultSetMapping=\"foo\")");
		
		namedQuery.setResultSetMapping(null);
		assertNull(namedQuery.getResultSetMapping());
		
		assertSourceDoesNotContain("@NamedNativeQuery");
	}

	public void testHints() throws Exception {
		IType testType = this.createTestNamedNativeQueries();
		JavaResourcePersistentType typeResource = buildJavaTypeResource(testType); 
		
		NamedNativeQueries namedQueries = (NamedNativeQueries) typeResource.annotation(JPA.NAMED_NATIVE_QUERIES);
		NamedNativeQueryAnnotation namedQuery = namedQueries.nestedAnnotations().next();
		
		assertEquals(0, namedQuery.hintsSize());
	}
	
	public void testHints2() throws Exception {
		IType testType = this.createTestNamedNativeQueries();
		JavaResourcePersistentType typeResource = buildJavaTypeResource(testType); 
		
		NamedNativeQueries namedQueries = (NamedNativeQueries) typeResource.annotation(JPA.NAMED_NATIVE_QUERIES);
		NamedNativeQueryAnnotation namedQuery = namedQueries.nestedAnnotations().next();
		
		namedQuery.addHint(0);
		namedQuery.addHint(1);
		
		assertEquals(2, namedQuery.hintsSize());
	}
	
	public void testHints3() throws Exception {
		IType testType = this.createTestNamedNativeQueryWithQueryHints();
		JavaResourcePersistentType typeResource = buildJavaTypeResource(testType); 
		
		NamedNativeQueries namedQueries = (NamedNativeQueries) typeResource.annotation(JPA.NAMED_NATIVE_QUERIES);
		NamedNativeQueryAnnotation namedQuery = namedQueries.nestedAnnotations().next();
		
		assertEquals(2, namedQuery.hintsSize());
	}
	
	public void testAddHint() throws Exception {
		IType testType = this.createTestNamedNativeQueries();
		JavaResourcePersistentType typeResource = buildJavaTypeResource(testType); 
		
		NamedNativeQueries namedQueries = (NamedNativeQueries) typeResource.annotation(JPA.NAMED_NATIVE_QUERIES);
		NamedNativeQueryAnnotation namedQuery = namedQueries.nestedAnnotations().next();
		
		namedQuery.addHint(0).setName("FOO");
		namedQuery.addHint(1);
		namedQuery.addHint(0).setName("BAR");

		assertEquals("BAR", namedQuery.hintAt(0).getName());
		assertEquals("FOO", namedQuery.hintAt(1).getName());
		assertNull(namedQuery.hintAt(2).getName());
		assertSourceContains("@NamedNativeQuery(hints={@QueryHint(name=\"BAR\"),@QueryHint(name=\"FOO\"), @QueryHint})");
	}
	
	public void testRemoveHint() throws Exception {
		IType testType = this.createTestNamedNativeQueryWithQueryHints();
		JavaResourcePersistentType typeResource = buildJavaTypeResource(testType); 
		
		NamedNativeQueries namedQueries = (NamedNativeQueries) typeResource.annotation(JPA.NAMED_NATIVE_QUERIES);
		NamedNativeQueryAnnotation namedQuery = namedQueries.nestedAnnotations().next();
		namedQuery.addHint(0).setName("BAZ");
		
		assertEquals("BAZ", namedQuery.hintAt(0).getName());
		assertEquals("BAR", namedQuery.hintAt(1).getName());
		assertNull(namedQuery.hintAt(2).getName());
		assertEquals(3, namedQuery.hintsSize());
		
		namedQuery.removeHint(2);
		assertEquals("BAZ", namedQuery.hintAt(0).getName());
		assertEquals("BAR", namedQuery.hintAt(1).getName());
		assertEquals(2, namedQuery.hintsSize());
		assertSourceContains("@NamedNativeQueries(@NamedNativeQuery(hints={@QueryHint(name=\"BAZ\"), @QueryHint(name=\"BAR\", value=\"FOO\")}))");
		
		namedQuery.removeHint(0);
		assertEquals("BAR", namedQuery.hintAt(0).getName());
		assertEquals(1, namedQuery.hintsSize());
		assertSourceContains("@NamedNativeQueries(@NamedNativeQuery(hints=@QueryHint(name=\"BAR\", value=\"FOO\")))");
		
	
		namedQuery.removeHint(0);
		assertEquals(0, namedQuery.hintsSize());
		assertSourceDoesNotContain("@NamedNativeQuery");
	}
	
	public void testMoveHint() throws Exception {
		IType testType = this.createTestNamedNativeQueryWithQueryHints();
		JavaResourcePersistentType typeResource = buildJavaTypeResource(testType); 
		
		NamedNativeQueries namedQueries = (NamedNativeQueries) typeResource.annotation(JPA.NAMED_NATIVE_QUERIES);
		NamedNativeQueryAnnotation namedQuery = namedQueries.nestedAnnotations().next();
		
		namedQuery.addHint(0).setName("BAZ");
		
		assertEquals("BAZ", namedQuery.hintAt(0).getName());
		assertEquals("BAR", namedQuery.hintAt(1).getName());
		assertNull(namedQuery.hintAt(2).getName());
		assertEquals(3, namedQuery.hintsSize());
	
		namedQuery.moveHint(2, 0);
		
		assertEquals("BAR", namedQuery.hintAt(0).getName());
		assertNull(namedQuery.hintAt(1).getName());
		assertEquals("BAZ", namedQuery.hintAt(2).getName());
		assertEquals(3, namedQuery.hintsSize());
		assertSourceContains("@NamedNativeQueries(@NamedNativeQuery(hints={@QueryHint(name=\"BAR\", value=\"FOO\"), @QueryHint, @QueryHint(name=\"BAZ\")}))");
	}
	
	public void testMoveHint2() throws Exception {
		IType testType = this.createTestNamedNativeQueryWithQueryHints();
		JavaResourcePersistentType typeResource = buildJavaTypeResource(testType); 
		
		NamedNativeQueries namedQueries = (NamedNativeQueries) typeResource.annotation(JPA.NAMED_NATIVE_QUERIES);
		NamedNativeQueryAnnotation namedQuery = namedQueries.nestedAnnotations().next();
		
		namedQuery.addHint(0).setName("BAZ");
		
		assertEquals("BAZ", namedQuery.hintAt(0).getName());
		assertEquals("BAR", namedQuery.hintAt(1).getName());
		assertNull(namedQuery.hintAt(2).getName());
		assertEquals(3, namedQuery.hintsSize());
	
		namedQuery.moveHint(0, 2);
		
		assertNull(namedQuery.hintAt(0).getName());
		assertEquals("BAZ", namedQuery.hintAt(1).getName());
		assertEquals("BAR", namedQuery.hintAt(2).getName());
		assertEquals(3, namedQuery.hintsSize());
		assertSourceContains("@NamedNativeQueries(@NamedNativeQuery(hints={@QueryHint, @QueryHint(name=\"BAZ\"), @QueryHint(name=\"BAR\", value=\"FOO\")}))");
	}
	
	public void testAddNamedNativeQueryCopyExisting() throws Exception {
		IType jdtType = createTestNamedNativeQuery();
		JavaResourcePersistentType typeResource = buildJavaTypeResource(jdtType);
		
		NamedNativeQueryAnnotation namedQuery = (NamedNativeQueryAnnotation) typeResource.addAnnotation(1, JPA.NAMED_NATIVE_QUERY, JPA.NAMED_NATIVE_QUERIES);
		namedQuery.setName("BAR");
		assertSourceContains("@NamedNativeQueries({@NamedNativeQuery(name=\"foo\", query = \"bar\", hints = @QueryHint(name=\"BAR\", value = \"FOO\"), resultClass = Foo.class, resultSetMapping = \"mapping\"),@NamedNativeQuery(name=\"BAR\")})");
		
		assertNull(typeResource.annotation(JPA.NAMED_NATIVE_QUERY));
		assertNotNull(typeResource.annotation(JPA.NAMED_NATIVE_QUERIES));
		assertEquals(2, CollectionTools.size(typeResource.annotations(JPA.NAMED_NATIVE_QUERY, JPA.NAMED_NATIVE_QUERIES)));
	}
	
	public void testAddNamedNativeQueryToBeginningOfList() throws Exception {
		IType jdtType = createTestNamedNativeQuery();
		JavaResourcePersistentType typeResource = buildJavaTypeResource(jdtType);
		
		NamedNativeQueryAnnotation namedQuery = (NamedNativeQueryAnnotation) typeResource.addAnnotation(1, JPA.NAMED_NATIVE_QUERY, JPA.NAMED_NATIVE_QUERIES);
		namedQuery.setName("BAR");
		assertSourceContains("@NamedNativeQueries({@NamedNativeQuery(name=\"foo\", query = \"bar\", hints = @QueryHint(name=\"BAR\", value = \"FOO\"), resultClass = Foo.class, resultSetMapping = \"mapping\"),@NamedNativeQuery(name=\"BAR\")})");		
		
		namedQuery = (NamedNativeQueryAnnotation) typeResource.addAnnotation(0, JPA.NAMED_NATIVE_QUERY, JPA.NAMED_NATIVE_QUERIES);
		namedQuery.setName("BAZ");
		assertSourceContains("@NamedNativeQueries({@NamedNativeQuery(name=\"BAZ\"),@NamedNativeQuery(name=\"foo\", query = \"bar\", hints = @QueryHint(name=\"BAR\", value = \"FOO\"), resultClass = Foo.class, resultSetMapping = \"mapping\"), @NamedNativeQuery(name=\"BAR\")})");		

		Iterator<JavaResourceNode> namedQueries = typeResource.annotations(JPA.NAMED_NATIVE_QUERY, JPA.NAMED_NATIVE_QUERIES);
		assertEquals("BAZ", ((NamedNativeQueryAnnotation) namedQueries.next()).getName());
		assertEquals("foo", ((NamedNativeQueryAnnotation) namedQueries.next()).getName());
		assertEquals("BAR", ((NamedNativeQueryAnnotation) namedQueries.next()).getName());

		assertNull(typeResource.annotation(JPA.NAMED_NATIVE_QUERY));
		assertNotNull(typeResource.annotation(JPA.NAMED_NATIVE_QUERIES));
		assertEquals(3, CollectionTools.size(typeResource.annotations(JPA.NAMED_NATIVE_QUERY, JPA.NAMED_NATIVE_QUERIES)));
	}
	
	public void testRemoveNamedNativeQueryCopyExisting() throws Exception {
		IType jdtType = createTestNamedNativeQuery();
		JavaResourcePersistentType typeResource = buildJavaTypeResource(jdtType);
		
		NamedNativeQueryAnnotation namedQuery = (NamedNativeQueryAnnotation) typeResource.addAnnotation(1, JPA.NAMED_NATIVE_QUERY, JPA.NAMED_NATIVE_QUERIES);
		namedQuery.setName("BAR");
		assertSourceContains("@NamedNativeQueries({@NamedNativeQuery(name=\"foo\", query = \"bar\", hints = @QueryHint(name=\"BAR\", value = \"FOO\"), resultClass = Foo.class, resultSetMapping = \"mapping\"),@NamedNativeQuery(name=\"BAR\")})");
		
		typeResource.removeAnnotation(1, JPA.NAMED_NATIVE_QUERY, JPA.NAMED_NATIVE_QUERIES);
		assertSourceContains("@NamedNativeQuery(name=\"foo\", query = \"bar\", hints = @QueryHint(name=\"BAR\", value = \"FOO\"), resultClass = Foo.class, resultSetMapping = \"mapping\")");
	}
}
