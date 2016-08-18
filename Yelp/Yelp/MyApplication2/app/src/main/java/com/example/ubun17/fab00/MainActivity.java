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
    ArrayList<String> arrayLatitute = new ArrayList<String>();
    ArrayList<String> arrayLongtitute = new ArrayList<>();
    ArrayList<String> arraySNtext = new ArrayList<>();
    ArrayList<String> arraySNimageURL = new ArrayList<>();
    ArrayList<String> arrayRating_S_URL = new ArrayList<>();
    ArrayList<String> arrayRating_M_URL = new ArrayList<>();
    ArrayList<String> arrayAddress = new ArrayList<>();
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
                //////////////////////////////////////////////////////////////////
                InfoArraySingleton newYelp = InfoArraySingleton.getInstance();

                ArrayList<InfoBussiness> arrayYelp = newYelp.getmInfoArray();
                ///////////////You can get your data in for loop..

                for(int i = 0; i < arrayYelp.size(); i++) {
                    InfoBussiness eachBusiness = arrayYelp.get(i);

                    String name = eachBusiness.getmName();
                    String latitute = eachBusiness.getmLatitude().toString();
                    String longtitute = eachBusiness.getmLongtitude().toString();
                    String SNtext = eachBusiness.getmSNtext();
                    String SNimageURL = eachBusiness.getmSNurl();
                    String Rating_S_url = eachBusiness.getmRatingSurl();
                    String Rating_M_url = eachBusiness.getmRatingMurl();
                    String addressFull = eachBusiness.getmAddress();

                    arrayName.add(name);
                    arrayLatitute.add(latitute);
                    arrayLongtitute.add(longtitute);
                    arraySNtext.add(SNtext);
                    arraySNimageURL.add(SNimageURL);
                    arrayRating_S_URL.add(Rating_S_url);
                    arrayRating_M_URL.add(Rating_M_url);
                    arrayAddress.add(addressFull);
                    arrayAll.add(name+": "+ SNtext + "///" + addressFull);

                    Log.d("It's onClick View", SNtext);
                }//End of for loop


                mListView = (ListView) findViewById(R.id.listView);
                mAdapter = new ArrayAdapter<>(MainActivity.this
                    , android.R.layout.simple_list_item_1, arrayName);
                mListView.setAdapter(mAdapter);

            }//End of public void onClick(View v)
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

                InfoArraySingleton newArraySingle = InfoArraySingleton.getInstance();
                newArraySingle.removeAll();

                arrayName.clear();
                arrayLatitute.clear();
                arrayLongtitute.clear();
                arrayAll.clear();
                arraySNtext.clear();
                arraySNimageURL.clear();
                arrayRating_S_URL.clear();
                arrayRating_M_URL.clear();
                arrayAddress.clear();

                for (int i= 0; i < businesSize ;i ++) {
                    String stName = businesses.get(i).name().toString();

                    Coordinate coordinate = response.businesses().get(i).location().coordinate();

                    Double latitue = coordinate.latitude();
                    Double longtitue = coordinate.longitude();
                    String stSNtext = businesses.get(i).snippetText();
                    String stSNimageURL = businesses.get(i).snippetImageUrl();
                    String stRatingSurl = businesses.get(i).ratingImgUrlSmall();
                    String stRatingMurl = businesses.get(i).ratingImgUrl();
                    String addressStreet = businesses.get(i).location().displayAddress().get(0);
                    String addressCity = businesses.get(i).location().city();
                    String addressState = businesses.get(i).location().stateCode();
                    String addressZip = businesses.get(i).location().postalCode();
                    String addressFull = addressStreet+" "+ addressCity+" "
                            + addressState+" "+addressZip;

                    InfoBussiness infoBussiness = new InfoBussiness();
                    infoBussiness.setmName(stName);
                    infoBussiness.setmLatitude(latitue);
                    infoBussiness.setmLongtitude(longtitue);
                    infoBussiness.setmSNtext(stSNtext);
                    infoBussiness.setmSNurl(stSNimageURL);
                    infoBussiness.setmRatingSurl(stRatingSurl);
                    infoBussiness.setmRatingMurl(stRatingMurl);
                    infoBussiness.setmAddress(addressFull);

                    newArraySingle.addInstance(infoBussiness);

                }

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }//End of doInBackground

        @Override
        protected void onPostExecute(Void aVoid) {

        }
    }
}
