/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package nostr.bot.job.impl;

import lombok.extern.java.Log;

/**
 *
 * @author eric
 */
@Log
public class TextNoteSubscriber extends AbstractSubscriber {

    public TextNoteSubscriber(String subscriptionId, String jsonEvent) {
        super(subscriptionId, jsonEvent);
    }

    @Override
    protected String getContent() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
}
