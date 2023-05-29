/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/module-info.java to edit this template
 */

module nostr.bot.client {
    requires nostr.bot.job;
    requires nostr.event;
    requires static lombok;
    requires java.logging;
    requires nostr.id;
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
    requires org.eclipse.jetty.http2.client;
    requires org.eclipse.jetty.http2.common;
    requires org.eclipse.jetty.http2.hpack;
    requires org.eclipse.jetty.alpn.client;
    requires org.eclipse.jetty.http2.http.client.transport;
    requires org.eclipse.jetty.alpn.java.client;
    requires org.bouncycastle.provider;
    requires nostr.bot.core;
    requires org.glassfish.expressly;
    requires jakarta.el;
    requires org.hibernate.validator.cdi;
    requires org.hibernate.validator;
    requires jakarta.validation;
    requires org.jboss.logging;
    requires com.fasterxml.classmate;
    requires nostr.bot.util;
    requires quartz;
    requires c3p0;
    requires mchange.commons.java;
    requires HikariCP.java7;
    requires slf4j.api;
    requires nostr.ws.handler;
    requires nostr.base;
    requires com.fasterxml.jackson.databind;
    requires com.fasterxml.jackson.annotation;
    requires com.fasterxml.jackson.core;
    requires nostr.crypto;
    requires nostr.util;
    
    exports nostr.bot.client;
}
