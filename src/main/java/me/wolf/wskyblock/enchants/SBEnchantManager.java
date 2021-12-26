package me.wolf.wskyblock.enchants;

import me.wolf.wskyblock.files.YamlConfig;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class SBEnchantManager {


    private final Set<SBEnchantment> sbEnchantments = new HashSet<>();

    public void loadEnchants(final YamlConfig cfg) {

        for (final String enchant : cfg.getConfig().getConfigurationSection("enchants").getKeys(false)) {
            final boolean isEnabled = cfg.getConfig().getBoolean("enchants." + enchant + ".enabled");
            if (isEnabled) {
                final SupportEnchantItem target = SupportEnchantItem.valueOf(cfg.getConfig().getString("enchants." + enchant + ".target").toUpperCase());
                final String name = cfg.getConfig().getString("enchants." + enchant + ".name");
                final String display = cfg.getConfig().getString("enchants." + enchant + ".display");
                final int maxLevel = cfg.getConfig().getInt("enchants." + enchant + ".max-level");
                final int cost = cfg.getConfig().getInt("enchants." + enchant + ".cost");
                final double costIncrease = cfg.getConfig().getDouble("enchants." + enchant + ".cost-increase");

                addSBEnchants(new SBEnchantment(target, name, display, maxLevel, costIncrease, cost));
            }
        }

    }

    private void addSBEnchants(final SBEnchantment... sbEnchantments) {
        this.sbEnchantments.addAll(Arrays.asList(sbEnchantments));
    }


    public SBEnchantment getEnchantmentByName(final String name) {
        return sbEnchantments.stream().filter(enchantment -> enchantment.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }
}
