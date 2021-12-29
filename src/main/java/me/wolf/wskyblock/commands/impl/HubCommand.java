package me.wolf.wskyblock.commands.impl;

import me.wolf.wskyblock.SkyblockPlugin;
import me.wolf.wskyblock.commands.BaseCommand;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.Objects;

public class HubCommand extends BaseCommand {
    private final SkyblockPlugin plugin;

    public HubCommand(final SkyblockPlugin plugin) {
        super("hub");
        setAliases(Collections.singletonList("spawn"));
        this.plugin = plugin;
    }

    @Override
    protected void run(CommandSender sender, String[] args) {
        final Location hub = new Location(Bukkit.getWorld(Objects.requireNonNull(plugin.getConfig().getString("spawn.world"))),
                plugin.getConfig().getDouble("spawn.x"),
                plugin.getConfig().getDouble("spawn.y"),
                plugin.getConfig().getDouble("spawn.z"));

        if(hub.getWorld() == null) {
            tell("&cNo Hub has been set yet!");
        } else {
            ((Player)sender).teleport(hub);
            tell("&aTeleported to the hub!");
        }

    }
}
