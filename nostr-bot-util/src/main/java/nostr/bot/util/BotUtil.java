/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package nostr.bot.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import lombok.extern.java.Log;
import nostr.event.impl.GenericEvent;
import nostr.id.Client;

/**
 *
 * @author eric
 */
@Log
public class BotUtil {

    //public static Identity IDENTITY = Identity.getInstance();
    public static long readLongFromFile(String filename) {

        long value = System.currentTimeMillis();
        try {

            if (!new File(filename).exists()) {
                log.log(Level.FINE, "Creating the data file...");
                if (new File(filename).createNewFile()) {
                    log.log(Level.FINE, "File {0} created!s", filename);
                } else {
                    throw new IOException(String.format("Could not create file %s", filename));
                }
            }

            try (var fileInputStream = new FileInputStream(filename); var dataInputStream = new DataInputStream(fileInputStream)) {
                value = dataInputStream.readLong();
            }

        } catch (IOException e) {
            log.log(Level.WARNING, null, e);
            throw new RuntimeException(e);
        }
        return value;
    }

    public static void storeLongToFile(long value, String filename) {
        log.log(Level.FINER, "Storing value {0} to file...", value);
        try {
            try (FileOutputStream fileOutputStream = new FileOutputStream(filename); DataOutputStream dataOutputStream = new DataOutputStream(fileOutputStream)) {
                dataOutputStream.writeLong(value);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Client createClient() {
        final var client = Client.getInstance();

        do {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            }
        } while (client.getThreadPool().getCompletedTaskCount() < (client.getRelays().size() / 2));

        return client;
    }

    public static GenericEvent unmarshallEvent(String jsonEvent) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            GenericEvent event = objectMapper.readValue(jsonEvent, GenericEvent.class);
            return event;
        } catch (JsonProcessingException ex) {
            throw new RuntimeException(ex);
        }
    }    
}
