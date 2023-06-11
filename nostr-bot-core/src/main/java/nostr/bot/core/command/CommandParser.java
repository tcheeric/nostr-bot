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
import nostr.bot.core.Bot;
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
    private final Bot bot;

    public static final String COMMAND_PREFIX = "!";

    public void parse() throws ParseException, NostrException {

        log.log(Level.INFO, ">>> Parsing content: {0}", command);

        String[] arr = command.split(" ");
        String strCmd = arr[0];

        if (strCmd.startsWith(COMMAND_PREFIX)) {

            final String cmdId = strCmd.substring(1);

            var cmd = bot.getContext().getCommand();
            log.log(Level.INFO, ">>>>>>>>>! Command class: {0}", cmd);
            if (cmd.getId().equals(cmdId)) {
                cmd.setParameterValues(arr, bot.getContext());
                return;
            }
        }

        throw new ParseException(command, 0);
    }
}
