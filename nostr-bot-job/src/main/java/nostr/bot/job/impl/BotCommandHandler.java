/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package nostr.bot.job.impl;

import java.io.IOException;
import java.util.logging.Level;
import lombok.NoArgsConstructor;
import lombok.extern.java.Log;
import nostr.base.Command;
import nostr.base.PublicKey;
import nostr.base.Relay;
import nostr.bot.core.Bot;
import nostr.bot.core.BotRunner;
import nostr.bot.core.command.CommandParser;
import nostr.event.unmarshaller.impl.EventUnmarshaller;
import nostr.id.Identity;
import nostr.util.NostrException;
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
        try {
            final var botRunner = getBotRunner(jsonEvent);

            final String strCmd = getCommand(jsonEvent);

            final var cmd = CommandParser.builder().command(strCmd).botRunner(botRunner).build().parse();

            botRunner.execute(cmd);
        } catch (IOException | NostrException ex) {
            log.log(Level.SEVERE, null, ex);
        }

    }

    @Override
    public void onAuth(String challenge, Relay relay) {
        log.log(Level.INFO, "Command: {0} - Challenge: {1} - Relay {3}", new Object[]{Command.AUTH, challenge, relay});
    }

    private BotRunner getBotRunner(String jsonEvent) throws IOException, NostrException {
        final var identity = new Identity("/profile.properties");
        final var bot = new Bot();
        final PublicKey recipient = getRecipient(jsonEvent);

        return new BotRunner(bot, identity, recipient);
    }

    private String getCommand(String jsonEvent) throws IOException, NostrException {
        final var dmEvent = new EventUnmarshaller(jsonEvent).unmarshall();
        final var id = new Identity("/profile.properties");
        return id.decryptDirectMessage(dmEvent.getContent(), dmEvent.getPubKey());
    }

    private PublicKey getRecipient(String jsonEvent) {
        final var dmEvent = new EventUnmarshaller(jsonEvent).unmarshall();
        return dmEvent.getPubKey();
    }
}
