package taintedmagic.client.handler;

public class AbyssalShadowClientHandler {

    // 默认开启
    private static boolean armorEnabled = false;

    public static boolean isArmorEnabled() {
        return armorEnabled;
    }

    public static void setArmorEnabled(boolean status) {
        armorEnabled = status;
    }

    public static void toggleArmor() {
        armorEnabled = !armorEnabled;
    }
}
