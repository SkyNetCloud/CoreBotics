package ca.skynetcloud.core_botics.common.mixin;

import ca.skynetcloud.core_botics.common.accessor.FurnaceAccessor;
import net.minecraft.block.entity.AbstractFurnaceBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(AbstractFurnaceBlockEntity.class)
public abstract class MixinAbstractFurnaceBlockEntity implements FurnaceAccessor {

    @Shadow private int cookingTimeSpent;
    @Shadow private int cookingTotalTime;
    @Shadow private int litTimeRemaining;

    @Override
    public int getCookingTimeSpent() { return cookingTimeSpent; }
    @Override
    public void setCookingTimeSpent(int value) { cookingTimeSpent = value; }
    @Override
    public int getCookingTotalTime() { return cookingTotalTime; }
    @Override
    public int getLitTimeRemaining() { return litTimeRemaining; }
}