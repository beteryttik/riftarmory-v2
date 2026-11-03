package com.riftarmory;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class RiftSpearItem extends Item {
    public RiftSpearItem(Settings settings) {
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
            Vec3d point = hit.getPos();
            server.createExplosion(null, point.x, point.y, point.z, 3.0f, World.ExplosionSourceType.TNT);
            server.playSound(null, player.getBlockPos(),
                    SoundEvents.ENTITY_GENERIC_EXPLODE.value(), SoundCategory.PLAYERS, 1.0f, 1.2f);
            player.getItemCooldownManager().set(stack, 60);
        }
        return ActionResult.SUCCESS;
    }
}
