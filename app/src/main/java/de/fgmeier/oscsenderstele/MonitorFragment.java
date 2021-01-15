package de.fgmeier.oscsenderstele;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


public class MonitorFragment extends Fragment {
    private TextView insidepeople;
    private Button ip_Button;
    private Button im_Button;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.monitor_fragment, container, false);

        insidepeople = v.findViewById(R.id.number_inside);
        ip_Button = v.findViewById(R.id.button_plus);
        ip_Button = v.findViewById(R.id.button_minus);



        return  v;
    }
}
