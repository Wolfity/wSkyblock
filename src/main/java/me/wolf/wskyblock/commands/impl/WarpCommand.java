package me.wolf.wskyblock.commands.impl;

import me.wolf.wskyblock.SkyblockPlugin;
import me.wolf.wskyblock.commands.BaseCommand;
import me.wolf.wskyblock.gui.guis.WarpGUI;
import me.wolf.wskyblock.island.Island;
import me.wolf.wskyblock.player.SkyblockPlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class WarpCommand extends BaseCommand {
    private final SkyblockPlugin plugin;

    public WarpCommand(final SkyblockPlugin plugin) {
        super("warp");
        this.plugin = plugin;
    }

    @Override
    protected void run(CommandSender sender, String[] args) {
        final SkyblockPlayer player = plugin.getPlayerManager().getSkyblockPlayer(((Player) sender).getUniqueId());
        final Island island = plugin.getIslandManager().getIslandByOwner(player);

        if (args.length == 0) {
            if (island == null) {
                player.sendMessage("&cYou need an island in order to execute this command!");
            } else new WarpGUI(plugin, player);
        }

        if (args.length == 1) {
            if (island != null) {
                final String warpName = args[0];
                if (doesWarpExist(plugin.getIslandManager().getIslandByOwner(player), warpName)) {
                    player.getBukkitPlayer().teleport(plugin.getIslandManager().getWarpByName(island, warpName).getLocation());
                    player.sendMessage("&aWarped to &2" + warpName);
                }
            } else player.sendMessage("&cYou need an island to execute this command!");
        }
    }


    private boolean doesWarpExist(final Island island, final String name) {
        return island.getWarps().stream().anyMatch(warp -> warp.getName().equalsIgnoreCase(name));
    }

}
