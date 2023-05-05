/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package nostr.bot.job.impl;

import java.util.logging.Level;
import lombok.NoArgsConstructor;
import lombok.extern.java.Log;
import nostr.base.Command;
import nostr.base.Relay;
import nostr.ws.handler.command.spi.ICommandHandler;

/**
 *
 * @author eric
 */
@Log
@NoArgsConstructor
public class BotCommandHandler implements ICommandHandler {

    @Override
    public void onEose(String subscriptionId, Relay relay) {
        log.log(Level.INFO, "Command: {0} - Subscription ID: {1} - Relay {3}", new Object[]{Command.EOSE, subscriptionId, relay});
    }

    @Override
    public void onOk(String eventId, String reasonMessage, Reason reason, boolean result, Relay relay) {
        log.log(Level.INFO, "Command: {0} - Event ID: {1} - Reason: {2} ({3}) - Result: {4} - Relay {5}", new Object[]{Command.OK, eventId, reason, reasonMessage, result, relay});
    }

    @Override
    public void onNotice(String message) {
        log.log(Level.WARNING, "Command: {0} - Message: {1}", new Object[]{Command.NOTICE, message});
    }

    @Override
    public void onEvent(String jsonEvent, String subId, Relay relay) {
        log.log(Level.INFO, ">>>> Command: {0} - Event: {1} - Subscription ID: {2} - Relay {3}", new Object[]{Command.EVENT, jsonEvent, subId, relay});
    }

    @Override
    public void onAuth(String challenge, Relay relay) {
        log.log(Level.INFO, "Command: {0} - Challenge: {1} - Relay {3}", new Object[]{Command.AUTH, challenge, relay});
    }
    
}
