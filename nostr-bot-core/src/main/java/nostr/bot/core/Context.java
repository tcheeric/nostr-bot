/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package nostr.bot.core;

import java.io.Serializable;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Level;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.ToString;
import lombok.extern.java.Log;
import nostr.bot.core.command.ICommand;
import nostr.id.Client;
import nostr.id.Identity;

/**
 *
 * @author eric
 * @param <T>
 */
@Data
@Log
public class Context<T extends ICommand> {

    private static Context INSTANCE;

    private T command;
    private final Map<ContextElement, Object> parameters;
    private final Map<ContextElement, NostrEventStatus> processedEvents;
    private final Identity identity;
    private final Client client;

    private final static long FIVE_MINUTES = 60 * 5;
    final static String STATUS_PROCESSED = "OK";
    final static String STATUS_PENDING = "PENDING";

    private Context(Client client, Identity identity) {
        this.parameters = new HashMap<>();
        this.processedEvents = new HashMap<>();
        this.client = client;
        this.identity = identity;
    }

    public static final Context getInstance(Client client, Identity identity) {
        if (INSTANCE == null) {
            INSTANCE = new Context(client, identity);
        }

        return INSTANCE;
    }

    public void addParamValue(String param, Object value) {
        var key = new ContextElement(param);
        log.log(Level.FINE, "Adding parameter ({0}, {1}) to context", new Object[]{key, value});
        this.parameters.put(key, value);
    }

    public Object[] getValues(String key) {
        Object[] keyArr = parameters.keySet().stream().filter(k -> k.getKey().equals(key)).toArray();
        var result = new Object[keyArr.length];

        int i = 0;
        for (Object o : keyArr) {
            result[i++] = parameters.get((ContextElement) o);
        }

        return result;
    }

    synchronized boolean addEvent(@NonNull String eventId, long date) {
        if (containsEvent(eventId)) {
            return false;
        }

        this.processedEvents.put(new ContextElement(eventId), NostrEventStatus.builder().date(date).build());
        return true;
    }

    synchronized boolean updateEventStatus(@NonNull String eventId, @NonNull String status) {
        if (!containsEvent(eventId)) {
            return false;
        }

        var optElement = getElement(eventId);
        if (optElement.isPresent()) {
            this.processedEvents.get(optElement.get()).setStatus(status);
            return true;
        }
        return false;
    }

    synchronized boolean containsEvent(String eventId) {
        return this.processedEvents.keySet().stream().anyMatch(c -> c.getKey().equals(eventId));
    }

    synchronized Optional<ContextElement> getElement(String eventId) {
        return this.processedEvents.keySet().stream().filter(c -> c.getKey().equals(eventId)).findFirst();
    }

    void purgeStaleEvents() {
        long now = Instant.now().getEpochSecond();
        this.processedEvents.keySet()
                .stream()
                .filter(e -> now - this.processedEvents.get(e).getDate() > FIVE_MINUTES && this.processedEvents.get(e).getStatus().equals(STATUS_PROCESSED))
                .forEach(e -> {
                    log.log(Level.FINE, "Removing processed event {0} from memory", e);
                    this.processedEvents.remove(e);
                });
    }

    @Data
    @Builder
    private static class NostrEventStatus {

        private final long date;
        @Builder.Default
        private String status = STATUS_PENDING;
    }

    @Data
    @ToString
    @AllArgsConstructor
    @EqualsAndHashCode
    private static class ContextElement implements Serializable {

        private final Date date;        
        private final String key;

        public ContextElement(String key) {
            this(new Date(), key);
        }

    }
}
