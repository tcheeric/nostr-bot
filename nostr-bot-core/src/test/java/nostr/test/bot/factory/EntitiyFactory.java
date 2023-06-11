/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package nostr.test.bot.factory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import nostr.base.PublicKey;
import nostr.bot.core.Bot;
import nostr.event.BaseTag;
import nostr.event.impl.DirectMessageEvent;
import nostr.event.impl.GenericEvent;
import nostr.event.tag.PubKeyTag;
import nostr.util.NostrException;

/**
 *
 * @author eric
 */
public class EntitiyFactory {

    public static Bot createBot(PublicKey recipient) {
        return Bot.getInstance(recipient);
    }

    public static Bot createBot() throws IOException, NostrException {
        return createBot(createPublicKey());
    }

    public static DirectMessageEvent createDirectMessageEvent(PublicKey senderPublicKey, PublicKey rcptPublicKey, String content) {
        List<BaseTag> tagList = new ArrayList<>();
        tagList.add(PubKeyTag.builder().publicKey(rcptPublicKey).petName("uq7yfx3l").build());
        GenericEvent event = new DirectMessageEvent(senderPublicKey, tagList, content);
        event.update();
        return (DirectMessageEvent) event;
    }

    private static PublicKey createPublicKey() {
        return new PublicKey(new byte[32]);
    }

}
