package net.iljorus.logstrippermod.gui.element;

import com.mojang.blaze3d.platform.InputConstants;
import net.iljorus.logstrippermod.block.entity.config.SideConfig;
import net.iljorus.logstrippermod.gui.LogStripperMenu;
import net.iljorus.logstrippermod.network.ModPacketHandler;
import net.iljorus.logstrippermod.network.packet.SideConfigPacket;
import net.minecraft.core.Direction;

public class SideConfigControlElement extends ElementBase {
    private LogStripperMenu menu;

    public SideConfigControlElement(int posX, int posY, int width, int height, LogStripperMenu menu) {
        super(posX, posY, width, height);
        this.menu = menu;
    }

    @Override
    public boolean mouseClicked(double x, double y, int button) {
        if (InputConstants.MOUSE_BUTTON_LEFT == button) {
            ModPacketHandler.INSTANCE.sendToServer(new SideConfigPacket(this.menu.blockEntity.getBlockPos(), Direction.EAST.get3DDataValue(), SideConfig.Action.PUSH.getIntValue(), 1));
            return true;
        }
        return false;
    }
}
