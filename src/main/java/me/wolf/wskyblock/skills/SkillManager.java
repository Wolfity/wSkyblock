package me.wolf.wskyblock.skills;

import me.wolf.wskyblock.SkyblockPlugin;
import me.wolf.wskyblock.files.FileManager;
import me.wolf.wskyblock.files.YamlConfig;
import me.wolf.wskyblock.player.SkyblockPlayer;
import me.wolf.wskyblock.skills.skilltypes.LumberJack;
import me.wolf.wskyblock.skills.skilltypes.Miner;
import me.wolf.wskyblock.skills.skilltypes.MonsterKiller;
import me.wolf.wskyblock.utils.ItemUtils;
import me.wolf.wskyblock.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class SkillManager {

    private final SkyblockPlugin plugin;
    private final Set<Skill> skills = new HashSet<>();

    public SkillManager(final SkyblockPlugin plugin) {
        this.plugin = plugin;
    }

    public void loadSkills() {
        addSkills(new LumberJack(), new MonsterKiller(), new Miner());
        final FileManager cfg = plugin.getFileManager();

        List<String> description = new ArrayList<>();
        ItemStack icon = null;
        String scoreboardDisplay = "";

        for (final Skill skill : skills) {
            if (skill.getName().equalsIgnoreCase("miner")) {
                description = plugin.getFileManager().getMinerConfig().getConfig().getStringList("description");
                icon = ItemUtils.createItem(Material.valueOf(cfg.getMinerConfig().getConfig().getString("icon-material")), cfg.getMinerConfig().getConfig().getString("icon-name"));
                scoreboardDisplay = Utils.colorize(cfg.getMinerConfig().getConfig().getString("scoreboard-display"));
            } else if (skill.getName().equalsIgnoreCase("lumberjack")) {
                description = plugin.getFileManager().getLumberjackConfig().getConfig().getStringList("description");
                icon = ItemUtils.createItem(Material.valueOf(cfg.getLumberjackConfig().getConfig().getString("icon-material")), cfg.getLumberjackConfig().getConfig().getString("icon-name"));
                scoreboardDisplay = Utils.colorize(cfg.getLumberjackConfig().getConfig().getString("scoreboard-display"));
            } else if (skill.getName().equalsIgnoreCase("monster killer")) {
                description = plugin.getFileManager().getMonsterKillerConfig().getConfig().getStringList("description");
                icon = ItemUtils.createItem(Material.valueOf(cfg.getMonsterKillerConfig().getConfig().getString("icon-material")), cfg.getMonsterKillerConfig().getConfig().getString("icon-name"));
                scoreboardDisplay = Utils.colorize(cfg.getMonsterKillerConfig().getConfig().getString("scoreboard-display"));
            }
            skill.setDescription(description.toArray(new String[0]));
            skill.setIcon(icon);
            skill.setScoreboardDisplay(scoreboardDisplay);
        }
    }


    public Set<Skill> getSkills() {
        return skills;
    }

    // get the skill by a name, from a specific user (To track per user progress)
    public Skill getSkillByNamePlayer(final SkyblockPlayer player, final String name) {
        return player.getSkills().stream().filter(skill -> skill.getName().equalsIgnoreCase(name)).collect(Utils.toSingleton());
    }

    public void cacheRewards() { // cache all the rewards from every skill
        for (final Skill skill : skills) {
            if (skill.getName().equalsIgnoreCase("lumberjack")) {
                ((LumberJack) skill).setSkillRewards(getLoadedMaterialRewards(plugin.getFileManager().getLumberjackConfig()));
            } else if (skill.getName().equalsIgnoreCase("miner")) {
                ((Miner) skill).setSkillRewards(getLoadedMaterialRewards(plugin.getFileManager().getMinerConfig()));
            } else if (skill.getName().equalsIgnoreCase("monster killer")) {
                ((MonsterKiller) skill).setSillRewards(getLoadedEntityRewards(plugin.getFileManager().getMonsterKillerConfig()));
            }
        }
    }

    public void addExperience(final SkyblockPlayer player, final Skill skill, int amount) { // TODO save to DB more frequent
        if (skill.getLucky()) { // if the user got lucky, double the XP ;)
            amount *= 2;
        }
        if (skill.getCurrentExp() + amount >= skill.getExperienceNextLevel()) { // check if they will level up
            double difference = skill.getExperienceNextLevel() - skill.getCurrentExp() + amount; // if so, get the difference and add it to 0 (new level)
            skill.levelUp(); // then level them up and save it to the database
            skill.setCurrentExp((int) difference);
            Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
                plugin.getSqLiteManager().saveSkillData(player.getUuid(), skill, skill.getLevel() + " " + skill.getCurrentExp() + " " + skill.getExperienceNextLevel() + " " + skill.getLevelCap());
            });
            player.sendMessage("&aCongrats! Your &b" + skill.getName() + "&a has leveled up and is now level &c" + skill.getLevel());
        }
        skill.addExperience(amount); // add the actual XP to the skill
    }

    public Set<Skill> getSkillsByUser(final SkyblockPlayer player) {
        return player.getSkills();
    }

    private void addSkills(final Skill... skills) {
        this.skills.addAll(Arrays.asList(skills));
    }

    private void removeSkill(final Skill skill) {
        this.skills.remove(skill);
    }

    // for skills that need to break blocks
    private SkillReward<Material> getLoadedMaterialRewards(final YamlConfig config) { // for skills such as lumber & miner
        final SkillReward<Material> skillReward = new SkillReward<>();
        for (final String s : config.getConfig().getConfigurationSection("rewards").getKeys(false)) { // 1, 2, 3, 4...
            final Material action = Material.valueOf(config.getConfig().getConfigurationSection("rewards." + s).getString(".material"));
            final int reward = config.getConfig().getConfigurationSection("rewards." + s).getInt(".exp");
            skillReward.getRewardsMap().put(action, reward);
        }
        return skillReward;
    }

    // for skills that require interactions with entities
    private SkillReward<EntityType> getLoadedEntityRewards(final YamlConfig config) {
        final SkillReward<EntityType> skillReward = new SkillReward<>();
        for (final String s : config.getConfig().getConfigurationSection("rewards").getKeys(false)) { // 1, 2, 3, 4...
            final EntityType entityType = EntityType.valueOf(config.getConfig().getConfigurationSection("rewards." + s).getString(".entity"));
            final int reward = config.getConfig().getConfigurationSection("rewards." + s).getInt(".exp");
            skillReward.getRewardsMap().put(entityType, reward);
        }
        return skillReward;
    }

}


