package com.der.ephone.fragments;

import android.Manifest;
import android.app.Notification;
import android.app.SearchManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.der.ephone.MainActivity;
import com.der.ephone.R;
import com.der.ephone.adapter.ContactAdapter;
import com.der.ephone.adapter.IMainActivity;
import com.der.ephone.adapter.IToolBarTitle;
import com.der.ephone.adapter.OnContactCallClickListener;
import com.der.ephone.model.Contact;
import com.der.ephone.utils.Communicator;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ContactFragment extends Fragment implements OnContactCallClickListener {

    public static final String TAG = "ContactFragment";

    ContactAdapter adapter;
    RecyclerView recyclerView;
    private SearchView searchView;
    TextView textView;
    List<Contact> contactList;
    ContactProfileFragment contactProfileFragment;
    IToolBarTitle iToolBarTitle;
    IMainActivity iMainActivity;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        iToolBarTitle = (IToolBarTitle) getActivity();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: ");
        iToolBarTitle.setToolBarTitle(getTag());
        contactList = new ArrayList<>();
        contactList = getContacts();
        adapter = new ContactAdapter(contactList, getContext(), this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_contact, container, false);
        super.onSaveInstanceState(savedInstanceState);
        Log.d(TAG, "onCreateView: ");
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.contact_recycler);
        searchView = view.findViewById(R.id.search_bar);
        textView = view.findViewById(R.id.contact_size);
        Log.d(TAG, "onViewCreated: ");

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
        textView.setText(contactSize()+ " contacts");
        searchContact();
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

        for (Contact contact : getContacts()){
            if (contact.getContactNumber().toLowerCase().contains(newText.toLowerCase())){
                filterContactList.add(contact);
            }else if (contact.getContactName().toLowerCase().contains(newText.toLowerCase())){
                filterContactList.add(contact);
            }
        }
        adapter.filterList(filterContactList);
    }

    public int contactSize(){
        return getContacts().size();
    }

    public List<Contact> getContacts(){

        List<Contact> contacts = new ArrayList<>();

        if ( ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_CONTACTS}, 1);
        }

        ContentResolver contentResolver = getContext().getContentResolver();
        Cursor cursor = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, ContactsContract.Contacts.DISPLAY_NAME+ " ASC");

        cursor.moveToFirst();

        while (cursor.moveToNext()){
            int name = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
            int number = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
            int image = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_URI);

            Contact contact = new Contact();
            contact.setContactName(cursor.getString(name));
            contact.setContactNumber(cursor.getString(number));
            contact.setImage(cursor.getString(image));

            if (contact.getContactName() != null){
                contacts.add(contact);
            }
        }
        return contacts;
    }

    @Override
    public void onClick(Contact contact) {
        contactProfileFragment = new ContactProfileFragment();
        doFragmentTransition(contactProfileFragment, getString(R.string.contact_profile), false, "", contact);
    }

    private void doFragmentTransition(Fragment fragment, String tag, boolean addToBackStack, String message, Contact contact){
        Bundle bundle = new Bundle();
        bundle.putString("name", contact.getContactName());
        bundle.putString("number", contact.getContactNumber());

        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        contactProfileFragment.setArguments(bundle);
        transaction.replace(R.id.main_activity, fragment, tag);

        if (addToBackStack){
            transaction.addToBackStack(tag);
        }
        transaction.commit();

    }


}