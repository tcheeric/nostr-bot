/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package nostr.bot.job.impl;

import java.util.logging.Level;
import lombok.extern.java.Log;
import nostr.bot.util.BotUtil;
import nostr.event.impl.GenericEvent;
import nostr.event.unmarshaller.impl.EventUnmarshaller;
import nostr.util.NostrException;

/**
 *
 * @author eric
 */
@Log
public class DMSubscriber extends AbstractSubscriber {

    public DMSubscriber() {
        this(null, null);
    }

    public DMSubscriber(String subscriptionId, String jsonEvent) {
        super(subscriptionId, jsonEvent);
    }

    @Override
    protected String getContent() {
        try {
            final GenericEvent event = new EventUnmarshaller(getJsonEvent()).unmarshall();
            return BotUtil.IDENTITY.decryptDirectMessage(event.getContent(), event.getPubKey());
        } catch (NostrException ex) {
            log.log(Level.SEVERE, null, ex);
            return "";
        }
    }

}
