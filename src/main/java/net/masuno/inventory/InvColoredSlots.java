package net.masuno.inventory;

import net.masuno.config.Config;
import net.masuno.config.ConfigManager;
import net.masuno.input.InvInput;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.sounds.SoundEvents;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

public final class InvColoredSlots {
	public static boolean isEditing = false;

	public static final String EDITING = "textures/gui/container/editing.png";
	public static final String CREATIVE_EDITING = "textures/gui/container/creative_editing.png";
	public static final String SLOT = "textures/gui/container/slot.png";

	public static void tickMouseSlot(int index) {
		if (!isSlotEmpty(index)) return;
		if (!isHeldEmpty()) return;
		if (!isEditing) return;

		Minecraft mc = Minecraft.getInstance();

		if (InvInput.ColorKeyPressed && !InvInput.wasHoldingColorKey) {
			if (InvInput.Sneaking) resetSlotColor(index);
			else swapSlotColor(index);
			InvInput.wasHoldingColorKey = true;

			mc.getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
		}

		if (InvInput.IconKeyPressed && !InvInput.wasHoldingIconKey) {
			if (InvInput.Sneaking) resetSlotIcon(index);
			else swapSlotIcon(index);
			InvInput.wasHoldingIconKey = true;

			mc.getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
		}
	}

	public static void swapSlotIcon(int index) {
		Config.Preset preset = activePreset();
		preset.icons.set(index, SlotIcon.fromValue(preset.icons.get(index)).next().ordinal());
		ConfigManager.save();
	}

	public static void swapSlotColor(int index) {
		Config.Preset preset = activePreset();
		preset.colors.set(index, SlotColor.fromValue(preset.colors.get(index)).next().ordinal());
		ConfigManager.save();
	}

	public static void resetSlotColor(int index) {
		activePreset().colors.set(index, SlotColor.NONE.ordinal());
		ConfigManager.save();
	}

	public static void resetSlotIcon(int index) {
		activePreset().icons.set(index, SlotIcon.EMPTY.ordinal());
		ConfigManager.save();
	}

	public static Config.Preset activePreset() {
		Config config = ConfigManager.get();
		return config.presets.computeIfAbsent(config.activePreset, k -> new Config.Preset());
	}

	public static String activePresetName() {
		return ConfigManager.get().activePreset;
	}

	public static List<String> presetNames() {
		return new ArrayList<>(ConfigManager.get().presets.keySet());
	}

	public static void ensureDefaultPreset() {
		Config config = ConfigManager.get();
		if (!config.presets.containsKey(config.activePreset)) {
			config.presets.put(config.activePreset, new Config.Preset());
			ConfigManager.save();
		}
	}

	public static boolean savePreset(String name) {
		if (name == null || name.isEmpty()) return false;

		Config config = ConfigManager.get();
		config.presets.put(name, activePreset().copy());
		config.activePreset = name;
		ConfigManager.save();
		return true;
	}

	public static boolean setPreset(String name) {
		Config config = ConfigManager.get();
		if (!config.presets.containsKey(name)) return false;

		config.activePreset = name;
		ConfigManager.save();
		return true;
	}

	public static boolean deletePreset(String name) {
		Config config = ConfigManager.get();
		if (!config.presets.containsKey(name)) return false;

		config.presets.remove(name);
		if (config.presets.isEmpty()) {
			config.activePreset = "default";
			config.presets.put("default", new Config.Preset());
		} else if (config.activePreset.equals(name)) {
			config.activePreset = config.presets.keySet().iterator().next();
		}
		ConfigManager.save();
		return true;
	}

	public static boolean isMouseInSlot(float xm, float ym, int x, int y) {
		return xm > x && xm < x + 18 && ym > y && ym < y + 18;
	}

	public static boolean isHeldEmpty() {
		Minecraft mc = Minecraft.getInstance();
		if (mc.player == null) return true;
		return mc.player.containerMenu.getCarried().isEmpty();
	}

	public static boolean isSlotEmpty(int index) {
		Minecraft mc = Minecraft.getInstance();
		if (mc.player == null) return true;
		return mc.player.getInventory().getItem(index).isEmpty();
	}

	public static String getIcon(int slot) {
		return SlotIcon.fromValue(activePreset().icons.get(slot)).path();
	}
	public static Color getColor(int slot) {
		return SlotColor.fromValue(activePreset().colors.get(slot)).color();
	}
}