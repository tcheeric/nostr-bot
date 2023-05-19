/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package nostr.bot.util;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import lombok.extern.java.Log;
import nostr.id.Identity;
import nostr.util.NostrException;

/**
 *
 * @author eric
 */
@Log
public class BotUtil {

    public static Identity IDENTITY = getIdentity();

    public static long readLongFromFile(String filename) {

        long value = System.currentTimeMillis();
        try {

            if (!new File(filename).exists()) {
                log.log(Level.INFO, "Creating the data file...");
                if (new File(filename).createNewFile()) {
                    log.log(Level.INFO, "File {0} created!s", filename);
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
        log.log(Level.INFO, "Storing value {0} to file...", value);
        try {
            try (FileOutputStream fileOutputStream = new FileOutputStream(filename); DataOutputStream dataOutputStream = new DataOutputStream(fileOutputStream)) {
                dataOutputStream.writeLong(value);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static Identity getIdentity() {
        try {
            return new Identity("/profile.properties");
        } catch (IOException | NostrException ex) {
            log.log(Level.SEVERE, null, ex);
            return null;
        }
    }

}
