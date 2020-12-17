package de.fgmeier.oscsenderstele;


import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.os.Process;

import androidx.annotation.NonNull;

public class OSCHandlerThreat extends HandlerThread {
    private static final String TAG = "OSCHandlerThreat";
    Context context;
    private Handler handler;
    public OSCHandlerThreat(Context context) {
        super("OSCThread", Process.THREAD_PRIORITY_BACKGROUND);
        this.context = context;
    }

    @Override
    protected void onLooperPrepared() {
        handler = new OSCMsgHandler(this.context);
    }

    public Handler getHandler(){
        return handler;
    }
}
