package com.maps.rahat.mapswithlocation;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Parcelable;
import android.util.Log;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.maps.rahat.mapswithlocation.Activity.AddressActivity;
import com.maps.rahat.mapswithlocation.Activity.MapsActivity;
import com.maps.rahat.mapswithlocation.DataParser.DataParser;

import java.io.Serializable;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by rahat on 1/31/17.
 */

public class GetNearbyPlacesData extends AsyncTask<Object, String, String> {

    String googlePlacesData;
    GoogleMap mMap;
    String url;
    ArrayList<HashMap<String, String>> myPlaceList = new ArrayList<HashMap<String, String>>();

    private Context context;
    private ListView listView;

    public GetNearbyPlacesData(Context context,ListView listView) {
        this.context = context;
        this.listView = listView;
    }

    @Override
    protected String doInBackground(Object... params) {
        try {
            Log.d("GetNearbyPlacesData", "doInBackground entered");
            mMap = (GoogleMap) params[0];
            url = (String) params[1];
            DownloadUrl downloadUrl = new DownloadUrl();
            googlePlacesData = downloadUrl.readUrl(url);

            Log.d("GooglePlacesReadTask", "doInBackground Exit");
        } catch (Exception e) {
            Log.d("GooglePlacesReadTask", e.toString());
        }
        return googlePlacesData;
    }

    @Override
    protected void onPostExecute(String result) {
        Log.d("GooglePlacesReadTask", "onPostExecute Entered");

        List<HashMap<String, String>> nearbyPlacesList = null;

        Log.d("tt", "onPostExecute: "+result);
        DataParser dataParser = new DataParser();
        nearbyPlacesList =  dataParser.parse(result);
      //  ShowNearbyPlaces(nearbyPlacesList);
        listNearbyPlaces(nearbyPlacesList);

        Log.d("GooglePlacesReadTask", "onPostExecute Exit");
    }

    private void ShowNearbyPlaces(List<HashMap<String, String>> nearbyPlacesList) {

        for (int i = 0; i < nearbyPlacesList.size(); i++) {
            Log.d("onPostExecute","Entered into showing locations");
            MarkerOptions markerOptions = new MarkerOptions();

            HashMap<String, String> googlePlace = nearbyPlacesList.get(i);
            double lat = Double.parseDouble(googlePlace.get("lat"));
            double lng = Double.parseDouble(googlePlace.get("lng"));
            String placeName = googlePlace.get("place_name");
            String vicinity = googlePlace.get("vicinity");
            LatLng latLng = new LatLng(lat, lng);
            markerOptions.position(latLng);
            markerOptions.title(placeName + " : " + vicinity);
            mMap.addMarker(markerOptions);
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
            //move map camera
            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(11));
        }
    }
    private void listNearbyPlaces(List<HashMap<String, String>> nearbyPlacesList) {

        for (int i = 0; i < nearbyPlacesList.size(); i++) {
            Log.d("onPostExecute","Entered into showing locations");

            HashMap<String, String> googlePlaceList = nearbyPlacesList.get(i);

            double lat = Double.parseDouble(googlePlaceList.get("lat"));
            double lng = Double.parseDouble(googlePlaceList.get("lng"));

            String placeName = googlePlaceList.get("place_name");
            String vicinity = googlePlaceList.get("vicinity");
            LatLng latLng = new LatLng(lat, lng);
            googlePlaceList.put("placeName",placeName);
//            googlePlaceList.put("vicinity",vicinity);
            myPlaceList.add(googlePlaceList);
        }
        String[] from = new String[] {"placeName"};
        int[] to = new int[] { R.id.textView1 };
        SimpleAdapter adapter = new SimpleAdapter(context, myPlaceList, R.layout.activity_place_row, from, to);
        listView.setAdapter(adapter);



    }



}
