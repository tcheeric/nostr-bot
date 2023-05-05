[![](https://jitpack.io/v/tcheeric/nostr-bot.svg)](https://jitpack.io/#tcheeric/nostr-bot)

# nostr-bot
A framework for creating interactive bots on nostr.

## Installation
//TODO


## Some Definitions
1. We define a **bot** as an automated program that performs tasks (commands) in a sequential manner, on behalf of a user.
2. A **command** is a Java class that defines a specific task that a bot can execute. 

## Instructions
To create your bot, all you need is to write your command class(es).

The command is instantiated by parsing the DM text you send to the bot nostr account.

You use the ``BotRunner`` class to execute the commands.

## Example
I define here the ``hello`` command. To run the command, send the following text as DM to the bot account:

``
!hello Johnny
``

The bot will parse the DM and instantiate the command class and set the name attribute (parameter) to Johnny, then send a DM reply with the text

``
hi Johnny
``

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
