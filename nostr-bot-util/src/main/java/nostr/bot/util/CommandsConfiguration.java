/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package nostr.bot.util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.stream.Collectors;
import lombok.extern.java.Log;
import nostr.id.Identity;
import nostr.util.NostrException;

/**
 *
 * @author eric
 */
@Log
public class CommandsConfiguration extends BotBaseConfiguration {

    public CommandsConfiguration(String prefix) throws IOException {
        this("/commands.properties", prefix);
    }

    public CommandsConfiguration(String file, String prefix) throws IOException {
        super(file, prefix);
    }

    public String getAllCommands() throws IOException {

        return properties.keySet()
                .stream()
                .filter(k -> k.toString().startsWith(getPrefix()))
                .map(k -> properties.get(k).toString())
                .collect(Collectors.joining(","));
    }
    
    public Identity getAdmin(String commandId) throws FileNotFoundException, IOException, NostrException {
        String idFile = getFileLocation("file.admin." + commandId);
        return new Identity(idFile);
    }

}
