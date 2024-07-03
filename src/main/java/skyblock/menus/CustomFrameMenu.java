package skyblock.menus;

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
import skyblock.api.CustomFrameMaterials;
import skyblock.api.Island;
import skyblock.api.IslandManager;
import skyblock.api.IslandMaterials;
import skyblock.main.Main;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CustomFrameMenu implements Listener {
    private static final String INVENTORY_NAME = "Island Custom Frame";
    private static final int INVENTORY_ROWS = 3;

    CustomFrameMaterials customFrameMaterials = Main.instance.customFrameMaterials;

    public void openMenu(Player player) {


        IslandManager islandManager = Main.islandManager;
        Island islands = islandManager.island.get(player.getUniqueId());

        Inventory inventory = Bukkit.createInventory(null, INVENTORY_ROWS * 9, INVENTORY_NAME);

        for (int i = 0; i <= customFrameMaterials.materials.size() - 1; i++) {

            ItemStack item = new ItemStack(customFrameMaterials.materials.get(i), 1);
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName("§c" + customFrameMaterials.materials.get(i).toString());
            List<String> lore = new ArrayList<>();
            lore.add("");
            lore.add("§bClick to activate");
            meta.setLore(lore);
            item.setItemMeta(meta);
            inventory.setItem(i + 10, item);
        }

        player.openInventory(inventory);
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
                island.setCustomFrameMaterial(Objects.requireNonNull(event.getCurrentItem()).getType());
                player.closeInventory();
            }
        }
    }
}
