package net.iljorus.logstrippermod.network.packet;

import net.iljorus.logstrippermod.gui.LogStripperMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public record SideConfigPacket(BlockPos blockPos, int direction, int action, int slotIndex) {

    public void encode(FriendlyByteBuf buf) {
        buf.writeBlockPos(blockPos);
        buf.writeInt(direction);
        buf.writeInt(action);
        buf.writeInt(slotIndex);
    }

    public static SideConfigPacket decode(FriendlyByteBuf buf) {
        return new SideConfigPacket(buf.readBlockPos(), buf.readInt(), buf.readInt(), buf.readInt());
    }

    public static void handle(SideConfigPacket packet, Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            ServerPlayer sender = context.get().getSender();
            ((LogStripperMenu) sender.containerMenu).setSideConfig(packet.slotIndex, packet.direction, packet.action);
        });
    }
}
