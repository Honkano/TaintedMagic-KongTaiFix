package taintedmagic.common.items.equipment;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import taintedmagic.client.handler.AbyssalShadowClientHandler;
import taintedmagic.client.model.ModelWings;

/**
 * 深渊暗影胸甲 - 继承自基础暗影套，增加翅膀模型渲染
 * 翅膀显示条件：开关开启 + 正在飞行
 */
public class ItemAbyssalShadowChestplate extends ItemFallenGodShadowArmor {

    public ItemAbyssalShadowChestplate(ArmorMaterial material, int renderIndex, int armorType) {
        super(material, renderIndex, armorType);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public ModelBiped getArmorModel(EntityLivingBase entityLiving, ItemStack stack, int armorSlot) {
        // 开关开启时显示翅膀，关闭时返回 null
        if (AbyssalShadowClientHandler.isArmorEnabled()) {
            return new ModelWings();
        }
        return null;
    }

    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, int slot, String type) {
        return "taintedmagic:textures/models/ModelFallenGodShadowArmor.png";
    }
}
