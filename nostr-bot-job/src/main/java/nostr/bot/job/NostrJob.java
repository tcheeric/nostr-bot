/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package nostr.bot.job;

import java.io.IOException;
import java.util.Date;
import java.util.logging.Level;
import lombok.NoArgsConstructor;
import lombok.extern.java.Log;
import static nostr.bot.job.Mode.PRIVATE;
import static nostr.bot.job.Mode.PUBLIC;
import nostr.bot.job.impl.DMPublisher;
import nostr.bot.job.impl.TextNotePublisher;
import nostr.bot.util.BotUtil;
import nostr.bot.util.JobConfiguration;
import nostr.util.NostrException;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobKey;

/**
 *
 * @author eric
 */
@Log
@NoArgsConstructor
public class NostrJob implements Job {

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        try {
            JobKey jobKey = context.getJobDetail().getKey();
            log.log(Level.INFO, "Executing job: {0} executing at {1},fired by: {2}", new Object[]{jobKey, new Date(), context.getTrigger().getKey()});
            IPublisher publisher = getPublisher();
            publisher.publish(BotUtil.createClient());
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    private IPublisher getPublisher() throws IOException, NostrException {

        JobConfiguration jc = new JobConfiguration("job");
        var mode = Mode.valueOf(jc.getMode());

        switch (mode) {
            case PRIVATE -> {
                //return new DMPublisher(BotUtil.IDENTITY.getPublicKey());
                return new DMPublisher();
            }
            case PUBLIC -> {
                //return new TextNotePublisher(BotUtil.IDENTITY.getPublicKey());
                return new TextNotePublisher();
            }
            default ->
                throw new AssertionError();
        }
    }

}
