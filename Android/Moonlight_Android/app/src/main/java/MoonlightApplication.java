import android.app.Application;

import com.parse.Parse;

/**
 * Created by roshiniThiagarajan on 4/26/15.
 */
public class MoonlightApplication extends Application{
    @Override
    public void onCreate() {
        super.onCreate();
        // Enable Local Datastore.
        Parse.enableLocalDatastore(this);

        Parse.initialize(this, "Un6I0fGo4poWYSEsg4muV3G09C7OzNafBv7F2GIi", "AbitumkApEu1nmH6EXNkPe2r2D8khB7wFU5hHi7i");
    }
}
