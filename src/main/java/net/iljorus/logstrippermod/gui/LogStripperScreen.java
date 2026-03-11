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

/*
 * Handles Texture & GUI design.
 * */

public class LogStripperScreen extends AbstractContainerScreen<LogStripperMenu> {
    public static final ResourceLocation BASE_NO_AXE =
            ResourceLocation.fromNamespaceAndPath(LogStripperMod.MOD_ID, "textures/gui/log_stripper_no_axe.png");
    public static final ResourceLocation BASE_WITH_AXE =
            ResourceLocation.fromNamespaceAndPath(LogStripperMod.MOD_ID, "textures/gui/log_stripper_with_axe.png");
    public static final ResourceLocation PROGRESS_ICON =
            ResourceLocation.fromNamespaceAndPath(LogStripperMod.MOD_ID, "textures/gui/scale_saw.png");
    public static final ResourceLocation DURABILITY_INDICATOR =
            ResourceLocation.fromNamespaceAndPath(LogStripperMod.MOD_ID, "textures/gui/durability_indicator.png");
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
    }

    /*
     * Sub-Method to render Background, called in render()
     * */
    @Override
    protected void renderBg(@NotNull GuiGraphics guiGraphics, float pPartialTick, int pMouseX, int pMouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;
        if (BaseConfig.COMMON.AXE_SLOT.get()) {
            renderWithAxe(guiGraphics, x, y, pPartialTick, pMouseX, pMouseY);
            renderAxeDurability(guiGraphics, x, y);
        } else {
            renderNoAxe(guiGraphics, x, y, pPartialTick, pMouseX, pMouseY);
        }
        guiGraphics.blit(PROGRESS_ICON, x + 81, y + 34, 1, 0, 0, 16, 16, 256, 256);
        renderProgressIcon(guiGraphics, x, y);
    }

    private void renderProgressIcon(GuiGraphics guiGraphics, int x, int y) {
        if (menu.isCrafting()) {
            guiGraphics.blit(PROGRESS_ICON, x + 81, y + 34, 2, 16, 0, menu.getScaledProgress(), 16, 256, 256);
        }
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

    private void renderNoAxe(GuiGraphics guiGraphics, int x, int y, float pPartialTick, int pMouseX, int pMouseY) {
        RenderSystem.setShaderTexture(0, BASE_NO_AXE);
        guiGraphics.blit(BASE_NO_AXE, x, y, 0, 0, 0, imageWidth, imageHeight, 256, 256);
    }

    private void renderWithAxe(GuiGraphics guiGraphics, int x, int y, float pPartialTick, int pMouseX, int pMouseY) {
        RenderSystem.setShaderTexture(0, BASE_WITH_AXE);
        guiGraphics.blit(BASE_WITH_AXE, x, y, 0, 0, 0, imageWidth, imageHeight, 256, 256);
        elements.add(new DurabilityIndicatorElement(x + 8, y + 8, 16, 42, menu));
        guiGraphics.blit(DURABILITY_INDICATOR, x + 8, y + 8, 1, 0, 0, 16, 42, 256, 256);
    }

    private void renderAxeDurability(GuiGraphics guiGraphics, int x, int y) {
        int scaledAxeDurability = menu.getScaledAxeDurability();
        guiGraphics.blit(DURABILITY_INDICATOR, x + 8, y + 8 + (42 - scaledAxeDurability), 2, 16, 42 - scaledAxeDurability, 16, scaledAxeDurability, 256, 256);
    }

    private void drawTooltips(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        ElementBase el = getElementAtPosition(mouseX, mouseY);
        if (el == null) {
            return;
        }
        //el.drawTooltip
        guiGraphics.renderTooltip(font, el.getTooltip(), mouseX, mouseY);
    }

    private ElementBase getElementAtPosition(int mouseX, int mouseY) {
        for (ElementBase el : elements) {
            if (el.isHovered(mouseX, mouseY)) {
                return el;
            }
        }
        return null;
    }
}
