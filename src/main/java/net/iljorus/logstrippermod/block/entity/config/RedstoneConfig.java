package net.iljorus.logstrippermod.block.entity.config;

public class RedstoneConfig {
    private final int value;
    private final String name;
    public static RedstoneConfig NONE = new RedstoneConfig(0, "None");
    public static RedstoneConfig LOW = new RedstoneConfig(1, "Low");
    public static RedstoneConfig HIGH = new RedstoneConfig(2, "High");

    private RedstoneConfig(int value, String name) {
        this.value = value;
        this.name = name;
    }

    public int getIntValue() {
        return value;
    }

    public static RedstoneConfig fromIntValue(int value) {
        return switch (value) {
            case 0 -> NONE;
            case 1 -> LOW;
            case 2 -> HIGH;
            default -> null;
        };
    }

    public String asString() {
        return name;
    }

    public RedstoneConfig next() {
        int nextVal = this.getIntValue() + 1;
        return RedstoneConfig.fromIntValue(nextVal > 2 ? 0 : nextVal);
    }
}
