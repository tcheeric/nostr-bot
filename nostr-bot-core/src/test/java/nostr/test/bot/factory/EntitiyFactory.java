/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package nostr.test.bot.factory;

import java.io.IOException;
import nostr.base.PublicKey;
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
        return new Bot();
    }

    public static BotRunner createBotRunner(IBot bot) throws IOException, NostrException {
        return createBotRunner(bot, createIdentity(), createPublicKey());
    }

    public static BotRunner createBotRunner(IBot bot, Identity identity, PublicKey pk) {
        return new BotRunner(bot, identity, pk);
    }

    public static BotRunner createBotRunner() throws IOException, NostrException {
        return createBotRunner(createIdentity());
    }

    public static BotRunner createBotRunner(Identity identity) throws IOException, NostrException {
        return new BotRunner(createBot(), createIdentity(), createPublicKey());
    }

    private static Identity createIdentity() throws IOException, NostrException {
        return new Identity("/profile.properties");
    }

    private static PublicKey createPublicKey() {
        return new PublicKey(new byte[32]);
    }

}
