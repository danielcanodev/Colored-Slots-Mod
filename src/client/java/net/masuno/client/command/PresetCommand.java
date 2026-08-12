package net.masuno.client.command;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.masuno.ColoredSlots;
import net.masuno.client.inventory.InvColoredSlots;
import net.minecraft.network.chat.Component;

import java.util.List;

import static net.fabricmc.fabric.api.client.command.v2.ClientCommands.argument;
import static net.fabricmc.fabric.api.client.command.v2.ClientCommands.literal;

public class PresetCommand {
    public static void initialize() {
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> dispatcher.register(
                literal("cslots").then(
                        literal("preset")
                                .then(literal("list").executes(ctx -> listPresets(ctx.getSource())))
                                .then(literal("set").then(argument("name", StringArgumentType.word()).suggests(PRESETS).executes(ctx -> setPreset(ctx.getSource(), StringArgumentType.getString(ctx, "name")))))
                                .then(literal("save").then(argument("name", StringArgumentType.word()).suggests(PRESETS).executes(ctx -> savePreset(ctx.getSource(), StringArgumentType.getString(ctx, "name")))))
                                .then(literal("delete").then(argument("name", StringArgumentType.word()).suggests(PRESETS).executes(ctx -> deletePreset(ctx.getSource(), StringArgumentType.getString(ctx, "name")))))
                )
        ));
    }

    public static final SuggestionProvider<FabricClientCommandSource> PRESETS = (ctx, builder) -> {
        for (String name : InvColoredSlots.presetNames()) {
            builder.suggest(name, Component.literal("Preset: " + name));
        }
        return builder.buildFuture();
    };

    private static int listPresets(FabricClientCommandSource source) {
        List<String> names = InvColoredSlots.presetNames();
        String active = InvColoredSlots.activePresetName();

        if (names.isEmpty()) {
            source.sendError(Component.literal("No presets found."));
            return 0;
        }

        StringBuilder builder = new StringBuilder("Presets: ");
        for (String name : names) {
            builder.append(name);
            if (name.equals(active)) builder.append(" (active)");
            if (names.indexOf(name) < names.size() - 1) builder.append(", ");
        }
        source.sendFeedback(Component.literal(builder.toString()));
        return 1;
    }

    private static int setPreset(FabricClientCommandSource source, String name) {
        if (!InvColoredSlots.setPreset(name)) {
            source.sendError(Component.literal("Preset \"" + name + "\" not found."));
            return 0;
        }
        source.sendFeedback(Component.literal("Switched to preset \"" + name + "\"."));
        return 1;
    }

    private static int savePreset(FabricClientCommandSource source, String name) {
        if (!InvColoredSlots.savePreset(name)) {
            source.sendError(Component.literal("Preset name cannot be empty."));
            return 0;
        }
        source.sendFeedback(Component.literal("Saved current layout as preset \"" + name + "\"."));
        return 1;
    }

    private static int deletePreset(FabricClientCommandSource source, String name) {
        if (!InvColoredSlots.deletePreset(name)) {
            source.sendError(Component.literal("Preset \"" + name + "\" not found."));
            return 0;
        }
        source.sendFeedback(Component.literal("Deleted preset \"" + name + "\"."));
        return 1;
    }
}
