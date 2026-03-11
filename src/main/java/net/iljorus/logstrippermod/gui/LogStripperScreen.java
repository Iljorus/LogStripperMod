package net.iljorus.logstrippermod.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import net.iljorus.logstrippermod.LogStripperMod;
import net.iljorus.logstrippermod.config.BaseConfig;
import net.iljorus.logstrippermod.gui.element.DurabilityIndicatorElement;
import net.iljorus.logstrippermod.gui.element.ElementBase;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import static net.iljorus.logstrippermod.gui.GuiConstants.*;

/*
 * Handles Texture & GUI design.
 * */
public class LogStripperScreen extends AbstractContainerScreen<LogStripperMenu> {
    public static final ResourceLocation LOG_STRIPPER_BASE =
            ResourceLocation.fromNamespaceAndPath(LogStripperMod.MOD_ID, "textures/gui/log_stripper_base.png");
    public static final ResourceLocation PROGRESS_ICON =
            ResourceLocation.fromNamespaceAndPath(LogStripperMod.MOD_ID, "textures/gui/scale_saw.png");
    public static final ResourceLocation DURABILITY_INDICATOR =
            ResourceLocation.fromNamespaceAndPath(LogStripperMod.MOD_ID, "textures/gui/durability_indicator.png");
    public static final ResourceLocation AXE_SLOT =
            ResourceLocation.fromNamespaceAndPath(LogStripperMod.MOD_ID, "textures/gui/axe_slot.png");
    public static final ResourceLocation SLOT =
            ResourceLocation.fromNamespaceAndPath(LogStripperMod.MOD_ID, "textures/gui/slot.png");

    private ArrayList<ElementBase> elements = new ArrayList<>();

    public LogStripperScreen(LogStripperMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
    }

    @Override
    protected void init() {
        super.init();
        this.titleLabelX = 31; //35
        elements.add(new DurabilityIndicatorElement(leftPos + 8, topPos + 8, 16, 42, menu));
        elements.add(new RedstoneControlElement(leftPos + 117, topPos + 65, 14, 14, menu));
    }

    @Override
    public void onClose() {
        elements.clear();
        super.onClose();
    }

    /*
     * Sub-Method to render Background, called in super.render()
     * */
    @Override
    protected void renderBg(@NotNull GuiGraphics guiGraphics, float pPartialTick, int pMouseX, int pMouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        int x = leftPos;
        int y = topPos;

        renderBase(guiGraphics, x, y);
        if (BaseConfig.COMMON.AXE_SLOT.get()) {
            renderAxeSlot(guiGraphics, x, y);
            renderAxeDurability(guiGraphics, x, y);
        }

        renderProgressIcon(guiGraphics, x, y);
    }

    /*
     * Renders the Gui
     * */
    @Override
    public void render(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
        renderBackground(guiGraphics);
        super.render(guiGraphics, mouseX, mouseY, delta);
        renderTooltip(guiGraphics, mouseX, mouseY);
        if (this.menu.getCarried().isEmpty()) {
            drawTooltips(guiGraphics, mouseX, mouseY);
        }
    }

    public void renderBase(GuiGraphics guiGraphics, int x, int y) {
        RenderSystem.setShaderTexture(0, LOG_STRIPPER_BASE);
        guiGraphics.blit(LOG_STRIPPER_BASE, x, y, 0, 0, 0, imageWidth, imageHeight, 256, 256);
    }

    private void renderProgressIcon(GuiGraphics guiGraphics, int x, int y) {
        guiGraphics.blit(PROGRESS_ICON, x + 81, y + 34, 1, 0, 0, 16, 16, 256, 256);
        if (menu.isCrafting()) {
            guiGraphics.blit(PROGRESS_ICON, x + 81, y + 34, 2, 16, 0, menu.getScaledProgress(), 16, 256, 256);
        }
    }

    private void renderAxeSlot(GuiGraphics guiGraphics, int x, int y) {
        guiGraphics.blit(AXE_SLOT, x + AXE_SLOT_X - 1, y + AXE_SLOT_Y - 1, 1, 0, 0, 18, 18, 256, 256);

    }

    private void renderAxeDurability(GuiGraphics guiGraphics, int x, int y) {
        int scaledAxeDurability = menu.getScaledAxeDurability();
        guiGraphics.blit(DURABILITY_INDICATOR, x + 8, y + 8, 1, 0, 0, 16, 42, 256, 256);
        guiGraphics.blit(DURABILITY_INDICATOR, x + 8, y + 8 + (42 - scaledAxeDurability), 2, 16, 42 - scaledAxeDurability, 16, scaledAxeDurability, 256, 256);
    }

    private void drawTooltips(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        ElementBase el = getElementAtPosition(mouseX, mouseY);
        if (el == null) {
            return;
        }
        guiGraphics.renderTooltip(font, el.getTooltip(), mouseX, mouseY);
    }

    private ElementBase getElementAtPosition(double mouseX, double mouseY) {
        for (ElementBase el : elements) {
            if (el.isHovered(mouseX, mouseY)) {
                return el;
            }
        }
        return null;
    }

    @Override
    public boolean mouseClicked(double x, double y, int button) {
        ElementBase el = getElementAtPosition(x, y);
        if (el != null && el.mouseClicked(x, y, button)) {
            return true;
        }
        return super.mouseClicked(x, y, button);
    }
}
