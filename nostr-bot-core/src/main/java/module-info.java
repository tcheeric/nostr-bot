/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/module-info.java to edit this template
 */

module nostr.bot.core {
    requires static lombok;
    requires org.glassfish.expressly;
    requires jakarta.el;
    requires org.hibernate.validator.cdi;
    requires org.hibernate.validator;
    requires jakarta.validation;
    requires org.jboss.logging;
    requires com.fasterxml.classmate;
    requires nostr.id;
    requires nostr.base;
    requires com.fasterxml.jackson.databind;
    requires com.fasterxml.jackson.annotation;
    requires com.fasterxml.jackson.core;
    requires nostr.crypto;
    requires nostr.event;
    requires nostr.util;
    requires nostr.ws;
    requires org.eclipse.jetty.websocket.jetty.client;
    requires org.eclipse.jetty.websocket.jetty.api;
    requires org.eclipse.jetty.websocket.jetty.common;
    requires org.eclipse.jetty.websocket.core.common;
    requires org.eclipse.jetty.websocket.core.client;
    requires org.eclipse.jetty.client;
    requires org.eclipse.jetty.http;
    requires org.eclipse.jetty.util;
    requires org.eclipse.jetty.io;
    requires org.slf4j;
    requires org.eclipse.jetty.http2.client;
    requires org.eclipse.jetty.http2.common;
    requires org.eclipse.jetty.http2.hpack;
    requires org.eclipse.jetty.alpn.client;
    requires org.eclipse.jetty.http2.http.client.transport;
    requires org.eclipse.jetty.alpn.java.client;
    requires org.bouncycastle.provider;
    requires nostr.bot.util;
    requires java.logging;
    requires java.desktop;
    
    uses nostr.bot.core.command.ICommand;
        
    exports nostr.bot.core;
    exports nostr.bot.core.command;
    exports nostr.bot.core.command.annotation;
}
