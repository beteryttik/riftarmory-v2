package com.riftarmory;

import net.minecraft.item.Item;
import net.minecraft.item.ToolMaterial;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;

public class ModItems {
    private static Item.Settings settings(String name) {
        return new Item.Settings()
                .registryKey(RegistryKey.of(RegistryKeys.ITEM, Identifier.of(RiftArmory.MOD_ID, name)));
    }

    private static Item register(String name, Item item) {
        return Registry.register(Registries.ITEM, Identifier.of(RiftArmory.MOD_ID, name), item);
    }

    public static final Item ROD_DIAMOND = register("rod_diamond",
            new Item(settings("rod_diamond")));

    // Сердца комплектов: возвращаются при крафте, чтобы одного сердца хватило на булаву и копьё
    public static final Item VOID_FRAGMENT = register("void_fragment",
            new Item(settings("void_fragment")) {
                @Override
                public Item getRecipeRemainder() {
                    return VOID_FRAGMENT;
                }
            });

    public static final Item DRAGON_HEART = register("dragon_heart",
            new Item(settings("dragon_heart")) {
                @Override
                public Item getRecipeRemainder() {
                    return DRAGON_HEART;
                }
            });

    public static final Item GHOST_GLASS = register("ghost_glass",
            new Item(settings("ghost_glass")) {
                @Override
                public Item getRecipeRemainder() {
                    return GHOST_GLASS;
                }
            });

    public static final Item MACE_RIFT = register("mace_rift",
            new RiftMaceItem(settings("mace_rift").sword(ToolMaterial.DIAMOND, 6.0f, -2.8f)));

    public static final Item SPEAR_RIFT = register("spear_rift",
            new RiftSpearItem(settings("spear_rift").sword(ToolMaterial.DIAMOND, 4.0f, -2.2f)));

    public static final Item MACE_DRAGON = register("mace_dragon",
            new DragonMaceItem(settings("mace_dragon").sword(ToolMaterial.DIAMOND, 5.0f, -2.6f)));

    public static final Item SPEAR_DRAGON = register("spear_dragon",
            new DragonSpearItem(settings("spear_dragon").sword(ToolMaterial.DIAMOND, 4.0f, -2.2f)));

    public static final Item MACE_LIGHTNING = register("mace_lightning",
            new LightningMaceItem(settings("mace_lightning").sword(ToolMaterial.DIAMOND, 5.0f, -2.7f)));

    public static final Item MACE_GHOST = register("mace_ghost",
            new GhostMaceItem(settings("mace_ghost").sword(ToolMaterial.DIAMOND, 5.0f, -2.7f)));

    public static final Item SPEAR_GHOST = register("spear_ghost",
            new GhostSpearItem(settings("spear_ghost").sword(ToolMaterial.DIAMOND, 4.0f, -2.4f)));

    public static final Item DESTROY_STICK = register("destroy_stick",
            new DestroyStickItem(settings("destroy_stick").maxCount(1)));

    public static void init() {
    }
}
