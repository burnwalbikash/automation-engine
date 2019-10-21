package com.ss.bean;

import java.util.List;

import com.ss.util.CommonUtils;

/**
 * @author Bikash
 * Apr 18, 2018
 */
public class TaskBean {

    public TaskBean(){}
    
    public TaskBean(String taskId, Integer lineNumber) {
        this.lineNumber = lineNumber;
        this.taskId = taskId;
        setTaskName();
    }
    
    public TaskBean(String taskId, Integer lineNumber, List<CommandBean> listCommandBean) {
        this.lineNumber = lineNumber;
        this.taskId = taskId;
        this.listCommandBean = listCommandBean;
        setTaskName();
    }
    /**
     * @return the taskId
     */
    public String getTaskId() {
        return taskId;
    }
    /**
     * @param taskId the taskId to set
     */
    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }
    /**
     * @return the taskName
     */
    public String getTaskName() {
        return taskName;
    }
    /**
     * @param taskName the taskName to set
     */
    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }
    
    /**
     * @param taskName the taskName to set
     */
    private void setTaskName() {
        if(CommonUtils.isPatternMatchAvailable(this.taskId, Constants.TASK_NAME_PATTERN))
            setTaskName(CommonUtils.getMatchesBasedOnPattern(this.taskId, Constants.TASK_NAME_PATTERN, null).get(0));
    }
    
    /**
     * @return the listCommandBean
     */
    public List<CommandBean> getListCommandBean() {
        return listCommandBean;
    }
    
    /**
     * @param listCommandBean the listCommandBean to set
     */
    public void setListCommandBean(List<CommandBean> listCommandBean) {
        this.listCommandBean = listCommandBean;
    }
    
    /**
     * @return the result
     */
    public Object getResult() {
        return result;
    }

    /**
     * @param result the result to set
     */
    public void setResult(Object result) {
        this.result = result;
    }

    private String taskId; //taskname as there in commands.hf, this taskid can be used to refer furthur to another task
    private Integer lineNumber;
    private String taskName; //Actual task name
    private static Integer taskSerialNo=1; //To keep track of sequence and avoid duplicy in Map key
    private List<CommandBean> listCommandBean;
    private Object result;
}
