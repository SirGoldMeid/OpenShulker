package mrgoldmeid.openshulker;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ShulkerCommand implements CommandExecutor {
    private final ShulkerBoxManager shulkerBoxManager;
    private final PermissionManager permissionManager;

    public ShulkerCommand(ShulkerBoxManager shulkerBoxManager, PermissionManager permissionManager) {
        this.shulkerBoxManager = shulkerBoxManager;
        this.permissionManager = permissionManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Este comando solo puede ser ejecutado por un jugador.");
            return true;
        }

        Player player = (Player) sender;
        if (!permissionManager.hasPermission(player, "openshulker.command")) {
            player.sendMessage("¡No tienes permiso para ejecutar este comando!");
            return true;
        }

        // Aquí puedes manejar cualquier lógica adicional para el comando

        return true;
    }
}
