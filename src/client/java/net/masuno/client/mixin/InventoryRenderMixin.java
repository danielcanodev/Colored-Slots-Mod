package net.masuno.client.mixin;

import net.masuno.client.inventory.InventoryColoredSlots;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.AbstractRecipeBookScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.gui.screens.recipebook.RecipeBookComponent;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.InventoryMenu;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.awt.*;

import static net.masuno.client.inventory.InventoryColoredSlots.tickMouseSlot;

@Mixin(InventoryScreen.class)
public abstract class InventoryRenderMixin extends AbstractRecipeBookScreen<InventoryMenu> {
	@Shadow
	private float xMouse;
	@Shadow
	private float yMouse;

	public InventoryRenderMixin(InventoryMenu menu, RecipeBookComponent<?> recipeBookComponent, Inventory inventory, Component title) {
		super(menu, recipeBookComponent, inventory, title);
	}
	@Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphicsExtractor;blit(Lcom/mojang/blaze3d/pipeline/RenderPipeline;Lnet/minecraft/resources/Identifier;IIFFIIII)V",shift = At.Shift.AFTER), method = "extractBackground")
	private void injectBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a, CallbackInfo ci) {
		int index = 0;
		int opacity = 125;
		int tool_opacity = 125;
		int tool_color = new Color(255, 255, 255,tool_opacity).getRGB();

		if (InventoryColoredSlots.isEditing) graphics.blit(RenderPipelines.GUI_TEXTURED, InventoryColoredSlots.EDITING, this.leftPos, this.topPos, 0.0F, 0.0F, this.imageWidth, this.imageHeight, 256, 256);

		//Hotbar
		for (int i = 0; i < 9; i++) {
			Color rgb = InventoryColoredSlots.getColor(index);
			int color = new Color(rgb.getRed(),rgb.getGreen(),rgb.getBlue(),opacity).getRGB();

			graphics.blit(RenderPipelines.GUI_TEXTURED, InventoryColoredSlots.SLOT, this.leftPos + 7 + (i * 18), this.topPos + 141, 0.0F, 0.0F, 18, 18, 18, 18,color);
			if (InventoryColoredSlots.isSlotEmpty(index)) graphics.blit(RenderPipelines.GUI_TEXTURED, InventoryColoredSlots.getIcon(index), this.leftPos + 7 + (i * 18), this.topPos + 141, 0.0F, 0.0F, 18, 18, 18, 18,tool_color);

			if (InventoryColoredSlots.isMouseInSlot(xMouse,yMouse, this.leftPos + 7 + (i * 18), this.topPos + 141)) {
				tickMouseSlot(index);
			}
			index++;
		}

		//Inv
		for (int y = 0; y < 3; y++) {
			for (int x = 0; x < 9; x++) {
				Color rgb = InventoryColoredSlots.getColor(index);
				int color = new Color(rgb.getRed(),rgb.getGreen(),rgb.getBlue(),opacity).getRGB();
				graphics.blit(RenderPipelines.GUI_TEXTURED, InventoryColoredSlots.SLOT, this.leftPos + 7 + (x * 18), this.topPos + 83 + (y * 18), 0.0F, 0.0F, 18, 18, 18, 18,color);
				if (InventoryColoredSlots.isSlotEmpty(index)) graphics.blit(RenderPipelines.GUI_TEXTURED, InventoryColoredSlots.getIcon(index), this.leftPos + 7 + (x * 18), this.topPos + 83 + (y * 18), 0.0F, 0.0F, 18, 18, 18, 18,tool_color);

				if (InventoryColoredSlots.isMouseInSlot(xMouse,yMouse, this.leftPos + 7 + (x * 18), this.topPos + 83 + (y * 18))) {
					tickMouseSlot(index);
				}
				index++;
			}
		}

		//Offhand
		Color rgb = InventoryColoredSlots.getColor(index);
		int color = new Color(rgb.getRed(),rgb.getGreen(),rgb.getBlue(),opacity).getRGB();
		graphics.blit(RenderPipelines.GUI_TEXTURED, InventoryColoredSlots.SLOT, this.leftPos + 76, this.topPos + 61, 0.0F, 0.0F, 18, 18, 18, 18,color);
		if (InventoryColoredSlots.isSlotEmpty(index)) graphics.blit(RenderPipelines.GUI_TEXTURED, InventoryColoredSlots.getIcon(index), this.leftPos + 76, this.topPos + 61, 0.0F, 0.0F, 18, 18, 18, 18,tool_color);

		if (InventoryColoredSlots.isMouseInSlot(xMouse,yMouse, this.leftPos + 76, this.topPos + 61)) {
			tickMouseSlot(index);
		}
	}
}
