/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package nostr.bot.job.impl;

import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.java.Log;
import nostr.base.PublicKey;
import nostr.bot.core.Bot;
import nostr.bot.core.BotRunner;
import nostr.bot.core.IBot;
import nostr.bot.core.command.CommandParser;
import nostr.bot.job.ISubscriber;
import nostr.bot.util.BotUtil;
import nostr.event.unmarshaller.impl.EventUnmarshaller;
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
                final var botRunner = getBotRunner();

                final var message = getContent();

                final var command = CommandParser.builder().command(message).botRunner(botRunner).build().parse();

                log.log(Level.INFO, "Executing the bot runner...");
                botRunner.execute(command);

            } catch (IOException | NostrException ex) {
                log.log(Level.SEVERE, null, ex);
                throw new RuntimeException(ex);
            }
        });
    }

    protected abstract String getContent();

    private BotRunner getBotRunner() throws IOException, NostrException {
        final IBot bot = new Bot();
        return new BotRunner(bot, BotUtil.IDENTITY, getRecipient());
    }

    private PublicKey getRecipient() {
        final var dmEvent = new EventUnmarshaller(jsonEvent).unmarshall();
        return dmEvent.getPubKey();
    }

}
