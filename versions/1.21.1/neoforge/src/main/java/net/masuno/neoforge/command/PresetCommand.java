package net.masuno.neoforge.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import net.masuno.inventory.InvColoredSlots;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public final class PresetCommand {
	public static final SuggestionProvider<CommandSourceStack> PRESETS = (ctx, builder) -> {
		for (String name : InvColoredSlots.presetNames()) {
			builder.suggest(name, Component.literal("Preset: " + name));
		}
		return CompletableFuture.completedFuture(builder.build());
	};

	public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
		dispatcher.register(Commands.literal("cslots").then(
				Commands.literal("preset")
						.then(Commands.literal("list").executes(ctx -> listPresets(ctx.getSource())))
						.then(Commands.literal("set").then(Commands.argument("name", StringArgumentType.word()).suggests(PRESETS).executes(ctx -> setPreset(ctx.getSource(), StringArgumentType.getString(ctx, "name")))))
						.then(Commands.literal("save").then(Commands.argument("name", StringArgumentType.word()).suggests(PRESETS).executes(ctx -> savePreset(ctx.getSource(), StringArgumentType.getString(ctx, "name")))))
						.then(Commands.literal("delete").then(Commands.argument("name", StringArgumentType.word()).suggests(PRESETS).executes(ctx -> deletePreset(ctx.getSource(), StringArgumentType.getString(ctx, "name")))))
		));
	}

	private static int listPresets(CommandSourceStack source) {
		List<String> names = InvColoredSlots.presetNames();
		String active = InvColoredSlots.activePresetName();

		if (names.isEmpty()) {
			source.sendFailure(Component.literal("No presets found."));
			return 0;
		}

		StringBuilder builder = new StringBuilder("Presets: ");
		for (String name : names) {
			builder.append(name);
			if (name.equals(active)) builder.append(" (active)");
			if (names.indexOf(name) < names.size() - 1) builder.append(", ");
		}
		source.sendSuccess(() -> Component.literal(builder.toString()), false);
		return 1;
	}

	private static int setPreset(CommandSourceStack source, String name) {
		if (!InvColoredSlots.setPreset(name)) {
			source.sendFailure(Component.literal("Preset \"" + name + "\" not found."));
			return 0;
		}
		source.sendSuccess(() -> Component.literal("Switched to preset \"" + name + "\"."), false);
		return 1;
	}

	private static int savePreset(CommandSourceStack source, String name) {
		if (!InvColoredSlots.savePreset(name)) {
			source.sendFailure(Component.literal("Preset name cannot be empty."));
			return 0;
		}
		source.sendSuccess(() -> Component.literal("Saved current layout as preset \"" + name + "\"."), false);
		return 1;
	}

	private static int deletePreset(CommandSourceStack source, String name) {
		if (!InvColoredSlots.deletePreset(name)) {
			source.sendFailure(Component.literal("Preset \"" + name + "\" not found."));
			return 0;
		}
		source.sendSuccess(() -> Component.literal("Deleted preset \"" + name + "\"."), false);
		return 1;
	}
}