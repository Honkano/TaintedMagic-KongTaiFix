package taintedmagic.client.handler;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;

import org.lwjgl.input.Keyboard;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.InputEvent;
import taintedmagic.common.items.equipment.ItemFallenGodShadowArmor;

public class AbyssalShadowKeyHandler {

    public static final KeyBinding toggleAbility = new KeyBinding(
        "key.abyssalshadow.toggle",
        Keyboard.KEY_U,
        "key.categories.taintedmagic");

    public AbyssalShadowKeyHandler() {
        ClientRegistry.registerKeyBinding(toggleAbility);
        FMLCommonHandler.instance()
            .bus()
            .register(this);
    }

    @SubscribeEvent
    public void onKeyInput(InputEvent.KeyInputEvent event) {
        if (toggleAbility.isPressed()) {
            if (isFullSetEquipped()) {
                boolean newStatus = !AbyssalShadowClientHandler.isArmorEnabled();
                AbyssalShadowClientHandler.setArmorEnabled(newStatus);

                if (newStatus) {
                    ToolModeHUDHandler.setTooltip(
                        EnumChatFormatting.RED + StatCollector.translateToLocal("ttmisc.abyssalshadow.enable"));
                } else {
                    ToolModeHUDHandler.setTooltip(
                        EnumChatFormatting.RED + StatCollector.translateToLocal("ttmisc.abyssalshadow.disable"));
                }
            } else {
                ToolModeHUDHandler.setTooltip(
                    EnumChatFormatting.RED + StatCollector.translateToLocal("ttmisc.abyssalshadow.incomplete"));
            }
        }
    }

    private boolean isFullSetEquipped() {
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.thePlayer == null) return false;
        return isFullSetEquipped(mc.thePlayer);
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
