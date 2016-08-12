package com.example.ubun17.fab00;

import android.os.AsyncTask;
import android.util.Log;

import com.yelp.clientlib.connection.YelpAPIFactory;
import com.yelp.clientlib.entities.Business;
import com.yelp.clientlib.entities.Coordinate;
import com.yelp.clientlib.entities.SearchResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;

/**
 * Created by ubun17 on 8/12/16.
 */
public class YelpAPI {
//    String mSearchTerm;
//    ArrayList<InfoBussiness> mBusinessArray;
//
//    public YelpAPI(String search) {
//        mSearchTerm = search;
//    }
//
//    String consumerkey, consumerSecret, token, tokenSecret;
//    consumerkey = "c2LzoUcz0rLTPzChnw-94g";
//    consumerSecret = "F4FBkmDJ_j0SQ7Zniw6CYVeg1Mk"
//    token = "e1LlbERYgyXGV1lZgXAeOwWulAwKodh8";
//    tokenSecret = "0hyn5LJB6e1KhRGGYDkJr_HKaE0";
//
//    YelpAPIFactory apiFactory = new YelpAPIFactory(consumerkey, consumerSecret, token, tokenSecret);
//    yelpAPI = apiFactory.createAPI();
//
//    public ArrayList<InfoBussiness> getBusninessList() {
//
//
//
//
//        YELPapiAsyncTask yelPapiAsyncTask = new YELPapiAsyncTask();
//        yelPapiAsyncTask.execute();
//
//        return null;
//    }
//
//    private class YELPapiAsyncTask extends AsyncTask<String, Void, Void> {
//
//        @Override
//        protected Void doInBackground(String... strings) {
//
//            Map<String, String> params = new HashMap<>();
//
//// general params
//            SearchTerm searchTerm = SearchTerm.getInstance();
//
//            params.put("term", searchTerm.getmSearch());
//            params.put("limit", "3");
//
//            Call<SearchResponse> call = yelpAPI.search("San Francisco", params);
//
//            try {
////                Response<SearchResponse> response = call.execute();
////                Response<SearchResponse> response = call.execute();
//                SearchResponse response = call.execute().body();
//
//                ArrayList<Business> businesses = response.businesses();
//                int businesSize = businesses.size();
//                businessArray = new ArrayList<InfoBussiness>();
//
//                InfoArraySingleton newArraySingle = InfoArraySingleton.getInstance();
//
//
//                for (int i= 0; i < businesSize ;i ++) {
//                    InfoBussiness infoBussiness = new InfoBussiness();
//                    infoBussiness.setmName(businesses.get(i).name().toString());
//                    arrayName.add(businesses.get(i).name().toString());
//
//                    Coordinate coordinate = response.businesses().get(i).location().coordinate();
//                    Double latitue = coordinate.latitude();
//                    Double longtitue = coordinate.longitude();
//
//                    arrayLatitute.add(latitue);
//                    arrayLongtitute.add(longtitue);
//
//                    infoBussiness.setmLatitude(latitue);
//                    infoBussiness.setmLongtitude(longtitue);
//                    Log.d("Name",infoBussiness.getmName());
//                    Log.d("Name", String.valueOf(latitue));
//                    Log.d("Name", String.valueOf(longtitue));
//                    businessArray.add(infoBussiness);
//
//                    newArraySingle.addInstance(infoBussiness);
//
//                }
//
//                Log.d("Tag", "Working");
//                Log.d("Test businessArry ///", arrayName.get(1));
//                searchTerm.setSearchTerm(arrayName.get(1));
//
//
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//            return null;
//        }//End of doInBackground
//
////        @Override
////        protected void onPostExcute(InfoArraySingleton singleton) {
////            super.onPostExecute(singleton);
////
////        }
//    }

}


