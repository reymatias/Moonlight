package helios.moonlight_android;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by MoeSaiko on 5/6/2015.
 */
public class DetailActivity extends ActionBarActivity {

    @InjectView(R.id.bike_image)
    ImageView mBikeImage;

    @InjectView(R.id.Title)
    TextView mtitle;
    @InjectView(R.id.ID)
    TextView mid;
    @InjectView(R.id.Serial)
    TextView mserial;
    @InjectView(R.id.Manufacturer)
    TextView mmanufacturer_name;
    @InjectView(R.id.Model)
    TextView mframe_model;
    @InjectView(R.id.Year)
    TextView myear;
    @InjectView(R.id.Frame_size)
    TextView mframe_size;
    @InjectView(R.id.Frame_Material)
    TextView mframe_material_slug;
    @InjectView(R.id.Handlebar_type)
    TextView mhandlebar_type_slug;
    //@InjectView(R.id.Wheel_diameter)TextView mlist_item;
    @InjectView(R.id.Primary_colors)
    TextView mframe_colors;
    @InjectView(R.id.Phone_number)TextView mphone_number;
    @InjectView(R.id.Location)
    TextView mstolen_location;
    @InjectView(R.id.Locking_description)
    TextView mlocking_description;
    /*    @InjectView(R.id.Locking_circumvented)
        TextView mlist_item;*/
    @InjectView(R.id.Date_stolen)
    TextView mdate_stolen;
    @InjectView(R.id.Description)
    TextView mtheft_description;
    @InjectView(R.id.Police_report)
    TextView mpolice_report_number;
    @InjectView(R.id.Department_city)
    TextView mpolice_report_department;

    public static final String TAG = StolenListActivity.class.getSimpleName();

    // JSON Node names
    private static final String TAG_BIKES = "bike";
    //within it
    private static final String TAG_ID = "id";
    private static final String TAG_TITLE = "title";
    private static final String TAG_SERIAL = "serial";
    private static final String TAG_MANUFACTURER_NAME = "manufacturer_name";
    private static final String TAG_FRAME_MODEL = "frame_model";
    private static final String TAG_YEAR = "year";
    private static final String TAG_FRAME_COLORS = "frame_colors";
    //private static final String TAG_THUMB = "thumb";
    private static final String TAG_LARGE_IMG = "large_img";
    //private static final String TAG_IS_STOCK_IMG = "is_stock_img";
    private static final String TAG_STOLEN = "stolen";
    private static final String TAG_STOLEN_LOCATION = "stolen_location";
    private static final String TAG_DATE_STOLEN = "date_stolen";
    private static final String TAG_FRAME_SIZE = "frame_size";
    private static final String TAG_HANDLEBAR_TYPE_SLUG = "handlebar_type_slug";
    private static final String TAG_FRAME_MATERIAL_SLUG = "frame_material_slug";
    // JSON Node names
    private static final String TAG_STOLEN_RECORD = "stolen_record";
    //within it
    //private static final String TAG_DATE_STOLEN= "date_stolen";
    private static final String TAG_LOCATION = "location";
    private static final String TAG_LATITUDE = "latitude";
    private static final String TAG_LONGITUDE = "longitude";
    private static final String TAG_THEFT_DESCRIPTION = "theft_description";
    private static final String TAG_LOCKING_DESCRIPTION = "locking_description";
    private static final String TAG_LOCK_DEFEAT_DESCRIPTION = "lock_defeat_description";
    private static final String TAG_POLICE_REPORT_NUMBER = "police_report_number";
    private static final String TAG_POLICE_REPORT_DEPARTMENT = "police_report_department";
    private static final String TAG_DATA_FROM = "BikeIndex";
    //private static final String TAG_LIST_ITEM = "list_item";
    //String large_img = "a";

    private static Context context;

    ArrayList<HashMap<String, String>> bikeList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        DetailActivity.context = getApplicationContext();
        ButterKnife.inject(this);

        bikeList = new ArrayList<HashMap<String, String>>();
        getStolenBikeDetail();
        //Log.i(TAG, large_img);

        //Picasso.with(this).load(large_img).into(mBikeImage);
    }

    private void getStolenBikeDetail() {
        Intent intent = getIntent();
        final String id = intent.getStringExtra(TAG_ID);
        final String title = intent.getStringExtra(TAG_TITLE);
        String datafrom = intent.getStringExtra(TAG_DATA_FROM);
        Log.d(TAG, datafrom);
        String stolen_location = intent.getStringExtra(TAG_STOLEN_LOCATION);


        if (datafrom.contentEquals("Moonlight")) {
            Parse.initialize(this, "Un6I0fGo4poWYSEsg4muV3G09C7OzNafBv7F2GIi", "AbitumkApEu1nmH6EXNkPe2r2D8khB7wFU5hHi7i");

            Log.d(TAG, "Moonlight();");
            ParseQuery<ParseObject> query = ParseQuery.getQuery("BikeProfile");
            query.whereEqualTo("title", title);
            query.getFirstInBackground(new GetCallback<ParseObject>() {
                public void done(ParseObject profile, ParseException e) {
                    if (e == null) {
                        Log.d("Email", "Retrieved " + profile.getString("Email"));
                        //String name = result.getString("Name");

                     //title = profile.getString("title"));
                        final String manufacturer_name = profile.getString("manufacturer_name");
                        final String frame_model = profile.getString("frame_model");
                        final String year = profile.getString("year");
                        final String serial = profile.getString("serial");
                        final String frame_colors = profile.getString("frame_colors");
                        final String stolen_address = profile.getString("Stolen_Address");


                        //mprofile_notes.setText(profile.getString("Notes"));

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Log.d(TAG, "updateDisplay();");

                                mtitle.setText(title);
                                mid.setText("ID: " + id);
                                mmanufacturer_name.setText("Manufacturer: " + manufacturer_name);
                                mframe_model.setText("Model: " + frame_model);
                                myear.setText("Year: " + year);
                                mframe_colors.setText("Color: " + frame_colors);
                                mserial.setText("Serial: " + serial);

                                mstolen_location.setText("Last Location Seen: " + stolen_address);

                                mphone_number.setText("");
                                mframe_size.setText("");
                                mframe_material_slug.setText("");
                                mhandlebar_type_slug.setText("");
                                mlocking_description.setText("");
                                mdate_stolen.setText("");
                                mtheft_description.setText("");
                                mpolice_report_number.setText("");
                                mpolice_report_department.setText("");
                            }

                        });
                    } else {
                        Log.d("Email", "Error: No info given!" + e.getMessage());
                    }
                }
            });



        } else {
            String bikeindexURL = "https://bikeindex.org/api/v2/bikes/" + id;
            Log.i(TAG, bikeindexURL);

            if (isNetworkAvailable()) {
                //toggleRefresh();
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder().url(bikeindexURL).build();

                Call call = client.newCall(request);
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(Request request, IOException e) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //toggleRefresh();
                            }
                        });
                        Log.d(TAG, "alertUserAboutError();");
                    }

                    @Override
                    public void onResponse(Response response) throws IOException {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //toggleRefresh();
                            }
                        });

                        try {
                            String jsonData = response.body().string();
                            Log.v(TAG, jsonData);

                            //Response response = call.execute(); SYNCHRONOUS METHOD
                            if (response.isSuccessful()) {
                                Log.d(TAG, "isSuccessful();");

                                JSONObject bike = new JSONObject(jsonData);

                                String bikeDetails = bike.getString(TAG_BIKES);
                                JSONObject c = new JSONObject(bikeDetails);

                                //for (int i = 0; i < bikes.length(); i++) {
                                //JSONObject c = bikes.getJSONObject(i);

                                final String id = c.getString(TAG_ID);
                                final String title = c.getString(TAG_TITLE);
                                final String serial = c.getString(TAG_SERIAL);
                                final String manufacturer_name = c.getString(TAG_MANUFACTURER_NAME);
                                final String frame_model = c.getString(TAG_FRAME_MODEL);
                                final String year = c.getString(TAG_YEAR);

                                JSONArray colors = c.getJSONArray(TAG_FRAME_COLORS);
                                Vector<String> all_frame_color = new Vector<>();
                                for (int i = 0; i < colors.length(); i++) {
                                    String frame_color = colors.getString(i);
                                    all_frame_color.add(frame_color);
                                }

                                //String thumb = c.getString(TAG_THUMB);
                                final String large_img = c.getString(TAG_LARGE_IMG);
                                //String is_stock_img = c.getString(TAG_IS_STOCK_IMG);
                                String stolen = c.getString(TAG_STOLEN);
                                String stolen_location = c.getString(TAG_STOLEN_LOCATION);
                                final String date_stolen = c.getString(TAG_DATE_STOLEN);
                                final String frame_size = c.getString(TAG_FRAME_SIZE);
                                final String handlebar_type_slug = c.getString(TAG_HANDLEBAR_TYPE_SLUG);
                                final String frame_material_slug = c.getString(TAG_FRAME_MATERIAL_SLUG);

                                String stolenDetails = c.getString(TAG_STOLEN_RECORD);
                                JSONObject stolen_record = new JSONObject(stolenDetails);
                                //JSONObject stolen_record = c.getJSONObject(TAG_STOLEN_RECORD);
                                final String location = stolen_record.getString(TAG_LOCATION);
                                String latitude = stolen_record.getString(TAG_LATITUDE);
                                String longitude = stolen_record.getString(TAG_LONGITUDE);
                                //final String list_item = stolen_record.getString(TAG_LIST_ITEM);
                                //String date_stolen = stolen_record.getString(TAG_DATE_STOLEN);
                                final String theft_description = stolen_record.getString(TAG_THEFT_DESCRIPTION);
                                final String locking_description = stolen_record.getString(TAG_LOCKING_DESCRIPTION);
                                String lock_defeat_description = stolen_record.getString(TAG_LOCK_DEFEAT_DESCRIPTION);
                                final String police_report_number = stolen_record.getString(TAG_POLICE_REPORT_NUMBER);
                                final String police_report_department = stolen_record.getString(TAG_POLICE_REPORT_DEPARTMENT);
                                String frame_color = "";

                                for (int i = 0; i < all_frame_color.size(); i++) {
                                    frame_color += all_frame_color.get(i);
                                    if (i == all_frame_color.size() - 1)
                                        frame_color += " ";
                                    else
                                        frame_color += ", ";
                                }

                                //mCurrentStolen = getCurrentDetails(jsonData);
                                //Let main UI thread know that data is available from http request
                                final String finalFrame_color = frame_color;

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Log.d(TAG, "updateDisplay();");
                                        //updateDisplay();
                                        //toggleRefresh();
                                        //Refresh button does not work properly until these
                                        //two lines are added
                                        //mProgressBar.setVisibility(View.INVISIBLE);
                                        //mRefreshImageView.setVisibility(View.VISIBLE);
                                        mtitle.setText(title);
                                        mid.setText("ID: " + id);
                                        mserial.setText("Serial: " + serial);
                                        mmanufacturer_name.setText("Manufacturer: " + manufacturer_name);
                                        mframe_model.setText("Model: " + frame_model);
                                        myear.setText("Year: " + year);
                                        mframe_colors.setText("Color: " + finalFrame_color);
                                        //stolen
                                        mframe_size.setText("Frame Size: " + frame_size);
                                        mframe_material_slug.setText("Frame Material: " + frame_material_slug);
                                        mhandlebar_type_slug.setText("Handlebar Type: " + handlebar_type_slug);

                                        mphone_number.setText("");
                                        mstolen_location.setText("Last Location Seen: " + location);
                                        mlocking_description.setText("Locking Description: " + locking_description);
                                        //mlist_item.setText(list_item);
                                        mdate_stolen.setText("Date Stolen: " + date_stolen);
                                        mtheft_description.setText("Theft Description: " + theft_description);
                                        mpolice_report_number.setText("Police Report Number: " + police_report_number);
                                        mpolice_report_department.setText("Police Report Department: " + police_report_department);
                                        Log.i(TAG, large_img);
                                        //Picasso.with(DetailActivity.getAppContext()).load(large_img).into(mBikeImage);
                                        Picasso.with(getApplicationContext()).load(large_img).into(mBikeImage);
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

    public static Context getAppContext() {
        return DetailActivity.context;
    }

}