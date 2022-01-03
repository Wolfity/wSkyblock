package me.wolf.wskyblock.utils;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public final class ItemUtils {

    private ItemUtils() {
    }

    public static ItemStack createItem(final Material mat, final String display, final int amount) {
        final ItemStack is = new ItemStack(mat, amount);
        final ItemMeta meta = is.getItemMeta();

        assert meta != null;
        meta.setDisplayName(Utils.colorize(display));
        is.setItemMeta(meta);

        return is;
    }

    public static ItemStack createItem(final Material mat, final List<String> lore) {
        final ItemStack is = new ItemStack(mat);
        final ItemMeta meta = is.getItemMeta();
        final List<String> coloredLore = new ArrayList<>();

        for (final String s : lore) {
            coloredLore.add(Utils.colorize(s));
        }

        assert meta != null;
        meta.setLore(coloredLore);
        is.setItemMeta(meta);

        return is;
    }

    public static ItemStack createItem(final Material mat, final String display, final int amount, final short data) {
        final ItemStack is = new ItemStack(mat, amount, data);
        final ItemMeta meta = is.getItemMeta();

        assert meta != null;
        meta.setDisplayName(Utils.colorize(display));
        is.setItemMeta(meta);

        return is;
    }

    public static ItemStack createItem(final ItemStack is, final List<String> lore) {
        final ItemMeta itemMeta = is.getItemMeta();
        final List<String> newLore = new ArrayList<>();
        for (final String s : lore) {
            newLore.add(Utils.colorize(s));
        }
        assert itemMeta != null;
        itemMeta.setLore(newLore);
        is.setItemMeta(itemMeta);
        return is;
    }

    public static ItemStack createItem(final Material mat, final String display, final List<String> lore) {
        final ItemStack is = new ItemStack(mat);
        final ItemMeta meta = is.getItemMeta();

        assert meta != null;
        meta.setDisplayName(Utils.colorize(display));
        meta.setLore(Utils.colorize(lore));
        is.setItemMeta(meta);

        return is;
    }

    public static ItemStack createItem(final Material mat, final String display, final short data) {
        final ItemStack is = new ItemStack(mat, data);
        final ItemMeta meta = is.getItemMeta();

        assert meta != null;
        meta.setDisplayName(Utils.colorize(display));
        is.setItemMeta(meta);

        return is;
    }

    public static ItemStack createItem(final Material mat, final String display) {
        final ItemStack is = new ItemStack(mat);
        final ItemMeta meta = is.getItemMeta();

        assert meta != null;
        meta.setDisplayName(Utils.colorize(display));
        is.setItemMeta(meta);

        return is;
    }

    public static String deserializeItemStack(final ItemStack is) {
        try {
            final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            final BukkitObjectOutputStream bukkitObjectOutputStream = new BukkitObjectOutputStream(byteArrayOutputStream);
            bukkitObjectOutputStream.writeObject(is);
            bukkitObjectOutputStream.flush();

            byte[] obj = byteArrayOutputStream.toByteArray();
            return Base64.getEncoder().encodeToString(obj);

        } catch (final IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static ItemStack serializeItemStack(final String s) {
        try {
            final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(Base64.getDecoder().decode(s));
            final BukkitObjectInputStream bukkitObjectInputStream = new BukkitObjectInputStream(byteArrayInputStream);
            return (ItemStack) bukkitObjectInputStream.readObject();
        } catch (final IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean isBoots(final ItemStack is) {
        return is.getType() == Material.LEATHER_BOOTS || is.getType() == Material.CHAINMAIL_BOOTS || is.getType() == Material.IRON_BOOTS || is.getType() == Material.DIAMOND_BOOTS || is.getType() == Material.GOLDEN_BOOTS || is.getType() == Material.NETHERITE_BOOTS;
    }

}
