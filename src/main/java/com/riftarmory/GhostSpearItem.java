package com.riftarmory;

import net.minecraft.entity.LivingEntity;
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
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.List;

public class GhostSpearItem extends Item {
    private static final double RANGE = 24.0;

    public GhostSpearItem(Settings settings) {
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
            Vec3d eye = player.getEyePos();
            Vec3d dir = player.getRotationVec(1.0f);
            for (double d = 1.0; d <= RANGE; d += 0.5) {
                Vec3d p = eye.add(dir.multiply(d));
                server.spawnParticles(ParticleTypes.PORTAL, p.x, p.y, p.z, 1, 0.0, 0.0, 0.0, 0.0);
                Box box = new Box(p.x - 0.75, p.y - 0.75, p.z - 0.75, p.x + 0.75, p.y + 0.75, p.z + 0.75);
                List<LivingEntity> victims = server.getEntitiesByClass(LivingEntity.class, box,
                        e -> e != player && e.isAlive() && !e.isSpectator());
                for (LivingEntity victim : victims) {
                    victim.damage(server, player.damageSources().playerAttack(player), 12.0f);
                    victim.addStatusEffect(new StatusEffectInstance(StatusEffects.DARKNESS, 100, 0, false, false, true));
                }
            }
            server.playSound(null, player.getBlockPos(),
                    SoundEvents.PARTICLE_SOUL_ESCAPE.value(), SoundCategory.PLAYERS, 1.0f, 0.7f);
            player.getItemCooldownManager().set(stack, 100);
        }
        return ActionResult.SUCCESS;
    }
}
