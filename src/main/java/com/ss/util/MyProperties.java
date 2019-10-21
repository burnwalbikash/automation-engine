/**
 * Load the hierarchical property data that duplicate keys
 */
package com.ss.util;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import com.ss.bean.CommandBean;
import com.ss.bean.Constants;
import com.ss.bean.TaskBean;

/**
 * @author Bikash
 * Apr 18, 2018
 */
public class MyProperties {
    
    /*
    * This method will load the command file(*.hf)
    */
    public synchronized void load(String path) {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(Constants.RESOURCE_PATH+"\\"+path)));
            Integer lineNumber = 0;
            String line;
            List<CommandBean> subData = null;
            while ((line = in.readLine()) != null) {
                lineNumber++;
                line = line.trim();
                if(!("".equals(line) || line.startsWith("#"))) {
                    if(line.startsWith("[")&&line.endsWith("]")) {
                        subData = new ArrayList<CommandBean>();
                        TaskBean taskBean = new TaskBean(line.substring(1, line.length()-1),lineNumber);
                        taskBean.setListCommandBean(subData);
                        data.add(taskBean);
                    } else {
                        subData.add(new CommandBean(line,lineNumber));
                    }
                }
            }
        } catch(IOException ioe) {
            ioe.printStackTrace();
        }    
    }
    
    /**
     * @return the data
     */
    public List<TaskBean> getData() {
        return data;
    }

    /**
     * @param data the data to set
     */
    public void setData(List<TaskBean> data) {
        this.data = data;
    }

    private List<TaskBean> data = new ArrayList<TaskBean>();        
}
