package cn.ksmcbrigade.sleep.client;

import net.minecraftforge.common.ForgeConfigSpec;

public class ClientConfig {
    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec.BooleanValue ALLOW_FORCE_SLEEP_ON_BED = BUILDER.define("allow_force_sleep_on_bed",false);
    public static final ForgeConfigSpec SPEC = BUILDER.build();
}
