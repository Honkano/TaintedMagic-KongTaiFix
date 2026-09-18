package taintedmagic.common.items.equipment;

import java.util.List;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.ISpecialArmor;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import taintedmagic.common.TaintedMagic;
import taintedmagic.common.registry.ItemRegistry;
import thaumcraft.api.IGoggles;
import thaumcraft.api.IRunicArmor;
import thaumcraft.api.IVisDiscountGear;
import thaumcraft.api.IWarpingGear;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.nodes.IRevealer;
import thaumcraft.common.items.armor.ItemFortressArmor;

// 实现了 IGoggles 和 IRevealer，为了给头盔加魔导透镜效果
public class ItemGoldenShadowFortressArmor extends ItemFortressArmor
    implements IWarpingGear, IVisDiscountGear, ISpecialArmor, IRunicArmor, IGoggles, IRevealer {

    public ItemGoldenShadowFortressArmor(final ArmorMaterial material, final int j, final int k) {
        super(material, j, k);
        setCreativeTab(TaintedMagic.tabTM);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(final IIconRegister ir) {
        iconHelm = ir.registerIcon("taintedmagic:ItemGoldenShadowFortressHelmet");
        iconChest = ir.registerIcon("taintedmagic:ItemGoldenShadowFortressChestplate");
        iconLegs = ir.registerIcon("taintedmagic:ItemGoldenShadowFortressLeggings");
    }

    @Override
    public void addInformation(final ItemStack stack, final EntityPlayer player, final List list, final boolean b) {
        // =================【标签显示区】=================
        // 这里就是你领悟的：必须自己写，才会显示出来！
        list.add(
            EnumChatFormatting.GOLD + StatCollector.translateToLocal("tc.visdiscount")
                + ": "
                + getVisDiscount(stack, player, null)
                + "%");

        // 头盔额外显示透镜提示（黄金套专属修复了暗影套的盲区）
        if (this.armorType == 0) {
            list.add("");
            list.add(StatCollector.translateToLocal("tm.text.GoldenShadowFortressHelmet.lore"));
            list.add("");
        }
        // ================================================
        super.addInformation(stack, player, list, b);
    }

    @Override
    public String getArmorTexture(final ItemStack stack, final Entity entity, final int slot, final String type) {
        return "taintedmagic:textures/models/ModelGoldenShadowFortressArmor.png";
    }

    @Override
    public EnumRarity getRarity(final ItemStack stack) {
        return EnumRarity.epic;
    }

    @Override
    public boolean getIsRepairable(final ItemStack stack, final ItemStack repairItem) {
        return repairItem.isItemEqual(new ItemStack(ItemRegistry.ItemMaterial, 1, 0)) ? true
            : super.getIsRepairable(stack, repairItem);
    }

    @Override
    public int getVisDiscount(final ItemStack stack, final EntityPlayer player, final Aspect aspect) {
        return 50; // 黄金版魔力减免
    }

    @Override
    public int getWarp(final ItemStack stack, final EntityPlayer player) {
        return 20; // 黄金版扭曲值
    }

    // =================【符文护盾生效区】=================
    // 每件提供 25 点护盾，四件套就是 100 点。
    // 想调护盾高低，改这里的数字即可。
    @Override
    public int getRunicCharge(ItemStack stack) {
        return 25;
    }

    // =================【魔导透镜生效区（仅限头盔）】=================
    @Override
    public boolean showIngamePopups(ItemStack itemStack, EntityLivingBase entityLivingBase) {
        return this.armorType == 0; // 只有头盔(true)才显示要素
    }

    @Override
    public boolean showNodes(ItemStack itemStack, EntityLivingBase entityLivingBase) {
        return this.armorType == 0; // 只有头盔(true)才看得到节点
    }

    // =================【抗伤害与护甲值调整区】=================
    @Override
    public ISpecialArmor.ArmorProperties getProperties(final EntityLivingBase entity, final ItemStack stack,
        final DamageSource source, final double dmg, final int slot) {
        int priority = 0;

        // 【调整防御 - 基础减伤】
        // 数字越小，基础减伤越高！原版要塞套是 /25.0D
        double ratio = damageReduceAmount / 45.0D;

        // 【调整防御 - 针对特定伤害的减伤】
        if (source.isMagicDamage()) {
            priority = 1;
            ratio = damageReduceAmount / 65.0D; // 魔法伤害减免
        } else if (source.isFireDamage() || source.isExplosion()) {
            priority = 1;
            ratio = damageReduceAmount / 50.0D; // 火焰/爆炸伤害减免
        } else if (source.isUnblockable()) {
            priority = 0;
            ratio = 0.0D; // 无法格挡的伤害（如虚空伤害）
        }

        if (entity instanceof EntityPlayer) {
            // 【调整防御 - 套装倍率】
            double set = 0.750D; // 基础倍率
            for (int a = 1; a < 4; a++) {
                final ItemStack piece = ((EntityPlayer) entity).inventory.armorInventory[a];
                if (piece != null && piece.getItem() instanceof ItemFortressArmor) {
                    set += 0.150D; // 每多穿一件黄金套（或任何要塞套）增加的倍率
                    if (piece.hasTagCompound() && piece.stackTagCompound.hasKey("mask")) {
                        set += 0.05D; // 加上面具的额外加成
                    }
                }
            }
            // 【核心翻倍代码】这里原版是 ratio *= set; 你改成了乘以 8.0D
            ratio *= set * 8.0D;
        }

        // 【防止无敌】强制封顶 99% 减伤，否则 100% 减伤会导致游戏崩溃或被服务器踢出！
        if (ratio > 0.99D) {
            ratio = 0.99D;
        }

        return new ISpecialArmor.ArmorProperties(priority, ratio, stack.getMaxDamage() + 1 - stack.getItemDamage());
    }

    @Override
    public int getArmorDisplay(final EntityPlayer player, final ItemStack stack, final int slot) {
        return damageReduceAmount;
    }

    @Override
    public void damageArmor(final EntityLivingBase entity, final ItemStack stack, final DamageSource source,
        final int dmg, final int slot) {
        if (source != DamageSource.fall) {
            stack.damageItem(dmg, entity);
        }
    }
}
