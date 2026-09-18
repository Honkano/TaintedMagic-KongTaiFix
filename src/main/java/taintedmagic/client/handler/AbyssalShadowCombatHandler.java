package taintedmagic.client.handler;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import taintedmagic.common.items.equipment.ItemFallenGodShadowArmor;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.common.items.baubles.ItemAmuletVis;
import thaumcraft.common.items.wands.ItemWandCasting;

/**
 * 永夜深渊暗影套 - 战斗事件处理器
 * 负责：被攻击时给背包内所有 Vis 容器充能
 */
public class AbyssalShadowCombatHandler {

    public AbyssalShadowCombatHandler() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onPlayerHurt(LivingHurtEvent event) {
        if (!(event.entityLiving instanceof EntityPlayer)) return;

        EntityPlayer player = (EntityPlayer) event.entityLiving;

        // 仅在服务端执行，避免客户端不同步
        if (player.worldObj.isRemote) return;

        if (!isFullSetEquipped(player)) return;

        // 虚空伤害不触发充能
        if (event.source == DamageSource.outOfWorld) return;

        // 遍历整个背包，给所有 Vis 容器充能
        for (int i = 0; i < player.inventory.getSizeInventory(); i++) {
            ItemStack stack = player.inventory.getStackInSlot(i);
            if (stack == null) continue;

            Item item = stack.getItem();

            // ---------- 法杖 ----------
            if (item instanceof ItemWandCasting) {
                ItemWandCasting wand = (ItemWandCasting) item;
                for (Aspect aspect : Aspect.getPrimalAspects()) {
                    wand.addVis(stack, aspect, 10, true);
                }
            }
            // ---------- 魔力石 ----------
            else if (item instanceof ItemAmuletVis) {
                ItemAmuletVis amulet = (ItemAmuletVis) item;
                for (Aspect aspect : Aspect.getPrimalAspects()) {
                    amulet.storeVis(stack, aspect, 10);
                }
            }
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
}
