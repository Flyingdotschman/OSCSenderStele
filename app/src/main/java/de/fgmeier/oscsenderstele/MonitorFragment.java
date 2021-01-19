package de.fgmeier.oscsenderstele;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


public class MonitorFragment extends Fragment {

    public static final String ARG_INSIDE = "argInside";
    public static final String ARG_MAXIMUS = "argMaximus";
    public static final String ARG_CONNECTED = "argConnected";

    private String inside;
    private String maxpeople;

    private TextView insidepeople;
    private ImageButton ip_Button;
    private ImageButton im_Button;

    private boolean isconnected;

    private MonitorFragmentListener listener;

    public interface MonitorFragmentListener{
            void sendPlusOne();
            void sendMinusOne();
    }

    public static MonitorFragment newInstance(String inside, String maxpeople, boolean connection){
        MonitorFragment fragment = new MonitorFragment();
        Bundle args = new Bundle();
        args.putString(ARG_INSIDE,inside);
        args.putString(ARG_MAXIMUS,maxpeople);
        args.putBoolean(ARG_CONNECTED,connection);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.monitor_fragment, container, false);

        if(getArguments() != null){
            inside = getArguments().getString(ARG_INSIDE);
            isconnected = getArguments().getBoolean(ARG_CONNECTED);
        }

        insidepeople = v.findViewById(R.id.number_inside);
        insidepeople.setText(inside);
        ip_Button = v.findViewById(R.id.button_plus);
        ip_Button.setEnabled(isconnected);
        ip_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.sendPlusOne();

            }
        });

        im_Button = v.findViewById(R.id.button_minus);
        im_Button.setEnabled(isconnected);
        im_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               listener.sendMinusOne();
            }
        });



        return  v;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof MonitorFragmentListener) {
            listener = (MonitorFragmentListener) context;
        }else{
            throw new RuntimeException(context.toString() + "Must implement MonitorFragmentListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    public void setInside(String i ){
        insidepeople.setText(i);
    }

    public void setArgConnected(boolean b){isconnected = b;
        im_Button.setEnabled(isconnected);
        ip_Button.setEnabled(isconnected);

    }

}
