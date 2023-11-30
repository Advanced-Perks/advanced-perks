package de.fabilucius.advancedperks.compatabilities.luckperms;

import com.google.inject.Inject;
import de.fabilucius.advancedperks.AdvancedPerks;
import de.fabilucius.advancedperks.data.PerkData;
import de.fabilucius.advancedperks.data.PerkDataRepository;
import de.fabilucius.advancedperks.data.state.PerkStateController;
import de.fabilucius.advancedperks.perk.Perk;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.event.node.NodeRemoveEvent;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.NodeType;
import net.luckperms.api.node.types.PermissionNode;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

import java.util.List;

public class LuckPermsCompatibility {

    @Inject
    public LuckPermsCompatibility(AdvancedPerks advancedPerks, PerkDataRepository perkDataRepository, PerkStateController perkStateController) {
        RegisteredServiceProvider<LuckPerms> luckPermsRegisteredServiceProvider = Bukkit.getServicesManager().getRegistration(LuckPerms.class);
        if (luckPermsRegisteredServiceProvider == null) {
            //TODO throw an error
            return;
        }
        LuckPerms luckPerms = luckPermsRegisteredServiceProvider.getProvider();
        luckPerms.getEventBus().subscribe(advancedPerks, NodeRemoveEvent.class, nodeRemoveEvent -> {
            if (nodeRemoveEvent.isUser() && nodeRemoveEvent.getNode().getType().equals(NodeType.PERMISSION)) {
                User user = (User) nodeRemoveEvent.getTarget();
                Player player = Bukkit.getPlayer(user.getUniqueId());
                if (player == null) {
                    return;
                }
                PermissionNode permissionNode = (PermissionNode) nodeRemoveEvent.getNode();
                PerkData perkData = perkDataRepository.getPerkDataByPlayer(player);
                List<Perk> toDisable = perkData.getEnabledPerks().stream()
                        .filter(perk -> perk.getPermission().isPresent() && perk.getPermission().get().equals(permissionNode.getPermission()))
                        .toList();
                perkStateController.disablePerks(player, toDisable);
            }
        });
    }

}
