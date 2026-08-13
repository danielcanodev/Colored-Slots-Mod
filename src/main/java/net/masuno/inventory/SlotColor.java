package net.masuno.inventory;

import java.awt.Color;

public enum SlotColor {
	NONE(Color.WHITE),
	GRAY(new Color(45, 45, 45)),
	RED(new Color(235, 0, 30)),
	GREEN(new Color(4, 255, 52)),
	BLUE(new Color(63, 112, 255)),
	AQUA(new Color(38, 230, 255)),
	YELLOW(new Color(255, 213, 0)),
	ORANGE(new Color(255, 115, 0)),
	PINK(new Color(255, 55, 215));

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