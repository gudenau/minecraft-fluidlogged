package net.gudenau.minecraft.fluidlogged.mixin;

import net.gudenau.minecraft.fluidlogged.duck.ModifiableWorldDuck;
import net.minecraft.world.ModifiableWorld;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ModifiableWorld.class)
public interface ModifiableWorldMixin extends ModifiableWorldDuck{
}
