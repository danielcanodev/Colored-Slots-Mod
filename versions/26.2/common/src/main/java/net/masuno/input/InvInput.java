package net.masuno.input;

import com.mojang.blaze3d.platform.InputConstants;
import net.masuno.inventory.InvColoredSlots;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.sounds.SoundEvents;
import org.lwjgl.glfw.GLFW;

public final class InvInput {
	public static KeyMapping TOGGLE_SLOT_EDITING = null;

	public static boolean Sneaking = false;
	public static boolean ColorKeyPressed = false;
	public static boolean wasHoldingToggleKey = false;
	public static boolean wasHoldingColorKey = false;
	public static boolean IconKeyPressed = false;
	public static boolean wasHoldingIconKey = false;

	public static void tick(Minecraft client, boolean editingScreen) {
		if (client.player == null) return;

		if (editingScreen) {
			if (InputConstants.isKeyDown(client.getWindow(), TOGGLE_SLOT_EDITING.getDefaultKey().getValue())) {
				if (!wasHoldingToggleKey) {
					InvColoredSlots.isEditing = !InvColoredSlots.isEditing;

					client.getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
					wasHoldingToggleKey = true;
				}
			} else {
				wasHoldingToggleKey = false;
			}

			long window = client.getWindow().handle();

			Sneaking = InputConstants.isKeyDown(client.getWindow(), client.options.keyShift.getDefaultKey().getValue());

			ColorKeyPressed = GLFW.glfwGetMouseButton(window, GLFW.GLFW_MOUSE_BUTTON_LEFT) == GLFW.GLFW_PRESS;
			if (!ColorKeyPressed) wasHoldingColorKey = false;

			IconKeyPressed = GLFW.glfwGetMouseButton(window, GLFW.GLFW_MOUSE_BUTTON_RIGHT) == GLFW.GLFW_PRESS;
			if (!IconKeyPressed) wasHoldingIconKey = false;
		} else {
			wasHoldingColorKey = false;
			wasHoldingIconKey = false;
		}
	}
}