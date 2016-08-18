package immersive.android.assembly.general.yelpquest;

/**
 * Created by Skywingz on 8/17/16.
 */
public class QuestObject {

    private int startTime;
    private int endTime;
    private String notificationMessage;
    private String queryInterest;
    private boolean includeFood;
    private String foodType;

    public QuestObject(int startTime, int endTime, String notificationMessage,
                       String queryInterest, boolean includeFood, String foodType) {

        this.startTime = startTime;
        this.endTime = endTime;
        this.notificationMessage = notificationMessage;
        this.queryInterest = queryInterest;
        this.includeFood = includeFood;
        this.foodType = foodType;
    }

    public int getStartTime() {
        return startTime;
    }

    public void setStartTime(int startTime) {
        this.startTime = startTime;
    }

    public int getEndTime() {
        return endTime;
    }

    public void setEndTime(int endTime) {
        this.endTime = endTime;
    }

    public String getNotificationMessage() {
        return notificationMessage;
    }

    public void setNotificationMessage(String notificationMessage) {
        this.notificationMessage = notificationMessage;
    }

    public String getQueryInterest() {
        return queryInterest;
    }

    public void setQueryInterest(String queryInterest) {
        this.queryInterest = queryInterest;
    }

    public boolean isIncludeFood() {
        return includeFood;
    }

    public void setIncludeFood(boolean includeFood) {
        this.includeFood = includeFood;
    }

    public String getFoodType() {
        return foodType;
    }

    public void setFoodType(String foodType) {
        this.foodType = foodType;
    }


    public void updateQuestObject(int startTime, int endTime, String notificationMessage,
                                  String queryInterest, boolean includeFood, String foodType) {

        this.startTime = startTime;
        this.endTime = endTime;
        this.notificationMessage = notificationMessage;
        this.queryInterest = queryInterest;
        this.includeFood = includeFood;
        this.foodType = foodType;
    }

}
