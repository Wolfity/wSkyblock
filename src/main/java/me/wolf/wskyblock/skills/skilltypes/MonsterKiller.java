package me.wolf.wskyblock.skills.skilltypes;

import me.wolf.wskyblock.skills.Skill;
import me.wolf.wskyblock.skills.SkillReward;
import org.bukkit.entity.EntityType;

public class MonsterKiller extends Skill {

    private SkillReward<EntityType> skillRewards;

    public MonsterKiller() {
        super("Monster Killer");

    }

    public void setSillRewards(SkillReward<EntityType> rewards) {
        this.skillRewards = rewards;
    }

    public SkillReward<EntityType> getSkillRewards() {
        return skillRewards;
    }
}
