package skyblock.menus;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import de.backpack.apfloat.Apfloat;
import de.backpack.listener.EconomyAPI;
import de.backpack.listener.UnlimitedNumber;
import jdk.vm.ci.meta.SpeculationLog;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.profile.PlayerProfile;
import skyblock.api.Island;
import skyblock.api.IslandManager;
import skyblock.api.IslandMaterials;
import skyblock.main.Main;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.bukkit.Bukkit.getServer;

public class IslandMemberMenu implements Listener {


    private static final String INVENTORY_NAME = "Island Member";
    private static final int INVENTORY_ROWS = 6;
    public void openMenu(Player player) {



        IslandManager islandManager = Main.islandManager;
        Island islands = islandManager.island.get(player.getUniqueId());

        Inventory inventory = Bukkit.createInventory(null, INVENTORY_ROWS * 9, INVENTORY_NAME);

        for (int i = 0; i < islands.getMembers().size(); i++) {

            OfflinePlayer member = getPlayerByUuid((UUID) islands.getMembers().get(i));

            if(member.isOnline()) {
                ItemStack item = new ItemStack(Material.PLAYER_HEAD, 1);
                SkullMeta meta = (SkullMeta) item.getItemMeta();
                meta.setOwner(member.getName());
                meta.setDisplayName("§a" + member.getName());
                List<String> lore = new ArrayList<>();
                lore.add("");
                lore.add("");
                lore.add("");
                lore.add("");
                meta.setLore(lore);
                item.setItemMeta(meta);
                inventory.setItem(i, item);
            } else {
                ItemStack skull = new ItemStack(Material.PLAYER_HEAD);
                SkullMeta skullMeta = (SkullMeta) skull.getItemMeta();

                // Set the custom texture for the player head
                GameProfile profile = new GameProfile(UUID.randomUUID(), null);
                profile.getProperties().put("textures", new Property("textures", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvM2M3ODNlMjRmZDEzYTc0MzFmNDYzMWMxNGQxNGI2NTZiYzk3YzU4YzQxMTNiMTk1OTRiNTNhYTI2ZDBlNDFiNyJ9fX0="));
                try {
                    Field profileField = skullMeta.getClass().getDeclaredField("profile");
                    profileField.setAccessible(true);
                    profileField.set(skullMeta, profile);
                } catch (NoSuchFieldException | IllegalAccessException e) {
                    e.printStackTrace();
                }
                skullMeta.setDisplayName("§c " + member.getName());
                skull.setItemMeta(skullMeta);
                inventory.setItem(i, skull);
            }
        }

        player.openInventory(inventory);
    }
    public OfflinePlayer getPlayerByUuid(UUID uuid) {

        for(OfflinePlayer p : getServer().getOfflinePlayers())
            if(p.getUniqueId().equals(uuid)) {
                return p;
            }

        throw new IllegalArgumentException();
    }
}
