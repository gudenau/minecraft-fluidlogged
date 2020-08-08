package net.gudenau.minecraft.fluidlogged.mixin;

import net.gudenau.minecraft.fluidlogged.duck.ModifiableWorldDuck;
import net.minecraft.block.BlockState;
import net.minecraft.block.FluidDrainable;
import net.minecraft.block.FluidFillable;
import net.minecraft.block.Waterloggable;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.WorldAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(Waterloggable.class)
public interface WaterloggableMixin extends FluidDrainable, FluidFillable{
    /**
     * @author gudenau
     * @reason Mixin limitations
     * */
    @Overwrite
    default boolean canFillWithFluid(BlockView world, BlockPos pos, BlockState state, Fluid fluid){
        FluidState fluidState = world.getFluidState(pos);
        return fluidState.isEmpty() || !fluidState.isStill();
    }

    /**
     * @author gudenau
     * @reason Mixin limitations
     * */
    @Overwrite
    default boolean tryFillWithFluid(WorldAccess world, BlockPos pos, BlockState state, FluidState newFluid){
        FluidState existing = world.getFluidState(pos);
        if(existing.isEmpty() || !existing.isStill()){
            if (!world.isClient()){
                world.setBlockState(pos, state.with(Properties.WATERLOGGED, true), 3);
                ((ModifiableWorldDuck)world).gud_fluidlogged$setFluidState(pos, newFluid, 3);
                world.getFluidTickScheduler().schedule(pos, newFluid.getFluid(), newFluid.getFluid().getTickRate(world));
            }

            return true;
        } else {
            return false;
        }
    }

    /**
     * @author gudenau
     * @reason Mixin limitations
     * */
    @Overwrite
    default Fluid tryDrainFluid(WorldAccess world, BlockPos pos, BlockState state){
        FluidState fluidState = world.getFluidState(pos);
        if(fluidState.isEmpty() || !fluidState.isStill()){
            world.setBlockState(pos, state.with(Properties.WATERLOGGED, false), 3);
            ((ModifiableWorldDuck)world).gud_fluidlogged$setFluidState(pos, Fluids.EMPTY.getDefaultState(), 3);
            return fluidState.getFluid();
        }else{
            return Fluids.EMPTY;
        }
    }
}
