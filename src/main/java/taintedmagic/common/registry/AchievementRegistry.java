package taintedmagic.common.registry;

import net.minecraft.item.ItemStack;
import net.minecraft.stats.Achievement;
import net.minecraftforge.common.AchievementPage;

public class AchievementRegistry {

    public static Achievement PRIMAL_BLADE;

    public static void init() {
        PRIMAL_BLADE = new Achievement(
            "primal_blade", // 统计ID
            "primal_blade", // 翻译键前缀
            -4,
            4, // 在成就界面的坐标
            new ItemStack(ItemRegistry.ItemSlashBladePrimal), // 成就图标
            (Achievement) null // 父成就（null 表示挂在主页面）
        ).registerStat()
            .setSpecial(); // setSpecial() = 紫色文字

        // 注册到自己的成就页面
        AchievementPage.registerAchievementPage(new AchievementPage("Tainted Magic", PRIMAL_BLADE));
    }
}
