package net.gudenau.minecraft.fluidlogged.duck;

import net.minecraft.fluid.FluidState;
import net.minecraft.util.math.BlockPos;

public interface ModifiableWorldDuck{
    boolean gud_fluidlogged$setFluidState(BlockPos pos, FluidState state, int flags);
}
