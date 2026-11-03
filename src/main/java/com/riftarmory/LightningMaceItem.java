package com.riftarmory;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.lightning.LightningEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

import java.util.Comparator;
import java.util.List;

public class LightningMaceItem extends Item implements RiftArmoryWeapon {
    public LightningMaceItem(Settings settings) {
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
        strike(world, target.getX(), target.getY(), target.getZ());

        List<LivingEntity> others = world.getEntitiesByClass(LivingEntity.class,
                new Box(target.getBlockPos()).expand(8.0),
                e -> e != target && e != player && e.isAlive() && !e.isSpectator());
        others.sort(Comparator.comparingDouble(e -> e.squaredDistanceTo(target)));

        int chained = 0;
        for (LivingEntity e : others) {
            if (chained >= 2) {
                break;
            }
            strike(world, e.getX(), e.getY(), e.getZ());
            chained++;
        }

        // В дождь молния бьёт и по владельцу
        if (world.isRaining() && world.isSkyVisible(player.getBlockPos())) {
            strike(world, player.getX(), player.getY(), player.getZ());
        }
    }

    private static void strike(ServerWorld world, double x, double y, double z) {
        LightningEntity bolt = EntityType.LIGHTNING_BOLT.create(world, SpawnReason.TRIGGERED);
        if (bolt == null) {
            return;
        }
        bolt.refreshPositionAfterTeleport(new Vec3d(x, y, z));
        world.spawnEntity(bolt);
    }
}
