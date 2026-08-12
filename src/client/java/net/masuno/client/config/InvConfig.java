package net.masuno.client.config;

import com.google.gson.GsonBuilder;
import dev.isxander.yacl3.config.v2.api.ConfigClassHandler;
import dev.isxander.yacl3.config.v2.api.SerialEntry;
import dev.isxander.yacl3.config.v2.api.serializer.GsonConfigSerializerBuilder;
import net.fabricmc.loader.api.FabricLoader;
import net.masuno.ColoredSlots;
import net.minecraft.resources.Identifier;
import org.joml.Vector2i;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public class InvConfig {
    public static ConfigClassHandler<InvConfig> HANDLER = ConfigClassHandler.createBuilder(InvConfig.class)
            .id(Identifier.fromNamespaceAndPath(ColoredSlots.MOD_ID, "inv_config"))
            .serializer(config -> GsonConfigSerializerBuilder.create(config)
                    .setPath(FabricLoader.getInstance().getConfigDir().resolve("inv_config.json5"))
                    .appendGsonBuilder(GsonBuilder::setPrettyPrinting)
                    .setJson5(true)
                    .build())
            .build();

    @SerialEntry
    public String ACTIVE_PRESET = "default";

    @SerialEntry
    public LinkedHashMap<String, Preset> PRESETS = new LinkedHashMap<>();

    public static class Preset {
        @SerialEntry
        public HashMap<String, List<Vector2i>> SLOTS = new HashMap<>();

        @SerialEntry
        public List<Integer> COLORS = new ArrayList<>(Collections.nCopies(36, 0));
        @SerialEntry
        public List<Integer> ICONS = new ArrayList<>(Collections.nCopies(36, 0));

        public Preset copy() {
            Preset copy = new Preset();
            copy.COLORS = new ArrayList<>(COLORS);
            copy.ICONS = new ArrayList<>(ICONS);
            for (var entry : SLOTS.entrySet()) {
                copy.SLOTS.put(entry.getKey(), new ArrayList<>(entry.getValue()));
            }
            return copy;
        }
    }
}
