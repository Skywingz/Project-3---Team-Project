package com.example.raymour.bottomsheetlayout;

import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.squareup.picasso.Picasso;
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
import java.util.concurrent.TimeUnit;

import retrofit2.Call;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    BottomSheetBehavior bottomSheetBehavior;

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
    Button buttonSearch;
    Context context;

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

        buttonSearch = (Button) findViewById(R.id.button);
        searchTerm = (EditText) findViewById(R.id.searchEdit);

        buttonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String stSearch = searchTerm.getText().toString();

                SearchTerm searchTerm = SearchTerm.getInstance();
                searchTerm.setSearchTerm(stSearch);

                YelpAPIAsyncTask yelpAPIAsyncTask = new YelpAPIAsyncTask();
                yelpAPIAsyncTask.execute();

                try {
                    TimeUnit.SECONDS.sleep(3);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                InfoArraySingleton newYelp = InfoArraySingleton.getInstance();

                ArrayList<InfoBussiness> arrayYelp = newYelp.getmInfoArray();

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

                    Log.d("It's onClick View", name);
                }
                mListView = (ListView) findViewById(R.id.listView);
                mAdapter = new ArrayAdapter<>(MainActivity.this
                        , android.R.layout.simple_list_item_1, arrayAddress);
                mListView.setAdapter(mAdapter);

                TextView textViewAddress = (TextView) findViewById(R.id.textview_business_address);
                textViewAddress.setText(arrayAddress.get(0));
                //returns address to bottomsheet
                TextView textViewName = (TextView) findViewById(R.id.textview_business_name);
                textViewName.setText(arrayName.get(0));
                //returns business name to bottomsheet
                TextView textViewSnippetReview = (TextView) findViewById(R.id.textview_business_summary);
                textViewSnippetReview.setText(arraySNtext.get(0));
                //returns business summary to bottomsheet
                ImageView ratingsImageSmall = (ImageView) findViewById(R.id.yelp_rating_imageSmall);
                Picasso.with(getApplicationContext()).load(arrayRating_S_URL.get(0)).into(ratingsImageSmall);
                //returns image to bottomsheet
                ImageView ratingsImage = (ImageView) findViewById(R.id.yelp_rating_image);
                Picasso.with(getApplicationContext()).load(arrayRating_M_URL.get(0)).into(ratingsImage);
                //returns business summary to bottomsheet
                ImageView businessImage = (ImageView) findViewById(R.id.imageview_business);
                Picasso.with(getApplicationContext()).load(arraySNimageURL.get(0)).into(businessImage);
                //returns business summary to bottomsheet

            }
        });

        View bottomSheet = findViewById(R.id.bottom_sheet);
        Button button1 = (Button) findViewById(R.id.button_1);
        Button button3 = (Button) findViewById(R.id.button_3);

        button1.setOnClickListener(this);
        button3.setOnClickListener(this);

        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);

        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_COLLAPSED){
                    bottomSheetBehavior.setPeekHeight(0);
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
            }
        });

        RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.modalViewLayout);
        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetBehavior.setPeekHeight(1050);
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                expandBottomSheet();
            }
        });


    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.button_1:
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                break;

            case R.id.button_3:
                bottomSheetBehavior.setPeekHeight(335);
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);

                //Must be linked to Restaurant/Business Marker... when clicked preview of
                //restaurant/business appears displaying name/rating/small picture (peek mode)

                break;
                }
            }

    private void expandBottomSheet(){
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.snippetViewLayout);
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            }
        });
        }

    private class YelpAPIAsyncTask extends AsyncTask<String, Void, Void>{

        ArrayList<String> mAsdfArray;

        @Override
        protected Void doInBackground(String... strings) {
            Map<String, String> params = new HashMap<>();

            SearchTerm searchTerm = SearchTerm.getInstance();
            params.put("term", searchTerm.getmSearch());
            params.put("limit", "1");

            Call<SearchResponse> call = yelpAPI.search("new york", params);

            try {
                SearchResponse response = call.execute().body();

                ArrayList<Business> businesses = response.businesses();
                int businessSize = businesses.size();

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

                for (int i = 0; i < businessSize; i++) {
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
        }

    }
    }



