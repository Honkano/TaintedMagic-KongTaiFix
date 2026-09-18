package taintedmagic.client.handler;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;

import baubles.api.BaublesApi;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import taintedmagic.common.items.equipment.ItemAbyssalTome;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.lib.events.EventHandlerRunic;
import thaumcraft.common.lib.network.PacketHandler;
import thaumcraft.common.lib.network.playerdata.PacketRunicCharge;

public class RunicShieldFastCharger {

    public RunicShieldFastCharger() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;
        if (event.player.worldObj.isRemote) return;

        EntityPlayer player = event.player;
        if (!hasTomeEquipped(player)) return;

        EventHandlerRunic handler = Thaumcraft.instance.runicEventHandler;
        int playerId = player.getEntityId();

        Integer[] info = handler.runicInfo.get(playerId);
        if (info == null) return;
        int max = info[0];
        if (max <= 0) return;

        Integer currentObj = handler.runicCharge.get(playerId);
        int current = currentObj == null ? 0 : currentObj;

        if (current >= max) return;

        // 瞬间充满，覆盖原版充能逻辑
        handler.runicCharge.put(playerId, max);
        PacketHandler.INSTANCE.sendTo(new PacketRunicCharge(player, (short) max, max), (EntityPlayerMP) player);
    }

    private boolean hasTomeEquipped(EntityPlayer player) {
        try {
            IInventory baubles = BaublesApi.getBaubles(player);
            if (baubles == null) return false;
            for (int i = 0; i < baubles.getSizeInventory(); i++) {
                ItemStack stack = baubles.getStackInSlot(i);
                if (stack != null && stack.getItem() instanceof ItemAbyssalTome) {
                    return true;
                }
            }
        } catch (Exception e) {}
        return false;
    }
}
