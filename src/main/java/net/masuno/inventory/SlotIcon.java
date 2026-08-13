package net.masuno.inventory;

public enum SlotIcon {
	EMPTY("textures/gui/container/icon/empty.png"),
	SWORD("textures/gui/container/icon/sword.png"),
	PICK("textures/gui/container/icon/pick.png"),
	FOOD("textures/gui/container/icon/food.png"),
	BLOCK("textures/gui/container/icon/block.png"),
	MACE("textures/gui/container/icon/mace.png"),
	SPEAR("textures/gui/container/icon/spear.png"),
	PEARL("textures/gui/container/icon/pearl.png"),
	ARROW("textures/gui/container/icon/arrow.png"),
	FIREWORK("textures/gui/container/icon/firework.png"),
	ELYTRA("textures/gui/container/icon/elytra.png"),
	AXE("textures/gui/container/icon/axe.png");

	private final String path;

	SlotIcon(String path) {
		this.path = path;
	}

	public String path() {
		return path;
	}

	public SlotIcon next() {
		SlotIcon[] values = values();
		return values[(ordinal() + 1) % values.length];
	}

	public static SlotIcon fromValue(int value) {
		SlotIcon[] values = values();
		if (value < 0 || value >= values.length) return EMPTY;
		return values[value];
	}
}