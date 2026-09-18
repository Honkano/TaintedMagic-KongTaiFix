package taintedmagic.client.handler;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ToolModeHUDHandler {

    private static String currentTooltip;
    private static int tooltipDisplayTicks;

    public static void setTooltip(String tooltip) {
        if (!tooltip.equals(currentTooltip)) {
            currentTooltip = tooltip;
            tooltipDisplayTicks = 400;
        }
    }

    @SubscribeEvent
    public void onRenderGameOverlay(RenderGameOverlayEvent.Post event) {
        if (event.type != ElementType.ALL) return;

        // ========== 关键：递减显示时间 ==========
        if (tooltipDisplayTicks > 0) {
            tooltipDisplayTicks--;
        }
        // ======================================

        if (tooltipDisplayTicks <= 0 || currentTooltip == null || currentTooltip.isEmpty()) return;

        Minecraft mc = Minecraft.getMinecraft();
        ScaledResolution resolution = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight);
        int screenWidth = resolution.getScaledWidth();
        int screenHeight = resolution.getScaledHeight();
        FontRenderer fontRenderer = mc.fontRenderer;

        int textWidth = fontRenderer.getStringWidth(currentTooltip);
        int x = (screenWidth - textWidth) / 2;
        int y = screenHeight - 72;

        int opacity = (int) (tooltipDisplayTicks * 256.0F / 10.0F);
        if (opacity > 160) opacity = 160;

        if (opacity > 0) {
            GL11.glPushMatrix();
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            fontRenderer.drawStringWithShadow(currentTooltip, x, y, 0xFF0000 + (opacity << 24));
            GL11.glDisable(GL11.GL_BLEND);
            GL11.glPopMatrix();
        }
    }
}
