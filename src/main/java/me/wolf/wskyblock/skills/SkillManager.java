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
import java.util.stream.Collectors;

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
        int maxLevel = 0;
        double experienceNextLevel = 0;
        double multiplier = 0;

        // loading in the visuals for every skill
        for (final Skill skill : skills) {
            if (skill.getName().equalsIgnoreCase("miner")) {
                maxLevel = cfg.getMinerConfig().getConfig().getInt("max-level");
                experienceNextLevel = cfg.getMinerConfig().getConfig().getDouble("xp-to-first-level");
                multiplier = cfg.getMinerConfig().getConfig().getDouble("levelup-xp-increase-multiplier");
                description = cfg.getMinerConfig().getConfig().getStringList("description");
                icon = ItemUtils.createItem(Material.valueOf(cfg.getMinerConfig().getConfig().getString("icon-material")), cfg.getMinerConfig().getConfig().getString("icon-name"));
                scoreboardDisplay = Utils.colorize(cfg.getMinerConfig().getConfig().getString("scoreboard-display"));
            } else if (skill.getName().equalsIgnoreCase("lumberjack")) {
                maxLevel = cfg.getMinerConfig().getConfig().getInt("max-level");
                experienceNextLevel = cfg.getMinerConfig().getConfig().getDouble("xp-to-first-level");
                multiplier = cfg.getMinerConfig().getConfig().getDouble("levelup-xp-increase-multiplier");
                description = cfg.getLumberjackConfig().getConfig().getStringList("description");
                icon = ItemUtils.createItem(Material.valueOf(cfg.getLumberjackConfig().getConfig().getString("icon-material")), cfg.getLumberjackConfig().getConfig().getString("icon-name"));
                scoreboardDisplay = Utils.colorize(cfg.getLumberjackConfig().getConfig().getString("scoreboard-display"));
            } else if (skill.getName().equalsIgnoreCase("monster killer")) {
                maxLevel = cfg.getMonsterKillerConfig().getConfig().getInt("max-level");
                experienceNextLevel = cfg.getMonsterKillerConfig().getConfig().getDouble("xp-to-first-level");
                multiplier = cfg.getMinerConfig().getConfig().getDouble("levelup-xp-increase-multiplier");
                description = cfg.getMonsterKillerConfig().getConfig().getStringList("description");
                icon = ItemUtils.createItem(Material.valueOf(cfg.getMonsterKillerConfig().getConfig().getString("icon-material")), cfg.getMonsterKillerConfig().getConfig().getString("icon-name"));
                scoreboardDisplay = Utils.colorize(cfg.getMonsterKillerConfig().getConfig().getString("scoreboard-display"));
            }
            skill.setDescription(description.toArray(new String[0]));
            skill.setIcon(icon);
            skill.setLevelCap(maxLevel);
            skill.setExpIncreaseMultiplier(multiplier);
            skill.setExperienceNextLevel(experienceNextLevel);
            skill.setScoreboardDisplay(scoreboardDisplay);
        }
    }


    public Set<Skill> getSkills() {
        return skills;
    }

    // get the skill by a name, from a specific player (To track per player progress)
    public Skill getSkillByNamePlayer(final SkyblockPlayer player, final String name) {
        return player.getSkills().stream().filter(skill -> skill.getName().equalsIgnoreCase(name)).collect(Collectors.toList()).get(0);
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

    /**
     * Adding experience to a skill
     * @param player the player you give the experience to
     * @param skill the skill you want to increase the experience of
     * @param amount the amount of XP you want to add
     */
    public void addExperience(final SkyblockPlayer player, final Skill skill, int amount) {
        if (skill.getLevel() < skill.getLevelCap()) { // if the skill exceeded the max level, stop leveling up/adding XP
            if (skill.getLucky()) { // if the player got lucky, double the XP ;)
                amount *= 2;
            }
            if (skill.getCurrentExp() + amount >= skill.getExperienceNextLevel()) { // check if they will level up
                skill.levelUp(); // then level them up and save it to the database
                skill.setCurrentExp(0);
                Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
                    plugin.getSqLiteManager().saveSkillData(player.getUuid(), skill, skill.getLevel() + " " + skill.getCurrentExp() + " " + skill.getExperienceNextLevel() + " " + skill.getLevelCap());
                });
                player.sendMessage("&aCongrats! Your &b" + skill.getName() + "&a has leveled up and is now level &c" + skill.getLevel());
            }
            skill.addExperience(amount); // add the actual XP to the skill
        }
    }

    private void addSkills(final Skill... skills) {
        this.skills.addAll(Arrays.asList(skills));
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

    // get the skill object just by their name
    public Skill getRawSkillByName(final String name) {
        return skills.stream().filter(skill -> skill.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }

}



