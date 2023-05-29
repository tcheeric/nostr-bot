/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package nostr.bot.job.impl;

import java.io.IOException;
import nostr.event.Kind;
import nostr.id.Identity;
import nostr.util.NostrException;

/**
 *
 * @author eric
 */
public class TextNotePublisher extends AbstractPublisher {

    public TextNotePublisher() throws IOException, NostrException {
        super(Kind.TEXT_NOTE, Identity.getInstance().getPublicKey());
    }    
}
