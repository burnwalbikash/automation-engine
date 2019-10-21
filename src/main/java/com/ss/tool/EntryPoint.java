package com.ss.tool;

import java.io.Console;
import java.util.Scanner;

import com.ss.bean.Constants;
import com.ss.processor.Processor;

/**
 * @author Bikash
 * Apr 18, 2018
 */
public class EntryPoint {

    /**
     * Main method to start the tool 'Automation Tool'
     * @param args
     */
    private static int retryCount = 0;
    
    public static void main(String[] args) {
        System.out.println("*************************************************************************************");
        System.out.println("\t\t Start of Automation Tool");
        System.out.println("*************************************************************************************");
        System.out.println();
        System.out.println("*************************************************************************************");
        System.out.println("Would you like to enter the resources path(Y - Yes or N - No), default path of your \n"
                + "resources must be parallel to executable jar");
        System.out.println("*************************************************************************************");
        
        Scanner sc = new Scanner(System.in);
        String decision = sc.next().toUpperCase();
        if("Y".equals(decision) || "YES".equals(decision)) {
            System.out.println("*************************************************************************************");
            System.out.println("  Please enter the path of resources folder where contains configuration files  ");
            System.out.println("\t\t Like:  c:\\ProgramData\\resources \t\t\t\t");
            System.out.println("*************************************************************************************");
            Constants.RESOURCE_PATH = sc.next().replace("/", "\\");

        } else if("N".equals(decision) || "NO".equals(decision)) {
            Constants.RESOURCE_PATH = System.getProperty("user.dir").replace("/", "\\") + "\\resources"; //This works for executable jar as well
            
        } else {
            retryCount++;
            if(retryCount < 3) {
                System.out.println("*************************************************************************************");
                System.out.println(" Please choose the correct option ");
                main(null);
            }
            else 
                return;
            System.out.println("*************************************************************************************");
            System.out.println(" Please try again ");
            System.out.println("*************************************************************************************");
            System.exit(0);

        }

        System.out.println("*************************************************************************************");
        System.out.println("  Please enter the stash password  ");
        System.out.println("\t\t Like:  xyz@123 \t\t\t\t");
        System.out.println("*************************************************************************************");
        Console console = System.console();
        //read the password, without echoing the output. This only works on command prompt
        Constants.STASH_PASSWORD = ( console == null ? sc.next() : String.valueOf(console.readPassword()) ).trim();

        Processor.getProcessorObj().execute();
        //TODO: Generate the report in HTML or Excel or text format based on command output file
        //TODO: Add column to analyze the system output to decide whether command has passed/failed

        System.exit(0);
    }
    
    public static void terminationMessage() {
        System.out.println("*************************************************************************************");
        System.out.println("\t\t End of Automation Tool");
        System.out.println("*************************************************************************************");
    }
}