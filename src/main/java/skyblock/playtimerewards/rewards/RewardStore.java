package skyblock.playtimerewards.rewards;

import java.util.HashMap;
import java.util.Map;

public class RewardStore {
    private Map<Integer, Rewards> rewards;

    public RewardStore(){
        rewards = new HashMap<>();
    }

    public void setRewards(Integer level, Rewards reward) {
        rewards.put(level, reward);
    }

    public Map<Integer, Rewards> getRewards() {
        return rewards;
    }
}
