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
import nostr.bot.core.BotRunner;
import nostr.bot.core.command.CommandParser;

import nostr.id.Identity;

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

        final var botRunner = getBotRunner();
        
        final String strCmd = "!hello world";
        final var helloCmd = CommandParser.builder().command(strCmd).botRunner(botRunner).build().parse();

        botRunner.execute(helloCmd);
    }
    
    private static BotRunner getBotRunner() throws IOException, NostrException {
        return BotRunner.getInstance(new PublicKey(new byte[32]));        
    }
}
