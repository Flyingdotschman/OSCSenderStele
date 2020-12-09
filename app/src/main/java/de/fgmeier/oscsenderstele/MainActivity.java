package de.fgmeier.oscsenderstele;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import java.net.*;
import java.util.*;

import com.illposed.osc.*;
import com.illposed.osc.transport.udp.OSCPortOut;

public class MainActivity extends Activity {
    int t = 0;
    /* These two variables hold the IP address and port number.
     * You should change them to the appropriate address and port.
     */
    private String myIP = "192.168.1.149";
    private int myPort = 9001;


    private Button firstButton;
    // This is used to send messages
    private OSCPortOut oscPortOut;

    class OscSender extends Thread {
        OscSender(){}
        @Override
        public void run() {


            /* The first part of the run() method initializes the OSCPortOut for sending messages.
             *
             * For more advanced apps, where you want to change the address during runtime, you will want
             * to have this section in a different thread, but since we won't be changing addresses here,
             * we only have to initialize the address once.
             */

            try {
                // Connect to some IP address and port
                oscPortOut = new OSCPortOut(InetAddress.getByName(myIP), myPort);
                sleep(5000);
            } catch (UnknownHostException e) {
                // Error handling when your IP isn't found
                Log.d("IP", "IP not found");
                return;
            } catch (Exception e) {
                // Error handling for any other errors
                Log.d("IP", "Something different");

                return;
            }


            /* The second part of the run() method loops infinitely and sends messages every 500
             * milliseconds.
             */
            boolean run_me = true;

            if (oscPortOut != null) {
                // Creating the message
                Object[] thingsToSend = new Object[3];
                thingsToSend[0] = "Hello World";
                thingsToSend[1] = 12345;
                thingsToSend[2] = 1.2345;

                /* The version of JavaOSC from the Maven Repository is slightly different from the one
                 * from the download link on the main website at the time of writing this tutorial.
                 *
                 * The Maven Repository version (used here), takes a Collection, which is why we need
                 * Arrays.asList(thingsToSend).
                 *
                 * If you're using the downloadable version for some reason, you should switch the
                 * commented and uncommented lines for message below
                 */
                OSCMessage message = new OSCMessage("/pipresents/pipresents/core/hallo", Collections.singletonList(t));
                // OSCMessage message = new OSCMessage(myIP, thingsToSend);
                t++;

                /* NOTE: Since this version of JavaOSC uses Collections, we can actually use ArrayLists,
                 * or any other class that implements the Collection interface. The following code is
                 * valid for this version.
                 *
                 * The benefit of using an ArrayList is that you don't have to know how much information
                 * you are sending ahead of time. You can add things to the end of an ArrayList, but not
                 * to an Array.
                 *
                 * If you want to use this code with the downloadable version, you should switch the
                 * commented and uncommented lines for message2
                 */
                ArrayList<Object> moreThingsToSend = new ArrayList<Object>();
                moreThingsToSend.add("Hello World2");
                moreThingsToSend.add(123456);
                moreThingsToSend.add(12.345);

                OSCMessage message2 = new OSCMessage("/pipresents/pipresents/core/hallo", moreThingsToSend);
                //OSCMessage message2 = new OSCMessage(myIP, moreThingsToSend.toArray());

                try {
                    // Send the messages
                    oscPortOut.send(message);
                    //oscPortOut.send(message2);
                    run_me = false;
                    // Pause for half a second
                    sleep(500);
                } catch (Exception e) {
                    // Error handling for some error
                    Log.d("SEND", String.valueOf(e));
                }
            }

        }





    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_main);

        firstButton = findViewById(R.id.button);
        firstButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OscSender thread = new OscSender();
                thread.start();
            }
        });


        // Start the thread that sends messages
        // oscThread.start();
    }


}