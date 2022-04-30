package com.der.ephone.adapter;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.der.ephone.R;
import com.der.ephone.model.Contact;

import java.util.List;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.ViewHolder>{

    List<Contact> list;
    Context context;
    OnContactCallClickListener contactCallClickListener;

    public FavoriteAdapter(List<Contact> list, Context context, OnContactCallClickListener contactCallClickListener) {
        this.list = list;
        this.context = context;
        this.contactCallClickListener = contactCallClickListener;
    }

    @NonNull
    @Override
    public FavoriteAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.contact_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteAdapter.ViewHolder holder, int position) {

        String name = list.get(position).getContactName();
        String number = list.get(position).getContactNumber();


        if (name != null){
            holder.contactName.setText(name);
            holder.contactNumber.setText(number);
        }

    }

    public void filterList(List<Contact> contacts){
        this.list = contacts;
        notifyDataSetChanged();
    }

    //TODO Duplicate contact number

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView contactName, contactNumber;
        ImageView contactImage, contactPhCall;
        OnContactCallClickListener onContactCallClickListener;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            contactName = itemView.findViewById(R.id.contact_name);
            contactNumber = itemView.findViewById(R.id.contact_number);
            contactImage = itemView.findViewById(R.id.contact_image);
            contactPhCall = itemView.findViewById(R.id.contact_phone_call);;

            this.onContactCallClickListener = contactCallClickListener;
            itemView.setOnClickListener(this);

            contactPhCall.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Contact contact = list.get(getAdapterPosition());
                    makePhoneCall(contact);
                }
            });
        }

        private void makePhoneCall(Contact contact){
            String dial = "tel:" + contact.getContactNumber();
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED){
                return;
            }
            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse(dial));
            context.startActivity(intent);
        }

        @Override
        public void onClick(View view) {
            Contact contact = list.get(getAdapterPosition());
            int id = view.getId();
            if (id == R.id.contact_layout){
                onContactCallClickListener.onClick(contact);
            }
        }
    }
}
