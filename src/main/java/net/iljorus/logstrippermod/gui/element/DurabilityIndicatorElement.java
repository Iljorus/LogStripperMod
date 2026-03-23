package net.iljorus.logstrippermod.gui.element;

import net.iljorus.logstrippermod.gui.LogStripperMenu;
import net.minecraft.network.chat.Component;

import static java.lang.String.format;

public class DurabilityIndicatorElement extends ElementBase {
    protected LogStripperMenu menu;

    public DurabilityIndicatorElement(int posX, int posY, int width, int height, LogStripperMenu menu) {
        super(posX, posY, width, height);
        this.menu = menu;
    }

    @Override
    public Component generateTooltip() {
        if (menu.blockEntity.hasAxePresent()) {
            return Component.literal(format(menu.blockEntity.currentAxeDurability() + " / " + menu.blockEntity.maximumAxeDurability()));
        } else {
            return Component.literal("No Axe");
        }
    }
}
