package net.masuno.client.mixin;

import net.fabricmc.fabric.api.client.creativetab.v1.FabricCreativeModeInventoryScreen;
import net.masuno.client.inventory.InvColoredSlots;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.awt.*;

import static net.masuno.client.inventory.InvColoredSlots.tickMouseSlot;

@Mixin(CreativeModeInventoryScreen.class)
public class CreativeInventoryRenderMixin extends AbstractContainerScreen<CreativeModeInventoryScreen.ItemPickerMenu>
        implements FabricCreativeModeInventoryScreen {

    @Shadow
    private static CreativeModeTab selectedTab;

    public CreativeInventoryRenderMixin(CreativeModeInventoryScreen.ItemPickerMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
    }

    @Inject(method = "extractBackground", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphicsExtractor;blit(Lcom/mojang/blaze3d/pipeline/RenderPipeline;Lnet/minecraft/resources/Identifier;IIFFIIII)V",shift = At.Shift.AFTER))
    private void injectBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a, CallbackInfo ci) {
        if (selectedTab == CreativeModeTabs.tabs().get(12)){
            int index = 0;
            int opacity = 125;
            int tool_opacity = 125;
            int tool_color = new Color(255, 255, 255,tool_opacity).getRGB();

            if (InvColoredSlots.isEditing) graphics.blit(RenderPipelines.GUI_TEXTURED, InvColoredSlots.CREATIVE_EDITING, this.leftPos, this.topPos, 0.0F, 0.0F, this.imageWidth, this.imageHeight, 256, 256);

            //Hotbar
            for (int i = 0; i < 9; i++) {
                Color rgb = InvColoredSlots.getColor(index);
                int color = new Color(rgb.getRed(),rgb.getGreen(),rgb.getBlue(),opacity).getRGB();

                graphics.blit(RenderPipelines.GUI_TEXTURED, InvColoredSlots.SLOT, this.leftPos + 8 + (i * 18), this.topPos + 111, 0.0F, 0.0F, 18, 18, 18, 18,color);
                if (InvColoredSlots.isSlotEmpty(index)) graphics.blit(RenderPipelines.GUI_TEXTURED, InvColoredSlots.getIcon(index), this.leftPos + 8 + (i * 18), this.topPos + 111, 0.0F, 0.0F, 18, 18, 18, 18,tool_color);

                if (InvColoredSlots.isMouseInSlot(mouseX,mouseY, this.leftPos + 8 + (i * 18), this.topPos + 111)) {
                    tickMouseSlot(index);
                }
                index++;
            }

            //Inv
            for (int y = 0; y < 3; y++) {
                for (int x = 0; x < 9; x++) {
                    Color rgb = InvColoredSlots.getColor(index);
                    int color = new Color(rgb.getRed(),rgb.getGreen(),rgb.getBlue(),opacity).getRGB();
                    graphics.blit(RenderPipelines.GUI_TEXTURED, InvColoredSlots.SLOT, this.leftPos + 8 + (x * 18), this.topPos + 53 + (y * 18), 0.0F, 0.0F, 18, 18, 18, 18,color);
                    if (InvColoredSlots.isSlotEmpty(index)) graphics.blit(RenderPipelines.GUI_TEXTURED, InvColoredSlots.getIcon(index), this.leftPos + 8 + (x * 18), this.topPos + 53 + (y * 18), 0.0F, 0.0F, 18, 18, 18, 18,tool_color);

                    if (InvColoredSlots.isMouseInSlot(mouseX,mouseY, this.leftPos + 8 + (x * 18), this.topPos + 53 + (y * 18))) {
                        tickMouseSlot(index);
                    }
                    index++;
                }
            }
        }
    }

}
