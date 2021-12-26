package me.wolf.wskyblock.gui;

import me.wolf.wskyblock.player.PlayerManager;
import me.wolf.wskyblock.player.SkyblockPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.UUID;

public class GUIListener implements Listener {

    private final PlayerManager playerManager;

    public GUIListener(final PlayerManager playerManager) {
        this.playerManager = playerManager;
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) return;
        if (playerManager.getSkyblockPlayer(event.getWhoClicked().getUniqueId()) == null) return;

        final SkyblockPlayer skyblockPlayer = playerManager.getSkyblockPlayer(event.getWhoClicked().getUniqueId());

        final UUID guiUUID = SkyblockGUI.getOpenInventories().get(skyblockPlayer.getUuid());
        if (guiUUID == null) return;

        event.setCancelled(true);
        final SkyblockGUI skyblockGUI = SkyblockGUI.getInventoriesByUUID().get(guiUUID);
        final SkyblockGUI.ClickAction clickAction = skyblockGUI.getActions().get(event.getSlot());

        if (clickAction == null) return;
        clickAction.click(skyblockPlayer.getBukkitPlayer());

    }

    @EventHandler
    public void onClose(InventoryCloseEvent event) {
        final SkyblockPlayer skyblockPlayer = playerManager.getSkyblockPlayer(event.getPlayer().getUniqueId());
        if (skyblockPlayer == null) return;

        SkyblockGUI.getOpenInventories().remove(skyblockPlayer.getUuid());
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        final SkyblockPlayer skyblockPlayer = playerManager.getSkyblockPlayer(event.getPlayer().getUniqueId());
        if (skyblockPlayer == null) return;

        SkyblockGUI.getOpenInventories().remove(skyblockPlayer.getUuid());
    }


}
