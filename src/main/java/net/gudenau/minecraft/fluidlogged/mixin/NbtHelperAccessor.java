package net.gudenau.minecraft.fluidlogged.mixin;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.state.State;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(NbtHelper.class)
public interface NbtHelperAccessor{
    @Invoker
    static <S extends State<?, S>, T extends Comparable<T>> S invokeWithProperty(S state, net.minecraft.state.property.Property<T> property, String key, CompoundTag propertiesTag, CompoundTag mainTag){
        return null;
    }

    @Invoker
    static <T extends Comparable<T>> String invokeNameValue(net.minecraft.state.property.Property<T> property, Comparable<?> value){
        return null;
    }
}
