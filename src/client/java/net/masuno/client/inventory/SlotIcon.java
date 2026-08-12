package net.masuno.client.inventory;

import net.minecraft.resources.Identifier;

public enum SlotIcon {
	EMPTY(Identifier.withDefaultNamespace("textures/gui/container/icon/empty.png")),
	SWORD(Identifier.withDefaultNamespace("textures/gui/container/icon/sword.png")),
	PICK(Identifier.withDefaultNamespace("textures/gui/container/icon/pick.png")),
	FOOD(Identifier.withDefaultNamespace("textures/gui/container/icon/food.png")),
	BLOCK(Identifier.withDefaultNamespace("textures/gui/container/icon/block.png")),
	MACE(Identifier.withDefaultNamespace("textures/gui/container/icon/mace.png")),
	SPEAR(Identifier.withDefaultNamespace("textures/gui/container/icon/spear.png")),
	PEARL(Identifier.withDefaultNamespace("textures/gui/container/icon/pearl.png")),
	ARROW(Identifier.withDefaultNamespace("textures/gui/container/icon/arrow.png")),
	FIREWORK(Identifier.withDefaultNamespace("textures/gui/container/icon/firework.png")),
	ELYTRA(Identifier.withDefaultNamespace("textures/gui/container/icon/elytra.png")),
	AXE(Identifier.withDefaultNamespace("textures/gui/container/icon/axe.png"));
	private final Identifier texture;

	SlotIcon(Identifier texture) {
		this.texture = texture;
	}

	public Identifier texture() {
		return texture;
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
