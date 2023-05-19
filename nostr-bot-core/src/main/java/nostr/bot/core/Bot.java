/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package nostr.bot.core;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ServiceLoader;
import java.util.logging.Level;

import lombok.Data;
import lombok.NonNull;
import lombok.extern.java.Log;

import nostr.bot.core.command.ICommand;
import nostr.bot.core.command.annotation.Command;

/**
 *
 * @author eric
 */
@Data
@Log
public class Bot implements IBot {

    private final List<ICommand> commands;

    public Bot() {

        this.commands = new ArrayList<>();
        
        ServiceLoader
                .load(ICommand.class)
                .stream()
                .map(p -> p.get())
                .filter(c -> c.getClass().isAnnotationPresent(Command.class))
                .forEach(c -> registerCommand(c));
    }

    @Override
    public Optional<ICommand> getCommand(final @NonNull String id) {
        return commands.stream().filter(c -> {
            Command annotation = c.getClass().getDeclaredAnnotation(Command.class);
            return annotation.id().equalsIgnoreCase(id);
        }).findFirst();
    }

    @Override
    public void registerCommand(ICommand command) {
        log.log(Level.INFO, String.format("Registering command %s...", command));

        if (!commands.contains(command)) {
            this.commands.add(command);
            log.log(Level.INFO, String.format("Command %s registered.", command));
        } else {
            log.log(Level.WARNING, String.format("The command %s was already registered. Skipping...", command));
        }
    }

    public ICommand getStartCommand() {
        var optCommand = commands.stream().filter(c -> c.getSources().length == 0).findFirst();
        if (optCommand.isPresent()) {
            return optCommand.get();
        }
        throw new RuntimeException("Start command not found.");
    }
}
