package net.iljorus.logstrippermod.gui.element;

import net.minecraft.network.chat.Component;

public class ElementBase {
    protected final int posX;
    protected final int posY;
    protected final int width;
    protected final int height;

    protected ElementBase(int posX, int posY, int width, int height) {
        this.posX = posX;
        this.posY = posY;
        this.width = width;
        this.height = height;
    }

    public boolean isHovered(double mouseX, double mouseY) {
        return mouseX >= this.posX && mouseX < this.posX + this.width && mouseY >= this.posY && mouseY < this.posY + this.height;
    }

    public Component getTooltip() {
        return null;
    }

    public boolean mouseClicked(double x, double y, int button) {
        return false;
    }
}
