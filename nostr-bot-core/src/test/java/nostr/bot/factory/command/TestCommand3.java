/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package nostr.bot.factory.command;

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
@Command(id = "command3", name = "TestCommand3 Command", parents = {"command1"})
@Whitelist(domains = {"@badgr.space"})
@Log
@Data
@EqualsAndHashCode(callSuper = false)
public class TestCommand3 extends AbstractCommand<Integer> {

    @Param(index = 0, name = "name")
    private String name;
    
    @Override
    public Integer execute(Context context) {
        return 1;
    }

    @Override
    public String getHelp() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
}
