/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/module-info.java to edit this template
 */

module nostr.bot.command.handler.provider {
    requires nostr.base;
    requires static lombok;
    requires java.logging;
    requires com.fasterxml.jackson.databind;
    requires com.fasterxml.jackson.annotation;
    requires com.fasterxml.jackson.core;
    requires nostr.util;
    requires nostr.crypto;
    requires nostr.id;
    requires nostr.event;
    requires nostr.ws;
    requires nostr.bot.core;
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
    requires nostr.ws.response.handler.provider;
    requires org.bouncycastle.provider;
    requires nostr.ws.handler;
    requires nostr.ws.request.handler.provider;
    requires org.glassfish.expressly;
    requires jakarta.el;
    requires org.hibernate.validator.cdi;
    requires org.hibernate.validator;
    requires jakarta.validation;
    requires org.jboss.logging;
    requires com.fasterxml.classmate;
    requires nostr.bot.util;
    
    exports nostr.bot.command.handler.provider;
    
    provides nostr.ws.handler.command.spi.ICommandHandler with nostr.bot.command.handler.provider.BotCommandHandler;

}
