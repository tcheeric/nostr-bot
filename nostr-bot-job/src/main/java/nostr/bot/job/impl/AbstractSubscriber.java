/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package nostr.bot.job.impl;

import java.io.IOException;
import java.text.ParseException;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.java.Log;
import nostr.base.PublicKey;
import nostr.bot.core.Bot;
import nostr.bot.core.command.CommandParser;
import nostr.bot.core.command.ICommand;
import nostr.bot.job.ISubscriber;
import nostr.bot.util.BotUtil;
import nostr.util.NostrException;

/**
 *
 * @author eric
 */
@Log
@Data
@AllArgsConstructor
public abstract class AbstractSubscriber implements ISubscriber {

    private String subscriptionId;
    private String jsonEvent;

    @Override
    public void subscribe() {
        log.log(Level.INFO, "process({0}, {1})", new Object[]{getSubscriptionId(), getJsonEvent()});

        var executor = Executors.newSingleThreadExecutor();

        executor.submit(() -> {
            try {
                final var bot = getBot();

                final var message = getContent();

                CommandParser.builder().command(message).bot(bot).build().parse();

                log.log(Level.INFO, "Executing the bot runner...");
                
                ICommand command = bot.getContext().getCommand();                
                bot.execute(command, BotUtil.unmarshallEvent(jsonEvent));

            } catch (IOException ex) {
                log.log(Level.SEVERE, null, ex);
                throw new RuntimeException(ex);
            } catch (ParseException | NostrException ex) {
                log.log(Level.SEVERE, null, ex);
            }
        });
    }

    protected abstract String getContent();

    private Bot getBot() throws IOException, NostrException {
        return Bot.getInstance(getRecipient());
    }

    private PublicKey getRecipient() {
        final var dmEvent = BotUtil.unmarshallEvent(jsonEvent);
        return dmEvent.getPubKey();
    }

}
