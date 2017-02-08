package com.maps.rahat.mapswithlocation.Activity;


import android.Manifest;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;

import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.maps.rahat.mapswithlocation.Address;
import com.maps.rahat.mapswithlocation.GetNearbyPlacesData;
import com.maps.rahat.mapswithlocation.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,LocationListener {

    private GoogleMap mMap;

    private int PROXIMITY_RADIUS = 1000;
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    Marker mCurrLocationMarker;
    LocationRequest mLocationRequest;
    double latitude ;
    double longitude ;
    ListView listView;
    Button btnMasjidBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            checkLocationPermission();
        }
        listView = (ListView)findViewById(R.id.restaurantListView);


        if (!CheckGooglePlayServices()) {
            Toast.makeText(this, "Google Play service not available", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Google Play service  available", Toast.LENGTH_SHORT).show();
        }

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        mapFragment.getMapAsync(this);

    }

    private boolean CheckGooglePlayServices() {

        GoogleApiAvailability googleAPI = GoogleApiAvailability.getInstance();
        int result = googleAPI.isGooglePlayServicesAvailable(this);
        if (result != ConnectionResult.SUCCESS) {

            if (googleAPI.isUserResolvableError(result)) {
                googleAPI.getErrorDialog(this, result,
                        0).show();
            }
            return false;
        }
        return true;
    }
    private String getUrl(double latitude, double longitude, String nearbyPlace) {

        StringBuilder googlePlacesUrl = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        googlePlacesUrl.append("location=" + latitude + "," + longitude);
        googlePlacesUrl.append("&radius=" + PROXIMITY_RADIUS);
        googlePlacesUrl.append("&type=" + nearbyPlace);
        googlePlacesUrl.append("&sensor=true");
        googlePlacesUrl.append("&key=" + "AIzaSyDjc8P0vqa3dMg9Mu-tSUh_s32wAijFB98");
        Log.d("getUrl", googlePlacesUrl.toString());
        return (googlePlacesUrl.toString());
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
            initializedGoogleApiClient();
            mMap.setMyLocationEnabled(true);

        }
        else {
            initializedGoogleApiClient();
            mMap.setMyLocationEnabled(true);
        }


        Button btnRestaurant = (Button) findViewById(R.id.btnRestaurant);
        Button btnCafe = (Button) findViewById(R.id.btnCafe);

        Button btnAtmBooth = (Button) findViewById(R.id.btnAtmBooth);
        btnMasjidBtn = (Button) findViewById(R.id.btnMasjidBtn);

        btnRestaurant.setOnClickListener(new View.OnClickListener() {
            String Restaurant = "restaurant";

            @Override
            public void onClick(View v) {

                mMap.clear();
                if(mLastLocation != null) {
                String url = getUrl(mLastLocation.getLatitude(), mLastLocation.getLongitude(), Restaurant);

                    Object[] DataTransfer = new Object[2];
                    DataTransfer[0] = mMap;
                    DataTransfer[1] = url;
                    Log.d("onClick", url);

                    GetNearbyPlacesData getNearbyPlacesData = new GetNearbyPlacesData(MapsActivity.this, listView);

                    getNearbyPlacesData.execute(DataTransfer);

                    Toast.makeText(MapsActivity.this, "Nearby Restaurants", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(MapsActivity.this, "Please Location Service Enabled", Toast.LENGTH_SHORT).show();
                    return ;

                }
            }
        });
        btnCafe.setOnClickListener(new View.OnClickListener() {
            String cafe = "cafe";

            @Override
            public void onClick(View v) {

                mMap.clear();
                if(mLastLocation != null) {
                String url = getUrl(mLastLocation.getLatitude(), mLastLocation.getLongitude(), cafe);

                    Object[] DataTransfer = new Object[2];
                    DataTransfer[0] = mMap;
                    DataTransfer[1] = url;
                    Log.d("onClick", url);

                    GetNearbyPlacesData getNearbyPlacesData = new GetNearbyPlacesData(MapsActivity.this, listView);

                    getNearbyPlacesData.execute(DataTransfer);

                    Toast.makeText(MapsActivity.this, "Nearby Cafe", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(MapsActivity.this, "Please Location Service Enabled", Toast.LENGTH_SHORT).show();
                    return ;

                }
            }
        });
        btnMasjidBtn.setOnClickListener(new View.OnClickListener() {
            String masjid = "masjid";

            @Override
            public void onClick(View v) {

                mMap.clear();
                if(mLastLocation != null) {
                String url = getUrl(mLastLocation.getLatitude(), mLastLocation.getLongitude(), masjid);

                    Object[] DataTransfer = new Object[2];
                    DataTransfer[0] = mMap;
                    DataTransfer[1] = url;
                    Log.d("onClick", url);

                    GetNearbyPlacesData getNearbyPlacesData = new GetNearbyPlacesData(MapsActivity.this, listView);

                    getNearbyPlacesData.execute(DataTransfer);

                    Toast.makeText(MapsActivity.this, "Nearby Mashjid", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(MapsActivity.this, "Please Location Service Enabled", Toast.LENGTH_SHORT).show();
                    return ;

                }
            }
        });
        btnAtmBooth.setOnClickListener(new View.OnClickListener() {
            String atm = "atm";

            @Override
            public void onClick(View v) {

                mMap.clear();
                if(mLastLocation != null) {
                String url = getUrl(mLastLocation.getLatitude(), mLastLocation.getLongitude(), atm);

                    Object[] DataTransfer = new Object[2];
                    DataTransfer[0] = mMap;
                    DataTransfer[1] = url;
                    Log.d("onClick", url);

                    GetNearbyPlacesData getNearbyPlacesData = new GetNearbyPlacesData(MapsActivity.this, listView);

                    getNearbyPlacesData.execute(DataTransfer);

                    Toast.makeText(MapsActivity.this, "Nearby atm", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(MapsActivity.this, "Please Location Service Enabled", Toast.LENGTH_SHORT).show();
                    return ;

                }
            }
        });


    }
    protected synchronized void initializedGoogleApiClient() {

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

        Toast.makeText(this, connectionResult.getErrorMessage(), Toast.LENGTH_SHORT).show();
        return;
    }

    @Override
    public void onLocationChanged(Location location) {
        mLastLocation = location;
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }

        //Place current location marker
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("Current Position");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
        mCurrLocationMarker = mMap.addMarker(markerOptions);

        //move map camera
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(16));

        //stop location updates
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }
    }

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    public boolean checkLocationPermission(){

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Asking user if explanation is needed
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

                //Prompt the user once explanation has been shown
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }
}
