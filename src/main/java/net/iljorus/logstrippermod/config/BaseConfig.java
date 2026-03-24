package net.iljorus.logstrippermod.config;

import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

public class BaseConfig {
    public static final BaseCommonConfig COMMON;
    public static final ForgeConfigSpec COMMON_SPEC;

    static {
        Pair<BaseCommonConfig, ForgeConfigSpec> pair = new ForgeConfigSpec.Builder()
                .configure(BaseCommonConfig::new);
        COMMON = pair.getLeft();
        COMMON_SPEC = pair.getRight();
    }

    public static boolean axeSlotEnabled(){
        return COMMON.AXE_SLOT.get();
    }
}
