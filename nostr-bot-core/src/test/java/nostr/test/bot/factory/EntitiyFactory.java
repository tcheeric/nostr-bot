/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package nostr.test.bot.factory;

import java.beans.IntrospectionException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.security.NoSuchAlgorithmException;
import nostr.base.PublicKey;
import nostr.bot.core.Bot;
import nostr.bot.core.BotRunner;
import nostr.bot.core.IBot;
import nostr.event.impl.DirectMessageEvent;
import nostr.event.impl.GenericEvent;
import nostr.event.list.TagList;
import nostr.event.tag.PubKeyTag;
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
        return createBotRunner(bot, Identity.getInstance(), createPublicKey());
    }

    public static BotRunner createBotRunner(IBot bot, Identity identity, PublicKey pk) {
        return BotRunner.getInstance(bot, identity, pk);
    }

    public static BotRunner createBotRunner() throws IOException, NostrException {
        return createBotRunner(Identity.getInstance());
    }

    public static BotRunner createBotRunner(Identity identity) throws IOException, NostrException {
        return BotRunner.getInstance(createBot(), Identity.getInstance(), createPublicKey());
    }

    public static DirectMessageEvent createDirectMessageEvent(PublicKey senderPublicKey, PublicKey rcptPublicKey, String content) {
        try {
            TagList tagList = new TagList();
            tagList.add(PubKeyTag.builder().publicKey(rcptPublicKey).petName("uq7yfx3l").build());
            GenericEvent event = new DirectMessageEvent(senderPublicKey, tagList, content);
            event.update();
            return (DirectMessageEvent) event;
        } catch (NoSuchAlgorithmException | IntrospectionException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchFieldException | NostrException ex) {
            throw new RuntimeException(ex);
        }
    }

//    private static Identity createIdentity() throws IOException, NostrException {
//        return new Identity("/profile.properties");
//    }

    private static PublicKey createPublicKey() {
        return new PublicKey(new byte[32]);
    }

}
