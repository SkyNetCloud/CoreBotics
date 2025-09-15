package ca.skynetcloud.core_botics.client.screen.machine;

import ca.skynetcloud.core_botics.client.screen.handler.BiorayCollectorScreenHandler;
import ca.skynetcloud.core_botics.client.screen.handler.BiorayInfusionMatrixScreenHandler;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

import static ca.skynetcloud.core_botics.CoreBoticsMain.MODID;

public class BiorayInfusionMatrixScreen extends HandledScreen<BiorayInfusionMatrixScreenHandler> {

    private static final Identifier TEXTURE = Identifier.of(MODID,"textures/screen/infusion_matrix_screen.png");

    public BiorayInfusionMatrixScreen(BiorayInfusionMatrixScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
        this.backgroundHeight = 197;
        this.backgroundWidth =  190;
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

        int storedEntropy = handler.getStoredBioray();
        int maxEntropy = handler.getMaxEntropy();

        context.drawText(this.textRenderer, Text.translatable("gui.core_botics.label_stored_entropy", storedEntropy, maxEntropy), this.x + 9, this.y + 55, -12829636, false);

        String craftingStatus = handler.isCrafting() ? "Crafting..." : "Idle";
        int textX = this.x + 9;
        int textY = this.y + 25;
        int width = this.textRenderer.getWidth(craftingStatus);
        int height = 10;
        context.drawText(this.textRenderer, Text.literal(craftingStatus), textX, textY, -12829636, false);

        int storedBioray = handler.getStoredBioray();
        int maxBioray = handler.getMaxEntropy();

    }

    @Override
    protected void drawBackground(DrawContext context, float deltaTicks, int mouseX, int mouseY) {

        this.backgroundWidth = 197;
        this.backgroundHeight = 190;

        context.drawTexture(RenderPipelines.GUI_TEXTURED, TEXTURE, this.x, this.y, 0, 0, 175, 166,   this.backgroundWidth, this.backgroundHeight);

    }
}
