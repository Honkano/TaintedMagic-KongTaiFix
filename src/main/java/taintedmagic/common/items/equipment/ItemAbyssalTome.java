package taintedmagic.common.items.equipment;

import java.util.List;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

import baubles.api.BaubleType;
import baubles.api.IBauble;
import taintedmagic.common.TaintedMagic;
import thaumcraft.api.IRunicArmor;
import thaumcraft.api.IVisDiscountGear;
import thaumcraft.api.IWarpingGear;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.items.wands.ItemWandCasting;

public class ItemAbyssalTome extends Item implements IBauble, IRunicArmor, IVisDiscountGear, IWarpingGear {

    private static final int RUNIC_SHIELD = 2000;
    private static final int VIS_CAPACITY_PER_ASPECT = 20000;
    private static final int VIS_DISCOUNT = 50;
    private static final int WARP = 50000;

    public ItemAbyssalTome() {
        super();
        this.setMaxStackSize(1);
        this.setCreativeTab(TaintedMagic.tabTM);
        this.setUnlocalizedName("ItemAbyssalTome");
        this.setTextureName("taintedmagic:ItemAbyssalTome");
    }

    // ========== 饰品栏 (IBauble) ==========
    @Override
    public BaubleType getBaubleType(ItemStack itemstack) {
        return BaubleType.AMULET;
    }

    @Override
    public void onWornTick(ItemStack itemstack, EntityLivingBase player) {
        if (player instanceof EntityPlayer) {
            EntityPlayer entityPlayer = (EntityPlayer) player;
            ItemStack heldItem = entityPlayer.getHeldItem();
            if (heldItem != null && heldItem.getItem() instanceof ItemWandCasting) {
                chargeWandFromTome(itemstack, heldItem);
            }
        }
    }

    @Override
    public void onEquipped(ItemStack itemstack, EntityLivingBase player) {}

    @Override
    public void onUnequipped(ItemStack itemstack, EntityLivingBase player) {}

    @Override
    public boolean canEquip(ItemStack itemstack, EntityLivingBase player) {
        return true;
    }

    @Override
    public boolean canUnequip(ItemStack itemstack, EntityLivingBase player) {
        return true;
    }

    // ========== 符文护盾 ==========
    @Override
    public int getRunicCharge(ItemStack itemstack) {
        return RUNIC_SHIELD;
    }

    // ========== Vis折扣 ==========
    @Override
    public int getVisDiscount(ItemStack stack, EntityPlayer player, Aspect aspect) {
        return VIS_DISCOUNT;
    }

    // ========== 扭曲值 ==========
    @Override
    public int getWarp(ItemStack stack, EntityPlayer player) {
        return WARP;
    }

    // ========== 右键：潜行打开魔导书，普通右键装备 ==========
    @Override
    public ItemStack onItemRightClick(ItemStack itemstack, World world, EntityPlayer player) {
        if (player.isSneaking()) {
            // 只在服务端打开 GUI
            if (!world.isRemote) {
                world.playSoundAtEntity(player, "thaumcraft:page", 1.0F, 1.0F);
                player.openGui(Thaumcraft.instance, 12, world, 0, 0, 0);
            }
        }
        // 普通右键：返回 stack，让 Baubles 处理装备
        return itemstack;
    }

    // ========== 物品栏提示 ==========
    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean advanced) {
        list.add(
            EnumChatFormatting.DARK_PURPLE + StatCollector.translateToLocal("tc.visdiscount")
                + ": "
                + VIS_DISCOUNT
                + "%");

        // 显示 Vis 存储（神秘时代会自动渲染，但这里也手动显示一次以便查看）
        AspectList aspects = getAspects(stack);
        if (aspects != null && aspects.size() > 0) {
            list.add(EnumChatFormatting.AQUA + "Vis:");
            for (Aspect aspect : aspects.getAspects()) {
                if (aspects.getAmount(aspect) > 0) {
                    list.add("  " + aspect.getName() + ": " + aspects.getAmount(aspect));
                }
            }
        }

        list.add("");
        list.add(EnumChatFormatting.GRAY + StatCollector.translateToLocal("tm.text.ItemAbyssalTome.lore"));
        list.add(EnumChatFormatting.DARK_GRAY + StatCollector.translateToLocal("tm.text.ItemAbyssalTome.lore.1"));
        list.add(EnumChatFormatting.DARK_GRAY + StatCollector.translateToLocal("tm.text.ItemAbyssalTome.lore.2"));

        super.addInformation(stack, player, list, advanced);
    }

    @Override
    public EnumRarity getRarity(ItemStack stack) {
        return EnumRarity.epic;
    }

    // ========== Vis 存储 ==========
    public AspectList getAspects(ItemStack stack) {
        if (!stack.hasTagCompound() || !stack.getTagCompound()
            .hasKey("Aspects")) {
            return new AspectList();
        }
        AspectList aspects = new AspectList();
        aspects.readFromNBT(
            stack.getTagCompound()
                .getCompoundTag("Aspects"));
        return aspects;
    }

    public void setAspects(ItemStack stack, AspectList aspects) {
        if (!stack.hasTagCompound()) {
            stack.setTagCompound(new NBTTagCompound());
        }
        NBTTagCompound aspectTag = new NBTTagCompound();
        aspects.writeToNBT(aspectTag);
        stack.getTagCompound()
            .setTag("Aspects", aspectTag);
    }

    public int getVis(ItemStack stack, Aspect aspect) {
        return getAspects(stack).getAmount(aspect);
    }

    public void storeVis(ItemStack stack, Aspect aspect, int amount) {
        AspectList aspects = getAspects(stack);
        int current = aspects.getAmount(aspect);
        int newValue = Math.max(0, Math.min(VIS_CAPACITY_PER_ASPECT, current + amount));
        aspects.remove(aspect);
        if (newValue > 0) {
            aspects.add(aspect, newValue);
        }
        setAspects(stack, aspects);
    }

    // ========== 内部辅助 ==========
    private void chargeWandFromTome(ItemStack tomeStack, ItemStack wandStack) {
        ItemWandCasting wand = (ItemWandCasting) wandStack.getItem();
        for (Aspect aspect : Aspect.getPrimalAspects()) {
            int storedVis = getVis(tomeStack, aspect);
            if (storedVis > 0) {
                int maxVis = wand.getMaxVis(wandStack);
                int currentVis = wand.getVis(wandStack, aspect);
                if (currentVis < maxVis) {
                    int amountToTransfer = Math.min(storedVis, maxVis - currentVis);
                    wand.addVis(wandStack, aspect, amountToTransfer, true);
                    storeVis(tomeStack, aspect, -amountToTransfer);
                }
            }
        }
    }
}
