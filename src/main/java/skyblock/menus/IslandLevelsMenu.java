package skyblock.menus;

import com.sun.org.apache.bcel.internal.generic.ANEWARRAY;
import de.backpack.apfloat.Apfloat;
import de.backpack.listener.EconomyAPI;
import de.backpack.listener.UnlimitedNumber;
import org.bukkit.Bukkit;
import org.bukkit.Material;
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

import java.util.ArrayList;
import java.util.List;

public class IslandLevelsMenu implements Listener{


    private static final String INVENTORY_NAME = "Island Levels";
    private static final int INVENTORY_ROWS = 6;
    private static final int ISLAND_START_SIZE = Island.ISLAND_START_SIZE;

    private static final char[] alphabet = "abcdefghijklmnopqrstuvwxyz".toCharArray();

    private static final int NUM_LEVELS = alphabet.length;

    IslandMaterials islandMaterials = Main.islandMaterials;
    public void openMenu(Player player) {



        IslandManager islandManager = Main.islandManager;
        Island islands = islandManager.island.get(player.getUniqueId());

        Inventory inventory = Bukkit.createInventory(null, INVENTORY_ROWS * 9, INVENTORY_NAME);

        for (int i = 0; i <= NUM_LEVELS-1; i++) {

            ItemStack item = new ItemStack(Material.BARRIER, i+1);
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName("§cMine World " + alphabet[i]);
            List<String> lore = new ArrayList<>();

            int island_size = ISLAND_START_SIZE+(i*6);


            EconomyAPI economyAPI = de.backpack.main.Main.economyAPI;
            Apfloat cost = economyAPI.calcWorldCost(player, i);
            UnlimitedNumber unlimitedNumber = new UnlimitedNumber(String.valueOf(cost));


            lore.add("");
            lore.add("§a§lInformation");
            lore.add("");
            lore.add("§bStatus: §c§lLock");
            lore.add("§bIsland size: §f" + island_size + "x" + island_size);
            lore.add("§bRequired: " + unlimitedNumber.format());
            lore.add("");
            lore.add("§b§lClick to unlock");
            meta.setLore(lore);
            item.setItemMeta(meta);
            inventory.setItem(i, item);
        }
        for (int i = 0; i <= islands.getISLAND_WORLD_LEVEL(); i++) {
            ItemStack item = new ItemStack(islandMaterials.islandMaterials.get(i), i+1);
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName("§aMine World " + alphabet[i]);
            List<String> lore = new ArrayList<>();

            int island_size = ISLAND_START_SIZE+i*6;

            lore.add("");
            lore.add("§a§lInformation");
            lore.add("");
            lore.add("§bStatus: §a§lUnlock");
            lore.add("§bIsland size: §f" + island_size + "x" + island_size);
            meta.setLore(lore);
            item.setItemMeta(meta);
            inventory.setItem(i, item);
        }
        player.openInventory(inventory);
    }


    @EventHandler

    public void onInventoryClick(InventoryClickEvent event) {
        Inventory inventory = event.getInventory();
        if (event.getView().getTitle().equals(INVENTORY_NAME)) {
            event.setCancelled(true);
        }
    }
}
