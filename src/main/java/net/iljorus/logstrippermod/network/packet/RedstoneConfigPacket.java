package net.iljorus.logstrippermod.network.packet;

import net.iljorus.logstrippermod.block.entity.LogStripperBlockEntity;
import net.iljorus.logstrippermod.block.entity.config.RedstoneConfig;
import net.iljorus.logstrippermod.gui.LogStripperMenu;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public record RedstoneConfigPacket(int payload, BlockPos blockPos) {

    public RedstoneConfigPacket(RedstoneConfig config, BlockPos pos) {
        this(config.getIntValue(), pos);
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeInt(payload);
        buf.writeBlockPos(blockPos);
    }

    public static RedstoneConfigPacket decode(FriendlyByteBuf buf) {
        return new RedstoneConfigPacket(buf.readInt(), buf.readBlockPos());
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
