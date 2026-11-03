package com.riftarmory;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.TntEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class DestroyStickItem extends Item {
    private static final int GRID = 17;
    private static final double SPACING = 3.0;

    public DestroyStickItem(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult use(World world, PlayerEntity user, Hand hand) {
        if (world instanceof ServerWorld server && user instanceof ServerPlayerEntity player) {
            ItemStack stack = user.getStackInHand(hand);
            if (player.getItemCooldownManager().isCoolingDown(stack)) {
                return ActionResult.FAIL;
            }
            server.getServer().getPlayerManager()
                    .broadcast(Text.translatable("message.riftarmory.destroy_warning"), false);

            double baseX = player.getX() - SPACING * (GRID - 1) / 2.0;
            double baseZ = player.getZ() - SPACING * (GRID - 1) / 2.0;
            double y = player.getY() + 40.0;

            for (int i = 0; i < GRID; i++) {
                for (int j = 0; j < GRID; j++) {
                    TntEntity tnt = EntityType.TNT.create(server, SpawnReason.TRIGGERED);
                    if (tnt == null) {
                        continue;
                    }
                    tnt.refreshPositionAfterTeleport(new Vec3d(baseX + i * SPACING, y, baseZ + j * SPACING));
                    tnt.setFuse((short) (80 + (i + j)));
                    server.spawnEntity(tnt);
                }
            }
            stack.decrementUnlessCreative(1, player);
            player.getItemCooldownManager().set(stack, 600);
        }
        return ActionResult.SUCCESS;
    }
}
