package de.fgmeier.oscsenderstele;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

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
    // This is used to send messages

    private OSCHandlerThreat handlerThreat = new OSCHandlerThreat(this);




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_main);

        handlerThreat.start();


        firstButton = findViewById(R.id.button2);
        firstButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // msg = Message.obtain();

                handlerThreat.getHandler().sendEmptyMessage(1);
            }
        });
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
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handlerThreat.quit();
    }
}