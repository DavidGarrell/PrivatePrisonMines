package skyblock.battlepass.api;

import skyblock.main.Main;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BattlePassLevel {


    private Map<Integer, BattlePassReward> rewards;

    public BattlePassLevel(){
        rewards = new HashMap<>();
    }

    public void setRewards(Integer level, BattlePassReward reward) {
        rewards.put(level, reward);
    }

    public Map<Integer, BattlePassReward> getRewards() {
        return rewards;
    }
}
