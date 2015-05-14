package helios.moonlight_android;

import android.app.Activity;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.SimpleExpandableListAdapter;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DeviceControlActivity extends ActionBarActivity {
    public static final String EXTRAS_DEVICE_NAME = "DEVICE_NAME";
    public static final String EXTRAS_DEVICE_ADDRESS = "DEVICE_ADDRESS";

    private TextView mConnectionState;
    private TextView mDataField;
    private TextView mRssiField;

    private String mDeviceName;
    private String mDeviceAddress;

    private BluetoothLeService mBluetoothLeService;
    private ArrayList<ArrayList<BluetoothGattCharacteristic>> mGattCharacteristics =
            new ArrayList<ArrayList<BluetoothGattCharacteristic>>();

    private final String LIST_NAME = "NAME";
    private final String LIST_UUID = "UUID";

    private boolean mConnected = false;

    private Button mTheftOn;
    private Button mTheftOff;
    private Button mDeviceList;
    private Button mResetConfig;
    private ToggleButton mSwitch;
    private String mStringToWrite;

    // Code to manage Service lifecycle.
    private final ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            mBluetoothLeService = ((BluetoothLeService.LocalBinder) service).getService();

            if (!mBluetoothLeService.initialize()) {
                Log.e("", "Unable to initialize Bluetooth");
                finish();
            }

            mBluetoothLeService.connect(mDeviceAddress);
            mBluetoothLeService.setBLEServiceCb(mDCServiceCb);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mBluetoothLeService = null;
        }
    };

    private void writeCharacteristic(BluetoothGattCharacteristic characteristic) {
        if (mBluetoothLeService != null) {
            mBluetoothLeService.writeCharacteristic(characteristic);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gatt_services_characteristics);

        final Intent intent = getIntent();
        mDeviceName = intent.getStringExtra(EXTRAS_DEVICE_NAME);
        mDeviceAddress = intent.getStringExtra(EXTRAS_DEVICE_ADDRESS);

        ((TextView) findViewById(R.id.device_address)).setText(mDeviceAddress);
        mConnectionState = (TextView) findViewById(R.id.connection_state);
        mRssiField = (TextView) findViewById(R.id.signal_rssi);

        mTheftOn = (Button) findViewById(R.id.theft_on);
        mTheftOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mStringToWrite = "off";
                byte[] mStringByte = mStringToWrite.getBytes();

                mBluetoothLeService.writeCharacteristic_new(mStringByte);
            }
        });

        mTheftOff = (Button) findViewById(R.id.theft_off);
        mTheftOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mStringToWrite = "on";
                byte[] mStringByte = mStringToWrite.getBytes();

                mBluetoothLeService.writeCharacteristic_new(mStringByte);
            }
        });

        mResetConfig = (Button) findViewById(R.id.reset_config);
        mResetConfig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mStringToWrite = "off";
                byte[] mStringByte = mStringToWrite.getBytes();

                mBluetoothLeService.writeCharacteristic_new(mStringByte);
            }
        });

        mDeviceList = (Button) findViewById(R.id.device_list);
        mDeviceList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DeviceControlActivity.this, DeviceScanActivity.class);
                startActivity(intent);
            }
        });

        //getActionBar().setTitle(mDeviceName);
        //getActionBar().setDisplayHomeAsUpEnabled(true);
        Intent gattServiceIntent = new Intent(this, BluetoothLeService.class);
        bindService(gattServiceIntent, mServiceConnection, BIND_AUTO_CREATE);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (mBluetoothLeService != null) {
            final boolean result = mBluetoothLeService.connect(mDeviceAddress);
            Log.d("", "Connect request result=" + result);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(mServiceConnection);
        mBluetoothLeService = null;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.gatt_services, menu);
        if (mConnected) {
            menu.findItem(R.id.menu_connect).setVisible(false);
            menu.findItem(R.id.menu_disconnect).setVisible(true);
        } else {
            menu.findItem(R.id.menu_connect).setVisible(true);
            menu.findItem(R.id.menu_disconnect).setVisible(false);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_connect:
                mBluetoothLeService.connect(mDeviceAddress);
                return true;
            case R.id.menu_disconnect:
                mBluetoothLeService.disconnect();
                return true;
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void updateConnectionState(final int resourceId) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mConnectionState.setText(resourceId);
            }
        });
    }

    private void displayData(String data) {
        if (data != null) {
            mDataField.setText(data);
        }
    }

    private void displayRssi(String rssi) {
        if (rssi != null) {
            mRssiField.setText(rssi);
        }
    }

    private DCServiceCb mDCServiceCb = new DCServiceCb();

    public class DCServiceCb implements BluetoothLeService.BLEServiceCallback {

        @Override
        public void displayRssi(final int rssi) {
            runOnUiThread(new Runnable() {
                              @Override
                              public void run() {
                                  DeviceControlActivity.this.displayRssi(String.valueOf(rssi));
                              }
                          }
            );

        }

        @Override
        public void displayData(final String data) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    DeviceControlActivity.this.displayData(data);
                }
            });

        }

        @Override
        public void notifyConnectedGATT() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mConnected = true;
                    updateConnectionState(R.string.connected);
                    invalidateOptionsMenu();
                }
            });

        }

        @Override
        public void notifyDisconnectedGATT() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mConnected = false;
                    updateConnectionState(R.string.disconnected);
                    invalidateOptionsMenu();
                    // clearUI();
                }
            });
        }
    }
}
