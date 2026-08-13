package net.masuno.config;

public interface ConfigStore<T> {
	void load();
	void save();
	T instance();
}