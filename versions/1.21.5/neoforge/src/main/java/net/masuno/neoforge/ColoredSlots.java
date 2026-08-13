package net.masuno.neoforge;

import com.mojang.blaze3d.platform.InputConstants;
import net.masuno.config.ConfigManager;
import net.masuno.config.GsonConfigStore;
import net.masuno.input.InvInput;
import net.masuno.inventory.InvColoredSlots;
import net.masuno.mixin.CreativeModeInventoryScreenAccessor;
import net.masuno.neoforge.command.PresetCommand;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.world.item.CreativeModeTabs;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.fml.loading.FMLPaths;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RegisterClientCommandsEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.common.NeoForge;

@Mod(net.masuno.ColoredSlotsInfo.MOD_ID)
public class ColoredSlots {
	public ColoredSlots(IEventBus modEventBus) {
		if (FMLEnvironment.dist != Dist.CLIENT) return;
		ConfigManager.init(new GsonConfigStore(FMLPaths.CONFIGDIR.get()));
		InvColoredSlots.ensureDefaultPreset();

		InvInput.TOGGLE_SLOT_EDITING = new KeyMapping(
				"key.colored_slots.toggle",
				InputConstants.Type.KEYSYM,
				InputConstants.KEY_O,
				InvInput.CATEGORY
		);

		NeoForge.EVENT_BUS.addListener(ColoredSlots::registerClientCommands);
		modEventBus.addListener(ColoredSlots::registerKeyMappings);
		NeoForge.EVENT_BUS.addListener(ColoredSlots::onClientTick);
	}

	public static void registerClientCommands(RegisterClientCommandsEvent event) {
		PresetCommand.register(event.getDispatcher());
	}

	public static void registerKeyMappings(RegisterKeyMappingsEvent event) {
		if (InvInput.TOGGLE_SLOT_EDITING != null) event.register(InvInput.TOGGLE_SLOT_EDITING);
	}

	private static void onClientTick(ClientTickEvent.Pre event) {
		Minecraft client = Minecraft.getInstance();
		InvInput.tick(client, isEditingScreen(client));
	}

	private static boolean isEditingScreen(Minecraft client) {
		if (client.screen instanceof InventoryScreen) return true;
		if (client.screen instanceof CreativeModeInventoryScreen screen) {
			return ((CreativeModeInventoryScreenAccessor) screen).coloredSlots$selectedTab() == CreativeModeTabs.searchTab();
		}
		return false;
	}
}