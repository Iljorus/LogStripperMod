package net.iljorus.logstrippermod.network.packet;

import net.iljorus.logstrippermod.block.entity.config.RedstoneConfig;
import net.iljorus.logstrippermod.gui.LogStripperMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public record RedstoneConfigPacket(BlockPos blockPos, int payload) {

    public RedstoneConfigPacket(BlockPos pos, RedstoneConfig config) {
        this(pos, config.getIntValue());
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeBlockPos(blockPos);
        buf.writeInt(payload);
    }

    public static RedstoneConfigPacket decode(FriendlyByteBuf buf) {
        return new RedstoneConfigPacket(buf.readBlockPos(), buf.readInt());
    }

    public static void handle(RedstoneConfigPacket packet, Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            RedstoneConfig config = RedstoneConfig.fromIntValue(packet.payload());
            ServerPlayer sender = context.get().getSender();
            ((LogStripperMenu) sender.containerMenu).setRedstoneConfig(config);
        });
        context.get().setPacketHandled(true);
    }
}
