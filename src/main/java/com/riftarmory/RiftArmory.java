package com.riftarmory;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class RiftArmory implements ModInitializer {
    public static final String MOD_ID = "riftarmory";

    public static final ItemGroup GROUP = FabricItemGroup.builder()
            .icon(() -> new ItemStack(ModItems.MACE_RIFT))
            .displayName(Text.translatable("itemgroup.riftarmory"))
            .entries((context, entries) -> {
                entries.add(ModItems.ROD_DIAMOND);
                entries.add(ModItems.VOID_FRAGMENT);
                entries.add(ModItems.DRAGON_HEART);
                entries.add(ModItems.GHOST_GLASS);
                entries.add(ModItems.MACE_RIFT);
                entries.add(ModItems.SPEAR_RIFT);
                entries.add(ModItems.MACE_DRAGON);
                entries.add(ModItems.SPEAR_DRAGON);
                entries.add(ModItems.MACE_LIGHTNING);
                entries.add(ModItems.MACE_GHOST);
                entries.add(ModItems.SPEAR_GHOST);
                entries.add(ModItems.DESTROY_STICK);
            })
            .build();

    @Override
    public void onInitialize() {
        ModComponents.init();
        ModItems.init();
        Registry.register(Registries.ITEM_GROUP, Identifier.of(MOD_ID, "main"), GROUP);
        DamageHandler.register();
        ChestSpawner.register();
        StabilityTicker.register();
    }
}
