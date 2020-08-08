package net.gudenau.minecraft.fluidlogged;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.gudenau.minecraft.fluidlogged.command.GetFluidStateCommand;
import net.gudenau.minecraft.fluidlogged.fluid.MilkFluid;
import net.gudenau.minecraft.fluidlogged.mixin.FluidTagsAccessor;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.FluidBlock;
import net.minecraft.block.Material;
import net.minecraft.fluid.FlowableFluid;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.BucketItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.tag.Tag;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import static net.minecraft.item.Items.BUCKET;

public class FluidLogged implements ModInitializer{
    public static final String MOD_ID = "gud_fluidlogged";

    @Override
    public void onInitialize(){
        Commands.init();
        Fluids.init();
        Blocks.init();
        Items.init();
    }

    public static final class Commands{
        private static void init(){
            CommandRegistrationCallback.EVENT.register((dispatcher, dedicated)->
                GetFluidStateCommand.register(dispatcher)
            );
        }
    }

    public static final class Fluids{
        public static final FlowableFluid FLOWING_MILK = new MilkFluid.Flowing();
        public static final FlowableFluid MILK = new MilkFluid.Still();

        private static void init(){
            register("flowing_milk", FLOWING_MILK);
            register("milk", MILK);
        }

        private static void register(String name, Fluid fluid){
            Registry.register(Registry.FLUID, new Identifier(MOD_ID, name), fluid);
        }
    }

    public static final class Blocks{
        public static final Block MILK = new FluidBlock(Fluids.MILK, AbstractBlock.Settings.of(Material.WATER).noCollision().strength(100.0F).dropsNothing()){};

        private static void init(){
            register("milk", MILK);
        }

        private static void register(String name, Block block){
            Registry.register(Registry.BLOCK, new Identifier(MOD_ID, name), block);
        }
    }

    public static final class Items{
        public static final Item MILK_BUCKET = new BucketItem(Fluids.MILK, new Item.Settings().recipeRemainder(BUCKET).maxCount(1).group(ItemGroup.MISC));

        private static void init(){}
    }

    public static final class Tags{
        public static final Tag.Identified<Fluid> MILK = registerFluid("milk");

        private static Tag.Identified<Fluid> registerFluid(String name){
            return FluidTagsAccessor.getACCESSOR().get(MOD_ID + ":" + name);
        }
    }
}
