package de.fabilucius.advancedperks.commands;

import de.fabilucius.advancedperks.AdvancedPerks;
import de.fabilucius.advancedperks.commands.subcommands.BuySubCommand;
import de.fabilucius.advancedperks.commands.subcommands.OpenSubCommand;
import de.fabilucius.advancedperks.commands.subcommands.ReloadSubCommand;
import de.fabilucius.advancedperks.commands.subcommands.ToggleSubCommand;
import de.fabilucius.advancedperks.gui.PerkGuiWindow;
import de.fabilucius.sympel.command.command.AbstractCommand;
import de.fabilucius.sympel.command.command.AbstractSubCommand;
import de.fabilucius.sympel.command.metadata.SubCommands;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.stream.Collectors;

@SubCommands({OpenSubCommand.class, ToggleSubCommand.class, BuySubCommand.class, ReloadSubCommand.class})
public class PerksCommand extends AbstractCommand {

    private PerksCommand() {
        super("perks");
    }

    public static PerksCommand registerCommand() {
        PerksCommand perksCommand = new PerksCommand();
        perksCommand.setNoPermissionMessage(AdvancedPerks.getMessageConfiguration().getMessage("Command.No-Permission"));
        return perksCommand;
    }

    @Override
    public void handleCommandExecute(CommandSender commandSender, String... arguments) {
        if (commandSender instanceof Player) {
            if (arguments.length == 0) {
                Player player = (Player) commandSender;
                AdvancedPerks.getGuiManager().openGui(player, new PerkGuiWindow(player));
            }
        }
    }

    @Override
    public List<String> handleTabComplete(CommandSender commandSender, String... arguments) {
        if (arguments.length == 0 || arguments[0].length() == 1) {
            return this.getSubCommands().stream()
                    .map(AbstractSubCommand::getIdentifier)
                    .collect(Collectors.toList());
        } else {
            return this.getSubCommands().stream()
                    .map(AbstractSubCommand::getIdentifier)
                    .filter(subCommands -> subCommands.toLowerCase().startsWith(arguments[0].toLowerCase()))
                    .collect(Collectors.toList());
        }
    }
}
