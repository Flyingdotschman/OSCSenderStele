package de.fgmeier.oscsenderstele;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


public class MonitorFragment extends Fragment {

    public static final String ARG_INSIDE = "argInside";
    public static final String ARG_MAXIMUS = "argMaximus";

    private String inside;
    private String maxpeople;

    private TextView insidepeople;
    private Button ip_Button;
    private Button im_Button;

    public static MonitorFragment newInstance(String inside, String maxpeople){
        MonitorFragment fragment = new MonitorFragment();
        Bundle args = new Bundle();
        args.putString(ARG_INSIDE,inside);
        args.putString(ARG_MAXIMUS,maxpeople);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.monitor_fragment, container, false);

        if(getArguments() != null){
            inside = getArguments().getString(ARG_INSIDE);
        }

        insidepeople = v.findViewById(R.id.number_inside);
        insidepeople.setText(inside);
        ip_Button = v.findViewById(R.id.button_plus);
        ip_Button = v.findViewById(R.id.button_minus);



        return  v;
    }
}
