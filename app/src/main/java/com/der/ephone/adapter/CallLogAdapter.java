package com.der.ephone.adapter;


import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.der.ephone.R;
import com.der.ephone.model.CallLog;

import java.util.List;

public class CallLogAdapter extends RecyclerView.Adapter<CallLogAdapter.ViewHolder> {

    List<CallLog> list;
    Context context;
    OnPhoneCallClickListener phoneCallClickListener;

    public CallLogAdapter(List<CallLog> list, Context context, OnPhoneCallClickListener phoneCallClickListener) {
        this.list = list;
        this.context = context;
        this.phoneCallClickListener = phoneCallClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.call_log_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String duration = Integer.parseInt(list.get(position).getDuration()) == 0 ? "Cancelled" : "["+list.get(position).getDuration()+" sec]";

        if (Integer.parseInt(list.get(position).getDuration()) == 0){
            holder.contactName.setTextColor(Color.parseColor("#FFE60404"));
            holder.call_duration.setTextColor(Color.parseColor("#FFE60404"));
        }else {
            holder.contactName.setTextColor(Color.parseColor("#000000"));
            holder.call_duration.setTextColor(Color.parseColor("#000000"));
        }

        holder.contactName.setText(list.get(position).getName());
        holder.call_duration.setText(duration);
        holder.callDate.setText(list.get(position).getDate());
        holder.call_number.setText(list.get(position).getNumber());

    }

    //TODO : sec to min if sec > 60 than 1 min 2sec

    @Override
    public int getItemCount() {
        return list.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView contactName, callDate, call_number, call_duration;
        OnPhoneCallClickListener onPhoneCallClickListener;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            contactName = itemView.findViewById(R.id.call_name);
            callDate = itemView.findViewById(R.id.call_date);
            call_number = itemView.findViewById(R.id.call_number);
            call_duration = itemView.findViewById(R.id.call_duration);
            this.onPhoneCallClickListener = phoneCallClickListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            CallLog callLog = list.get(getAdapterPosition());
            int id = view.getId();
            if (id == R.id.call_log_layout){
                onPhoneCallClickListener.onClick(callLog);
            }
        }
    }
}
