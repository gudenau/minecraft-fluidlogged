package net.gudenau.minecraft.fluidlogged.mixin;

import net.minecraft.fluid.Fluid;
import net.minecraft.tag.FluidTags;
import net.minecraft.tag.GlobalTagAccessor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(FluidTags.class)
public interface FluidTagsAccessor{
    @Accessor static GlobalTagAccessor<Fluid> getACCESSOR(){
        return null;
    }
}
