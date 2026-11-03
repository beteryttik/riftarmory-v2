package com.riftarmory;

import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

public class GhostMaceItem extends Item implements RiftArmoryWeapon {
    public GhostMaceItem(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult use(World world, PlayerEntity user, Hand hand) {
        if (world instanceof ServerWorld server && user instanceof ServerPlayerEntity player) {
            ItemStack stack = user.getStackInHand(hand);
            if (player.getItemCooldownManager().isCoolingDown(stack)) {
                return ActionResult.FAIL;
            }
            if (!Stability.tryUse(player, stack)) {
                return ActionResult.FAIL;
            }
            stack.set(ModComponents.GHOST_PHASE, 100);
            server.spawnParticles(ParticleTypes.SOUL,
                    player.getX(), player.getBodyY(0.5), player.getZ(),
                    40, 0.5, 0.5, 0.5, 0.05);
            server.playSound(null, player.getBlockPos(),
                    SoundEvents.PARTICLE_SOUL_ESCAPE.value(), SoundCategory.PLAYERS, 1.0f, 0.7f);
            player.getItemCooldownManager().set(stack, 300);
        }
        return ActionResult.SUCCESS;
    }

    @Override
    public void onHit(ServerPlayerEntity player, LivingEntity target, ItemStack stack, float amount) {
        // Пассивка комплекта: урон наносится как обычно
    }
}
