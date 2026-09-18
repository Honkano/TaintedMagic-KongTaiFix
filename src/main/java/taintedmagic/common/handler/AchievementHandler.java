package taintedmagic.common.handler;

import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import taintedmagic.common.registry.AchievementRegistry;
import taintedmagic.common.registry.ItemRegistry;

public class AchievementHandler {

    @SubscribeEvent
    public void onItemPickup(EntityItemPickupEvent event) {
        if (event.entityPlayer.worldObj.isRemote) return;

        ItemStack stack = event.item.getEntityItem();
        if (stack != null && stack.getItem() == ItemRegistry.ItemSlashBladePrimal) {
            event.entityPlayer.addStat(AchievementRegistry.PRIMAL_BLADE, 1);
        }
    }
}
