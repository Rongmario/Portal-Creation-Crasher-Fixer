package zone.rong.pccf.mixins;

import net.minecraft.block.AbstractFireBlock;
import net.minecraft.block.Blocks;
import net.minecraft.block.PortalSize;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(AbstractFireBlock.class)
public class AbstractFireBlockMixin {

    @Shadow private static boolean canLightPortal(World world) { return true; }

    /**
     * @author Rongmario
     * @reason Fixes MC-210855
     */
    @Overwrite
    private static boolean shouldLightPortal(World world, BlockPos pos, Direction direction) {
        if (!canLightPortal(world) || direction == Direction.DOWN || direction == Direction.UP) {
            return false;
        } else {
            BlockPos.Mutable mut = pos.toMutable();
            for (Direction d : Direction.values()) {
                if (world.getBlockState(mut.setPos(pos).move(d)).isIn(Blocks.OBSIDIAN)) {
                    return PortalSize.func_242964_a(world, pos, direction.rotateYCCW().getAxis()).isPresent();
                }
            }
        }
        return false;
    }

}
