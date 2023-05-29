/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package nostr.test.bot.core.command;

import java.io.IOException;
import java.text.ParseException;
import java.util.Arrays;
import java.util.List;
import nostr.base.PrivateKey;
import nostr.base.PublicKey;
import nostr.bot.core.command.CommandParser;
import nostr.bot.core.command.ICommand;
import nostr.test.bot.factory.EntitiyFactory;
import nostr.test.bot.factory.command.TestCommand1;
import nostr.id.Identity;
import nostr.util.NostrException;
import org.junit.jupiter.api.Assertions;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

/**
 *
 * @author eric
 */
public class CommandParserTest {

    @Test
    public void testParse() throws IOException, NostrException, ParseException {
        System.out.println("testParse");

        var runner = EntitiyFactory.createBotRunner();

        CommandParser instance = CommandParser.builder().botRunner(runner).command("!command1 12 Satoshi").build();
        ICommand result = instance.parse();

        assertEquals(TestCommand1.class, result.getClass());
        assertEquals(12, ((TestCommand1) result).getLength());
        assertEquals("Satoshi", ((TestCommand1) result).getName());
    }

    @Test
    public void testParseFail() throws IOException, NostrException {
        System.out.println("testParseFail");

        var runner = EntitiyFactory.createBotRunner();

        CommandParser instance = CommandParser.builder().botRunner(runner).command("command1 12 Satoshi").build();

        var thrown = Assertions.assertThrows(ParseException.class,
                () -> {
                    instance.parse();
                }
        );
        Assertions.assertNotNull(thrown);
    }

    @Test
    public void testParseFailValidation() throws IOException, NostrException {
        System.out.println("testParseFailValidation");

        var runner = EntitiyFactory.createBotRunner();

        CommandParser instance = CommandParser.builder().botRunner(runner).command("!command1 12").build();

        var thrown = Assertions.assertThrows(RuntimeException.class,
                () -> {
                    instance.getBotRunner().execute(instance.parse(), EntitiyFactory.createDirectMessageEvent(new PublicKey(new byte[32]), new PublicKey(new byte[32]), "testParseFailValidation"));
                }
        );
        Assertions.assertNotNull(thrown);

        var context = instance.getBotRunner().getContext();
        List<Object> values = Arrays.asList(context.getValues("command1"));
        Assertions.assertNotNull(values.stream().filter(o -> (o instanceof RuntimeException)).findFirst().get());
    }

    @Test
    public void testCheckCommandIsInScopeError() throws IOException, NostrException {
        System.out.println("testCheckCommandIsInScopeError");

        var runner = EntitiyFactory.createBotRunner();

        CommandParser instance = CommandParser.builder().botRunner(runner).command("command2").build();
        String topStackCommand = runner.getContext().getTopCommandFromStack();

        var thrown = Assertions.assertThrows(ParseException.class,
                () -> {
                    instance.parse();
                }, String.format("Invalid command call. %s cannot be invoked after %s", new Object[]{"command2", topStackCommand})
        );
        Assertions.assertNotNull(thrown);
    }

    @Test
    public void testCheckSecurityNpubFail() throws Exception {
        System.out.println("testCheckSecurityNpubFail");

        var runner = EntitiyFactory.createBotRunner(new Identity(new PrivateKey("00a5f09264f2a9c0db9b16b06e05ecd94ca3fd2fb7bb043a27232473e1525fdc")));        

        CommandParser instance = CommandParser.builder().botRunner(runner).command("command1 32 CSW").build();

        var thrown = Assertions.assertThrows(ParseException.class,
                () -> {
                    runner.execute(instance.parse(), EntitiyFactory.createDirectMessageEvent(new PublicKey(new byte[32]), new PublicKey(new byte[32]), "testCheckSecurityNpubFail"));
                }
        );
        Assertions.assertNotNull(thrown);
    }

}
