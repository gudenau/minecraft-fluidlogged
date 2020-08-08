package net.gudenau.minecraft.fluidlogged.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.gudenau.minecraft.fluidlogged.duck.ChunkSectionDuck;
import net.gudenau.minecraft.fluidlogged.util.FluidNbtHelper;
import net.minecraft.block.BlockState;
import net.minecraft.block.FluidFillable;
import net.minecraft.block.SlabBlock;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.chunk.ChunkSection;
import net.minecraft.world.chunk.IdListPalette;
import net.minecraft.world.chunk.Palette;
import net.minecraft.world.chunk.PalettedContainer;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(ChunkSection.class)
public abstract class ChunkSectionMixin implements ChunkSectionDuck{
    @Shadow private short nonEmptyFluidCount;

    @Shadow @Final private PalettedContainer<BlockState> container;
    @Unique private short gud_fluidlogged$randomTickableFluidCount;
    @Unique private static Palette<FluidState> gud_fluidlogged$fluidPalette;
    @Unique private PalettedContainer<FluidState> gud_fluidlogged$fluidContainer;

    @Inject(
        method = "<clinit>()V",
        at = @At("TAIL")
    )
    private static void clinit(CallbackInfo info){
        gud_fluidlogged$fluidPalette = new IdListPalette<>(Fluid.STATE_IDS, Fluids.EMPTY.getDefaultState());
    }

    @Inject(
        method = "<init>(ISSS)V",
        at = @At("TAIL")
    )
    private void init(int yOffset, short nonEmptyBlockCount, short randomTickableBlockCount, short nonEmptyFluidCount, CallbackInfo info){
        gud_fluidlogged$fluidContainer = new PalettedContainer<>(
            gud_fluidlogged$fluidPalette,
            Fluid.STATE_IDS,
            FluidNbtHelper::toFluidState,
            FluidNbtHelper::fromBlockState,
            Fluids.EMPTY.getDefaultState()
        );
    }

    @Inject(
        method = "getFluidState",
        at = @At("HEAD"),
        cancellable = true
    )
    private void getFluidState(int x, int y, int z, CallbackInfoReturnable<FluidState> info){
        FluidState state = gud_fluidlogged$fluidContainer.get(x, y, z);
        if(state.getFluid() == Fluids.WATER && container.get(x, y, z).getBlock() instanceof SlabBlock){
            System.out.print("");
        }
        if(state.isEmpty()){
            state = container.get(x, y, z).getFluidState();
        }
        info.setReturnValue(state);
    }

    @Override
    public FluidState gud_fluidlogged$getStoredFluidState(int x, int y, int z){
        return gud_fluidlogged$fluidContainer.get(x, y, z);
    }

    @Inject(
        method = "lock",
        at = @At("TAIL")
    )
    private void lock(CallbackInfo info){
        gud_fluidlogged$fluidContainer.lock();
    }

    @Inject(
        method = "unlock",
        at = @At("TAIL")
    )
    private void unlock(CallbackInfo info){
        gud_fluidlogged$fluidContainer.unlock();
    }

    @Unique
    @Override
    public FluidState gud_fluidlogged$setFluidState(int x, int y, int z, FluidState newState, boolean lock){
        FluidState oldState;
        if(lock){
            oldState = gud_fluidlogged$fluidContainer.setSync(x, y, z, newState);
        } else {
            oldState = gud_fluidlogged$fluidContainer.set(x, y, z, newState);
        }

        if(!oldState.isEmpty()){
            nonEmptyFluidCount--;
            if(oldState.hasRandomTicks()){
                gud_fluidlogged$randomTickableFluidCount--;
            }
        }

        if(!newState.isEmpty()){
            nonEmptyFluidCount++;
            if(newState.hasRandomTicks()){
                gud_fluidlogged$randomTickableFluidCount++;
            }
        }

        return oldState;
    }

    @Redirect(
        method = "setBlockState(IIILnet/minecraft/block/BlockState;Z)Lnet/minecraft/block/BlockState;",
        at = @At(
            value = "INVOKE",
            opcode = Opcodes.INVOKEVIRTUAL,
            target = "Lnet/minecraft/block/BlockState;getFluidState()Lnet/minecraft/fluid/FluidState;"
        )
    )
    private FluidState setBlockState$nonEmptyFluidCount$set(BlockState state){
        return Fluids.EMPTY.getDefaultState();
    }

    @Inject(
        method = "setBlockState(IIILnet/minecraft/block/BlockState;Z)Lnet/minecraft/block/BlockState;",
        at = @At("TAIL"),
        locals = LocalCapture.CAPTURE_FAILHARD
    )
    private void setBlockState$tail(int x, int y, int z, BlockState newState, boolean lock, CallbackInfoReturnable<BlockState> info, BlockState oldState){
        FluidState newFluidState = newState.getFluidState();

        if(newFluidState.isEmpty() && (newState.isAir() || newState.getBlock() instanceof FluidFillable)){
            return;
        }

        FluidState oldFluidState;
        if(lock){
            oldFluidState = gud_fluidlogged$fluidContainer.setSync(x, y, z, newFluidState);
        }else{
            oldFluidState = gud_fluidlogged$fluidContainer.set(x, y, z, newFluidState);
        }

        if(!oldFluidState.isEmpty()){
            nonEmptyFluidCount--;
            if(oldFluidState.hasRandomTicks()){
                gud_fluidlogged$randomTickableFluidCount--;
            }
        }
        if(!newFluidState.isEmpty()){
            nonEmptyFluidCount++;
            if(newFluidState.hasRandomTicks()){
                gud_fluidlogged$randomTickableFluidCount++;
            }
        }
    }

    @Inject(
        method = "isEmpty()Z",
        at = @At("HEAD"),
        cancellable = true
    )
    private void isEmpty(CallbackInfoReturnable<Boolean> info){
        if(nonEmptyFluidCount == 0){
            info.setReturnValue(false);
        }
    }

    @Inject(
        method = "hasRandomFluidTicks",
        at = @At("HEAD"),
        cancellable = true
    )
    private void hasRandomFluidTicks(CallbackInfoReturnable<Boolean> info){
        info.setReturnValue(gud_fluidlogged$randomTickableFluidCount > 0);
    }

    @Inject(
        method = "calculateCounts",
        at = @At("TAIL")
    )
    private void calculateCounts(CallbackInfo info){
        gud_fluidlogged$fluidContainer.count((state, count)->{
            if(!state.isEmpty()){
                nonEmptyFluidCount += count;
                if(state.hasRandomTicks()){
                    gud_fluidlogged$randomTickableFluidCount += count;
                }
            }
        });
    }

// method_21731(Lnet/minecraft/block/BlockState;I)V
    @Redirect(
        method = "method_21731",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/fluid/FluidState;isEmpty()Z"
        )
    )
    private boolean method_21731$BlockState$getFluidState(FluidState state){
        return true;
    }

    @Unique
    @Override
    public PalettedContainer<FluidState> gud_fluidlogged$getFluidContainer(){
        return gud_fluidlogged$fluidContainer;
    }

    @Inject(
        method = "fromPacket",
        at = @At("TAIL")
    )
    @Environment(EnvType.CLIENT)
    private void fromPacket(PacketByteBuf packetByteBuf, CallbackInfo info){
        nonEmptyFluidCount = packetByteBuf.readShort();
        gud_fluidlogged$fluidContainer.fromPacket(packetByteBuf);
    }

    @Inject(
        method = "toPacket",
        at = @At("TAIL")
    )
    private void toPacket(PacketByteBuf packetByteBuf, CallbackInfo info){
        packetByteBuf.writeShort(nonEmptyFluidCount);
        gud_fluidlogged$fluidContainer.toPacket(packetByteBuf);
    }

    @Inject(
        method = "getPacketSize",
        at = @At("HEAD"),
        cancellable = true
    )
    private void getPacketSize(CallbackInfoReturnable<Integer> info){
        // Would be cool to be able to call "super" here.
        info.setReturnValue(
            4 + container.getPacketSize() + gud_fluidlogged$fluidContainer.getPacketSize()
        );
    }
}
