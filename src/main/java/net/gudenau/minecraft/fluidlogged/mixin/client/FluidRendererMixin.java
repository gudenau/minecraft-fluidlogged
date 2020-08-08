package net.gudenau.minecraft.fluidlogged.mixin.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.gudenau.minecraft.fluidlogged.FluidLoggedClient;
import net.minecraft.client.render.block.FluidRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(FluidRenderer.class)
public abstract class FluidRendererMixin{
    @Inject(
        method = "onResourceReload",
        at = @At("TAIL")
    )
    private void onResourceReload(CallbackInfo info){
        FluidLoggedClient.Textures.onResourceReload();
    }
}
