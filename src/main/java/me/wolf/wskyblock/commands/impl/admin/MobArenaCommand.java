package me.wolf.wskyblock.commands.impl.admin;

import me.wolf.wskyblock.SkyblockPlugin;
import me.wolf.wskyblock.commands.BaseCommand;
import me.wolf.wskyblock.mobarena.MobArena;
import me.wolf.wskyblock.player.SkyblockPlayer;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class MobArenaCommand extends BaseCommand {
    final List<Location> bounds = new ArrayList<>();
    private final SkyblockPlugin plugin;

    public MobArenaCommand(final SkyblockPlugin plugin) {
        super("mobarena");
        this.plugin = plugin;
    }

    @Override
    protected void run(CommandSender sender, String[] args) {

        if (isAdmin()) {
            final SkyblockPlayer player = plugin.getPlayerManager().getSkyblockPlayer(((Player) sender).getUniqueId());
            if (args.length == 2) {
                final String name = args[1];
                if (args[0].equalsIgnoreCase("create")) {
                    if (!plugin.getMobArenaManager().doesMobArenaExist(name)) {
                        plugin.getMobArenaManager().createMobArena(name);
                        player.sendMessage("&aSuccessfully created the arena &2" + name);
                    } else player.sendMessage("&cThis arena already exists!");
                }
                if (args[0].equalsIgnoreCase("addbound")) {

                    if (plugin.getMobArenaManager().doesMobArenaExist(name)) {
                        bounds.add(player.getLocation());
                        player.sendMessage("&aSet bound " + bounds.size() + " for &2" + name + "!");

                        if (bounds.size() == 2) {
                            player.sendMessage("&aArena Cuboid Created");
                            final MobArena mobArena = plugin.getMobArenaManager().getMobArenaByName(name);
                            if (mobArena != null) {
                                plugin.getMobArenaManager().setCuboid(mobArena, bounds.get(0), bounds.get(1));
                            }

                            bounds.clear();
                        }
                    } else tell("&cInvalid Arena!");
                }

                if (args[0].equalsIgnoreCase("remove")) {
                    if (plugin.getMobArenaManager().doesMobArenaExist(name)) {
                        plugin.getMobArenaManager().removeMobArena(name);
                        player.sendMessage("&aSuccessfully deleted the Mob Arena &2 " + name);
                    } else player.sendMessage("&cThis Mob Arena does not exist!");
                }
            }


        } else tell("&cNo Permission!");
    }

}
