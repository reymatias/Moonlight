package helios.moonlight_android;

import java.util.Set; // Utilize 'Set'

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.app.ListActivity; // 'List activities'
import android.content.Intent; // 'Intent' library
import android.view.Menu;
import android.view.MenuItem;
import android.bluetooth.BluetoothAdapter; // Bluetooth adapter
import android.bluetooth.BluetoothDevice; // Bluetooth device
import android.bluetooth.BluetoothManager; // Bluetooth manager

public class MainActivity extends ActionBarActivity {
    // Viewed the following tutorial on using Bluetooth on an Android app
    // TutorialsPoint: http://www.tutorialspoint.com/android/android_bluetooth.htm

    // Variables used for Bluetooth connectivity
    private BluetoothAdapter btadapter;
    private Set<BluetoothDevice> pairedDevice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


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
}
