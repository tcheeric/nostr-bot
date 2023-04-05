/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package nostr.bot.core;

import java.util.Optional;
import nostr.bot.core.command.ICommand;

/**
 *
 * @author eric
 */
public interface IBot {

    public void registerCommand(ICommand command);

    public Optional<ICommand> getCommand(String id);    
}
