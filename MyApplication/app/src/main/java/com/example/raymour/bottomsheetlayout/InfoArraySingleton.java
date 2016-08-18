package com.example.raymour.bottomsheetlayout;

import java.util.ArrayList;

/**
 * Created by raymour on 8/17/16.
 */
public class InfoArraySingleton {
    private static InfoArraySingleton infoArraySingleton = null;
    private static ArrayList<InfoBussiness> mInfoArray;

    private InfoArraySingleton(){
        mInfoArray = new ArrayList<InfoBussiness>();
    }

    static InfoArraySingleton getInstance() {
        if (infoArraySingleton == null) {
            infoArraySingleton = new InfoArraySingleton();
        }
        return infoArraySingleton;
    }

    public ArrayList<InfoBussiness> getmInfoArray() {
        return mInfoArray;
    }

    public void addInstance(InfoBussiness instance){
        mInfoArray.add(instance);
    }

    public void removeAll() {
        mInfoArray.clear();
    }

}

