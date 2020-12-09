package de.fgmeier.oscsenderstele;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import androidx.annotation.NonNull;

import com.illposed.osc.OSCMessage;
import com.illposed.osc.transport.udp.OSCPortOut;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;

public class OSCMsgHandler extends Handler {
// psfi  public static final int
    public static final int CONNECT_PI = 1;
    public static final int SEND_PI = 2;

    private OSCPortOut oscPortOut;

    private String myIP = "192.168.1.149";
    private int myPort = 9001;
    private boolean success = false;
    @Override
    public void handleMessage(@NonNull Message msg) {
        switch (msg.what){
            case CONNECT_PI:

                try {
                    // Connect to some IP address and port
                    oscPortOut = new OSCPortOut(InetAddress.getByName(myIP), myPort);
                    success = true;
                } catch (UnknownHostException e) {
                    // Error handling when your IP isn't found
                    Log.d("IP", "IP not found");
                    return;
                } catch (Exception e) {
                    // Error handling for any other errors
                    Log.d("IP", "Something different");

                    return;
                }
                break;
            case SEND_PI:
                if(success){
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
                        OSCMessage message = new OSCMessage("/pipresents/pipresents/core/hallo", Collections.singletonList(0));
                        // OSCMessage message = new OSCMessage(myIP, thingsToSend);


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

                            // Pause for half a second

                        } catch (Exception e) {
                            // Error handling for some error
                            Log.d("SEND", String.valueOf(e));
                        }
                    }
                }

        }
    }
}
