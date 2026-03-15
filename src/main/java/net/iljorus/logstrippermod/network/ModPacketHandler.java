package net.iljorus.logstrippermod.network;

import net.iljorus.logstrippermod.LogStripperMod;
import net.iljorus.logstrippermod.network.packet.RedstoneConfigPacket;
import net.iljorus.logstrippermod.network.packet.SideConfigPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

public class ModPacketHandler {
    private static final String PROTOCOL_VERSION = "1";
    private static int MSG_ID = 0;
    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
            ResourceLocation.fromNamespaceAndPath(LogStripperMod.MOD_ID, "main"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );

    public static void register() {
        INSTANCE.registerMessage(MSG_ID++, RedstoneConfigPacket.class,
                RedstoneConfigPacket::encode,
                RedstoneConfigPacket::decode,
                RedstoneConfigPacket::handle);

        INSTANCE.registerMessage(MSG_ID++, SideConfigPacket.class,
                SideConfigPacket::encode,
                SideConfigPacket::decode,
                SideConfigPacket::handle);
    }
}
