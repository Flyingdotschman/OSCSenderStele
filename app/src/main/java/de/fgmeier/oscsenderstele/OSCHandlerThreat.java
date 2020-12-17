package de.fgmeier.oscsenderstele;


import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Process;



public class OSCHandlerThreat extends HandlerThread {
    private static final String TAG = "OSCHandlerThreat";
    Context context;
    private Handler sending_handler;
    public OSCHandlerThreat(Context context) {
        super("OSCThread", Process.THREAD_PRIORITY_BACKGROUND);
        this.context = context;
    }

    @Override
    protected void onLooperPrepared() {
        sending_handler = new OSCMsgHandler(this.context);
    }

    public Handler getHandler(){
        return sending_handler;
    }
}
