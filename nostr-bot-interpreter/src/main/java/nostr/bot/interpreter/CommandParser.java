/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package nostr.bot.interpreter;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import nostr.bot.core.BotRunner;
import nostr.bot.core.Context;
import nostr.bot.core.IBot;
import nostr.bot.core.command.ICommand;

/**
 *
 * @author eric
 */
@Data
@Builder
public class CommandParser {

    @NonNull
    private final String command;
    private final BotRunner botRunner;
    
    public static final String COMMAND_PREFIX = "!";
    
    public ICommand parse() {
        ICommand cmd;

        String[] arr = command.split(" ");
        String strCmd = arr[0];

        if (strCmd.startsWith(COMMAND_PREFIX)) {
            
            final String cmdId = strCmd.substring(1);
      
            IBot bot = botRunner.getBot();
            if (bot.getCommand(cmdId).isPresent()) {
                cmd = bot.getCommand(cmdId).get();
                Context context = botRunner.getContext();
                cmd.setParameters(arr, context);
                return cmd;
            }
        }

        throw new RuntimeException("Parsing error at index 0");
    }
}
