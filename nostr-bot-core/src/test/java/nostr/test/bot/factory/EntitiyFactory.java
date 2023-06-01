/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package nostr.test.bot.factory;

import java.io.IOException;
import nostr.base.PublicKey;
import nostr.bot.core.Bot;
import nostr.bot.core.BotRunner;
import nostr.event.impl.DirectMessageEvent;
import nostr.event.impl.GenericEvent;
import nostr.event.list.TagList;
import nostr.event.tag.PubKeyTag;
import nostr.util.NostrException;

/**
 *
 * @author eric
 */
public class EntitiyFactory {

    public static Bot createBot() {
        return new Bot();
    }

    public static BotRunner createBotRunner() throws IOException, NostrException {
        return createBotRunner(createPublicKey());
    }

    public static BotRunner createBotRunner(PublicKey pk) {
        return BotRunner.getInstance(pk);
    }

    public static DirectMessageEvent createDirectMessageEvent(PublicKey senderPublicKey, PublicKey rcptPublicKey, String content) {
        TagList tagList = new TagList();
        tagList.add(PubKeyTag.builder().publicKey(rcptPublicKey).petName("uq7yfx3l").build());
        GenericEvent event = new DirectMessageEvent(senderPublicKey, tagList, content);
        event.update();
        return (DirectMessageEvent) event;
    }

    private static PublicKey createPublicKey() {
        return new PublicKey(new byte[32]);
    }

}
