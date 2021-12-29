package me.wolf.wskyblock.crafingrecipes;

import me.wolf.wskyblock.SkyblockPlugin;
import me.wolf.wskyblock.utils.ItemUtils;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class CustomCraftingRecipes {

    private final Set<ShapedRecipe> recipes = new HashSet<>();

    public void loadRecipes(final SkyblockPlugin plugin) {
        addRecipes(timberAxeRecipe(plugin), godAppleRecipe(plugin), telekinesisPickaxeRecipe(plugin), godPotionRecipe(plugin));
        for (final ShapedRecipe recipe : recipes) {
            plugin.getServer().addRecipe(recipe); // adding all the recipes to the server
        }
    }


    private void addRecipes(final ShapedRecipe... recipes) {
        this.recipes.addAll(Arrays.asList(recipes));
    }


    public ShapedRecipe timberAxeRecipe(final SkyblockPlugin plugin) {
        final ItemStack reward = ItemUtils.createItem(Material.GOLDEN_AXE, "&cCrafted Timber Axe", Collections.singletonList("&bTimber 1"));
        final ItemMeta meta = reward.getItemMeta();
        final PersistentDataContainer pdc = meta.getPersistentDataContainer();

        pdc.set(new NamespacedKey(plugin, "timber"), PersistentDataType.STRING, "timber1");
        reward.setItemMeta(meta);
        final ShapedRecipe recipe = new ShapedRecipe(new NamespacedKey(plugin, "craftedtimberaxe"), reward);
        recipe.shape("ada", "aga", "ada"); // a = ancient debris, g = golden axe, d = diamond

        recipe.setIngredient('a', Material.ANCIENT_DEBRIS);
        recipe.setIngredient('d', Material.DIAMOND);
        recipe.setIngredient('g', Material.GOLDEN_AXE);

        return recipe;
    }

    public ShapedRecipe telekinesisPickaxeRecipe(final SkyblockPlugin plugin) {
        final ItemStack reward = ItemUtils.createItem(Material.DIAMOND_PICKAXE, "&bCrafted Telekinesis Pickaxe", Collections.singletonList("&bTelekinesis 1"));
        final ItemMeta meta = reward.getItemMeta();
        final PersistentDataContainer pdc = meta.getPersistentDataContainer();

        pdc.set(new NamespacedKey(plugin, "telekinesis"), PersistentDataType.STRING, "telekinesis1");
        reward.setItemMeta(meta);
        final ShapedRecipe recipe = new ShapedRecipe(new NamespacedKey(plugin, "craftedtelekinesispickaxe"), reward);
        recipe.shape("ada", "aia", "ada"); // a = ancient debris, i = iron pickaxe axe, d = diamond

        recipe.setIngredient('a', Material.ANCIENT_DEBRIS);
        recipe.setIngredient('d', Material.DIAMOND);
        recipe.setIngredient('i', Material.IRON_PICKAXE);

        return recipe;

    }

    private ShapedRecipe godAppleRecipe(final SkyblockPlugin plugin) {
        final ItemStack reward = ItemUtils.createItem(Material.ENCHANTED_GOLDEN_APPLE, "&c&lGod Apple");

        final ShapedRecipe recipe = new ShapedRecipe(new NamespacedKey(plugin, "godapple"), reward);
        recipe.shape("ggg", "gag", "ggg"); // g = gold block a = gold apple

        recipe.setIngredient('g', Material.GOLD_BLOCK);
        recipe.setIngredient('a', Material.GOLDEN_APPLE);

        return recipe;
    }

    private ShapedRecipe godPotionRecipe(final SkyblockPlugin plugin) {
        final ItemStack reward = ItemUtils.createItem(Material.POTION, "&b&lGod Potion &7(30 min)");
        final PotionMeta meta = (PotionMeta) reward.getItemMeta();
        meta.addCustomEffect(new PotionEffect(PotionEffectType.SPEED, 36000, 4, false, false), false);
        meta.addCustomEffect(new PotionEffect(PotionEffectType.ABSORPTION, 36000, 4, false, false), false);
        meta.addCustomEffect(new PotionEffect(PotionEffectType.REGENERATION, 36000, 4, false, false), false);
        meta.addCustomEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 36000, 4, false, false), false);
        reward.setItemMeta(meta);

        final ShapedRecipe recipe = new ShapedRecipe(new NamespacedKey(plugin, "godpot"), reward);
        recipe.shape("gdg", "gwg", "gdg"); // g = gold block | w = water bucket | d = diamond block

        recipe.setIngredient('g', Material.GOLD_BLOCK);
        recipe.setIngredient('d', Material.DIAMOND_BLOCK);
        recipe.setIngredient('w', Material.WATER_BUCKET);
        return recipe;
    }


}
