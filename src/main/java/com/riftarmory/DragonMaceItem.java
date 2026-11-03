package com.riftarmory;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;

public class DragonMaceItem extends Item implements RiftArmoryWeapon {
    public DragonMaceItem(Settings settings) {
        super(settings);
    }

    @Override
    public void onHit(ServerPlayerEntity player, LivingEntity target, ItemStack stack, float amount) {
        if (!(player.getWorld() instanceof ServerWorld world)) {
            return;
        }
        if (!Stability.tryUse(player, stack)) {
            return;
        }
        target.setOnFireFor(10);
        world.spawnParticles(ParticleTypes.FLAME,
                target.getX(), target.getBodyY(0.5), target.getZ(),
                20, 0.5, 0.5, 0.5, 0.05);
        world.playSound(null, target.getBlockPos(),
                SoundEvents.ENTITY_BLAZE_SHOOT.value(), SoundCategory.PLAYERS, 1.0f, 0.8f);
    }
}
