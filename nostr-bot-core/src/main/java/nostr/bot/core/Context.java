/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package nostr.bot.core;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.logging.Level;
import lombok.Data;
import lombok.extern.java.Log;
import nostr.id.Identity;

/**
 *
 * @author eric
 */
@Data
@Log
public class Context {

    private final LinkedList<String> commandStack;
    private final HashMap<String, Object> parameters;
    private Identity identity;

    public Context() {
        this.commandStack = new LinkedList<>();
        this.parameters = new HashMap<>();
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
        return commandStack.getLast();
    }
}
