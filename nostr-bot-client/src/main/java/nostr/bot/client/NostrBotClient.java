/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package nostr.bot.client;

import java.io.IOException;
import java.util.logging.Level;
import lombok.extern.java.Log;
import nostr.bot.job.NostrJob;
import nostr.bot.util.JobConfiguration;
import nostr.util.NostrException;
import static org.quartz.JobBuilder.newJob;
import org.quartz.SchedulerException;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;
import org.quartz.impl.StdSchedulerFactory;

/**
 *
 * @author eric
 */
@Log
public class NostrBotClient {

    public static void main(String[] args) throws InterruptedException, IOException, NostrException {
        NostrBotClient.schedule();
//        Client client = BotUtil.createClient();
//        Identity identity = new Identity("/profile.properties");
//        client.auth(identity, String.valueOf(System.currentTimeMillis()));
    }

    public static void schedule() throws InterruptedException {
        try {
            var scheduler = StdSchedulerFactory.getDefaultScheduler();

            scheduler.start();

            var job = newJob(NostrJob.class)
                    .withIdentity("NostrBotJob", "group1")
                    .build();

            var jc = new JobConfiguration("job");
            var interval = jc.getInterval();

            var trigger = newTrigger()
                    .withIdentity("NostrBotTrigger", "group1")
                    .startNow()
                    .withSchedule(simpleSchedule()
                            .withIntervalInSeconds(interval)
                            .repeatForever())
                    .build();

            // Tell quartz to schedule the job using our trigger
            scheduler.scheduleJob(job, trigger);

        } catch (SchedulerException | IOException ex) {
            log.log(Level.SEVERE, null, ex);
            throw new RuntimeException(ex);
        }
    }

}
