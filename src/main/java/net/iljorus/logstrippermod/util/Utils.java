package net.iljorus.logstrippermod.util;

public class Utils {

    public static final int UPDATE_RATE = 40;
    private static int timeTick = 0;

    public static void tickTime() {
        if (++timeTick >= UPDATE_RATE) {
            timeTick = 0;
        }
    }

    public static boolean updateRateNormal() {
        return timeTick == 0;
    }
}
