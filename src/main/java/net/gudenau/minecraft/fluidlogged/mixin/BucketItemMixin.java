package net.gudenau.minecraft.fluidlogged.mixin;

import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.BucketItem;
import net.minecraft.item.Item;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.Slice;

@Mixin(BucketItem.class)
public abstract class BucketItemMixin extends Item{
    @Shadow @Final private Fluid fluid;

    @SuppressWarnings("ConstantConditions")
    private BucketItemMixin(){
        super(null);
    }

    @Redirect(
        method = "use",
        at = @At(
            value = "FIELD",
            opcode = Opcodes.GETFIELD,
            target = "Lnet/minecraft/item/BucketItem;fluid:Lnet/minecraft/fluid/Fluid;"
        ),
        slice = @Slice(
            from = @At(
                value = "INVOKE",
                target = "Lnet/minecraft/world/World;getBlockState(Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/block/BlockState;"
            ),
            to = @At(
                value = "INVOKE",
                target = "Lnet/minecraft/item/BucketItem;placeFluid(Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/hit/BlockHitResult;)Z"
            )
        )
    )
    private Fluid use$fluid$get(BucketItem bucket){
        return fluid == Fluids.EMPTY ? fluid : Fluids.WATER;
    }

    @Redirect(
        method = "placeFluid",
        at = @At(
            value = "FIELD",
            opcode = Opcodes.GETFIELD,
            target = "Lnet/minecraft/item/BucketItem;fluid:Lnet/minecraft/fluid/Fluid;",
            ordinal = 0
        ),
        slice = @Slice(
            from = @At(
                value = "INVOKE",
                target = "Lnet/minecraft/world/World;addParticle(Lnet/minecraft/particle/ParticleEffect;DDDDDD)V"
            ),
            to = @At(
                value = "INVOKE",
                target = "Lnet/minecraft/block/FluidFillable;tryFillWithFluid(Lnet/minecraft/world/WorldAccess;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;Lnet/minecraft/fluid/FluidState;)Z"
            )
        )
    )
    private Fluid placeFluid$fluid$get(BucketItem bucket){
        return Fluids.WATER;
    }
}
