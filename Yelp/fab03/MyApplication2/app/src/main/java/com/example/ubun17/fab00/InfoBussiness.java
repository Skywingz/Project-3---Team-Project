package com.example.ubun17.fab00;

/**
 * Created by ubun17 on 8/12/16.
 */
public class InfoBussiness {
    String mName, mSNtext, mSNurl, mRatingSurl, mRatingMurl, mAddress;
    Double mLatitude, mLongtitude;

    public InfoBussiness() {

    }

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
