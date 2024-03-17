package mrgoldmeid.openshulker;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
    private ShulkerBoxManager shulkerBoxManager;
    private PermissionManager permissionManager;


    @Override
    public void onEnable() {
        // Inicialización de los managers
        shulkerBoxManager = new ShulkerBoxManager(this);
        permissionManager = new PermissionManager(this);

        // Registro de eventos y comando
        Bukkit.getPluginManager().registerEvents(shulkerBoxManager, this);
        getCommand("openshulker").setExecutor(new ShulkerCommand(shulkerBoxManager, permissionManager));

        // Mensaje de inicio
        getLogger().info("¡El plugin OpenShulker se ha iniciado correctamente!");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    // Getters para acceder a los managers
    public ShulkerBoxManager getShulkerBoxManager() {
        return shulkerBoxManager;
    }

    public PermissionManager getPermissionManager() {
        return permissionManager;
    }
}