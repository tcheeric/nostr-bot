/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package nostr.bot.factory.command;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.extern.java.Log;
import nostr.bot.core.Context;
import nostr.bot.core.command.AbstractCommand;
import nostr.bot.factory.command.TestCommand2.TestData;
import nostr.bot.core.command.annotation.Command;
import nostr.bot.core.command.annotation.Whitelist;

/**
 *
 * @author eric
 */
@Command(id = "command2", name = "TestCommand2 Command", parents = {"command1"})
@Whitelist(groups = {"admin"})
@Log
@Data
@EqualsAndHashCode(callSuper = false)
public class TestCommand2 extends AbstractCommand<TestData>{

    @Override
    public TestData execute(Context context) {
        return new TestData();
    }

    @Override
    public String getHelp() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Data
    @ToString
    public static class TestData {
        
        private final String a;
        private final int b;

        public TestData() {
            this.a = this.getClass().getName();
            this.b = 100;
        }
        
    }
}
