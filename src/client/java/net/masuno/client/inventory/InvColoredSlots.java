package net.masuno.client.inventory;

import net.masuno.client.config.InvConfig;
import net.masuno.client.input.InvInput;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvents;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class InvColoredSlots {
    public static boolean isEditing = false;

    public static final Identifier EDITING = Identifier.withDefaultNamespace("textures/gui/container/editing.png");
    public static final Identifier CREATIVE_EDITING = Identifier.withDefaultNamespace("textures/gui/container/creative_editing.png");
    public static final Identifier SLOT = Identifier.withDefaultNamespace("textures/gui/container/slot.png");

    public static void tickMouseSlot(int index) {
        if (!isSlotEmpty(index)) return;
        if (!isHeldEmpty()) return;
        if (!isEditing) return;

        if (InvInput.ColorKeyPressed && !InvInput.wasHoldingColorKey) {
            if (InvInput.Sneaking) resetSlotColor(index);
            else swapSlotColor(index);
            InvInput.afterColorKey();

            Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
        }

        if (InvInput.IconKeyPressed && !InvInput.wasHoldingIconKey) {
            if (InvInput.Sneaking) resetSlotIcon(index);
            else swapSlotIcon(index);
            InvInput.afterIconKey();

            Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
        }
    }

    public static void swapSlotIcon(int index) {
        InvConfig.Preset preset = activePreset();
        preset.ICONS.set(index, SlotIcon.fromValue(preset.ICONS.get(index)).next().ordinal());
        InvConfig.HANDLER.save();
    }

    public static void swapSlotColor(int index) {
        InvConfig.Preset preset = activePreset();
        preset.COLORS.set(index, SlotColor.fromValue(preset.COLORS.get(index)).next().ordinal());
        InvConfig.HANDLER.save();
    }

    public static void resetSlotColor(int index) {
        activePreset().COLORS.set(index, SlotColor.NONE.ordinal());
        InvConfig.HANDLER.save();
    }

    public static void resetSlotIcon(int index) {
        activePreset().ICONS.set(index, SlotIcon.EMPTY.ordinal());
        InvConfig.HANDLER.save();
    }

    public static InvConfig.Preset activePreset() {
        InvConfig config = InvConfig.HANDLER.instance();
        return config.PRESETS.computeIfAbsent(config.ACTIVE_PRESET, k -> new InvConfig.Preset());
    }

    public static String activePresetName() {
        return InvConfig.HANDLER.instance().ACTIVE_PRESET;
    }

    public static List<String> presetNames() {
        return new ArrayList<>(InvConfig.HANDLER.instance().PRESETS.keySet());
    }

    public static void ensureDefaultPreset() {
        InvConfig config = InvConfig.HANDLER.instance();
        if (!config.PRESETS.containsKey(config.ACTIVE_PRESET)) {
            config.PRESETS.put(config.ACTIVE_PRESET, new InvConfig.Preset());
            InvConfig.HANDLER.save();
        }
    }

    public static boolean savePreset(String name) {
        if (name == null || name.isEmpty()) return false;

        InvConfig config = InvConfig.HANDLER.instance();
        config.PRESETS.put(name, activePreset().copy());
        config.ACTIVE_PRESET = name;
        InvConfig.HANDLER.save();
        return true;
    }

    public static boolean setPreset(String name) {
        InvConfig config = InvConfig.HANDLER.instance();
        if (!config.PRESETS.containsKey(name)) return false;

        config.ACTIVE_PRESET = name;
        InvConfig.HANDLER.save();
        return true;
    }

    public static boolean deletePreset(String name) {
        InvConfig config = InvConfig.HANDLER.instance();
        if (!config.PRESETS.containsKey(name)) return false;

        config.PRESETS.remove(name);
        if (config.PRESETS.isEmpty()) {
            config.ACTIVE_PRESET = "default";
            config.PRESETS.put("default", new InvConfig.Preset());
        } else if (config.ACTIVE_PRESET.equals(name)) {
            config.ACTIVE_PRESET = config.PRESETS.keySet().iterator().next();
        }
        InvConfig.HANDLER.save();
        return true;
    }

    public static boolean isMouseInSlot(float xm, float ym, int x, int y) {
        int s = 18;
        return xm > x && xm < x + s && ym > y && ym < y + s;
    }

    public static boolean isHeldEmpty() {
        if (Minecraft.getInstance().player == null) return true;
        return Minecraft.getInstance().player.containerMenu.getCarried().isEmpty();
    }

    public static boolean isSlotEmpty(int index) {
        if (Minecraft.getInstance().player == null) return true;
        return Minecraft.getInstance().player.getInventory().getItem(index).isEmpty();
    }

    public static Identifier getIcon(int slot) {
        return SlotIcon.fromValue(activePreset().ICONS.get(slot)).texture();
    }

    public static Color getColor(int slot) {
        return SlotColor.fromValue(activePreset().COLORS.get(slot)).color();
    }
}
