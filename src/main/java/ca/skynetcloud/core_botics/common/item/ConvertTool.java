package ca.skynetcloud.core_botics.common.item;

import ca.skynetcloud.core_botics.common.init.BlockInit;
import net.minecraft.block.BeaconBlock;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LightningEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ConvertTool extends Item {
    public ConvertTool(Settings settings) {
        super(settings);
    }



    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        World world = context.getWorld();
        BlockPos pos = context.getBlockPos();
        BlockState state = world.getBlockState(pos);

        if (world.isClient) return ActionResult.SUCCESS;

        if (!(state.getBlock() instanceof BeaconBlock)) return ActionResult.PASS;

        if (!isDiamondRing(world, pos)) return ActionResult.PASS;

        LightningEntity lightning = new LightningEntity(EntityType.LIGHTNING_BOLT, world);
        lightning.setPos(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5);
        world.spawnEntity(lightning);

        for (BlockPos ringPos : getDiamondRing(pos)) {
            world.setBlockState(ringPos, net.minecraft.block.Blocks.AIR.getDefaultState());
        }

        world.setBlockState(pos, BlockInit.BIORAY_COLLECTOR_BLOCK.getDefaultState());

        return ActionResult.SUCCESS;
    }


    private static boolean isDiamondRing(World world, BlockPos center) {
        for (BlockPos pos : getDiamondRing(center)) {
            if (!world.getBlockState(pos).isOf(net.minecraft.block.Blocks.DIAMOND_BLOCK)) {
                return false;
            }
        }
        return true;
    }

    private static BlockPos[] getDiamondRing(BlockPos center) {
        return new BlockPos[]{
                center.north(), center.south(), center.east(), center.west(),
                center.north().east(), center.north().west(),
                center.south().east(), center.south().west()
        };
    }
}
