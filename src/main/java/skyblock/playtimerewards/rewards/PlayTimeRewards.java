package skyblock.playtimerewards.rewards;

import de.backpack.listener.EconomyAPI;
import de.prestigesystem.api.PlayerTalents;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import skyblock.battlepass.rewards.RewardsEnum;
import skyblock.main.Main;
import skyblock.store.PlayerStore;

import java.util.HashMap;
import java.util.Map;

public class PlayTimeRewards {

    int level;

    Map<Integer, Boolean> claimed = new HashMap<>();
    Map<Integer, Boolean> canClaim = new HashMap<>();

    public PlayTimeRewards(){

    }

    public void claimReward(int level, Player player){
        if(!claimed.containsKey(level)) {
            claimed.put(level, true);

            RewardStore rewardStore = Main.instance.rewardStore;
            Rewards rewards = rewardStore.getRewards().get(level);

            if(rewards.getRewardType().equals(RewardsEnum.Item)){
                ItemStack item = rewards.getItem();
                ItemMeta meta = rewards.getItemMeta();
                item.setItemMeta(meta);
                player.getInventory().addItem(item);
            }
            if(rewards.getRewardType().equals(RewardsEnum.Tokens)){
                EconomyAPI economyAPI = de.backpack.main.Main.economyAPI;
                economyAPI.addTokens(player, rewards.getTokenValue());
            }
            if(rewards.getRewardType().equals(RewardsEnum.Money)){
                EconomyAPI economyAPI = de.backpack.main.Main.economyAPI;
                economyAPI.addCashBalance(player, rewards.getMoneyValue());
            }

        }
    }

    public boolean canClaim(int level){

        return canClaim.get(level);
    }
}
