package me.wolf.wskyblock.commands.impl;

import me.wolf.wskyblock.SkyblockPlugin;
import me.wolf.wskyblock.commands.BaseCommand;
import me.wolf.wskyblock.gui.guis.SkillGUI;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SkillCommand extends BaseCommand {
    private final SkyblockPlugin plugin;

    public SkillCommand(final SkyblockPlugin plugin) {
        super("skills");
        this.plugin = plugin;
    }

    @Override
    protected void run(CommandSender sender, String[] args) {
        new SkillGUI(plugin, plugin.getPlayerManager().getSkyblockPlayer(((Player) sender).getUniqueId()));
    }
}
