/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/module-info.java to edit this template
 */

module nostr.bot.test {
    // REQUIRES
    requires static lombok;
    requires java.logging;

    requires nostr.bot.core;
    requires nostr.id;
    requires nostr.util;
    requires nostr.event;
    requires nostr.base;
    
    requires java.desktop;

    requires jakarta.validation;

    requires org.junit.jupiter.api;
    requires org.junit.platform.engine;
    requires org.junit.jupiter.engine;
    requires org.junit.platform.commons;
    
    // EXPORTS
    exports nostr.test.bot.factory.command;
    exports nostr.test.bot.core.command;
    
    // PROVIDES
    provides nostr.bot.core.command.ICommand with nostr.test.bot.factory.command.TestCommand1;
    
    // OPENS
    opens nostr.test.bot.factory.command to org.hibernate.validator;
}
