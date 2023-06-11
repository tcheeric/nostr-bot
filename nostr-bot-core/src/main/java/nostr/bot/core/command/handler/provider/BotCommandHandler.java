/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package nostr.bot.core.command.handler.provider;

import java.io.IOException;
import java.text.ParseException;
import java.util.logging.Level;
import lombok.NoArgsConstructor;
import lombok.extern.java.Log;
import nostr.base.Command;
import nostr.base.PublicKey;
import nostr.base.Relay;
import nostr.bot.core.Bot;
import nostr.bot.core.command.CommandParser;
import nostr.bot.core.command.ICommand;
import static nostr.bot.util.BotUtil.unmarshallEvent;
import nostr.event.impl.GenericEvent;
import nostr.id.Client;
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
        log.log(Level.INFO, "Command: {0} - Subscription ID: {1} - Relay {2}", new Object[]{Command.EOSE, subscriptionId, relay});
    }

    @Override
    public void onOk(String eventId, String reasonMessage, Reason reason, boolean result, Relay relay) {
        log.log(Level.INFO, "Command: {0} - Event ID: {1} - Reason: {2} ({3}) - Result: {4} - Relay {5}", new Object[]{Command.OK, eventId, reason, reasonMessage, result, relay});
        Bot.updateEventStatus(eventId);
    }

    @Override
    public void onNotice(String message) {
        log.log(Level.WARNING, "Command: {0} - Message: {1}", new Object[]{Command.NOTICE, message});
    }

    @Override
    public void onEvent(String jsonEvent, String subId, Relay relay) {
        try {
            final var bot =  getBot(jsonEvent);

            final String strCmd = getCommand(jsonEvent);

            CommandParser.builder().command(strCmd).bot(bot).build().parse();

            ICommand command = bot.getContext().getCommand();
            
            GenericEvent event = unmarshallEvent(jsonEvent);

            bot.execute(command, event);
        } catch (IOException | ParseException | NostrException ex) {
            log.log(Level.SEVERE, null, ex);
        }

    }

    @Override
    public void onAuth(String challenge, Relay relay) throws NostrException {
        log.log(Level.INFO, "Command: {0} - Challenge: {1} - Relay {3}", new Object[]{Command.AUTH, challenge, relay});
        
        var client = Client.getInstance();
        var identity = Identity.getInstance();
        
        client.auth(identity, challenge, relay);
    }

    private Bot getBot(String jsonEvent) throws IOException, NostrException {
        final var recipient = getRecipient(jsonEvent);

        return Bot.getInstance(recipient);
    }

    private String getCommand(String jsonEvent) throws IOException, NostrException {
        final var dmEvent = unmarshallEvent(jsonEvent);
        final var id = Identity.getInstance();
        return id.decryptDirectMessage(dmEvent.getContent(), dmEvent.getPubKey());
    }

    private PublicKey getRecipient(String jsonEvent) {
        final var dmEvent = unmarshallEvent(jsonEvent);
        return dmEvent.getPubKey();
    }
}
