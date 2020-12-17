package de.fgmeier.oscsenderstele;


import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Process;



public class OSCHandlerThreat extends HandlerThread {
    private static final String TAG = "OSCHandlerThreat";
    private Context context;
    private MainActivity activity;
    private Handler sending_handler;
    public OSCHandlerThreat(Context context, MainActivity activity) {
        super("OSCThread", Process.THREAD_PRIORITY_BACKGROUND);
        this.context = context;
        this.activity = activity;
    }

    @Override
    protected void onLooperPrepared() {
        sending_handler = new OSCMsgHandler(this.context, this.activity);
    }

    public Handler getHandler(){
        return sending_handler;
    }
}
