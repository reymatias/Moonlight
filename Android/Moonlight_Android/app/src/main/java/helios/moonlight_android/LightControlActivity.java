package helios.moonlight_android;

import android.app.Activity;
import android.bluetooth.BluetoothGattCharacteristic;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.ToggleButton;

public class LightControlActivity extends Activity {
    /**
     * Bluetooth elements form 'DeviceControlActivity'
     */
    public static final String EXTRAS_DEVICE_NAME = "DEVICE_NAME";
    public static final String EXTRAS_DEVICE_ADDRESS = "DEVICE_ADDRESS";

    private BluetoothLeService mBluetoothLeService;
    private BluetoothGattCharacteristic mNotifyCharacteristic;
    private BluetoothGattCharacteristic mWriteCharacteristic;

    private TextView mConnectionState;
    private TextView mDataField;
    private TextView mRssiField;

    private String mDeviceName;
    private String mDeviceAddress;

    // UI elements
    private Button mConnectButton;
    private Button mDisconnectButton;
    private Button mLedOn;
    private Button mLedOff;
    private ToggleButton mLedSwitch;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // Render UI elements
        super.onCreate(savedInstanceState);
        setContentView(R.layout.light_control);

        // Initialize UI elements
        mConnectButton = (Button) findViewById(R.id.connect);
        mLedOn = (Button) findViewById(R.id.light_on);
        mLedOff = (Button) findViewById(R.id.light_off);
        mDisconnectButton = (Button) findViewById(R.id.disconnect);
        mLedSwitch = (ToggleButton) findViewById(R.id.bluetooth_switch);

        // Button operations
        mConnectButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                // Switch to 'DeviceScanActivity'
                Intent scan_intent = new Intent(LightControlActivity.this, DeviceScanActivity.class);
                startActivity(scan_intent);
            }
        });

        mLedOn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                // Turn lights on

            }
        });

        mLedOff.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                // Turn lights off
            }
        });

        mDisconnectButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                // Disconnect bluetooth using 'DeviceControlActivity'
            }
        });
    }

    /**
     * Code from 'DeviceControlActivity'
     */
}
