package net.gudenau.minecraft.fluidlogged.mixin;

import net.fabricmc.fabric.api.util.NbtType;
import net.gudenau.minecraft.fluidlogged.duck.ChunkSectionDuck;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.structure.StructureManager;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.ChunkSerializer;
import net.minecraft.world.ChunkTickScheduler;
import net.minecraft.world.biome.source.BiomeArray;
import net.minecraft.world.biome.source.BiomeSource;
import net.minecraft.world.chunk.*;
import net.minecraft.world.chunk.light.LightingProvider;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.poi.PointOfInterestStorage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(ChunkSerializer.class)
public abstract class ChunkSerializerMixin{
    @Inject(
        method = "deserialize",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/chunk/ChunkSection;calculateCounts()V"
        ),
        locals = LocalCapture.CAPTURE_FAILHARD
    )
    private static void deserialize(
        ServerWorld world, StructureManager structureManager, PointOfInterestStorage poiStorage, ChunkPos pos, CompoundTag tag,
        CallbackInfoReturnable<ProtoChunk> info,
        ChunkGenerator p1, BiomeSource p2, CompoundTag p3, BiomeArray p4, UpgradeData p5, ChunkTickScheduler<?> p6, ChunkTickScheduler<?> p7, boolean p8, ListTag p9, int p10, ChunkSection[] p11, boolean p12, ChunkManager p13, LightingProvider p14, int p15, CompoundTag sectionTag, int p17, ChunkSection chunkSection
    ){
        if(sectionTag.contains("FluidPalette", NbtType.LIST) && sectionTag.contains("FluidStates", NbtType.LONG_ARRAY)){
            ((ChunkSectionDuck)chunkSection).gud_fluidlogged$getFluidContainer().read(sectionTag.getList("FluidPalette", NbtType.COMPOUND), sectionTag.getLongArray("FluidStates"));
        }
    }

    @Inject(
        method = "serialize",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/chunk/PalettedContainer;write(Lnet/minecraft/nbt/CompoundTag;Ljava/lang/String;Ljava/lang/String;)V"
        ),
        locals = LocalCapture.CAPTURE_FAILHARD
    )
    private static void serialize(
        ServerWorld world, Chunk chunk,
        CallbackInfoReturnable<CompoundTag> info,
        ChunkPos p1, CompoundTag p2, CompoundTag p3, ChunkSection[] p4, ListTag p5, LightingProvider p6, boolean p7, int p8, int p9, ChunkSection chunkSection, ChunkNibbleArray p11, ChunkNibbleArray p12, CompoundTag sectionTag
    ){
        ((ChunkSectionDuck)chunkSection).gud_fluidlogged$getFluidContainer().write(sectionTag, "FluidPalette", "FluidStates");
    }
}
