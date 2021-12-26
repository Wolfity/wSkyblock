package me.wolf.wskyblock.skills;

import java.util.HashMap;
import java.util.Map;

public class SkillReward<T> {

    private final Map<T, Integer> rewardsMap;

    public SkillReward() {
        this.rewardsMap = new HashMap<>();
    }

    public Map<T, Integer> getRewardsMap() {
        return rewardsMap;
    }


}
