package com.ss.bean;

/**
 * @author Bikash
 * Apr 18, 2018
 */
public class Constants {

    public static final String EMPTY_STRING = "";
    public static final int MULTILINE = 0x08;
    public static final String APP_VERSION = "app-version";
    public static final String APP_RPTAB_NAME = "app-rptab-name";
    public static final String TAGGED_VERSION_VAR = "tagged-version";
    public static final String GIT_RELEASE_BRANCH_VAR = "git-release-branch";
    public static final String PREV_TAGGED_VERSION_VAR = "pre-tagged-version";
    public static final String JIRA_ID_STARTS_WITH = "jira_id_start_with";
    public static final String STASH_PASSWORD_ENCODED = "stash-password-encoded";

    //RegEx patterns
    public static final String VARIABLE_PATTERN = "(\\$\\{([A-Z a-z 0-9 -]*\\w+)\\})";
    public static final String CMD_OUTPUT_PATTERN = "echo Start of (.*)+\n+((?:.|\n)*)(?:echo End of \1)";
    public static final String CMD_OUTPUT_PATTERN_CUSTOM = "echo (?:.*)+\n+(.*)";
    public static final String TASK_NAME_PATTERN = "(.*)(?:-[0-9]{1,})";
    public static final String EXTRACT_FILE_NAME_PATTERN = "file\\((.*)\\)";
    public static final String CS_FILENAME_PATTERN = "client+.*\\/+(.*\\.cs)$";
    public static final String HOTFIX_NUMBER_PATTERN = "(HF[0-9]{1,})";
    public static final String REPLACE_JAR_CMD_PATTERN = "(\n{1})(?:#\n)(?:# Install the entire bin directory.)";
    public static final String WEB_INSTALLATION_PATTERN = "((?:\\={1,80})(?:\n)(?:\\s{1,19})W E B(?:\\s{1,3})I N S T A L L A T I O N(?:\\s{1,3})N O T E S(?:.*)(?:\n)(?:\\={1,80})(?:(?:\n.*){1,}))(?:\\={1,80}\n)(?:\\s{1,17})Reporting and SS Installation Notes";
    public static final String ERRORLEVEL_PATTERN = "ERRORLEVEL=(.*)";
    //Only first occurance if depends on is applied- ^([^,]+)(?:-[0-9]{1,})
    //Match as many as there in a line- ([^,]+)(?:-[0-9]{1,})


    public static final String RESOURCE_PATH_VAR = "resource_path_var";
    public static final String LOG_FILE_NAME = "app-commit-info.log";
    public static final String MASTER_PROPERTIES_FILE_NAME = "master-config.properties";
    public static final String HOTFIX_CMD_FILE_NAME = "commands.hf";
    public static final String HOTFIX_CMD_FILE_NAME_VAR = "hotfix-cmd-filename";
    public static final String DEFAULT_TAG_VAR = "default-tag";
    public static final String HOTFIX_NUMBER_VAR = "hotfix-number";
    public static final String CS_FILE_CHANGED_VAR = "cs-file-changed";
    public static final String OLD_DDL_FILENAMES_VAR = "old-ddl-filenames";
    public static final String NEW_DDL_FILENAMES_VAR = "new-ddl-filenames";
    public static final String GIT_TIMESTAMP_VAR = "git-timestamp";
    
    public static final String CMD_OUTPUT_FILENAME_ONLY = "cmd_output_";
    public static final String CMD_FULL_OUTPUT_FILENAME_ONLY = "cmd_full_output_";
    public static final String LOG_FILE_EXTENSION = ".log";
    public static final String REPLACE_JAR_CMD_VAR = "replace-jar-cmd";
    public static final String WEB_INSTALLATION_VAR = "web-installation-pattern";
    public static final String READ_PROGRESSIVE_VAR = "read-progressive";
    public static final String FULL_LOG_FILENAME_VAR = "full-log-filename";
    
    //Message
    public static final String ERROR_MESSAGE = "****** An error while executing the process. Further Execution has been terminated ******";
    public static final String SUCCESSFUL_MESSAGE = "****** Program is exiting safely ******";
    
    //Non-final variable can be initialized based on user choice
    public static String RESOURCE_PATH = null;
    public static String STASH_PASSWORD = null;
}
