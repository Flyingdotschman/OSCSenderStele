package de.fgmeier.oscsenderstele;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


public class SettingsFragment extends Fragment {

    private TextView insideView;
    private TextView maxView;
    private Button ip_Button;
    private Button im_Button;
    private Button mp_Button;
    private Button mm_Button;
    private Button maxSendButton;
    private Button insideSendButton;
    private EditText insideSettingTextField;
    private EditText maxSettingTextField;


    public static final String ARG_INSIDE = "argInside";
    public static final String ARG_MAXIMUS = "argMaximus";

    private String inside;
    private String maxpeople;

    private SettingsFragmentListener listener;

    public interface SettingsFragmentListener{
        void sendPlusOne();
        void sendMinusOne();
        void sendMaxPlusOne();
        void sendMaxMinusOne();
        void sendSetInside(int i);
        void sendSetMaximum(int i);
    }
    public static SettingsFragment newInstance(String inside, String maxpeople){
        SettingsFragment fragment = new SettingsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_INSIDE,inside);
        args.putString(ARG_MAXIMUS,maxpeople);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.settings_fragment, container, false);

        insideView = v.findViewById(R.id.insidepeopleTextView);
        maxView = v.findViewById(R.id.maxpoepleTextView);

        if(getArguments() != null){
            inside = "Aktuelle Besucher: " + getArguments().getString(ARG_INSIDE);
            maxpeople = "Maximale Besucher: " + getArguments().getString(ARG_MAXIMUS);
            insideView.setText(inside);
            maxView.setText(maxpeople);
        }
        ip_Button = v.findViewById(R.id.button_ip);
        ip_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.sendPlusOne();

            }
        });

        im_Button = v.findViewById(R.id.button_im);
        im_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.sendMinusOne();
            }
        });

        mp_Button = v.findViewById(R.id.button_mp);
        mp_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.sendMaxPlusOne();
            }
        });

        mm_Button = v.findViewById(R.id.button_mm);
        mm_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.sendMaxMinusOne();
            }
        });

        insideSettingTextField = v.findViewById(R.id.insideTextField);
        insideSendButton = v.findViewById(R.id.sendSetInside);

        String visitors = "Setze Besucher auf:\n";
        String insidehint= "Zahl eingeben oder\nauf 0 setzen";
        insideSendButton.setText(visitors + insidehint);
        insideSettingTextField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length()>0){
                    insideSendButton.setText(visitors + s.toString());
                }else{
                    insideSendButton.setText(visitors + insidehint);
                }

            }
        });
        insideSendButton = v.findViewById(R.id.sendSetInside);
        insideSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String p =insideSettingTextField.getText().toString();
                Log.d("READ INSIDE", "onClick: " + p);
                if(p.length()>0){

                            listener.sendSetInside(Integer.parseInt(p));

                }else{
                    listener.sendSetInside(0);

                }

            }
        });

        String maxvisotors = "Setze\n maximale Besucher auf:\n";
        String maxhint = "Zahl eingben";
        maxSettingTextField = v.findViewById(R.id.maxTextField);

        maxSendButton = v.findViewById(R.id.sendMaxButton);
        maxSendButton.setText(maxvisotors + maxhint);

        //maxSettingTextField.setHint(maxhint);
        maxSettingTextField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.toString().length()>0){
                    maxSendButton.setText(maxvisotors + s.toString());
                }else{
                    maxSendButton.setText(maxvisotors+maxhint);
                }

            }
        });
        maxSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String p = maxSettingTextField.getText().toString();
                if(p.length()>0){
                    listener.sendSetMaximum(Integer.parseInt(p));
                    maxSettingTextField.setText(null);

                }else{
                   // listener.sendSetMaximum(Integer.parseInt(maxSettingTextField.getHint().toString()));
                }
            }
        });

        return v;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof SettingsFragment.SettingsFragmentListener) {
            listener = (SettingsFragment.SettingsFragmentListener) context;
        }else{
            throw new RuntimeException(context.toString() + "Must implement SettingsFragmentListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }
    public void setNumbersSettingScreen(String inside, String maxpeople){
        inside = "Aktuelle Besucher: <b>" + inside + "</b>";
        maxpeople = "Maximale Besucher: <b>" + maxpeople + "</b>";
        insideView.setText(Html.fromHtml(inside));
        maxView.setText(Html.fromHtml(maxpeople));
    }
}
