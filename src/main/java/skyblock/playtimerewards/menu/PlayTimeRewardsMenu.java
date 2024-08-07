package skyblock.playtimerewards.menu;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import skyblock.battlepass.api.BattlePassLevel;
import skyblock.battlepass.api.BattlePassReward;
import skyblock.battlepass.rewards.RewardsEnum;
import skyblock.main.Main;
import skyblock.playtimerewards.rewards.RewardStore;
import skyblock.playtimerewards.rewards.Rewards;

import java.util.ArrayList;
import java.util.List;

public class PlayTimeRewardsMenu {


    private static final String INVENTORY_NAME = "Rewards";
    private static final int INVENTORY_ROWS = 6;

    public void openMenu(Player player) {
        Inventory inventory = Bukkit.createInventory(null, INVENTORY_ROWS * 9, INVENTORY_NAME);

        RewardStore rewards = Main.instance.rewardStore;

        for (int i = 0; i < INVENTORY_ROWS * 9; i++) {
            Rewards reward = rewards.getRewards().get(i);
            if (reward != null) {
                ItemStack item;
                ItemMeta meta;
                if (reward.getRewardType() == RewardsEnum.Item) {
                    item = new ItemStack(reward.getItem().getType(), 1);
                    meta = reward.getItemMeta();
                    item.setItemMeta(meta);
                } else {
                    item = new ItemStack(Material.GOLD_INGOT, 1);
                    meta = item.getItemMeta();
                    meta.setDisplayName("§cReward: " + i);
                    List<String> lore = new ArrayList<>();
                    lore.add("");
                    meta.setLore(lore);
                    item.setItemMeta(meta);
                }
                inventory.setItem(i, item);
            }
        }

        player.openInventory(inventory);
    }


}
