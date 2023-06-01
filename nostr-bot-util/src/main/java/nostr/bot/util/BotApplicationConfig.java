/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package nostr.bot.util;

import java.io.IOException;
import nostr.util.ApplicationConfiguration;

/**
 *
 * @author eric
 */
public class BotApplicationConfig extends ApplicationConfiguration {

    private final String DEFAULT_COMMANDS_CONFIG = "/commands.properties";
    private final String DEFAULT_JOB_CONFIG = "/nostr-job.properties";
    private final String DEFAULT_SECURITY_CONFIG = "/security.properties";

    public BotApplicationConfig() throws IOException {
        super();
    }

    public String getCommandsProperties() {
        var property = getProperty("commands");
        return property == null ? DEFAULT_COMMANDS_CONFIG : property;
    }

    public String getJobProperties() {
        var property = getProperty("job");
        return property == null ? DEFAULT_JOB_CONFIG : property;
    }

    public String getSecurityProperties() {
        var property = getProperty("security");
        return property == null ? DEFAULT_SECURITY_CONFIG : property;
    }
}
