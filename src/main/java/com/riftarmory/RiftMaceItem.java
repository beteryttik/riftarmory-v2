package com.riftarmory;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.TntEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.Vec3d;

public class RiftMaceItem extends Item implements RiftArmoryWeapon {
    public RiftMaceItem(Settings settings) {
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
        double cx = target.getX();
        double cy = target.getBodyY(0.5);
        double cz = target.getZ();
        spawnTnt(world, cx, cy, cz);
        double r = 2.0;
        for (int i = 0; i < 6; i++) {
            double a = Math.PI * 2.0 * i / 6.0;
            spawnTnt(world, cx + Math.cos(a) * r, cy, cz + Math.sin(a) * r);
        }
        world.playSound(null, target.getBlockPos(),
                SoundEvents.ENTITY_GENERIC_EXPLODE.value(), SoundCategory.PLAYERS, 1.0f, 0.8f);
    }

    private static void spawnTnt(ServerWorld world, double x, double y, double z) {
        TntEntity tnt = EntityType.TNT.create(world, SpawnReason.TRIGGERED);
        if (tnt == null) {
            return;
        }
        tnt.refreshPositionAfterTeleport(new Vec3d(x, y, z));
        tnt.setFuse((short) 20);
        world.spawnEntity(tnt);
    }
}
