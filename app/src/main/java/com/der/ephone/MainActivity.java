package com.der.ephone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;
import androidx.transition.Transition;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.der.ephone.adapter.ContactAdapter;
import com.der.ephone.adapter.IToolBarTitle;
import com.der.ephone.adapter.OnContactCallClickListener;
import com.der.ephone.fragments.ContactFragment;
import com.der.ephone.fragments.ContactProfileFragment;
import com.der.ephone.model.Contact;
import com.der.ephone.utils.Communicator;
import com.der.ephone.utils.MyViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements IToolBarTitle {

    private NavController navController;
    private BottomNavigationView bottomNavigationView;
    private ContactAdapter contactAdapter;
    private TextView contactSize;

    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

    ContactFragment contactFragment;
    ContactProfileFragment contactProfileFragment;

    List<Contact> contactList;

    private TextView mToolBarTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        contactSize = findViewById(R.id.contact_size);
        mToolBarTitle = findViewById(R.id.tool_bar_title);

        //if you want to set your toolbar label set it here.

/*        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);*/
        Toolbar toolbar = findViewById(R.id.toolbar);
        setTitle("");
        setSupportActionBar(toolbar);

        navController = Navigation.findNavController(this, R.id.host_fragment);
        bottomNavigationView =findViewById(R.id.bottom_menu);
        permission();

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                return NavigationUI.onNavDestinationSelected(item, navController);
            }
        });
    }

    private void doFragmentTransition(Fragment fragment, String tag, boolean addToBackStack, String message){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.main_activity, fragment, tag);

        if (addToBackStack){
            transaction.addToBackStack(tag);
        }
        transaction.commit();

    }

    public void permission(){
        Dexter.withContext(this).withPermissions(Manifest.permission.CALL_PHONE, Manifest.permission.READ_CALL_LOG, Manifest.permission.READ_CONTACTS,
                Manifest.permission.READ_PHONE_NUMBERS)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {

                    }
                }).check();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.top_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_about) {
            Intent intent = new Intent(MainActivity.this, AboutActivity.class);
            startActivity(intent);
        }/*else if (id == R.id.action_setting){
            return true;
        }*/

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setToolBarTitle(String fragmentTag) {
        mToolBarTitle.setText(fragmentTag);
    }
}