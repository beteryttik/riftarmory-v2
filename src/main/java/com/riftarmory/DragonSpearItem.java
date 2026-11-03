package com.riftarmory;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class DragonSpearItem extends Item {
    public DragonSpearItem(Settings settings) {
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
            HitResult hit = player.raycast(64.0, 1.0f);
            Vec3d landing = hit.getPos();
            BlockPos landingPos = BlockPos.ofFloored(landing.x, landing.y - 0.1, landing.z);
            BlockState state = world.getBlockState(landingPos);

            if (!state.getFluidState().isEmpty()) {
                player.sendMessage(Text.translatable("message.riftarmory.dash_blocked"), true);
                Stability.set(stack, Stability.get(stack) + 25);
                return ActionResult.FAIL;
            }
            if (landing.y > player.getY() + 8.0) {
                player.sendMessage(Text.translatable("message.riftarmory.dash_too_high"), true);
                Stability.set(stack, Stability.get(stack) + 25);
                return ActionResult.FAIL;
            }

            player.teleport(landing.x, landing.y, landing.z, false);

            for (LivingEntity e : server.getEntitiesByClass(LivingEntity.class,
                    new Box(player.getBlockPos()).expand(3.0),
                    e -> e != player && e.isAlive() && !e.isSpectator())) {
                e.setOnFireFor(8);
            }
            for (int i = 0; i < 24; i++) {
                double a = Math.PI * 2.0 * i / 24.0;
                server.spawnParticles(ParticleTypes.SOUL_FIRE_FLAME,
                        player.getX() + Math.cos(a) * 2.0,
                        player.getY() + 0.2,
                        player.getZ() + Math.sin(a) * 2.0,
                        2, 0.0, 0.05, 0.0, 0.01);
            }
            server.playSound(null, player.getBlockPos(),
                    SoundEvents.ENTITY_BLAZE_SHOOT.value(), SoundCategory.PLAYERS, 1.0f, 1.0f);
            player.getItemCooldownManager().set(stack, 200);
        }
        return ActionResult.SUCCESS;
    }
}
