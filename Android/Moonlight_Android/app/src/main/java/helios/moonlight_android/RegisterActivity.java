package helios.moonlight_android;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.io.File;
import java.io.IOException;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class RegisterActivity extends ActionBarActivity {

/*    protected EditText profile_name;
    protected EditText profile_email;
    protected EditText profile_make;
    protected EditText profile_model;
    protected EditText profile_year;
    protected EditText profile_serial;
    protected EditText profile_colors;
    protected EditText profile_notes;
    protected TextView profile_upload;
    protected Button profile_submit;*/

    @InjectView(R.id.profile_name)
    EditText mprofile_name;
    @InjectView(R.id.profile_email)
    TextView mprofile_email;
    @InjectView(R.id.profile_make)
    EditText mprofile_make;
    @InjectView(R.id.profile_model)
    EditText mprofile_model;
    @InjectView(R.id.profile_year)
    EditText mprofile_year;
    @InjectView(R.id.profile_serial)
    EditText mprofile_serial;
    @InjectView(R.id.profile_colors)
    EditText mprofile_colors;
    @InjectView(R.id.profile_notes)
    EditText mprofile_notes;
    @InjectView(R.id.profile_upload)
    TextView mprofile_upload;
    @InjectView(R.id.profile_submit)
    Button mprofile_submit;
    @InjectView(R.id.profile_snap)
    ImageButton mprofile_snap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Parse.initialize(this, "Un6I0fGo4poWYSEsg4muV3G09C7OzNafBv7F2GIi", "AbitumkApEu1nmH6EXNkPe2r2D8khB7wFU5hHi7i");
        final ParseUser currentUser = ParseUser.getCurrentUser();


        ButterKnife.inject(this);

        final String email = currentUser.getEmail();
        final String relationID = currentUser.getObjectId();
        mprofile_email.setText(email);
        mprofile_name.setHint("Name of BIKE");


        ParseQuery<ParseObject> query = ParseQuery.getQuery("BikeProfile");
        query.whereEqualTo("Email", email);
        query.getFirstInBackground(new GetCallback<ParseObject>() {
            public void done(ParseObject profile, ParseException e) {
                if (e == null) {
                    Log.d("Email", "Retrieved " + profile.getString("Email"));
                    //String name = result.getString("Name");
<<<<<<< Updated upstream
                    mprofile_name.setText(profile.getString("title"));
                    mprofile_make.setText(profile.getString("manufacturer_name"));
                    mprofile_model.setText(profile.getString("frame_model"));
                    mprofile_year.setText(profile.getString("year"));
                    mprofile_serial.setText(profile.getString("Serial"));
                    mprofile_colors.setText(profile.getString("frame_colors"));
=======
                    mprofile_name.setText(profile.getString("Name"));
                    mprofile_make.setText(profile.getString("Make"));
                    mprofile_model.setText(profile.getString("Model"));
                    mprofile_year.setText(profile.getString("Year"));
                    mprofile_serial.setText(profile.getString("Serial"));
                    mprofile_colors.setText(profile.getString("Color"));
>>>>>>> Stashed changes
                    mprofile_notes.setText(profile.getString("Notes"));
                } else {
                    Log.d("Email", "Error: No info given!" + e.getMessage());
                }
            }
        });

        mprofile_snap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // picture taking
            }
        });

        //mprofile_submit.setOnClickListener(this);
        mprofile_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String name = mprofile_name.getText().toString().trim();
                final String make = mprofile_make.getText().toString().trim();
                final String model = mprofile_model.getText().toString().trim();
                final String year = mprofile_year.getText().toString().trim();
                final String serial = mprofile_serial.getText().toString().trim();
                final String colors = mprofile_colors.getText().toString().trim();
                final String notes = mprofile_notes.getText().toString().trim();

                ParseQuery<ParseObject> query = ParseQuery.getQuery("BikeProfile");
                query.whereEqualTo("Email", email);
                query.getFirstInBackground(new GetCallback<ParseObject>() {
                    public void done(ParseObject profile, ParseException e) {
                        if (e == null) {
                            Log.d("ACC", "Updating " + profile.getString("Email"));
                            //String name = result.getString("Name");
<<<<<<< Updated upstream
                            profile.put("title", name);
                            profile.put("Email", email);
                            profile.put("manufacturer_name", make);
                            profile.put("frame_model", model);
                            profile.put("year", year);
                            profile.put("Serial", serial);
                            profile.put("frame_colors", colors);
=======
                            profile.put("Name", name);
                            profile.put("Email", email);
                            profile.put("Make", make);
                            profile.put("Model", model);
                            profile.put("Year", year);
                            profile.put("Serial", serial);
                            profile.put("Color", colors);
>>>>>>> Stashed changes
                            profile.put("Notes", notes);
                            profile.put("OwnerID", relationID);
                            profile.saveInBackground();
                        } else {
                            Log.d("ACC", "Creating New" + e.getMessage());
                            ParseObject newprofile = new ParseObject("BikeProfile");
<<<<<<< Updated upstream
                            newprofile.put("title", name);
                            newprofile.put("Email", email);
                            newprofile.put("manufacturer_name", make);
                            newprofile.put("frame_model", model);
                            newprofile.put("year", year);
                            newprofile.put("Serial", serial);
                            newprofile.put("frame_colors", colors);
=======
                            newprofile.put("Name", name);
                            newprofile.put("Email", email);
                            newprofile.put("Make", make);
                            newprofile.put("Model", model);
                            newprofile.put("Year", year);
                            newprofile.put("Serial", serial);
                            newprofile.put("Color", colors);
>>>>>>> Stashed changes
                            newprofile.put("Notes", notes);
                            newprofile.put("OwnerID", relationID);
                            newprofile.saveInBackground();
                        }
                    }
                });


                Toast.makeText(RegisterActivity.this, "SAVED!", Toast.LENGTH_LONG).show();
            }
        });


    }
}
