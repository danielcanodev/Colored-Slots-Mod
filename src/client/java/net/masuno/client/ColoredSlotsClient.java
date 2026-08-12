package net.masuno.client;

import net.fabricmc.api.ClientModInitializer;
import net.masuno.client.command.PresetCommand;
import net.masuno.client.config.InvConfig;
import net.masuno.client.input.InvInput;
import net.masuno.client.inventory.InvColoredSlots;

public class ColoredSlotsClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		InvConfig.HANDLER.load();
		InvColoredSlots.ensureDefaultPreset();
		InvInput.initialize();
		PresetCommand.initialize();
	}

}