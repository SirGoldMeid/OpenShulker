package mrgoldmeid.openshulker;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.BlockState;
import org.bukkit.block.ShulkerBox;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.logging.Level;

public class Main extends JavaPlugin implements Listener {
    private BukkitTask timeTask = null;
    private int maxSlots;
    private String permission;

    @Override
    public void onEnable() {
        loadConfigValues();
        Bukkit.getPluginManager().registerEvents(this, this);
        this.timeTask = new BukkitRunnable() {
            @Override
            public void run() {
                CheckHandShulker();
            }
        }.runTaskTimer(this, 2L, 2L);
    }

    @Override
    public void onDisable() {
        if (timeTask != null) {
            timeTask.cancel();
        }
    }

    private void loadConfigValues() {
        saveDefaultConfig();
        FileConfiguration config = getConfig();
        maxSlots = config.getInt("max_slots", 27);
        permission = config.getString("permission", "openshulker.slot");
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void GameClick(PlayerInteractEvent e) {
        if (e.getHand() == EquipmentSlot.HAND && !e.getPlayer().isSneaking()) {
            ItemStack itemInHand = e.getPlayer().getEquipment().getItemInMainHand();
            if (isShulkerBox(itemInHand) && e.getPlayer().hasPermission("openshulker.use")) {
                e.setCancelled(true);
                if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
                    BlockState blockState = ((BlockStateMeta) itemInHand.getItemMeta()).getBlockState();
                    if (blockState instanceof ShulkerBox) {
                        ShulkerBox shulker = (ShulkerBox) blockState;
                        Inventory shulkerInventory = Bukkit.createInventory(null, maxSlots, "Box");
                        shulkerInventory.setContents(shulker.getInventory().getContents());
                        e.getPlayer().openInventory(shulkerInventory);
                    }
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.LOW)
    public void InvClick(InventoryClickEvent e) {
        if (e.getInventory().getType() == InventoryType.CHEST && e.getView().getTitle().equals("Box")) {
            ItemStack clickedItem = e.getCurrentItem();
            ItemStack itemInHand = e.getWhoClicked().getEquipment().getItemInMainHand();

            if (isShulkerBox(clickedItem) && isShulkerBox(itemInHand)) {
                e.setCancelled(true);
                ((Player) e.getWhoClicked()).updateInventory();
                return;
            }

            BlockStateMeta im = (BlockStateMeta) itemInHand.getItemMeta();
            ShulkerBox shulker = (ShulkerBox) im.getBlockState();
            shulker.getInventory().setContents(e.getInventory().getContents());
            im.setBlockState((BlockState) shulker);
            itemInHand.setItemMeta((ItemMeta) im);
        }
    }

    @EventHandler(priority = EventPriority.LOW)
    public void InvClose(InventoryCloseEvent e) {
        if (e.getView().getTitle().equals("Box") && e.getPlayer().getEquipment().getItemInMainHand() != null &&
                isShulkerBox(e.getPlayer().getEquipment().getItemInMainHand())) {
            BlockStateMeta im = (BlockStateMeta) e.getPlayer().getEquipment().getItemInMainHand().getItemMeta();
            ShulkerBox shulker = (ShulkerBox) im.getBlockState();
            shulker.getInventory().setContents(e.getInventory().getContents());
            im.setBlockState((BlockState) shulker);
            e.getPlayer().getEquipment().getItemInMainHand().setItemMeta((ItemMeta) im);
        }
    }

    private boolean isShulkerBox(ItemStack item) {
        if (item == null) {
            return false;
        }

        Material itemType = item.getType();
        return itemType.name().endsWith("_SHULKER_BOX");
    }

    public void CheckHandShulker() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.getOpenInventory().getType() == InventoryType.CHEST && player.getOpenInventory().getTitle().equals("Box")) {
                if (!isShulkerBox(player.getEquipment().getItemInMainHand())) {
                    player.closeInventory();
                } else {
                    BlockStateMeta im = (BlockStateMeta) player.getEquipment().getItemInMainHand().getItemMeta();
                    ShulkerBox shulker = (ShulkerBox) im.getBlockState();
                    shulker.getInventory().setContents(player.getOpenInventory().getTopInventory().getContents());
                    im.setBlockState((BlockState) shulker);
                    player.getEquipment().getItemInMainHand().setItemMeta((ItemMeta) im);
                }
            }
        }
    }
}