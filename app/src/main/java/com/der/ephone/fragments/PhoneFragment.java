package com.der.ephone.fragments;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.der.ephone.R;
import com.der.ephone.adapter.CallLogAdapter;
import com.der.ephone.adapter.OnPhoneCallClickListener;
import com.der.ephone.model.CallLog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PhoneFragment extends Fragment implements OnPhoneCallClickListener {

    DialedFragment dialedFragment;
    CallLogAdapter adapter;
    RecyclerView recyclerView;
    List<CallLog> callLogList;

    public PhoneFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        callLogList = new ArrayList<>();
        callLogList = getCallLog();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_phone, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.recycler_view);

        adapter = new CallLogAdapter(callLogList, getContext(), this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        dialedFragment = new DialedFragment();
        FloatingActionButton fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showBottomSheetDialog();
            }
        });
    }

    private void showBottomSheetDialog() {
        dialedFragment.show(getActivity().getSupportFragmentManager(), dialedFragment.getTag());
    }

    public List<CallLog> getCallLog(){

        List<CallLog> calls = new ArrayList<>();

        if ( ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_CALL_LOG}, 1);
        }

        //todo : calllog do not show the lastest call

        Cursor cursor = getActivity().getContentResolver().query(android.provider.CallLog.Calls.CONTENT_URI, null, null, null, android.provider.CallLog.Calls.DATE + " DESC");
        int number = cursor.getColumnIndex(android.provider.CallLog.Calls.NUMBER);
        int name = cursor.getColumnIndex(android.provider.CallLog.Calls.CACHED_NAME);
        int duration = cursor.getColumnIndex(android.provider.CallLog.Calls.DURATION);
        int date = cursor.getColumnIndex(android.provider.CallLog.Calls.DATE);
        String direction = null;

        //cursor.moveToFirst();
        while (cursor.moveToNext()){
            Date callDate = new Date(Long.valueOf(cursor.getString(date)));
            String formatDate = formatDate(callDate);

            CallLog callLog = new CallLog();
            callLog.setName(cursor.getString(name) == null ? cursor.getString(number) : cursor.getString(name));
            callLog.setNumber(cursor.getString(number));
            callLog.setDuration(cursor.getString(duration));
            callLog.setDate(formatDate);
            calls.add(callLog);
        }
        return calls;

    }

    private String formatDate(Date d){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat();
        simpleDateFormat.applyPattern("dd/MMM/yyyy");
        String date = simpleDateFormat.format(d.getTime());
        return date;
    }

    @Override
    public void onClick(CallLog callLog) {
        makePhoneCall(callLog);
    }

    private void makePhoneCall(CallLog callLog){
        String dial = "tel:" + callLog.getNumber();
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED){
            return;
        }
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse(dial));
        getContext().startActivity(intent);


    }
}