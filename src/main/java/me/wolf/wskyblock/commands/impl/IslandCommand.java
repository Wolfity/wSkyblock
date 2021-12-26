package me.wolf.wskyblock.commands.impl;

import me.wolf.wskyblock.SkyblockPlugin;
import me.wolf.wskyblock.commands.BaseCommand;
import me.wolf.wskyblock.player.SkyblockPlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;

public class IslandCommand extends BaseCommand {

    private final SkyblockPlugin plugin;

    public IslandCommand(final SkyblockPlugin plugin) {
        super("is");
        setAliases(Collections.singletonList("island"));
        this.plugin = plugin;
    }

    @Override
    protected void run(CommandSender sender, String[] args) {
        final SkyblockPlayer player = plugin.getPlayerManager().getSkyblockPlayer(((Player) sender).getUniqueId());

        if (plugin.getIslandManager().getIslandByOwner(player) != null) {
            tell("&aWarping to your island!");
            player.getBukkitPlayer().teleport(plugin.getIslandManager().getIslandByOwner(player).getSpawn());
        } else tell("&cYou dont have an island!");
    }
}
