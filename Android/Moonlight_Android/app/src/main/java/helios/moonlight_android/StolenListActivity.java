package helios.moonlight_android;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.ButterKnife;
import butterknife.InjectView;

import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import android.support.v7.app.ActionBarActivity;

/**
 * Created by MoeSaiko on 5/1/2015.
 */
public class StolenListActivity extends ActionBarActivity {

    //private CurrentStolen mCurrentStolen;

    @InjectView(R.id.refreshImageView)ImageView mRefreshImageView;
    @InjectView(R.id.progressBar)ProgressBar mProgressBar;
    @InjectView(R.id.list_item)ListView mlist_item;

    public static final String TAG = StolenListActivity.class.getSimpleName();

    // JSON Node names
    private static final String TAG_BIKES = "bikes";
    private static final String TAG_ID = "id";
    private static final String TAG_TITLE = "title";
    private static final String TAG_SERIAL = "serial";
    private static final String TAG_MANUFACTURER_NAME = "manufacturer_name";
    private static final String TAG_FRAME_MODEL = "frame_model";
    private static final String TAG_YEAR = "year";
    private static final String TAG_FRAME_COLORS = "frame_colors";
    private static final String TAG_THUMB = "thumb";
    private static final String TAG_LARGE_IMG = "large_img";
    private static final String TAG_IS_STOCK_IMG = "is_stock_img";
    private static final String TAG_STOLEN = "stolen";
    private static final String TAG_STOLEN_LOCATION = "stolen_location";
    private static final String TAG_DATE_STOLEN = "date_stolen";

    JSONArray bikes = null;

    ArrayList<HashMap<String, String>> bikeList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stolenlist);
        ButterKnife.inject(this);

        mProgressBar.setVisibility(View.VISIBLE);

        bikeList = new ArrayList<HashMap<String, String>>();
        //mlist_item.addFooterView(view);

        mlist_item.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String title = ((TextView) view.findViewById(R.id.title))
                        .getText().toString();
                String stolen_location = ((TextView) view.findViewById(R.id.stolen_location))
                        .getText().toString();
                String bike_id = ((TextView) view.findViewById(R.id.id))
                        .getText().toString();

                //Starting single contact activity
                Intent in = new Intent(getApplicationContext(), DetailActivity.class);
                in.putExtra(TAG_TITLE, title);
                in.putExtra(TAG_ID, bike_id);
                //in.getStringExtra(TAG_ID);
                //Log.d(TAG, in.getStringExtra(TAG_ID));
                in.putExtra(TAG_STOLEN_LOCATION, stolen_location);
                //in.putExtra(TAG_PHONE_MOBILE, description);
                startActivity(in);
            }
        });

        mRefreshImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "clear and retrieve!");
                mlist_item.setAdapter(null);
                bikeList.clear();
                getStolenBikeList();
            }
        });

        getStolenBikeList();
        Log.d(TAG, "Main UI code is running");

    }

    private void getStolenBikeList() {

        //String bikeindexURL = "https://bikeindex.org:443/api/v2/bikes_search/stolen?page=1&proximity=Merced%2C%20CA&proximity_square=25";
        String bikeindexURL = "https://bikeindex.org/api/v2/bikes_search/stolen?page=1&proximity_square=100";
        //will have to mess with search parameters later!!!

        if (isNetworkAvailable()) {
            toggleRefresh();
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder().url(bikeindexURL).build();

            Call call = client.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Request request, IOException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            toggleRefresh();
                        }
                    });
                    Log.d(TAG, "alertUserAboutError();");
                }

                @Override
                public void onResponse(Response response) throws IOException {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            toggleRefresh();
                        }
                    });

                    try {
                        String jsonData = response.body().string();
                        Log.v(TAG, jsonData);

                        //Response response = call.execute(); SYNCHRONOUS METHOD
                        if (response.isSuccessful()) {
                            Log.d(TAG, "isSuccessful();");

                            JSONObject jsonObj = new JSONObject(jsonData);

                            bikes = jsonObj.getJSONArray(TAG_BIKES);

                            for (int i = 0; i < bikes.length(); i++) {
                                JSONObject c = bikes.getJSONObject(i);

                                String id = c.getString(TAG_ID);
                                String title = c.getString(TAG_TITLE);
                                String serial = c.getString(TAG_SERIAL);
                                String manufacturer_name = c.getString(TAG_MANUFACTURER_NAME);
                                String frame_model = c.getString(TAG_FRAME_MODEL);
                                String year = c.getString(TAG_YEAR);
                                //String frame_colors = c.getString(TAG_FRAME_COLORS);
                                String thumb = c.getString(TAG_THUMB);
                                String large_img = c.getString(TAG_LARGE_IMG);
                                String is_stock_img = c.getString(TAG_IS_STOCK_IMG);
                                String stolen = c.getString(TAG_STOLEN);
                                String stolen_location = c.getString(TAG_STOLEN_LOCATION);
                                String date_stolen = c.getString(TAG_DATE_STOLEN);

/*                                JSONArray frame_colors = c.getJSONArray(TAG_FRAME_COLORS);
                                for (int j = 0; j < bikes.length(); j++) {
                                    frame_colors.getString(j);
                                }*/

                                HashMap<String, String> contact = new HashMap<String, String>();

                                contact.put(TAG_ID, id);
                                contact.put(TAG_TITLE, title);
                                contact.put(TAG_SERIAL, serial);
                                contact.put(TAG_MANUFACTURER_NAME, manufacturer_name);
                                contact.put(TAG_FRAME_MODEL, frame_model);
                                contact.put(TAG_YEAR, year);
                                contact.put(TAG_THUMB, thumb);
                                contact.put(TAG_LARGE_IMG, large_img);
                                contact.put(TAG_IS_STOCK_IMG, is_stock_img);
                                contact.put(TAG_STOLEN, stolen);
                                contact.put(TAG_STOLEN_LOCATION, stolen_location);
                                contact.put(TAG_DATE_STOLEN, date_stolen);
                                //bike color is missing

                                bikeList.add(contact);
                            }


                            //mCurrentStolen = getCurrentDetails(jsonData);
                            //Let main UI thread know that data is available from http request
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Log.d(TAG, "updateDisplay();");
                                    updateDisplay();
                                    toggleRefresh();
                                    //Refresh button does not work properly until these
                                    //two lines are added
                                    mProgressBar.setVisibility(View.INVISIBLE);
                                    mRefreshImageView.setVisibility(View.VISIBLE);
                                }
                            });
                        } else {
                            Log.d(TAG, "alertUserAboutError();");
                        }

                    } catch (IOException e) {
                        Log.e(TAG, "Exception caught: ", e);
                    } catch (JSONException e) {
                        Log.e(TAG, "Exception caught: ", e);
                    }

                }
            });
        } else {
            Toast.makeText(this, "Unavailable", Toast.LENGTH_LONG).show();

        }

    }


    private boolean isNetworkAvailable() {

        ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        boolean isAvailable = false;
        //Does active network exist and is it connected.
        if (networkInfo != null && networkInfo.isConnected()) {
            isAvailable = true;
        }
        return isAvailable;
    }

    private void toggleRefresh() {
        if (mProgressBar.getVisibility() == View.INVISIBLE) {
            mProgressBar.setVisibility(View.VISIBLE);
            mRefreshImageView.setVisibility(View.INVISIBLE);
        } else {
            mProgressBar.setVisibility(View.INVISIBLE);
            mRefreshImageView.setVisibility(View.VISIBLE);
        }
    }


    private void updateDisplay() {
        ListAdapter adapter = new SimpleAdapter(
                StolenListActivity.this, bikeList,
                R.layout.list_item, new String[]{TAG_TITLE,
                TAG_STOLEN_LOCATION,TAG_ID}, new int[]{R.id.title,
                R.id.stolen_location, R.id.id});

//        setListAdapter(adapter);
        Log.d(TAG, "mlist_item.setAdapter(adapter);");
        mlist_item.setAdapter(adapter);


    }
}
