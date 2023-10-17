package de.fabilucius.advancedperks.compatability.luckperms;

import de.fabilucius.advancedperks.AdvancedPerks;
import de.fabilucius.advancedperks.perks.Perk;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.event.node.NodeRemoveEvent;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.NodeType;
import net.luckperms.api.node.types.PermissionNode;
import org.bukkit.Bukkit;

import java.util.List;
import java.util.Objects;

public class LuckPermsPermissionRemoveCompatability {

    public LuckPermsPermissionRemoveCompatability() {
        LuckPerms luckPerms = Objects.requireNonNull(Bukkit.getServicesManager().getRegistration(LuckPerms.class)).getProvider();
        luckPerms.getEventBus().subscribe(this, NodeRemoveEvent.class, nodeRemoveEvent -> {
            if (nodeRemoveEvent.isUser() && nodeRemoveEvent.getNode().getType() == NodeType.PERMISSION) {
                User user = (User) nodeRemoveEvent.getTarget();
                PermissionNode permissionNode = (PermissionNode) nodeRemoveEvent.getNode();
                AdvancedPerks.getInstance().getPerkDataRepository().getPerkDataByUuid(user.getUniqueId()).ifPresent(perkData -> {
                    List<Perk> perksToDisable = perkData.getActivatedPerks().stream().filter(perk -> perk.getPermission().equals(permissionNode.getPermission())).toList();
                    perksToDisable.forEach(perk -> AdvancedPerks.getInstance().getPerkStateController().disablePerk(perkData.getPlayer(),perk));
                });
            }
        });
    }

}
