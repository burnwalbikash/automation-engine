/**
 * 
 */
package com.ss.init;

import static com.ss.util.CommonUtils.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.TimeZone;

import com.ss.bean.Constants;
import com.ss.util.MyProperties;

/**
 * @author Bikash
 * Apr 18, 2018
 */
public class ContextInilializer implements IContext {
    
    /**
     * To initialize the Context
     */
    public void initialize() {
        initializeMasterConfig();
        initializeHotfixCommands();
        additionToMasterConfig();
        parseMasterConfig();
    }
    
    /* (non-Javadoc)
     * @see com.ss.init.IContext#initializeMasterConfig()
     */
    @Override
    public void initializeMasterConfig() {
        Context context = Context.getContext();
        context.setMasterConfigData(loadPropertiesToMap(Constants.MASTER_PROPERTIES_FILE_NAME));
        context.getMasterConfigData().put("hot-fix-home-folder", context.get("hot-fix-home-folder"));
    }

    /*
     * This method will parse the masterconfig data itself
     * since there may be a variable linked
     */
    private void parseMasterConfig() {
        Map<String, String> masterConfig = Context.getContext().getMasterConfigData();
        Iterator<String> it = masterConfig.keySet().iterator();
        while(it.hasNext()) {
            String key = it.next();
            String value = masterConfig.get(key);
            if(isPatternMatchAvailable(value, Constants.VARIABLE_PATTERN))
                masterConfig.put(key, replaceMatchFromMasterConfig(value, Constants.VARIABLE_PATTERN));
        }
    }
    
    /* (non-Javadoc)
     * @see com.ss.init.IContext#initializeGitProperty()
     */
    @Override
    public void initializeHotfixCommands() {
        MyProperties myProperties = new MyProperties();
        String hotfixCmdFilename = Context.getContext().getVariablesData(Constants.HOTFIX_CMD_FILE_NAME_VAR);
        if(hotfixCmdFilename == null || "".equals(hotfixCmdFilename))
            hotfixCmdFilename = Constants.HOTFIX_CMD_FILE_NAME;
        myProperties.load(hotfixCmdFilename);
        Context.getContext().setTaskList(myProperties.getData());
    }

    /* (non-Javadoc)
     * @see com.ss.init.IContext#initializeCfg()
     */
    @Override
    public void initializeCfg() {
    }
    
    /*
     * Add additional data to config
     */
    private void additionToMasterConfig() {
        Map<String, String> masterConfig = Context.getContext().getMasterConfigData();
        masterConfig.put(Constants.RESOURCE_PATH_VAR, Constants.RESOURCE_PATH);
        masterConfig.put(Constants.DEFAULT_TAG_VAR, masterConfig.get(Constants.DEFAULT_TAG_VAR+"-"+Constants.APP_VERSION));
        masterConfig.put(Constants.REPLACE_JAR_CMD_VAR, Constants.REPLACE_JAR_CMD_PATTERN);
        masterConfig.put(Constants.WEB_INSTALLATION_VAR, Constants.WEB_INSTALLATION_PATTERN);
        //Add additional parameter
        masterConfig.put("stash-password", Constants.STASH_PASSWORD); //Saving password if it requires later
        
        String stashPasswordEncoded = convertToEncodedString(Constants.STASH_PASSWORD);
        masterConfig.put(Constants.STASH_PASSWORD_ENCODED, stashPasswordEncoded);
        
        if(masterConfig.get(Constants.GIT_RELEASE_BRANCH_VAR)==null) {
            masterConfig.put(Constants.GIT_RELEASE_BRANCH_VAR, "release/"+masterConfig.get(Constants.APP_VERSION));
        }
        
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT"));        
        Date date = new Date();
        masterConfig.put(Constants.GIT_TIMESTAMP_VAR, sdf.format(date));
    }

}
