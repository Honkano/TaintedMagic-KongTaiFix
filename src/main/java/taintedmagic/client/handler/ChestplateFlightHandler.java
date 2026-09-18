package taintedmagic.client.handler;

import java.util.HashSet;
import java.util.Set;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import taintedmagic.common.items.equipment.ItemAbyssalShadowChestplate;

@SideOnly(Side.CLIENT)
public class ChestplateFlightHandler {

    private static final Set<String> flyingPlayers = new HashSet<String>();

    private static String getPlayerKey(EntityPlayer player) {
        return player.getUniqueID()
            .toString() + ":"
            + player.worldObj.isRemote;
    }

    private static boolean isWearingChestplate(EntityPlayer player) {
        ItemStack chestplate = player.getCurrentArmor(2);
        return chestplate != null && chestplate.getItem() instanceof ItemAbyssalShadowChestplate;
    }

    @SubscribeEvent
    public void onLivingUpdate(LivingUpdateEvent event) {
        if (!(event.entityLiving instanceof EntityPlayer)) {
            return;
        }

        EntityPlayer player = (EntityPlayer) event.entityLiving;
        String key = getPlayerKey(player);

        boolean hasChestplate = isWearingChestplate(player);

        // ========== 关键：开关开启 + 穿着胸甲才能飞行 ==========
        boolean canFly = hasChestplate && AbyssalShadowClientHandler.isArmorEnabled();

        if (canFly) {
            if (!flyingPlayers.contains(key)) {
                flyingPlayers.add(key);
            }
            player.capabilities.allowFlying = true;
        } else {
            if (flyingPlayers.contains(key)) {
                flyingPlayers.remove(key);
                if (!player.capabilities.isCreativeMode) {
                    player.capabilities.allowFlying = false;
                    player.capabilities.isFlying = false;
                }
            }
        }
    }
}
