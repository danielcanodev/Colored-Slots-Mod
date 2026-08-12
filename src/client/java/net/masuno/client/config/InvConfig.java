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
    public HashMap<String, List<Vector2i>> SLOTS = new HashMap<>();

    @SerialEntry
    public List<Integer> COLORS = new ArrayList<>(Collections.nCopies(37, 0));
    @SerialEntry
    public List<Integer> ICONS = new ArrayList<>(Collections.nCopies(37, 0));
}