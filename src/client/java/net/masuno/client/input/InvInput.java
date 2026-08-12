package net.masuno.client.input;

import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.masuno.ColoredSlots;
import net.masuno.client.inventory.InvColoredSlots;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvents;
import org.lwjgl.glfw.GLFW;

public class InvInput {
    public static final KeyMapping.Category CATEGORY = KeyMapping.Category.register(
            Identifier.fromNamespaceAndPath(ColoredSlots.MOD_ID, "inventory")
    );
    public static boolean Sneaking = false;

    public static boolean ColorKeyPressed = false;
    public static boolean wasHoldingToggleKey = false;
    public static boolean wasHoldingColorKey = false;

    public static boolean IconKeyPressed = false;
    public static boolean wasHoldingIconKey = false;

    public static final KeyMapping TOGGLE_SLOT_EDITING = KeyMappingHelper.registerKeyMapping(
            new KeyMapping(
                    "key.colored_slots.toggle",
                    InputConstants.Type.KEYSYM,
                    InputConstants.KEY_O,
                    CATEGORY
            ));

    public static void afterColorKey() {
        wasHoldingColorKey = true;
    }

    public static void afterIconKey() {
        wasHoldingIconKey = true;
    }

    public static void initialize() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player == null) return;

            if (client.gui.screen() instanceof InventoryScreen || client.gui.screen() instanceof CreativeModeInventoryScreen) {
                if (InputConstants.isKeyDown(client.getWindow(), TOGGLE_SLOT_EDITING.getDefaultKey().getValue())) {
                    if (!wasHoldingToggleKey) {
                        InvColoredSlots.isEditing = !InvColoredSlots.isEditing;

                        Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
                        wasHoldingToggleKey = true;
                    }
                } else {
                    wasHoldingToggleKey = false;
                }


                long window = client.getWindow().handle();

                Sneaking = (InputConstants.isKeyDown(client.getWindow(), Minecraft.getInstance().options.keyShift.getDefaultKey().getValue()));

                ColorKeyPressed =
                        GLFW.glfwGetMouseButton(window, GLFW.GLFW_MOUSE_BUTTON_LEFT)
                                == GLFW.GLFW_PRESS;

                if (!ColorKeyPressed)
                    wasHoldingColorKey = false;

                IconKeyPressed =
                        GLFW.glfwGetMouseButton(window, GLFW.GLFW_MOUSE_BUTTON_RIGHT)
                                == GLFW.GLFW_PRESS;
                if (!IconKeyPressed)
                    wasHoldingIconKey = false;
            }
            else {
                wasHoldingColorKey = false;
                wasHoldingIconKey = false;
            }
        });
    }
}
