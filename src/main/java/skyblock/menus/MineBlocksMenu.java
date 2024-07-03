package skyblock.menus;

import com.sun.tools.javac.jvm.Items;
import de.backpack.apfloat.Apfloat;
import de.backpack.apfloat.ApfloatMath;
import de.backpack.listener.EconomyAPI;
import de.backpack.listener.UnlimitedNumber;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;
import skyblock.api.Island;
import skyblock.api.IslandManager;
import skyblock.api.IslandMaterials;
import skyblock.api.MaterialValues;
import skyblock.main.Main;

import java.util.ArrayList;
import java.util.List;

public class MineBlocksMenu implements Listener {


    private static final String INVENTORY_NAME = "Mine Blocks";
    private static final int INVENTORY_ROWS = 6;
    private static final int ITEMS_PER_PAGE = 45;

    private int currentPage = 1;

    IslandMaterials islandMaterials = Main.islandMaterials;

    public void openMenu(Player player) {
        Inventory inventory = Bukkit.createInventory(null, INVENTORY_ROWS * 9, INVENTORY_NAME);

        loadPage(player, inventory, currentPage);

        player.openInventory(inventory);
    }

    private void loadPage(Player player, Inventory inventory, int page) {
        int startIndex = (page - 1) * ITEMS_PER_PAGE;
        int endIndex = Math.min(startIndex + ITEMS_PER_PAGE - 1, islandMaterials.islandMaterials.size() - 1);

        for (int i = startIndex; i <= endIndex; i++) {
            Material material = islandMaterials.islandMaterials.get(i);
            if (material != null) {
                ItemStack item = new ItemStack(islandMaterials.islandMaterials.get(i), 1);
                ItemMeta meta = getMeta(player, item, i);
                item.setItemMeta(meta);
                inventory.setItem(i - startIndex, item);
            }
        }

        // Add next page item if there are more items
        if (endIndex < islandMaterials.islandMaterials.size() - 1) {
            inventory.setItem(ITEMS_PER_PAGE + 8, nextPageItem());
        }
        if(currentPage>1){
            inventory.setItem(ITEMS_PER_PAGE, previousPageItem());
        }
        inventory.setItem(49, infoItem(player));
    }

    public ItemStack infoItem(Player player){
        ItemStack item = new ItemStack(Material.CAULDRON);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("§e§lInfo");
        List<String> lore = new ArrayList<>();

        IslandManager islandManager = Main.islandManager;
        Island island = islandManager.island.get(player.getUniqueId());


        for(Material m : island.getMine_Materials().keySet()) {

            if(m.equals(Material.RAW_GOLD_BLOCK)){
                lore.add("§fLuckyBlock §7- " + island.getMineMaterialPercent(m) * 100 + "%");
            } else {
                lore.add("§f" + m + " §7- " + island.getMineMaterialPercent(m) * 100 + "%");
            }
        }
        lore.add("");

        UnlimitedNumber unlimitedNumber = new UnlimitedNumber(String.valueOf(island.calcBaseValueAverage()));

        lore.add("Average: " + unlimitedNumber.format());
        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }

    public ItemStack nextPageItem() {
        ItemStack item = new ItemStack(Material.ARROW);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("§cNext Page");
        List<String> lore = new ArrayList<>();
        lore.add("");
        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }
    public ItemStack previousPageItem() {
        ItemStack item = new ItemStack(Material.ARROW);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("§cPrevious Page");
        List<String> lore = new ArrayList<>();
        lore.add("");
        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }

    public ItemMeta getMeta(Player player, ItemStack item, int i){

        ItemMeta meta = item.getItemMeta();

        IslandManager islandManager = Main.islandManager;
        Island islands = islandManager.island.get(player.getUniqueId());

        MaterialValues materialValues = new MaterialValues();

        String name = item.getType().toString();
        if(item.getType().equals(Material.RAW_GOLD_BLOCK)) {
            name = "Lucky Block";
        }

        meta.setDisplayName("§6§l" + name);



        UnlimitedNumber unlimitedNumber1 = new UnlimitedNumber(String.valueOf(islands.calcBaseBlockValue(item.getType())));

        List<String> lore = new ArrayList<>();
        EconomyAPI economyAPI = de.backpack.main.Main.economyAPI;
        Apfloat cost = islands.calcMineBlockCost(item.getType(), 1);
        UnlimitedNumber unlimitedNumber = new UnlimitedNumber(String.valueOf(cost));
        lore.add("");
        //if(islands.canUpgrade(item.getType())){
            lore.add("§a§lInformation");
            lore.add("§e§l| §fBase Value: §e$" + unlimitedNumber1.format());
            lore.add("§e§l| §fEdit cost: §e" + unlimitedNumber.format() + "$");
            lore.add("§e§l| §fChance: §e" + String.format("%.1f", islands.calcDisplayBlockChance(item.getType()) * 100) + "§e%");
            lore.add("");
            lore.add("§e§lBlock Upgrading");
            lore.add("§fLeft-Click to add §b1 §flevel");
            lore.add("§fRight-Click to remove §c1 §flevel");
            lore.add("§fLeft-Shift-Click to add §b10 §flevel");
            lore.add("§fRight-Shift-Click to remove §c10 §flevel");
        //}
        if(!islands.canUpgrade(item.getType())){
            lore.add("§cUnlock at Mine level: " + i*10);
        }


        meta.setLore(lore);

        return meta;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Inventory inventory = event.getInventory();
        Player player = (Player) event.getWhoClicked();
        IslandManager islandManager = Main.islandManager;
        Island island = islandManager.island.get(player.getUniqueId());

        EconomyAPI economyAPI = de.backpack.main.Main.economyAPI;

        if (event.getView().getTitle().equals(INVENTORY_NAME)) {
            event.setCancelled(true);

            if (event.getCurrentItem() != null && event.getRawSlot() < inventory.getSize()) {
                ItemStack clickedItem = event.getCurrentItem();
                Inventory customInventory = player.getOpenInventory().getTopInventory();

                if(customInventory != null && customInventory.equals(inventory) && clickedItem.getType().equals(Material.ARROW)) {
                    if (clickedItem.getItemMeta().getDisplayName().equalsIgnoreCase("§cNext Page")) {
                        currentPage++; // Move to next page
                    } else if (clickedItem.getItemMeta().getDisplayName().equalsIgnoreCase("§cPrevious Page")) {
                        currentPage = Math.max(1, currentPage - 1); // Ensure currentPage doesn't go below 1
                    }
                    Inventory newInventory = Bukkit.createInventory(null, INVENTORY_ROWS * 9, INVENTORY_NAME);
                    loadPage(player, newInventory, currentPage);
                    player.openInventory(newInventory);
                    return;
                }


                if (customInventory != null && customInventory.equals(inventory) && !clickedItem.getType().equals(Material.RAW_GOLD_BLOCK) && !clickedItem.getType().equals(Material.CAULDRON)) {

                    Apfloat cost = new Apfloat(0);

                    if (island.canUpgrade(clickedItem.getType())) {

                        if (event.getClick().equals(ClickType.LEFT)) {
                            if (!island.isMaterialBlockChanceMax(clickedItem.getType())) {
                                cost = island.calcMineBlockCost(clickedItem.getType(), 1);
                                if (cost.compareTo(economyAPI.getCashNumberBalance(player)) <= 0) {
                                    economyAPI.removeCashNumberBalance(player, cost);
                                    island.addMineMaterialPercent(clickedItem.getType(), 1);
                                }
                            }
                        } else if (event.getClick().equals(ClickType.SHIFT_LEFT)) {
                            if (!island.isMaterialBlockChanceMax(clickedItem.getType())) {
                                cost = island.calcMineBlockCost(clickedItem.getType(), 10);
                                if (cost.compareTo(economyAPI.getCashNumberBalance(player)) <= 0) {
                                    economyAPI.removeCashNumberBalance(player, cost);
                                    island.addMineMaterialPercent(clickedItem.getType(), 10);

                                }
                            }
                        } else if (event.getClick().equals(ClickType.RIGHT)) {
                            island.removeMineMaterialPercent(clickedItem.getType(), 1);
                        } else if (event.getClick().equals(ClickType.SHIFT_RIGHT)) {
                            island.removeMineMaterialPercent(clickedItem.getType(), 10);
                        }

                        // Aktualisieren der Meta-Daten des geklickten Gegenstands
                        ItemMeta updatedMeta = getMeta(player, clickedItem, event.getRawSlot());
                        clickedItem.setItemMeta(updatedMeta);

                        // Aktualisieren des Inventars des Spielers
                        customInventory.setItem(event.getSlot(), clickedItem);

                        inventory.setItem(49, infoItem(player));
                        player.setMetadata("mine_material_changes", new FixedMetadataValue(Main.instance, true));
                    }
                }
            }
        }
    }

    @EventHandler

    public void onCloseInventory(InventoryCloseEvent event){

        Inventory inventory = event.getInventory();

        if(event.getView().getTitle().equalsIgnoreCase(INVENTORY_NAME)){

            currentPage=1;

            Player player = (Player) event.getPlayer();

            if (player.hasMetadata("mine_material_changes")) {

                Bukkit.getScheduler().runTaskLater(Main.instance, () -> {

                            IslandManager islandManager = Main.islandManager;
                            Island island = islandManager.island.get(player.getUniqueId());

                            island.resetIsland(player);
                        }, 10L);

                player.removeMetadata("mine_material_changes", Main.instance);
            }

        }
    }

}
