package skyblock.battlepass.api;

import de.backpack.apfloat.Apfloat;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import skyblock.battlepass.rewards.RewardsEnum;

public class BattlePassReward {
    private RewardsEnum rewardType;
    private Apfloat tokenValue;
    private Apfloat moneyValue;
    private ItemStack item;
    private ItemMeta itemMeta;

    public BattlePassReward(RewardsEnum rewardType, Apfloat value) {
        this.rewardType = rewardType;

        if (rewardType == RewardsEnum.Tokens) {
            this.tokenValue = value;
        } else if (rewardType == RewardsEnum.Money) {
            this.moneyValue = value;
        } else if (rewardType != RewardsEnum.Item) {
            throw new IllegalArgumentException("Invalid reward type for this value.");
        }
    }

    public BattlePassReward(RewardsEnum rewardType, ItemStack item, ItemMeta itemMeta) {
        this.rewardType = rewardType;

        if (rewardType == RewardsEnum.Item) {
            this.item = item;
            this.itemMeta = itemMeta;
        } else {
            throw new IllegalArgumentException("Invalid reward type for this item.");
        }
    }

    public RewardsEnum getRewardType() {
        return rewardType;
    }

    public Apfloat getTokenValue() {
        if (rewardType == RewardsEnum.Tokens) {
            return tokenValue;
        } else {
            return Apfloat.ZERO; // Return a default value or handle appropriately
        }
    }

    public Apfloat getMoneyValue() {
        if (rewardType == RewardsEnum.Money) {
            return moneyValue;
        } else {
            return Apfloat.ZERO; // Return a default value or handle appropriately
        }
    }

    public ItemStack getItem() {
        if (rewardType == RewardsEnum.Item) {
            return item;
        } else {
            return null; // Return null or handle appropriately
        }
    }

    public ItemMeta getItemMeta() {
        if (rewardType == RewardsEnum.Item) {
            return itemMeta;
        } else {
            return null; // Return null or handle appropriately
        }
    }
}
