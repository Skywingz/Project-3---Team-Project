package com.example.ubun17.fab00;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.yelp.clientlib.connection.YelpAPI;
import com.yelp.clientlib.connection.YelpAPIFactory;
import com.yelp.clientlib.entities.Business;
import com.yelp.clientlib.entities.Coordinate;
import com.yelp.clientlib.entities.SearchResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;

public class MainActivity extends AppCompatActivity {
    YelpAPI yelpAPI;
    ArrayList<InfoBussiness> businessArray;
    private ListView mListView;
    private ArrayAdapter<String> mAdapter;
    ArrayList<String> arrayName = new ArrayList<String>();
    ArrayList<Double> arrayLatitute = new ArrayList<Double>();
    ArrayList<Double> arrayLongtitute = new ArrayList<>();

    EditText searchTerm;
    Button buSearch;



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

        searchTerm = (EditText) findViewById(R.id.searchEdit);
        String stSearch = searchTerm.getText().toString();

        SearchTerm searchTerm = SearchTerm.getInstance();
        searchTerm.setSearchTerm(stSearch);


        buSearch = (Button) findViewById(R.id.button);
        buSearch.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {


                YELPapiAsyncTask yelPapiAsyncTask = new YELPapiAsyncTask();
                yelPapiAsyncTask.execute();
            }
        });



        mListView = (ListView) findViewById(R.id.listView);

        mAdapter = new ArrayAdapter<>(MainActivity.this
                , android.R.layout.simple_list_item_1, arrayName);

        try {
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
       // Log.d("Test businessArry ///", arrayName.get(1));
        mListView.setAdapter(mAdapter);


//// locale params
//        params.put("lang", "en");
        ////////////////////////////////////////////////////////////
        ////begin your with busi
//                Log.d("Test businessArry ///", arrayName.get(1));
//                Log.d("Test businessArry ///",String.valueOf(arrayLatitute.get(1)) );
//                Log.d("Test businessArry ///", String.valueOf(arrayLongtitute.get(1)));
        //String test = arrayName.get(1);
        Toast.makeText(this,searchTerm.getmSearch(),Toast.LENGTH_LONG).show();



        /////end of your code

    }

    private class YELPapiAsyncTask extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... strings) {

            Map<String, String> params = new HashMap<>();

// general params
            SearchTerm searchTerm = SearchTerm.getInstance();

            params.put("term", searchTerm.getmSearch());
            params.put("limit", "3");

            Call<SearchResponse> call = yelpAPI.search("San Francisco", params);

            try {
//                Response<SearchResponse> response = call.execute();
//                Response<SearchResponse> response = call.execute();
                SearchResponse response = call.execute().body();

                ArrayList<Business> businesses = response.businesses();
                int businesSize = businesses.size();
                businessArray = new ArrayList<InfoBussiness>();

                InfoArraySingleton newArraySingle = InfoArraySingleton.getInstance();


                for (int i= 0; i < businesSize ;i ++) {
                    InfoBussiness infoBussiness = new InfoBussiness();
                    infoBussiness.setmName(businesses.get(i).name().toString());
                    arrayName.add(businesses.get(i).name().toString());

                    Coordinate coordinate = response.businesses().get(i).location().coordinate();
                    Double latitue = coordinate.latitude();
                    Double longtitue = coordinate.longitude();

                    arrayLatitute.add(latitue);
                    arrayLongtitute.add(longtitue);

                    infoBussiness.setmLatitude(latitue);
                    infoBussiness.setmLongtitude(longtitue);
                    Log.d("Name",infoBussiness.getmName());
                    Log.d("Name", String.valueOf(latitue));
                    Log.d("Name", String.valueOf(longtitue));
                    businessArray.add(infoBussiness);

                    newArraySingle.addInstance(infoBussiness);

                }

                Log.d("Tag", "Working");
                Log.d("Test businessArry ///", arrayName.get(1));
                searchTerm.setSearchTerm(arrayName.get(1));


            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }//End of doInBackground

//        @Override
//        protected void onPostExcute(InfoArraySingleton singleton) {
//            super.onPostExecute(singleton);
//
//        }
    }
}
