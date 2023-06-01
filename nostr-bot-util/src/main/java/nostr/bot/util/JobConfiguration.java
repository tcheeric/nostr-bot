/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package nostr.bot.util;

import java.io.IOException;

/**
 *
 * @author eric
 */
public class JobConfiguration extends BotBaseConfiguration {

    public JobConfiguration(String prefix) throws IOException {
        super(prefix);
        var configFile = ((BotApplicationConfig) getAppConfig()).getJobProperties();
        configFile = configFile.startsWith("/") ? configFile : "/" + configFile;
        load(configFile);
    }


    public String getDataFile() {
        return getProperty("file");
    }
    
    public int getInterval() {
        return Integer.parseInt(getProperty("interval"));
    }
    
    public String getMode() {
        return getProperty("mode");
    }
}
