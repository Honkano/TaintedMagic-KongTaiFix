package taintedmagic.common.registry;

import net.minecraft.item.Item;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemSpade;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.util.ResourceLocation;

import cpw.mods.fml.common.registry.GameRegistry;
import taintedmagic.common.handler.WandHandler;
import taintedmagic.common.items.ItemCrimsonBlood;
import taintedmagic.common.items.ItemFlyteCharm;
import taintedmagic.common.items.ItemGateKey;
import taintedmagic.common.items.ItemMagicFunguar;
import taintedmagic.common.items.ItemMaterial;
import taintedmagic.common.items.ItemNightshadeBerries;
import taintedmagic.common.items.ItemSalis;
import taintedmagic.common.items.ItemVoidBlood;
import taintedmagic.common.items.ItemWarpFertilizer;
import taintedmagic.common.items.TMMaterials;
import taintedmagic.common.items.equipment.ItemAbyssalShadowChestplate;
import taintedmagic.common.items.equipment.ItemAbyssalTome;
import taintedmagic.common.items.equipment.ItemFallenGodShadowArmor;
import taintedmagic.common.items.equipment.ItemGoldenShadowFortressArmor;
import taintedmagic.common.items.equipment.ItemLumosRing;
import taintedmagic.common.items.equipment.ItemShadowFortressArmor;
import taintedmagic.common.items.equipment.ItemVoidFortressArmor;
import taintedmagic.common.items.equipment.ItemVoidmetalGoggles;
import taintedmagic.common.items.equipment.ItemVoidwalkerBoots;
import taintedmagic.common.items.equipment.ItemVoidwalkerSash;
import taintedmagic.common.items.equipment.ItemWarpedGoggles;
import taintedmagic.common.items.tools.ItemHollowDagger;
import taintedmagic.common.items.tools.ItemKatana;
import taintedmagic.common.items.tools.ItemPrimalBlade;
import taintedmagic.common.items.tools.ItemShadowmetalAxe;
import taintedmagic.common.items.tools.ItemShadowmetalHoe;
import taintedmagic.common.items.tools.ItemShadowmetalPick;
import taintedmagic.common.items.tools.ItemShadowmetalSpade;
import taintedmagic.common.items.tools.ItemShadowmetalSword;
import taintedmagic.common.items.tools.ItemSlashBladePrimal;
import taintedmagic.common.items.tools.ItemThaumicDisassembler;
import taintedmagic.common.items.wand.ItemWandCap;
import taintedmagic.common.items.wand.ItemWandRod;
import taintedmagic.common.items.wand.foci.ItemFocusDarkMatter;
import taintedmagic.common.items.wand.foci.ItemFocusLumos;
import taintedmagic.common.items.wand.foci.ItemFocusMageMace;
import taintedmagic.common.items.wand.foci.ItemFocusShockwave;
import taintedmagic.common.items.wand.foci.ItemFocusTaintSwarm;
import taintedmagic.common.items.wand.foci.ItemFocusVisShard;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.wands.StaffRod;
import thaumcraft.api.wands.WandCap;
import thaumcraft.api.wands.WandRod;

public class ItemRegistry {

    public static Item ItemMagicFunguar;
    public static Item ItemNightshadeBerries;
    public static Item ItemMaterial;
    public static Item ItemVoidBlood;
    public static Item ItemCrimsonBlood;
    public static Item ItemFlyteCharm;
    public static Item ItemSalis;
    public static Item ItemGateKey;
    public static Item ItemWarpFertilizer;

    // 拔刀
    public static Item ItemSlashBladePrimal;

    // Wand
    public static Item ItemWandRod;

    public static WandRod WAND_ROD_WARPWOOD;
    public static StaffRod STAFF_ROD_WARPWOOD;

    public static Item ItemWandCap;

    public static WandCap WAND_CAP_SHADOWMETAL;
    public static WandCap WAND_CAP_CLOTH;
    public static WandCap WAND_CAP_CRIMSON_CLOTH;
    public static WandCap WAND_CAP_SHADOW_CLOTH;

    // Foci
    public static Item ItemFocusTaintSwarm;
    public static Item ItemFocusDarkMatter;
    public static Item ItemFocusMageMace;
    public static Item ItemFocusShockwave;
    public static Item ItemFocusVisShard;
    public static Item ItemFocusLumos;

    // Armor and Baubles
    public static Item ItemWarpedGoggles;
    public static Item ItemVoidmetalGoggles;

    public static Item ItemVoidwalkerBoots;
    public static Item ItemVoidwalkerSash;

    public static Item ItemVoidFortressHelmet;
    public static Item ItemVoidFortressChestplate;
    public static Item ItemVoidFortressLeggings;

    public static Item ItemShadowFortressHelmet;
    public static Item ItemShadowFortressChestplate;
    public static Item ItemShadowFortressLeggings;

    // ===== 黄金暗影堡垒套 声明 =====
    public static Item ItemGoldenShadowFortressHelmet;
    public static Item ItemGoldenShadowFortressChestplate;
    public static Item ItemGoldenShadowFortressLeggings;

    public static Item ItemLumosRing;

    // ===== 堕神暗影套 声明 =====
    public static Item itemFallenGodShadowHelmet;
    public static Item itemFallenGodShadowChestplate;
    public static Item itemFallenGodShadowLeggings;
    public static Item itemFallenGodShadowBoots;
    // ===========================

    // ===== 永夜深渊魔典 声明 =====
    public static Item ItemAbyssalTome;
    // ==============================

    // Tools
    public static Item ItemThaumicDisassembler;
    public static Item ItemHollowDagger;

    public static ItemHoe ItemShadowmetalHoe;
    public static ItemPickaxe ItemShadowmetalPick;
    public static ItemSpade ItemShadowmetalSpade;
    public static ItemAxe ItemShadowmetalAxe;
    public static ItemSword ItemShadowmetalSword;

    public static ItemSword ItemPrimalBlade;

    public static Item ItemKatana;

    public static void initItems() {

        ItemSlashBladePrimal = new ItemSlashBladePrimal(TMMaterials.TOOL_PRIMAL, 7.0F);
        GameRegistry.registerItem(ItemSlashBladePrimal, "primal_blade");

        ItemMaterial = new ItemMaterial();
        GameRegistry.registerItem(ItemMaterial, "ItemMaterial");

        ItemVoidBlood = new ItemVoidBlood();
        GameRegistry.registerItem(ItemVoidBlood, "ItemVoidBlood");

        ItemCrimsonBlood = new ItemCrimsonBlood();
        GameRegistry.registerItem(ItemCrimsonBlood, "ItemCrimsonBlood");

        ItemMagicFunguar = new ItemMagicFunguar(3, 0.2F, false);
        GameRegistry.registerItem(ItemMagicFunguar, "ItemMagicFunguar");

        ItemNightshadeBerries = new ItemNightshadeBerries(0, 0.0F, false);
        GameRegistry.registerItem(ItemNightshadeBerries, "ItemNightshadeBerries");

        ItemFlyteCharm = new ItemFlyteCharm();
        GameRegistry.registerItem(ItemFlyteCharm, "ItemFlyteCharm");

        ItemSalis = new ItemSalis();
        GameRegistry.registerItem(ItemSalis, "ItemSalis");

        ItemGateKey = new ItemGateKey();
        GameRegistry.registerItem(ItemGateKey, "ItemGateKey");

        ItemWarpFertilizer = new ItemWarpFertilizer();
        GameRegistry.registerItem(ItemWarpFertilizer, "ItemWarpFertilizer");

        // Armor and Baubles
        ItemWarpedGoggles = new ItemWarpedGoggles(TMMaterials.ARMOR_WARPED, 4, 0);
        GameRegistry.registerItem(ItemWarpedGoggles, "ItemWarpedGoggles");

        ItemVoidFortressHelmet = new ItemVoidFortressArmor(TMMaterials.ARMOR_VOIDFORTRESS, 4, 0)
            .setUnlocalizedName("ItemVoidFortressHelmet");
        GameRegistry.registerItem(ItemVoidFortressHelmet, "ItemVoidFortressHelmet");

        ItemVoidFortressChestplate = new ItemVoidFortressArmor(TMMaterials.ARMOR_VOIDFORTRESS, 4, 1)
            .setUnlocalizedName("ItemVoidFortressChestplate");
        GameRegistry.registerItem(ItemVoidFortressChestplate, "ItemVoidFortressChestplate");

        ItemVoidFortressLeggings = new ItemVoidFortressArmor(TMMaterials.ARMOR_VOIDFORTRESS, 4, 2)
            .setUnlocalizedName("ItemVoidFortressLeggings");
        GameRegistry.registerItem(ItemVoidFortressLeggings, "ItemVoidFortressLeggings");

        ItemVoidwalkerBoots = new ItemVoidwalkerBoots(TMMaterials.ARMOR_VOIDWALKER, 4, 3)
            .setUnlocalizedName("ItemVoidwalkerBoots");
        GameRegistry.registerItem(ItemVoidwalkerBoots, "ItemVoidwalkerBoots");

        ItemVoidwalkerSash = new ItemVoidwalkerSash();
        GameRegistry.registerItem(ItemVoidwalkerSash, "ItemVoidwalkerSash");

        ItemShadowFortressHelmet = new ItemShadowFortressArmor(TMMaterials.ARMOR_SHADOWFORTRESS, 4, 0)
            .setUnlocalizedName("ItemShadowFortressHelmet");
        GameRegistry.registerItem(ItemShadowFortressHelmet, "ItemShadowFortressHelmet");

        ItemShadowFortressChestplate = new ItemShadowFortressArmor(TMMaterials.ARMOR_SHADOWFORTRESS, 4, 1)
            .setUnlocalizedName("ItemShadowFortressChestplate");
        GameRegistry.registerItem(ItemShadowFortressChestplate, "ItemShadowFortressChestplate");

        ItemShadowFortressLeggings = new ItemShadowFortressArmor(TMMaterials.ARMOR_SHADOWFORTRESS, 4, 2)
            .setUnlocalizedName("ItemShadowFortressLeggings");
        GameRegistry.registerItem(ItemShadowFortressLeggings, "ItemShadowFortressLeggings");

        // ===== 黄金暗影堡垒套 注册 =====
        ItemGoldenShadowFortressHelmet = new ItemGoldenShadowFortressArmor(TMMaterials.ARMOR_SHADOWFORTRESS, 4, 0)
            .setUnlocalizedName("ItemGoldenShadowFortressHelmet");
        GameRegistry.registerItem(ItemGoldenShadowFortressHelmet, "ItemGoldenShadowFortressHelmet");

        ItemGoldenShadowFortressChestplate = new ItemGoldenShadowFortressArmor(TMMaterials.ARMOR_SHADOWFORTRESS, 4, 1)
            .setUnlocalizedName("ItemGoldenShadowFortressChestplate");
        GameRegistry.registerItem(ItemGoldenShadowFortressChestplate, "ItemGoldenShadowFortressChestplate");

        ItemGoldenShadowFortressLeggings = new ItemGoldenShadowFortressArmor(TMMaterials.ARMOR_SHADOWFORTRESS, 4, 2)
            .setUnlocalizedName("ItemGoldenShadowFortressLeggings");
        GameRegistry.registerItem(ItemGoldenShadowFortressLeggings, "ItemGoldenShadowFortressLeggings");

        // ==============================

        ItemVoidmetalGoggles = new ItemVoidmetalGoggles(ThaumcraftApi.armorMatSpecial, 4, 0);
        GameRegistry.registerItem(ItemVoidmetalGoggles, "ItemVoidmetalGoggles");

        ItemLumosRing = new ItemLumosRing();
        GameRegistry.registerItem(ItemLumosRing, "ItemLumosRing");

        // ===== 堕神暗影套 注册 =====
        // 头盔：用通用类
        itemFallenGodShadowHelmet = new ItemFallenGodShadowArmor(TMMaterials.ARMOR_SHADOWFORTRESS, 4, 0)
            .setUnlocalizedName("ItemFallenGodShadowHelmet")
            .setTextureName("taintedmagic:ItemFallenGodShadowHelmet");
        GameRegistry.registerItem(itemFallenGodShadowHelmet, "itemFallenGodShadowHelmet");

        // 胸甲：用专用类（带翅膀！）
        itemFallenGodShadowChestplate = new ItemAbyssalShadowChestplate(TMMaterials.ARMOR_SHADOWFORTRESS, 4, 1)
            .setUnlocalizedName("ItemFallenGodShadowChestplate")
            .setTextureName("taintedmagic:ItemFallenGodShadowChestplate");
        GameRegistry.registerItem(itemFallenGodShadowChestplate, "itemFallenGodShadowChestplate");

        // 护腿：用通用类
        itemFallenGodShadowLeggings = new ItemFallenGodShadowArmor(TMMaterials.ARMOR_SHADOWFORTRESS, 4, 2)
            .setUnlocalizedName("ItemFallenGodShadowLeggings")
            .setTextureName("taintedmagic:ItemFallenGodShadowLeggings");
        GameRegistry.registerItem(itemFallenGodShadowLeggings, "itemFallenGodShadowLeggings");

        // 靴子：用通用类
        itemFallenGodShadowBoots = new ItemFallenGodShadowArmor(TMMaterials.ARMOR_SHADOWFORTRESS, 4, 3)
            .setUnlocalizedName("ItemFallenGodShadowBoots")
            .setTextureName("taintedmagic:ItemFallenGodShadowBoots");
        GameRegistry.registerItem(itemFallenGodShadowBoots, "itemFallenGodShadowBoots");
        // =================================

        // ===== 永夜深渊魔典 注册 =====
        ItemAbyssalTome = new ItemAbyssalTome();
        GameRegistry.registerItem(ItemAbyssalTome, "ItemAbyssalTome");
        // ============================

        // Wands and Staves
        ItemWandRod = new ItemWandRod();
        GameRegistry.registerItem(ItemWandRod, "ItemWandRod");

        WAND_ROD_WARPWOOD = new WandRod(
            "warpwood",
            250,
            new ItemStack(ItemWandRod, 1, 0),
            16,
            new WandHandler(),
            new ResourceLocation("taintedmagic:textures/models/ModelWAND_ROD_WARPWOOD.png"));
        WAND_ROD_WARPWOOD.setGlowing(true);

        STAFF_ROD_WARPWOOD = new StaffRod(
            "warpwood",
            500,
            new ItemStack(ItemWandRod, 1, 1),
            20,
            new WandHandler(),
            new ResourceLocation("taintedmagic:textures/models/ModelWAND_ROD_WARPWOOD.png"));
        STAFF_ROD_WARPWOOD.setRunes(true);
        STAFF_ROD_WARPWOOD.setGlowing(true);

        ItemWandCap = new ItemWandCap();
        GameRegistry.registerItem(ItemWandCap, "ItemWandCap");

        WAND_CAP_SHADOWMETAL = new WandCap("shadowmetal", 0.65F, new ItemStack(ItemWandCap, 1, 0), 12);
        WAND_CAP_SHADOWMETAL
            .setTexture(new ResourceLocation("taintedmagic:textures/models/ModelWAND_CAP_SHADOWMETAL.png"));

        WAND_CAP_CLOTH = new WandCap("cloth", 0.97F, new ItemStack(ItemWandCap, 1, 1), 2);
        WAND_CAP_CLOTH.setTexture(new ResourceLocation("taintedmagic:textures/models/ModelWAND_CAP_CLOTH.png"));

        WAND_CAP_CRIMSON_CLOTH = new WandCap("crimsoncloth", 0.93F, new ItemStack(ItemWandCap, 1, 2), 3);
        WAND_CAP_CRIMSON_CLOTH
            .setTexture(new ResourceLocation("taintedmagic:textures/models/ModelWAND_CAP_CRIMSON_CLOTH.png"));

        WAND_CAP_SHADOW_CLOTH = new WandCap("shadowcloth", 0.93F, new ItemStack(ItemWandCap, 1, 3), 4);
        WAND_CAP_SHADOW_CLOTH
            .setTexture(new ResourceLocation("taintedmagic:textures/models/ModelWAND_CAP_SHADOW_CLOTH.png"));

        // Foci
        ItemFocusTaintSwarm = new ItemFocusTaintSwarm();
        GameRegistry.registerItem(ItemFocusTaintSwarm, "ItemFocusTaintSwarm");

        ItemFocusDarkMatter = new ItemFocusDarkMatter();
        GameRegistry.registerItem(ItemFocusDarkMatter, "ItemFocusDarkMatter");

        ItemFocusMageMace = new ItemFocusMageMace();
        GameRegistry.registerItem(ItemFocusMageMace, "ItemFocusMageMace");

        ItemFocusShockwave = new ItemFocusShockwave();
        GameRegistry.registerItem(ItemFocusShockwave, "ItemFocusShockwave");

        ItemFocusVisShard = new ItemFocusVisShard();
        GameRegistry.registerItem(ItemFocusVisShard, "ItemFocusVisShard");

        ItemFocusLumos = new ItemFocusLumos();
        GameRegistry.registerItem(ItemFocusLumos, "ItemFocusLumos");

        // Tools
        ItemThaumicDisassembler = new ItemThaumicDisassembler();
        GameRegistry.registerItem(ItemThaumicDisassembler, "ItemThaumicDisassembler");

        ItemHollowDagger = new ItemHollowDagger(TMMaterials.TOOL_HOLLOW);
        GameRegistry.registerItem(ItemHollowDagger, "ItemHollowDagger");

        ItemShadowmetalHoe = new ItemShadowmetalHoe(TMMaterials.TOOL_SHADOW);
        GameRegistry.registerItem(ItemShadowmetalHoe, "ItemShadowmetalHoe");

        ItemShadowmetalPick = new ItemShadowmetalPick(TMMaterials.TOOL_SHADOW);
        GameRegistry.registerItem(ItemShadowmetalPick, "ItemShadowmetalPick");

        ItemShadowmetalAxe = new ItemShadowmetalAxe(TMMaterials.TOOL_SHADOW);
        GameRegistry.registerItem(ItemShadowmetalAxe, "ItemShadowmetalAxe");

        ItemShadowmetalSpade = new ItemShadowmetalSpade(TMMaterials.TOOL_SHADOW);
        GameRegistry.registerItem(ItemShadowmetalSpade, "ItemShadowmetalSpade");

        ItemShadowmetalSword = new ItemShadowmetalSword(TMMaterials.TOOL_SHADOW);
        GameRegistry.registerItem(ItemShadowmetalSword, "ItemShadowmetalSword");

        ItemPrimalBlade = new ItemPrimalBlade(TMMaterials.TOOL_PRIMAL);
        GameRegistry.registerItem(ItemPrimalBlade, "ItemPrimalBlade");

        ItemKatana = new ItemKatana();
        GameRegistry.registerItem(ItemKatana, "ItemKatana");
    }
}
