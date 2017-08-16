package br.com.brunohpmarques.ewbc.bluetooth;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

import br.com.brunohpmarques.ewbc.MainActivity;
import br.com.brunohpmarques.ewbc.R;

/**
 * Created by Usuario on 31/01/2017.
 */

public class BluetoothReceive extends BroadcastReceiver {

    private Activity activity;

    public BluetoothReceive(){}

    public void setActivity(Activity activity){
        try{
            if(this.activity != null)
                this.activity.unregisterReceiver(this);
        }catch (Exception e){}

        this.activity = activity;

        IntentFilter filter = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
        this.activity.registerReceiver(this, filter);

        // Register for broadcasts when a device is discovered
        filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        this.activity.registerReceiver(this, filter);

        // Register for broadcasts when discovery has finished
        filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        this.activity.registerReceiver(this, filter);
    }

    public void unregisterReceiver(){
        try {
            activity.unregisterReceiver(this);
        }catch (Exception e){}
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        final String action = intent.getAction();

        if (BluetoothDevice.ACTION_FOUND.equals(action)) {
            BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
            Log.i("ACTION_FOUND", device.getName());
            if (device.getBondState() != BluetoothDevice.BOND_BONDED && !BluetoothHC.discoveredDevices.contains(device)) {
                BluetoothHC.discoveredDevices.add(device);
                BluetoothHC.discoveredDevicesAdapter.add(device.getName() + "\n" + device.getAddress());
                BluetoothHC.discoveredDevicesAdapter.notifyDataSetChanged();
            }
            return;
        }

        if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
            if (BluetoothHC.discoveredDevices.isEmpty()) {
                if(BluetoothHC.dialog!=null)
                    BluetoothHC.dialog.setTitle(activity.getString(R.string.deviceNotFound));
            }else{
                if(BluetoothHC.dialog!=null)
                    BluetoothHC.dialog.setTitle(activity.getString(R.string.selectADevice));
            }
            return;
        }

        if (action.equals(BluetoothAdapter.ACTION_STATE_CHANGED)) {
            BluetoothAdapter ba = MainActivity.bt.getBluetoothAdapter();
            if (ba.isEnabled()) {
                Log.i("ACTION_STATE_CHANGED", "ON");
                MainActivity.setIcBluetooth(R.id.btnBltActivated);

            } else {
                Log.i("ACTION_STATE_CHANGED", "OFF");
                MainActivity.setIcBluetooth(R.id.btnBltDisabled);
            }
        }
    }
}
