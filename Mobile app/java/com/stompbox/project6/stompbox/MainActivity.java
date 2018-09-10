package com.stompbox.project6.stompbox;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

/* Android uses Activities, kinda like main(), to start up the app
 * Activity = single, focused thing that the user can do (i.e. different screens)
 * an Activity can also be thought of as the Java code that supports a screen
 * MainActivity will act as the "Home" screen
 * Is a slide-out Navigation Drawer
 */
public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    // declare programmatic side of the XML objects
    ListView topListView;
    FloatingActionButton fab_send;
    FloatingActionButton fab_record;
    Fragment newFragment;
    FragmentTransaction fragmentTransaction;
    RecordDialogFragment recordDialogFragment;
    HomeScreenList homeScreenList;
    ArrayList homeScreenListItems;
    ArrayAdapter<String> arrayAdapter;
    Handler handler;
    final static int ONE_SECOND = 1000;

    //

    //
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); // initializes activity
        setContentView(R.layout.activity_main); // explicitly set activity content w/ a specific view; inflate activity's UI

        // identify and link XML w/ future programmatic interactions
        topListView = (ListView) findViewById(R.id.topList_Window);
        fab_send = (FloatingActionButton) findViewById(R.id.fab_send);
        fab_record = (FloatingActionButton) findViewById(R.id.fab_record);

        // show Home screen library
        homeScreenListItems = new HomeScreenList().listItems;
        arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, homeScreenListItems);
        topListView.setAdapter(arrayAdapter);

        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                updateAppLibraryList();
                handler.postDelayed(this, ONE_SECOND); // repeat refresh
            }
        }, ONE_SECOND); // refresh


        // Action Bar toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        // modify app bar to include Bluetooth?
        setSupportActionBar(toolbar); // makes the Toolbar 'toolbar' act as the action bar for this activity window

        // Floating Action Button - Send
        fab_send.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                Snackbar.make(view, "Replace with SEND action code", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            }
        });

        // Floating Action Button - Record
        fab_record.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                //Snackbar.make(view, "Replace with record action code", Snackbar.LENGTH_LONG).setAction("Action", null).show();

                // Change record fab appearance

                // start Record actions w/ Dialog Fragment
                if(getFragmentManager().findFragmentByTag("record_dialog") == null){
                    recordDialogFragment = new RecordDialogFragment(); // instantiate new RecordDialogFragment (DialogFragment)
                    recordDialogFragment.show(getFragmentManager(), "record_dialog"); // make visible
                    recordDialogFragment.runCountdownTimer(); // begin countdown timer upon clicking on fab
                    recordDialogFragment.recordAudio(); // create an object for recording some audio
                }
                else{ // record fragment already exists, and just want a callback
                    getFragmentManager().findFragmentByTag("record_dialog");
                }

            }
        });


        // Navigation Drawer
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    } // end onCreate()

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch(item.getItemId()){
            case (R.id.action_settings):
                // Settings Fragment
                Toast.makeText(getApplicationContext(), "Replace with SETTINGS action code", Toast.LENGTH_LONG).show();
                return true;

            default: break;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.

        switch(item.getItemId()){
            case (R.id.nav_home):
                // Home Fragment
                // getFragmentManager().popBackStack(); // to go back to previous screen (previous fragment)
                Toast.makeText(getApplicationContext(), "HOME screen", Toast.LENGTH_SHORT).show();
                break;

            case (R.id.nav_connections):
                // Connections Fragment
                newFragment = new ConnectionsFragment();
                fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragment, newFragment); // replace fragment
                fragmentTransaction.addToBackStack(null); // add transaction to Back stack
                fragmentTransaction.commit(); // commit new transaction

                Toast.makeText(getApplicationContext(), "Replace with CONNECTIONS action code", Toast.LENGTH_SHORT).show();
                break;

            case (R.id.nav_share):
                // Share Fragment
                Toast.makeText(getApplicationContext(), "Replace with SHARE action code", Toast.LENGTH_SHORT).show();
                break;

            case (R.id.nav_trash):
                // Trash Fragment
                Toast.makeText(getApplicationContext(), "Replace with TRASH action code", Toast.LENGTH_SHORT).show();
                break;

            case (R.id.nav_advancedSetting):
                // Advanced Settings Fragment
                Toast.makeText(getApplicationContext(), "Replace with ADVANCED SETTINGS action code", Toast.LENGTH_SHORT).show();
                break;

            default: break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    //
    public void showDialog(View v){

    }

    //
    public void updateAppLibraryList(){
        ArrayList tempList = new HomeScreenList().listItems;
        homeScreenListItems.clear();
        for(int i=0; i<tempList.size(); i++){ // add each item one at a time, else with add an array as the list item
            homeScreenListItems.add(tempList.get(i));
        }
        arrayAdapter.notifyDataSetChanged();
    }


}
