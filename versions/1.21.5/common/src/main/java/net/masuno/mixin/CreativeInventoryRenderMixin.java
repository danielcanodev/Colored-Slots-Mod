package net.masuno.mixin;

import net.masuno.inventory.InvColoredSlots;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.awt.Color;

import static net.masuno.inventory.InvColoredSlots.tickMouseSlot;

@Mixin(CreativeModeInventoryScreen.class)
public abstract class CreativeInventoryRenderMixin extends AbstractContainerScreen<CreativeModeInventoryScreen.ItemPickerMenu> {

	@Shadow
	private static CreativeModeTab selectedTab;

	public CreativeInventoryRenderMixin(CreativeModeInventoryScreen.ItemPickerMenu menu, Inventory inventory, Component title) {
		super(menu, inventory, title);
	}

	@Inject(method = "renderBg", at = @At("TAIL"))
	private void injectBackground(GuiGraphics graphics, float partialTick, int mouseX, int mouseY, CallbackInfo ci) {
		if (selectedTab == CreativeModeTabs.searchTab()) {
			int index = 0;
			int opacity = 125;
			int tool_color = new Color(255, 255, 255, opacity).getRGB();

			if (InvColoredSlots.isEditing) graphics.blit(RenderType::guiTextured, ResourceLocation.withDefaultNamespace(InvColoredSlots.CREATIVE_EDITING), this.leftPos, this.topPos, 0.0F, 0.0F, this.imageWidth, this.imageHeight, 256, 256);

			//Hotbar
			for (int i = 0; i < 9; i++) {
				Color rgb = InvColoredSlots.getColor(index);
				int color = new Color(rgb.getRed(), rgb.getGreen(), rgb.getBlue(), opacity).getRGB();

				graphics.blit(RenderType::guiTextured, ResourceLocation.withDefaultNamespace(InvColoredSlots.SLOT), this.leftPos + 9 + (i * 18), this.topPos + 112, 0.0F, 0.0F, 18, 18, 18, 18, color);
				if (InvColoredSlots.isSlotEmpty(index)) graphics.blit(RenderType::guiTextured, ResourceLocation.withDefaultNamespace(InvColoredSlots.getIcon(index)), this.leftPos + 9 + (i * 18), this.topPos + 112, 0.0F, 0.0F, 18, 18, 18, 18, tool_color);

				if (InvColoredSlots.isMouseInSlot(mouseX, mouseY, this.leftPos + 9 + (i * 18), this.topPos + 112)) {
					tickMouseSlot(index);
				}
				index++;
			}

			//Inv
			for (int y = 0; y < 3; y++) {
				for (int x = 0; x < 9; x++) {
					Color rgb = InvColoredSlots.getColor(index);
					int color = new Color(rgb.getRed(), rgb.getGreen(), rgb.getBlue(), opacity).getRGB();
					graphics.blit(RenderType::guiTextured, ResourceLocation.withDefaultNamespace(InvColoredSlots.SLOT), this.leftPos + 9 + (x * 18), this.topPos + 54 + (y * 18), 0.0F, 0.0F, 18, 18, 18, 18, color);
					if (InvColoredSlots.isSlotEmpty(index)) graphics.blit(RenderType::guiTextured, ResourceLocation.withDefaultNamespace(InvColoredSlots.getIcon(index)), this.leftPos + 9 + (x * 18), this.topPos + 54 + (y * 18), 0.0F, 0.0F, 18, 18, 18, 18, tool_color);

					if (InvColoredSlots.isMouseInSlot(mouseX, mouseY, this.leftPos + 9 + (x * 18), this.topPos + 54 + (y * 18))) {
						tickMouseSlot(index);
					}
					index++;
				}
			}
		}
	}
}