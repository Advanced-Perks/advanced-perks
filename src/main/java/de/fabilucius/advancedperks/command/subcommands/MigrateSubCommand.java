package de.fabilucius.advancedperks.command.subcommands;

import com.google.inject.Inject;
import de.fabilucius.advancedperks.configuration.ConfigurationLoader;
import de.fabilucius.advancedperks.configuration.exception.ConfigurationInitializationException;
import de.fabilucius.advancedperks.core.command.AbstractSubCommand;
import de.fabilucius.advancedperks.core.command.annotation.CommandIdentifier;
import de.fabilucius.advancedperks.core.command.annotation.Permission;
import de.fabilucius.advancedperks.data.PerkDataRepository;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.List;

@CommandIdentifier("migrate")
@Permission("advancedperks.command.migrate")
public class MigrateSubCommand extends AbstractSubCommand {

    @Inject
    private PerkDataRepository perkDataRepository;

    @Inject
    protected MigrateSubCommand(ConfigurationLoader configurationLoader) throws ConfigurationInitializationException {
        super(configurationLoader);
    }

    @Override
    public void executeCommand(CommandSender commandSender, String... arguments) {
        if (arguments.length > 0 && arguments[0].equals("CONFIRM")) {
            if (this.perkDataRepository.migratePerkData()) {
                commandSender.sendMessage(ChatColor.GREEN + "Perk data migration was successful.");
            } else {
                commandSender.sendMessage(ChatColor.RED + "Perk data migration failed check the console and/or contact the plugins author.");
            }
        } else {
            commandSender.sendMessage(ChatColor.RED + "You need to type /perks migrate CONFIRM to start the perk database migration.");
        }
    }

    @Override
    public List<String> tabComplete(CommandSender commandSender, String... arguments) {
        return null;
    }
}
