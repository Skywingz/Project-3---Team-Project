package immersive.android.assembly.general.yelpquest;

/**
 * Created by Skywingz on 8/16/16.
 */
public class MarkerObject {

    private String markerTag;
    private double latitude;
    private double longitude;
    private String businessName;
    private String businessAddress;
    private boolean onQuest;
    private int markerStatus;   // 1 = Undiscovered(red) ; 2 = Discovered(yellow) ; 3 = Selected(green)
                                // 4 = Quest New(cyan) ; 5 = Quest Completed(yellow) ; 6 = Quest Selected(green)
                                // 7 = Quest Completion Available(violet)
    private String unlockTime;



    public MarkerObject(String markerTag, double latitude, double longitude, String businessName,
                        String businessAddress, boolean onQuest, int markerStatus, String unlockTime) {
        this.markerTag = markerTag;
        this.latitude = latitude;
        this.longitude = longitude;
        this.businessName = businessName;
        this.businessAddress = businessAddress;
        this.onQuest = onQuest;
        this.markerStatus = markerStatus;
        this.unlockTime = unlockTime;
    }

    public String getMarkerTag() {
        return markerTag;
    }

    public void setMarkerTag(String markerTag) {
        this.markerTag = markerTag;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public String getBusinessAddress() {
        return businessAddress;
    }

    public void setBusinessAddress(String businessAddress) {
        this.businessAddress = businessAddress;
    }

    public boolean isOnQuest() {
        return onQuest;
    }

    public void setOnQuest(boolean onQuest) {
        this.onQuest = onQuest;
    }

    public int getMarkerStatus() {
        return markerStatus;
    }

    public void setMarkerStatus(int markerStatus) {
        this.markerStatus = markerStatus;
    }

    public String getUnlockTime() {
        return unlockTime;
    }

    public void setUnlockTime(String unlockTime) {
        this.unlockTime = unlockTime;
    }


    public void updateMarkerObject(String markerTag, double latitude, double longitude, String businessName,
                        String businessAddress, boolean onQuest, int markerStatus, String unlockTime) {

        this.markerTag = markerTag;
        this.latitude = latitude;
        this.longitude = longitude;
        this.businessName = businessName;
        this.businessAddress = businessAddress;
        this.onQuest = onQuest;
        this.markerStatus = markerStatus;
        this.unlockTime = unlockTime;
    }

}
