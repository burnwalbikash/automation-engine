package com.ss.processor;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.ss.bean.CommandBean;
import com.ss.bean.Constants;
import com.ss.init.Context;
import com.ss.util.CommonUtils;

/**
 * @author Bikash
 * Apr 18, 2018
 */
public class CommandExecutor implements IExecutor {

    private CommandExecutor() {}

    public static CommandExecutor getCommandExecutor() {
        synchronized (CommandExecutor.class) {
            if(cmdExecutor == null)
                cmdExecutor =  new CommandExecutor();
        }
        return cmdExecutor;
    }

    @Override
    public void start() {
        if(isStarted) return;

        isStarted = true;
        FULL_LOG_FILENAME = Context.getContext().getVariablesData(Constants.FULL_LOG_FILENAME_VAR);
        File errStreamOutput = null;
        try {
            //file errStreamOutputxxxx will created under C:\Users\bikash\AppData\Local\Temp folder
            //It is advisible to not delete this file during process
            errStreamOutput = File.createTempFile("errStreamOutput", null);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        builder.redirectErrorStream(true).redirectOutput(errStreamOutput);
        try {
            process = builder.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //Add the command whose ERRORLEVEL should not consider to terminate the process
        ignoreErroeLevelCmdList.add("nmake -f makefile.nt clean superclean");
    }

    @Override
    public synchronized void execute(List<CommandBean> cmdList) {
        start();
        BufferedWriter p_stdin = new BufferedWriter(new OutputStreamWriter(process.getOutputStream()));
        
        for (CommandBean cmd : cmdList) {
            cmd.setCommand(cmd.getCommand().trim());
            try {
                if(abortFurtherExecution)
                    cmd.setCommand("exit"); //update the command to exit so that it get terminated
                
                if(cmd.getCommand().equals("exit"))
                    isExit = true;

                parseCmd(cmd);
                p_stdin.write(cmd.getCommand());
                p_stdin.newLine();
                p_stdin.flush();
                cmd.setIsExecuted(true);
                //cmd.setErrorLevel(process.exitValue());

                sleep();                

                //process.waitFor();
            } catch (IOException e) {
                System.out.println(e);
            } catch (Exception e) {
                System.out.println(e);
            }
        }
        abortFurtherExecution = false;
        terminate();
    }

    @Override
    public void terminate() {
        if(isExit) {
            // write stdout of cmd (=output of all commands including everything)
            StringBuffer cmdOutputBuffer = new StringBuffer();
            Scanner scanner = new Scanner(process.getInputStream());
            while (scanner.hasNextLine()) {
                cmdOutputBuffer.append(scanner.nextLine()).append("\n");
            }
            scanner.close();

            String logfileName = Constants.CMD_OUTPUT_FILENAME_ONLY+System.nanoTime()+Constants.LOG_FILE_EXTENSION;
            CommonUtils.writeStringBufferToFile(cmdOutputBuffer, 
                    Context.getContext().getVariablesData(Constants.RESOURCE_PATH_VAR)+"/temp/"+ logfileName);
            Context.getContext().addVariablesData(Constants.CMD_OUTPUT_FILENAME_ONLY, logfileName);

            isExit = false;
        }
    }

    private void parseCmd(CommandBean cmd) {
        //Modified the variable to given exact value and execute the command
        String cmdString = CommonUtils.replaceMatchFromMasterConfig(cmd.getCommand(), Constants.VARIABLE_PATTERN);
        String cmdPrefix = "";
        String cmdSuffix = "";

        if(!(cmd.getCommand().startsWith("echo") || cmd.getCommand().contains(">") || cmd.getCommand().startsWith("exit"))) {
            sleepFlag = true;
            cmdPrefix = "echo Start of " + cmdString + " TS-"+ System.currentTimeMillis()+" >> " + FULL_LOG_FILENAME + "\n";
            if(!needToIgnoreErrorLevel(cmdString))
                cmdSuffix += "\n" + "echo ERRORLEVEL=%ERRORLEVEL% >> " + FULL_LOG_FILENAME;

            cmdSuffix += "\n" + "echo End of " + cmdString + " TS-" + System.currentTimeMillis() + " >> " + FULL_LOG_FILENAME;
            cmdString += " >> " + FULL_LOG_FILENAME;
        } else {
            sleepFlag = false;
        }
        cmd.setCommand(cmdPrefix+cmdString+cmdSuffix);
    }

    private boolean needToIgnoreErrorLevel(String cmdString) {
        for(String s : ignoreErroeLevelCmdList) {
            if(s.equalsIgnoreCase(cmdString))
                return true;
        }
        return false;
    }

    public void terminateExecution(Boolean isAbort) {
        if(isAbort)
            abortFurtherExecution = true;
        System.out.println("terminateExecution " + isAbort);
        //Commented below line to avoid pipe has been closed error
        /*List<CommandBean> cmdList = new ArrayList<CommandBean>();
        CommandBean cmd = new CommandBean("exit", -1);
        cmdList.add(cmd);
        execute(cmdList);*/
        System.exit(0);
    }
    
    private void sleep() {
        //Timeout has been enable to terminate the execution if anything goes wrong
        if(sleepFlag) {
            try {
                Thread.sleep(3000); //wait for 3 sec
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    
    /*
     * This method will log any messages to cmd_full_xxx.log file inside resources\temp folder
     */
    public void log(Class classobj, String message, Integer errorLevel) {
        start();
        List<CommandBean> cmdList = new ArrayList<CommandBean>();
        CommandBean cmd = new CommandBean("echo " + classobj.getName() + ":" + message + " >> " + FULL_LOG_FILENAME, 0);
        cmdList.add(cmd);
        execute(cmdList);
        if(errorLevel != 0) {
            System.out.println("Error- "+errorLevel+" , classobj- "+classobj.getName()+", Message- "+message);
            terminateExecution(true);
        }
    }
    
    public static String FULL_LOG_FILENAME = null;
    private static CommandExecutor cmdExecutor = null;
    private static ProcessBuilder builder = new ProcessBuilder( "cmd" ); // bin/bash can be used for linux env. 
    private Process process = null;
    private static Boolean isStarted = false;
    private static Boolean isExit = false;
    private static Boolean abortFurtherExecution = false;
    private static Boolean sleepFlag = false;
    private List<String> ignoreErroeLevelCmdList = new ArrayList<String>();
}
