package blue.aodev.memory.data.goal;

import com.google.gson.annotations.SerializedName;

/**
 * A goal, from the goals API.
 */
public class Goal {
    @SerializedName("goal_items") private GoalItem[] goalItems;
    private String title;

    public String getTitle() {
        return title;
    }

    public GoalItem[] getGoalItems() {
        return goalItems;
    }
}
