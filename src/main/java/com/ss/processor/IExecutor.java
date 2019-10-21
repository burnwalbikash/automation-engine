package com.ss.processor;

import java.util.List;

import com.ss.bean.CommandBean;

/**
 * @author Bikash
 * Apr 18, 2018
 */
public interface IExecutor {
    void start();
    void execute(List<CommandBean> cmdList);
    void terminate();
}
