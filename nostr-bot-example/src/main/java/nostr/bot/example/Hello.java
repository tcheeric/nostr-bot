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

/**
 *
 * @author eric
 */
@Command(id = "hello", name = "Hello Command", sources = {})
@Whitelist(groups = {"admin"})
@Log
@Data
@EqualsAndHashCode(callSuper = false)
public class Hello extends AbstractCommand<String> {

    @Param(name = "name", index = 0)
    private String name;

    public Hello() {
        super();
    }

    public Hello(String name) {
        this.name = name;
    }

    @Override
    public String execute(Context context) {
        return sayHi(context);
    }

    @Override
    public String getHelp() {
        log.log(Level.FINE, "getHelp");
        return null;
    }

    private String sayHi(Context context) {
        var tmp = this.name == null ? context.getIdentity().getPublicKey().toBech32() : this.name;
        return "Hi " + tmp;
    }
}
