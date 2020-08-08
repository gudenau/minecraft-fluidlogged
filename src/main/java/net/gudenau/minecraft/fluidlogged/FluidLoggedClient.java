package net.gudenau.minecraft.fluidlogged;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandlerRegistry;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.model.ModelLoader;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.util.Identifier;

import java.util.HashSet;

import static net.gudenau.minecraft.fluidlogged.FluidLogged.MOD_ID;

@Environment(EnvType.CLIENT)
public class FluidLoggedClient implements ClientModInitializer{
    @Override
    public void onInitializeClient(){
        Textures.init();
    }

    public static final class Textures{
        public static final SpriteIdentifier MILK_FLOW = new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, new Identifier(MOD_ID, "block/milk_flow"));
        public static final SpriteIdentifier MILK_OVERLAY = new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, new Identifier(MOD_ID, "block/milk_overlay"));

        private static final Sprite[] MILK_SPRITES = new Sprite[2];

        private static void init(){
            FluidRenderHandlerRegistry.INSTANCE.register(
                FluidLogged.Fluids.FLOWING_MILK,
                (renderView, pos, state)->Textures.MILK_SPRITES
            );
            FluidRenderHandlerRegistry.INSTANCE.register(
                FluidLogged.Fluids.MILK,
                (renderView, pos, state)->Textures.MILK_SPRITES
            );
        }

        public static void registerTextures(HashSet<SpriteIdentifier> identifiers){
            identifiers.add(MILK_FLOW);
            identifiers.add(MILK_OVERLAY);
        }

        public static void onResourceReload(){
            MILK_SPRITES[0] = MinecraftClient.getInstance().getBakedModelManager().getBlockModels().getModel(FluidLogged.Blocks.MILK.getDefaultState()).getSprite();
            MILK_SPRITES[1] = MILK_FLOW.getSprite();
        }
    }
}
