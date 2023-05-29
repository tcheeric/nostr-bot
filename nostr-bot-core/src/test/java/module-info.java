/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/module-info.java to edit this template
 */

module nostr.bot.test {
    requires nostr.bot.core;
    requires nostr.id;
    requires nostr.util;
    requires nostr.event;
    requires static lombok;
    requires java.logging;
    requires java.desktop;
    requires nostr.base;
    requires org.junit.jupiter.api;
    requires jakarta.validation;
    requires org.junit.platform.engine;
    requires org.junit.jupiter.engine;
    requires org.junit.platform.commons;
    
    exports nostr.test.bot.factory.command;
    exports nostr.test.bot.core.command;
    
    provides nostr.bot.core.command.ICommand with nostr.test.bot.factory.command.TestCommand1;
}
