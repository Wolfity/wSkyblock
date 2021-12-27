package me.wolf.wskyblock.gui.guis;

import me.wolf.wskyblock.SkyblockPlugin;
import me.wolf.wskyblock.gui.SkyblockGUI;
import me.wolf.wskyblock.player.SkyblockPlayer;
import me.wolf.wskyblock.utils.ItemUtils;
import me.wolf.wskyblock.utils.Utils;
import me.wolf.wskyblock.warps.Warp;
import org.bukkit.Material;

import java.util.List;

public class WarpGUI extends SkyblockGUI {
    public WarpGUI(final SkyblockPlugin plugin, final SkyblockPlayer owner) {
        super(54, "&bWarps", owner);

        final List<Warp> warps = plugin.getIslandManager().getIslandByOwner(owner).getWarps();

        for (int i = 0; i < warps.size(); i++) {
            int finalI = i;
            setItem(i, ItemUtils.createItem(
                    Material.valueOf(plugin.getConfig().getString("warp-icon")),
                    Utils.colorize(plugin.getConfig().getString("warp-display") + warps.get(i).getName())), player -> {
                player.teleport(warps.get(finalI).getLocation());
                owner.sendMessage("&aWarping to: " + warps.get(finalI));
            });
        }

        openSkyblockGUI(owner);
    }
}
