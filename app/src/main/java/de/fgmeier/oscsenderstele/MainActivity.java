package de.fgmeier.oscsenderstele;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.core.content.res.ResourcesCompat;

import java.nio.channels.SeekableByteChannel;
import java.util.Objects;

import static java.lang.Math.abs;
import static java.lang.Math.getExponent;

// Techniker Branch
public class MainActivity extends Activity implements MonitorFragment.MonitorFragmentListener, SettingsFragment.SettingsFragmentListener {
    int t = 0;
    /* These two variables hold the IP address and port number.
     * You should change them to the appropriate address and port.
     */
    private Button firstButton;
    private ToggleButton switchFragmentButton;
    private TextView connectionStatus;
    private ImageView stopngo;

    // This is used to send messages

    private OSCHandlerThreat handlerThreat = new OSCHandlerThreat(this, MainActivity.this);
    private Handler mHandler;
    private int checkNumbers = 1000;
    boolean isconnected = false;
    boolean tryingtoconnect = true;
    private long tStart;
    private long tEnd;
    private long tDelta;
    int x;
    private String maximumPeople = "?";
    private String insidePeople = "?";


    private MonitorFragment monitorFragment;
    private SettingsFragment settingsFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_main);

        monitorFragment = MonitorFragment.newInstance(insidePeople,maximumPeople,isconnected);
        settingsFragment = SettingsFragment.newInstance(insidePeople,maximumPeople);

        Bundle args = new Bundle();

        getFragmentManager().beginTransaction().add(R.id.container, monitorFragment).hide(monitorFragment).add(R.id.container, settingsFragment).hide(settingsFragment).commit();
       // getFragmentManager().beginTransaction().show(monitorFragment).commit();
     //   getFragmentManager().beginTransaction().add(R.id.container, monitorFragment).commit();

       // connectionStatus = findViewById(R.id.ConnectedStatus);
        tStart =  System.currentTimeMillis();
        tEnd = System.currentTimeMillis();
        handlerThreat.start();
        mHandler = new Handler();
        startCheckNumber();
        stopngo = findViewById(R.id.stopngo);
        firstButton = findViewById(R.id.button_connect);

        startConnectingButtonAnimation();
        firstButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // msg = Message.obtain();
                tryingtoconnect = true;
                handlerThreat.getHandler().sendEmptyMessage(1);
                startConnectingButtonAnimation();

            }
        });





        switchFragmentButton = findViewById(R.id.buttonX);
        switchFragmentButton.setEnabled(isconnected);
        showMyFragment();
        switchFragmentButton.setTextOff("");
        switchFragmentButton.setTextOn("");
        switchFragmentButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                showMyFragment();
            }
        });
        setUisTextViews();


    }




    @Override
    protected void onDestroy() {
        super.onDestroy();
        handlerThreat.quit();
        stopCheckNumbers();
    }

    Runnable mCheckNumbers = new Runnable() {
        @Override
        public void run() {
            try {
                handlerThreat.getHandler().sendEmptyMessage(8);
                long mytEnd = tEndtoRunnable();
                tDelta = abs(mytEnd - tStart);
                String tDeltaString = Long.toString(tDelta);
                Log.d("tEnd", "mCheckNumbers: mytEnd  " + Long.toString(mytEnd % 100000));
                Log.d("TIME: ", "DeltaTime: " + tDeltaString);
                if (tDelta < 3 * checkNumbers) {
                    isconnected = true;

                } else {
                   isconnected = false;
                   setUisTextViews();

                }
                monitorFragment.setArgConnected(isconnected);
                tStart = System.currentTimeMillis();
                Log.d("tStart", "mCheckNumbers: tStart " + Long.toString(tStart % 100000) );
            } catch (Exception e) {
                Log.d("CheckNumbers", "run: No Connection ");
            }
            mHandler.postDelayed(mCheckNumbers, checkNumbers);
        }
    };

    Runnable mConnectingButtonUI = new Runnable() {
        @Override
        public void run() {
            switch (x){

                case 1:
                    firstButton.setText("(- CONNECTING -)");
                    stopngo.setImageResource(R.drawable.bar1);
                    break;
                case 2:
                    firstButton.setText("(- (- CONNECTING -) -)");
                    stopngo.setImageResource(R.drawable.bar2);
                    break;
                case 3:
                    firstButton.setText("(- (- (- CONNECTING -) -) -)");
                    stopngo.setImageResource(R.drawable.bar3);
                    break;
                default:
                    Drawable drawable = ResourcesCompat.getDrawable(getResources(), R.drawable.grey_c, null);
                    stopngo.setBackground(drawable);
                    firstButton.setText("CONNECTING");
                    stopngo.setImageResource(R.drawable.bar0);
                    x = 0;

            }
            x++;
            Log.d("X", "X : " + Integer.toString(x));
            mHandler.postDelayed(mConnectingButtonUI,750);
        }
    };

    void startConnectingButtonAnimation(){
        mConnectingButtonUI.run();
    }
    void  stopConnectingButtonAnimation(){
        mHandler.removeCallbacks(mConnectingButtonUI);
    }


    void startCheckNumber() {
        mCheckNumbers.run();
    }

    void stopCheckNumbers() {
        mHandler.removeCallbacks(mCheckNumbers);
    }

    void showMyFragment(){

        if(switchFragmentButton.isChecked()){
            //getFragmentManager().beginTransaction().replace(R.id.container, settingsFragment).commit();

            getFragmentManager().beginTransaction().hide(monitorFragment).commit();
            getFragmentManager().beginTransaction().show(settingsFragment).commit();
        }else{

            getFragmentManager().beginTransaction().hide(settingsFragment).commit();
            getFragmentManager().beginTransaction().show(monitorFragment).commit();
        }
    }

    void setUisTextViews(){
        switchFragmentButton.setEnabled(isconnected);

        if(isconnected) {
            stopConnectingButtonAnimation();


                Log.d("UI", "setUisTextViews: ");
                settingsFragment.setNumbersSettingScreen(insidePeople,maximumPeople);


            int m = Integer.parseInt(maximumPeople);
            int i = Integer.parseInt(insidePeople);
            if (i >= m) {
                stopngo.setImageResource(R.drawable.stop);
                Drawable drawable = ResourcesCompat.getDrawable(getResources(), R.drawable.red_c, null);
                stopngo.setBackground(drawable);

            } else {
                stopngo.setImageResource(R.drawable.go);
                Drawable drawable = ResourcesCompat.getDrawable(getResources(), R.drawable.green_c, null);
                stopngo.setBackground(drawable);
            }

        }else{

            maximumPeople = "?";
            insidePeople = "?";
        }

//        maxText.setText(maximumPeople);
//        insideText.setText(insidePeople);


        if (isconnected) {
            tryingtoconnect = false;
         //   connectionStatus.setText("CONNECTED");
            firstButton.setText("CONNECTED    \u2713");
            monitorFragment.setInside(insidePeople);
        } else {

            Drawable drawable = ResourcesCompat.getDrawable(getResources(), R.drawable.grey_c, null);
            stopngo.setBackground(drawable);

            if (tryingtoconnect){

            }else{
                monitorFragment.setInside(insidePeople);
                stopngo.setImageResource(R.drawable.nothing);
                firstButton.setText("RECONNECT");
                getFragmentManager().beginTransaction().hide(settingsFragment).commit();
                getFragmentManager().beginTransaction().show(monitorFragment).commit();
                switchFragmentButton.setChecked(isconnected);
            }

        }
    }

    public void receivedMsg(String max, String inside) {
        tEnd = System.currentTimeMillis();
        Log.d("tEnd", "receivedMsg: tEnd  " + Long.toString(tEnd % 100000));
        maximumPeople = max;
        insidePeople = inside;
        if(!isconnected){
            mHandler.removeCallbacks(mCheckNumbers);
            mCheckNumbers.run();
        }
        setUisTextViews();
    }

    public long tEndtoRunnable(){
        long tmp = tEnd;
        return tmp;
    }

    @Override
    public void sendPlusOne() {
        handlerThreat.getHandler().sendEmptyMessage(4);
    }

    @Override
    public void sendMinusOne() {
        handlerThreat.getHandler().sendEmptyMessage(5);
    }

    @Override
    public void sendMaxPlusOne() {
        handlerThreat.getHandler().sendEmptyMessage(2);
    }

    @Override
    public void sendMaxMinusOne() {
        handlerThreat.getHandler().sendEmptyMessage(3);
    }

    @Override
    public void sendSetInside(int i) {
        Message msg = Message.obtain();
        msg.what = 7;
        msg.arg1 = i;
        handlerThreat.getHandler().sendMessage(msg);
    }

    @Override
    public void sendSetMaximum(int i) {
        Message msg = Message.obtain();
        msg.what = 6;
        msg.arg1 = i;
        handlerThreat.getHandler().sendMessage(msg);
    }
}