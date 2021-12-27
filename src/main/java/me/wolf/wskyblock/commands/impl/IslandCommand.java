package me.wolf.wskyblock.commands.impl;

import me.wolf.wskyblock.SkyblockPlugin;
import me.wolf.wskyblock.commands.BaseCommand;
import me.wolf.wskyblock.island.Island;
import me.wolf.wskyblock.player.SkyblockPlayer;
import me.wolf.wskyblock.warps.Warp;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.Objects;

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
        final Island island = plugin.getIslandManager().getIslandByOwner(player);
        if (args.length == 0) {
            if (island != null) {
                tell("&aWarping to your island!");
                player.getBukkitPlayer().teleport(island.getSpawn());
            } else tell("&cYou dont have an island!");
        }
        if (args.length == 1 && args[0].equalsIgnoreCase("addwarp")) {
            player.sendMessage("&cIncorrect usage: /is addwarp <name>");

        } else if (args.length == 2) {
            final String name = args[1];
            // /is addwarp <name>
            if (args[0].equalsIgnoreCase("addwarp")) {
                if (island != null) {
                    // if a player isn't in the same world as their island, disallow it
                    if (!Objects.equals(player.getLocation().getWorld(), island.getSpawn().getWorld()))
                        player.sendMessage("&cYou must be on your island!");
                    if (doesWarpNotExist(island, name)) {
                        plugin.getIslandManager().addWarp(island, new Warp(player.getLocation(), name));
                        player.sendMessage("&aSuccessfully created the warp &2" + name);

                    } else player.sendMessage("&cThis warp already exists!");
                } else player.sendMessage("&cYou need an island to use this command!");

            } else if (args[0].equalsIgnoreCase("removewarp")) {
                if (island != null) {
                    if(!doesWarpNotExist(island, name)) {
                        plugin.getIslandManager().removeWarp(island, name);
                        player.sendMessage("&aThe warp &2" + name + "&a was successfully removed!");
                    } else player.sendMessage("&cThis warp does not exist!");

                }
            }
        }
    }

    private boolean doesWarpNotExist(final Island island, final String warpName) {
        return island.getWarps().stream().noneMatch(warp -> warp.getName().equalsIgnoreCase(warpName));
    }
}
