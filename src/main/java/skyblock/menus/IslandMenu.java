package skyblock.menus;


import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Skull;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import skyblock.api.Island;
import skyblock.api.IslandManager;
import skyblock.api.IslandMaterials;
import skyblock.main.Main;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class IslandMenu implements Listener {


    private static final String INVENTORY_NAME = "Mine Menu";
    private static final int INVENTORY_ROWS = 6;
    private static final int NUM_LEVELS = Island.ISLAND_LEVEL_MAX;
    private static final int ISLAND_START_SIZE = Island.ISLAND_START_SIZE;

    IslandMaterials islandMaterials = Main.islandMaterials;
    public void openMenu(Player player) {



        IslandManager islandManager = Main.islandManager;
        Island islands = islandManager.island.get(player.getUniqueId());

        Inventory inventory = Bukkit.createInventory(null, INVENTORY_ROWS * 9, INVENTORY_NAME);

        for (int i = 0; i < INVENTORY_ROWS*9; i++) {
            ItemStack item = new ItemStack(Material.GRAY_STAINED_GLASS_PANE, 1);
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(" ");
            item.setItemMeta(meta);
            inventory.setItem(i, item);
        }


        ItemStack item = new ItemStack(Material.COMPASS, 1);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("§bTeleport to mine");
        List<String> lore = new ArrayList<>();
        lore.add("");
        lore.add("§7Click to teleport to your personal mine");
        lore.add("§7and mine blocks for tokens");
        lore.add("");
        lore.add("§bclick to teleport!");
        meta.setLore(lore);
        item.setItemMeta(meta);


        ItemStack item2 = new ItemStack(Material.PAINTING, 1);
        ItemMeta meta2 = item2.getItemMeta();
        meta2.setDisplayName("§bMine Themes");
        List<String> lore2 = new ArrayList<>();
        lore2.add("");
        lore2.add("§7Click to change your personal mine skin");
        lore2.add("");
        lore2.add("§bclick to open!");
        meta2.setLore(lore2);
        item2.setItemMeta(meta2);

        ItemStack item3 = new ItemStack(Material.OAK_HANGING_SIGN, 1);
        ItemMeta meta3 = item3.getItemMeta();
        meta3.setDisplayName("§bMine Settings");
        List<String> lore3 = new ArrayList<>();
        lore3.add("");
        lore3.add("§7Click to open the settings");
        lore3.add("§7for your personal mine");
        lore3.add("");
        lore3.add("§bclick to open!");
        meta3.setLore(lore3);
        item3.setItemMeta(meta3);



        inventory.setItem(13, item);
        inventory.setItem(31, item2);
        inventory.setItem(33, item3);


        player.openInventory(inventory);
    }

    @EventHandler

    public void onInventoryClick(InventoryClickEvent event) {

        IslandManager islandManager = Main.islandManager;

        Player player = (Player) event.getWhoClicked();
        Island island = islandManager.island.get(player.getUniqueId());

        Inventory inventory = event.getInventory();
        if(event.getView().getTitle().equalsIgnoreCase(INVENTORY_NAME)) {
            if (event.getCurrentItem() != null && event.getCurrentItem().getItemMeta() != null) {
                event.setCancelled(true);
                if(event.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§bTeleport to mine")){
                    player.teleport(island.getIslandLocation());
                    player.closeInventory();
                }
                if(event.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§bMine World")){


                }
                if(event.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§bMine Themes")){
                    event.setCancelled(true);
                }
                if(event.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§bMine Settings")){
                    event.setCancelled(true);
                }
            }
        }
    }
}
