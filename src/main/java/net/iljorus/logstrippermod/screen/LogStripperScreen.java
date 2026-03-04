package net.iljorus.logstrippermod.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import net.iljorus.logstrippermod.LogStripperMod;
import net.iljorus.logstrippermod.config.BaseConfig;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

/*
 * Handles Texture & GUI design.
 * */

public class LogStripperScreen extends AbstractContainerScreen<LogStripperMenu> {
    public static final ResourceLocation BASE_NO_AXE =
            ResourceLocation.fromNamespaceAndPath(LogStripperMod.MOD_ID, GuiConstants.BASE_TEXTURE_NO_AXE_LOCATION);
    public static final ResourceLocation BASE_WITH_AXE =
            ResourceLocation.fromNamespaceAndPath(LogStripperMod.MOD_ID, GuiConstants.BASE_TEXTURE_WITH_AXE_LOCATION);
    public static final ResourceLocation PROGRESS_ICON =
            ResourceLocation.fromNamespaceAndPath(LogStripperMod.MOD_ID, "textures/gui/scale_saw.png");
    public static final ResourceLocation DURABILITY_INDICATOR =
            ResourceLocation.fromNamespaceAndPath(LogStripperMod.MOD_ID, "textures/gui/durability_indicator.png");
    public static final ResourceLocation SLOT =
            ResourceLocation.fromNamespaceAndPath(LogStripperMod.MOD_ID, "textures/gui/slot.png");

    public LogStripperScreen(LogStripperMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
    }

    @Override
    protected void init() {
        super.init();
        this.titleLabelX = 31; //35
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float pPartialTick, int pMouseX, int pMouseY) {
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

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
        renderBackground(guiGraphics);
        super.render(guiGraphics, mouseX, mouseY, delta);
        renderTooltip(guiGraphics, mouseX, mouseY);
    }

    private void renderNoAxe(GuiGraphics guiGraphics, int x, int y, float pPartialTick, int pMouseX, int pMouseY) {
        RenderSystem.setShaderTexture(0, BASE_NO_AXE);
        guiGraphics.blit(BASE_NO_AXE, x, y, 0, 0, 0, imageWidth, imageHeight, 256, 256);
    }

    private void renderWithAxe(GuiGraphics guiGraphics, int x, int y, float pPartialTick, int pMouseX, int pMouseY) {
        RenderSystem.setShaderTexture(0, BASE_WITH_AXE);
        guiGraphics.blit(BASE_WITH_AXE, x, y, 0, 0, 0, imageWidth, imageHeight, 256, 256);
        guiGraphics.blit(DURABILITY_INDICATOR, x + 8, y + 8, 1, 0, 0, 16, 42, 256, 256);
    }

    private void renderAxeDurability(GuiGraphics guiGraphics, int x, int y) {
        int scaledAxeDurability = menu.getScaledAxeDurability();
        guiGraphics.blit(DURABILITY_INDICATOR, x + 8, y + 8 + (42 - scaledAxeDurability), 2, 16, 42 - scaledAxeDurability, 16, scaledAxeDurability, 256, 256);
    }
}
