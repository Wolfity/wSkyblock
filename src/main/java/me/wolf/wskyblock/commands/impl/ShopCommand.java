package me.wolf.wskyblock.commands.impl;

import me.wolf.wskyblock.SkyblockPlugin;
import me.wolf.wskyblock.commands.BaseCommand;
import me.wolf.wskyblock.gui.guis.ShopGUI;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ShopCommand extends BaseCommand {
    private final SkyblockPlugin plugin;

    public ShopCommand(final SkyblockPlugin plugin) {
        super("shop");
        this.plugin = plugin;
    }

    @Override
    protected void run(CommandSender sender, String[] args) {
        new ShopGUI(plugin, plugin.getPlayerManager().getSkyblockPlayer(((Player) sender).getUniqueId()));
    }
}
