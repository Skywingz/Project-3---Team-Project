package immersive.android.assembly.general.yelpquest;


public class CompletedQuestMarkerObjects {

    private double latitude;
    private double longitude;
    private String businessName;
    private String businessAddress;

    public CompletedQuestMarkerObjects(double latitude, double longitude, String businessName, String businessAddress) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.businessName = businessName;
        this.businessAddress = businessAddress;
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


    public void updateCompletedQuestMarkerObjects(double latitude, double longitude, String businessName, String businessAddress) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.businessName = businessName;
        this.businessAddress = businessAddress;
    }

}
