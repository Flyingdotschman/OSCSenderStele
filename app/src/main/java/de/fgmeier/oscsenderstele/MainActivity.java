package de.fgmeier.oscsenderstele;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import static java.lang.Math.abs;


public class MainActivity extends Activity {
    int t = 0;
    /* These two variables hold the IP address and port number.
     * You should change them to the appropriate address and port.
     */
    private Button firstButton;
    private Button mp_Button;
    private Button mm_Button;
    private Button rm_Button;
    private Button ip_Button;
    private Button im_Button;
    private Button ri_Button;
    private TextView maxText;
    private TextView insideText;
    private TextView connectionStatus;
    private ImageView stopngo;

    // This is used to send messages

    private OSCHandlerThreat handlerThreat = new OSCHandlerThreat(this, MainActivity.this);
    private Handler mHandler;
    private int checkNumbers = 1000;
    boolean isconnected = false;
    private long tStart;
    private long tEnd;
    private long tDelta;

    private String maximumPeople;
    private String insidePeople;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_main);

        connectionStatus = findViewById(R.id.ConnectedStatus);

        tEnd = System.currentTimeMillis();
        handlerThreat.start();
        mHandler = new Handler();
        startCheckNumber();

        firstButton = findViewById(R.id.button_connect);
        firstButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // msg = Message.obtain();

                handlerThreat.getHandler().sendEmptyMessage(1);
            }
        });
        /*
        mp_Button = findViewById(R.id.button_mp);
        mp_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handlerThreat.getHandler().sendEmptyMessage(2);
            }
        });

        mm_Button = findViewById(R.id.button_mm);
        mm_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handlerThreat.getHandler().sendEmptyMessage(3);
            }
        });
*/
        ip_Button = findViewById(R.id.button_ip);
        ip_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handlerThreat.getHandler().sendEmptyMessage(4);

            }
        });
        im_Button = findViewById(R.id.button_im);
        im_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handlerThreat.getHandler().sendEmptyMessage(5);
            }
        });
        /*
        rm_Button = findViewById(R.id.button_rm);
        rm_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handlerThreat.getHandler().sendEmptyMessage(6);
            }
        });

        ri_Button = findViewById(R.id.button_ri);
        ri_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handlerThreat.getHandler().sendEmptyMessage(7);
            }
        });
*/
        maxText = findViewById(R.id.textView_max);
        insideText = findViewById(R.id.textView_inside);
        stopngo = findViewById(R.id.stopngo);
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
                tDelta = abs(tEnd - tStart);
                String tDeltaString = Long.toString(tDelta);
                Log.d("TIME: ", "DeltaTime: " + tDeltaString);
                if (tDelta < 3 * checkNumbers) {
                    isconnected = true;

                } else {
                   isconnected = false;
                   setUisTextViews();

                }
                tStart = System.currentTimeMillis();
            } catch (Exception e) {
                Log.d("CheckNumbers", "run: No Connection ");
            }
            mHandler.postDelayed(mCheckNumbers, checkNumbers);
        }
    };

    void startCheckNumber() {
        mCheckNumbers.run();
    }

    void stopCheckNumbers() {
        mHandler.removeCallbacks(mCheckNumbers);
    }

    void setUisTextViews(){
        if(isconnected) {
            int m = Integer.parseInt(maximumPeople);
            int i = Integer.parseInt(insidePeople);
            if (i >= m) {
                stopngo.setImageResource(R.drawable.red_c);
            } else {
                stopngo.setImageResource(R.drawable.green_c);
            }
        }else{
            stopngo.setImageResource(R.drawable.grey_c);
            maximumPeople = "?";
            insidePeople = "?";
        }

        maxText.setText(maximumPeople);
        insideText.setText(insidePeople);

        if (isconnected) {
            connectionStatus.setText("CONNECTED");
        } else {
            connectionStatus.setText("DISCONNECTED");
        }
    }

    public void receivedMsg(String max, String inside) {
        tEnd = System.currentTimeMillis();
        maximumPeople = max;
        insidePeople = inside;
        tEnd = System.currentTimeMillis();
        setUisTextViews();


    }

}