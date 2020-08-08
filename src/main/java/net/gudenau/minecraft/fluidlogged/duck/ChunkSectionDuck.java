package net.gudenau.minecraft.fluidlogged.duck;

import net.minecraft.fluid.FluidState;
import net.minecraft.world.chunk.PalettedContainer;

public interface ChunkSectionDuck{
    PalettedContainer<FluidState> gud_fluidlogged$getFluidContainer();
    FluidState gud_fluidlogged$getStoredFluidState(int x, int y, int z);
    FluidState gud_fluidlogged$setFluidState(int x, int y, int z, FluidState state, boolean lock);
    default FluidState gud_fluidlogged$setFluidState(int x, int y, int z, FluidState state){
        return gud_fluidlogged$setFluidState(x, y, z, state, true);
    }
}
