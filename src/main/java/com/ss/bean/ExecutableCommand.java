package com.ss.bean;

/**
 * @author Bikash
 * Apr 18, 2018
 */
public class ExecutableCommand {
    
    public ExecutableCommand() {}
    
    public ExecutableCommand(String command) {
        this.command = command;
    }
    
    public ExecutableCommand(String command, Boolean isRequiredUserInput) {
        this.command = command;
        this.isRequiredUserInput = isRequiredUserInput;
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
     * @return the isProcessed
     */
    public Boolean getIsProcessed() {
        return isProcessed;
    }
    /**
     * @param isProcessed the isProcessed to set
     */
    public void setIsProcessed(Boolean isProcessed) {
        this.isProcessed = isProcessed;
    }
    /**
     * @return the isRequiredUserInput
     */
    public Boolean getIsRequiredUserInput() {
        return isRequiredUserInput;
    }
    /**
     * @param isRequiredUserInput the isRequiredUserInput to set
     */
    public void setIsRequiredUserInput(Boolean isRequiredUserInput) {
        this.isRequiredUserInput = isRequiredUserInput;
    }
    /**
     * @return the output
     */
    public String getOutput() {
        return output;
    }
    /**
     * @param output the output to set
     */
    public void setOutput(String output) {
        this.output = output;
    }
    private String command = null;
    private Boolean isProcessed = false;
    private Boolean isRequiredUserInput = false;
    private String output = null; //Optional field
}
