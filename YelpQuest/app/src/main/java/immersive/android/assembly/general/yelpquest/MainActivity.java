package immersive.android.assembly.general.yelpquest;

import android.animation.ObjectAnimator;
import android.animation.TypeEvaluator;
import android.app.SearchManager;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Point;
import android.location.Location;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.Property;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.maps.android.SphericalUtil;
import com.squareup.picasso.Picasso;
import com.yelp.clientlib.connection.YelpAPI;
import com.yelp.clientlib.connection.YelpAPIFactory;
import com.yelp.clientlib.entities.Business;
import com.yelp.clientlib.entities.Coordinate;
import com.yelp.clientlib.entities.SearchResponse;
import com.yelp.clientlib.entities.options.CoordinateOptions;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Set;


import immersive.android.assembly.general.yelpquest.NYTimes.NYtimesResult;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Call;




public class MainActivity extends AppCompatActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        GoogleMap.OnMarkerClickListener, GoogleMap.OnMapClickListener, View.OnClickListener {

    private static final String TAG = "MainActivity";

    private SharedPreferences prefs;
    private static final String SHARED_PREFS_NAME = "yelp_quest_shared_preferences";
    private static final String PREFS_CURRENTLY_ON_QUEST = "yelp_quest_shared_prefs_currently_on_quest_boolean";
    private static final int PERMISSIONS_REQUEST_CODE = 66;

    private SearchView searchView;
    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private double currentLatitude, currentLongitude;
    private Toolbar topToolbar, botToolbar;
    private ImageView questButton;
    private BottomSheetBehavior bottomSheetBehavior;
    private View bottomSheet;
    private RelativeLayout bottomSheetRelativeLayout;
    private TextView bottomSheetBusinessSummary;
    private Button twitterShareButton;
    private Button completeQuestButton;


    private HashMap<String, DetailObject> allQueryDetails;
    private HashMap<String, DetailObject> questDetails;
    private HashMap<String, MarkerObject> markerDetails;
    private HashMap<String, Marker> markers;
    private ArrayList<CompletedQuestMarkerObjects> completedMarkerObjects;
    private QuestObject currentQuest;
    private String queryString;
    private String currentSelectedMarkerTag;
    private int totalQuestHours;


    private TextView undiscoveredButton, filterFoodButton, filterCoffeeButton, filterBarsButton;
    private int bottomToolBarSelection;
    private boolean showUndiscoveredLocationsOnly;
    private boolean markerIsSelected;


    String consumerkey, consumerSecret, token, tokenSecret;
    YelpAPI yelpAPI;
    HashMap<String, String> arrayName = new HashMap<>();
    HashMap<String, Double> arrayLatitute = new HashMap<>();
    HashMap<String, Double> arrayLongtitute = new HashMap<>();
    HashMap<String, String> arraySNtext = new HashMap<>();
    HashMap<String, String> arraySNimageURL = new HashMap<>();
    HashMap<String, String> arrayRating_S_URL = new HashMap<>();
    HashMap<String, String> arrayRating_M_URL = new HashMap<>();
    HashMap<String, String> arrayAddress = new HashMap<>();
//    ArrayList<String> arrayName = new ArrayList<String>();
//    ArrayList<String> arrayLatitute = new ArrayList<String>();
//    ArrayList<String> arrayLongtitute = new ArrayList<>();
//    ArrayList<String> arraySNtext = new ArrayList<>();
//    ArrayList<String> arraySNimageURL = new ArrayList<>();
//    ArrayList<String> arrayRating_S_URL = new ArrayList<>();
//    ArrayList<String> arrayRating_M_URL = new ArrayList<>();
//    ArrayList<String> arrayAddress = new ArrayList<>();
    String nyAPIkey = "48c66fa2c448eda40826487d4f19a018:0:71658152";
    String baseURL = "http://api.nytimes.com/svc/search/v2/articlesearch.json?q=";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        prefs = getSharedPreferences(SHARED_PREFS_NAME, MODE_PRIVATE);

        topToolbar = (Toolbar) findViewById(R.id.topToolbar);
        botToolbar = (Toolbar) findViewById(R.id.botToolbar);
        questButton = (ImageView) findViewById(R.id.questButton);
        twitterShareButton = (Button) findViewById(R.id.tweet_button);
        completeQuestButton = (Button) findViewById(R.id.completeQuestButton);

        allQueryDetails = new HashMap<>();
        questDetails = new HashMap<>();
        markerDetails = new HashMap<>();
        markers = new HashMap<>();
        completedMarkerObjects = new ArrayList<>();
        currentQuest = null;
        queryString = null;
        currentSelectedMarkerTag = null;
        totalQuestHours = 0;

        bottomSheet = findViewById(R.id.bottom_sheet);
        undiscoveredButton = (TextView) findViewById(R.id.undiscoveredButton);
        filterFoodButton = (TextView) findViewById(R.id.filterFoodButton);
        filterCoffeeButton = (TextView) findViewById(R.id.filterCoffeeButton);
        filterBarsButton = (TextView) findViewById(R.id.filterBarsButton);
        undiscoveredButton.setOnClickListener(this);
        filterFoodButton.setOnClickListener(this);
        filterCoffeeButton.setOnClickListener(this);
        filterBarsButton.setOnClickListener(this);


        bottomToolBarSelection = 0;
        showUndiscoveredLocationsOnly = false;
        markerIsSelected = false;

        setSupportActionBar(topToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);


        consumerkey = "c2LzoUcz0rLTPzChnw-94g";
        consumerSecret = "F4FBkmDJ_j0SQ7Zniw6CYVeg1Mk";
        token = "e1LlbERYgyXGV1lZgXAeOwWulAwKodh8";
        tokenSecret = "0hyn5LJB6e1KhRGGYDkJr_HKaE0";

        YelpAPIFactory apiFactory = new YelpAPIFactory(consumerkey, consumerSecret, token, tokenSecret);
        yelpAPI = apiFactory.createAPI();

        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_COLLAPSED) { // || newState == BottomSheetBehavior.STATE_HIDDEN){
                    bottomSheetBehavior.setPeekHeight(0);
//                    if (markerIsSelected) {
//                        onMapClick(null);
//                    }
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
            }
        });

        bottomSheetBusinessSummary = (TextView) findViewById(R.id.textview_business_summary);
        bottomSheetRelativeLayout = (RelativeLayout) findViewById(R.id.modalViewLayout);
        bottomSheetRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetBehavior.setPeekHeight(bottomSheetRelativeLayout.getHeight());
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            }
        });
        bottomSheetBusinessSummary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            }
        });

        completeQuestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CompletedQuestMarkerObjects completedObject = new CompletedQuestMarkerObjects(
                        arrayLatitute.get(currentSelectedMarkerTag), arrayLongtitute.get(currentSelectedMarkerTag),
                        arrayName.get(currentSelectedMarkerTag), arrayAddress.get(currentSelectedMarkerTag));
                markerDetails.get(currentSelectedMarkerTag).setMarkerStatus(5);
                Toast.makeText(MainActivity.this, "Congratulations, you completed a quest!", Toast.LENGTH_SHORT).show();
                completeQuestButton.setVisibility(View.GONE);
                twitterShareButton.setVisibility(View.VISIBLE);
                new CompletedQuestAsyncTask().execute(completedObject);
            }
        });

        twitterShareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, TwitterLogIn.class);
                intent.putExtra("restaurant_name", arrayName.get(currentSelectedMarkerTag));
                startActivity(intent);
            }
        });



        questButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!prefs.getBoolean(PREFS_CURRENTLY_ON_QUEST, false)) {
                    Animation clickAnim = AnimationUtils.loadAnimation(MainActivity.this, R.anim.scale_bounce);
                    questButton.startAnimation(clickAnim);

                    final View dialogView = LayoutInflater.from(MainActivity.this).inflate(R.layout.popup_fab, null);
                    final EditText searchInput = (EditText) dialogView.findViewById(R.id.fabPopupInterestEditText);
                    final CardView beginQuest = (CardView) dialogView.findViewById(R.id.fabPopupBeginQuestButton);
                    final Spinner startTime = (Spinner) dialogView.findViewById(R.id.fabPopupStartTime);
                    final Spinner endTime = (Spinner) dialogView.findViewById(R.id.fabPopupEndTime);
                    final Spinner includeFood = (Spinner) dialogView.findViewById(R.id.fabPopupIncludeFoodYesNo);
                    final Spinner includeFoodType = (Spinner) dialogView.findViewById(R.id.fabPopupIncludeFoodType);

                    ArrayAdapter<CharSequence> startTimeArray =
                            ArrayAdapter.createFromResource(MainActivity.this, R.array.time, android.R.layout.simple_spinner_dropdown_item);
                    ArrayAdapter<CharSequence> endTimeArray =
                            ArrayAdapter.createFromResource(MainActivity.this, R.array.time, android.R.layout.simple_spinner_dropdown_item);
                    ArrayAdapter<CharSequence> includeFoodArray =
                            ArrayAdapter.createFromResource(MainActivity.this, R.array.include_food, android.R.layout.simple_spinner_dropdown_item);
                    ArrayAdapter<CharSequence> includeFoodTypeArray =
                            ArrayAdapter.createFromResource(MainActivity.this, R.array.food_types, android.R.layout.simple_spinner_dropdown_item);
                    startTime.setAdapter(startTimeArray);
                    endTime.setAdapter(endTimeArray);
                    includeFood.setAdapter(includeFoodArray);
                    includeFoodType.setAdapter(includeFoodTypeArray);
                    includeFood.setEnabled(false);
                    includeFoodType.setEnabled(false);

                    int currentHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
                    startTime.setSelection(currentHour);
                    if (currentHour == 23) {
                        endTime.setSelection(0);
                    } else {
                        endTime.setSelection(Calendar.getInstance().get(Calendar.HOUR_OF_DAY) + 1);
                    }

                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setView(dialogView);
                    final AlertDialog dialog = builder.create();

                    beginQuest.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Boolean includeFoodWithQuest = false;
                            if (includeFood.getSelectedItem().toString().equals("YES")){
                                includeFoodWithQuest = true;
                            }

                            if (searchInput.getText().toString().trim().length() <= 0 && !includeFoodWithQuest) {
                                searchInput.setError("This field cannot be blank.");
                            } else {
                                createNewQuest(searchInput.getText().toString(),
                                        startTime.getSelectedItem().toString(), endTime.getSelectedItem().toString(),
                                        includeFoodWithQuest, includeFoodType.getSelectedItem().toString());

                                JobInfo jobInfo = new JobInfo.Builder(1, new ComponentName(getPackageName(),
                                        Notifications.class.getName()))
                                        .setOverrideDeadline(10000)
                                        .setPersisted(false)
                                        .build();
                                Log.i(TAG, "onClick: 10 second count ");
                                JobScheduler jobScheduler = (JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE);
                                jobScheduler.schedule(jobInfo);

                                dialog.dismiss();
                            }
                        }
                    });

                    dialog.show();
                } else {
                    Animation clickAnim = AnimationUtils.loadAnimation(MainActivity.this, R.anim.scale_bounce);
                    questButton.startAnimation(clickAnim);

                    final View dialogView = LayoutInflater.from(MainActivity.this).inflate(R.layout.popup_cancel_quest, null);
                    final TextView cancelButton = (TextView) dialogView.findViewById(R.id.cancelQuestPopupCancelButton);
                    final TextView yesButton = (TextView) dialogView.findViewById(R.id.cancelQuestPopupYesButton);

                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setView(dialogView);
                    final AlertDialog dialog = builder.create();

                    cancelButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.dismiss();
                        }
                    });

                    yesButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            cancelQuest();
                            dialog.dismiss();
                        }
                    });

                    dialog.show();
                }



            }
        });



        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

        handleIntent(getIntent());

    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setMapToolbarEnabled(false);
        mMap.getUiSettings().setZoomControlsEnabled(false);
        mMap.setMinZoomPreference(13.0f);
        mMap.setMaxZoomPreference(20.0f);

        mMap.setOnMarkerClickListener(this);
        mMap.setOnMapClickListener(this);

    }





    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflator = getMenuInflater();
        inflator.inflate(R.menu.main_menu_options, menu);

        SearchManager manager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.searchOption).getActionView();
        ComponentName name = new ComponentName(this, MainActivity.class);
        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.setSearchableInfo(manager.getSearchableInfo(name));

        return true;
    }


    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        if(Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = getIntent().getStringExtra(SearchManager.QUERY);

            bottomToolBarSelection = 0;
            filterFoodButton.setBackgroundResource(R.drawable.white_border_transparent);
            filterFoodButton.setTextColor(Color.parseColor("#FFFFFF"));
            filterCoffeeButton.setBackgroundResource(R.drawable.white_border_transparent);
            filterCoffeeButton.setTextColor(Color.parseColor("#FFFFFF"));
            filterBarsButton.setBackgroundResource(R.drawable.white_border_transparent);
            filterBarsButton.setTextColor(Color.parseColor("#FFFFFF"));

            queryString = query;
            SearchTerm.getInstance().setSearchTerm(queryString);
            new CompletedQuestMarkersAsyncTask().execute();
//            queryMapLocations(query);
        }
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.undiscoveredButton:
                if (showUndiscoveredLocationsOnly) {
                    undiscoveredButton.setBackgroundResource(R.drawable.circle_white_border_grey);
                    undiscoveredButton.setTextColor(Color.parseColor("#FFFFFF"));
                    showUndiscoveredLocationsOnly = false;
                } else {
                    undiscoveredButton.setBackgroundResource(R.drawable.circle_yellow_border_black);
                    undiscoveredButton.setTextColor(Color.parseColor("#FFEB3B"));
                    showUndiscoveredLocationsOnly = true;
                }
                updateScreenMarkers();
                break;
            case R.id.filterFoodButton:
                if (bottomToolBarSelection != 1) {
                    searchView.setQuery("", false);
                    searchView.clearFocus();

                    filterFoodButton.setBackgroundResource(R.drawable.yellow_border_black);
                    filterFoodButton.setTextColor(Color.parseColor("#FFEB3B"));
                    filterCoffeeButton.setBackgroundResource(R.drawable.white_border_transparent);
                    filterCoffeeButton.setTextColor(Color.parseColor("#FFFFFF"));
                    filterBarsButton.setBackgroundResource(R.drawable.white_border_transparent);
                    filterBarsButton.setTextColor(Color.parseColor("#FFFFFF"));
                    bottomToolBarSelection = 1;

                    queryString = "food";
                    SearchTerm.getInstance().setSearchTerm(queryString);
                    new CompletedQuestMarkersAsyncTask().execute();
//                    queryMapLocations("food");
                }
                break;
            case R.id.filterCoffeeButton:
                if (bottomToolBarSelection != 2) {
                    searchView.setQuery("", false);
                    searchView.clearFocus();

                    filterCoffeeButton.setBackgroundResource(R.drawable.yellow_border_black);
                    filterCoffeeButton.setTextColor(Color.parseColor("#FFEB3B"));
                    filterFoodButton.setBackgroundResource(R.drawable.white_border_transparent);
                    filterFoodButton.setTextColor(Color.parseColor("#FFFFFF"));
                    filterBarsButton.setBackgroundResource(R.drawable.white_border_transparent);
                    filterBarsButton.setTextColor(Color.parseColor("#FFFFFF"));
                    bottomToolBarSelection = 2;

                    queryString = "coffee";
                    SearchTerm.getInstance().setSearchTerm(queryString);
                    new CompletedQuestMarkersAsyncTask().execute();
//                    queryMapLocations("coffee");
                }
                break;
            case R.id.filterBarsButton:
                if (bottomToolBarSelection != 3) {
                    searchView.setQuery("", false);
                    searchView.clearFocus();

                    filterBarsButton.setBackgroundResource(R.drawable.yellow_border_black);
                    filterBarsButton.setTextColor(Color.parseColor("#FFEB3B"));
                    filterFoodButton.setBackgroundResource(R.drawable.white_border_transparent);
                    filterFoodButton.setTextColor(Color.parseColor("#FFFFFF"));
                    filterCoffeeButton.setBackgroundResource(R.drawable.white_border_transparent);
                    filterCoffeeButton.setTextColor(Color.parseColor("#FFFFFF"));
                    bottomToolBarSelection = 3;

                    queryString = "bars";
                    SearchTerm.getInstance().setSearchTerm(queryString);
                    new CompletedQuestMarkersAsyncTask().execute();
//                    queryMapLocations("bars");
                }
                break;
            default: break;
        }

    }






    private class MyBounceInterpolator extends android.view.animation.BounceInterpolator {
        double mAmplitude = 1;
        double mFrequency = 10;

        public MyBounceInterpolator(double amplitude, double frequency) {
            mAmplitude = amplitude;
            mFrequency = frequency;
        }

        public float getInterpolation(float time) {
//            double amplitude = mAmplitude;
//            if (amplitude == 0) { amplitude = 0.05; }
            return (float) (-1 * Math.pow(Math.E, -time/ mAmplitude) * Math.cos(mFrequency * time) + 1);
        }
    }

    private void dropMarker(final Marker marker, GoogleMap map) {
        final LatLng finalPosition = new LatLng(marker.getPosition().latitude, marker.getPosition().longitude);

        Projection projection = map.getProjection();
        Point startPoint = projection.toScreenLocation(finalPosition);
        startPoint.y = 0;
        final LatLng startLatLng = projection.fromScreenLocation(startPoint);
        final Interpolator interpolator = new MyBounceInterpolator(0.1, 10); //(0.11, 4.6);

        TypeEvaluator<LatLng> typeEvaluator = new TypeEvaluator<LatLng>() {
            @Override
            public LatLng evaluate(float fraction, LatLng startValue, LatLng endValue) {
                float t = interpolator.getInterpolation(fraction);
                double lng = t * finalPosition.longitude + (1 - t) * startLatLng.longitude;
                double lat = t * finalPosition.latitude + (1 - t) * startLatLng.latitude;
                return new LatLng(lat, lng);
            }
        };

        Property<Marker, LatLng> property = Property.of(Marker.class, LatLng.class, "position");
        ObjectAnimator animator = ObjectAnimator.ofObject(marker, property, typeEvaluator, finalPosition);
        animator.setDuration(1200);
        animator.start();
    }






    @Override
    public void onMapClick(LatLng latLng) {

        if (markerIsSelected) {

            markerIsSelected = false;

            markers.get(currentSelectedMarkerTag);
            setMarkerIcon(markers.get(currentSelectedMarkerTag), markerDetails.get(currentSelectedMarkerTag).getMarkerStatus());
            questButton.setVisibility(View.VISIBLE);

            bottomSheetBehavior.setPeekHeight(0);
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);

            completeQuestButton.setVisibility(View.INVISIBLE);
            twitterShareButton.setVisibility(View.GONE);

            Log.i(TAG, "On QUEST: " + prefs.getBoolean(PREFS_CURRENTLY_ON_QUEST, false));
            if (!prefs.getBoolean(PREFS_CURRENTLY_ON_QUEST, false)) {
                topToolbar.setVisibility(View.VISIBLE);
                botToolbar.setVisibility(View.VISIBLE);
            } else {
                topToolbar.setVisibility(View.VISIBLE);
                topToolbar.setVisibility(View.GONE);
            }
        }

    }

    @Override
    public boolean onMarkerClick(Marker marker) {

        markerIsSelected = true;

        if (currentSelectedMarkerTag == null) {
            currentSelectedMarkerTag = (String)marker.getTag();
        } else if (!currentSelectedMarkerTag.equals((String)marker.getTag())) {
            setMarkerIcon(markers.get(currentSelectedMarkerTag), markerDetails.get(currentSelectedMarkerTag).getMarkerStatus());
            currentSelectedMarkerTag = (String)marker.getTag();
        }

        marker.setIcon((BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
        questButton.setVisibility(View.INVISIBLE);
        topToolbar.setVisibility(View.GONE);
        botToolbar.setVisibility(View.GONE);

        Log.i(TAG, "On QUEST: " + prefs.getBoolean(PREFS_CURRENTLY_ON_QUEST, false));
        MarkerObject selectedMarker = markerDetails.get(currentSelectedMarkerTag);
        if (prefs.getBoolean(PREFS_CURRENTLY_ON_QUEST, false)
                && selectedMarker.getMarkerStatus() != 2 && selectedMarker.getMarkerStatus() != 5) {
//                && isUserCloseToQuestNode(selectedMarker)) {

            twitterShareButton.setVisibility(View.GONE);
            completeQuestButton.setVisibility(View.VISIBLE);

        } else if (prefs.getBoolean(PREFS_CURRENTLY_ON_QUEST, false)) {

            twitterShareButton.setVisibility(View.VISIBLE);
            completeQuestButton.setVisibility(View.GONE);

        }



        TextView textViewAddress = (TextView) findViewById(R.id.textview_business_address);
        textViewAddress.setText(arrayAddress.get(currentSelectedMarkerTag));
        //returns address to bottomsheet
        TextView textViewName = (TextView) findViewById(R.id.textview_business_name);
        textViewName.setText(arrayName.get(currentSelectedMarkerTag));
        //returns business name to bottomsheet
        TextView textViewSnippetReview = (TextView) findViewById(R.id.textview_business_summary);
        textViewSnippetReview.setText(arraySNtext.get(currentSelectedMarkerTag));
        //returns business summary to bottomsheet
//        ImageView ratingsImageSmall = (ImageView) findViewById(R.id.yelp_rating_imageSmall);
//        Picasso.with(getApplicationContext()).load(arrayRating_S_URL.get(currentSelectedMarkerTag)).into(ratingsImageSmall);
        //returns image to bottomsheet
        ImageView ratingsImage = (ImageView) findViewById(R.id.yelp_rating_image);
        Picasso.with(getApplicationContext()).load(arrayRating_M_URL.get(currentSelectedMarkerTag)).into(ratingsImage);
        //returns business summary to bottomsheet
        ImageView businessImage = (ImageView) findViewById(R.id.imageview_business);
        Picasso.with(getApplicationContext()).load(arraySNimageURL.get(currentSelectedMarkerTag)).into(businessImage);
        //returns business summary to bottomsheet
        TextView distance = (TextView) findViewById(R.id.textview_distance);
        LatLng fromLocation = new LatLng(currentLatitude, currentLongitude);
        LatLng toLocation = new LatLng(markerDetails.get(currentSelectedMarkerTag).getLatitude(),
                markerDetails.get(currentSelectedMarkerTag).getLongitude());
        double distanceMeters = Math.abs(SphericalUtil.computeDistanceBetween(fromLocation, toLocation));
        double distanceMiles = distanceMeters / 1609.344;
        DecimalFormat roundDec = new DecimalFormat("#.#");
        String miles = String.format("%.1f", Double.valueOf(roundDec.format(distanceMiles)));
        distance.setText(miles + " mi");

        bottomSheetBehavior.setPeekHeight(bottomSheetRelativeLayout.getHeight());
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);

        new NYTimesAsyncTask().execute();


        // Return false to indicate that we have not consumed the event and that we wish
        // for the default behavior to occur (which is for the camera to move such that the
        // marker is centered and for the marker's info window to open, if it has one).
        return false;
    }

    private boolean isUserCloseToQuestNode(MarkerObject markerObject) {
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            Toast.makeText(MainActivity.this, "Requires Permissions", Toast.LENGTH_SHORT).show();
        }
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        currentLatitude = mLastLocation.getLatitude();
        currentLongitude = mLastLocation.getLongitude();

        LatLng fromLocation = new LatLng(currentLatitude, currentLongitude);
        LatLng toLocation = new LatLng(markerObject.getLatitude(), markerObject.getLongitude());

        return Math.abs(SphericalUtil.computeDistanceBetween(fromLocation, toLocation)) <= 2.4384; // Returns in meters
    }












    private class NYTimesAsyncTask extends AsyncTask<Void, Void, ArrayList<String>> {

        @Override
        protected ArrayList<String> doInBackground(Void... voids) {
            String businessName = arrayName.get(currentSelectedMarkerTag);
            businessName = businessName.replaceAll(" ", "%20");
            String finalAPIstring = baseURL + businessName + "&sort=newest&&api-key=" + nyAPIkey;
            Log.d("url : ", finalAPIstring);
            return returnAPIcall(finalAPIstring);
        }

        @Override
        protected void onPostExecute(ArrayList<String> data) {
//            if (data == null || data.isEmpty()) {
//                TextView title = (TextView) findViewById(R.id.newyorktimes_title);
//                TextView body = (TextView) findViewById(R.id.newyorktimes_article);
//                TextView date = (TextView) findViewById(R.id.newyorktimes_date);
//
//                date.setText(Integer.toString(data.size()));
//                title.setText(data.get(0));
//                body.setText(data.get(1));
//            }
        }
    }

    private class InitialQuestObjectAsyncTask extends AsyncTask<Void, Void, QuestObject> {

        @Override
        protected QuestObject doInBackground(Void... voids) {
            return DatabaseHelper.getInstance(MainActivity.this).getCurrentQuestObject();
        }

        @Override
        protected void onPostExecute(QuestObject questObject) {
            currentQuest = questObject;
            new InitialMarkerObjectsAsyncTask().execute();
        }
    }

    private class InitialMarkerObjectsAsyncTask extends AsyncTask<Void, Void, HashMap<String, MarkerObject>> {

        @Override
        protected HashMap<String, MarkerObject> doInBackground(Void... voids) {
            return DatabaseHelper.getInstance(MainActivity.this).getAllCurrentMarkers();
        }

        @Override
        protected void onPostExecute(HashMap<String, MarkerObject> stringMarkerObjectHashMap) {
            markerDetails = stringMarkerObjectHashMap;
            new InitialDetailObjectAsyncTask().execute();
        }
    }

    private class InitialDetailObjectAsyncTask extends AsyncTask<Void, Void, HashMap<String, DetailObject>> {

        @Override
        protected HashMap<String, DetailObject> doInBackground(Void... voids) {
            ArrayList<DetailObject> details = DatabaseHelper.getInstance(MainActivity.this).getAllDetailObjects();
            HashMap<String, DetailObject> mapDetailObjects = new HashMap<>();
            if (!details.isEmpty()) {
                for (DetailObject detail : details) {
                    mapDetailObjects.put(detail.getmMarkerTag(), detail);
                }
            }
            return mapDetailObjects;
        }

        @Override
        protected void onPostExecute(HashMap<String, DetailObject> stringDetailObjectHashMap) {
            questDetails = stringDetailObjectHashMap;
            initializeArrayHashMaps();
//            updateScreenMarkers();
        }
    }


    private class CompletedQuestMarkersAsyncTask extends AsyncTask<Void, Void, ArrayList<CompletedQuestMarkerObjects>> {

        @Override
        protected void onPreExecute() {completedMarkerObjects.clear();}
        @Override
        protected ArrayList<CompletedQuestMarkerObjects> doInBackground(Void... strings) {
            return DatabaseHelper.getInstance(MainActivity.this).getAllCompletedQuestMarkerObjects();}
        @Override
        protected void onPostExecute(ArrayList<CompletedQuestMarkerObjects> completedObjects) {
            completedMarkerObjects = completedObjects;
//            new QueryLocationsAsyncTask().execute(queryString);
            new YelpAPIAsyncTask().execute("general");
        }
    }

    private class StartQuestCheckAsyncTask extends AsyncTask<Void, Void, ArrayList<CompletedQuestMarkerObjects>> {

        @Override
        protected void onPreExecute() {completedMarkerObjects.clear();}
        @Override
        protected ArrayList<CompletedQuestMarkerObjects> doInBackground(Void... strings) {
            return DatabaseHelper.getInstance(MainActivity.this).getAllCompletedQuestMarkerObjects();}
        @Override
        protected void onPostExecute(ArrayList<CompletedQuestMarkerObjects> completedObjects) {
            completedMarkerObjects = completedObjects;
//            new QueryLocationsAsyncTask().execute(queryString);
            new YelpAPIAsyncTask().execute("quest");
        }
    }

    private class CancelQuestAsyncTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            DatabaseHelper.getInstance(MainActivity.this).cancelQuest();
            return null;
        }
    }

    private class CompletedQuestAsyncTask extends AsyncTask<CompletedQuestMarkerObjects, Void, Void> {

        String tag;

        @Override
        protected void onPreExecute() {
            this.tag = currentSelectedMarkerTag;
        }

        @Override
        protected Void doInBackground(CompletedQuestMarkerObjects... completedQuestMarkerObject) {
            DatabaseHelper.getInstance(MainActivity.this).addCompletedQuestMarkerObject(completedQuestMarkerObject[0]);
            DatabaseHelper.getInstance(MainActivity.this).updateMarkerStatus(tag);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {

            int count = 0;
            Set<String> questKeys = questDetails.keySet();
            for (String questkey : questKeys) {
                for (int i = 0; i < markerDetails.size(); i++) {
                    if (markerDetails.get(questkey).getMarkerStatus() == 2 || markerDetails.get(questkey).getMarkerStatus() == 5) {
                        count++;
                        break;
                    }
                }
            }
            if (count == questDetails.size()) {
                final View dialogView = LayoutInflater.from(MainActivity.this).inflate(R.layout.popup_all_quests_completed, null);
                final TextView doneButton = (TextView) dialogView.findViewById(R.id.allQuestCompletedPopupDoneButton);

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setView(dialogView);
                final AlertDialog dialog = builder.create();

                doneButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

                dialog.show();
            }
        }
    }

    private void setMarkerIcon(Marker marker, int status) {
        // 1 = Undiscovered(red) ; 2 = Discovered(yellow) ; 3 = Selected(green)
        // 4 = Quest New(cyan) ; 5 = Quest Completed(yellow) ; 6 = Quest Selected(green)
        // 7 = Quest Completion Available(violet)
        switch(status) {
            case 1: marker.setIcon((BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))); break;
            case 2: marker.setIcon((BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW))); break;
            case 3: marker.setIcon((BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))); break;
            case 4: marker.setIcon((BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN))); break;
            case 5: marker.setIcon((BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW))); break;
            case 6: marker.setIcon((BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))); break;
            case 7: marker.setIcon((BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET))); break;
            default: break;
        }
    }

    private void queryMapLocations(String string) {

        ArrayList<DetailObject> apiQuery = YelpAPIHelper.getInstance().getmInfoArray();

        allQueryDetails.clear();
        markerDetails.clear();
        if (!apiQuery.isEmpty()) {
            int tagCount = 1;
            for (DetailObject detail : apiQuery) {
                String newTag = Integer.toString(tagCount);
                int markerStatus = 1;
                if (!completedMarkerObjects.isEmpty()) {
                    for (CompletedQuestMarkerObjects completed : completedMarkerObjects) {
                        if (completed.getBusinessName().equals(detail.getmName())
                                && completed.getBusinessAddress().equals(detail.getmAddress())) {
                            markerStatus = 2;
                            break;
                        }
                    }
                }

                allQueryDetails.put(newTag, detail);

                MarkerObject newMarkerObject = new MarkerObject(newTag,
                        detail.getmLatitude(),
                        detail.getmLongtitude(),
                        detail.getmName(),
                        detail.getmAddress(),
                        false,
                        markerStatus,
                        null);

                markerDetails.put(newTag, newMarkerObject);

                detail.setmMarkerTag(newTag);
                arrayName.put(newTag, detail.getmName());
                arrayLatitute.put(newTag, detail.getmLatitude());
                arrayLongtitute.put(newTag, detail.getmLongtitude());
                arraySNtext.put(newTag, detail.getmSNtext());
                arraySNimageURL.put(newTag, detail.getmSNurl());
                arrayRating_S_URL.put(newTag, detail.getmRatingSurl());
                arrayRating_M_URL.put(newTag, detail.getmRatingMurl());
                arrayAddress.put(newTag, detail.getmAddress());

                tagCount++;
            }

            if (string.equals("general")) {
                updateScreenMarkers();
            } else if (string.equals("quest")) {
                questDetails.clear();
                HashMap<String, DetailObject> tempQuestHolder = new HashMap<>();
                ArrayList<String> newKeys = new ArrayList<>();

                if (!allQueryDetails.isEmpty()) {
                    Set<String> keys = allQueryDetails.keySet();
                    for (String key : keys) {
                        boolean isCompleted = false;
                        DetailObject tempDetail = allQueryDetails.get(key);
                        for (CompletedQuestMarkerObjects completed : completedMarkerObjects) {
                            if (completed.getBusinessName().equals(tempDetail.getmName())
                                    && completed.getLatitude() == tempDetail.getmLatitude()
                                    && completed.getLongitude() == tempDetail.getmLongtitude()) {
                                isCompleted = true;
                                break;
                            }
                        }
                        if (!isCompleted) {
                            newKeys.add(key);
                            tempQuestHolder.put(key, tempDetail);
                        }
                    }
                }

                Log.i(TAG, "COMPLETED MARKERS: " + completedMarkerObjects.size());
                Log.i(TAG, "ALL QUERY DETAILS: " + allQueryDetails.size());
                Log.i(TAG, "TEMP HOLDER: " + tempQuestHolder.size());
                if (tempQuestHolder.size() <= totalQuestHours) {
                    questDetails.putAll(tempQuestHolder);
                } else {
                    while (newKeys.size() > totalQuestHours) {
                        Random rand = new Random();
                        newKeys.remove(rand.nextInt(newKeys.size()));
                    }

                    for (String key : newKeys) {
                        questDetails.put(key, tempQuestHolder.get(key));
                    }

                }

                updateQuestMarkers();
            }


        } else {
            Toast.makeText(MainActivity.this, "Search returned empty results", Toast.LENGTH_SHORT).show();
        }
    }

    private void initializeArrayHashMaps() {

        Set<String> keys = questDetails.keySet();

        for (String key : keys) {
            arrayName.put(key, questDetails.get(key).getmName());
            arrayLatitute.put(key, questDetails.get(key).getmLatitude());
            arrayLongtitute.put(key, questDetails.get(key).getmLongtitude());
            arraySNtext.put(key, questDetails.get(key).getmSNtext());
            arraySNimageURL.put(key, questDetails.get(key).getmSNurl());
            arrayRating_S_URL.put(key, questDetails.get(key).getmRatingSurl());
            arrayRating_M_URL.put(key, questDetails.get(key).getmRatingMurl());
            arrayAddress.put(key, questDetails.get(key).getmAddress());
        }

        updateScreenMarkers();
    }

    private void updateScreenMarkers() {

        mMap.clear();
        markers.clear();
        currentSelectedMarkerTag = null;
        if (!markerDetails.isEmpty()) {
            Set<String> keys = markerDetails.keySet();

            double markerLatitude = 0.0;
            double markerLongitude = 0.0;
            for (String key : keys) {
                if (showUndiscoveredLocationsOnly) {
                    if (markerDetails.get(key).getMarkerStatus() == 1) {
                        markerLatitude = markerDetails.get(key).getLatitude();
                        markerLongitude = markerDetails.get(key).getLongitude();
                        LatLng markerLocation = new LatLng(markerLatitude, markerLongitude);
                        Marker newMarker = mMap.addMarker(new MarkerOptions().position(markerLocation));
                        newMarker.setTag(key);
                        setMarkerIcon(newMarker, markerDetails.get(key).getMarkerStatus());

                        markers.put(key, newMarker);
                        dropMarker(newMarker, mMap);
                        Log.i(TAG, "UPDATE SCREEN MARKERS: " + key);
                    }
                } else {
                    markerLatitude = markerDetails.get(key).getLatitude();
                    markerLongitude = markerDetails.get(key).getLongitude();
                    LatLng markerLocation = new LatLng(markerLatitude, markerLongitude);
                    Marker newMarker = mMap.addMarker(new MarkerOptions().position(markerLocation));
                    newMarker.setTag(key);
                    setMarkerIcon(newMarker, markerDetails.get(key).getMarkerStatus());

                    markers.put(key, newMarker);
                    dropMarker(newMarker, mMap);
                    Log.i(TAG, "UPDATE SCREEN MARKERS: " + key);
                }
            }

//            LatLng lastMarkerPosition = new LatLng(markerLatitude, markerLongitude);
            LatLng lastMarkerPosition = new LatLng(currentLatitude, currentLongitude);
            float zoomLevel = 14f;
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lastMarkerPosition, zoomLevel));
        }
    }

    private void updateQuestMarkers() {

//        if (questDetails.isEmpty() && allQueryDetails.isEmpty()) { // query returned empty results

        if (allQueryDetails.isEmpty() || (questDetails.size() < totalQuestHours && allQueryDetails.size() > questDetails.size())) { // query returned results less than needed for quest
            final View dialogView = LayoutInflater.from(MainActivity.this).inflate(R.layout.popup_query_no_results, null);
            final TextView secondaryText = (TextView) dialogView.findViewById(R.id.noResultsPopupText2);
            final TextView cancelButton = (TextView) dialogView.findViewById(R.id.noResultsPopupCancelButton);
            final TextView yesButton = (TextView) dialogView.findViewById(R.id.noResultsPopupYesButton);

            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setView(dialogView);
            final AlertDialog dialog = builder.create();

            if (allQueryDetails.isEmpty()) {
                secondaryText.setVisibility(View.INVISIBLE);
                yesButton.setVisibility(View.GONE);
            } else if (allQueryDetails.size() > questDetails.size()) {
                yesButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (allQueryDetails.size() <= totalQuestHours) {
                            questDetails.clear();
                            questDetails.putAll(allQueryDetails);
                        } else {
                            if (questDetails.size() > 0) {
                                Set<String> questKeys = questDetails.keySet();

                                for (String key : questKeys) {
                                    allQueryDetails.remove(key);
                                }

                                Set<String> remainingKeys = allQueryDetails.keySet();
                                ArrayList<String> keyHolder = new ArrayList<>();
                                keyHolder.addAll(remainingKeys);
                                while (questDetails.size() < totalQuestHours) {
                                    String key = keyHolder.get(0);
                                    questDetails.put(key, allQueryDetails.get(key));
                                    keyHolder.remove(0);
                                }

                            } else {
                                Set<String> remainingKeys = allQueryDetails.keySet();
                                ArrayList<String> keyHolder = new ArrayList<>();
                                keyHolder.addAll(remainingKeys);
                                while (questDetails.size() < totalQuestHours) {
                                    String key = keyHolder.get(0);
                                    questDetails.put(key, allQueryDetails.get(key));
                                    keyHolder.remove(0);
                                }
                            }
                        }

                        updateQuestMarkers();
                        dialog.dismiss();
                    }
                });
            }

            cancelButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    currentQuest = null;
                    dialog.dismiss();
                }
            });

            dialog.show();


        } else { // both hashmaps have items

            // TODO start job scheduler and notifications
            // TODO
            // TODO


            prefs.edit().putBoolean(PREFS_CURRENTLY_ON_QUEST, Boolean.TRUE).commit();

            mMap.clear();
            markers.clear();
            currentSelectedMarkerTag = null;

            Set<String> keys = questDetails.keySet();

            for (String key : keys) {
                double markerLatitude = markerDetails.get(key).getLatitude();
                double markerLongitude = markerDetails.get(key).getLongitude();
                LatLng markerLocation = new LatLng(markerLatitude, markerLongitude);
                Marker newMarker = mMap.addMarker(new MarkerOptions().position(markerLocation));
                newMarker.setTag(key);
                setMarkerIcon(newMarker, markerDetails.get(key).getMarkerStatus());

                markers.put(key, newMarker);
                dropMarker(newMarker, mMap);
            }

            questButton.setImageResource(R.drawable.cancel_icon);
            topToolbar.setVisibility(View.GONE);
            botToolbar.setVisibility(View.GONE);

            undiscoveredButton.setBackgroundResource(R.drawable.circle_white_border_grey);
            undiscoveredButton.setTextColor(Color.parseColor("#FFFFFF"));
            filterFoodButton.setBackgroundResource(R.drawable.white_border_transparent);
            filterFoodButton.setTextColor(Color.parseColor("#FFFFFF"));
            filterCoffeeButton.setBackgroundResource(R.drawable.white_border_transparent);
            filterCoffeeButton.setTextColor(Color.parseColor("#FFFFFF"));
            filterBarsButton.setBackgroundResource(R.drawable.white_border_transparent);
            filterBarsButton.setTextColor(Color.parseColor("#FFFFFF"));

            bottomToolBarSelection = 0;
            showUndiscoveredLocationsOnly = false;
            queryString = "";
            currentSelectedMarkerTag = null;


            LatLng lastMarkerPosition = new LatLng(currentLatitude, currentLongitude);
            float zoomLevel = 14f;
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lastMarkerPosition, zoomLevel));

            new SaveNewQuestAsyncTask().execute();

        }


    }

    private void createNewQuest(String query, String startTime, String endTime, boolean includeFood, String foodType) {
        totalQuestHours = 0;
        int starttime = getTimeAsInt(startTime);
        int endtime = getTimeAsInt(endTime);
        if (endtime == starttime) {
            totalQuestHours = 24;
        } else if (endtime > starttime) {
            totalQuestHours = endtime - starttime;
        } else if (endtime < starttime) {
            totalQuestHours = (24 - starttime) + endtime;
        }

        currentQuest = new QuestObject(starttime, endtime, "Quest time is up!", query, includeFood, foodType);

        queryString = query;
        SearchTerm.getInstance().setSearchTerm(query);
        new StartQuestCheckAsyncTask().execute();

//        updateQuestMarkers();
    }

    private int getTimeAsInt(String time) {
        switch(time) {
            default:
            case "12:00am": return 0;
            case "1:00am": return 1;
            case "2:00am": return 2;
            case "3:00am": return 3;
            case "4:00am": return 4;
            case "5:00am": return 5;
            case "6:00am": return 6;
            case "7:00am": return 7;
            case "8:00am": return 8;
            case "9:00am": return 9;
            case "10:00am": return 10;
            case "11:00am": return 11;
            case "12:00pm": return 12;
            case "1:00pm": return 13;
            case "2:00pm": return 14;
            case "3:00pm": return 15;
            case "4:00pm": return 16;
            case "5:00pm": return 17;
            case "6:00pm": return 18;
            case "7:00pm": return 19;
            case "8:00pm": return 20;
            case "9:00pm": return 21;
            case "10:00pm": return 22;
            case "11:00pm": return 23;
        }
    }

    private void cancelQuest() {

        new CancelQuestAsyncTask().execute();

        prefs.edit().putBoolean(PREFS_CURRENTLY_ON_QUEST, Boolean.FALSE).commit();
        questButton.setImageResource(R.drawable.quest_icon);
        topToolbar.setVisibility(View.VISIBLE);
        botToolbar.setVisibility(View.VISIBLE);

        mMap.clear();
        currentSelectedMarkerTag = null;
        allQueryDetails.clear();
        questDetails.clear();
        markerDetails.clear();
        markers.clear();
        completedMarkerObjects.clear();
        currentQuest = null;
        totalQuestHours = 0;
        queryString = null;

    }












    private class SaveNewQuestAsyncTask extends AsyncTask<Void, Void, Void> {

        HashMap<String, MarkerObject> questMarkerObjects;
        HashMap<String, DetailObject> questDetailObjects;
        QuestObject quest;

        @Override
        protected void onPreExecute() {
            this.quest = currentQuest;
            this.questMarkerObjects = markerDetails;
            this.questDetailObjects = questDetails;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            DatabaseHelper.getInstance(MainActivity.this).cancelQuest();
            ArrayList<DetailObject> details = new ArrayList<>();
            HashMap<String, MarkerObject> tempMarkerObjects = new HashMap<>();
            Set<String> keys = questDetailObjects.keySet();
            for (String key : keys) {
                details.add(questDetailObjects.get(key));
                tempMarkerObjects.put(key, questMarkerObjects.get(key));
            }

            DatabaseHelper.getInstance(MainActivity.this).saveQuestObject(quest);
            DatabaseHelper.getInstance(MainActivity.this).saveDetailObjects(details);
            DatabaseHelper.getInstance(MainActivity.this).saveCurrentMarkers(tempMarkerObjects);

            return null;
        }
    }

    private class YelpAPIAsyncTask extends AsyncTask<String, Void, String> {

        ArrayList<CompletedQuestMarkerObjects> completedObjects;
//        QuestObject activeQuest;
        int questHours;

        @Override
        protected void onPreExecute() {
            this.completedObjects = completedMarkerObjects;
//            this.activeQuest = currentQuest;
            this.questHours = totalQuestHours;
        }

        @Override
        protected String doInBackground(String... strings) {

            Map<String, String> params = new HashMap<>();
            SearchTerm searchTerm = SearchTerm.getInstance();
            params.put("term", searchTerm.getmSearch());
            params.put("radius_filter", "1500");

            if (strings[0].equals("general")) {
                params.put("limit", "10");
            } else if (strings[0].equals("quest")) {
                params.put("limit", "20");
            }

            Log.i(TAG, "SEARCH TERM: "+ searchTerm.getmSearch());

            CoordinateOptions userCoordinate = CoordinateOptions.builder()
                    .latitude(currentLatitude)
                    .longitude(currentLongitude).build();

            Log.i(TAG, "LATITUDE: "+ currentLatitude);
            Log.i(TAG, "LONGITUDE: "+ currentLongitude);

            Call<SearchResponse> call = yelpAPI.search(userCoordinate, params); // Beta testing in New York first

            try {
                SearchResponse response = call.execute().body();

                ArrayList<Business> businesses = response.businesses();
                int businessSize = businesses.size();

                YelpAPIHelper newArraySingle = YelpAPIHelper.getInstance();
                newArraySingle.removeAll();

                arrayName.clear();
                arrayLatitute.clear();
                arrayLongtitute.clear();
                arraySNtext.clear();
                arraySNimageURL.clear();
                arrayRating_S_URL.clear();
                arrayRating_M_URL.clear();
                arrayAddress.clear();

                for (int i = 0; i < businessSize; i++) {
                    Log.i(TAG, "BUSINESSE: " + i);

                    String stName = businesses.get(i).name().toString();

                    Coordinate coordinate = response.businesses().get(i).location().coordinate();

                    Double latitude = coordinate.latitude();
                    Double longitude = coordinate.longitude();
                    String stSNtext = businesses.get(i).snippetText();
                    String stSNimageURL = businesses.get(i).imageUrl();
                    String stRatingSurl = businesses.get(i).ratingImgUrlSmall();
                    String stRatingMurl = businesses.get(i).ratingImgUrl();
                    String addressStreet = businesses.get(i).location().displayAddress().get(0);
                    String addressCity = businesses.get(i).location().city();
                    String addressState = businesses.get(i).location().stateCode();
                    String addressZip = businesses.get(i).location().postalCode();
                    String addressFull = addressStreet+" "+ addressCity+" "
                            + addressState+" "+addressZip;

                    DetailObject infoBusiness = new DetailObject();
                    infoBusiness.setmName(stName);
                    infoBusiness.setmLatitude(latitude);
                    infoBusiness.setmLongtitude(longitude);
                    infoBusiness.setmSNtext(stSNtext);
                    infoBusiness.setmSNurl(stSNimageURL);
                    infoBusiness.setmRatingSurl(stRatingSurl);
                    infoBusiness.setmRatingMurl(stRatingMurl);
                    infoBusiness.setmAddress(addressFull);

                    newArraySingle.addInstance(infoBusiness);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
            return strings[0];
        }

        @Override
        protected void onPostExecute(String string) {
            queryMapLocations(string);
        }
    }

    public ArrayList<String> returnAPIcall(String finalURL){
        final ArrayList<String> nyTimesData = new ArrayList<>();

        OkHttpClient client = new OkHttpClient();

        final Request apiRequest = new Request.Builder()
                .url(finalURL)
                .build();

        client.newCall(apiRequest).enqueue(new Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                Log.i("Sorry", "Connection failure");
            }

            @Override
            public void onResponse(okhttp3.Call call, Response response) throws IOException {
                if (!response.isSuccessful()){
                    Log.i("Connection:","No response");
                } else {
                    String stBody = response.body().string();

//                    for(int i = 0; i<nyTimesResult.getResponse().getDocs().size(); i++){
//                        String nytimesURL = nyTimesResult.getResponse().getDocs().get(i).getWebUrl();
//                        String snippet = nyTimesResult.getResponse().getDocs().get(i).getSnippet();
////                        String lead_paragraph = nyTimesResult.getResponse().getDocs().get(i).getLeadParagraph();
//                    }


                    Gson gson = new Gson();
//                    final NYtimesResult nyTimesResult = gson.fromJson(stBody, NYtimesResult.class);
//
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            TextView title = (TextView) findViewById(R.id.newyorktimes_title);
//                            TextView body = (TextView) findViewById(R.id.newyorktimes_article);
//                            title.setText(nyTimesResult.getResponse().getDocs().get(0).getWebUrl());
//                            body.setText(nyTimesResult.getResponse().getDocs().get(0).getSnippet());
//                        }
//                    });
                    try {
                        final NYtimesResult nyTimesResult = gson.fromJson(stBody, NYtimesResult.class);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (!nyTimesResult.getResponse().getDocs().isEmpty()) {
                                    TextView title = (TextView) findViewById(R.id.newyorktimes_title);
                                    TextView body = (TextView) findViewById(R.id.newyorktimes_article);
                                    title.setText(nyTimesResult.getResponse().getDocs().get(0).getWebUrl());
                                    body.setText(nyTimesResult.getResponse().getDocs().get(0).getSnippet());
                                }
                            }
                        });
                    } catch(Exception e) {
                        e.printStackTrace();
                        try {
                            final NYtimesResult[] nyTimesResult = gson.fromJson(stBody, NYtimesResult[].class);

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (!nyTimesResult[0].getResponse().getDocs().isEmpty()) {
                                        TextView title = (TextView) findViewById(R.id.newyorktimes_title);
                                        TextView body = (TextView) findViewById(R.id.newyorktimes_article);
                                        title.setText(nyTimesResult[0].getResponse().getDocs().get(0).getWebUrl());
                                        body.setText(nyTimesResult[0].getResponse().getDocs().get(0).getSnippet());
                                    }
                                }
                            });
                        } catch(Exception ex) {
                            ex.printStackTrace();
                        }
                    }



                }
            }

        }); //End of newCall


        return nyTimesData;

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSIONS_REQUEST_CODE) {
            if (ContextCompat.checkSelfPermission(this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                    && ContextCompat.checkSelfPermission(this,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                mMap.clear();
                mMap.setMyLocationEnabled(true);
                mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
                if (mLastLocation != null) {
                    currentLatitude = mLastLocation.getLatitude();
                    currentLongitude = mLastLocation.getLongitude();

                    LatLng currentLocation = new LatLng(currentLatitude, currentLongitude);
                    float zoomLevel = 18f;
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, zoomLevel));
                }

                if (prefs.getBoolean(PREFS_CURRENTLY_ON_QUEST, false)) {
                    questButton.setImageResource(R.drawable.cancel_icon);
                    topToolbar.setVisibility(View.GONE);
                    botToolbar.setVisibility(View.GONE);

                    new InitialQuestObjectAsyncTask().execute();
                }

            }
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION},
                    PERMISSIONS_REQUEST_CODE);

            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        mMap.clear();
        mMap.setMyLocationEnabled(true);
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mLastLocation != null) {
            currentLatitude = mLastLocation.getLatitude();
            currentLongitude = mLastLocation.getLongitude();

            LatLng currentLocation = new LatLng(currentLatitude, currentLongitude);
            float zoomLevel = 18f;
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, zoomLevel));
        }

        if (prefs.getBoolean(PREFS_CURRENTLY_ON_QUEST, false)) {
            questButton.setImageResource(R.drawable.cancel_icon);
            topToolbar.setVisibility(View.GONE);
            botToolbar.setVisibility(View.GONE);

            new InitialQuestObjectAsyncTask().execute();
        }


    }

    @Override
    public void onConnectionSuspended(int i) {}

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(MainActivity.this, "Could not connect to Google Maps API Client", Toast.LENGTH_SHORT).show();
    }

    protected void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    protected void onStop() {
        mGoogleApiClient.disconnect();
        queryString = null;
        bottomToolBarSelection = 0;

        undiscoveredButton.setBackgroundResource(R.drawable.circle_white_border_grey);
        undiscoveredButton.setTextColor(Color.parseColor("#FFFFFF"));
        showUndiscoveredLocationsOnly = false;

        filterCoffeeButton.setBackgroundResource(R.drawable.white_border_transparent);
        filterCoffeeButton.setTextColor(Color.parseColor("#FFFFFF"));
        filterFoodButton.setBackgroundResource(R.drawable.white_border_transparent);
        filterFoodButton.setTextColor(Color.parseColor("#FFFFFF"));
        filterBarsButton.setBackgroundResource(R.drawable.white_border_transparent);
        filterBarsButton.setTextColor(Color.parseColor("#FFFFFF"));

        super.onStop();
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        showUndiscoveredLocationsOnly = savedInstanceState.getBoolean("show_discovered_locations_only");
        if (showUndiscoveredLocationsOnly) {
            undiscoveredButton.setBackgroundResource(R.drawable.circle_yellow_border_black);
            undiscoveredButton.setTextColor(Color.parseColor("#FFEB3B"));
        } else {
            undiscoveredButton.setBackgroundResource(R.drawable.circle_white_border_grey);
            undiscoveredButton.setTextColor(Color.parseColor("#FFFFFF"));
        }
        bottomToolBarSelection = savedInstanceState.getInt("bottom_toolbar");

        switch(bottomToolBarSelection) {
            case 1:
                filterFoodButton.setBackgroundResource(R.drawable.yellow_border_black);
                filterFoodButton.setTextColor(Color.parseColor("#FFEB3B"));
                break;
            case 2:
                filterCoffeeButton.setBackgroundResource(R.drawable.yellow_border_black);
                filterCoffeeButton.setTextColor(Color.parseColor("#FFEB3B"));
                break;
            case 3:
                filterBarsButton.setBackgroundResource(R.drawable.yellow_border_black);
                filterBarsButton.setTextColor(Color.parseColor("#FFEB3B"));
                break;
            default: break;
        }

        currentLatitude = savedInstanceState.getDouble("saved_latitude");
        currentLongitude = savedInstanceState.getDouble("saved_longitude");
        if (!prefs.getBoolean(PREFS_CURRENTLY_ON_QUEST, false)) {
            queryString = savedInstanceState.getString("query_string_key");
            if (queryString != null) {
                SearchTerm.getInstance().setSearchTerm(queryString);
                new CompletedQuestMarkersAsyncTask().execute();
            }
        }
        Log.i(TAG, "onRestoreInstanceState: " + queryString);

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            Toast.makeText(MainActivity.this, "Requires Permissions", Toast.LENGTH_SHORT).show();
        }
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        currentLatitude = mLastLocation.getLatitude();
        currentLongitude = mLastLocation.getLongitude();

        outState.putBoolean("show_discovered_locations_only", showUndiscoveredLocationsOnly);
        outState.putInt("bottom_toolbar", bottomToolBarSelection);
        outState.putString("query_string_key", queryString);
        outState.putDouble("saved_latitude", currentLatitude);
        outState.putDouble("saved_longitude", currentLongitude);
        super.onSaveInstanceState(outState);
    }
}
