package taintedmagic.client.model;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import taintedmagic.client.handler.AbyssalShadowClientHandler;

public class ModelWings extends ModelBiped {

    // ===== 原有翅膀部件 =====
    ModelRenderer Wing1;
    ModelRenderer Wing2;

    // ===== 新增：新翅膀部件（无尽贪婪风格） =====
    private ModelRenderer darksesLeftWing;
    private ModelRenderer darksesRightWing;

    // ===== 新增：纹理路径 =====
    private static final ResourceLocation WING_TEX = new ResourceLocation(
        "taintedmagic:textures/models/Darkses_wing.png");
    private static final ResourceLocation WING_GLOW_TEX = new ResourceLocation(
        "taintedmagic:textures/models/Darkses_wingglow.png");

    public ModelWings() {
        super(1F);

        // ========== 关键修正：纹理尺寸改为 64x64（与无尽贪婪一致） ==========
        textureWidth = 64;
        textureHeight = 64; // 原来是 32，导致纹理拉伸！
        // ==================================================================

        // ---------- 原有翅膀（保持不变） ----------
        Wing1 = new ModelRenderer(this, 16, -12);
        Wing1.addBox(0F, 0F, 0F, 0, 7, 12);
        Wing1.setRotationPoint(-2F, 1F, 2F);
        setRotation(Wing1, 0F, -0.6108652F, 0F);
        bipedBody.addChild(Wing1);

        Wing2 = new ModelRenderer(this, 16, -12);
        Wing2.addBox(0.1F, 0F, 0F, 0, 7, 12);
        Wing2.setRotationPoint(2F, 1F, 2F);
        setRotation(Wing2, 0F, 0.4468043F, 0F);
        bipedBody.addChild(Wing2);

        // ---------- 新增：新翅膀部件（完全复刻无尽贪婪参数） ----------
        darksesLeftWing = new ModelRenderer(this, 0, 0);
        darksesLeftWing.mirror = true;
        darksesLeftWing.addBox(0F, -11.6F, 0F, 0, 32, 32);
        darksesLeftWing.setRotationPoint(-1.5F, 0.0F, 2.0F);
        darksesLeftWing.rotateAngleY = (float) (Math.PI * 0.4);
        bipedBody.addChild(darksesLeftWing);

        darksesRightWing = new ModelRenderer(this, 0, 0);
        darksesRightWing.addBox(0F, -11.6F, 0F, 0, 32, 32);
        darksesRightWing.setRotationPoint(1.5F, 0.0F, 2.0F);
        darksesRightWing.rotateAngleY = (float) (-Math.PI * 0.4);
        bipedBody.addChild(darksesRightWing);

        // 默认不显示（等待飞行时开启）
        darksesLeftWing.showModel = false;
        darksesRightWing.showModel = false;
    }

    @Override
    public void render(Entity entity, float v1, float v2, float v3, float v4, float v5, float v6) {
        setRotationAngles(v1, v2, v3, v4, v5, v6, entity);

        // 隐藏原版头、腿等，只保留身体（身体上挂载了翅膀）
        bipedHead.showModel = false;
        bipedHeadwear.showModel = false;
        bipedLeftLeg.showModel = false;
        bipedRightLeg.showModel = false;

        // ===== 先渲染原有身体 + 旧翅膀（使用默认纹理） =====
        super.render(entity, v1, v2, v3, v4, v5, v6);

        // ===== 新增：渲染新翅膀（仅在飞行且开关开启时） =====
        if (entity instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) entity;
            // 条件：开关开启 && 正在飞行
            if (AbyssalShadowClientHandler.isArmorEnabled() && player.capabilities.isFlying) {
                // 设置新翅膀可见
                darksesLeftWing.showModel = true;
                darksesRightWing.showModel = true;

                // ---------- 1. 渲染普通纹理 ----------
                Minecraft.getMinecraft().renderEngine.bindTexture(WING_TEX);
                darksesLeftWing.render(v6);
                darksesRightWing.render(v6);

                // ---------- 2. 渲染发光纹理（完全复刻无尽贪婪） ----------
                GL11.glPushMatrix();
                GL11.glDisable(GL11.GL_LIGHTING);
                GL11.glEnable(GL11.GL_BLEND);
                GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
                GL11.glDepthMask(false);

                // 脉动透明度（与无尽贪婪完全一致）
                long time = player.worldObj.getWorldTime();
                double pulse = Math.sin(time / 10.0) * 0.5 + 0.5;
                float alpha = (float) (pulse * pulse * pulse * pulse * pulse * pulse * 0.5);
                GL11.glColor4d(0.84, 1.0, 0.95, alpha);

                Minecraft.getMinecraft().renderEngine.bindTexture(WING_GLOW_TEX);
                darksesLeftWing.render(v6);
                darksesRightWing.render(v6);

                GL11.glDepthMask(true);
                GL11.glDisable(GL11.GL_BLEND);
                GL11.glEnable(GL11.GL_LIGHTING);
                GL11.glPopMatrix();

                // 恢复颜色
                GL11.glColor4d(1, 1, 1, 1);
            } else {
                // 不显示时隐藏
                darksesLeftWing.showModel = false;
                darksesRightWing.showModel = false;
            }
        }
    }

    @Override
    public void setRotationAngles(float v1, float v2, float v3, float v4, float v5, float v6, Entity entity) {
        EntityLivingBase living = (EntityLivingBase) entity;
        isSneak = living != null && living.isSneaking();

        if (living instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) living;

            // 手持物品动画
            ItemStack itemstack = player.inventory.getCurrentItem();
            heldItemRight = itemstack != null ? 1 : 0;
            if (itemstack != null && player.getItemInUseCount() > 0) {
                EnumAction enumaction = itemstack.getItemUseAction();
                if (enumaction == EnumAction.block) heldItemRight = 3;
                else if (enumaction == EnumAction.bow) aimedBow = true;
            }

            // ---- 原有翅膀扇动 ----
            if (player.capabilities.isFlying) {
                Wing1.rotateAngleY = (float) ((Math.sin(entity.ticksExisted) + 1) * (Math.PI / 180F) * 15 - 0.6108652F);
                Wing2.rotateAngleY = -Wing1.rotateAngleY;
            } else {
                Wing1.rotateAngleY = -0.6108652F;
                Wing2.rotateAngleY = 0.4468043F;
            }

            // ---- 新翅膀扇动（与旧翅膀独立） ----
            if (AbyssalShadowClientHandler.isArmorEnabled() && player.capabilities.isFlying) {
                float flap = (float) ((Math.sin(entity.ticksExisted * 0.8) + 1) * 0.3);
                darksesLeftWing.rotateAngleY = (float) (Math.PI * 0.4) + flap;
                darksesRightWing.rotateAngleY = (float) (-Math.PI * 0.4) - flap;
            } else {
                darksesLeftWing.rotateAngleY = (float) (Math.PI * 0.4);
                darksesRightWing.rotateAngleY = (float) (-Math.PI * 0.4);
            }
        }

        super.setRotationAngles(v1, v2, v3, v4, v5, v6, entity);
    }

    private void setRotation(ModelRenderer model, float x, float y, float z) {
        model.rotateAngleX = x;
        model.rotateAngleY = y;
        model.rotateAngleZ = z;
    }
}
