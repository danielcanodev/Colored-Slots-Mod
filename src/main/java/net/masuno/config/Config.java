package net.masuno.config;

import com.google.gson.annotations.SerializedName;
import org.joml.Vector2i;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public class Config {
	@SerializedName("ACTIVE_PRESET")
	public String activePreset = "default";
	@SerializedName("PRESETS")
	public LinkedHashMap<String, Preset> presets = new LinkedHashMap<>();

	public static class Preset {
		@SerializedName("SLOTS")
		public HashMap<String, List<Vector2i>> slots = new HashMap<>();
		@SerializedName("COLORS")
		public List<Integer> colors = new ArrayList<>(Collections.nCopies(36, 0));
		@SerializedName("ICONS")
		public List<Integer> icons = new ArrayList<>(Collections.nCopies(36, 0));

		public Preset copy() {
			Preset copy = new Preset();
			copy.colors = new ArrayList<>(colors);
			copy.icons = new ArrayList<>(icons);
			for (var entry : slots.entrySet()) {
				copy.slots.put(entry.getKey(), new ArrayList<>(entry.getValue()));
			}
			return copy;
		}
	}
}