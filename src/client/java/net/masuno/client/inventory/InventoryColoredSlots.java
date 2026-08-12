package net.masuno.client.inventory;

import net.masuno.client.config.InvConfig;
import net.masuno.client.input.InventoryInput;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvents;

import java.awt.*;
import java.util.Arrays;
import java.util.List;

public class InventoryColoredSlots {
    public static boolean isEditing = false;
    public static final List<Color> PALETTE = Arrays.asList(
            Color.WHITE,
            new Color(45, 45, 45),
            new Color(235,0,30),
            new Color(0,225,45),
            new Color(0,60,236),
            new Color(235,235,30),
            new Color(235,90,15),
            new Color(235,60,200)
    );

    public static final Identifier EDITING = Identifier.withDefaultNamespace("textures/gui/container/editing.png");
    public static final Identifier SLOT = Identifier.withDefaultNamespace("textures/gui/container/slot.png");

    public static final int ICON_COUNT = 5;
    public static final Identifier EMPTY = Identifier.withDefaultNamespace("textures/gui/container/icon/empty.png");
    public static final Identifier SWORD = Identifier.withDefaultNamespace("textures/gui/container/icon/sword.png");
    public static final Identifier PICK = Identifier.withDefaultNamespace("textures/gui/container/icon/pick.png");
    public static final Identifier FOOD = Identifier.withDefaultNamespace("textures/gui/container/icon/food.png");
    public static final Identifier BLOCK = Identifier.withDefaultNamespace("textures/gui/container/icon/block.png");

    public static void TickMouseSlot(int index){
        if (!isSlotEmpty(index)) return;
        if (!isHeldEmpty()) return;
        if (!isEditing) return;

        if (InventoryInput.ColorKeyPressed && !InventoryInput.wasHoldingColorKey){
            if (InventoryInput.Sneaking) ResetSlotColor(index);
            else SwapSlotColor(index);
            InventoryInput.AfterColorKey();

            Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
        }

        if (InventoryInput.IconKeyPressed && !InventoryInput.wasHoldingIconKey){
            if (InventoryInput.Sneaking) ResetSlotIcon(index);
            else SwapSlotIcon(index);
            InventoryInput.AfterIconKey();

            Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
        }
    }
    public static void SwapSlotIcon(int index){
        int v = InvConfig.HANDLER.instance().ICONS.get(index);

        v++;
        if (v >= ICON_COUNT) v -= ICON_COUNT;

        InvConfig.HANDLER.instance().ICONS.set(index,v);
        InvConfig.HANDLER.save();
    }
    public static void SwapSlotColor(int index){
        int v = InvConfig.HANDLER.instance().COLORS.get(index);

        v++;
        if (v >= PALETTE.size()) v -= PALETTE.size();

        InvConfig.HANDLER.instance().COLORS.set(index,v);
        InvConfig.HANDLER.save();
    }
    public static void ResetSlotColor(int index){
        InvConfig.HANDLER.instance().COLORS.set(index,0);
        InvConfig.HANDLER.save();
    }
    public static void ResetSlotIcon(int index){
        InvConfig.HANDLER.instance().ICONS.set(index,0);
        InvConfig.HANDLER.save();
    }

    public static boolean isMouseInSlot(float xm, float ym, int x, int y){
        int s = 18;
        return xm > x && xm < x + s && ym > y && ym < y + s;
    }

    public static boolean isHeldEmpty(){
        if (Minecraft.getInstance().player == null) return true;
        return Minecraft.getInstance().player.containerMenu.getCarried().isEmpty();
    }

    public static boolean isSlotEmpty(int index){
        if (Minecraft.getInstance().player == null) return true;
        return Minecraft.getInstance().player.getInventory().getItem(index).isEmpty();
    }

    public static Identifier GetIcon(int slot) {
        int value = InvConfig.HANDLER.instance().ICONS.get(slot);

        return switch (value) {
            case 1 -> SWORD;
            case 2 -> PICK;
            case 3 -> FOOD;
            case 4 -> BLOCK;
            default -> EMPTY;
        };
    }

    public static Color GetColor(int slot) {
        int value = InvConfig.HANDLER.instance().COLORS.get(slot);
        return PALETTE.get(value);
    }
}
