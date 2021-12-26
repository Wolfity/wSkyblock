package me.wolf.wskyblock.commands.impl;

import me.wolf.wskyblock.SkyblockPlugin;
import me.wolf.wskyblock.commands.BaseCommand;
import me.wolf.wskyblock.gui.guis.MagicMarketGUI;
import me.wolf.wskyblock.player.SkyblockPlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;

public class MagicMarketCommand extends BaseCommand {

    private final SkyblockPlugin plugin;

    public MagicMarketCommand(final SkyblockPlugin plugin) {
        super("magicmarket");
        setAliases(Collections.singletonList("mm"));
        this.plugin = plugin;
    }

    @Override
    protected void run(CommandSender sender, String[] args) {
        final Player player = (Player) sender;
        final SkyblockPlayer skyblockPlayer = plugin.getPlayerManager().getSkyblockPlayer(player.getUniqueId());

        if (args.length == 0)
            new MagicMarketGUI(plugin, skyblockPlayer, plugin.getMagicMarketManager().getMagicMarket(), 1);


    }

}
