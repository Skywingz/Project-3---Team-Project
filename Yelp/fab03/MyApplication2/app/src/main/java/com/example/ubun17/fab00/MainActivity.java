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
    ArrayList<String> arrayAll = new ArrayList<>();

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

        buSearch = (Button) findViewById(R.id.button);
        searchTerm = (EditText) findViewById(R.id.searchEdit);
        buSearch.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                String stSearch = searchTerm.getText().toString();

                SearchTerm searchTerm = SearchTerm.getInstance();
                searchTerm.setSearchTerm(stSearch);

                YELPapiAsyncTask yelPapiAsyncTask = new YELPapiAsyncTask();
                yelPapiAsyncTask.execute();

                try {
                    TimeUnit.SECONDS.sleep(3);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                InfoArraySingleton newAsdf = InfoArraySingleton.getInstance();
                String stFirst = newAsdf.getmInfoArray().get(0).getmName();
                Log.d("asdfasf", stFirst);
            }
        });

    }

    private class YELPapiAsyncTask extends AsyncTask<String, Void, Void> {
        ArrayList<String> mAsdfArray;

        @Override
        protected Void doInBackground(String... strings) {

            Map<String, String> params = new HashMap<>();

// general params
            SearchTerm searchTerm = SearchTerm.getInstance();

            params.put("term", searchTerm.getmSearch());
            params.put("limit", "10");

            Call<SearchResponse> call = yelpAPI.search("new york", params);

            try {
                SearchResponse response = call.execute().body();

                ArrayList<Business> businesses = response.businesses();
                int businesSize = businesses.size();
                businessArray = new ArrayList<InfoBussiness>();

                InfoArraySingleton newArraySingle = InfoArraySingleton.getInstance();
                newArraySingle.removeAll();

                arrayName.clear();
                arrayLatitute.clear();
                arrayLongtitute.clear();
                arrayAll.clear();

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
                    businessArray.add(infoBussiness);

                    String stAll = businesses.get(i).name().toString()+ "/ Lat:"+latitue+"/ Long: "+ longtitue;
                    arrayAll.add(stAll);

                    newArraySingle.addInstance(infoBussiness);
                }
                mAsdfArray = arrayName;

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }//End of doInBackground

        @Override
        protected void onPostExecute(Void aVoid) {

            InfoArraySingleton newArraySingleOne = InfoArraySingleton.getInstance();
            mListView = (ListView) findViewById(R.id.listView);
            mAdapter = new ArrayAdapter<>(MainActivity.this
                    , android.R.layout.simple_list_item_1, arrayAll);
            mListView.setAdapter(mAdapter);

        }
    }
}
