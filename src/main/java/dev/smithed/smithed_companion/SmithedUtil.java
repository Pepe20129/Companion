package dev.smithed.smithed_companion;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.MathHelper;
import org.apache.commons.lang3.tuple.ImmutablePair;

import java.awt.*;

public class SmithedUtil {

    // just didn't wanna type these out every time
    public static boolean hasNBT(ItemStack stack, String key) {
        return stack.getSubNbt(key) != null;
    }
    // Takes only NBT instead of an entire Item
    public static boolean hasNBT(NbtCompound nbtCompound, String key) {
        return nbtCompound.getCompound(key) != null;
    }
    public static boolean hasSmithedNBT(ItemStack stack) {
        return hasNBT(stack, "smithed");
    }
    public static boolean hasSmithedNBT(NbtCompound nbtCompound) {
        return hasNBT(nbtCompound, "smithed");
    }

    public static NbtCompound getSmithed(NbtCompound nbtCompound) {
        return nbtCompound.getCompound("smithed");
    }
}
