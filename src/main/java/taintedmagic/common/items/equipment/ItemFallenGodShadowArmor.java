package taintedmagic.common.items.equipment;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.common.ISpecialArmor;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import taintedmagic.common.TaintedMagic;
import thaumcraft.api.IGoggles;
import thaumcraft.api.IRunicArmor;
import thaumcraft.api.IVisDiscountGear;
import thaumcraft.api.IWarpingGear;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.nodes.IRevealer;
import thaumcraft.client.fx.ParticleEngine;
import thaumcraft.client.fx.particles.FXWispEG;

/**
 * 永夜深渊暗影套 - 堕神暗影套的终极版本
 */
public class ItemFallenGodShadowArmor extends ItemArmor
    implements ISpecialArmor, IRunicArmor, IVisDiscountGear, IWarpingGear, IGoggles, IRevealer {

    public ItemFallenGodShadowArmor(ArmorMaterial material, int renderIndex, int armorType) {
        super(material, renderIndex, armorType);
        setCreativeTab(TaintedMagic.tabTM);
        setMaxDamage(0);

        String suffix = "";
        switch (armorType) {
            case 0:
                suffix = "Helmet";
                break;
            case 1:
                suffix = "Chestplate";
                break;
            case 2:
                suffix = "Leggings";
                break;
            case 3:
                suffix = "Boots";
                break;
        }
        setUnlocalizedName("ItemFallenGodShadowArmor" + suffix);
        setTextureName("taintedmagic:ItemFallenGodShadowArmor" + suffix);
    }

    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, int slot, String type) {
        if (slot == 2) {
            return "taintedmagic:textures/models/ModelFallenGodShadowArmor_legs.png";
        }
        return "taintedmagic:textures/models/ModelFallenGodShadowArmor.png";
    }

    @Override
    public void setDamage(ItemStack stack, int damage) {
        super.setDamage(stack, 0);
    }

    @Override
    public boolean isDamageable() {
        return false;
    }

    @Override
    public EnumRarity getRarity(ItemStack stack) {
        return EnumRarity.epic;
    }

    // ========== 提示框：Vis 折扣 + 每件独立 lore + 套装通用 lore ==========
    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean b) {
        // 1. Vis 折扣
        list.add(
            EnumChatFormatting.DARK_PURPLE + StatCollector.translateToLocal("tc.visdiscount")
                + ": "
                + getVisDiscount(stack, player, null)
                + "%");

        // 2. 每件装备独立的 lore
        String suffix = "";
        switch (armorType) {
            case 0:
                suffix = "Helmet";
                break;
            case 1:
                suffix = "Chestplate";
                break;
            case 2:
                suffix = "Leggings";
                break;
            case 3:
                suffix = "Boots";
                break;
        }

        String perPieceKey = "tm.text.FallenGodShadow" + suffix + ".lore";
        if (StatCollector.canTranslate(perPieceKey)) {
            list.add("");
            list.add(StatCollector.translateToLocal(perPieceKey));
        }

        // 3. 套装通用 lore（多行，四件都显示）
        String[] setLoreKeys = { "tm.text.FallenGodShadowSet.lore", "tm.text.FallenGodShadowSet.lore.1",
            "tm.text.FallenGodShadowSet.lore.2", "tm.text.FallenGodShadowSet.lore.3",
            "tm.text.FallenGodShadowSet.lore.4", "tm.text.FallenGodShadowSet.lore.5",
            "tm.text.FallenGodShadowSet.lore.6" };
        boolean hasAny = false;
        for (String key : setLoreKeys) {
            if (StatCollector.canTranslate(key)) {
                if (!hasAny) {
                    list.add("");
                    hasAny = true;
                }
                list.add(StatCollector.translateToLocal(key));
            }
        }

        super.addInformation(stack, player, list, b);
    }

    // ========== 神秘时代API：Vis折扣 ==========
    @Override
    public int getVisDiscount(ItemStack stack, EntityPlayer player, Aspect aspect) {
        return 60;
    }

    // ========== 神秘时代API：扭曲值 ==========
    @Override
    public int getWarp(ItemStack stack, EntityPlayer player) {
        return 2000;
    }

    // ========== 神秘时代API：符文护盾 ==========
    @Override
    public int getRunicCharge(ItemStack stack) {
        return 1000;
    }

    // ========== 神秘时代API：护目镜功能（仅头盔） ==========
    @Override
    public boolean showIngamePopups(ItemStack itemStack, EntityLivingBase entityLivingBase) {
        return this.armorType == 0;
    }

    @Override
    public boolean showNodes(ItemStack itemStack, EntityLivingBase entityLivingBase) {
        return this.armorType == 0;
    }

    // ========== 特殊护甲：套装减伤效果 ==========
    @Override
    public ArmorProperties getProperties(EntityLivingBase player, ItemStack stack, DamageSource source, double damage,
        int slot) {
        double ratio = damageReduceAmount / 85.0D;

        if (source.isMagicDamage()) {
            ratio = damageReduceAmount / 70.0D;
        } else if (source.isFireDamage() || source.isExplosion()) {
            ratio = damageReduceAmount / 100.0D;
        } else if (source.isUnblockable()) {
            ratio = 80.0D;
        }

        if (player instanceof EntityPlayer) {
            EntityPlayer entityPlayer = (EntityPlayer) player;
            double set = 7.5D;

            for (int a = 0; a < 4; a++) {
                ItemStack piece = entityPlayer.inventory.armorInventory[a];
                if (piece != null && piece.getItem() instanceof ItemFallenGodShadowArmor) {
                    set += 1.5D;
                    if (piece.hasTagCompound() && piece.stackTagCompound.hasKey("mask")) {
                        set += 0.5D;
                    }
                }
            }

            ratio *= set;
        }

        if (ratio > 0.99D) {
            ratio = 0.99D;
        }

        return new ArmorProperties(1, ratio, Integer.MAX_VALUE);
    }

    @Override
    public int getArmorDisplay(EntityPlayer player, ItemStack stack, int slot) {
        return damageReduceAmount;
    }

    @Override
    public void damageArmor(EntityLivingBase entity, ItemStack stack, DamageSource source, int damage, int slot) {}

    // ========== 靴子：黑色粒子特效 ==========
    @Override
    public void onArmorTick(World world, EntityPlayer player, ItemStack itemStack) {
        super.onArmorTick(world, player, itemStack);

        if (this.armorType != 3) return;

        double motion = Math.abs(player.motionX) + Math.abs(player.motionZ) + Math.abs(0.5 * player.motionY);

        if (world.isRemote && (motion > 0.1D || !player.onGround)) {
            int count = 5 + world.rand.nextInt(4);
            for (int i = 0; i < count; i++) {
                spawnParticle(world, player);
            }
        }
    }

    @SideOnly(Side.CLIENT)
    private void spawnParticle(World world, EntityPlayer player) {
        double x = player.posX + (world.rand.nextDouble() - 0.5) * 0.5;
        double y = player.boundingBox.minY + 0.05 + (world.rand.nextDouble() - 0.5) * 0.15;
        double z = player.posZ + (world.rand.nextDouble() - 0.5) * 0.5;

        FXWispEG fx = new FXWispEG(world, x, y, z, player);
        ParticleEngine.instance.addEffect(world, fx);
    }
}
