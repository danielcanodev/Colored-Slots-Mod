package net.masuno.client.command;

import com.mojang.brigadier.arguments.StringArgumentType;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.masuno.ColoredSlots;
import net.masuno.client.inventory.InventoryColoredSlots;
import net.minecraft.network.chat.Component;

import java.util.List;

import static net.fabricmc.fabric.api.client.command.v2.ClientCommands.argument;
import static net.fabricmc.fabric.api.client.command.v2.ClientCommands.literal;

public class PresetCommand {
    public static void initialize() {
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> dispatcher.register(
                literal(ColoredSlots.MOD_ID).then(
                        literal("preset")
                                .then(literal("list").executes(ctx -> listPresets(ctx.getSource())))
                                .then(literal("set").then(argument("name", StringArgumentType.word()).executes(ctx -> setPreset(ctx.getSource(), StringArgumentType.getString(ctx, "name")))))
                                .then(literal("save").then(argument("name", StringArgumentType.word()).executes(ctx -> savePreset(ctx.getSource(), StringArgumentType.getString(ctx, "name")))))
                                .then(literal("delete").then(argument("name", StringArgumentType.word()).executes(ctx -> deletePreset(ctx.getSource(), StringArgumentType.getString(ctx, "name")))))
                )
        ));
    }

    private static int listPresets(FabricClientCommandSource source) {
        List<String> names = InventoryColoredSlots.presetNames();
        String active = InventoryColoredSlots.activePresetName();

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
        if (!InventoryColoredSlots.setPreset(name)) {
            source.sendError(Component.literal("Preset \"" + name + "\" not found."));
            return 0;
        }
        source.sendFeedback(Component.literal("Switched to preset \"" + name + "\"."));
        return 1;
    }

    private static int savePreset(FabricClientCommandSource source, String name) {
        if (!InventoryColoredSlots.savePreset(name)) {
            source.sendError(Component.literal("Preset name cannot be empty."));
            return 0;
        }
        source.sendFeedback(Component.literal("Saved current layout as preset \"" + name + "\"."));
        return 1;
    }

    private static int deletePreset(FabricClientCommandSource source, String name) {
        if (!InventoryColoredSlots.deletePreset(name)) {
            source.sendError(Component.literal("Preset \"" + name + "\" not found."));
            return 0;
        }
        source.sendFeedback(Component.literal("Deleted preset \"" + name + "\"."));
        return 1;
    }
}
