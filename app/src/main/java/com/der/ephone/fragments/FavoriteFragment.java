package com.der.ephone.fragments;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.der.ephone.R;
import com.der.ephone.adapter.ContactAdapter;
import com.der.ephone.adapter.FavoriteAdapter;
import com.der.ephone.adapter.OnContactCallClickListener;
import com.der.ephone.model.Contact;

import java.util.ArrayList;
import java.util.List;

public class FavoriteFragment extends Fragment implements OnContactCallClickListener {

    FavoriteAdapter adapter;
    RecyclerView recyclerView;
    private SearchView searchView;
    List<Contact> contactList;
    ContactProfileFragment contactProfileFragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        contactList = new ArrayList<>();
        contactList = getFavoriteContacts();
        adapter = new FavoriteAdapter(contactList, getContext(), this);
        contactProfileFragment = new ContactProfileFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_favorite, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.favorite_recycler);
        searchView = view.findViewById(R.id.favorite_search_bar);
        adapter = new FavoriteAdapter(getFavoriteContacts(), getContext(), this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
        searchContact();;
    }

    private void searchContact(){
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                contactFilter(newText);
                return true;
            }
        });
    }

    private void contactFilter(String newText){
        List<Contact> filterContactList = new ArrayList<>();
        for (Contact contact : getFavoriteContacts()){
            if (contact.getContactNumber().toLowerCase().contains(newText.toLowerCase())){
                filterContactList.add(contact);
            }else if (contact.getContactName().toLowerCase().contains(newText.toLowerCase())){
                filterContactList.add(contact);
            }
        }

        adapter.filterList(filterContactList);
    }

    private List<Contact> getFavoriteContacts(){

        List<Contact> contacts = new ArrayList<>();

        if ( ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_CONTACTS}, 1);
        }

        Uri queryUri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        ContentResolver contentResolver = getContext().getContentResolver();
        String orderByName = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC";

        Cursor cursor = contentResolver.query(queryUri, null, "starred=?", new String[] {"1"}, orderByName);

        cursor.moveToFirst();

        while (cursor.moveToNext()){
            int name = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
            int number = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
            int image = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_URI);
            int starred = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.STARRED);

            Contact contact = new Contact();
            contact.setContactName(cursor.getString(name));
            contact.setContactNumber(cursor.getString(number));
            contact.setImage(cursor.getString(image));
            contact.setStarred(cursor.getString(starred));

            if (contact.getContactName() != null){
                contacts.add(contact);
            }
        }
        return contacts;
    }

    @Override
    public void onClick(Contact contact) {

/*        Bundle bundle = new Bundle();
        bundle.putString("name", contact.getContactName());
        bundle.putString("number", contact.getContactNumber());

        FragmentTransaction transition = getActivity().getSupportFragmentManager().beginTransaction();
        contactProfileFragment.setArguments(bundle);
        transition.replace(R.id.main_activity, contactProfileFragment);
        //TODO : addToBackStack (back press dose not show fragment )
        //transition.addToBackStack("fragment_contact");
        transition.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        transition.commit();*/

    }
}