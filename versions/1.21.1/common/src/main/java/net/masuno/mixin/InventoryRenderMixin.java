package net.masuno.mixin;

import net.masuno.inventory.InvColoredSlots;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.EffectRenderingInventoryScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.InventoryMenu;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.awt.Color;

import static net.masuno.inventory.InvColoredSlots.tickMouseSlot;

@Mixin(InventoryScreen.class)
public abstract class InventoryRenderMixin extends EffectRenderingInventoryScreen<InventoryMenu> {

	public InventoryRenderMixin(InventoryMenu menu, Inventory inventory, Component title) {
		super(menu, inventory, title);
	}

	@Inject(method = "renderBg", at = @At("TAIL"))
	private void injectBackground(GuiGraphics graphics, float partialTick, int mouseX, int mouseY, CallbackInfo ci) {
		int index = 0;
		int opacity = 125;

		if (InvColoredSlots.isEditing) {
			graphics.blit(ResourceLocation.withDefaultNamespace(InvColoredSlots.EDITING), this.leftPos, this.topPos, this.imageWidth, this.imageHeight, 0.0F, 0.0F, 256, 256, 256, 256);
		}

		//Hotbar
		for (int i = 0; i < 9; i++) {
			Color rgb = InvColoredSlots.getColor(index);
			blitSlot(graphics, ResourceLocation.withDefaultNamespace(InvColoredSlots.SLOT), this.leftPos + 8 + (i * 18), this.topPos + 142, rgb, opacity);
			if (InvColoredSlots.isSlotEmpty(index)) blitSlot(graphics, ResourceLocation.withDefaultNamespace(InvColoredSlots.getIcon(index)), this.leftPos + 8 + (i * 18), this.topPos + 142, Color.WHITE, 125);

			if (InvColoredSlots.isMouseInSlot(mouseX, mouseY, this.leftPos + 8 + (i * 18), this.topPos + 142)) {
				tickMouseSlot(index);
			}
			index++;
		}

		//Inv
		for (int y = 0; y < 3; y++) {
			for (int x = 0; x < 9; x++) {
				Color rgb = InvColoredSlots.getColor(index);
				blitSlot(graphics, ResourceLocation.withDefaultNamespace(InvColoredSlots.SLOT), this.leftPos + 8 + (x * 18), this.topPos + 84 + (y * 18), rgb, opacity);
				if (InvColoredSlots.isSlotEmpty(index)) blitSlot(graphics, ResourceLocation.withDefaultNamespace(InvColoredSlots.getIcon(index)), this.leftPos + 8 + (x * 18), this.topPos + 84 + (y * 18), Color.WHITE, 125);

				if (InvColoredSlots.isMouseInSlot(mouseX, mouseY, this.leftPos + 8 + (x * 18), this.topPos + 84 + (y * 18))) {
					tickMouseSlot(index);
				}
				index++;
			}
		}
	}

	private static void blitSlot(GuiGraphics graphics, ResourceLocation texture, int x, int y, Color color, int alpha) {
		graphics.setColor(color.getRed() / 255.0F, color.getGreen() / 255.0F, color.getBlue() / 255.0F, alpha / 255.0F);
		graphics.blit(texture, x, y, 18, 18, 0.0F, 0.0F, 18, 18, 18, 18);
		graphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);
	}
}