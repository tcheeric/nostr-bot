/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package nostr.bot.factory.command;

import jakarta.validation.constraints.NotNull;
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
@Command(id = "command1", name = "TestCommand1 Command", sources = {})
@Whitelist(npubs = "npub1abcdef")
@Log
@Data
@EqualsAndHashCode(callSuper = false)
public class TestCommand1 extends AbstractCommand<String> {

    @Param(index = 0, name = "len")
    private Integer length;
    
    @Param(index = 1, name = "name")
    @NotNull
    private String name;
    
    @Override
    public String execute(Context context) {
        return this.toString();
    }

    @Override
    public String getHelp() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

}
