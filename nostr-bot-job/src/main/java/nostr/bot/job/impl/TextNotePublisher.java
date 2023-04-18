/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package nostr.bot.job.impl;

import nostr.base.PublicKey;
import nostr.event.Kind;

/**
 *
 * @author eric
 */
public class TextNotePublisher extends AbstractPublisher {

    public TextNotePublisher(PublicKey publicKey) {
        super(Kind.TEXT_NOTE, publicKey);
    }    
}
