/**
 * Only one instance should run in a JVM memory
 */
package com.ss.processor;

import static com.ss.bean.Constants.*;
import static com.ss.util.CommonUtils.*;

import java.util.ArrayList;
import java.util.List;

import com.ss.bean.CommandBean;
import com.ss.bean.Constants;
import com.ss.bean.TaskBean;
import com.ss.init.Context;
import com.ss.init.ContextInilializer;
import com.ss.util.ReadProgressiveFile;
import com.ss.util.ZipFunction;

/**
 * @author Bikash
 * Apr 18, 2018
 */
public class Processor {
    
    public static Processor getProcessorObj() {
        
        synchronized (Processor.class) {
            if(processor == null)
                processor = new Processor();
        }
        return processor;
    }
    
    public synchronized void init() {
        if(isInitCalled) return;
        
        isInitCalled = true;
        ContextInilializer contextInilializer = new ContextInilializer();
        contextInilializer.initialize();
        
        deleteFolder(Context.getContext().getVariablesData(RESOURCE_PATH_VAR)+"\\temp", "hotfix_");
        deleteFolder(Context.getContext().getVariablesData(RESOURCE_PATH_VAR)+"\\temp", "report.txt");
        
        String fullLogFilename = Context.getContext().getVariablesData(Constants.RESOURCE_PATH_VAR)+"\\temp\\"
                    + Constants.CMD_FULL_OUTPUT_FILENAME_ONLY+System.nanoTime()+Constants.LOG_FILE_EXTENSION;
        Context.getContext().addVariablesData(Constants.FULL_LOG_FILENAME_VAR, fullLogFilename);

        createFile(fullLogFilename);
        
        readProgressive = new ReadProgressiveFile(fullLogFilename);
        Thread readProgressiveThread = new Thread(readProgressive);
        readProgressiveThread.start();
        Context.getContext().addProcessObj(Constants.READ_PROGRESSIVE_VAR, readProgressive);
    }
    
    public synchronized void execute() {
        init();
        //Call the console or java function to execute the result.
        List<TaskBean> taskList = Context.getContext().getTaskList(); 
        
        for(TaskBean task : taskList) {
            //TODO: Handle the return output
            while(!task.getTaskName().equals("build") && !readProgressive.canExecuteJavaProgram) {
                try {
                    Thread.sleep(4000); //wait for 4 sec
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("Task " + task.getTaskId() + " has start executing");
            task.setResult(execute(task.getTaskName(), task.getListCommandBean()));
        }
        
        generateReport();
        exit();
        
        //TODO: Post-exit process should start
        //1. Check whether everything is fine or not
        //2. Arrange the log to user readable format
        //3. Write the result to a file(.hml/.xls)
    }
    
    private synchronized Object execute(String taskName, List<CommandBean> cmdOrInstructions) {
        switch(taskName) {
            case "build":
                    return buildCommand(cmdOrInstructions);
            
            case "initialize-product-path":
                    return initializeProductPath(cmdOrInstructions);
            
            case "increment-tagged-version":
                    return incrementAndSetTaggedVersionToContext(cmdOrInstructions);
    
            case "folder-check":
                    return folderCheckFunction(cmdOrInstructions);
    
            case "get-directory-list":
                    return writeDirectoryListToFile(cmdOrInstructions);
            
            case "check-diff-on-files":
                    return checkDifferenceOnFiles(cmdOrInstructions);
            
            case "perform-check-operation":
                    return performCheckOperation(cmdOrInstructions);
            
            case "check-if-contains":
                    return checkContentInFiles(cmdOrInstructions);
            
            case "fetch-hotfix-number":
                    return fetchHotFixNumber(cmdOrInstructions);
            
            case "replace-if-contains":
                    return replaceIfContains(cmdOrInstructions);
            
            case "zip-funcion":
                    return zipFunction(cmdOrInstructions);
            
            default: //do not do anything if command is not defined
                return null;
        }
    }
    
    /*
     * This method will create the report which we need to generate the final xls sheet
     * This method should not get called if entire process doesn't run smoothly
     */
    private void generateReport() {
        String templateFilename = Context.getContext().getVariablesData(RESOURCE_PATH_VAR)+"\\report-template.txt";
        String reportFilename = Context.getContext().getVariablesData(RESOURCE_PATH_VAR)+"\\temp\\report.txt";
        createFile(reportFilename);
        
        String[] reportRowData = loadFileToBuffer(templateFilename, true, true).toString().split("\n");
        StringBuffer finalReport = new StringBuffer();
        for(String rowData : reportRowData) {
            if(!("".equals(rowData) || rowData.startsWith("#"))) {
                rowData = replaceMatchFromMasterConfig(rowData,    Constants.VARIABLE_PATTERN);
            }
            finalReport.append(rowData).append("\n");
        }
        
        System.out.println("************** Final Report ********************");
        System.out.println("\n" + finalReport.toString() + "\n");
        System.out.println("************** Generated Report ****************");
        writeStringToFile(finalReport.toString(), reportFilename);
    }
    
    /*
     * This will run the command on cmd prompt
     */
    private synchronized Object buildCommand(List<CommandBean> cmds) {
        commandExecutor.log(classObj, "Stared executing system commands", 0);
        commandExecutor.execute(cmds);
        commandExecutor.log(classObj, "Done with executing system commands", 0);
        return null;
    }
    
    /*
     * 
     */
    private synchronized Object initializeProductPath(List<CommandBean> cmds) {
        commandExecutor.log(classObj, "Stared initializing product path", 0);
        String filename1 = getValueFromCommandBeanList(cmds, 0);
               filename1 = replaceMatchFromMasterConfig(filename1, VARIABLE_PATTERN);
        List<String> folderPathList = loadFileToList(filename1, true);
        for(String in : folderPathList) {
            if( !( null == in || "".equals(in) ) && in.split("=").length == 2 && in.split(" ").length <= 3){
                Context.getContext().addVariablesData(in.split("=")[0].trim(), in.split("=")[1].trim());
            }
        }
        commandExecutor.log(classObj, "Done with initializing product path", 0);
        return null;
    }
    
    /*
     * This method will write all the directory list to a file
     */
    private synchronized Object writeDirectoryListToFile(List<CommandBean> cmds) {
        commandExecutor.log(classObj, "Stared writing directory list to file", 0);
        StringBuffer folderNames = getFileList( replaceMatchFromMasterConfig(getValueFromCommandBeanList(cmds, 0) , VARIABLE_PATTERN) );
        writeStringBufferToFile(folderNames, replaceMatchFromMasterConfig(getValueFromCommandBeanList(cmds, 1), VARIABLE_PATTERN) );
        commandExecutor.log(classObj, "Done with writing directory list to file", 0);
        return null;
    }
    
    /*
     * This method will perform a check operation
     */
    private synchronized Boolean performCheckOperation(List<CommandBean> cmds) {
        commandExecutor.log(classObj, "Stared performing the check operation", 0);
        String filename1 = getValueFromCommandBeanList(cmds, 0);
               filename1 = replaceMatchFromMasterConfig(filename1, VARIABLE_PATTERN);
        String filename2 = getValueFromCommandBeanList(cmds, 1);
               filename2 = replaceMatchFromMasterConfig(filename2, VARIABLE_PATTERN);
        String listofAllFiles = loadFileToBuffer(filename1, true, true).toString();
        List<String> listofCSFiles = (listofAllFiles==null||"".equals(listofAllFiles))? new ArrayList<String>() : 
            getMatchesBasedOnPattern(listofAllFiles, CS_FILENAME_PATTERN, Constants.MULTILINE);
        Integer noofFileCommitted = 0;
        try {
            noofFileCommitted = Integer.parseInt(loadFileToList(filename2, true).get(0));
        } catch (NumberFormatException nfe) {
            commandExecutor.log(classObj, "There is NumberFormatException while doing check operation", 1);
            System.out.println("NumberFormatException Occured ["+nfe.getMessage()+"]");
            
        } catch (NullPointerException e) {
            commandExecutor.log(classObj, "There is NullPointerException while doing check operation", 1);
            System.out.println("NullPointerException Occured ["+e.getMessage()+"]");
            
        } catch (IndexOutOfBoundsException e) {
            commandExecutor.log(classObj, "There is IndexOutOfBoundsException while doing check operation", 1);
            System.out.println("IndexOutOfBoundsException Occured ["+e.getMessage()+"]");
            
        }

        Context.getContext().addVariablesData(Constants.CS_FILE_CHANGED_VAR, listofCSFiles.toString());
        if(listofCSFiles.size() + 2 == noofFileCommitted) {
            commandExecutor.log(classObj, "Done with the check operation", 0);
            return true;
        }
        
        commandExecutor.log(classObj, "Check performace doesn't seems ok, we are terminating the process."
                + "\nCount of cs file changed- "+listofCSFiles.size()+" , count of file committed- "+noofFileCommitted, 1);
        return false;
    } 
    
    /*
     * Difference in two files and set the value to context
     */
    private synchronized List<String> checkDifferenceOnFiles(List<CommandBean> cmds) {
        commandExecutor.log(classObj, "Stared performing difference on files", 0);
        String filename1 = getValueFromCommandBeanList(cmds, 0);
               filename1 = replaceMatchFromMasterConfig(filename1, VARIABLE_PATTERN);
        String filename2 = getValueFromCommandBeanList(cmds, 1);
               filename2 = replaceMatchFromMasterConfig(filename2, VARIABLE_PATTERN);
        List<String> fileDiffList = getDifferenceInFiles(filename1, filename1);
        if(fileDiffList.size() == 0) {
            commandExecutor.log(classObj, "No .cs file has changed and hence no ddl file changed", 0);
        } else {
            String oldddlFilename = "";
            String newddlFilename = "";
            for(String s : fileDiffList) {
                oldddlFilename += s.split(":")[0] + ", ";
                newddlFilename += s.split(":")[1] + ", ";                
            }
            Context.getContext().addVariablesData(Constants.OLD_DDL_FILENAMES_VAR, oldddlFilename);
            Context.getContext().addVariablesData(Constants.NEW_DDL_FILENAMES_VAR, newddlFilename);
        }
        
        commandExecutor.log(classObj, "Done with performing difference on files", 0);
        return fileDiffList;
    }
    
    
    /*
     * 
     */
    private synchronized Boolean checkContentInFiles(List<CommandBean> cmds) {
        commandExecutor.log(classObj, "Stared checking content on files", 0);
        String content = getValueFromCommandBeanList(cmds, 0);
        String folderPath = getValueFromCommandBeanList(cmds, 1);
        String fileList[] = getValueFromCommandBeanList(cmds, 2).split(",");
        for(String file : fileList) {
            if(!checkContentInFile(content, folderPath+"/"+file)) {
                commandExecutor.log(classObj, "Content is not there in file-"+file+" , content-"+content
                        +", process will be terminated.", 1);
                return false;
            }
        }
        commandExecutor.log(classObj, "Done with checking content on files", 0);
        return true;
    }
    
    /*
     * This method will fetch the hotfix number and set to context
     */
    private synchronized String fetchHotFixNumber(List<CommandBean> cmds) {
        commandExecutor.log(classObj, "Stared fetching hotfix number", 0);
        String filename1 = getValueFromCommandBeanList(cmds, 0);
               filename1 = replaceMatchFromMasterConfig(filename1, VARIABLE_PATTERN);
        StringBuffer fileContent = loadFileToBuffer(filename1, true, true);
        List<String> hotfixNumberList = getMatchesBasedOnPattern(fileContent.toString(), HOTFIX_NUMBER_PATTERN, null);
        String hotfixNumber = null; 
        if(hotfixNumberList==null || hotfixNumberList.size()==0)
            commandExecutor.log(classObj, "Not able to fetch the hotfix number from given input-"+fileContent.toString()
                +", process will be terminated.", 1);
        else 
            hotfixNumberList.get(0);
        Context.getContext().addVariablesData(HOTFIX_NUMBER_VAR, hotfixNumber);
        commandExecutor.log(classObj, "Done with fetching hotfix number", 0);
        return hotfixNumber;
    }
    
    /*
     * This method will increment the tagged version and set to context
     */    
    private synchronized String incrementAndSetTaggedVersionToContext(List<CommandBean> cmds) {
        commandExecutor.log(classObj, "Stared incrementing the tagged version", 0);
        String filename1 = getValueFromCommandBeanList(cmds, 0);
               filename1 = replaceMatchFromMasterConfig(filename1, VARIABLE_PATTERN);
        String filename2 = getValueFromCommandBeanList(cmds, 1);
               filename2 = replaceMatchFromMasterConfig(filename2, VARIABLE_PATTERN );
        
        String oldTaggedVersion = loadFileToList(filename1, true).get(0);
        Context.getContext().addVariablesData(PREV_TAGGED_VERSION_VAR, oldTaggedVersion);
        String incrementedTaggedVersion = incrementTaggedVersion(oldTaggedVersion);
        Context.getContext().addVariablesData(TAGGED_VERSION_VAR, incrementedTaggedVersion);
        writeStringToFile(incrementedTaggedVersion, filename1);
        writeStringToFile(incrementedTaggedVersion, filename2);
        commandExecutor.log(classObj, "Done with incrementing tagged version", 0);
        return incrementedTaggedVersion;
    }
    
    /*
     * Replace the content if there in given file or input text
     */
    private synchronized Integer replaceIfContains(List<CommandBean> cmds) {
        commandExecutor.log(classObj, "Stared replacing the content if any", 0);
        String[] inputContents = replaceMatchFromMasterConfig(getValueFromCommandBeanList(cmds, 0), VARIABLE_PATTERN).split(",");
        String replaceContent = replaceMatchFromMasterConfig(getValueFromCommandBeanList(cmds, 1), VARIABLE_PATTERN);
        if(replaceContent.startsWith("file")) {
            List<String> replaceContentFilename = getMatchesBasedOnPattern(replaceContent, EXTRACT_FILE_NAME_PATTERN, null);
            if(replaceContentFilename != null && replaceContentFilename.size()==1) {
                replaceContent = loadFileToBuffer(replaceContentFilename.get(0),true, true).toString();
            }
        }
        
        String filename = replaceMatchFromMasterConfig(getValueFromCommandBeanList(cmds, 2), VARIABLE_PATTERN);
        for(String content : inputContents) {
            checkContentAndReplaceInFile(content, replaceContent, filename);
        }
        commandExecutor.log(classObj, "Done with replacing the content if any", 0);
        return 0;
    }
    
    /*
     * This method will check the count inside the folder
     */
    private synchronized Boolean folderCheckFunction(List<CommandBean> cmds) {
        commandExecutor.log(classObj, "Stared performing folder check opration", 0);
        String path= getValueFromCommandBeanList(cmds, 0);
        path = replaceMatchFromMasterConfig(path, VARIABLE_PATTERN);
        Integer count = Integer.parseInt(getValueFromCommandBeanList(cmds, 1).trim());
        commandExecutor.log(classObj, "Done with folder check opration", 0);
        return folderCheck(path) == count;
    }
    
    /*
     * This method will zip a folder
     */
    private synchronized Boolean zipFunction(List<CommandBean> cmds) {
        commandExecutor.log(classObj, "Stared compressing the folder", 0);
        String folderPath = getValueFromCommandBeanList(cmds, 0);
               folderPath = replaceMatchFromMasterConfig(folderPath, VARIABLE_PATTERN);
        String zipFilePath = getValueFromCommandBeanList(cmds, 1);
               zipFilePath = replaceMatchFromMasterConfig(zipFilePath, VARIABLE_PATTERN);
        
        commandExecutor.log(classObj, "Done with compressing the folder", 0);
        return new ZipFunction().zipDirectory(folderPath, zipFilePath);
    }
    
    /**
     * This method will terminate the ReadProgressive thread
     * exit will come out from the build
     */
    public synchronized void exit() {
        System.out.println(SUCCESSFUL_MESSAGE);
        commandExecutor.log(classObj, "Stared executing the exit", 0);
        readProgressive.stopReading();
        commandExecutor.terminateExecution(false);
    }
    
    private Processor() {}
    private static Processor processor = null;
    private boolean isInitCalled = false;
    private CommandExecutor commandExecutor = CommandExecutor.getCommandExecutor();
    private static Class classObj = Processor.class;
    private ReadProgressiveFile readProgressive = null;
}
