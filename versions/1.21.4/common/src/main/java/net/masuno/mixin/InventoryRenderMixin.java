package net.masuno.mixin;

import net.masuno.inventory.InvColoredSlots;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractRecipeBookScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.gui.screens.recipebook.RecipeBookComponent;
import net.minecraft.client.renderer.RenderType;
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
public abstract class InventoryRenderMixin extends AbstractRecipeBookScreen<InventoryMenu> {

	public InventoryRenderMixin(InventoryMenu menu, RecipeBookComponent<?> recipeBookComponent, Inventory inventory, Component title) {
		super(menu, recipeBookComponent, inventory, title);
	}

	@Inject(method = "renderBg", at = @At("TAIL"))
	private void injectBackground(GuiGraphics graphics, float partialTick, int mouseX, int mouseY, CallbackInfo ci) {
		int index = 0;
		int opacity = 125;
		int tool_color = new Color(255, 255, 255, opacity).getRGB();

		if (InvColoredSlots.isEditing) graphics.blit(RenderType::guiTextured, ResourceLocation.withDefaultNamespace(InvColoredSlots.EDITING), this.leftPos, this.topPos, 0.0F, 0.0F, this.imageWidth, this.imageHeight, 256, 256);

		//Hotbar
		for (int i = 0; i < 9; i++) {
			Color rgb = InvColoredSlots.getColor(index);
			int color = new Color(rgb.getRed(), rgb.getGreen(), rgb.getBlue(), opacity).getRGB();

			graphics.blit(RenderType::guiTextured, ResourceLocation.withDefaultNamespace(InvColoredSlots.SLOT), this.leftPos + 8 + (i * 18), this.topPos + 142, 0.0F, 0.0F, 18, 18, 18, 18, color);
			if (InvColoredSlots.isSlotEmpty(index)) graphics.blit(RenderType::guiTextured, ResourceLocation.withDefaultNamespace(InvColoredSlots.getIcon(index)), this.leftPos + 8 + (i * 18), this.topPos + 142, 0.0F, 0.0F, 18, 18, 18, 18, tool_color);

			if (InvColoredSlots.isMouseInSlot(mouseX, mouseY, this.leftPos + 8 + (i * 18), this.topPos + 142)) {
				tickMouseSlot(index);
			}
			index++;
		}

		//Inv
		for (int y = 0; y < 3; y++) {
			for (int x = 0; x < 9; x++) {
				Color rgb = InvColoredSlots.getColor(index);
				int color = new Color(rgb.getRed(), rgb.getGreen(), rgb.getBlue(), opacity).getRGB();
				graphics.blit(RenderType::guiTextured, ResourceLocation.withDefaultNamespace(InvColoredSlots.SLOT), this.leftPos + 8 + (x * 18), this.topPos + 84 + (y * 18), 0.0F, 0.0F, 18, 18, 18, 18, color);
				if (InvColoredSlots.isSlotEmpty(index)) graphics.blit(RenderType::guiTextured, ResourceLocation.withDefaultNamespace(InvColoredSlots.getIcon(index)), this.leftPos + 8 + (x * 18), this.topPos + 84 + (y * 18), 0.0F, 0.0F, 18, 18, 18, 18, tool_color);

				if (InvColoredSlots.isMouseInSlot(mouseX, mouseY, this.leftPos + 8 + (x * 18), this.topPos + 84 + (y * 18))) {
					tickMouseSlot(index);
				}
				index++;
			}
		}
	}
}