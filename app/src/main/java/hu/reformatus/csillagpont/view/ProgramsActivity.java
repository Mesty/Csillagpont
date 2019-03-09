package hu.reformatus.csillagpont.view;

import android.annotation.SuppressLint;
import android.app.SearchManager;
import android.app.SearchableInfo;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;;
import android.support.v7.widget.Toolbar;
import android.content.Context;
import android.os.Bundle;
//import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import hu.reformatus.csillagpont.R;
import hu.reformatus.csillagpont.model.programs.RemoteDatabase;
import hu.reformatus.csillagpont.model.programs.databases.DatabaseQuery;
import hu.reformatus.csillagpont.model.programs.databases.EventObjects;
import hu.reformatus.csillagpont.viewmodel.OnSwipeTouchListener;

import static hu.reformatus.csillagpont.model.programs.databases.EventObjects.getCollideLevel;


public class ProgramsActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {
    private static final String TAG = ProgramsActivity.class.getSimpleName();

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_programs);
        Toolbar toolbar = findViewById(R.id.programs_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getString(R.string.programs_title));
        RemoteDatabase rmDb = new RemoteDatabase(this);
        if (rmDb.isNetworkAvailable())
            rmDb.checkAndDownloadUpdates();
        else
            Toast.makeText(this, getString(R.string.offline_mode), Toast.LENGTH_SHORT).show();
        // Get the ViewPager and set it's PagerAdapter so that it can display items
        ViewPager viewPager = findViewById(R.id.viewpager);
        viewPager.setAdapter(new ProgramsFragmentPagerAdapter(getSupportFragmentManager(),
                ProgramsActivity.this));
        // Give the TabLayout the ViewPager
        TabLayout tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.programs_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.search_bar);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        if (searchView != null) {
            searchView.setOnQueryTextListener(this);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.item1:
                item.setChecked(!item.isChecked());
                break;
            case R.id.item2:
                item.setChecked(!item.isChecked());
                break;
            case R.id.item3:
                item.setChecked(!item.isChecked());
                break;
            case R.id.item4:
                item.setChecked(!item.isChecked());
                break;
            case R.id.item5:
                item.setChecked(!item.isChecked());
                break;
            case R.id.item6:
                item.setChecked(!item.isChecked());
                break;
            case R.id.item7:
                item.setChecked(!item.isChecked());
                break;
            case R.id.item8:
                item.setChecked(!item.isChecked());
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        //keep appbar submenu visible
        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW);
        item.setActionView(new View(this));
        item.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                return false;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                return false;
            }
        });
        return super.onOptionsItemSelected(item);
    }

    public boolean onQueryTextChange(String newText) {
        Log.d(TAG, "onQueryTextChange: Query = " + newText);
        return false;
    }

    public boolean onQueryTextSubmit(String query) {
        Log.d(TAG, "onQueryTextSubmit: Query = " + query + " : submitted");
        return false;
    }
}
