package ca.skynetcloud.core_botics.common.accessor;

public interface FurnaceAccessor {
    int getCookingTimeSpent();
    void setCookingTimeSpent(int value);
    int getCookingTotalTime();
    int getLitTimeRemaining();
}
