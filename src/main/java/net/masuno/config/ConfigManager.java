package net.masuno.config;

public final class ConfigManager {
	private static ConfigStore<Config> store;

	public static void init(ConfigStore<Config> configStore) {
		store = configStore;
		store.load();
	}

	public static Config get() {
		return store.instance();
	}
	public static void save() {
		store.save();
	}
}