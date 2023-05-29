/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package nostr.bot.example;

import java.util.logging.Level;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.java.Log;
import nostr.bot.core.Context;
import nostr.bot.core.command.AbstractCommand;
import nostr.bot.core.command.annotation.Command;
import nostr.bot.core.command.annotation.Param;
import nostr.bot.core.command.annotation.Whitelist;
import nostr.util.NostrException;

/**
 *
 * @author eric
 */
@Command(id = "hello", name = "Hello Command", parents = {})
@Whitelist(groups = {"admin"})
@Log
@Data
@EqualsAndHashCode(callSuper = false)
public class HelloCommand extends AbstractCommand<String> {

    @Param(name = "name", index = 0)
    private String name;

    public HelloCommand() {
        super();
    }

    public HelloCommand(String name) {
        this.name = name;
    }

    @Override
    public String execute(Context context) {
        try {
            return sayHi(context);
        } catch (NostrException ex) {
            log.log(Level.SEVERE, null, ex);
            throw new RuntimeException(ex);
        }
    }

    @Override
    public String getHelp() {
        log.log(Level.FINE, "getHelp");
        return null;
    }

    private String sayHi(Context context) throws NostrException {
        var tmp = this.name == null ? context.getIdentity().getPublicKey().getBech32() : this.name;
        return "Hi " + tmp;
    }
}
