package net.iljorus.logstrippermod.block.entity.config;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;

import java.util.HashMap;
import java.util.Set;

public class SideConfig {
    private HashMap<Direction, Action> config = new HashMap<>(6);

    public SideConfig() {
    }

    public void set(Direction direction, Action action) {
        config.put(direction, action);
    }

    public Action get(Direction direction) {
        return config.get(direction);
    }

    public Set<Direction> keySet() {
        return config.keySet();
    }

    public CompoundTag serializeNBT() {
        ListTag listTag = new ListTag();
        config.forEach(((direction, action) -> {
            CompoundTag tag = new CompoundTag();
            tag.putIntArray(direction.getName(), new int[]{direction.get3DDataValue(), action.getIntValue()}); //name is all lowercase
            listTag.add(tag);
        }));

        CompoundTag nbt = new CompoundTag();
        nbt.put("Config", listTag);
        return nbt;
    }

    public void deserializeNBT(CompoundTag nbt) {
        ListTag listTag = nbt.getList("Config", Tag.TAG_INT_ARRAY);
        for (int i = 0; i < listTag.size(); i++) {
            int[] array = listTag.getIntArray(i);
            config.put(Direction.from3DDataValue(array[0]), Action.fromIntValue(array[1]));
        }
    }

    public enum Action {
        NONE(0),
        PUSH(1),
        PULL(2),
        BOTH(3);

        private final int value;

        Action(int value) {
            this.value = value;
        }

        public int getIntValue() {
            return this.value;
        }

        public static Action fromIntValue(int value) {
            return switch (value) {
                case 0 -> NONE;
                case 1 -> PUSH;
                case 2 -> PULL;
                case 3 -> BOTH;
                default -> null;
            };
        }

        public Action next() {
            return switch (this) {
                case NONE -> PUSH;
                case PUSH -> PULL;
                case PULL -> BOTH;
                case BOTH -> NONE;
            };
        }
    }
}
