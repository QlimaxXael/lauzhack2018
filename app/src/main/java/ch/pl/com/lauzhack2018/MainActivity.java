/*
 * Copyright(C) Seiko Epson Corporation 2016. All rights reserved.
 *
 * Warranty Disclaimers.
 * You acknowledge and agree that the use of the software is at your own risk.
 * The software is provided "as is" and without any warranty of any kind.
 * Epson and its licensors do not and cannot warrant the performance or results
 * you may obtain by using the software.
 * Epson and its licensors make no warranties, express or implied, as to non-infringement,
 * merchantability or fitness for any particular purpose.
 */

package ch.pl.com.lauzhack2018;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;

import com.epson.moverio.btcontrol.DisplayControl;

import ch.pl.com.lauzhack2018.R;

public class MainActivity extends AppCompatActivity {

    private Handler mHandler;
    private boolean displayingInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mHandler = new Handler(Looper.getMainLooper());

        IntentFilter intentFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent intent = this.registerReceiver(mBroadcastReceiver, intentFilter);

        setBatteryInfo(intent);

        View view = this.getWindow().getDecorView();
        view.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

        //Changes display distance
        DisplayControl displayControl = new DisplayControl(this);
        displayControl.setDisplayDistance(15);

        //Set up the toolbar
        Toolbar toolbar = findViewById(R.id.mainToolbarId);
        setSupportActionBar(toolbar);

        displayingInfo = false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_bar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.action_info:
                toggleInfoDisplay();
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            update(intent);
        }
    };

    private void update(final Intent intent) {
        if (mHandler != null) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    setBatteryInfo(intent);
                }
            });
        }
    }

    private void toggleInfoDisplay() {
        displayingInfo = !displayingInfo;

        if (displayingInfo) {
            (findViewById(R.id.textViewStatus)).setVisibility(View.VISIBLE);
            (findViewById(R.id.textViewLabelStatus)).setVisibility(View.VISIBLE);
            (findViewById(R.id.textViewPresent)).setVisibility(View.VISIBLE);
            (findViewById(R.id.textViewLabelPresent)).setVisibility(View.VISIBLE);
            (findViewById(R.id.textViewLevel)).setVisibility(View.VISIBLE);
            (findViewById(R.id.textViewLabelLevel)).setVisibility(View.VISIBLE);
            (findViewById(R.id.textViewPlugged)).setVisibility(View.VISIBLE);
            (findViewById(R.id.textViewLabelPlugged)).setVisibility(View.VISIBLE);
            (findViewById(R.id.textViewVoltage)).setVisibility(View.VISIBLE);
            (findViewById(R.id.textViewLabelVoltage)).setVisibility(View.VISIBLE);
            (findViewById(R.id.textViewTemperature)).setVisibility(View.VISIBLE);
            (findViewById(R.id.textViewLabelTemperature)).setVisibility(View.VISIBLE);
        } else {
            (findViewById(R.id.textViewStatus)).setVisibility(View.GONE);
            (findViewById(R.id.textViewLabelStatus)).setVisibility(View.GONE);
            (findViewById(R.id.textViewPresent)).setVisibility(View.GONE);
            (findViewById(R.id.textViewLabelPresent)).setVisibility(View.GONE);
            (findViewById(R.id.textViewLevel)).setVisibility(View.GONE);
            (findViewById(R.id.textViewLabelLevel)).setVisibility(View.GONE);
            (findViewById(R.id.textViewPlugged)).setVisibility(View.GONE);
            (findViewById(R.id.textViewLabelPlugged)).setVisibility(View.GONE);
            (findViewById(R.id.textViewVoltage)).setVisibility(View.GONE);
            (findViewById(R.id.textViewLabelVoltage)).setVisibility(View.GONE);
            (findViewById(R.id.textViewTemperature)).setVisibility(View.GONE);
            (findViewById(R.id.textViewLabelTemperature)).setVisibility(View.GONE);
        }
    }

    void setBatteryInfo(Intent intent) {
        if (intent != null) {
            ((TextView) findViewById(R.id.textViewStatus)).setText(getBatteryStatus(intent));
            ((TextView) findViewById(R.id.textViewPresent)).setText(getBatteryPresent(intent));
            ((TextView) findViewById(R.id.textViewLevel)).setText(getBatteryLevel(intent));
            ((TextView) findViewById(R.id.textViewPlugged)).setText(getBatteryPlugged(intent));
            ((TextView) findViewById(R.id.textViewVoltage)).setText(getBatteryVoltage(intent));
            ((TextView) findViewById(R.id.textViewTemperature)).setText(getBatteryTemperature(intent));
        }
    }

    String getBatteryStatus(@NonNull Intent intent) {
        String status = "-";
        int value = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
        switch (value) {
            case BatteryManager.BATTERY_STATUS_CHARGING:
                status = "CHARGING";
                break;
            case BatteryManager.BATTERY_STATUS_DISCHARGING:
                status = "DISCHARGING";
                break;
            case BatteryManager.BATTERY_STATUS_FULL:
                status = "FULL";
                break;
            case BatteryManager.BATTERY_STATUS_NOT_CHARGING:
                status = "NOT CHARGING";
                break;
            case BatteryManager.BATTERY_STATUS_UNKNOWN:
                status = "UNKNOWN";
                break;
        }
        return status;
    }

    String getBatteryPresent(@NonNull Intent intent) {
        String present;
        boolean isPresent = intent.getBooleanExtra(BatteryManager.EXTRA_PRESENT, false);
        if (isPresent) {
            present = "PRESENT";
        } else {
            present = "NOT PRESENT";
        }
        return present;
    }

    String getBatteryLevel(@NonNull Intent intent) {
        int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        return String.valueOf(level) + "  [%]";
    }

    String getBatteryPlugged(@NonNull Intent intent) {
        String plugged = "-";
        int value = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
        switch (value) {
            case BatteryManager.BATTERY_PLUGGED_AC:
                plugged = "PLUGGED AC";
                break;
            case BatteryManager.BATTERY_PLUGGED_USB:
                plugged = "PLUGGED USB";
                break;
            case BatteryManager.BATTERY_PLUGGED_WIRELESS:
                plugged = "PLUGGED WIRELESS";
                break;
        }

        return plugged;
    }

    String getBatteryVoltage(@NonNull Intent intent) {
        int voltage = intent.getIntExtra(BatteryManager.EXTRA_VOLTAGE, -1);
        return String.valueOf(voltage) + "  [mV]";
    }

    String getBatteryTemperature(@NonNull Intent intent) {
        int temperature = intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, -1);
        float temp = temperature / 10;
        return String.valueOf(temp) + "  [degC]";
    }
}
