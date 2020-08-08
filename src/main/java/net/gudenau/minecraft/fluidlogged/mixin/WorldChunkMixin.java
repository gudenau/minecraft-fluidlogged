package net.gudenau.minecraft.fluidlogged.mixin;

import net.gudenau.minecraft.fluidlogged.duck.ChunkDuck;
import net.gudenau.minecraft.fluidlogged.duck.ChunkSectionDuck;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkSection;
import net.minecraft.world.chunk.WorldChunk;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import static net.minecraft.world.chunk.WorldChunk.EMPTY_SECTION;

@Mixin(WorldChunk.class)
public abstract class WorldChunkMixin implements Chunk, ChunkDuck{
    @Shadow @Final private ChunkSection[] sections;
    @Shadow @Final private World world;
    @Shadow private volatile boolean shouldSave;

    @Override
    public FluidState gud_fluidlogged$setFluidState(BlockPos pos, FluidState state, boolean moved){
        int x = pos.getX() & 15;
        int y = pos.getY();
        int z = pos.getZ() & 15;
        ChunkSection chunkSection = sections[y >> 4];
        if (chunkSection == EMPTY_SECTION) {
            if(state.isEmpty()){
                return null;
            }

            chunkSection = new ChunkSection(y >> 4 << 4);
            sections[y >> 4] = chunkSection;
        }

        boolean emptySection = chunkSection.isEmpty();
        FluidState fluidState = ((ChunkSectionDuck)chunkSection).gud_fluidlogged$setFluidState(x, y & 15, z, state);
        if(fluidState == state){
            return null;
        }else{
            Fluid newFluid = state.getFluid();
            Fluid oldFluid = fluidState.getFluid();
            // TODO?
//            ((Heightmap)this.heightmaps.get(Heightmap.Type.MOTION_BLOCKING)).trackUpdate(x, y, z, state);
//            ((Heightmap)this.heightmaps.get(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES)).trackUpdate(x, y, z, state);
//            ((Heightmap)this.heightmaps.get(Heightmap.Type.OCEAN_FLOOR)).trackUpdate(x, y, z, state);
//            ((Heightmap)this.heightmaps.get(Heightmap.Type.WORLD_SURFACE)).trackUpdate(x, y, z, state);
            boolean isEmptyNow = chunkSection.isEmpty();
            if(emptySection != isEmptyNow){
                world.getChunkManager().getLightingProvider().updateSectionStatus(pos, isEmptyNow);
            }

            /*TODO?
            if(!world.isClient){
                fluidState.onStateReplaced(this.world, pos, state, moved);
            }
             */

            if(chunkSection.getFluidState(x, y & 15, z).getFluid() != newFluid){
                return null;
            }else{
                /* TODO?
                if(!this.world.isClient){
                    state.onBlockAdded(this.world, pos, fluidState, moved);
                }
                 */

                shouldSave = true;
                return fluidState;
            }
        }
    }
}
