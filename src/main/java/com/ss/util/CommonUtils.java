package com.ss.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.ss.bean.CommandBean;
import com.ss.bean.Constants;
import com.ss.bean.TaskBean;
import com.ss.init.Context;
import com.ss.processor.CommandExecutor;

/**
 * @author Bikash
 * Apr 18, 2018
 */
public final class CommonUtils {

    public static Properties loadProperties(String path) {
        Properties properties = new Properties();        
        InputStream inputStream = null;
        try {            
            inputStream = new FileInputStream(Constants.RESOURCE_PATH+"\\"+path);
            properties.load(inputStream);
        } catch (FileNotFoundException e) {
            System.err.println(e);
        } catch (IOException e) {
            System.err.println(e);
        } finally{
            try {
                if(inputStream != null)
                    inputStream.close();
            } catch (IOException e) {
                System.err.println(e);
            }            
        }
        return properties;
    }
    
    public static Map<String, String> loadPropertiesToMap(String path) {
        Properties properties = loadProperties(path);
        return new HashMap<String, String>((Map) properties);
    }
    
    public static List<TaskBean> loadHierarchicalProperties(String path) {
        MyProperties myProperties = new MyProperties();
        myProperties.load(path);

        return myProperties.getData();
    }
    
    public static List<String> loadFileToList(String path, Boolean isPathFullyQualified) {
        if(path == null || "".equals(path.trim())) return null;
        List<String> fileData = new ArrayList<String>();
        String line;

        try {
            path = ( isPathFullyQualified ? path : Constants.RESOURCE_PATH+"\\"+path );
            BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(path.trim())));
            while ((line = in.readLine()) != null) {
                line = line.trim();
                if(!("".equals(line) || line.startsWith("#")))
                    fileData.add(line);
            }
        } catch (FileNotFoundException e) {
            commandExecutor.log(classObj, "File not found exception occured, process will be terminated, filename-"+path, 1);
            System.err.println(e);
        } catch (IOException e) {
            commandExecutor.log(classObj, "IO Exception occured, process will be terminated, filename-"+path, 1);
            System.out.println(e);
        }
        return fileData;
    }
    
    /*
     * It is a vartual path 
     */
    public static StringBuffer loadFileToBuffer(String path, Boolean isPathFullyQualified, Boolean loadAsItIsFlag) {
        if(path == null || "".equals(path.trim())) return null;
        StringBuffer buffer = new StringBuffer();
        BufferedReader in = null;
        String line;

        try {
            path = ( isPathFullyQualified ? path : Constants.RESOURCE_PATH+"\\"+path );
            in = new BufferedReader(new InputStreamReader(new FileInputStream(path.trim())));
            while ((line = in.readLine()) != null) {
                buffer.append((loadAsItIsFlag)?line:line.trim()).append("\n");
            }
        } catch (FileNotFoundException e) {
            commandExecutor.log(classObj, "File not found exception occured, process will be terminated, filename-"+path, 1);
            System.err.println(e);
        } catch (IOException e) {
            commandExecutor.log(classObj, "IO Exception occured, process will be terminated, filename-"+path, 1);
            System.err.println(e);
        }
        return buffer;
    }
    
    
    /*
     * Write StringBuffer data to a file
     * filename must be fully qualified path or else it will write parallel to app
     */
    public static void writeStringBufferToFile(StringBuffer data, String filename) {
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(filename.trim()));
            writer.write(data.toString());
            writer.close();
        } catch (FileNotFoundException e) {
            commandExecutor.log(classObj, "File not found exception occured, process will be terminated, filename-"+filename, 1);
            System.err.println(e);
        } catch (IOException e) {
            commandExecutor.log(classObj, "IO Exception occured, process will be terminated, filename-"+filename, 1);
            System.err.println(e);
        }
    }
    
    /*
     * Write String data to a file
     * filename must be fully qualified path or else it will write parallel to app
     */
    public static void writeStringToFile(String data, String filename) {
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(filename.trim()));
            writer.write(data);
            writer.close();
        } catch (FileNotFoundException e) {
            commandExecutor.log(classObj, "File not found exception occured, process will be terminated, filename-"+filename, 1);
            System.err.println(e);
        } catch (IOException e) {
            commandExecutor.log(classObj, "IO Exception occured, process will be terminated, filename-"+filename, 1);
            System.err.println(e);
        }
    }
    
    public static Boolean isBlankOrNull(Object o) {
        if(null == o) return false;
        if("".equals(o.toString())) return false;
        return true;
    }
    
    /*
     * zip a folder
     * Path should contain fully qualified path
     */
    /*public static int zipAFolder(final String sourceDirPath, final String zipFilePath) throws IOException {
        Path p = null; 
                try{
                p = Files.createFile(Paths.get(zipFilePath));
                } catch (FileAlreadyExistsException fe) {
                    new File(zipFilePath).delete();
                    //zipAFolder(sourceDirPath, zipFilePath);
                    return 0;
                } catch (IOException e) {
                    System.err.println(e);
                }
        try (ZipOutputStream zs = new ZipOutputStream(Files.newOutputStream(p))) {
            Path pp = Paths.get(sourceDirPath);
            Files.walk(pp)
              .filter(path -> !Files.isDirectory(path))
              .forEach(path -> {
                  ZipEntry zipEntry = new ZipEntry(pp.relativize(path).toString());
                  try {
                      zs.putNextEntry(zipEntry);
                      Files.copy(path, zs);
                      zs.closeEntry();
                  } catch (IOException e) {
                      System.err.println(e);
                  }
              });
        }
        return 1;
    }*/
    
    
    public static String incrementTaggedVersion(String taggedVersion) {
    	if(taggedVersion==null || "".equals(taggedVersion)){
            commandExecutor.log(classObj, "Tagged version is blank or null, process will be terminated.", 1);
            return taggedVersion;
        }
        
        String newTaggedVersion = null;
        int lastOccuranceofDot = taggedVersion.lastIndexOf("."); 
        try {
            Integer lastTagged = Integer.parseInt(taggedVersion.substring(lastOccuranceofDot+1, taggedVersion.length()));
            newTaggedVersion = taggedVersion.substring(0, lastOccuranceofDot+1) + (lastTagged+1);
        } catch (NumberFormatException e) {
            char c = taggedVersion.charAt(taggedVersion.length()-1);
            if((c >= 'a' && c < 'z') || (c >= 'A' && c < 'Z')) {
                c++;
                newTaggedVersion = taggedVersion.substring(0, taggedVersion.length()-1) + String.valueOf(c);
            } else {
                newTaggedVersion = taggedVersion+"a";
            }
        }
        return newTaggedVersion;
    }
    
    /*
     * Replace match from master config having list of values
     */
    public static List<String> replaceMatch(List<String> listOfInput, String pattern) {
        if((listOfInput == null || listOfInput.size() == 0) && pattern == null) return null;
        if(listOfInput != null && listOfInput.size() > 0 && pattern == null) return listOfInput;

        List<String> listOfOutput = new ArrayList<String>();
        for(String input : listOfInput) {
            listOfOutput.add(replaceMatchFromMasterConfig(input, pattern));
        }
        return listOfOutput;
    }
    
    /*
     * find the match based on pattern and replace with MasterConfigData
     */
    public static String replaceMatchFromMasterConfig(String input, String pattern) {
        if(input == null && pattern == null) return null;
        if(input != null && pattern == null) return input;
        
        Pattern _pattern = Pattern.compile(pattern);
        Matcher matcher = _pattern.matcher(input);
        while(matcher.find()) {
            String matchedString = matcher.group(1);
            matchedString = matchedString.substring(2, matchedString.length()-1);
            String replaceTxt = Context.getContext().getMasterConfigData().get(matchedString);
            if(!( replaceTxt == null || "".equals(replaceTxt) )) {
                input = matcher.replaceFirst(Matcher.quoteReplacement(replaceTxt));
                matcher = _pattern.matcher(input);
            }
        }
        return input;
    }
    
    /*
     * This will replace the content from MasterConfig based on match
     */
    public static String replaceMatchNative(String input, String pattern) {
        if(input == null && pattern == null) return null;
        if(input != null && pattern == null) return input;
        
        Pattern _pattern = Pattern.compile(pattern);
        Matcher matcher = _pattern.matcher(input);
        while(matcher.find()) {
            String matchedString = matcher.group(1);
            matchedString = matchedString.substring(2, matchedString.length()-1);
            String replaceTxt = Context.getContext().getMasterConfigData().get(matchedString);
            if(replaceTxt != null) {
                input = matcher.replaceFirst(replaceTxt);
                matcher = _pattern.matcher(input);
            }
        }
        return input;
    }
    
    /*
     * To find whether match is there of not
     */
    public static Boolean isPatternMatchAvailable(String input, String pattern) {
        if(input == null || pattern == null) return false;
        
        Pattern _pattern = Pattern.compile(pattern);
        Matcher matcher = _pattern.matcher(input);
        while(matcher.find())
            return true;
        return false;
    }    
    
    /**
     * @param input
     * @param pattern
     * @return List<String>
     * Extract row facts from message 
     * Default value for flag should be 0
     */
    public static List<String> getMatchesBasedOnPattern(String input, String pattern, Integer flag) {
        if(input == null || pattern == null) return null;
        if(flag == null) flag = 0;
        
        List<String> matchedString = new ArrayList<String>();
        Pattern _pattern = Pattern.compile(pattern, flag);
        Matcher matcher = _pattern.matcher(input);
        while(matcher.find())
            matchedString.add(matcher.group(1));
        return matchedString;
    }

    /**
     * @param input
     * @param pattern
     * @return List<String>
     * Extract row facts from message
     */
    public static List<List<String>> getMultiMatchesBasedOnPattern(String input, String pattern, Integer matchCount) {
        if(input == null || pattern == null || matchCount == 0) return null;
        
        List<List<String>> matchedString = new ArrayList<List<String>>();
        Pattern _pattern = Pattern.compile(pattern);
        Matcher matcher = _pattern.matcher(input);
        while(matcher.find()) {
            List<String> temp = new ArrayList<String>();
            for(Integer i = 1; i <= matchCount; i++)
                temp.add(matcher.group(i));
            matchedString.add(temp);
        }
        return matchedString;
    }

    /**
     * @param fileName
     * @param pattern
     * @return List<String>
     * Extract row facts from message
     * FileName must contain the fully qualified path
     */
    public static List<String> getMatchesBasedOnPatternFromFile(String fileName, String pattern) {
        if(fileName == null || pattern == null) return null;

        List<String> matchedString = new ArrayList<String>();
        Pattern _pattern = Pattern.compile(pattern);
        Matcher matcher = _pattern.matcher(loadFileToBuffer(fileName, true, true));
        while(matcher.find())
            matchedString.add(matcher.group(1));
        return matchedString;
    }

    /*
     * This will count no of files or folders
     * folderPath would be fully qualified data
     */
    public static Integer folderCheck(String folderPath) {
        if(folderPath == null || "".equals(folderPath)) {
            commandExecutor.log(classObj, "Folder path is blank or null, process will be terminated.", 1);
            return -1;
        }
        Integer contentCount = 0;
        File f = new File(folderPath);
        File[] files = f.listFiles();
        if(files != null) {
            for (File file : files) {
                if(!file.isHidden()) {
                    contentCount++;
                }
            }
        }
        return contentCount;
    }

    /*
     * It will create new file in a given path
     * filename must be fully qualified
     */
    public static void createFile(String filename) {
        File f = new File(filename.trim());
        f.getParentFile().mkdirs(); 
        try {
            f.createNewFile();
        }  catch (FileNotFoundException e) {
            commandExecutor.log(classObj, "File not found exception occured, process will be terminated, filename-"+filename, 1);
            System.err.println(e);
        } catch (IOException e) {
            commandExecutor.log(classObj, "IO Exception occured, process will be terminated, filename-"+filename, 1);
            System.err.println(e);
        }
    }

    /*
     * Delete files/folder inside a given folder
     */
    public static void deleteFolder(String folderPath, String filenamePrefix) {
        if(folderPath == null || "".equals(folderPath)) return;
        
        File folder = new File(folderPath);
        File[] files = folder.listFiles();
        if(files != null) {
            for(File f: files) {
                if (f.exists() && f.isFile() && f.getName().contains(filenamePrefix))
                    f.delete();
            }
        }
    }
    
    /*
     * Check where supplied content is there in file or not
     * fileName must be fully qualified
     */
    public static Boolean checkContentInFile(String content, String fileName) {
        StringBuffer fileContent = loadFileToBuffer(fileName, true, true);
        return fileContent.toString().contains(content);
    }

    /*
     * Check and replace where supplied content is there in file
     * fileName must be fully qualified
     */
    public static void checkContentAndReplaceInFile(String inputContent, String replaceContent, String filename) {
        StringBuffer fileContent = loadFileToBuffer(filename, true, true);
        if(isPatternMatchAvailable(fileContent.toString(), inputContent)) {
            writeStringToFile(fileContent.toString().replaceAll(inputContent, replaceContent), filename);
        } else if (isPatternMatchAvailable(fileContent.toString(), inputContent.substring(1, inputContent.length()-1))) {
            writeStringToFile(fileContent.toString().replaceAll(inputContent.substring(1, inputContent.length()-1), 
                    replaceContent.substring(1, replaceContent.length()-1)), filename);
        }
    }
    
    /*
     * Return list of file in sorted order
     */
    public static StringBuffer getFileList(String folderPath) {
        if(folderPath == null || "".equals(folderPath)) {
            commandExecutor.log(classObj, "Folder path is blank or null, process will be terminated.", 1);
            return null;
        }
        
        StringBuffer fileList = new StringBuffer();
        File f = new File(folderPath.trim());
        if(f != null && f.listFiles() != null) {
            for (File file : f.listFiles()) {
                if(!file.isHidden() && file.isFile())
                    fileList.append(file.getName()).append("\n");
            }
        }
        return fileList;
    }
    
    /*
     * Get the key based on -> separated values, where index tells which Bean's value 
     */
    public static String getKeyFromCommandBeanList(List<CommandBean> commandBeanList, Integer index) {
        if(commandBeanList == null || commandBeanList.size() == 0 || index < 0) return null;
        index = (index != null) ? index : 0;
        if(commandBeanList.size() < index+1) return null;
        return commandBeanList.get(index).getCommand().split("->")[0].trim();
    }
    
    /*
     * Get the value based on -> separated values, where index tells which Bean's value 
     */
    public static String getValueFromCommandBeanList(List<CommandBean> commandBeanList, Integer index) {
        if(commandBeanList == null || commandBeanList.size() == 0 || index < 0) return null;
        index = (index != null) ? index : 0;
        if(commandBeanList.size() < index+1) return null;
        String val[] = commandBeanList.get(index).getCommand().split("->");
        if(val.length==1) return null;
        return val[1].trim();
    }
    
    public static String getKeyFromCommandBean(CommandBean commandBean) {
        if(commandBean == null) return null;
        if(commandBean.getCommand() == null) return null;
        return commandBean.getCommand().split("->")[0].trim();
    }
    
    public static String getValueFromCommandBean(CommandBean commandBean) {
        if(commandBean == null) return null;
        if(commandBean.getCommand() == null) return null;
        String val[] = commandBean.getCommand().split("->");
        if(val.length==1) return null;
        return val[1].trim();
    }
    
    /*
     * Get percentage match in sequence
     */
    public static Integer getPercentageMatchInSeq(String first, String second) {
        if(first == null || second == null) return 0;
        
        int matchedChar = 0;
        for(int i = 0; i < first.length() && i < second.length(); i++) {
            if(first.charAt(i) != second.charAt(i))
                break;
            matchedChar++;
                
        }
        return (int)((matchedChar/(float)first.length())*100);
    }
    
    /*
     * filename1, filename2 must be fully qualified path
                File1
                ======
                    123
                    abc
                    dfg
                    xyz
            
                File2
                ======
                    123
                    a79
                    abd
                    dfg
                    pqr
            
                O/P
                Difference on files- 3
                    -abc:+a79
                        :+abd
                    -xyz:+pqr
     */
    public static List<String> getDifferenceInFiles(String filename1, String filename2) {
        if(filename1 == null || "".equals(filename1) || filename2 == null || "".equals(filename2)) {
            commandExecutor.log(classObj, "Input filename is blank or null, filename1-"+filename1+" , filename2-"+filename2
                    +", process will be terminated.", 1);
            return null;
        }
        
        String[] file1Input = loadFileToBuffer(filename1, true, false).toString().split("\n");
        String[] file2Input = loadFileToBuffer(filename2, true, false).toString().split("\n");
        List<String> result = new ArrayList<String>();
        
        int pntr1 = 0, pntr2=0;
        
        do {
            if((pntr1<file1Input.length && pntr2<file2Input.length)
                    && !file1Input[pntr1].equals(file2Input[pntr2])) {
                if(pntr1+1<file1Input.length && file1Input[pntr1+1].equals(file2Input[pntr2])) {
                    result.add(file1Input[pntr1]+": "); //added space to avoid IndexOutOfBoundException
                    pntr1+=2;
                    pntr2++;
                } else if(pntr2+1<file2Input.length && file1Input[pntr1].equals(file2Input[pntr2+1])) {
                    result.add(" :"+file2Input[pntr2]);
                    pntr1++;
                    pntr2+=2;
                } else if(pntr1+1<file1Input.length && pntr2+1<file2Input.length 
                        && !file1Input[pntr1+1].equals(file2Input[pntr2+1])) {
                    result.add(file1Input[pntr1]+":"+file2Input[pntr2]);
                    pntr1++;
                    pntr2++;
                } else if(pntr1+1<file1Input.length && pntr2+1<file2Input.length 
                        && file1Input[pntr1+1].equals(file2Input[pntr2+1])) {
                    result.add(file1Input[pntr1]+":"+file2Input[pntr2]);
                    pntr1+=2;
                    pntr2+=2;
                } else {
                    result.add(file1Input[pntr1]+":"+file2Input[pntr2]);
                    pntr1++;
                    pntr2++;
                }
            } else {
                pntr1++;
                pntr2++;
            }
            
            if(pntr1 > file1Input.length-1 && pntr2 > file2Input.length-1)
                break;
        } while(true);
        
        return result;
    }
    
    /*
     * filename1, filename2 must be fully qualified path
                File1
                ======
                    123
                    abc
                    dfg
                    xyz
            
                File2
                ======
                    123
                    a79
                    abd
                    dfg
                    pqr
            
                O/P
                Difference on files- 3
                    -abc:+a79
                        :+abd
                    -xyz:+pqr
     */
    /*
     * This method will return you meta data information of difference in two files
     * filename1, filename2 must be fully qualified path
     */
    public static List<String> getMetaInfoBetweenTwoFiles(String filename1, String filename2) {
    	//TODO: This block should returns the data in such a way that same meta info can be applied to
    	//similar(same) file. So that same meta info. cab be applied and new file should be closely identical to 
    	//supplied file
        if(filename1 == null || "".equals(filename1) || filename2 == null || "".equals(filename2)) {
            commandExecutor.log(classObj, "Input filename is blank or null, filename1-"+filename1+" , filename2-"+filename2
                    +", process will be terminated.", 1);
            return null;
        }
        
        String[] file1Input = loadFileToBuffer(filename1, true, false).toString().split("\n");
        String[] file2Input = loadFileToBuffer(filename2, true, false).toString().split("\n");
        List<String> result = new ArrayList<String>();
        
        int pntr1 = 0, pntr2=0;
        
        do {
            if((pntr1<file1Input.length && pntr2<file2Input.length)
                    && !file1Input[pntr1].equals(file2Input[pntr2])) {
                if(pntr1+1<file1Input.length && file1Input[pntr1+1].equals(file2Input[pntr2])) {
                    result.add("+"+file1Input[pntr1]+": "); //added space to avoid IndexOutOfBoundException
                    pntr1 += 2;
                    pntr2 += 1;
                } else if(pntr2+1<file2Input.length && file1Input[pntr1].equals(file2Input[pntr2+1])) {
                    result.add(" :+"+file2Input[pntr2]);
                    pntr1 += 1;
                    pntr2 += 2;
                } else if(pntr1+1<file1Input.length && pntr2+1<file2Input.length 
                        && !file1Input[pntr1+1].equals(file2Input[pntr2+1])) {
                    result.add("+"+file1Input[pntr1]+":-"+file2Input[pntr2]);
                    pntr1 += 1;
                    pntr2 += 1;
                } else if(pntr1+1<file1Input.length && pntr2+1<file2Input.length 
                        && file1Input[pntr1+1].equals(file2Input[pntr2+1])) {
                    result.add("+"+file1Input[pntr1]+":-"+file2Input[pntr2]);
                    pntr1 += 2;
                    pntr2 += 2;
                } else {
                    result.add("+"+file1Input[pntr1]+":-"+file2Input[pntr2]);
                    pntr1 += 1;
                    pntr2 += 1;
                }
            } else {
                pntr1 += 1;
                pntr2 += 1;
            }

            if(pntr1 > file1Input.length-1 && pntr2 > file2Input.length-1)
                break;
        } while(true);

        return result;
    }
    
    public static String convertToEncodedString(String input) {
        if( input == null || input.trim().length() == 0 ) return input;
        
        StringBuffer output = new StringBuffer();
        for(char c : input.toCharArray()) {            
            if( (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || (c >= 0 && c <= 9) ) {
                output.append(c);
            } else {
                String encodedValue = getEncodedValue(String.valueOf(c));
                output.append(encodedValue != null?encodedValue:c);
            }
        }                
        return output.toString();
    }
    
    /*
     * Get the encoded value for a special character that can't pass as stash URL
     */
    private static String getEncodedValue(String k) {
        if(k != null && k.length() != 1) return null;
        switch(k) {
            case "!" : return "%21";
            case "#" : return "%23";
            case "$" : return "%24";
            case "&" : return "%26";
            case "@" : return "%40";
            case "`" : return "%60";
            case "~" : return "%7E";
            default  : return null;
        }
    }

    private static Class classObj = CommonUtils.class;
    private static CommandExecutor commandExecutor = CommandExecutor.getCommandExecutor();
    
    public static void main(String[] args) {
        
        System.out.println("To get the difference in files");
        List<String> result = getMetaInfoBetweenTwoFiles("C:\\dev\\proj_workspace\\regular_ws\\automation-engine\\test\\new_dlls.txt", "C:\\dev\\proj_workspace\\regular_ws\\automation-engine\\test\\old_dlls.txt");
        for(String v : result) {
            System.out.println(v);
        }
    }

}
