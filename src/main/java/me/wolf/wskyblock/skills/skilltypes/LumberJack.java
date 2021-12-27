package me.wolf.wskyblock.skills.skilltypes;

import me.wolf.wskyblock.skills.Skill;
import me.wolf.wskyblock.skills.SkillReward;
import org.bukkit.Material;

public class LumberJack extends Skill {

    private SkillReward<Material> skillRewards;

    public LumberJack() {
        super("Lumberjack");
    }

    public SkillReward<Material> getSkillRewards() {
        return skillRewards;
    }

    public void setSkillRewards(SkillReward<Material> skillReward) {
        this.skillRewards = skillReward;
    }
}
