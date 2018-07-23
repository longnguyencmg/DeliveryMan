package com.planday.deliveroo;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.Pair;
import android.text.Html;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
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
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.PolyUtil;
import com.planday.deliveroo.model.MapPlace;
import com.planday.deliveroo.service.GoogleService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MapActivity extends FragmentActivity implements OnMapReadyCallback,
                                                             GoogleApiClient.ConnectionCallbacks,
                                                             GoogleApiClient.OnConnectionFailedListener,
                                                             LocationListener {
    private String TAG = "MapActivity";
    private GoogleMap       mMap;
    private GoogleApiClient mGoogleApiClient;
    private double          latitude;
    private double          longitude;
    private List<MapPlace>  selectedPlaces;
    private ProgressDialog  dialog;
    private Boolean isStartingLocationSet = false;
    private GoogleService googleService;
    private List<MapPlace> notifyPlaces;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        googleService = new GoogleService();
        selectedPlaces = (List<MapPlace>) getIntent().getSerializableExtra(PlaceActivity.SELECTED_PLACES);

        dialog = new ProgressDialog(this);
        dialog.setTitle("Đợi 1 chút");
        dialog.setMessage("Đang tìm đường ngắn nhất!");
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkLocationPermission();
        }

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    @Override
    public void onLocationChanged(Location location) {
        latitude = location.getLatitude();
        longitude = location.getLongitude();

        if (!isStartingLocationSet) {
            selectedPlaces = getPlaces();
            isStartingLocationSet = true;
            notifyPlaces = new ArrayList<>(selectedPlaces);
            findRoute();
        }

        //do notification
        if (isStartingLocationSet) {
            String placeName = "";

            for (MapPlace place : notifyPlaces) {
                if (DistanceUtil.distance(latitude, longitude, place.getLatitude(), place.getLongitude()) <= 0.4) {
                    pushNotification(place);
                    placeName = place.getName();
                    break;
                }
            }

            Iterator<MapPlace> it = notifyPlaces.iterator();
            while (it.hasNext()) {
                MapPlace mapPlace = it.next();
                if (mapPlace.getName().equalsIgnoreCase(placeName)) {
                    it.remove();
                }
            }
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onConnected(@Nullable Bundle bundle) {
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(1000);
        locationRequest.setFastestInterval(1000);
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        if (ContextCompat.checkSelfPermission(this,
                                              Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, locationRequest, this);

        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                buildGoogleApiClient();
                mMap.setMyLocationEnabled(true);
            }
        } else {
            buildGoogleApiClient();
            mMap.setMyLocationEnabled(true);
        }
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    void displayDirections(List<String[]> directions, List<MapPlace> reorderedPlaces) {
        mMap.clear();
        mMap.animateCamera(CameraUpdateFactory.zoomBy(0));
        displayMarkers(reorderedPlaces);
        for (String legs[] : directions) {
            for (String directionsList : legs) {
                PolylineOptions options = new PolylineOptions();
                options.color(Color.RED);
                options.width(8);
                options.addAll(PolyUtil.decode(directionsList));
                mMap.addPolyline(options);
            }
        }

        LatLng latLng = new LatLng(latitude, longitude);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(14));
    }

    private void displayMarkers(List<MapPlace> orderedPlaces) {
        LatLng latLng;
        MarkerOptions markerOptions = new MarkerOptions();

        MapPlace currentLocation = null;
        int iterator = 0;

        if (orderedPlaces.get(0).getName().contains(getResources().getString(R.string.current_location))) {
            currentLocation = orderedPlaces.get(0);
            iterator = 1;
        }

        if (currentLocation != null) {
            //current location:
            latLng = new LatLng(orderedPlaces.get(0).getLatitude(), orderedPlaces.get(0).getLongitude());
            markerOptions.position(latLng);
            markerOptions.title(orderedPlaces.get(0).getName());
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
            mMap.addMarker(markerOptions);

            //other locations:
            for (int i = iterator; i < orderedPlaces.size(); i++) {
                latLng = new LatLng(orderedPlaces.get(i).getLatitude(), orderedPlaces.get(i).getLongitude());
                markerOptions.position(latLng);
                markerOptions.title(i + ". " + orderedPlaces.get(i).getName());
                markerOptions.snippet(orderedPlaces.get(i).getDescription());
                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                mMap.addMarker(markerOptions);
            }
        }

        //if there is no current location in the tour
        for (int i = iterator; i < orderedPlaces.size(); i++) {
            latLng = new LatLng(orderedPlaces.get(i).getLatitude(), orderedPlaces.get(i).getLongitude());
            markerOptions.position(latLng);
            markerOptions.title(orderedPlaces.get(i).getName());
            markerOptions.snippet(orderedPlaces.get(i).getDescription());
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
            mMap.addMarker(markerOptions);
        }

    }

    void displayAdditionalInfo(List<MapPlace> reorderedPlaces) {
        TextView detailsTextView = findViewById(R.id.result);
        String solution = detailsTextView.getText() + getString(R.string.final_route);
        for (MapPlace place : reorderedPlaces) {
            solution += place.getName() + " -> ";
        }
        solution += reorderedPlaces.get(0).getName() + "\n\n";

        solution += getString(R.string.shortest_path) + googleService.getShortestDistance() / 1000 + "[km],  "
                    + getString(R.string.longest_path) + googleService.getLongestDistance() / 1000 + "[km]";

        detailsTextView.setText(solution);
    }

    public List<MapPlace> getPlaces() {
        return selectedPlaces;
    }

    private void findRoute() {
        StringBuilder origins = new StringBuilder();
        for (MapPlace mapPlace : selectedPlaces) {
            origins.append(mapPlace.getLatitude()).append(",").append(mapPlace.getLongitude()).append("|");
        }

        addSubscription(googleService.getDirections(selectedPlaces, "metric", origins.toString(), origins.toString(),
                                                    "walking", getResources().getString(R.string.GoogleMapsDirectionsApiKey))
                                     .subscribeOn(Schedulers.io())
                                     .observeOn(AndroidSchedulers.mainThread())
                                     .subscribe(new Subscriber<Pair<List<String[]>, List<MapPlace>>>() {
                                         @Override
                                         public void onStart() {
                                             super.onStart();
                                             showProgressDialog(true);
                                         }

                                         @Override
                                         public void onCompleted() {

                                         }

                                         @Override
                                         public void onError(Throwable e) {
                                             Log.e(TAG, e.getMessage());
                                             showProgressDialog(false);
                                         }

                                         @Override
                                         public void onNext(Pair<List<String[]>, List<MapPlace>> data) {
                                             showProgressDialog(false);
                                             displayDirections(data.first, data.second);
                                             displayAdditionalInfo(data.second);
                                         }
                                     }));
    }

    void showProgressDialog(boolean isShow) {
        if (isShow) {
            dialog.show();
        } else {
            dialog.dismiss();
        }
    }

    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this,
                                              Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED) {

            // Asking user if explanation is needed
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                                                                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                ActivityCompat.requestPermissions(this,
                                                  new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                                  MY_PERMISSIONS_REQUEST_LOCATION);


            } else {
                ActivityCompat.requestPermissions(this,
                                                  new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                                  MY_PERMISSIONS_REQUEST_LOCATION);
            }
        }
    }

    protected void addSubscription(Subscription subscription) {
        SubscriptionManager.subscribe(this.getClass(), subscription);
    }

    private void pushNotification(MapPlace place) {
        Intent intent = new Intent(this, MapActivity.class);
        PendingIntent pIntent = PendingIntent.getActivity(this, (int) System.currentTimeMillis(), intent, 0);

        // Build notification
        // Actions are just fake
        Notification noti = new Notification.Builder(this)
                .setContentTitle("Deliveroooooooooo")
                .setContentText("Sap den " + place.getName() + " - " + place.getDescription())
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setContentIntent(pIntent).build();
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        // hide the notification after its selected
        noti.flags |= Notification.FLAG_AUTO_CANCEL;

        notificationManager.notify(0, noti);
    }
}
