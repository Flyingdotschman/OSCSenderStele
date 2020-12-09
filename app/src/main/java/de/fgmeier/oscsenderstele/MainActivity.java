package de.fgmeier.oscsenderstele;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity {
    int t = 0;
    /* These two variables hold the IP address and port number.
     * You should change them to the appropriate address and port.
     */
    private Button firstButton;
    private Button secondButton;
    // This is used to send messages

    private OSCHandlerThreat handlerThreat = new OSCHandlerThreat();




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
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
        secondButton = findViewById(R.id.button1);
        secondButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handlerThreat.getHandler().sendEmptyMessage(2);
            }
        });



    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handlerThreat.quit();
    }
}