/*******************************************************************************
* Copyright (c) 2007, 2008 Oracle. All rights reserved.
* This program and the accompanying materials are made available under the
* terms of the Eclipse Public License v1.0, which accompanies this distribution
* and is available at http://www.eclipse.org/legal/epl-v10.html.
*
* Contributors:
*     Oracle - initial API and implementation
*******************************************************************************/
package org.eclipse.jpt.eclipselink.ui.internal;

/**
 *  EclipseLinkUiMessages
 */
import org.eclipse.osgi.util.NLS;

public class EclipseLinkUiMessages extends NLS
{
	private static final String BUNDLE_NAME = "eclipselink_ui"; //$NON-NLS-1$

	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, EclipseLinkUiMessages.class);
	}

	public static String Boolean_True;
	public static String Boolean_False;

	public static String DefaultWithoutValue;
	public static String DefaultWithValue;

	public static String PersistenceXmlTabFolder_defaultEmpty;
	public static String PersistenceXmlTabFolder_defaultWithOneParam;

	public static String PersistenceXmlGeneralTab_name;
	public static String PersistenceXmlGeneralTab_provider;
	public static String PersistenceXmlGeneralTab_browse;
	public static String PersistenceXmlGeneralTab_description;

	// Connection
	public static String PersistenceXmlConnectionTab_title;
	public static String PersistenceXmlConnectionTab_sectionTitle;
	public static String PersistenceXmlConnectionTab_sectionDescription;
	public static String PersistenceXmlConnectionTab_defaultWithOneParam;
	public static String PersistenceXmlConnectionTab_defaultEmpty;

	public static String PersistenceXmlConnectionTab_transactionTypeLabel;

	public static String ConnectionPropertiesComposite_Database_GroupBox;

	public static String JdbcPropertiesComposite_EclipseLinkConnectionPool_GroupBox;

	public static String JdbcConnectionPropertiesComposite_ConnectionDialog_Message;
	public static String JdbcConnectionPropertiesComposite_ConnectionDialog_Title;

	public static String TransactionTypeComposite_jta;
	public static String TransactionTypeComposite_resource_local;

	public static String PersistenceXmlConnectionTab_nativeSqlLabel;
	public static String PersistenceXmlConnectionTab_nativeSqlLabelDefault;

	public static String PersistenceXmlConnectionTab_batchWritingLabel;

	public static String BatchWritingComposite_none;
	public static String BatchWritingComposite_jdbc;
	public static String BatchWritingComposite_buffered;
	public static String BatchWritingComposite_oracle_jdbc;

	public static String PersistenceXmlConnectionTab_cacheStatementsLabel;

	public static String PersistenceXmlConnectionTab_jtaDataSourceLabel;
	public static String PersistenceXmlConnectionTab_nonJtaDataSourceLabel;

	public static String PersistenceXmlConnectionTab_driverLabel;
	public static String PersistenceXmlConnectionTab_urlLabel;
	public static String PersistenceXmlConnectionTab_userLabel;
	public static String PersistenceXmlConnectionTab_passwordLabel;
	public static String PersistenceXmlConnectionTab_bindParametersLabel;
	public static String PersistenceXmlConnectionTab_bindParametersLabelDefault;

	public static String PersistenceXmlConnectionTab_readConnectionsSharedLabel;
	public static String PersistenceXmlConnectionTab_readConnectionsSharedLabelDefault;
	public static String PersistenceXmlConnectionTab_readConnectionsSectionTitle;
	public static String PersistenceXmlConnectionTab_readConnectionsMinLabel;
	public static String PersistenceXmlConnectionTab_readConnectionsMaxLabel;
	public static String PersistenceXmlConnectionTab_writeConnectionsSectionTitle;
	public static String PersistenceXmlConnectionTab_writeConnectionsMinLabel;
	public static String PersistenceXmlConnectionTab_writeConnectionsMaxLabel;

	// SchemaGeneration
	public static String PersistenceXmlSchemaGenerationTab_title;
	public static String PersistenceXmlSchemaGenerationTab_sectionTitle;
	public static String PersistenceXmlSchemaGenerationTab_sectionDescription;
	public static String PersistenceXmlSchemaGenerationTab_defaultWithOneParam;
	public static String PersistenceXmlSchemaGenerationTab_defaultEmpty;

	public static String PersistenceXmlSchemaGenerationTab_ddlGenerationTypeLabel;
	public static String PersistenceXmlSchemaGenerationTab_outputModeLabel;

	public static String PersistenceXmlSchemaGenerationTab_createDdlFileNameLabel;
	public static String PersistenceXmlSchemaGenerationTab_dropDdlFileNameLabel;

	public static String OutputModeComposite_both;
	public static String OutputModeComposite_sql_script;
	public static String OutputModeComposite_database;

	public static String DdlGenerationTypeComposite_none;
	public static String DdlGenerationTypeComposite_create_tables;
	public static String DdlGenerationTypeComposite_drop_and_create_tables;

	public static String PersistenceXmlSchemaGenerationTab_ddlGenerationLocationLabel;

	public static String DdlGenerationLocationComposite_dialogTitle;
	public static String DdlGenerationLocationComposite_dialogMessage;

	// Caching
	public static String PersistenceXmlCachingTab_title;
	public static String PersistenceXmlCachingTab_sectionTitle;
	public static String PersistenceXmlCachingTab_sectionDescription;

	public static String PersistenceXmlCachingTab_defaultCacheTypeLabel;
	public static String PersistenceXmlCachingTab_cacheTypeLabel;

	public static String PersistenceXmlCachingTab_defaultSharedCacheLabel;
	public static String PersistenceXmlCachingTab_sharedCacheLabel;

	public static String PersistenceXmlCachingTab_defaultSharedCacheDefaultLabel;
	public static String PersistenceXmlCachingTab_sharedCacheDefaultLabel;

	public static String CacheSizeComposite_cacheSize;

	public static String CacheTypeComposite_full;
	public static String CacheTypeComposite_hard_weak;
	public static String CacheTypeComposite_none;
	public static String CacheTypeComposite_soft;
	public static String CacheTypeComposite_soft_weak;
	public static String CacheTypeComposite_weak;

	public static String DefaultCacheSizeComposite_defaultCacheSize;

	public static String DefaultCacheTypeComposite_full;
	public static String DefaultCacheTypeComposite_hard_weak;
	public static String DefaultCacheTypeComposite_none;
	public static String DefaultCacheTypeComposite_soft;
	public static String DefaultCacheTypeComposite_soft_weak;
	public static String DefaultCacheTypeComposite_weak;

	public static String EntityDialog_selectEntity;
	public static String EntityDialog_name;

	public static String CachingEntityListComposite_groupTitle;
	public static String CachingEntityListComposite_editButton;

	public static String CachingEntityListComposite_dialogMessage;
	public static String CachingEntityListComposite_dialogTitle;

	// Customization
	public static String PersistenceXmlCustomizationTab_title;
	public static String PersistenceXmlCustomizationTab_sectionTitle;
	public static String PersistenceXmlCustomizationTab_sectionDescription;

	public static String PersistenceXmlCustomizationTab_throwExceptionsLabelDefault;
	public static String PersistenceXmlCustomizationTab_throwExceptionsLabel;

	public static String PersistenceXmlCustomizationTab_weavingLabel;

	public static String PersistenceXmlCustomizationTab_weavingLazyLabelDefault;
	public static String PersistenceXmlCustomizationTab_weavingLazyLabel;

	public static String PersistenceXmlCustomizationTab_weavingChangeTrackingLabelDefault;
	public static String PersistenceXmlCustomizationTab_weavingChangeTrackingLabel;

	public static String PersistenceXmlCustomizationTab_weavingFetchGroupsLabelDefault;
	public static String PersistenceXmlCustomizationTab_weavingFetchGroupsLabel;

	public static String WeavingComposite_true_;
	public static String WeavingComposite_false_;
	public static String WeavingComposite_static_;

	public static String CustomizationEntityListComposite_groupTitle;
	public static String CustomizationEntityListComposite_editButton;

	public static String CustomizationEntityListComposite_dialogMessage;
	public static String CustomizationEntityListComposite_dialogTitle;
	
	public static String PersistenceXmlCustomizationTab_customizerLabel;
	public static String PersistenceXmlCustomizationTab_sessionCustomizerLabel;

	// Logging
	public static String PersistenceXmlLoggingTab_title;
	public static String PersistenceXmlLoggingTab_sectionTitle;
	public static String PersistenceXmlLoggingTab_sectionDescription;
	public static String PersistenceXmlLoggingTab_defaultWithOneParam;
	public static String PersistenceXmlLoggingTab_defaultEmpty;

	public static String PersistenceXmlLoggingTab_loggingLevelLabel;

	public static String LoggingLevelComposite_off;
	public static String LoggingLevelComposite_severe;
	public static String LoggingLevelComposite_warning;
	public static String LoggingLevelComposite_info;
	public static String LoggingLevelComposite_config;
	public static String LoggingLevelComposite_fine;
	public static String LoggingLevelComposite_finer;
	public static String LoggingLevelComposite_finest;

	public static String PersistenceXmlLoggingTab_loggerLabel;

	public static String LoggerComposite_default_logger;
	public static String LoggerComposite_java_logger;
	public static String LoggerComposite_server_logger;

	public static String PersistenceXmlLoggingTab_timestampLabel;
	public static String PersistenceXmlLoggingTab_timestampLabelDefault;
	public static String PersistenceXmlLoggingTab_threadLabel;
	public static String PersistenceXmlLoggingTab_threadLabelDefault;
	public static String PersistenceXmlLoggingTab_sessionLabel;
	public static String PersistenceXmlLoggingTab_sessionLabelDefault;
	public static String PersistenceXmlLoggingTab_exceptionsLabel;
	public static String PersistenceXmlLoggingTab_exceptionsLabelDefault;
	public static String PersistenceXmlLoggingTab_logFileLabel;
	public static String PersistenceXmlLoggingTab_logFileLabelDefault;
	public static String PersistenceXmlLoggingTab_loggersLabel;

	// Session Options
	public static String PersistenceXmlOptionsTab_title;
	public static String PersistenceXmlOptionsTab_sectionTitle;
	public static String PersistenceXmlOptionsTab_sectionDescription;
	public static String PersistenceXmlOptionsTab_defaultWithOneParam;
	public static String PersistenceXmlOptionsTab_defaultEmpty;

	public static String PersistenceXmlOptionsTab_sessionName;
	public static String PersistenceXmlOptionsTab_sessionsXml;

	public static String PersistenceXmlOptionsTab_includeDescriptorQueriesLabel;
	public static String PersistenceXmlOptionsTab_includeDescriptorQueriesLabelDefault;

	public static String PersistenceXmlOptionsTab_eventListenerLabel;
	public static String PersistenceXmlOptionsTab_targetDatabaseLabel;

	public static String TargetDatabaseComposite_attunity;
	public static String TargetDatabaseComposite_auto;
	public static String TargetDatabaseComposite_cloudscape;
	public static String TargetDatabaseComposite_database;
	public static String TargetDatabaseComposite_db2;
	public static String TargetDatabaseComposite_db2mainframe;
	public static String TargetDatabaseComposite_dbase;
	public static String TargetDatabaseComposite_derby;
	public static String TargetDatabaseComposite_hsql;
	public static String TargetDatabaseComposite_informix;
	public static String TargetDatabaseComposite_javadb;
	public static String TargetDatabaseComposite_mysql4;
	public static String TargetDatabaseComposite_oracle;
	public static String TargetDatabaseComposite_pointbase;
	public static String TargetDatabaseComposite_postgresql;
	public static String TargetDatabaseComposite_sqlanywhere;
	public static String TargetDatabaseComposite_sqlserver;
	public static String TargetDatabaseComposite_sybase;
	public static String TargetDatabaseComposite_timesten;

	// DDLGeneration
	public static String EclipseLinkDDLGeneratorUi_generatingDDLWarningTitle;
	public static String EclipseLinkDDLGeneratorUi_generatingDDLWarningMessage;

	private EclipseLinkUiMessages() {
		throw new UnsupportedOperationException();
	}
}