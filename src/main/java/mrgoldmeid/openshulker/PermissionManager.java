package mrgoldmeid.openshulker;

import org.bukkit.entity.Player;

public class PermissionManager {
    private final Main plugin;

    public PermissionManager(Main plugin) {
        this.plugin = plugin;
    }

    public boolean hasPermission(Player player, String permission) {
        return player.hasPermission(permission);
    }
}
