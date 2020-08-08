package net.gudenau.minecraft.fluidlogged.mixin.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.gudenau.minecraft.fluidlogged.FluidLoggedClient;
import net.minecraft.client.render.model.ModelLoader;
import net.minecraft.client.util.SpriteIdentifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.HashSet;

@Environment(EnvType.CLIENT)
@Mixin(ModelLoader.class)
public abstract class ModelLoaderMixin{
    @Inject(
        method = "method_24150",
        at = @At("TAIL")
    )
    private static void registerTextures(HashSet<SpriteIdentifier> identifiers, CallbackInfo info){
        FluidLoggedClient.Textures.registerTextures(identifiers);
    }
}
