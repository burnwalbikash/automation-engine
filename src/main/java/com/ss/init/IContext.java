/**
 * 
 */
package com.ss.init;

/**
 * @author bikash
 *
 */
public interface IContext {
    
    /*
     * Load the master-config.properties
     */
    void initializeMasterConfig();    
    
    void initializeHotfixCommands();
    
    void initializeCfg();
}
