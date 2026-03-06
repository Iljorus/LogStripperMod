package net.iljorus.logstrippermod.gui.element;

import net.iljorus.logstrippermod.gui.LogStripperMenu;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

import static java.lang.String.format;

public class DurabilityIndicatorElement extends ElementBase {
    protected LogStripperMenu menu;
    public DurabilityIndicatorElement(int posX, int posY, int width, int height, LogStripperMenu menu) {
        super(posX, posY, width, height);
        this.menu=menu;
    }

    @Override
    public Component getTooltip() {
        ItemStack axe = menu.blockEntity.getAxe();
        int maxDurability = axe.getMaxDamage();
        int curDurability = maxDurability - axe.getDamageValue();
        return axe.isEmpty() ? Component.literal("No Axe") : Component.literal(format(curDurability + " / " + maxDurability));
    }
}
