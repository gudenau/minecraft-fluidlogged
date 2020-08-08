package net.gudenau.minecraft.fluidlogged.mixin;

import net.gudenau.minecraft.fluidlogged.FluidLogged;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Items.class)
public abstract class ItemsMixin{
    @Shadow @Mutable @Final public static Item MILK_BUCKET;

    @Redirect(
        method = "<clinit>",
        at = @At(
            value = "FIELD",
            opcode = Opcodes.PUTSTATIC,
            target = "Lnet/minecraft/item/Items;MILK_BUCKET:Lnet/minecraft/item/Item;"
        )
    )
    private static void setMilkBucket(Item milkBucket){
        MILK_BUCKET = FluidLogged.Items.MILK_BUCKET;
    }
}
