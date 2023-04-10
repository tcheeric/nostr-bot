/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package nostr.bot.factory;

import nostr.base.PrivateKey;
import nostr.base.PublicKey;
import nostr.bot.core.Bot;
import nostr.bot.core.BotRunner;
import nostr.bot.core.IBot;
import nostr.id.Identity;

/**
 *
 * @author eric
 */
public class EntitiyFactory {

    public final static Identity IDENTITY = new Identity(new PrivateKey("04a7dd63ef4dfd4ab95ff8c1576b1d252831a0c53f13657d959a199b4de4b670"), new PublicKey("99cf4426cb4507688ff151a760ec098ff78af3cfcdcb6e74fa9c9ed76cba43fa"));

    public static Bot createBot() {
        return new Bot("/test-commands.properties");
    }

    public static BotRunner createBotRunner(IBot bot) {
        return createBotRunner(bot, IDENTITY);
    }

    public static BotRunner createBotRunner(IBot bot, Identity identity) {
        return new BotRunner(bot, identity);
    }

    public static BotRunner createBotRunner() {
        return createBotRunner(IDENTITY);
    }

    public static BotRunner createBotRunner(Identity identity) {
        return new BotRunner(createBot(), IDENTITY);
    }
}
