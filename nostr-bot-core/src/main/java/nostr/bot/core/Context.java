/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package nostr.bot.core;

import java.time.Instant;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.logging.Level;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import lombok.extern.java.Log;
import nostr.id.Client;
import nostr.id.Identity;

/**
 *
 * @author eric
 */
@Data
@Log
public class Context {

    private static Context INSTANCE;
    
    private final LinkedList<String> commandStack;
    private final Map<String, Object> parameters;
    private final Map<String, NostrEventStatus> processedEvents;
    private final Identity identity;
    private final Client client;
    
    private final static long FIVE_MINUTES = 60 * 5;
    final static String STATUS_PROCESSED = "OK";
    final static String STATUS_PENDING = "PENDING";

    private Context(Client client, Identity identity) {
        this.commandStack = new LinkedList<>();
        this.parameters = new HashMap<>();
        this.processedEvents = new HashMap<>();
        this.client = client;
        this.identity = identity;
    }
    
    public static final Context getInstance(Client client, Identity identity) {
        if(INSTANCE == null) {
            INSTANCE = new Context(client, identity);
        }
        
        return INSTANCE;
    }

    public void addCommandToStack(String commandId) {
        String key = System.currentTimeMillis() + "." + commandId;
        this.commandStack.add(key);
    }

    public void addParamValue(String param, Object value) {
        String key = System.currentTimeMillis() + "." + param;
        log.log(Level.FINE, "Adding parameter ({0}, {1}) to context", new Object[]{key, value});
        this.parameters.put(key, value);
    }

    public String getTopCommandFromStack() {
        if (commandStack.isEmpty()) {
            return null;
        }
        return commandStack.getLast();
    }

    public Object[] getValues(String key) {
        Object[] keyArr = parameters.keySet().stream().filter(k -> k.contains("." + key)).toArray();
        Object[] result = new Object[keyArr.length];

        int i = 0;
        for (Object o : keyArr) {
            result[i++] = parameters.get(o.toString());
        }

        return result;
    }

    synchronized boolean addEvent(@NonNull String eventId, long date) {
        if (containsEvent(eventId)) {
            return false;
        }

        this.processedEvents.put(eventId, NostrEventStatus.builder().date(date).build());
        return true;
    }

    synchronized boolean updateEventStatus(@NonNull String eventId, @NonNull String status) {
        if (!containsEvent(eventId)) {
            return false;
        }

        this.processedEvents.get(eventId).setStatus(status);
        return true;
    }
    
    synchronized boolean containsEvent(String eventId) {
        return this.processedEvents.containsKey(eventId);
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
}
