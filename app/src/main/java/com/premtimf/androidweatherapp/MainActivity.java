package com.premtimf.androidweatherapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.ActivityCompat;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.label305.asynctask.SimpleAsyncTask;
import com.premtimf.androidweatherapp.adapter.ViewPagerAdapter;
import com.premtimf.androidweatherapp.common.Common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.GZIPInputStream;

public class MainActivity extends AppCompatActivity {

    private List<String> listCities;
    private int cityRows;

    private androidx.appcompat.widget.Toolbar toolbar;
    private TabLayout tabLayout;
    private CoordinatorLayout coordinatorLayout;
    private ViewPager viewPager;

    private FusedLocationProviderClient fusedLocationProviderClient;
    private LocationCallback locationCallback;
    private LocationRequest locationRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.root_view);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Request permission
        Dexter.withActivity(this)
                .withPermissions(Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {

                        if (report.areAllPermissionsGranted()){
                            buildLocationRequest();
                            buildLocationCallback();

                            if(ActivityCompat.checkSelfPermission(MainActivity.this,
                                    Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                                    && ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED ){
                                return;
                            }

                            fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(MainActivity.this);
                            fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        Snackbar.make(coordinatorLayout, "Permission Denied", Snackbar.LENGTH_LONG)
                                .show();
                    }
                }).check();

        new LoadCities().execute();

    }

    public class LoadCities extends SimpleAsyncTask<List<String>> {
        @Override
        protected List<String> doInBackgroundSimple() {
            listCities = new ArrayList<>();
            try {
                StringBuilder stringBuilder = new StringBuilder();
                InputStream inputStream = getResources().openRawResource(R.raw.city_list);
                GZIPInputStream gzipInputStream = new GZIPInputStream(inputStream);

                InputStreamReader inputStreamReader = new InputStreamReader(gzipInputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                String readed;
                while ((readed = bufferedReader.readLine()) != null) {
                    stringBuilder.append(readed);
//                    System.out.println("-----" + readed);
                }

                listCities = new Gson().fromJson(stringBuilder.toString(), new TypeToken<List<String>>() {
                }.getType());


            } catch (IOException e) {
                e.printStackTrace();
            }
            return listCities;
        }
    }

    private void buildLocationCallback() {
        locationCallback = new LocationCallback(){
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);

                Common.current_location = locationResult.getLastLocation();

                viewPager = (ViewPager) findViewById(R.id.view_pager);
                setupViewPager(viewPager);

                tabLayout = (TabLayout) findViewById(R.id.tabs);
                tabLayout.setupWithViewPager(viewPager);

                Log.d("Location", locationResult.getLastLocation().getLatitude() + "/" + locationResult.getLastLocation().getLongitude());

            }
        };
    }

    private void setupViewPager(ViewPager viewPager) {

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(TodayWeatherFragment.getInstance(), "Today");
        adapter.addFragment(ForecastFragment.getInstance(), "Forecast");
        viewPager.setAdapter(adapter);

    }

    private void buildLocationRequest() {

        locationRequest = new LocationRequest();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(3000);
        locationRequest.setSmallestDisplacement(10.0f);

    }

}
