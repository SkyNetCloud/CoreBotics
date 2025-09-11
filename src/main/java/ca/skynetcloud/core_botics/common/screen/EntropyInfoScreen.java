package ca.skynetcloud.core_botics.common.screen;

import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import static ca.skynetcloud.core_botics.CoreBoticsMain.MODID;

public class EntropyInfoScreen extends HandledScreen<EntropyScreenHandler> {

    private static final Identifier TEXTURE = Identifier.of(MODID,"textures/gui/container/entropy_screen.png");

    public EntropyInfoScreen(EntropyScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
        this.backgroundHeight = 166;
        this.backgroundWidth =  175;
    }

    @Override
    protected void init() {
        super.init();
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float deltaTicks) {
        super.render(context, mouseX, mouseY, deltaTicks);
    }

    @Override
    public void renderBackground(DrawContext context, int mouseX, int mouseY, float deltaTicks) {
        super.renderBackground(context, mouseX, mouseY, deltaTicks);

        int storedEntropy = handler.getStoredEntropy();
        int maxEntropy = handler.getMaxEntropy();

        context.drawText(this.textRenderer, Text.translatable("gui.core_botics.entropyscreen.label_stored_entropy", storedEntropy, maxEntropy), this.x + 11, this.y + 36, -12829636, false);
    }

    @Override
    protected void drawBackground(DrawContext context, float deltaTicks, int mouseX, int mouseY) {
        context.drawTexture(RenderPipelines.GUI_TEXTURED, TEXTURE, this.x, this.y, 0, 0, this.backgroundWidth, this.backgroundHeight, this.backgroundWidth, this.backgroundHeight);
    }
}
