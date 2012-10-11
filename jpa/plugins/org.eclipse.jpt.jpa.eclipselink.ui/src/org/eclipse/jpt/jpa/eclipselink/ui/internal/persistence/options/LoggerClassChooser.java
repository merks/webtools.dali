/*******************************************************************************
* Copyright (c) 2008, 2012 Oracle. All rights reserved.
* This program and the accompanying materials are made available under the
* terms of the Eclipse Public License v1.0, which accompanies this distribution
* and is available at http://www.eclipse.org/legal/epl-v10.html.
*
* Contributors:
*     Oracle - initial API and implementation
*******************************************************************************/
package org.eclipse.jpt.jpa.eclipselink.ui.internal.persistence.options;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jpt.common.ui.internal.JptCommonUiMessages;
import org.eclipse.jpt.common.ui.internal.widgets.ClassChooserComboPane;
import org.eclipse.jpt.common.ui.internal.widgets.Pane;
import org.eclipse.jpt.common.utility.internal.collection.CollectionTools;
import org.eclipse.jpt.common.utility.internal.iterator.IteratorTools;
import org.eclipse.jpt.common.utility.internal.iterator.TransformationIterator;
import org.eclipse.jpt.common.utility.internal.model.value.CompositeListValueModel;
import org.eclipse.jpt.common.utility.internal.model.value.PropertyAspectAdapter;
import org.eclipse.jpt.common.utility.internal.model.value.PropertyListValueModelAdapter;
import org.eclipse.jpt.common.utility.internal.model.value.SimpleCollectionValueModel;
import org.eclipse.jpt.common.utility.internal.model.value.SortedListValueModelAdapter;
import org.eclipse.jpt.common.utility.internal.transformer.TransformerAdapter;
import org.eclipse.jpt.common.utility.model.value.CollectionValueModel;
import org.eclipse.jpt.common.utility.model.value.ListValueModel;
import org.eclipse.jpt.common.utility.model.value.ModifiablePropertyValueModel;
import org.eclipse.jpt.common.utility.model.value.PropertyValueModel;
import org.eclipse.jpt.common.utility.transformer.Transformer;
import org.eclipse.jpt.jpa.eclipselink.core.context.persistence.Logger;
import org.eclipse.jpt.jpa.eclipselink.core.context.persistence.Logging;
import org.eclipse.jpt.jpa.eclipselink.ui.internal.EclipseLinkUiMessages;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.Hyperlink;
import com.ibm.icu.text.Collator;

/**
 *  LoggerComposite
 */
public class LoggerClassChooser extends ClassChooserComboPane<Logging>
{
	/**
	 * Creates a new <code>LoggerComposite</code>.
	 *
	 * @param parentPane The parent container of this one
	 * @param parent The parent container
	 */
	public LoggerClassChooser(Pane<? extends Logging> parentPane,
	                           Composite parent,
	                           Hyperlink hyperlink) {

		super(parentPane, parent, hyperlink);
	}
	
	@Override
	protected String getClassName() {
		if (this.getSubject().getLogger() == null) {
			return Logger.default_logger.getClassName();
		}
		return Logger.getLoggerClassName(this.getSubject().getLogger());
	}
    
	@Override
	protected IJavaProject getJavaProject() {
		return getSubject().getJpaProject().getJavaProject();
	}
    
    @Override
    protected String getSuperInterfaceName() {
    	return Logging.ECLIPSELINK_LOGGER_CLASS_NAME;
    }
    
	@Override
	protected ModifiablePropertyValueModel<String> buildTextHolder() {
		return new PropertyAspectAdapter<Logging, String>(this.getSubjectHolder(), Logging.LOGGER_PROPERTY) {
			@Override
			protected String buildValue_() {

				String name = this.subject.getLogger();
				if (name == null) {
					name = LoggerClassChooser.this.getDefaultValue(this.subject);
				}
				return name;
			}

			@Override
			protected void setValue_(String value) {

				if (getDefaultValue(this.subject).equals(value)) {
					value = null;
				}
				this.subject.setLogger(value);
			}
		};
	}

	private PropertyValueModel<String> buildDefaultLoggerHolder() {
		return new PropertyAspectAdapter<Logging, String>(this.getSubjectHolder(), Logging.DEFAULT_LOGGER) {
			@Override
			protected String buildValue_() {
				return LoggerClassChooser.this.getDefaultValue(this.subject);
			}
		};
	}

	private ListValueModel<String> buildDefaultLoggerListHolder() {
		return new PropertyListValueModelAdapter<String>(
			this.buildDefaultLoggerHolder()
		);
	}

	private String buildDisplayString(String loggerName) {

		switch (Logger.valueOf(loggerName)) {
			case default_logger: {
				return EclipseLinkUiMessages.LoggerComposite_default_logger;
			}
			case java_logger: {
				return EclipseLinkUiMessages.LoggerComposite_java_logger;
			}
			case server_logger: {
				return EclipseLinkUiMessages.LoggerComposite_server_logger;
			}
			default: {
				return null;
			}
		}
	}

	private Comparator<String> buildLoggerComparator() {
		return new Comparator<String>() {
			public int compare(String logger1, String logger2) {
				logger1 = buildDisplayString(logger1);
				logger2 = buildDisplayString(logger2);
				return Collator.getInstance().compare(logger1, logger2);
			}
		};
	}

	@Override
	protected Transformer<String, String> buildClassConverter() {
		return new TransformerAdapter<String, String>() {
			@Override
			public String transform(String value) {
				try {
					Logger.valueOf(value);
					value = buildDisplayString(value);
				}
				catch (Exception e) {
					// Ignore since the value is not a Logger
				}
				return value;
			}
		};
	}

	@Override
	protected ListValueModel<String> buildClassListHolder() {
		ArrayList<ListValueModel<String>> holders = new ArrayList<ListValueModel<String>>(2);
		holders.add(this.buildDefaultLoggerListHolder());
		holders.add(this.buildLoggersListHolder());
		return CompositeListValueModel.forModels(holders);
	}

	private Iterator<String> buildLoggers() {
		return new TransformationIterator<Logger, String>(IteratorTools.iterator(Logger.values())) {
			@Override
			protected String transform(Logger next) {
				return next.name();
			}
		};
	}

	private CollectionValueModel<String> buildLoggersCollectionHolder() {
		return new SimpleCollectionValueModel<String>(
			CollectionTools.collection(this.buildLoggers())
		);
	}

	private ListValueModel<String> buildLoggersListHolder() {
		return new SortedListValueModelAdapter<String>(
			this.buildLoggersCollectionHolder(),
			this.buildLoggerComparator()
		);
	}

	private String getDefaultValue(Logging subject) {
		String defaultValue = subject.getDefaultLogger();

		if (defaultValue != null) {
			return NLS.bind(
				JptCommonUiMessages.DefaultWithOneParam,
				defaultValue
			);
		}
		return JptCommonUiMessages.DefaultEmpty;
	}
	
	@Override
	protected void setClassName(String className) {
		this.getSubject().setLogger(className);
	}
}