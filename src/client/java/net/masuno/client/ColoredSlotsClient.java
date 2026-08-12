package net.masuno.client;

import net.fabricmc.api.ClientModInitializer;
import net.masuno.client.config.InvConfig;
import net.masuno.client.input.InventoryInput;

public class ColoredSlotsClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		InvConfig.HANDLER.load();
		InventoryInput.Initialize();
	}

}