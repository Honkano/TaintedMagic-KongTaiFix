package taintedmagic.common.items.tools;

import java.util.List;
import java.util.Random;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

import mods.flammpfeil.slashblade.ItemSlashBlade;
import mods.flammpfeil.slashblade.ItemSlashBladeNamed;
import taintedmagic.common.TaintedMagic;
import taintedmagic.common.items.tools.sa.SAPrimalStorm;
import taintedmagic.common.registry.ItemRegistry;
import thaumcraft.api.IWarpingGear;

public class ItemSlashBladePrimal extends ItemSlashBladeNamed implements IWarpingGear {

    private static final Random rand = new Random();

    // 蓄力满多少 tick 触发 SA（40 tick = 2 秒）
    private static final int SA_CHARGE_MAX = 10;

    public ItemSlashBladePrimal(ToolMaterial material, float attack) {
        super(material, attack);
        this.setUnlocalizedName("primal_blade");
        this.setTextureName("taintedmagic:primal_blade");
        this.setCreativeTab(TaintedMagic.tabTM);
    }

    // ================== 物品显示名 ==================
    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        return StatCollector.translateToLocal("item.taintedmagic.primal_blade.name");
    }

    // ================== 扭曲值 ==================
    @Override
    public int getWarp(ItemStack stack, EntityPlayer player) {
        return 200;
    }

    // ================== 静态工厂方法：唯一出厂模板 ==================
    // 所有需要"一把初始元始妖刃"的地方（创造栏、研究图标、注魔输出）都调这个方法
    public static ItemStack createPrimalCursedBlade() {
        ItemStack stack = new ItemStack(ItemRegistry.ItemSlashBladePrimal);
        NBTTagCompound tag = new NBTTagCompound();
        stack.setTagCompound(tag);

        // ---- 物品显示名（拔刀剑内部用） ----
        ItemSlashBladeNamed.CurrentItemName.set(tag, "taintedmagic.primal_blade");

        // ---- 渲染路径 ----
        ItemSlashBlade.TextureName.set(tag, "primal/primordialedge");
        ItemSlashBlade.ModelName.set(tag, "primal/primordialedge");

        // ---- 基础属性 ----
        // ---- SA 类型（本体系统用，不影响我们自己的元始天劫触发） ----
        ItemSlashBlade.SpecialAttackType.set(tag, 6);
        ItemSlashBlade.StandbyRenderType.set(tag, 3);
        ItemSlashBladeNamed.CustomMaxDamage.set(tag, 30000);
        ItemSlashBlade.setBaseAttackModifier(tag, 15.0F);
        ItemSlashBlade.AttackAmplifier.set(tag, 6.0F);
        ItemSlashBlade.IsDestructable.set(tag, true);

        // ---- 妖刀状态 ----
        ItemSlashBladeNamed.IsDefaultBewitched.set(tag, true);

        // ---- 幻影剑与挥刀光效颜色：红色 ----
        ItemSlashBlade.SummonedSwordColor.set(tag, 0xFF0000);

        // ---- 默认附魔：力量X + 荆棘X ----
        stack.addEnchantment(Enchantment.power, 10);
        stack.addEnchantment(Enchantment.thorns, 10);

        // ---- SE 标签 ----
        NBTTagCompound seTag = new NBTTagCompound();
        seTag.setInteger("PrimalWeaken", 1);
        seTag.setInteger("PrimalStorm", 1);
        tag.setTag("SB.SEffect", seTag);

        return stack;
    }

    // ================== 创造模式物品栏 ==================
    @Override
    public void getSubItems(Item item, CreativeTabs tab, List list) {
        list.add(createPrimalCursedBlade());
    }

    // ================== 右键动作 ==================
    @Override
    public EnumAction getItemUseAction(ItemStack stack) {
        return EnumAction.block;
    }

    // ================== 右键：蓄力 + AOE 旋风 + 反胃 + 雷击 + SA ==================
    @Override
    public void onUsingTick(ItemStack stack, EntityPlayer player, int count) {
        super.onUsingTick(stack, player, count);

        NBTTagCompound tag = ItemSlashBlade.getItemTagCompound(stack);

        // ========== 蓄力触发 SA：元始天劫 ==========
        int charge = tag.getInteger("PrimalSACharge") + 1;
        tag.setInteger("PrimalSACharge", charge);

        if (charge == SA_CHARGE_MAX) {
            new SAPrimalStorm().doSpacialAttack(stack, player);
        }
        // ==========================================

        // ---- 右键自定义音效 ----
        if (player.ticksExisted % 20 == 0) {
            player.worldObj.playSoundAtEntity(player, "taintedmagic:primal_charge", 1.0F, 1.0F);
        }

        // ---- 原版背景音效 ----
        if (player.ticksExisted % 10 == 0) {
            player.worldObj.playSoundAtEntity(player, "thaumcraft:brain", 0.05F, 0.5F);
        }

        // ---- AOE 旋风逻辑 ----
        List<Entity> ents = player.worldObj.getEntitiesWithinAABB(
            Entity.class,
            AxisAlignedBB
                .getBoundingBox(
                    player.posX,
                    player.posY,
                    player.posZ,
                    player.posX + 1,
                    player.posY + 1,
                    player.posZ + 1)
                .expand(15.0D, 15.0D, 15.0D));

        if (ents != null && ents.size() > 0) {
            for (Entity entity : ents) {
                if (entity != player) {
                    // ---- 反胃效果：15秒 反胃III ----
                    if (entity instanceof EntityLivingBase) {
                        ((EntityLivingBase) entity).addPotionEffect(new PotionEffect(Potion.confusion.id, 300, 2));
                    }

                    // ---- 伤害与击退 ----
                    if (entity.isEntityAlive() && !entity.isEntityInvulnerable()) {
                        if (entity.getDistanceToEntity(player) < 2.0D) {
                            entity.attackEntityFrom(DamageSource.magic, 3.0F);
                        }
                    }
                    double x = (player.posX + 0.5D - entity.posX) / 20.0D;
                    double y = (player.posY + 0.5D - entity.posY) / 20.0D;
                    double z = (player.posZ + 0.5D - entity.posZ) / 20.0D;
                    double vec = Math.sqrt(x * x + y * y + z * z);
                    double vec2 = 1.0D - vec;
                    if (vec2 > 0.0D) {
                        vec2 *= vec2;
                        entity.motionX += x / vec * vec2 * 0.20D;
                        entity.motionY += y / vec * vec2 * 0.30D;
                        entity.motionZ += z / vec * vec2 * 0.20D;
                    }
                }
            }
        }

        // ---- 雷击效果：概率触发（PrimalStorm SE 的被动部分） ----
        NBTTagCompound seTag = tag.getCompoundTag("SB.SEffect");
        if (seTag.hasKey("PrimalStorm")) {
            if (player.ticksExisted % 20 == 0 && rand.nextFloat() < 0.15F) {
                for (Entity entity : ents) {
                    if (entity != player && entity instanceof EntityLivingBase) {
                        EntityLightningBolt bolt = new EntityLightningBolt(
                            player.worldObj,
                            entity.posX,
                            entity.posY,
                            entity.posZ);
                        player.worldObj.spawnEntityInWorld(bolt);
                    }
                }
            }
        }
    }

    // ================== 松开右键：重置蓄力 ==================
    @Override
    public void onPlayerStoppedUsing(ItemStack stack, World world, EntityPlayer player, int timeLeft) {
        NBTTagCompound tag = ItemSlashBlade.getItemTagCompound(stack);
        tag.setInteger("PrimalSACharge", 0);
        super.onPlayerStoppedUsing(stack, world, player, timeLeft);
    }

    // ================== 左键：命中敌人 ==================
    @Override
    public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker) {
        boolean result = super.hitEntity(stack, target, attacker);

        // ---- 左键自定义音效 ----
        attacker.worldObj.playSoundAtEntity(attacker, "taintedmagic:primal_swing", 1.0F, 1.0F);

        // ---- 原始之刃的凋零/虚弱 ----
        target.addPotionEffect(new PotionEffect(Potion.wither.id, 60, 1));
        target.addPotionEffect(new PotionEffect(Potion.weakness.id, 120, 1));

        // ---- 原始削弱 SE：额外附加 ----
        NBTTagCompound tag = ItemSlashBlade.getItemTagCompound(stack);
        NBTTagCompound seTag = tag.getCompoundTag("SB.SEffect");
        if (seTag.hasKey("PrimalWeaken")) {
            target.addPotionEffect(new PotionEffect(Potion.digSlowdown.id, 200, 1));
        }

        return result;
    }

    // ================== 自动修复 ==================
    @Override
    public void onUpdate(ItemStack stack, World world, Entity entity, int slot, boolean isHeld) {
        super.onUpdate(stack, world, entity, slot, isHeld);
        if (!world.isRemote && stack.isItemDamaged() && entity.ticksExisted % 20 == 0) {
            stack.damageItem(-1, (EntityLivingBase) entity);
        }
    }

    // ================== 物品提示信息 ==================
    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean advanced) {
        super.addInformation(stack, player, list, advanced);
        list.add("");
        list.add("§7§o继承自原始之刃的妖刀");
        list.add("");
    }
}
