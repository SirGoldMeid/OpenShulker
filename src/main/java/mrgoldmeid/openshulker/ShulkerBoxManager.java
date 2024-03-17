package mrgoldmeid.openshulker;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.BlockState;
import org.bukkit.block.ShulkerBox;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;

public class ShulkerBoxManager implements Listener {
    private final Main plugin;

    public ShulkerBoxManager(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            ItemStack itemInHand = player.getEquipment().getItemInMainHand();
            if (plugin.getPermissionManager().hasPermission(player, "openshulker.use") && isShulkerBox(itemInHand)) {
                openShulkerBox(player, itemInHand);
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getView().getTitle().equals("Box")) {
            event.setCancelled(true);
        }
    }

    private boolean isShulkerBox(ItemStack item) {
        if (item == null) {
            return false;
        }

        Material itemType = item.getType();
        return itemType.name().endsWith("_SHULKER_BOX");
    }

    private void openShulkerBox(Player player, ItemStack shulkerBoxItem) {
        ShulkerBox shulker = (ShulkerBox) ((BlockStateMeta) shulkerBoxItem.getItemMeta()).getBlockState();
        Inventory shulkerInventory = Bukkit.createInventory(null, 27, "Box");
        shulkerInventory.setContents(shulker.getInventory().getContents());
        player.openInventory(shulkerInventory);
    }
}
