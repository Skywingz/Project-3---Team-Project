package com.example.ubun17.nytimes;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.example.ubun17.nytimes.models.NYtimesResult;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class MainActivity extends AppCompatActivity {
    String nyAPIkey = "48c66fa2c448eda40826487d4f19a018:0:71658152";
    String nyAPIurl = "http://api.nytimes.com/svc/search/v2/articlesearch.json?q=shake shack&sort=newest&&api-key=48c66fa2c448eda40826487d4f19a018:0:71658152";
    String baseURL = "http://api.nytimes.com/svc/search/v2/articlesearch.json?q=";

    ArrayList<String> nyArray = new ArrayList<String>();
    ArrayAdapter adapter;
    ListView listview;

    EditText searchTerm;
    Button buSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listview = (ListView) findViewById(R.id.listView);
        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, nyArray);
        listview.setAdapter(adapter);
        buSearch = (Button) findViewById(R.id.button);
        searchTerm = (EditText) findViewById(R.id.searchEdit);

        buSearch.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                String searchString = searchTerm.getText().toString();
                String finalAPIstring = baseURL+searchString+"&sort=newest&&api-key="+ nyAPIkey;
                Log.d("url : ", finalAPIstring);
                returnAPIcall(finalAPIstring);
            }
        });

    }//End of onCreate

    public void returnAPIcall(String finalURL){
        OkHttpClient client = new OkHttpClient();

        final Request apiRequest = new Request.Builder()
                .url(finalURL)
                .build();

        client.newCall(apiRequest).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i("Sorry", "Connection failure");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()){
                    Log.i("Connection:","No response");
                } else {
                    String stBody = response.body().string();
                    nyArray.clear();
                    Gson gson = new Gson();
                    NYtimesResult nyTimesResult = gson.fromJson(stBody, NYtimesResult.class);

                    for(int i = 0; i<nyTimesResult.getResponse().getDocs().size(); i++){
                        String url = nyTimesResult.getResponse().getDocs().get(i).getWebUrl();
                        String snippet = nyTimesResult.getResponse().getDocs().get(i).getSnippet();
                        String lead_paragraph = nyTimesResult.getResponse().getDocs()
                                .get(i).getLeadParagraph();
                        nyArray.add(snippet);
                    }

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            adapter.notifyDataSetChanged();
                        }
                    });
                }
            }
        }); //End of newCall

    }//end of returnAPIcall
}//End of MainActivity
