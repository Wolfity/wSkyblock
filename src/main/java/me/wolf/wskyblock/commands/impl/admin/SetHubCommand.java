package me.wolf.wskyblock.commands.impl.admin;

import me.wolf.wskyblock.SkyblockPlugin;
import me.wolf.wskyblock.commands.BaseCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;

public class SetHubCommand extends BaseCommand {
    private final SkyblockPlugin plugin;

    public SetHubCommand(final SkyblockPlugin plugin) {
        super("sethub");
        setAliases(Collections.singletonList("setspawn"));
        this.plugin = plugin;

    }

    @Override
    protected void run(CommandSender sender, String[] args) {
        if (isAdmin()) {
            final Player player = (Player) sender;
            plugin.getConfig().createSection("spawn");
            plugin.getConfig().set("spawn.world", player.getLocation().getWorld().getName());
            plugin.getConfig().set("spawn.x", player.getLocation().getX());
            plugin.getConfig().set("spawn.y", player.getLocation().getY());
            plugin.getConfig().set("spawn.z", player.getLocation().getZ());
            plugin.saveConfig();

            tell("&aSuccessfully set the hub spawn!");
        } else tell("&cNo Permission!");
    }
}
