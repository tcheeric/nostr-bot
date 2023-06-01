/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package nostr.bot.util;

import java.io.IOException;

import lombok.extern.java.Log;

/**
 *
 * @author eric
 */
@Log
public class SecurityConfiguration extends BotBaseConfiguration {

    public SecurityConfiguration(String prefix) throws IOException {
        super(prefix);
        var configFile = ((BotApplicationConfig) getAppConfig()).getSecurityProperties();
        configFile = configFile.startsWith("/") ? configFile : "/" + configFile;
        load(configFile);
    }

    public String[] getGroups() {
        return this.getProperty("groups").split(",");
    }

    public String[] getGroupUsers(String group) {
        return this.getProperty(group).split(",");
    }
}
