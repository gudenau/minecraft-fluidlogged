package net.gudenau.minecraft.fluidlogged.util;

import com.google.common.collect.ImmutableMap;
import net.fabricmc.fabric.api.util.NbtType;
import net.gudenau.minecraft.fluidlogged.mixin.NbtHelperAccessor;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Property;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.Map;

public class FluidNbtHelper{
    public static FluidState toFluidState(CompoundTag tag){
        if(!tag.contains("Name", NbtType.STRING)){
            return Fluids.EMPTY.getDefaultState();
        }else{
            Fluid fluid = Registry.FLUID.get(new Identifier(tag.getString("Name")));
            FluidState state = fluid.getDefaultState();
            if(tag.contains("Properties", NbtType.COMPOUND)){
                CompoundTag propTag = tag.getCompound("Properties");
                StateManager<Fluid, FluidState> stateManager = fluid.getStateManager();

                for(String key : propTag.getKeys()){
                    Property<?> prop = stateManager.getProperty(key);
                    if(prop != null){
                        state = NbtHelperAccessor.invokeWithProperty(state, prop, key, propTag, tag);
                    }
                }
            }
            return state;
        }
    }

    public static CompoundTag fromBlockState(FluidState state){
        CompoundTag compoundTag = new CompoundTag();
        compoundTag.putString("Name", Registry.FLUID.getId(state.getFluid()).toString());
        ImmutableMap<Property<?>, Comparable<?>> immutableMap = state.getEntries();
        if(!immutableMap.isEmpty()){
            CompoundTag propTag = new CompoundTag();

            for(Map.Entry<Property<?>, Comparable<?>> entry : immutableMap.entrySet()){
                Property<?> prop = entry.getKey();
                propTag.putString(prop.getName(), NbtHelperAccessor.invokeNameValue(prop, entry.getValue()));
            }

            compoundTag.put("Properties", propTag);
        }

        return compoundTag;
    }
}
