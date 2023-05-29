/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package nostr.bot.core.command;

import java.text.ParseException;
import java.util.logging.Level;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import lombok.extern.java.Log;
import nostr.bot.core.BotRunner;
import nostr.bot.core.Context;
import nostr.bot.core.IBot;
import nostr.util.NostrException;

/**
 *
 * @author eric
 */
@Data
@Builder
@Log
public class CommandParser {

    @NonNull
    private final String command;
    private final BotRunner botRunner;
    
    public static final String COMMAND_PREFIX = "!";
    
    public ICommand parse() throws ParseException, NostrException {
        
        log.log(Level.INFO, ">>> Parsing content: {0}", command);
        
        ICommand cmd;

        String[] arr = command.split(" ");
        String strCmd = arr[0];

        if (strCmd.startsWith(COMMAND_PREFIX)) {
            
            final String cmdId = strCmd.substring(1);
      
            IBot bot = botRunner.getBot();
            if (bot.getCommand(cmdId).isPresent()) {
                cmd = bot.getCommand(cmdId).get();
                Context context = botRunner.getContext();
                cmd.setParameterValues(arr, context);
                return cmd;
            }
            
            throw new NostrException(String.format("Command %s plug-in not loaded in classpath", cmdId));
        }

        throw new ParseException("Parsing error at index 0", 0);
    }
}
