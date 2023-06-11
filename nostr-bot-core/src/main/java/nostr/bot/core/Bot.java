/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package nostr.bot.core;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ServiceLoader;
import java.util.logging.Level;
import lombok.Data;
import lombok.NonNull;
import lombok.extern.java.Log;
import nostr.base.PublicKey;
import nostr.bot.core.command.ICommand;
import nostr.bot.core.command.annotation.Command;
import nostr.bot.core.command.annotation.Whitelist;
import nostr.bot.util.BotUtil;
import nostr.bot.util.SecurityConfiguration;
import nostr.event.BaseTag;
import nostr.event.impl.DirectMessageEvent;
import nostr.event.impl.GenericEvent;
import nostr.event.impl.GenericMessage;
import nostr.event.message.EventMessage;
import nostr.event.tag.PubKeyTag;
import nostr.id.Client;
import nostr.id.Identity;
import nostr.util.NostrException;
import org.hibernate.validator.HibernateValidator;

/**
 *
 * @author eric
 * @param <T>
 */
@Data
@Log
public class Bot<T extends ICommand> {

    private static Bot INSTANCE;
    private final Context<T> context;
    private final PublicKey recipient;

    private Bot(PublicKey recipient) {

        this.context = Context.getInstance(BotUtil.createClient(), Identity.getInstance());

        ServiceLoader
                .load(ICommand.class)
                .stream()
                .map(p -> p.get())
                .filter(c -> c.getClass().isAnnotationPresent(Command.class))
                .forEach(c -> this.context.setCommand((T) c));

        this.recipient = recipient;
    }

    public static Bot getInstance(PublicKey recipient) {
        if (INSTANCE == null) {
            INSTANCE = new Bot(recipient);
        }

        return INSTANCE;
    }

    public void execute(ICommand command) {
        this.execute(command, null);
    }

    public void execute(ICommand command, GenericEvent event) {

        if (event != null) {
            if (!this.context.addEvent(event.getId(), event.getCreatedAt())) {
                return;
            }
        }

        String key = command.getId();

        try {
            validateCommand(command);

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

    }

    public String help() {
        return null;
    }

    public String help(ICommand command) {
        return command.getHelp();
    }

    public static void updateEventStatus(String eventId) {
        if (INSTANCE != null) {
            log.log(Level.FINE, "Event {0} status updated to {1}", new Object[]{eventId, Context.STATUS_PROCESSED});
            INSTANCE.context.updateEventStatus(eventId, Context.STATUS_PROCESSED);
        }
    }

    public static void auth(String challenge) throws IOException, NostrException {
        Identity identity = Identity.getInstance();
        INSTANCE.context.getClient().auth(identity, challenge);
    }

    private void sendDirectMessage(PublicKey recipient, String content, ICommand command) {

        log.log(Level.INFO, "Sending DM reply with content {0} from command {1}", new Object[]{content, command.getId()});
        try {
            final var tagList = new ArrayList<BaseTag>();
            tagList.add(PubKeyTag.builder().publicKey(recipient).build());

            final var sender = Identity.getInstance();
            final var event = new DirectMessageEvent(sender.getPublicKey(), tagList, content);

            sender.encryptDirectMessage(event);
            sender.sign(event);

            final GenericMessage message = new EventMessage(event);

            final var client = getClient();

            client.send(message);

        } catch (NostrException ex) {
            log.log(Level.SEVERE, null, ex);
        }
    }

    private Client getClient() {
        return this.context.getClient();
    }

    private void checkSecurity(ICommand command) throws NostrException {
        Whitelist whitelist = command.getClass().getDeclaredAnnotation(Whitelist.class);
        if (whitelist != null) {

            var npub = this.context.getIdentity().getPublicKey().getBech32();
            var npubOpt = Arrays.asList(whitelist.npubs()).stream().filter(n -> n.equalsIgnoreCase(npub)).findFirst();
            if (npubOpt.isEmpty()) {

                var grpOpt = Arrays.asList(whitelist.groups()).stream().map(s -> getGroupUsers(command, s)).filter(users -> Arrays.asList(users).contains(npub)).findAny();
                if (grpOpt.isEmpty()) {
                    throw new SecurityException(String.format("User %s is not allowed to execute the command %s", new Object[]{npub, command.getId()}));
                }
                log.log(Level.FINE, "Group-level security access");
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

    private void validateCommand(ICommand command) {
        if (!command.getId().equals(this.context.getCommand().getId())) {
            throw new RuntimeException("Invalid command call " + command);
        }
    }
}
