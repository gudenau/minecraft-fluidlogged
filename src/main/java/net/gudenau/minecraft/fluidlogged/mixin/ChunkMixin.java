package net.gudenau.minecraft.fluidlogged.mixin;

import net.gudenau.minecraft.fluidlogged.duck.ChunkDuck;
import net.minecraft.world.BlockView;
import net.minecraft.world.StructureHolder;
import net.minecraft.world.chunk.Chunk;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Chunk.class)
public interface ChunkMixin extends BlockView, StructureHolder, ChunkDuck{
}
