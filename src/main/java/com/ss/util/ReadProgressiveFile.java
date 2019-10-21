package com.ss.util;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;

import com.ss.bean.Constants;
import com.ss.processor.CommandExecutor;
import com.ss.tool.EntryPoint;

/**
 * @author Bikash
 * Apr 18, 2018
 */
public class ReadProgressiveFile  implements Runnable {

    public ReadProgressiveFile(String filename) {
        this.filename = filename;
    }
    
    @Override
    public void run() {
        try {
            startReading();
        } catch (InterruptedException e) {
            System.out.println("File intrupted ["+e.getMessage()+"]");
        } catch (IOException e) {
            System.out.println("IOException ["+e.getMessage()+"]");
        }
    }
    
    private void startReading() throws InterruptedException, IOException {
        isBreak = false;
        String line;
        try {
            LineNumberReader lnr = new LineNumberReader(new FileReader(this.filename));
            while (!isBreak) {
                line = lnr.readLine();
                if (line == null) {
                    if(count++ > 4 && lastLine.contains("Done with")){
                        canExecuteJavaProgram = true;
                    }
                    Thread.sleep(2000); //wait for 2 seconds
                    continue;
                } else {
                    count = 0;
                    canExecuteJavaProgram = false;
                }
                processLine(line);
            }
            lnr.close();
        } catch (FileNotFoundException e) {
            System.out.println("FileNotFoundException ["+e.getMessage()+"], filename ["+filename+"]");
        }
    }

    public void stopReading() {
        System.out.println("Stop reading invoked");
        isBreak = true;
    }

    private void processLine(String line) {
        //processing line
        lastLine = line.trim();
        if(CommonUtils.isPatternMatchAvailable(line, Constants.ERRORLEVEL_PATTERN) && !"ERRORLEVEL=0".equals(line.trim())) {
            CommandExecutor.getCommandExecutor().terminateExecution(true);
            System.out.println("Something went wrong, TS- "+System.currentTimeMillis());
            stopReading();
            System.out.println(Constants.ERROR_MESSAGE);
            EntryPoint.terminationMessage();
        }
    }

    /**
     * @return the filename
     */
    public String getFilename() {
        return filename;
    }

    private int count = 0;
    private boolean isBreak = false;
    public boolean canExecuteJavaProgram = false;
    private String filename;
    private String lastLine = null;
}
