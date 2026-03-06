package net.iljorus.logstrippermod.gui.element;

import net.minecraft.network.chat.Component;

public class ElementBase {
    protected final int posX;
    protected final int posY;
    protected final int width;
    protected final int height;

    public ElementBase(int posX, int posY, int width, int height) {
        this.posX = posX;
        this.posY = posY;
        this.width = width;
        this.height = height;
    }

    public boolean isHovered(int mouseX, int mouseY) {
        return mouseX >= this.posX && mouseX < this.posX + this.width && mouseY >= this.posY && mouseY < this.posY + this.height;
    }

    public Component getTooltip() {
        return null;
    }

    public void drawBackground(){}

    public void drawForeground(){}

    public void drawTooltip(){}
}
