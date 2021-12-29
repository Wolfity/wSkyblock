package me.wolf.wskyblock.skills;

import java.util.HashMap;
import java.util.Map;

public class SkillReward<T> {
    /* the goal to get rewards for a specific skill
       for example, mining blocks, killing entities, etc..
     */
    private final Map<T, Integer> rewardsMap;

    public SkillReward() {
        this.rewardsMap = new HashMap<>();
    }

    public Map<T, Integer> getRewardsMap() {
        return rewardsMap;
    }


}
