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

    private String myIP = "192.168.178.98";
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

                        OSCMessage message = new OSCMessage("/pipresents/pipresents/core/hallo", Collections.singletonList(0));



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
