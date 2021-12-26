package me.wolf.wskyblock.enchants;

import org.bukkit.Material;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum SupportEnchantItem {
    // all of the items enchants can be applied to
    BOOTS(Arrays.asList(Material.LEATHER_BOOTS, Material.CHAINMAIL_BOOTS, Material.GOLDEN_BOOTS, Material.IRON_BOOTS, Material.DIAMOND_BOOTS, Material.NETHERITE_BOOTS)),
    LEGGINGS(Arrays.asList(Material.LEATHER_LEGGINGS, Material.CHAINMAIL_LEGGINGS, Material.GOLDEN_LEGGINGS, Material.IRON_LEGGINGS, Material.DIAMOND_LEGGINGS, Material.NETHERITE_LEGGINGS)),
    CHESTPLATE(Arrays.asList(Material.LEATHER_CHESTPLATE, Material.CHAINMAIL_CHESTPLATE, Material.GOLDEN_CHESTPLATE, Material.IRON_CHESTPLATE, Material.DIAMOND_CHESTPLATE, Material.NETHERITE_CHESTPLATE)),
    HELMET(Arrays.asList(Material.LEATHER_HELMET, Material.CHAINMAIL_HELMET, Material.GOLDEN_HELMET, Material.IRON_HELMET, Material.DIAMOND_HELMET, Material.NETHERITE_HELMET)),
    ALL_ARMOR(Stream.of(BOOTS.getSupportedItems(), LEGGINGS.getSupportedItems(), CHESTPLATE.getSupportedItems(), HELMET.getSupportedItems()).flatMap(Collection::stream).collect(Collectors.toList())),
    BOW(Arrays.asList(Material.BOW, Material.CROSSBOW)),
    SWORD(Arrays.asList(Material.WOODEN_SWORD, Material.STONE_SWORD, Material.GOLDEN_SWORD, Material.IRON_SWORD, Material.DIAMOND_SWORD, Material.NETHERITE_SWORD)),
    AXE(Arrays.asList(Material.WOODEN_AXE, Material.STONE_AXE, Material.GOLDEN_AXE, Material.IRON_AXE, Material.DIAMOND_AXE, Material.NETHERITE_AXE)),
    PICKAXE(Arrays.asList(Material.WOODEN_PICKAXE, Material.STONE_PICKAXE, Material.GOLDEN_PICKAXE, Material.IRON_PICKAXE, Material.DIAMOND_PICKAXE, Material.NETHERITE_PICKAXE)),
    SHOVEL(Arrays.asList(Material.WOODEN_SHOVEL, Material.STONE_SHOVEL, Material.GOLDEN_SHOVEL, Material.IRON_SHOVEL, Material.DIAMOND_SHOVEL, Material.NETHERITE_SHOVEL)),
    ALL_TOOLS(Stream.of(AXE.getSupportedItems(), PICKAXE.getSupportedItems(), SHOVEL.getSupportedItems()).flatMap(Collection::stream).collect(Collectors.toList())),
    ;

    private final List<Material> supportedItems;

    SupportEnchantItem(final List<Material> supportedItems) {
        this.supportedItems = supportedItems;
    }

    public List<Material> getSupportedItems() {
        return supportedItems;
    }
}
