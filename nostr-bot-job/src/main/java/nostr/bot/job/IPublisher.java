/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package nostr.bot.job;

import nostr.id.Client;

/**
 *
 * @author eric
 */
public interface IPublisher {

    public void publish(Client client) throws Exception;
}
