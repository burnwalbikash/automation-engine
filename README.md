# automation-engine
This tool will server you a automated way to repeated day to day works. Suppose you have to execute set of system commands and then modify some files and later on run few more commands.
This tool can be used in both Windows and Linux.

# Project Consist below 3 packages
##### Entry point
  package com.ss.tool

##### For Main logic

package com.ss.init
package com.ss.processor

##### For Unit Test cases

Yet to write

# 1. To build and execute unit-test-cases
Project code link -> https://github.com/burnwalbikash/automation-engine

     1. Clone the project from above link.
     2. Navigate to project pom.xml location.
     3. mvn clean install

# 2. How to use the tool
     Once build is done, you have to configure your work.
     1. Go to resource/master-config.properties and modify/add the values based on need. Those are the row data which you can use in command file mentioned as property 'hotfix-cmd-filename'. Variables used in master-config.properties are self explanatory.
     2. Run the below command to execute the tool
            java -jar target\automation-tool-1.1-jar-with-dependencies.jar
        Prompt will ask for resource path location-
        "Would you like to enter the resources path(Y - Yes or N - No), default path of your resources must be parallel to executable jar"
        Enter 'N' since resource is paraller to the executable jar. Request 'Y' followed by resouce folder path if it placed somewhere else.
        Prompt will ask again-
        "Please enter the stash password"
        Enter the password against username supplied in master-config.properties file. If you do not want to intract with stash/github, you can enter dummy values.
        Script will execute the list of command supplied in commands.hf one by one.
        A report will be printed on console once executed successfully.
# 3. How to write/modify command file(commands.hf)
Format of command file:
[taskname-<unique#>]
statement-1
statement-2
.....
.....
.....
statement-N

There are 11 task supported as part of this tool.
1. build : This task will execute the system level commands
2. initialize-product-path : Can be used to initialize the product path/variables during runtime. It will read a file called prod-path.lst under $resouces/temp folder and load to context.
3. increment-tagged-version : It will increment the tagged version and update the latest tag to files conf\TeggedVersion and webclient\web\config
It increment the last minor tag and doesn't add further minor tag.
Example: It will increment from 4.2.1.39 to 4.2.1.40 but doesn't do like 4.2.1.39.1
4. folder-check : You can check whether there is any file or no of files are there inside a given folder.
5. get-directory-list : List down all the file's name to a file from a given directory.
6. check-diff-on-files : This will give you a difference between two files. Does same as 'git diff'.
7. perform-check-operation : It will check, count of file committed must be equals to no of file changed + 2.
8. check-if-contains : To check for a given content in a given file.
9. fetch-hotfix-number : This function will fetch the patch # from a given file. This will read the output of hfstart.pl command.
10. replace-if-contains : This will replace a text/file-content to a given file based on supplied search text.
11. zip-funcion : Will zip a given file/folder
Check commands.hf for more details of uses of above mentioned task.

# 4. How to diagnostic if something goes wrong
Tool trace were logged in to a file called $resouces/temp/hotfix_full_output_<#>.log
Window too generates log if any error occurs under C:\Users\bikash\AppData\Local\Temp\hotfixErrStreamOutputxxxx.tmp
Open the file hotfix_full_output_<#>.log and check for a regular expression "ERRORLEVEL=[1-9]". For successful execution ERRORLEVEL will return as 0 and non-zero for any error.

# 5. Output file generates under(log files)
     folder :  resources/temp
NOTE:
Use hash(#) to comment any line in master-config.properties or .hf file.

# Contributing
Pull requests are welcome. For major changes, please do proper comment and discuss what you would like to change.

Please make sure to update tests as appropriate.

# Author
- Linkedin: https://www.linkedin.com/in/bikash-burnwal-486b3919/
- Email: burnwalbikash@gmail.com
