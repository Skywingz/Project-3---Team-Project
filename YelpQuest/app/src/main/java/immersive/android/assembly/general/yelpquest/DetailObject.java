package immersive.android.assembly.general.yelpquest;

/**
 * Created by raymour on 8/17/16.
 */
public class DetailObject {

    private String mName, mSNtext, mSNurl, mRatingSurl, mRatingMurl, mAddress;
    private Double mLatitude, mLongtitude;

    public DetailObject() {}

    public String getmName() {return mName;}
    public Double getmLatitude() {return mLatitude;}
    public Double getmLongtitude() {return mLongtitude;}
    public String getmSNtext() {return mSNtext;}
    public String getmSNurl() {return  mSNurl;}
    public String getmRatingSurl() {return mRatingSurl;}
    public String getmRatingMurl() {return mRatingMurl;}
    public String getmAddress() {return mAddress;}

    public void setmName(String str) {mName = str;}
    public void setmLatitude(Double num) {mLatitude = num;}
    public void setmLongtitude(Double num) {mLongtitude = num;}
    public void setmSNtext(String str) {mSNtext = str;}
    public void setmSNurl(String str) {mSNurl = str;}
    public void setmRatingSurl(String str) {mRatingSurl = str;}
    public void setmRatingMurl(String str) {mRatingMurl = str;}
    public void setmAddress(String str) {mAddress = str;}

}