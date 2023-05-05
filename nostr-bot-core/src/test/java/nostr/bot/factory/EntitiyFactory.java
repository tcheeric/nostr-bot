/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package nostr.bot.factory;

import java.io.IOException;
import nostr.bot.core.Bot;
import nostr.bot.core.BotRunner;
import nostr.bot.core.IBot;
import nostr.id.Identity;
import nostr.util.NostrException;

/**
 *
 * @author eric
 */
public class EntitiyFactory {
   
    public static Bot createBot() {
        return new Bot("/test-commands.properties");
    }

    public static BotRunner createBotRunner(IBot bot) throws IOException, NostrException {
        return createBotRunner(bot, createIdentity());
    }

    public static BotRunner createBotRunner(IBot bot, Identity identity) {
        return new BotRunner(bot, identity);
    }

    public static BotRunner createBotRunner() throws IOException, NostrException {
        return createBotRunner(createIdentity());
    }

    public static BotRunner createBotRunner(Identity identity) throws IOException, NostrException {
        return new BotRunner(createBot(), createIdentity());
    }

    private static Identity createIdentity() throws IOException, NostrException {
        return new Identity("/profile.properties");
    }

}
