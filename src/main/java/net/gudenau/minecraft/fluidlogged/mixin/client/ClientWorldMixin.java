package net.gudenau.minecraft.fluidlogged.mixin.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.fluid.FluidState;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Environment(EnvType.CLIENT)
@Mixin(ClientWorld.class)
public abstract class ClientWorldMixin extends World{
    @SuppressWarnings("ConstantConditions")
    private ClientWorldMixin(){
        super(null, null, null, null, null, false, false, Long.MIN_VALUE);
        throw new RuntimeException("Why would you do this to me?");
    }

    @Redirect(
        method = "addParticle(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;Lnet/minecraft/particle/ParticleEffect;Z)V",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/block/BlockState;getFluidState()Lnet/minecraft/fluid/FluidState;"
        )
    )
    private FluidState addParticle$BlockState$getFluidState(BlockState blockState, BlockPos pos, BlockState blockState2, ParticleEffect parameters, boolean bl){
        return getFluidState(pos);
    }
}
