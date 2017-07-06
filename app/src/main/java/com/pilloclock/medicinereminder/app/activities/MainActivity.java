package com.pilloclock.medicinereminder.app.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.pilloclock.medicinereminder.app.R;
import com.pilloclock.medicinereminder.app.adapters.ViewPagerAdapter;
import com.pilloclock.medicinereminder.app.fragments.DescriptionFragment;
import com.pilloclock.medicinereminder.app.fragments.HomeFragment;
import com.pilloclock.medicinereminder.app.fragments.MapsFragment;

/***
 * Created by Nikko on May.
 * Project name: Pillo'Clock
 */

public class MainActivity extends AppCompatActivity {



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.new_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewPager);
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragments(new HomeFragment(), "Home");
        viewPagerAdapter.addFragments(new DescriptionFragment(), "Description");
        viewPagerAdapter.addFragments(new MapsFragment(), "Map");
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);


    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.options,menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        //disable back button
        //super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.read_docu:
                Intent document = new Intent(Intent.ACTION_VIEW, Uri.parse("http://spy-offenders-40771.bitballoon.com/"));
                startActivity(document);
                break;
            case R.id.watch_vid:
                Intent video = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=7mNnmETJqOo"));
                startActivity(video);
                break;
            case R.id.nav_signout:
                AuthUI.getInstance()
                        .signOut(this)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            public void onComplete(@NonNull Task<Void> task) {
                                // user is now signed out
                                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                                finish();
                            }
                        });
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
