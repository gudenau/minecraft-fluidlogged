package net.gudenau.minecraft.fluidlogged.mixin;

import net.gudenau.minecraft.fluidlogged.duck.ModifiableWorldDuck;
import net.gudenau.minecraft.fluidlogged.duck.ChunkDuck;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.chunk.WorldChunk;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin(World.class)
public abstract class WorldMixin implements WorldAccess, AutoCloseable, ModifiableWorldDuck{
    @Shadow @Final public boolean isClient;

    @Shadow public abstract boolean isDebugWorld();
    @Shadow public abstract WorldChunk getWorldChunk(BlockPos pos);
    @Shadow public abstract FluidState getFluidState(BlockPos pos);

    @Unique
    @Override
    public boolean gud_fluidlogged$setFluidState(BlockPos pos, FluidState state, int flags){
        if(World.isHeightInvalid(pos)){
            return false;
        } else if (!isClient && isDebugWorld()) {
            return false;
        } else {
            WorldChunk worldChunk = getWorldChunk(pos);
            Fluid fluid = state.getFluid();
            FluidState oldState = ((ChunkDuck)worldChunk).gud_fluidlogged$setFluidState(pos, state, (flags & 64) != 0);
            if(oldState == null){
                return false;
            }else{
                FluidState fluidState2 = getFluidState(pos);

                /* TODO Should probably implement this stuff
                if (blockState2 != blockState && (blockState2.getOpacity(this, pos) != blockState.getOpacity(this, pos) || blockState2.getLuminance() != blockState.getLuminance() || blockState2.hasSidedTransparency() || blockState.hasSidedTransparency())) {
                    this.getProfiler().push("queueCheckLight");
                    this.getChunkManager().getLightingProvider().checkBlock(pos);
                    this.getProfiler().pop();
                }
                 */

                if(fluidState2 == state){
                    /* TODO Should implement this too
                    if(oldState != fluidState2){
                        scheduleBlockRerenderIfNeeded(pos, oldState, fluidState2);
                    }

                    if((flags & 2) != 0 && (!this.isClient || (flags & 4) == 0) && (this.isClient || worldChunk.getLevelType() != null && worldChunk.getLevelType().isAfter(ChunkHolder.LevelType.TICKING))) {
                        this.updateListeners(pos, oldState, state, flags);
                    }

                    if ((flags & 1) != 0) {
                        this.updateNeighbors(pos, oldState.getBlock());
                        if (!this.isClient && state.hasComparatorOutput()) {
                            this.updateComparators(pos, fluid);
                        }
                    }

                    if ((flags & 16) == 0 && maxUpdateDepth > 0) {
                        int i = flags & -34;
                        oldState.prepare(this, pos, i, maxUpdateDepth - 1);
                        state.updateNeighbors(this, pos, i, maxUpdateDepth - 1);
                        state.prepare(this, pos, i, maxUpdateDepth - 1);
                    }

                    this.onBlockChanged(pos, oldState, fluidState2);
                     */
                }

                return true;
            }
        }
    }
}
