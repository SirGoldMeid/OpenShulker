package mrgoldmeid.openshulker;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
    private ShulkerBoxManager shulkerBoxManager;
    private PermissionManager permissionManager;

    @Override
    public void onEnable() {
        shulkerBoxManager = new ShulkerBoxManager(this);
        permissionManager = new PermissionManager(this);

        Bukkit.getPluginManager().registerEvents(shulkerBoxManager, this);
        getCommand("openshulker").setExecutor(new ShulkerCommand(shulkerBoxManager, permissionManager));
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public ShulkerBoxManager getShulkerBoxManager() {
        return shulkerBoxManager;
    }

    public PermissionManager getPermissionManager() {
        return permissionManager;
    }
}