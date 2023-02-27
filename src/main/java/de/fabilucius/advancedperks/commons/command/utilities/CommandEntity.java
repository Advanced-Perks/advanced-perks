package de.fabilucius.advancedperks.commons.command.utilities;

import org.bukkit.command.CommandSender;

import java.util.List;

/**
 * A command entity is any form of command that can be run be it a command itself or one of its subcommands.
 */
public interface CommandEntity {

    /**
     * This method will be forwarded by the bukkit default's onCommand and simplifies the arguments to only include the useful.
     *
     * @param commandSender the instance that issued the command
     * @param arguments     the passed arguments alongside the command
     */
    void handleCommandExecute(CommandSender commandSender, String... arguments);

    /**
     * This method will be forwarded by the bukkit default's onTabComplete and simplifies the arguments to only
     * include the useful.
     *
     * @param commandSender the instance that requested a tab complete
     * @param arguments     the passed arguments the request for a tab complete
     * @return the suggested auto complete words as a list of strings
     */
    List<String> handleTabComplete(CommandSender commandSender, String... arguments);

}
