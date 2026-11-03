package com.riftarmory;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Heightmap;

import java.util.Random;

public class ChestSpawner {
    private static int counter = 0;
    private static final Random RANDOM = new Random();

    public static void register() {
        ServerTickEvents.END_SERVER_TICK.register(server -> {
            counter++;
            if (counter < 24000) {
                return;
            }
            counter = 0;
            for (ServerWorld world : server.getWorlds()) {
                if (world.getPlayers().isEmpty()) {
                    continue;
                }
                ServerPlayerEntity player = world.getPlayers().get(RANDOM.nextInt(world.getPlayers().size()));
                int x = player.getBlockX() + RANDOM.nextInt(61) - 30;
                int z = player.getBlockZ() + RANDOM.nextInt(61) - 30;
                int y = world.getTopY(Heightmap.Type.MOTION_BLOCKING, x, z);
                BlockPos pos = new BlockPos(x, y, z);
                if (!world.getBlockState(pos).isAir()) {
                    continue;
                }
                world.setBlockState(pos, Blocks.CHEST.getDefaultState());
                if (world.getBlockEntity(pos) instanceof ChestBlockEntity chest) {
                    ItemStack loot = RANDOM.nextFloat() < 0.7f
                            ? new ItemStack(ModItems.VOID_FRAGMENT)
                            : new ItemStack(ModItems.DRAGON_HEART);
                    chest.setStack(3, loot);
                }
            }
        });
    }
}
