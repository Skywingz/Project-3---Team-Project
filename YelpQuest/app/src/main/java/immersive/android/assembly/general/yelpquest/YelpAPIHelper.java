package immersive.android.assembly.general.yelpquest;

import java.util.ArrayList;


public class YelpAPIHelper {
    private static YelpAPIHelper infoArraySingleton = null;
    private static ArrayList<DetailObject> mInfoArray;

    private YelpAPIHelper(){
        mInfoArray = new ArrayList<DetailObject>();
    }

    static YelpAPIHelper getInstance() {
        if (infoArraySingleton == null) {
            infoArraySingleton = new YelpAPIHelper();
        }
        return infoArraySingleton;
    }

    public ArrayList<DetailObject> getmInfoArray() {
        return mInfoArray;
    }

    public void addInstance(DetailObject instance){
        mInfoArray.add(instance);
    }

    public void removeAll() {
        mInfoArray.clear();
    }

}
