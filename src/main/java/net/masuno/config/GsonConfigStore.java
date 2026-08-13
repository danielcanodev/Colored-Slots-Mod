package net.masuno.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.masuno.ColoredSlotsInfo;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class GsonConfigStore implements ConfigStore<Config> {
	private final Gson gson = new GsonBuilder().setPrettyPrinting().create();
	private final Path file;
	private Config config = new Config();

	public GsonConfigStore(Path configDir) {
		this.file = configDir.resolve("colored_slots.json");
	}

	@Override
	public void load() {
		if (Files.exists(file)) {
			try (Reader reader = Files.newBufferedReader(file, StandardCharsets.UTF_8)) {
				Config loaded = gson.fromJson(reader, Config.class);
				if (loaded != null) {
					config = loaded;
				}
			} catch (IOException e) {
				ColoredSlotsInfo.LOGGER.error("Failed to load config from {}", file, e);
			}
		}
		if (config.presets.isEmpty()) {
			config.presets.put(config.activePreset, new Config.Preset());
		}
	}

	@Override
	public void save() {
		try {
			Files.createDirectories(file.getParent());
			try (Writer writer = Files.newBufferedWriter(file, StandardCharsets.UTF_8)) {
				gson.toJson(config, writer);
			}
		} catch (IOException e) {
			ColoredSlotsInfo.LOGGER.error("Failed to save config to {}", file, e);
		}
	}

	@Override
	public Config instance() {
		return config;
	}
}