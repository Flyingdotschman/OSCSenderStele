package de.fgmeier.oscsenderstele;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.illposed.osc.OSCListener;
import com.illposed.osc.OSCMessage;
import com.illposed.osc.OSCPortIn;
import com.illposed.osc.OSCPortOut;

import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Collections;
import java.util.Date;
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
    public static final int GET_NUMBERS = 8;
    Context context;
    private OSCPortOut oscPortOut;
    private OSCPortIn oscPortIn;

    private String myIP = "192.168.4.1";
    private int myPort = 9001;
    private boolean success = false;

    private Handler updateUi;
    private MainActivity activity;

    private String receivingString = "/counter_info";


    public OSCMsgHandler(Context context, MainActivity activity) {

        this.updateUi = new Handler(Looper.getMainLooper());
        this.context = context;
        this.activity = activity;
        try {
            WifiConfiguration wificonfig = new WifiConfiguration();
            String networkSSID = "OSCStele";
            String networkPASS = "quellbrunnmedium";
            wificonfig.SSID = String.format("\"%s\"", networkSSID);
            wificonfig.preSharedKey = String.format("\"%s\"", networkPASS);
            WifiManager wifiManager = (WifiManager) this.context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            if(!wifiManager.isWifiEnabled()){
                wifiManager.setWifiEnabled(true);
            }
            int netID = wifiManager.addNetwork(wificonfig);
            wifiManager.disconnect();
            if (netID == -1) {
                Log.d(TAG, "handleMessage: Network exist allready");
                int requestCode = 0;
                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(activity,new String[]{
                            Manifest.permission.ACCESS_FINE_LOCATION}, requestCode);

                }

            }
            List<WifiConfiguration> list = wifiManager.getConfiguredNetworks();
            for (WifiConfiguration i : list) {
                Log.d(TAG, String.format("Found :\"%s\"", i.SSID));
                if (i.SSID != null && i.SSID.equals("\"" + networkSSID + "\"")) {
                    netID = i.networkId;
                    Log.d(TAG, "handleMessage: exsisting Wifi setting found");
                    break;
                }
            }

            wifiManager.enableNetwork(netID, true);
            wifiManager.reconnect();
            Log.d(TAG, "handleMessage: WIFI");
            // Connect to some IP address and port
            ConnectivityManager connManager = (ConnectivityManager)  this.context.getApplicationContext().getSystemService(context.CONNECTIVITY_SERVICE);
            NetworkInfo wifiInfo = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            while(!wifiInfo.isConnected()) {
                sleep(50);
                Log.d(TAG, "OSCMsgHandler: Waiting for Connection");
                wifiInfo = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            }
            oscPortOut = new OSCPortOut(InetAddress.getByName(myIP), myPort);
            success = true;
            sleep(500);

        } catch (UnknownHostException e) {
            // Error handling when your IP isn't found
            Log.d("IP", "IP not found");
            return;
        } catch (Exception e) {
            // Error handling for any other errors
            Log.d("IP", String.valueOf(e));

            return;
        }
        if (oscPortOut != null) {

            OSCMessage message = new OSCMessage("/pipresents/pipresents/counter/counter_info", Collections.singletonList(0));
            Log.d(TAG, "OSCMsgHandler: Send Counter_info");

            try {
                // Send the messages
                oscPortOut.send(message);
                //oscPortOut.send(message2);
                Log.d(TAG, "handleMessage: info");
                // currentThread().sleep(500);
            } catch (Exception e) {
                // Error handling for some error
                Log.d("SEND", String.valueOf(e));
                return;
            }
        }
        if (success) {
            try {
                Log.d(TAG, "OSCMsgHandler: Trying Listener");
                oscPortIn = new OSCPortIn(myPort);
                OSCListener listener = new OSCListener() {
                    @Override
                    public void acceptMessage(Date time, OSCMessage message) {
                        Log.d("MESSAGE", "Received: Counter_info");
                        List<Object> list = message.getArguments();
                        String i1, i2;
                        i1 = list.get(0).toString();
                        i2 = list.get(1).toString();
                        Log.d("MESSAGE", "Received: " + i1 + " + " + i2);
                        if(i1.length()<6) {
                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {

                                    activity.receivedMsg(i1, i2);
                                }
                            });
                        }
                    }
                };

                oscPortIn.addListener(receivingString, listener);
                oscPortIn.startListening();
            } catch (SocketException e) {
                Log.d("SOCKET", String.valueOf(e));

            } catch (Exception e) {
                Log.d("LISTENER", String.valueOf(e));
            }
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
                    if(!wifiManager.isWifiEnabled()){
                        wifiManager.setWifiEnabled(true);
                    }
                    int netID = wifiManager.addNetwork(wificonfig);

                    if (netID == -1) {
                        Log.d(TAG, "handleMessage: Network exist allready");
                        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            // TODO: Consider calling
                            //    ActivityCompat#requestPermissions
                            // here to request the missing permissions, and then overriding
                            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                            //                                          int[] grantResults)
                            // to handle the case where the user grants the permission. See the documentation
                            // for ActivityCompat#requestPermissions for more details.
                            return;
                        }
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
                    ConnectivityManager connManager = (ConnectivityManager)  this.context.getApplicationContext().getSystemService(context.CONNECTIVITY_SERVICE);
                    NetworkInfo wifiInfo = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
                    while(!wifiInfo.isConnected()) {
                        sleep(50);
                        Log.d(TAG, "OSCMsgHandler: Waiting for Connection");
                        wifiInfo = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
                    }
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
                if (success) {
                    if (oscPortOut != null) {
                        try {
                            oscPortIn = new OSCPortIn(myPort);
                        } catch (SocketException e) {
                            e.printStackTrace();
                        }
                        if (oscPortIn.isListening()) {
                            Log.d("Listener", "is listening");
                        }
                        OSCMessage message = new OSCMessage("/pipresents/pipresents/counter/counter_info", Collections.singletonList(0));


                        try {
                            // Send the messages
                            oscPortOut.send(message);
                            //oscPortOut.send(message2);
                            Log.d(TAG, "handleMessage: info");
                            // currentThread().sleep(500);
                        } catch (Exception e) {
                            // Error handling for some error
                            Log.d("SEND", String.valueOf(e));
                            return;
                        }
                    }

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

                        OSCMessage message = new OSCMessage("/pipresents/pipresents/counter/reset_max", Collections.singletonList(msg.arg1));
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

                        OSCMessage message = new OSCMessage("/pipresents/pipresents/counter/reset_inside", Collections.singletonList(msg.arg1));
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
            case GET_NUMBERS:
                if (success) {
                    if (oscPortOut != null) {

                        OSCMessage message = new OSCMessage("/pipresents/pipresents/counter/counter_info", Collections.singletonList(0));
                        Log.d(TAG, "handleMessage: get_counters");

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
