/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package nostr.bot.util;

import java.io.IOException;
import java.util.stream.Collectors;
import lombok.extern.java.Log;

/**
 *
 * @author eric
 */
@Log
public class CommandsConfiguration extends BotBaseConfiguration {

    
    public CommandsConfiguration(String prefix) throws IOException {
        super(prefix);
        var configFile = ((BotApplicationConfig) getAppConfig()).getCommandsProperties();
        configFile = configFile.startsWith("/") ? configFile : "/" + configFile;
        load(configFile);
    }

    public String getAllCommands() throws IOException {

        return properties.keySet()
                .stream()
                .filter(k -> k.toString().startsWith(getPrefix()))
                .map(k -> properties.get(k).toString())
                .collect(Collectors.joining(","));
    }

}
