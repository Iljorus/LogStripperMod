package net.iljorus.logstrippermod.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class BaseCommonConfig {

    public final ForgeConfigSpec.IntValue DURATION;
    public final ForgeConfigSpec.BooleanValue AXE_SLOT;
    public final ForgeConfigSpec.IntValue DURABILITY_DEDUCTION;
    public final ForgeConfigSpec.BooleanValue PRESERVE_AXE;

    public BaseCommonConfig(ForgeConfigSpec.Builder builder) {
        DURATION =  builder.comment("Duration to complete one stripping operation [Integer (Ticks)]")
                .defineInRange("duration", 25, 1, Integer.MAX_VALUE);
        AXE_SLOT = builder.comment("Optional Axe slot, machine operates faster if axe is present [Boolean]")
                .define("require_axe", true);
        DURABILITY_DEDUCTION = builder.comment("Durability deduction when axe is present [Integer]")
                .defineInRange("durability", 1, 0,Integer.MAX_VALUE);
        PRESERVE_AXE = builder.comment("Extracts the Axe if processing another log would destroy it [Boolean]")
                .define("preserve_axe", true);
    }
}
