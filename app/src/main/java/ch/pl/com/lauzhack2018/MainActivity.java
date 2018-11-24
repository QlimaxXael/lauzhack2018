package ch.pl.com.lauzhack2018;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.epson.moverio.btcontrol.DisplayControl;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    private boolean displayingInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        View view = this.getWindow().getDecorView();
        view.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

        //Changes display distance
        DisplayControl displayControl = new DisplayControl(this);
        displayControl.setDisplayDistance(15);

        //Set up the toolbar
        Toolbar toolbar = findViewById(R.id.mainToolbarId);
        setSupportActionBar(toolbar);

        displayingInfo = true;

        new Thread(() -> {
            while(true) {
                Data data = getData();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        setInfo(data);
                    }
                });
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        ).start();

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

    public Data getData() {
        //JSon query
        String url = "http://www.duggan.ch/~akv_lauzhack/mastercut.php?MachineName=MasterCut";
        JsonQuery jsonQuery = new JsonQuery();
        JSONObject json = null;
        try {
            json = jsonQuery.getJson(url).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (json != null)
            Log.i("Lauzhack", json.toString());

        Data data = null;

        try {
            if (json != null)
                data = new Data(json);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return data;
    }

    private void toggleInfoDisplay() {
        displayingInfo = !displayingInfo;

        if (displayingInfo) {
            (findViewById(R.id.textViewId)).setVisibility(View.VISIBLE);
            (findViewById(R.id.textViewLabelId)).setVisibility(View.VISIBLE);
            (findViewById(R.id.textViewJobName)).setVisibility(View.VISIBLE);
            (findViewById(R.id.textViewLabelJobName)).setVisibility(View.VISIBLE);
            (findViewById(R.id.textViewMachineState)).setVisibility(View.VISIBLE);
            (findViewById(R.id.textViewLabelMachineState)).setVisibility(View.VISIBLE);
            (findViewById(R.id.textViewMachineSpeed)).setVisibility(View.VISIBLE);
            (findViewById(R.id.textViewLabelMachineSpeed)).setVisibility(View.VISIBLE);
            (findViewById(R.id.textViewInputCounter)).setVisibility(View.VISIBLE);
            (findViewById(R.id.textViewLabelInputCounter)).setVisibility(View.VISIBLE);
            (findViewById(R.id.textViewOutputCounter)).setVisibility(View.VISIBLE);
            (findViewById(R.id.textViewLabelOutputCounter)).setVisibility(View.VISIBLE);
            (findViewById(R.id.textViewCuttingForce)).setVisibility(View.VISIBLE);
            (findViewById(R.id.textViewLabelCuttingForce)).setVisibility(View.VISIBLE);
        } else {
            (findViewById(R.id.textViewId)).setVisibility(View.GONE);
            (findViewById(R.id.textViewLabelId)).setVisibility(View.GONE);
            (findViewById(R.id.textViewJobName)).setVisibility(View.GONE);
            (findViewById(R.id.textViewLabelJobName)).setVisibility(View.GONE);
            (findViewById(R.id.textViewMachineState)).setVisibility(View.GONE);
            (findViewById(R.id.textViewLabelMachineState)).setVisibility(View.GONE);
            (findViewById(R.id.textViewMachineSpeed)).setVisibility(View.GONE);
            (findViewById(R.id.textViewLabelMachineSpeed)).setVisibility(View.GONE);
            (findViewById(R.id.textViewInputCounter)).setVisibility(View.GONE);
            (findViewById(R.id.textViewLabelInputCounter)).setVisibility(View.GONE);
            (findViewById(R.id.textViewOutputCounter)).setVisibility(View.GONE);
            (findViewById(R.id.textViewLabelOutputCounter)).setVisibility(View.GONE);
            (findViewById(R.id.textViewCuttingForce)).setVisibility(View.GONE);
            (findViewById(R.id.textViewLabelCuttingForce)).setVisibility(View.GONE);
        }
    }

    void setInfo(Data data) {
        if (data == null) {
            return;
        }

        ((TextView) findViewById(R.id.textViewId)).setText(""+data.getID());
        ((TextView) findViewById(R.id.textViewJobName)).setText(""+data.getJobName());
        ((TextView) findViewById(R.id.textViewMachineState)).setText(""+data.getMachineState());
        ((TextView) findViewById(R.id.textViewMachineSpeed)).setText(""+data.getMachineSpeed());
        ((TextView) findViewById(R.id.textViewInputCounter)).setText(""+data.getInputCounter());
        ((TextView) findViewById(R.id.textViewOutputCounter)).setText(""+data.getOutputCounter());
        ((TextView) findViewById(R.id.textViewCuttingForce)).setText(""+data.getCuttingForce());

        // Setting warnings visibility
        if (data.getUrgentStop()) {
            findViewById(R.id.layout_urgent_stop).setVisibility(View.VISIBLE);
        } else {
            findViewById(R.id.layout_urgent_stop).setVisibility(View.GONE);
        }
        if (data.getNormalStop()) {
            findViewById(R.id.layout_normal_stop).setVisibility(View.VISIBLE);
        } else {
            findViewById(R.id.layout_normal_stop).setVisibility(View.GONE);
        }
        if (data.getOpenProtection()) {
            findViewById(R.id.layout_open).setVisibility(View.VISIBLE);
        } else {
            findViewById(R.id.layout_open).setVisibility(View.GONE);
        }
        if (data.getTechnicalDefect()) {
            findViewById(R.id.layout_defect).setVisibility(View.VISIBLE);
        } else {
            findViewById(R.id.layout_defect).setVisibility(View.GONE);
        }
        if (data.getMachineSpeed() > data.getMachineSpeedMax()){
            findViewById(R.id.layout_speed).setVisibility(View.VISIBLE);
        } else {
            findViewById(R.id.layout_speed).setVisibility(View.GONE);
        }
        if (data.getCuttingForce() > data.getCuttingForceMax()){
            findViewById(R.id.layout_cut).setVisibility(View.VISIBLE);
        } else {
            findViewById(R.id.layout_cut).setVisibility(View.GONE);
        }

        // Setting status visibility
        if (data.getMachineState() == 0) {
            findViewById(R.id.layout_stopped).setVisibility(View.VISIBLE);
            findViewById(R.id.layout_setting).setVisibility(View.GONE);
            findViewById(R.id.layout_running).setVisibility(View.GONE);
            findViewById(R.id.layout_producing).setVisibility(View.GONE);
            findViewById(R.id.layout_shutdown).setVisibility(View.GONE);
        } else if (data.getMachineState() == 1) {
            findViewById(R.id.layout_stopped).setVisibility(View.GONE);
            findViewById(R.id.layout_setting).setVisibility(View.VISIBLE);
            findViewById(R.id.layout_running).setVisibility(View.GONE);
            findViewById(R.id.layout_producing).setVisibility(View.GONE);
            findViewById(R.id.layout_shutdown).setVisibility(View.GONE);
        } else if (data.getMachineState() == 2) {
            findViewById(R.id.layout_stopped).setVisibility(View.GONE);
            findViewById(R.id.layout_setting).setVisibility(View.GONE);
            findViewById(R.id.layout_running).setVisibility(View.VISIBLE);
            findViewById(R.id.layout_producing).setVisibility(View.GONE);
            findViewById(R.id.layout_shutdown).setVisibility(View.GONE);
        } else if (data.getMachineState() == 3) {
            findViewById(R.id.layout_stopped).setVisibility(View.GONE);
            findViewById(R.id.layout_setting).setVisibility(View.GONE);
            findViewById(R.id.layout_running).setVisibility(View.GONE);
            findViewById(R.id.layout_producing).setVisibility(View.VISIBLE);
            findViewById(R.id.layout_shutdown).setVisibility(View.GONE);
        } else {
            findViewById(R.id.layout_stopped).setVisibility(View.GONE);
            findViewById(R.id.layout_setting).setVisibility(View.GONE);
            findViewById(R.id.layout_running).setVisibility(View.GONE);
            findViewById(R.id.layout_producing).setVisibility(View.GONE);
            findViewById(R.id.layout_shutdown).setVisibility(View.VISIBLE);
        }
    }
}
