package me.wolf.wskyblock.skills.skilltypes;

import me.wolf.wskyblock.skills.Skill;
import me.wolf.wskyblock.skills.SkillReward;
import org.bukkit.Material;

public class Miner extends Skill {

    private SkillReward<Material> skillRewards;

    public Miner() {
        super("Miner");

    }

    public SkillReward<Material> getSkillRewards() {
        return skillRewards;
    }

    public void setSkillRewards(SkillReward<Material> skillRewards) {
        this.skillRewards = skillRewards;
    }
}
