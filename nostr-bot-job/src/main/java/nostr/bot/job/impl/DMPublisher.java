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
public class DMPublisher extends AbstractPublisher {

    public DMPublisher() throws IOException, NostrException {
        super(Kind.ENCRYPTED_DIRECT_MESSAGE, Identity.getInstance().getPublicKey());
    }        
}
