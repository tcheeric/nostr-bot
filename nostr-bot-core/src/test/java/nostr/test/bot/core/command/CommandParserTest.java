/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package nostr.test.bot.core.command;

import java.io.IOException;
import java.text.ParseException;
import java.util.Arrays;
import java.util.List;
import nostr.base.PublicKey;
import nostr.bot.core.command.CommandParser;
import nostr.test.bot.factory.EntitiyFactory;
import nostr.test.bot.factory.command.TestCommand1;
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

        var bot = EntitiyFactory.createBot();

        CommandParser instance = CommandParser.builder().bot(bot).command("!command1 12 Satoshi").build();
        instance.parse();

        var command = instance.getBot().getContext().getCommand();

        assertEquals(TestCommand1.class, command.getClass());
        assertEquals(12, ((TestCommand1) command).getLength());
        assertEquals("Satoshi", ((TestCommand1) command).getName());
    }

    @Test
    public void testParseFail() throws IOException, NostrException {
        System.out.println("testParseFail");

        var bot = EntitiyFactory.createBot();

        CommandParser instance = CommandParser.builder().bot(bot).command("command1 12 Satoshi").build();

        var thrown = Assertions.assertThrows(ParseException.class,
                () -> {
                    instance.parse();
                }
        );
        Assertions.assertNotNull(thrown);
    }

    @Test
    public void testParseFailValidation() throws IOException, NostrException, ParseException {
        System.out.println("testParseFailValidation");

        var bot = EntitiyFactory.createBot();

        CommandParser instance = CommandParser.builder().bot(bot).command("!command1 12").build();

        instance.parse();

        var thrown = Assertions.assertThrows(RuntimeException.class,
                () -> {
                    final var command = bot.getContext().getCommand();
                    bot.execute(command, EntitiyFactory.createDirectMessageEvent(new PublicKey(new byte[32]), new PublicKey(new byte[32]), "testParseFailValidation"));
                }
        );
        Assertions.assertNotNull(thrown);

        var context = instance.getBot().getContext();
        List<Object> values = Arrays.asList(context.getValues("command1"));
        Assertions.assertNotNull(values.stream().filter(o -> (o instanceof RuntimeException)).findFirst().get());
    }

    @Test
    public void testCheckCommandIsInScopeError() throws IOException, NostrException {
        System.out.println("testCheckCommandIsInScopeError");

        var bot = EntitiyFactory.createBot();

        CommandParser instance = CommandParser.builder().bot(bot).command("command2").build();
        var command = bot.getContext().getCommand();

        var thrown = Assertions.assertThrows(ParseException.class,
                () -> {
                    instance.parse();
                }, String.format("Invalid command call. %s cannot be invoked after %s", new Object[]{"command2", command.getId()})
        );
        Assertions.assertNotNull(thrown);
    }

    @Test
    public void testCheckSecurityNpubFail() throws Exception {
        System.out.println("testCheckSecurityNpubFail");

        var bot = EntitiyFactory.createBot();

        CommandParser instance = CommandParser.builder().bot(bot).command("command1 32 CSW").build();

        var thrown = Assertions.assertThrows(ParseException.class,
                () -> {
                    instance.parse();
                }
        );
        Assertions.assertNotNull(thrown);
    }

}
