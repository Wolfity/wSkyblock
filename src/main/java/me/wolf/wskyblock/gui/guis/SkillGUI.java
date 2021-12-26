package me.wolf.wskyblock.gui.guis;

import me.wolf.wskyblock.SkyblockPlugin;
import me.wolf.wskyblock.gui.SkyblockGUI;
import me.wolf.wskyblock.player.SkyblockPlayer;
import me.wolf.wskyblock.skills.Skill;
import me.wolf.wskyblock.utils.ItemUtils;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SkillGUI extends SkyblockGUI {
    private final SkyblockPlugin plugin;

    public SkillGUI(final SkyblockPlugin plugin, final SkyblockPlayer owner) {
        super(9, "&cSkills", owner);
        this.plugin = plugin;

        int i = 1;
        final List<Skill> sortedList = new ArrayList<>(owner.getSkills());
        Collections.sort(sortedList);

        for (final Skill skill : sortedList) { // display each skill, the progress and their descriptions
            final List<String> newDescription = new ArrayList<>();
            for (String s : skill.getDescription()) {
                s = s.replace("{level}", String.valueOf(skill.getLevel()))
                        .replace("{xp}", String.valueOf(skill.getCurrentExp()))
                        .replace("{xptogo}", String.valueOf((int) skill.getExperienceNextLevel()));
                newDescription.add(s);
            }

            setItem(i, ItemUtils.createItem(skill.getIcon(), newDescription));

            setItem(8, getBackIcon(), player -> new MainGUI(plugin, owner));

            i += 2;
        }

        fillGUI(Material.ORANGE_STAINED_GLASS_PANE);
        openSkyblockGUI(owner);
    }
}
