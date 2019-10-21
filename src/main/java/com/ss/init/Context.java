/**
 * 
 */
package com.ss.init;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ss.bean.Constants;
import com.ss.bean.TaskBean;

/**
 * @author Bikash
 * Apr 18, 2018
 */
public class Context {
    
    public static Context getContext() {
        synchronized (Context.class) {
            if(context == null)
                context = new Context();
        }
        return context;
    }

    /**
     * @return the masterConfigData
     */
    public Map<String, String> getMasterConfigData() {
        return masterConfigData;
    }

    /**
     * @param masterConfigData the masterConfigData to set
     */
    public void setMasterConfigData(Map<String, String> masterConfigData) {
        this.masterConfigData = masterConfigData;
    }

    /**
     * @return the taskList
     */
    public List<TaskBean> getTaskList() {
        return taskList;
    }

    /**
     * @param taskList the taskList to set
     */
    public void setTaskList(List<TaskBean> taskList) {
        this.taskList = taskList;
    }

    /**
     * @return the cfgCommandData
     */
    public List<String> getCfgCommandData() {
        return cfgCommandData;
    }

    /**
     * @param cfgCommandData the cfgCommandData to set
     */
    public void setCfgCommandData(List<String> cfgCommandData) {
        this.cfgCommandData = cfgCommandData;
    }
    
    public String getVariablesData(String variableName) {
        return this.masterConfigData.get(variableName);
    }

    /**
     * @param variablesData the variablesData to set
     */
    public void addVariablesData(String variableName ,String variablesData) {
        if(variableName != null)
            this.masterConfigData.put(variableName, variablesData);
    }

    /**
     * @param key
     * @return String
     */
    public String get(String key) {
        if(null == key || Constants.EMPTY_STRING.equals(key)) return null;
        return masterConfigData.get(key);
    }

    /**
     * @return the processObj
     */
    public Object getProcessObject(String processName) {
        return this.processObj.get(processName);
    }

    /**
     * @param processObj the processObj to set
     */
    public void addProcessObj(String processName, Object processObject) {
        if(processName != null)
            this.processObj.put(processName, processObject);
    }

    private static Context context = null;
    private Context() {}

    private Map<String, String> masterConfigData = new HashMap<String, String>();
    private List<TaskBean> taskList = new ArrayList<TaskBean>();
    private List<String> cfgCommandData = new ArrayList<String>();
    private Map<String, Object> processObj = new HashMap<String, Object>();
}
