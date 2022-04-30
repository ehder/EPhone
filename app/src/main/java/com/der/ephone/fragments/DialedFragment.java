package com.der.ephone.fragments;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.der.ephone.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class DialedFragment extends BottomSheetDialogFragment {

    ImageView btn_1, btn_2, btn_3, btn_4, btn_5, btn_6, btn_7, btn_8, btn_9, btn_0,
            btn_star, btn_hash, phone_call, back;
    TextView textView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_dialpad, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        idInit(view);
        numberDial();
    }

    private void numberDial(){
        btn_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textView.setText(textView.getText() + "1");
            }
        });

        btn_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textView.setText(textView.getText() + "2");
            }
        });

        btn_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textView.setText(textView.getText() + "3");
            }
        });

        btn_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textView.setText(textView.getText() + "4");
            }
        });

        btn_5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textView.setText(textView.getText() + "5");
            }
        });

        btn_6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textView.setText(textView.getText() + "6");
            }
        });

        btn_7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textView.setText(textView.getText() + "7");
            }
        });

        btn_8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textView.setText(textView.getText() + "8");
            }
        });

        btn_9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textView.setText(textView.getText() + "9");
            }
        });

        btn_0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textView.setText(textView.getText() + "0");
            }
        });

        btn_star.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textView.setText(textView.getText() + "*");
            }
        });

        btn_hash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textView.setText(textView.getText() + "#");
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (textView.length() > 0){
                    CharSequence num = textView.getText();
                    StringBuffer deleteNum = new StringBuffer(num);
                    deleteNum.deleteCharAt(textView.length()-1);
                    textView.setText(deleteNum);
                }
            }
        });

        phone_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                makePhoneCall();
            }
        });
    }

    public void makePhoneCall(){
        String num = textView.getText().toString();
        String dial = "tel:" + num;

        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED){
            return;
        }

        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse(dial));
        startActivity(intent);
    }

    public void idInit(View view){
        btn_0 = view.findViewById(R.id.btn_0);
        btn_1 = view.findViewById(R.id.btn_1);
        btn_2 = view.findViewById(R.id.btn_2);
        btn_3 = view.findViewById(R.id.btn_3);
        btn_4 = view.findViewById(R.id.btn_4);
        btn_5 = view.findViewById(R.id.btn_5);
        btn_6 = view.findViewById(R.id.btn_6);
        btn_7 = view.findViewById(R.id.btn_7);
        btn_8 = view.findViewById(R.id.btn_8);
        btn_9 = view.findViewById(R.id.btn_9);
        btn_star = view.findViewById(R.id.btn_star);
        btn_hash = view.findViewById(R.id.btn_hash);
        phone_call = view.findViewById(R.id.phone_call);
        textView = view.findViewById(R.id.text_phone);
        back = view.findViewById(R.id.back);

    }
}