package net.masuno.fabric;

import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.fabricmc.loader.api.FabricLoader;
import net.masuno.config.ConfigManager;
import net.masuno.config.GsonConfigStore;
import net.masuno.fabric.command.PresetCommand;
import net.masuno.input.InvInput;
import net.masuno.inventory.InvColoredSlots;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.CreativeModeTabs;

public class ColoredSlots implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		ConfigManager.init(new GsonConfigStore(FabricLoader.getInstance().getConfigDir()));
		InvColoredSlots.ensureDefaultPreset();

		InvInput.TOGGLE_SLOT_EDITING = KeyMappingHelper.registerKeyMapping(
				new KeyMapping(
						"key.colored_slots.toggle",
						InputConstants.Type.KEYSYM,
						InputConstants.KEY_O,
						KeyMapping.Category.register(Identifier.fromNamespaceAndPath(net.masuno.ColoredSlotsInfo.MOD_ID, "inventory"))
				));

		ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> PresetCommand.register(dispatcher));
		ClientTickEvents.END_CLIENT_TICK.register(client -> InvInput.tick(client, isEditingScreen(client)));
	}

	private static boolean isEditingScreen(Minecraft client) {
		if (client.gui.screen() instanceof InventoryScreen) return true;
		if (client.gui.screen() instanceof CreativeModeInventoryScreen screen) {
			return screen.getSelectedTab() == CreativeModeTabs.searchTab();
		}
		return false;
	}
}