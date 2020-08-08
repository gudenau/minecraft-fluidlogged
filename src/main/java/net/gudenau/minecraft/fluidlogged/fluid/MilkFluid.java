package net.gudenau.minecraft.fluidlogged.fluid;

import net.gudenau.minecraft.fluidlogged.FluidLogged;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.FluidBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.fluid.FlowableFluid;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.state.StateManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;

public abstract class MilkFluid extends FlowableFluid{
    @Override
    public Fluid getFlowing(){
        return FluidLogged.Fluids.FLOWING_MILK;
    }

    @Override
    public Fluid getStill(){
        return FluidLogged.Fluids.MILK;
    }

    @Override
    public Item getBucketItem(){
        return Items.MILK_BUCKET;
    }

    @Override
    protected boolean isInfinite(){
        return false;
    }

    @Override
    protected void beforeBreakingBlock(WorldAccess world, BlockPos pos, BlockState state){
        BlockEntity blockEntity = state.getBlock().hasBlockEntity() ? world.getBlockEntity(pos) : null;
        Block.dropStacks(state, world.getWorld(), pos, blockEntity);
    }

    @Override
    public int getFlowSpeed(WorldView world){
        return 3;
    }

    @Override
    public BlockState toBlockState(FluidState state) {
        return FluidLogged.Blocks.MILK.getDefaultState().with(FluidBlock.LEVEL, method_15741(state));
    }

    @Override
    public boolean matchesType(Fluid fluid){
        return fluid == FluidLogged.Fluids.MILK || fluid == FluidLogged.Fluids.FLOWING_MILK;
    }

    @Override
    public int getLevelDecreasePerBlock(WorldView world){
        return 1;
    }

    @Override
    public int getTickRate(WorldView world){
        return 12;
    }

    @Override
    public boolean canBeReplacedWith(FluidState state, BlockView world, BlockPos pos, Fluid fluid, Direction direction){
        return false;
    }

    @Override
    protected float getBlastResistance(){
        return 100.0F;
    }

    public static class Flowing extends MilkFluid{
        protected void appendProperties(StateManager.Builder<Fluid, FluidState> builder){
            super.appendProperties(builder);
            builder.add(LEVEL);
        }

        public int getLevel(FluidState state){
            return state.get(LEVEL);
        }

        public boolean isStill(FluidState state){
            return false;
        }
    }

    public static class Still extends MilkFluid{
        public int getLevel(FluidState state){
            return 8;
        }

        public boolean isStill(FluidState state){
            return true;
        }
    }
}
