package com.example.ubun17.fab00;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.yelp.clientlib.connection.YelpAPI;
import com.yelp.clientlib.connection.YelpAPIFactory;
import com.yelp.clientlib.entities.Business;
import com.yelp.clientlib.entities.Coordinate;
import com.yelp.clientlib.entities.Location;
import com.yelp.clientlib.entities.SearchResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    YelpAPI yelpAPI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String consumerkey, consumerSecret, token, tokenSecret;
        consumerkey = "c2LzoUcz0rLTPzChnw-94g";
        consumerSecret = "F4FBkmDJ_j0SQ7Zniw6CYVeg1Mk";
        token = "e1LlbERYgyXGV1lZgXAeOwWulAwKodh8";
        tokenSecret = "0hyn5LJB6e1KhRGGYDkJr_HKaE0";

        YelpAPIFactory apiFactory = new YelpAPIFactory(consumerkey, consumerSecret, token, tokenSecret);
        yelpAPI = apiFactory.createAPI();

YELPapiAsyncTask yelPapiAsyncTask = new YELPapiAsyncTask();
        yelPapiAsyncTask.execute();

//// locale params
//        params.put("lang", "en");



    }

    private class YELPapiAsyncTask extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... strings) {

            Map<String, String> params = new HashMap<>();

// general params
            params.put("term", "food");
            params.put("limit", "3");

            Call<SearchResponse> call = yelpAPI.search("San Francisco", params);

            try {
//                Response<SearchResponse> response = call.execute();
//                Response<SearchResponse> response = call.execute();
                SearchResponse response = call.execute().body();
                Coordinate test = response.businesses().get(0).location().coordinate();
                ArrayList<Business> businesses = response.businesses();
                Log.d("Tag", String.valueOf(businesses));
                Log.d("Tag", String.valueOf(test));
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }
    }}
