package net.masuno.client;

import net.fabricmc.api.ClientModInitializer;
import net.masuno.client.command.PresetCommand;
import net.masuno.client.config.InvConfig;
import net.masuno.client.input.InventoryInput;
import net.masuno.client.inventory.InventoryColoredSlots;

public class ColoredSlotsClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		InvConfig.HANDLER.load();
		InventoryColoredSlots.ensureDefaultPreset();
		InventoryInput.initialize();
		PresetCommand.initialize();
	}

}