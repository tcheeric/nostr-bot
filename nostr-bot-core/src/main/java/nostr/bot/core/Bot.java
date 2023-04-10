/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package nostr.bot.core;

import java.io.IOException;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Level;

import lombok.Data;
import lombok.NonNull;
import lombok.extern.java.Log;

import nostr.bot.core.command.ICommand;
import nostr.bot.core.command.annotation.Command;
import nostr.bot.util.CommandsConfiguration;

/**
 *
 * @author eric
 */
@Data
@Log
public class Bot implements IBot {

    private final Set<ICommand> commands;
    private final CommandsConfiguration commandsConfiguration;

    public Bot(String configFileName) {
        this.commands = new HashSet<>();
        try {
            this.commandsConfiguration = new CommandsConfiguration(configFileName, "command");
            this.registerCommands();
        } catch (IOException ex) {
            log.log(Level.SEVERE, null, ex);
            throw new RuntimeException(ex);
        }
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
        this.commands.add(command);
    }

    public ICommand getStartCommand() {
        var optCommand = commands.stream().filter(c -> c.getSources().length == 0).findFirst();
        if (optCommand.isPresent()) {
            return optCommand.get();
        }
        throw new RuntimeException("Start command not found.");
    }

    private void registerCommands() throws IOException {

        String[] commandArr = commandsConfiguration.getAllCommands().split(",");

        Arrays.asList(commandArr)
                .stream()
                .forEach(c -> {
                    try {
                        log.log(Level.INFO, String.format("Registering command %s...", c));
                        final ICommand command = (ICommand) Class.forName(c).newInstance();
                        if (!commands.contains(command)) {
                            commands.add(command);
                            log.log(Level.INFO, String.format("Command %s registered.", c));
                        } else {
                            log.log(Level.WARNING, String.format("The command %s was already registered. Skipping...", c));
                        }
                    } catch (ClassNotFoundException | InstantiationException | IllegalAccessException ex) {
                        log.log(Level.SEVERE, null, ex);
                    }
                });
    }
}
