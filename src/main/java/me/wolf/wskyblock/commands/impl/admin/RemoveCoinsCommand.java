package me.wolf.wskyblock.commands.impl.admin;

import me.wolf.wskyblock.SkyblockPlugin;
import me.wolf.wskyblock.commands.BaseCommand;
import me.wolf.wskyblock.player.SkyblockPlayer;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RemoveCoinsCommand extends BaseCommand {
    private final SkyblockPlugin plugin;

    public RemoveCoinsCommand(final SkyblockPlugin plugin) {
        super("removecoins");
        this.plugin = plugin;
    }

    @Override
    protected void run(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) return;
        final SkyblockPlayer player = plugin.getPlayerManager().getSkyblockPlayer(((Player) sender).getUniqueId());

        if (isAdmin()) {
            if (args.length == 2) {
                if (Bukkit.getPlayerExact(args[0]) == null) player.sendMessage("&cInvalid User");
                final SkyblockPlayer target = plugin.getPlayerManager().getSkyblockPlayer(Bukkit.getPlayerExact(args[0]).getUniqueId()); // user is online

                int amount = 0;
                try {
                    amount = Integer.parseInt(args[1]);
                } catch (final NumberFormatException e) {
                    player.sendMessage("&cThis amount is not valid\nUsage: /removecoins <user> <amount> ");
                }

                int finalAmount = amount;
                Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> target.setCoins(target.getCoins() - finalAmount));
                player.sendMessage("&aSuccessfully taken &2" + amount + " &acoins of &2" + target.getName());

            }

        } else player.sendMessage("&cNo permission!");
    }
}
