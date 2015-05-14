package helios.moonlight_android;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class LostBikeActivity extends ActionBarActivity {

    final static String TAG = LostBikeActivity.class.getSimpleName();
    @InjectView(R.id.report_stolen_loc_textView)EditText mReportStolenTextView;
    @InjectView(R.id.currentLoc_checkbox)CheckBox mCurrentLocCheckBox;
    @InjectView(R.id.email_checkBox) CheckBox mEmailCheckBox;
    @InjectView(R.id.phone_checkBox) CheckBox mPhoneCheckBox;
    @InjectView(R.id.report_button) Button mReportButton;
    @InjectView(R.id.addressTextView)TextView mAddressTextView;
    @InjectView(R.id.email_id_textView) TextView mEmailIdTextView;
    @InjectView(R.id.phone_number_textView) TextView mPhoneNumTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lost_bike2);

        ButterKnife.inject(this);

        Parse.initialize(this, "Un6I0fGo4poWYSEsg4muV3G09C7OzNafBv7F2GIi", "AbitumkApEu1nmH6EXNkPe2r2D8khB7wFU5hHi7i");
        final ParseUser currentUser = ParseUser.getCurrentUser();


        String mCurrentUserId = currentUser.getObjectId();
        //final String[] phone = {currentUser.getString("Phone")};
        //Log.v(TAG, "phone is: " + phone[0]);
        ParseQuery<ParseObject> query = ParseQuery.getQuery("BikeProfile");
        //Constrain value, i.e. it is equivalent to "WHERE" in SQL
        query.whereEqualTo("OwnerID", mCurrentUserId);
        Log.v(TAG, "mCurrentuserId: "+ mCurrentUserId);



        query.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(final ParseObject parseObject, ParseException e) {
                if (parseObject == null) {
                    Log.d("score", "The getFirst request failed.");
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                            LostBikeActivity.this);
                    alertDialog.setTitle("Oops");
                    alertDialog.setMessage("Missing bike details. Please edit your Bike info.");
                    alertDialog.setPositiveButton("Ok",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                       //CALL PROFILE Page
//                                    Intent myIntent = new Intent(((Dialog) dialog).getContext(), RegisterActivity.class);
//                                    startActivity(myIntent);
                                }
                            });
                    alertDialog.setNegativeButton("Cancel",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                    alertDialog.show();
                } else {
                    //phone[0] = currentUser.getString("Phone");
                    //Log.v(TAG, "phone is: " + phone[0]);
                    Log.v("score", "Retrieved the object: " + parseObject);
                    Log.v(TAG, "Get status of current bike: " + parseObject.get("Status"));

                    if(parseObject.get("Status").equals("false")) {
                        parseObject.put("Status", "true");

//                        try {
//                            parseObject.save();
//                            mEmailIdTextView.setText(parseObject.get("Email").toString());
//                            mPhoneNumTextView.setText("");
//
//                        } catch (ParseException e1) {
//                            e1.printStackTrace();
//                            Log.v(TAG, "Report was not submitted. Please try Again.");
//                            AlertDialog.Builder alertDialog = new AlertDialog.Builder(
//                                    LostBikeActivity.this);
//                            alertDialog.setTitle("Oops");
//                            alertDialog.setMessage("Network Error. Please try Again.!");
//                            alertDialog.setPositiveButton("Ok",
//                                    new DialogInterface.OnClickListener() {
//                                        public void onClick(DialogInterface dialog, int which) {
//                                            Intent myIntent = new Intent(((Dialog) dialog).getContext(), LostBikeActivity.class);
//                                            startActivity(myIntent);
//                                        }
//                                    });
//                            alertDialog.setNegativeButton("Cancel",
//                                    new DialogInterface.OnClickListener() {
//                                        public void onClick(DialogInterface dialog, int which) {
//                                            dialog.cancel();
//                                        }
//                                    });
//                            alertDialog.show();
//                        }
                        Log.v(TAG, "Get status of current bike after changing: " + parseObject.get("Status"));
                        mEmailIdTextView.setText(parseObject.get("Email").toString());
                        mPhoneNumTextView.setText("");

                        mCurrentLocCheckBox.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (mCurrentLocCheckBox.isChecked()) {
                                    getCurrentLocation();
                                } else {
                                    mAddressTextView.setText("");
                                }

                            }
                        });

                        mReportButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Double latitude = 0.0;
                                Double longitude = 0.0;
                                String address = "";
                                LatLng latLng = null;
                                //Get data from form and from db to fill Stolen List
                                String bikeId = parseObject.getString("OwnerID");
                                String bikeTitle = parseObject.getString("Name");

                                //Check Checkboxes
                                if (mCurrentLocCheckBox.isChecked()) {
                                    address = String.valueOf(mAddressTextView.getText());
                                    Log.v(TAG, "current location address is: " + address);
                                    latLng = convertAddress(address);
                                    latitude = latLng.latitude;
                                    longitude = latLng.longitude;

                                } else {
                                    //Get address input from user
                                    address = mReportStolenTextView.getText().toString();
                                    latLng = convertAddress(address);
                                    latitude = latLng.latitude;
                                    longitude = latLng.longitude;
                                }

                                ParseObject newStolenBike = new ParseObject("StolenList");
                                newStolenBike.put("BikeId", bikeId);
                                newStolenBike.put("title", bikeTitle);
                                newStolenBike.put("latitude", latitude);
                                newStolenBike.put("longitude", longitude);

                                if (mEmailCheckBox.isChecked()) {
                                    newStolenBike.put("Email", "true");
                                }

                                if (mPhoneCheckBox.isChecked()) {
                                    newStolenBike.put("Phone", "true");
                                }

                                Log.v(TAG, bikeTitle);
                                Log.v(TAG, latitude + " , " + longitude);
                                try {
                                    parseObject.save();
                                    newStolenBike.save();
                                    Toast.makeText(LostBikeActivity.this, "Reported stolen Bike", Toast.LENGTH_LONG).show();
                                    mReportButton.setEnabled(false);

                                } catch (ParseException e1) {
                                    e1.printStackTrace();
                                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                                            LostBikeActivity.this);
                                    alertDialog.setTitle("Oops");
                                    alertDialog.setMessage("Network Error. Report was not submitted. Please try again!");
                                    alertDialog.setPositiveButton("Ok",
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {
                                                    Intent myIntent = new Intent(((Dialog) dialog).getContext(), LostBikeActivity.class);
                                                    startActivity(myIntent);
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
                            }
                        });
                    }
                    else {
                        Toast.makeText(LostBikeActivity.this, "You already reported your Stolen Bike", Toast.LENGTH_LONG).show();
                        mReportButton.setEnabled(false);
                    }
                }
            }
        });

    }

    private void getCurrentLocation() {
        AppLocationService appLocationService = new AppLocationService(LostBikeActivity.this);
        Location location = appLocationService.getLocation(LocationManager.GPS_PROVIDER);

        if (location != null) {
            final double currentLatitude = location.getLatitude();
            final double currentLongitude = location.getLongitude();
            LatLng latLng = new LatLng(currentLatitude, currentLongitude);

            final LocationAddress locationAddress = new LocationAddress();
            locationAddress.getAddressFromLocation(currentLatitude, currentLongitude,
                    getApplicationContext(), new GeocoderHandler());
            /////////////////////////////////////////////////////////////
            //Update UI for Current Location:

            Log.i(TAG, String.valueOf(mAddressTextView.getText()));

        } else {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                    LostBikeActivity.this);
            alertDialog.setTitle("SETTINGS");
            alertDialog.setMessage("Enable Location Provider! Go to settings menu?");
            alertDialog.setPositiveButton("Settings",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(
                                    Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            LostBikeActivity.this.startActivity(intent);
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
    }

    public LatLng convertAddress(String address) {
        Geocoder coder = new Geocoder(this);
        if (address != null && !address.isEmpty()) {
            try {
                List<Address> addressList = coder.getFromLocationName(address, 1);
                if (addressList != null && addressList.size() > 0) {
                    double lat = addressList.get(0).getLatitude();
                    double lng = addressList.get(0).getLongitude();
                    Log.v(TAG, "lat: "+lat + " lng: " +lng);
                    return (new LatLng(lat, lng));
                }
            } catch (Exception e) {
                e.printStackTrace();
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                        LostBikeActivity.this);
                alertDialog.setTitle("Oops");
                alertDialog.setMessage("Please check the address you entered!");
                alertDialog.setPositiveButton("Ok",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Intent myIntent = new Intent(((Dialog) dialog).getContext(), LostBikeActivity.class);
                                startActivity(myIntent);
                            }
                        });
                alertDialog.setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                alertDialog.show();

            } // end catch
        } // end if

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                LostBikeActivity.this);
        alertDialog.setTitle("Oops");
        alertDialog.setMessage("Empty address field!");
        alertDialog.setPositiveButton("Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent myIntent = new Intent(((Dialog) dialog).getContext(), LostBikeActivity.class);
                        startActivity(myIntent);
                    }
                });
        alertDialog.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        alertDialog.show();
        return (new LatLng(0.0, 0.0));
    } // end convertAddress

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_lost_bike, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
