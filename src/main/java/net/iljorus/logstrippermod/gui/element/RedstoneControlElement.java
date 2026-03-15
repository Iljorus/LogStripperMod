package net.iljorus.logstrippermod.gui.element;

import com.mojang.blaze3d.platform.InputConstants;
import net.iljorus.logstrippermod.block.entity.config.RedstoneConfig;
import net.iljorus.logstrippermod.block.entity.config.SideConfig;
import net.iljorus.logstrippermod.gui.LogStripperMenu;
import net.iljorus.logstrippermod.network.ModPacketHandler;
import net.iljorus.logstrippermod.network.packet.RedstoneConfigPacket;
import net.iljorus.logstrippermod.network.packet.SideConfigPacket;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;

import static java.lang.String.format;

public class RedstoneControlElement extends ElementBase {
    private final LogStripperMenu menu;

    public RedstoneControlElement(int posX, int posY, int width, int height, LogStripperMenu menu) {
        super(posX, posY, width, height);
        this.menu = menu;
    }

    @Override
    public Component getTooltip() {
        RedstoneConfig config = menu.getRedstoneConfig();
        return Component.literal(format("Redstone Control: %s", config.asString()));
    }

    @Override
    public boolean mouseClicked(double x, double y, int button) {
        if (InputConstants.MOUSE_BUTTON_LEFT == button) {
            RedstoneConfig nextConfig = this.menu.getRedstoneConfig().next();
            ModPacketHandler.INSTANCE.sendToServer(new RedstoneConfigPacket(this.menu.blockEntity.getBlockPos(), nextConfig));
            //TODO
            ModPacketHandler.INSTANCE.sendToServer(new SideConfigPacket(this.menu.blockEntity.getBlockPos(), Direction.EAST.get3DDataValue(), SideConfig.Action.PULL.getIntValue(), 0));
            return true;
        }
        return false;
    }
}
