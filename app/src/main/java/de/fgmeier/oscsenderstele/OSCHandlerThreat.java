package de.fgmeier.oscsenderstele;


import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.os.Process;

import androidx.annotation.NonNull;

public class OSCHandlerThreat extends HandlerThread {
    private static final String TAG = "OSCHandlerThreat";

    private Handler handler;
    public OSCHandlerThreat() {
        super("OSCThread", Process.THREAD_PRIORITY_BACKGROUND);

    }

    @Override
    protected void onLooperPrepared() {
        handler = new OSCMsgHandler();
    }

    public Handler getHandler(){
        return handler;
    }
}
