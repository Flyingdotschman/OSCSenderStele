package de.fgmeier.oscsenderstele;

import android.content.Context;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiEnterpriseConfig;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.util.Log;

import androidx.annotation.NonNull;

import com.illposed.osc.OSCMessage;
import com.illposed.osc.transport.udp.OSCPortOut;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static android.content.ContentValues.TAG;
import static java.lang.Thread.sleep;

public class OSCMsgHandler extends Handler {
    // psfi  public static final int
    public static final int CONNECT_PI = 1;
    public static final int MAX_PLUS = 2;
    public static final int MAX_MINUS = 3;
    public static final int INSIDE_PLUS = 4;
    public static final int INSIDE_MINUS = 5;
    public static final int RESET_MAX = 6;
    public static final int RESET_INSIDE = 7;
    Context context;
    private OSCPortOut oscPortOut;

    private String myIP = "192.168.4.1";
    private int myPort = 9001;
    private boolean success = false;

    public OSCMsgHandler(Context context){
        this.context = context;
        try {
            WifiConfiguration wificonfig = new WifiConfiguration();
            String networkSSID = "OSCStele";
            String networkPASS = "quellbrunnmedium";
            wificonfig.SSID = String.format("\"%s\"", networkSSID);
            wificonfig.preSharedKey = String.format("\"%s\"", networkPASS);
            WifiManager wifiManager = (WifiManager) this.context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);

            int netID = wifiManager.addNetwork(wificonfig);
            wifiManager.disconnect();
            if(netID == -1){
                Log.d(TAG, "handleMessage: Network exist allready");
                List<WifiConfiguration> list = wifiManager.getConfiguredNetworks();
                for(WifiConfiguration i : list){
                    Log.d(TAG, String.format("Found :\"%s\"", i.SSID));
                    if(i.SSID != null && i.SSID.equals("\""+ networkSSID + "\"")){
                        netID = i.networkId;
                        Log.d(TAG, "handleMessage: exsisting Wifi setting found");
                        break;
                    }
                }
            }
            wifiManager.disconnect();
            wifiManager.enableNetwork(netID,true);
            wifiManager.reconnect();
            Log.d(TAG, "handleMessage: WIFI");
            // Connect to some IP address and port
            sleep(500);
            oscPortOut = new OSCPortOut(InetAddress.getByName(myIP), myPort);
            success = true;
        } catch (UnknownHostException e) {
            // Error handling when your IP isn't found
            Log.d("IP", "IP not found");
            return;
        } catch (Exception e) {
            // Error handling for any other errors
            Log.d("IP", String.valueOf(e));

            return;
        }
    }

    @Override
    public void handleMessage(@NonNull Message msg) {
        switch (msg.what) {
            case CONNECT_PI:

                try {
                    WifiConfiguration wificonfig = new WifiConfiguration();
                    String networkSSID = "OSCStele";
                    String networkPASS = "quellbrunnmedium";
                    wificonfig.SSID = String.format("\"%s\"", networkSSID);
                    wificonfig.preSharedKey = String.format("\"%s\"", networkPASS);
                    WifiManager wifiManager = (WifiManager) this.context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);

                    int netID = wifiManager.addNetwork(wificonfig);
                    wifiManager.disconnect();
                    if(netID == -1){
                        Log.d(TAG, "handleMessage: Network exist allready");
                        List<WifiConfiguration> list = wifiManager.getConfiguredNetworks();
                        for(WifiConfiguration i : list){
                            Log.d(TAG, String.format("Found :\"%s\"", i.SSID));
                            if(i.SSID != null && i.SSID.equals("\""+ networkSSID + "\"")){
                                netID = i.networkId;
                                Log.d(TAG, "handleMessage: exsisting Wifi setting found");
                                break;
                            }
                        }
                    }
                    wifiManager.disconnect();
                    wifiManager.enableNetwork(netID,true);
                    wifiManager.reconnect();
                    Log.d(TAG, "handleMessage: WIFI");
                    // Connect to some IP address and port
                    sleep(500);
                    oscPortOut = new OSCPortOut(InetAddress.getByName(myIP), myPort);
                    success = true;
                } catch (UnknownHostException e) {
                    // Error handling when your IP isn't found
                    Log.d("IP", "IP not found");
                    return;
                } catch (Exception e) {
                    // Error handling for any other errors
                    Log.d("IP", String.valueOf(e));

                    return;
                }
                break;
            case MAX_PLUS:
                if (success) {
                    if (oscPortOut != null) {

                        OSCMessage message = new OSCMessage("/pipresents/pipresents/counter/max_plus", Collections.singletonList(0));
                        Log.d(TAG, "handleMessage: max_plus");

                        try {
                            // Send the messages
                            oscPortOut.send(message);
                            //oscPortOut.send(message2);

                            // Pause for half a second

                        } catch (Exception e) {
                            // Error handling for some error
                            Log.d("SEND", String.valueOf(e));
                            return;
                        }
                    }

                }
               break;
            case MAX_MINUS:
                if (success) {
                    if (oscPortOut != null) {

                        OSCMessage message = new OSCMessage("/pipresents/pipresents/counter/max_minus", Collections.singletonList(1));
                        Log.d(TAG, "handleMessage: max_minus");

                        try {
                            // Send the messages
                            oscPortOut.send(message);
                            //oscPortOut.send(message2);

                            // Pause for half a second

                        } catch (Exception e) {
                            // Error handling for some error
                            Log.d("SEND", String.valueOf(e));
                            return;
                        }
                    }
                }
               break;
            case INSIDE_PLUS:
                if (success) {
                    if (oscPortOut != null) {

                        OSCMessage message = new OSCMessage("/pipresents/pipresents/counter/inside_plus", Collections.singletonList(2));
                        Log.d(TAG, "handleMessage: inside_plus");

                        try {
                            // Send the messages
                            oscPortOut.send(message);
                            //oscPortOut.send(message2);

                            // Pause for half a second

                        } catch (Exception e) {
                            // Error handling for some error
                            Log.d("SEND", String.valueOf(e));
                            return;
                        }
                    }
                }
                break;
            case INSIDE_MINUS:
                if (success) {
                    if (oscPortOut != null) {

                        OSCMessage message = new OSCMessage("/pipresents/pipresents/counter/inside_minus", Collections.singletonList(6));
                        Log.d(TAG, "handleMessage: inside_minus");

                        try {
                            // Send the messages
                            oscPortOut.send(message);
                            //oscPortOut.send(message2);

                            // Pause for half a second

                        } catch (Exception e) {
                            // Error handling for some error
                            Log.d("SEND", String.valueOf(e));
                            return;
                        }
                    }
                }
            break;
            case RESET_MAX:
                if (success) {
                    if (oscPortOut != null) {

                        OSCMessage message = new OSCMessage("/pipresents/pipresents/counter/reset_max", Collections.singletonList(6));
                        Log.d(TAG, "handleMessage: reset_max");

                        try {
                            // Send the messages
                            oscPortOut.send(message);
                            //oscPortOut.send(message2);

                            // Pause for half a second

                        } catch (Exception e) {
                            // Error handling for some error
                            Log.d("SEND", String.valueOf(e));
                            return;
                        }
                    }
                }
                break;
            case RESET_INSIDE:
                if (success) {
                    if (oscPortOut != null) {

                        OSCMessage message = new OSCMessage("/pipresents/pipresents/counter/reset_inside", Collections.singletonList(6));
                        Log.d(TAG, "handleMessage: reset_inside");

                        try {
                            // Send the messages
                            oscPortOut.send(message);
                            //oscPortOut.send(message2);

                            // Pause for half a second

                        } catch (Exception e) {
                            // Error handling for some error
                            Log.d("SEND", String.valueOf(e));
                            return;
                        }
                    }
                }
                break;
        }
    }
}
