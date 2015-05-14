package helios.moonlight_android;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import butterknife.ButterKnife;
import butterknife.InjectView;

/*
  * Created by Roshini on 4/15/2015
  * */

public class MapsActivity extends ActionBarActivity {

    public static final String TAG = MapsActivity.class.getSimpleName();

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.

    private AppLocationService appLocationService;

    List<Marker> mMarkers = new ArrayList<Marker>();
    HashMap<String, LatLng> mLostBikeMap = new HashMap<>();
    Vector<LatLng> lostBikeVector = new Vector<>();
    Vector<lostBikeLatLng> mLostBikeVector = new Vector<lostBikeLatLng>();
    Vector<String> mLostBikeIdVector = new Vector<>();
    JSONArray mJsonBikes = null;
    LocationAddress locationAddress = new LocationAddress();


    //@InjectView(R.id.btnGPSShowLocation)Button mShowGPSLocationButton;
    //@InjectView(R.id.btnShowAddress)Button mShowAddressButton;
    @InjectView(R.id.addressTextView)
    TextView mAddressTextView;
    @InjectView(R.id.refreshImageViewMaps)
    ImageView mRefreshImageViewMaps;
    @InjectView(R.id.progressBarMaps)
    ProgressBar mProgressBarMaps;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        ButterKnife.inject(this);

        setUpMapIfNeeded();
        Parse.initialize(this, "Un6I0fGo4poWYSEsg4muV3G09C7OzNafBv7F2GIi", "AbitumkApEu1nmH6EXNkPe2r2D8khB7wFU5hHi7i");

        mProgressBarMaps.setVisibility(View.INVISIBLE);


        mRefreshImageViewMaps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "clear and retrieve!");
                //mlist_item.setAdapter(null);
                mMarkers.clear();
                mLostBikeMap.clear();
                mLostBikeIdVector.clear();
                mMap.clear();

                appLocationService = new AppLocationService(MapsActivity.this);
                Location location = appLocationService.getLocation(LocationManager.GPS_PROVIDER);

                if (location != null) {
                    final double currentLatitude = location.getLatitude();
                    final double currentLongitude = location.getLongitude();
                    LatLng latLng = new LatLng(currentLatitude, currentLongitude);
                     /*Collects data from Bike Index*/
                    getIdList(latLng);
                     /*Collect Data from parse and display*/

                    final LocationAddress locationAddress = new LocationAddress();
                    locationAddress.getAddressFromLocation(currentLatitude, currentLongitude,
                            getApplicationContext(), new GeocoderHandler());
                    /////////////////////////////////////////////////////////////
                    //Update UI for Current Location:
                    mMap.clear();

                    mMap.addMarker(new MarkerOptions()
                            .position(latLng)
                            .title("You are Here!"))
                            .showInfoWindow();
                    Log.i(TAG, String.valueOf(mAddressTextView.getText()));
                    mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                        @Override
                        public boolean onMarkerClick(Marker marker) {
                            if (marker.getTitle().equals("You are Here!")) {
                                locationAddress.getAddressFromLocation(currentLatitude, currentLongitude,
                                        getApplicationContext(), new GeocoderHandler());
                                //marker.setSnippet(String.valueOf(mAddressTextView.getText()));
                                Log.i(TAG, String.valueOf(mAddressTextView.getText()));
                                //marker.showInfoWindow();
                            }
                            return true;
                        }
                    });
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 6));

                } else {
                    showSettingsAlert();
                }
            }
        });

//         appLocationService = new AppLocationService(MapsActivity.this);
//         mShowGPSLocationButton.setOnClickListener(new View.OnClickListener() {
//             @Override
//             public void onClick(View v) {
//                 Location gpsLocation = appLocationService.getLocation(LocationManager.GPS_PROVIDER);
//
//                 if (gpsLocation != null){
//                     String result = "Latitude: " + gpsLocation.getLatitude() +
//                             " Longitude: " + gpsLocation.getLongitude();
//                     Toast.makeText(MapsActivity.this, result, Toast.LENGTH_LONG).show();
//                 } else {
//                     showSettingsAlert();
//                 }
//             }
//         });

//         mShowAddressButton.setOnClickListener(new View.OnClickListener() {
//             @Override
//             public void onClick(View v) {
//                 Location location = appLocationService.getLocation(LocationManager.GPS_PROVIDER);
//
//                 if (location != null) {
//                     final double currentLatitude = location.getLatitude();
//                     final double currentLongitude = location.getLongitude();
//                     LatLng latLng = new LatLng(currentLatitude, currentLongitude);
//                     getIdList(latLng);
//                     final LocationAddress locationAddress = new LocationAddress();
//                     locationAddress.getAddressFromLocation(currentLatitude, currentLongitude,
//                             getApplicationContext(), new GeocoderHandler());
//                     /////////////////////////////////////////////////////////////
//                     //Update UI for Current Location:
//                     mMap.clear();
//
//                     mMap.addMarker(new MarkerOptions()
//                         .position(latLng)
//                         .title("You are Here!"))
//                         .showInfoWindow();
//                     Log.i(TAG, String.valueOf(mAddressTextView.getText()));
//                     mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
//                         @Override
//                         public boolean onMarkerClick(Marker marker) {
//                             if (marker.getTitle().equals("You are Here!")) {
//                                 //locationAddress.getAddressFromLocation(currentLatitude, currentLongitude,
//                                 //getApplicationContext(), new GeocoderHandler());
//                                 //marker.setSnippet(String.valueOf(mAddressTextView.getText()));
//                                 //Log.i(TAG, String.valueOf(mAddressTextView.getText()));
//                                 //marker.showInfoWindow();
//                             }
//                             return true;
//                         }
//                     });
//                     mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
//                     mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 6));
//
//                 } else {
//                     showSettingsAlert();
//                 }
//             }
//         });

    }///

    private void getIdList(LatLng latLng) {
        //Actual code
        String lat = latLng.latitude + "";
        String lng = latLng.longitude + "";


        //For testing purposes on emulator
        //String lat = "37.3000";
        //String lng = "-120.4833";

        String proximity = "1";
        String latLongURL = "https://bikeindex.org:443/api/v2/bikes_search/stolen?page=1&proximity="
                + lat + "%2C" + lng +
                "&proximity_square="
                + proximity + "&access_token=0824a7ad26ac4576f365caa9d0587155a99f4b464ae98cc5c64d01cbad7be49a";

        //Get Data from Parse
        Parse.initialize(this, "Un6I0fGo4poWYSEsg4muV3G09C7OzNafBv7F2GIi", "AbitumkApEu1nmH6EXNkPe2r2D8khB7wFU5hHi7i");
        ParseQuery<ParseObject> query = ParseQuery.getQuery("StolenList");
        query.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject parseObject, ParseException e) {
                if (e == null) {
                    //Log.e(parseObject.get)
                }
            }
        });

        //Get data from Bike Index
        if (isNetworkAvailable()) {
            toggleRefresh();
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder().url(latLongURL).build();

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
                    Log.d(TAG, "alertUserAboutError from getting list of ids");
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
                        //Get string of JSON
                        int bikeId = 0;
                        if (response.isSuccessful()) {
                            String jsonData = response.body().string();
                            Log.v(TAG, "isSuccessful()");
                            Log.v(TAG, "From JSON: " + jsonData);

                            //Convert sub-part of collected string into JSON
                            JSONObject bikes = new JSONObject(jsonData);
                            String bikeDetails = bikes.getString("bikes");
                            Log.i(TAG, "From JSON: " + bikeDetails);

                            mJsonBikes = bikes.getJSONArray("bikes");
                            for (int i = 0; i < mJsonBikes.length(); i++) {
                                JSONObject object = mJsonBikes.getJSONObject(i);
                                String id = object.getString("id");
                                mLostBikeIdVector.add(id);
                            }
                            //                             JSONObject bikeDetailsJSON = new JSONObject(bikeDetails);
                            //                             bikeId = bikeDetailsJSON.getInt("id");
                            //                             Log.i(TAG, "From JSON: " + bikeId);

                            Log.v(TAG, "Ids include: " + mLostBikeIdVector.toString());

                            for (int j = 0; j < mLostBikeIdVector.size(); j++) {
                                getLostBikeLatLong(mLostBikeIdVector.get(j).toString());
                            }

                            for (int i = 0; i < mLostBikeMap.size(); i++) {
                                Log.v(TAG, mLostBikeMap.get(i).toString());
                            }

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Log.d(TAG, "inRunUiThread");
                                    toggleRefresh();
                                    //Refresh button does not work properly until these
                                    //two lines are added
                                    mProgressBarMaps.setVisibility(View.INVISIBLE);
                                    mRefreshImageViewMaps.setVisibility(View.VISIBLE);
                                }
                            });
                        }
                        //                         lostBikeMap.put(bikeId, new LatLng(latitude, longitude));
                        //                         Log.v(TAG, lostBikeMap.entrySet().toString());
                        //                         //lostBikeVector.add(new LatLng(latitude, longitude));
                        //                         //mLostBikeVector.
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

    private void getLostBikeLatLong(String bikeId) {
        String id = bikeId;
        String latLongURL = "https://bikeindex.org:443/api/v2/bikes/"
                + bikeId
                + "?access_token=0824a7ad26ac4576f365caa9d0587155a99f4b464ae98cc5c64d01cbad7be49a";

        if (isNetworkAvailable()) {
            //toggleRefresh();
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder().url(latLongURL).build();

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
                    Log.d(TAG, "alertUserAboutError from Maps");
                }

                @Override
                public void onResponse(Response response) throws IOException {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //toggleRefresh();
                        }
                    });
                    double latitude = 0.0;
                    double longitude = 0.0;
                    LatLng latLng = new LatLng(latitude, longitude);
                    String bikeId = "";
                    String bikeTitle = "";
                    try {
                        //Get string of JSON
                        if (response.isSuccessful()) {
                            String jsonData = response.body().string();
                            Log.v(TAG, "Getting Lost bike lat/lng and details");
                            Log.v(TAG, "From JSON: " + jsonData);

                            //Convert sub-part of collected string into JSON
                            JSONObject bike = new JSONObject(jsonData);
                            String bikeDetails = bike.getString("bike");
                            Log.i(TAG, "From JSON: " + bikeDetails);

                            JSONObject bikeDetailsJSON = new JSONObject(bikeDetails);
                            bikeId = bikeDetailsJSON.getString("id");
                            Log.i(TAG, "From JSON: " + bikeId);
                            bikeTitle = bikeDetailsJSON.getString("title");
                            Log.i(TAG, "From JSON: " + bikeTitle);
                            String bikeStolenRecord = bikeDetailsJSON.getString("stolen_record");
                            Log.i(TAG, "From JSON: " + bikeStolenRecord);

                            JSONObject stolenRecordJSON = new JSONObject(bikeStolenRecord);
                            latitude = stolenRecordJSON.getDouble("latitude");
                            longitude = stolenRecordJSON.getDouble("longitude");
                            Log.i(TAG, "From JSON: " + latitude);
                            Log.i(TAG, "From JSON: " + longitude);

                            final String finalBikeTitle = bikeTitle;
                            final String finalBikeId = bikeId;
                            final double finalLatitude = latitude;
                            final double finalLongitude = longitude;
                            latLng = new LatLng(latitude, longitude);
                            final LatLng finalLatLng = latLng;
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Log.d(TAG, "inRunUiThread");
                                    //toggleRefresh();
                                    //Refresh button does not work properly until these
                                    //two lines are added
                                    //mProgressBarMaps.setVisibility(View.INVISIBLE);
                                    //mRefreshImageViewMaps.setVisibility(View.VISIBLE);
                                    Log.i(TAG, "Displaying Markers");
                                    Marker marker = mMap.addMarker(new MarkerOptions()
                                                    .position(finalLatLng)
                                                    .title(finalBikeTitle + ", " + finalBikeId)
                                    );
                                    mMarkers.add(marker);
                                    Log.v(TAG, "Markers size(should increase): " + mMarkers.size());
                                    mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                                        @Override
                                        public boolean onMarkerClick(Marker marker) {
                                            locationAddress.getAddressFromLocation(marker.getPosition().latitude, marker.getPosition().longitude,
                                                    getApplicationContext(), new GeocoderHandler());
                                            //marker.setSnippet(String.valueOf(mAddressTextView.getText()));
                                            Log.i(TAG, String.valueOf(mAddressTextView.getText()));
                                            marker.showInfoWindow();
                                            return false;
                                        }
                                    });
                                }
                            });
                        }
                        mLostBikeMap.put(bikeId, latLng);
                        Log.v(TAG, "END OF getting lost bike lat/lng and details ");
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

    private void toggleRefresh() {
        if (mProgressBarMaps.getVisibility() == View.INVISIBLE) {
            mProgressBarMaps.setVisibility(View.VISIBLE);
            mRefreshImageViewMaps.setVisibility(View.INVISIBLE);
        } else {
            mProgressBarMaps.setVisibility(View.INVISIBLE);
            mRefreshImageViewMaps.setVisibility(View.VISIBLE);
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

    public void showSettingsAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                MapsActivity.this);
        alertDialog.setTitle("SETTINGS");
        alertDialog.setMessage("Enable Location Provider! Go to settings menu?");
        alertDialog.setPositiveButton("Settings",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(
                                Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        MapsActivity.this.startActivity(intent);
                    }
                });
        alertDialog.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        alertDialog.show();
    }


    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #mMap} is not null.
     * <p/>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p/>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p/>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap() {
        //mMap.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Marker"));
    }

    private class GeocoderHandler extends Handler {
        @Override
        public void handleMessage(Message message) {
            String locationAddress;
            switch (message.what) {
                case 1:
                    Bundle bundle = message.getData();
                    locationAddress = bundle.getString("address");
                    break;
                default:
                    locationAddress = null;
            }
            mAddressTextView.setText(locationAddress);
        }
    }
}
