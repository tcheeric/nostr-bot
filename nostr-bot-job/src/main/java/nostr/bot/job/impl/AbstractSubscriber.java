/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package nostr.bot.job.impl;

import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
import lombok.extern.java.Log;
import nostr.bot.core.Bot;
import nostr.bot.core.BotRunner;
import nostr.bot.core.IBot;
import nostr.bot.core.command.CommandParser;
import nostr.bot.job.ISubscriber;
import nostr.bot.util.BotUtil;
import nostr.id.Identity;
import nostr.util.NostrException;
import nostr.ws.handler.response.DefaultEventResponseHandler;

/**
 *
 * @author eric
 */
@Log
public abstract class AbstractSubscriber extends DefaultEventResponseHandler implements ISubscriber {
    
    public AbstractSubscriber(String subscriptionId, String jsonEvent) {
        super(subscriptionId, jsonEvent);
    }

    @Override
    public void process() throws NostrException {
        this.subscribe();
    }

    @Override
    public void subscribe() {
        log.log(Level.INFO, "process({0}, {1})", new Object[]{getSubscriptionId(), getJsonEvent()});

        var executor = Executors.newSingleThreadExecutor();

        executor.submit(() -> {
            try {
                final var botRunner  = getBotRunner();

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
    
    private static BotRunner getBotRunner() throws IOException, NostrException {        
        final IBot bot = new Bot("/commands.properties");
        return new BotRunner(bot, BotUtil.IDENTITY);
    }    
}
