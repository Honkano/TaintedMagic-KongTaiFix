package taintedmagic.client.handler;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderPlayerEvent;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import taintedmagic.common.items.equipment.ItemFallenGodShadowArmor;
import thaumcraft.client.lib.UtilsFX;

public class ClientEventHandler {

    private static final ResourceLocation DARK_CIRCLE = new ResourceLocation("taintedmagic:textures/misc/Dark.png");

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void onRenderPlayer(RenderPlayerEvent.Post event) {
        EntityPlayer player = event.entityPlayer;

        if (isFullSetEquipped(player)) {
            // ========== 新增：只有开关开启时才渲染圆盘 ==========
            if (AbyssalShadowClientHandler.isArmorEnabled()) {
                renderCircleBehindPlayer(player, event.partialRenderTick);
            }
            // ===================================================
        }
    }

    private boolean isFullSetEquipped(EntityPlayer player) {
        ItemStack helmet = player.getCurrentArmor(3);
        ItemStack chestplate = player.getCurrentArmor(2);
        ItemStack leggings = player.getCurrentArmor(1);
        ItemStack boots = player.getCurrentArmor(0);

        return helmet != null && helmet.getItem() instanceof ItemFallenGodShadowArmor
            && chestplate != null
            && chestplate.getItem() instanceof ItemFallenGodShadowArmor
            && leggings != null
            && leggings.getItem() instanceof ItemFallenGodShadowArmor
            && boots != null
            && boots.getItem() instanceof ItemFallenGodShadowArmor;
    }

    private void renderCircleBehindPlayer(EntityPlayer player, float partialTicks) {
        Tessellator t = Tessellator.instance;

        GL11.glPushMatrix();

        // ========== 关键：先旋转到玩家朝向 ==========
        // 让 Z 轴指向玩家背后的方向
        GL11.glRotatef(180 - player.rotationYaw, 0, 1, 0);

        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        // ========== 位置：和飞天魔符完全一样的计算方式 ==========
        GL11.glTranslated(
            0,
            (player != Minecraft.getMinecraft().thePlayer ? 1.62F : 0F) - player.getDefaultEyeHeight()
                + (player.isSneaking() ? 0.0625 : 0),
            0.8D // 背后距离
        );

        // ========== 竖立（绕 X 轴转 90°） ==========
        GL11.glRotatef(90, 1, 0, 0);

        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240, 240);

        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_CULL_FACE);

        GL11.glShadeModel(GL11.GL_SMOOTH);
        GL11.glColor4f(1F, 1F, 1F, 0.8F);

        GL11.glScalef(1.0F, 1.0F, 1.0F);
        GL11.glRotatef(player.ticksExisted + partialTicks, 0F, 1F, 0F);

        UtilsFX.bindTexture(DARK_CIRCLE);

        t.startDrawingQuads();
        t.addVertexWithUV(-1, 0, -1, 0, 0);
        t.addVertexWithUV(-1, 0, 1, 0, 1);
        t.addVertexWithUV(1, 0, 1, 1, 1);
        t.addVertexWithUV(1, 0, -1, 1, 0);
        t.draw();

        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glShadeModel(GL11.GL_FLAT);
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glDisable(GL11.GL_BLEND);

        GL11.glPopMatrix();
    }
}
