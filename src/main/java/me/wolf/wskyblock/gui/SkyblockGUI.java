package me.wolf.wskyblock.gui;

import me.wolf.wskyblock.player.SkyblockPlayer;
import me.wolf.wskyblock.utils.ItemUtils;
import me.wolf.wskyblock.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public abstract class SkyblockGUI {

    private static final Map<UUID, SkyblockGUI> inventoriesByUUID = new HashMap<>();
    private static final Map<UUID, UUID> openInventories = new HashMap<>(); // <player, inv>
    private final UUID uuid;
    private final Inventory skyblockGUI;
    private final Map<Integer, ClickAction> actions;
    private final SkyblockPlayer owner;

    public SkyblockGUI(final int size, final String name, final SkyblockPlayer owner) {
        this.uuid = UUID.randomUUID();
        this.owner = owner;
        actions = new HashMap<>();

        inventoriesByUUID.put(getUuid(), this);
        skyblockGUI = Bukkit.createInventory(null, size, Utils.colorize(name));
    }

    public static Map<UUID, SkyblockGUI> getInventoriesByUUID() {
        return inventoriesByUUID;
    }

    public static Map<UUID, UUID> getOpenInventories() {
        return openInventories;
    }

    public Inventory getSkyblockGUI() {
        return skyblockGUI;
    }

    public void setItem(final int slot, final ItemStack stack, final ClickAction action) {
        skyblockGUI.setItem(slot, stack);
        if (action != null) {
            actions.put(slot, action);
        }
    }

    public void setItem(final int slot, final ItemStack stack) {
        setItem(slot, stack, null);
    }

    public void openSkyblockGUI(final SkyblockPlayer player) {
        player.getBukkitPlayer().openInventory(skyblockGUI);
        openInventories.put(player.getUuid(), getUuid());
    }

    public UUID getUuid() {
        return uuid;
    }

    public Map<Integer, ClickAction> getActions() {
        return actions;
    }


    public ItemStack getBackIcon() {
        return ItemUtils.createItem(Material.BARRIER, "&cBack!");
    }

    public void fillGUI(final Material material) { // will fill all slots except the ones that are not empty
        for (int i = 0; i < getSkyblockGUI().getSize(); i++) {
            if (getSkyblockGUI().getItem(i) == null || getSkyblockGUI().getItem(i).getType() == Material.AIR) {
                getSkyblockGUI().setItem(i, new ItemStack(material));
            }
        }
    }

    public interface ClickAction {
        void click(final Player player);
    }

}


