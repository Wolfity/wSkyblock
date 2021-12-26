package me.wolf.wskyblock.commands.impl;

import me.wolf.wskyblock.SkyblockPlugin;
import me.wolf.wskyblock.commands.BaseCommand;
import me.wolf.wskyblock.gui.guis.MainGUI;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class SkyblockCommand extends BaseCommand {
    private final SkyblockPlugin plugin;

    public SkyblockCommand(final SkyblockPlugin plugin) {
        super("skyblock");
        setAliases(Arrays.asList("sb", "wsb", "wskyblock"));

        this.plugin = plugin;
    }

    @Override
    protected void run(CommandSender sender, String[] args) {
        final Player player = (Player) sender;
        if (args.length == 0) {
            new MainGUI(plugin, plugin.getPlayerManager().getSkyblockPlayer(player.getUniqueId()));
        }


    }
}
