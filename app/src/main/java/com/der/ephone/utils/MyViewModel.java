package com.der.ephone.utils;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.der.ephone.model.Contact;

import java.util.List;

public class MyViewModel extends ViewModel {
    private MutableLiveData<List<Contact>> contacts;
    public LiveData<List<Contact>> getUsers() {
        if (contacts == null) {
            contacts = new MutableLiveData<List<Contact>>();
            loadContact();
        }
        return contacts;
    }

    private void loadContact() {
        // Do an asynchronous operation to fetch users.
    }
}
