package net.iljorus.logstrippermod.inventory;

import net.iljorus.logstrippermod.block.entity.config.SideConfig;
import net.minecraft.core.Direction;
import net.minecraft.world.inventory.ContainerData;

public interface LogStripperContainerData extends ContainerData {
    int getSideConfig(int slotIndex, int side);

    void setSideConfig(int slotIndex, int side, int action);

    class EmptyData implements LogStripperContainerData {
        private final int[] ints;
        private final SideConfig[] configs;

        public EmptyData() {
            this.ints = new int[3];
            this.configs = new SideConfig[]{new SideConfig(), new SideConfig(), new SideConfig()};
        }

        @Override
        public int getSideConfig(int slotIndex, int side) {
            return this.configs[slotIndex].get(Direction.from3DDataValue(side)).getIntValue();
        }

        @Override
        public void setSideConfig(int slotIndex, int side, int action) {
            this.configs[slotIndex].set(Direction.from3DDataValue(side), SideConfig.Action.fromIntValue(action));
        }

        @Override
        public int get(int pIndex) {
            return this.ints[pIndex];
        }

        @Override
        public void set(int pIndex, int pValue) {
            this.ints[pIndex] = pValue;
        }

        @Override
        public int getCount() {
            return this.ints.length;
        }
    }
}
