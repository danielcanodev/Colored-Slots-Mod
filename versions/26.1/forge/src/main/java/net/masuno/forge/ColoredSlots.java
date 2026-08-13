package net.masuno.forge;

import com.mojang.blaze3d.platform.InputConstants;
import net.masuno.config.ConfigManager;
import net.masuno.config.GsonConfigStore;
import net.masuno.forge.command.PresetCommand;
import net.masuno.input.InvInput;
import net.masuno.inventory.InvColoredSlots;
import net.masuno.mixin.CreativeModeInventoryScreenAccessor;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterClientCommandsEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLLoader;
import net.minecraftforge.fml.loading.FMLPaths;

@Mod(net.masuno.ColoredSlotsInfo.MOD_ID)
public class ColoredSlots {
	public ColoredSlots(FMLJavaModLoadingContext context) {
		if (FMLLoader.getDist() != Dist.CLIENT) return;

		ConfigManager.init(new GsonConfigStore(FMLPaths.CONFIGDIR.get()));
		InvColoredSlots.ensureDefaultPreset();

		InvInput.TOGGLE_SLOT_EDITING = new KeyMapping(
				"key.colored_slots.toggle",
				InputConstants.Type.KEYSYM,
				InputConstants.KEY_O,
				KeyMapping.Category.register(Identifier.fromNamespaceAndPath(net.masuno.ColoredSlotsInfo.MOD_ID, "inventory"))
		);

		RegisterClientCommandsEvent.BUS.addListener(ColoredSlots::registerClientCommands);
		RegisterKeyMappingsEvent.BUS.addListener(ColoredSlots::registerKeyMappings);
		TickEvent.ClientTickEvent.Pre.BUS.addListener(ColoredSlots::onClientTick);
	}

	public static void registerClientCommands(RegisterClientCommandsEvent event) {
		PresetCommand.register(event.getDispatcher());
	}

	public static void registerKeyMappings(RegisterKeyMappingsEvent event) {
		if (InvInput.TOGGLE_SLOT_EDITING != null) event.register(InvInput.TOGGLE_SLOT_EDITING);
	}

	private static void onClientTick(TickEvent.ClientTickEvent.Pre event) {
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