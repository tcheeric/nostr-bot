/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package nostr.bot.core;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import java.io.IOException;
import java.util.Arrays;
import java.util.logging.Level;
import lombok.Data;
import lombok.NonNull;
import lombok.extern.java.Log;
import nostr.base.PublicKey;
import nostr.bot.core.command.ICommand;
import nostr.bot.core.command.annotation.Command;
import nostr.bot.core.command.annotation.Whitelist;
import nostr.bot.util.CommandsConfiguration;
import nostr.bot.util.SecurityConfiguration;
import nostr.event.impl.DirectMessageEvent;
import nostr.event.impl.GenericMessage;
import nostr.event.list.TagList;
import nostr.event.message.EventMessage;
import nostr.event.tag.PubKeyTag;
import nostr.id.Client;
import nostr.id.Identity;
import nostr.util.NostrException;
import org.hibernate.validator.HibernateValidator;

/**
 *
 * @author eric
 */
@Data
@Log
public class BotRunner {

    private final IBot bot;
    private final Context context;
    private final PublicKey recipient;

    public BotRunner(IBot bot, Identity identity, PublicKey recipient) {
        this.bot = bot;
        this.context = new Context();
        this.context.setIdentity(identity);
        this.recipient = recipient;
    }

    public void execute(ICommand command) {

        String key = command.getId();

        this.context.addCommandToStack(key);

        try {

            checkCommandIsInScope(command);

            validateCommandParameters(command);

            checkSecurity(command);

        } catch (RuntimeException | NostrException ex) {
            this.context.addParamValue(key, ex);

            sendDirectMessage(recipient, "AN ERROR OCCURRED: " + ex.getMessage(), command);

            throw new RuntimeException(ex);
        }

        Object value = command.execute(context);
        this.context.addParamValue(key, value);

        sendDirectMessage(recipient, value.toString(), command);

        log.log(Level.INFO, "Command value: {0}", value.toString());
    }

    public void execute(Bot bot) {
        var command = bot.getStartCommand();
        execute(command);
    }

    public String help() {
        return null;
    }

    public String help(ICommand command) {
        return command.getHelp();
    }

    private void sendDirectMessage(PublicKey recipient, String content, ICommand command) {

        log.log(Level.INFO, "Sending DM reply with content {0} from command {1}", new Object[]{content, command.getId()});
//        var executor = Executors.newSingleThreadExecutor();
//
//        executor.submit(() -> {
        try {
            final var tagList = new TagList();
            //final var rcptId = this.context.getIdentity();
            tagList.add(PubKeyTag.builder().publicKey(recipient).build());

            final var sender = getAdmin(command);
            final var event = new DirectMessageEvent(sender.getPublicKey(), tagList, content);

            sender.encryptDirectMessage(event);
            sender.sign(event);

            final GenericMessage message = new EventMessage(event);

            log.log(Level.INFO, "Message: {0}", message);

            final var client = new Client("/relays.properties");

            do {
                Thread.sleep(5000);
            } while (client.getThreadPool().getCompletedTaskCount() < (client.getRelays().size() / 2));

            client.send(message);

        } catch (NostrException | IOException | InterruptedException ex) {
            log.log(Level.SEVERE, null, ex);
        }
//        });
    }

    private void checkSecurity(ICommand command) throws NostrException {
        Whitelist whitelist = command.getClass().getDeclaredAnnotation(Whitelist.class);
        if (whitelist != null) {

            var npub = this.context.getIdentity().getPublicKey().getBech32();
            var npubOpt = Arrays.asList(whitelist.npubs()).stream().filter(n -> n.equalsIgnoreCase(npub)).findFirst();
            if (npubOpt.isEmpty()) {

                var domOpt = Arrays.asList(whitelist.domains()).stream().filter(d -> npub.endsWith(d)).findFirst();
                if (domOpt.isEmpty()) {

                    var grpOpt = Arrays.asList(whitelist.groups()).stream().map(s -> getGroupUsers(command, s)).filter(users -> Arrays.asList(users).contains(npub)).findAny();
                    if (grpOpt.isEmpty()) {
                        throw new SecurityException(String.format("User %s is not allowed to execute the command %s", new Object[]{npub, command.getId()}));
                    }
                    log.log(Level.FINE, "Group-level security access");
                    return;
                }
                log.log(Level.FINE, "Domain-level security access");
                return;
            }
            log.log(Level.FINE, "npub-level security access");
            return;
        }
        log.log(Level.FINE, "Global security access");
    }

    private String[] getGroupUsers(ICommand command, String group) {
        try {
            final var securityConfiguration = new SecurityConfiguration(command.getId());
            return securityConfiguration.getGroupUsers(group);
        } catch (IOException ex) {
            log.log(Level.WARNING, null, ex);
            return new String[]{};
        }
    }

    private void validateCommandParameters(@NonNull ICommand command) {
        log.log(Level.FINE, "validate");

        var constraintViolation = Validation.byProvider(HibernateValidator.class)
                .configure()
                .failFast(true)
                .buildValidatorFactory()
                .getValidator()
                .validate(command);

        if (!constraintViolation.isEmpty()) {
            final ConstraintViolation<ICommand> cv = constraintViolation.iterator().next();
            throw new RuntimeException(cv.getPropertyPath() + " " + cv.getMessage());
        }
    }

    private void checkCommandIsInScope(@NonNull ICommand command) {
        log.log(Level.FINE, "checkExecutionOrder");

        String[] sources = command.getClass().getDeclaredAnnotation(Command.class).parents();

        if (sources.length > 0) {
            String topStackCommand = this.context.getTopCommandFromStack();
            if (topStackCommand == null || !Arrays.asList(sources).contains(topStackCommand)) {
                throw new RuntimeException(String.format("Invalid command call. %s cannot be invoked after %s", new Object[]{command.getId(), topStackCommand}));
            }
        }

        log.log(Level.FINE, "checkExecutionOrder of {0} : OK", command.getId());
    }

    private Identity getAdmin(ICommand command) throws IOException, NostrException {
        var config = new CommandsConfiguration(command.getId());
        return config.getAdmin(command.getId());
    }
}
