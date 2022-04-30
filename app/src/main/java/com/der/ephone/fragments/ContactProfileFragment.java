package com.der.ephone.fragments;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
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
import com.der.ephone.model.Contact;

public class ContactProfileFragment extends Fragment {

    TextView contactName, contactNumber;
    ImageView phCall, phMessage;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contact_profile, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        contactName = view.findViewById(R.id.profile_contact_name);
        contactNumber = view.findViewById(R.id.profile_contact_number);
        phCall = view.findViewById(R.id.profile_contact_ph_call);
        phMessage = view.findViewById(R.id.profile_contact_ph_msg);

        String name = getArguments().getString("name");
        String number = getArguments().getString("number");
        contactName.setText(name);
        contactNumber.setText(number);

        phCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                makePhoneCall(number);
            }
        });

    }



    private void makePhoneCall(String number){
        String dial = "tel:" + number;
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED){
            return;
        }
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse(dial));
        getContext().startActivity(intent);
    }

}