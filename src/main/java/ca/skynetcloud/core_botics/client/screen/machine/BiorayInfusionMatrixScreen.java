package ca.skynetcloud.core_botics.client.screen.machine;

import ca.skynetcloud.core_botics.client.screen.handler.BiorayInfusionMatrixScreenHandler;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;

public class BiorayInfusionMatrixScreen extends HandledScreen<BiorayInfusionMatrixScreenHandler> {

    public BiorayInfusionMatrixScreen(BiorayInfusionMatrixScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
    }

    @Override
    protected void drawBackground(DrawContext context, float deltaTicks, int mouseX, int mouseY) {

    }
}
