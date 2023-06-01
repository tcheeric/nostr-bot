/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package nostr.bot.job.impl;

import java.util.logging.Level;
import lombok.extern.java.Log;
import nostr.bot.util.BotUtil;
import nostr.event.impl.GenericEvent;
import nostr.id.Identity;
import nostr.util.NostrException;

/**
 *
 * @author eric
 */
@Log
public class DMSubscriber extends AbstractSubscriber {

    public DMSubscriber(String subscriptionId, String jsonEvent) {
        super(subscriptionId, jsonEvent);
    }

    @Override
    protected String getContent() {
        try {
            final GenericEvent event = BotUtil.unmarshallEvent(getJsonEvent());
            return Identity.getInstance().decryptDirectMessage(event.getContent(), event.getPubKey());
        } catch (NostrException ex) {
            log.log(Level.SEVERE, null, ex);
            throw new RuntimeException(ex);
        }
    }

}
