package net.gudenau.minecraft.fluidlogged.command;

import com.mojang.brigadier.CommandDispatcher;
import net.gudenau.minecraft.fluidlogged.duck.ChunkSectionDuck;
import net.minecraft.command.arguments.BlockPosArgumentType;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.ChunkSection;
import net.minecraft.world.chunk.WorldChunk;

public class GetFluidStateCommand{
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher){
        dispatcher.register(CommandManager
            .literal("getFluidState").requires((source)->source.hasPermissionLevel(0))
            .then(CommandManager.argument("pos", BlockPosArgumentType.blockPos())
                .executes((context)->execute(context.getSource(), BlockPosArgumentType.getLoadedBlockPos(context, "pos")))
            )
        );
    }

    private static int execute(ServerCommandSource source, BlockPos pos){
        ServerWorld serverWorld = source.getWorld();
        FluidState state = serverWorld.getFluidState(pos);
        FluidState storedState = Fluids.EMPTY.getDefaultState();
        if(!World.isHeightInvalid(pos)){
            WorldChunk chunk = serverWorld.getWorldChunk(pos);
            ChunkSection[] sections = chunk.getSectionArray();
            int y = pos.getY();
            if(y >= 0 && y >> 4 < sections.length) {
                ChunkSection chunkSection = sections[y >> 4];
                if(!ChunkSection.isEmpty(chunkSection)){
                    storedState = ((ChunkSectionDuck)chunkSection).gud_fluidlogged$getStoredFluidState(
                        pos.getX() & 15,
                        pos.getY() & 15,
                        pos.getZ() & 15
                    );
                }
            }

        }
        source.sendFeedback(new TranslatableText(
            "commands.gud_fluidlogged.getfluid.success",
            pos.getX(), pos.getY(), pos.getZ(),
            state.getFluid(),
            state.getLevel(),
            storedState.getFluid(),
            storedState.getLevel()
        ), false);
        return 1;
    }
}
