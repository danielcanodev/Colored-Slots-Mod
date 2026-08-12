package net.masuno.client.inventory;

import java.awt.Color;

public enum SlotColor {
	NONE(Color.WHITE),
	GRAY(new Color(45, 45, 45)),
	RED(new Color(235, 0, 30)),
	GREEN(new Color(0, 225, 45)),
	BLUE(new Color(0, 60, 236)),
	YELLOW(new Color(235, 235, 30)),
	ORANGE(new Color(235, 90, 15)),
	PINK(new Color(235, 60, 200));

	private final Color color;

	SlotColor(Color color) {
		this.color = color;
	}

	public Color color() {
		return color;
	}

	public SlotColor next() {
		SlotColor[] values = values();
		return values[(ordinal() + 1) % values.length];
	}

	public static SlotColor fromValue(int value) {
		SlotColor[] values = values();
		if (value < 0 || value >= values.length) return NONE;
		return values[value];
	}
}
