package helios.moonlight_android;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.common.SignInButton;
import com.parse.Parse;
import com.parse.ParseUser;
import com.parse.ParseObject;


public class MainActivity extends ActionBarActivity implements View.OnClickListener {

    private SignInButton mSignInButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mSignInButton = (SignInButton) findViewById(R.id.sign_in_button);

        // Button listener
        mSignInButton.setOnClickListener(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
//        Parse.initialize(this, "Un6I0fGo4poWYSEsg4muV3G09C7OzNafBv7F2GIi", "AbitumkApEu1nmH6EXNkPe2r2D8khB7wFU5hHi7i");
//
//        ParseUser currentUser = ParseUser.getCurrentUser();
//        if (currentUser != null) {
//            Toast.makeText(MainActivity.this, "YOU ARE LOGGED IN!", Toast.LENGTH_SHORT).show();
//            // do stuff with the user
//        } else {
//            Intent intent = new Intent(this, LoginTwoActivity.class);
//            startActivity(intent);
//            Toast.makeText(MainActivity.this, "Please Login!", Toast.LENGTH_SHORT).show();
//            // show the signup or login screen
//        }
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(this, LoginTwoActivity.class);
        startActivity(intent);
    }

/*
    public void toSignIn(View view) {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
*/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
        try {
                     // check if any view exists on current view
                     //style = ((Button) findViewById(R.id.xyz_button));
                 } catch (Exception e) {
                     // Button was not found
                     // It means, your button doesn't exist on the "current" view
                     // It was freed from the memory, therefore stop of activity was performed
                     // In this case I restart my app
                     Intent i = new Intent();
                     i.setClass(getApplicationContext(), MainActivity.class);
                     i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                     startActivity(i);
                     // Show toast to the user
                     Toast.makeText(getApplicationContext(), "Data lost due to excess use of other apps", Toast.LENGTH_LONG).show();
                 }
    }
}
