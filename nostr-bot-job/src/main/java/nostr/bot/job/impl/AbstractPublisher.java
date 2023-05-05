/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package nostr.bot.job.impl;

import java.io.IOException;
import java.time.Instant;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import lombok.AllArgsConstructor;
import lombok.extern.java.Log;
import nostr.base.PublicKey;
import nostr.bot.job.IPublisher;
import nostr.bot.util.BotUtil;
import nostr.bot.util.JobConfiguration;
import nostr.event.Kind;
import nostr.event.impl.Filters;
import nostr.event.impl.GenericMessage;
import nostr.event.list.KindList;
import nostr.event.list.PublicKeyList;
import nostr.event.message.ReqMessage;
import nostr.id.Client;

/**
 *
 * @author eric
 */
@Log
@AllArgsConstructor
public class AbstractPublisher implements IPublisher {

    private final Kind kind;
    private final PublicKey recipient;

    @Override
    public void publish() throws IOException {

        var jc = new JobConfiguration("data");
        var datafile = jc.getDataFile();
        var client = new Client("/relays.properties");

        //Wait it until tried to connect to a half of relays
        do {
            try {
                log.log(Level.INFO, "Wait it until tried to connect to a half of relays...");
                Thread.sleep(5000);
            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            }
        } while (client.getThreadPool().getCompletedTaskCount() < (client.getRelays().size() / 2));

        try {
            var kinds = new KindList();
            kinds.add(this.kind);
            var referencePubKeys = new PublicKeyList();
            referencePubKeys.add(this.recipient);

            synchronized (AbstractPublisher.class) {
                var since = BotUtil.readLongFromFile(datafile);

                log.log(Level.INFO, "Filtering event since {0}", since);
                var filters = Filters.builder().kinds(kinds).referencePubKeys(referencePubKeys).since(since).build();

                log.log(Level.INFO, "Filters: {0}", filters);

                GenericMessage message = new ReqMessage("nostr-bot", filters);

                log.log(Level.INFO, "Sending message {0}", message);
                client.send(message);
            }
        } finally {
            synchronized (AbstractPublisher.class) {
                var since = Instant.now().getEpochSecond();
                BotUtil.storeLongToFile(since, datafile);
            }
        }
    }
}
