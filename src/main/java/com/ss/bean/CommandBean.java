package com.ss.bean;

/**
 * @author Bikash
 * Apr 18, 2018
 */
public class CommandBean {
    
    public CommandBean(){}
    
    public CommandBean(String command, Integer lineNumber) {
        this.command = command;
        this.lineNumber = lineNumber;
    }
    /**
     * @return the command
     */
    public String getCommand() {
        return command;
    }
    /**
     * @param command the command to set
     */
    public void setCommand(String command) {
        this.command = command;
    }
    /**
     * @return the isExecuted
     */
    public Boolean getIsExecuted() {
        return isExecuted;
    }
    /**
     * @param isExecuted the isExecuted to set
     */
    public void setIsExecuted(Boolean isExecuted) {
        this.isExecuted = isExecuted;
    }
    /**
     * @return the lineNumber
     */
    public Integer getLineNumber() {
        return lineNumber;
    }
    /**
     * @param lineNumber the lineNumber to set
     */
    public void setLineNumber(Integer lineNumber) {
        this.lineNumber = lineNumber;
    }
    
    /**
     * @return the errorLevel
     */
    public Integer getErrorLevel() {
        return errorLevel;
    }

    /**
     * @param errorLevel the errorLevel to set
     */
    public void setErrorLevel(Integer errorLevel) {
        this.errorLevel = errorLevel;
    }

    private String command;
    private Boolean isExecuted;
    private Integer errorLevel;
    private Integer lineNumber;
}
