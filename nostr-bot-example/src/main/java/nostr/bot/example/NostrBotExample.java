/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package nostr.bot.example;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;

import java.util.logging.LogManager;

import lombok.extern.java.Log;
import nostr.base.PublicKey;

import nostr.bot.core.Bot;
import nostr.bot.core.command.CommandParser;

import nostr.bot.core.command.ICommand;

import nostr.util.NostrException;

/**
 *
 * @author eric
 */
@Log
public class NostrBotExample {

    static {
        try {
            InputStream inputStream = NostrBotExample.class.getResourceAsStream("/logging.properties");
            LogManager.getLogManager().readConfiguration(inputStream);
        } catch (IOException ex) {
            System.out.println("WARNING: Could not open configuration file");
            System.out.println("WARNING: Logging not configured (console output only)");
        }
    }

    public static void main(String[] args) throws IOException, NostrException, ParseException {

        final var bot = getBot();
        
        final String strCmd = "!hello world";
        CommandParser.builder().command(strCmd).bot(bot).build().parse();
        ICommand helloCmd = bot.getContext().getCommand();
        
        bot.execute(helloCmd);
    }
    
    private static Bot getBot() throws IOException, NostrException {
        return Bot.getInstance(new PublicKey(new byte[32]));        
    }
}
