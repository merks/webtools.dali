/*******************************************************************************
 * Copyright (c) 2012 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 * 
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.common.utility.tests.internal;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import junit.framework.TestCase;
import org.eclipse.jpt.common.utility.ExceptionHandler;
import org.eclipse.jpt.common.utility.internal.CompositeExceptionHandler;
import org.eclipse.jpt.common.utility.internal.PrintStreamExceptionHandler;

@SuppressWarnings("nls")
public class PrintStreamExceptionHandlerTests
	extends TestCase
{
	public PrintStreamExceptionHandlerTests(String name) {
		super(name);
	}

	public void testHandleException() {
		OutputStream out = new ByteArrayOutputStream();
		PrintStream stream = new PrintStream(out);
		ExceptionHandler exceptionHandler = new PrintStreamExceptionHandler(stream);
		Exception npe = new NullPointerException();
		exceptionHandler.handleException(npe);
		stream.flush();

		assertTrue(out.toString().contains("NullPointerException"));
	}

	public void testToString() {
		CompositeExceptionHandler exceptionHandler = new CompositeExceptionHandler();
		assertNotNull(exceptionHandler.toString());
	}
}
