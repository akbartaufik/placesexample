package com.opika.placeautocomplete.views.activities;

import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.opika.placeautocomplete.R;
import com.opika.placeautocomplete.helpers.PermissionUtils;
import com.opika.placeautocomplete.helpers.Statics;
import com.opika.placeautocomplete.model.Place;
import com.opika.placeautocomplete.presenters.NearbyPresenter;
import com.opika.placeautocomplete.views.NearbyView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MapsActivity extends BaseActivity implements GoogleMap.OnMyLocationButtonClickListener,
        OnMapReadyCallback, ActivityCompat.OnRequestPermissionsResultCallback, LocationListener,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, NearbyView, Statics, GoogleMap.OnMarkerClickListener {

    @BindView(R.id.container_list_place)
    FrameLayout containerListPLace;

//    private BottomSheetBehavior mBottomSheetBehavior;

    private NearbyPresenter nearbyPresenter;
    private GoogleMap mMap;
    private LocationRequest mLocationRequest;
    private GoogleApiClient mGoogleApiClient;
    private LatLng latLng;
    private Marker currLocationMarker;
    private String currentLocation;
    private String TYPE;
    private int images;
    private ArrayList<Place> placeArrayList;

    private boolean mPermissionDenied = false;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        ButterKnife.bind(this);
        nearbyPresenter = new NearbyPresenter(mContext);
        nearbyPresenter.attachView(this);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        containerListPLace.setVisibility(View.GONE);
//        mBottomSheetBehavior = BottomSheetBehavior.from(containerListPLace);
//        mBottomSheetBehavior.setBottomSheetCallback(sheetCallback);

    }

    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
        if (mPermissionDenied) {
            // Permission was not granted, display error dialog.
            showMissingPermissionError();
            mPermissionDenied = false;
        }
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .build();
    }

    private void enableMyLocation() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission to access the location is missing.
            PermissionUtils.requestPermission(this, LOCATION_PERMISSION_REQUEST_CODE,
                    android.Manifest.permission.ACCESS_FINE_LOCATION, true);
        } else if (mMap != null) {
            // Access to the location has been granted to the app.
            mMap.setMyLocationEnabled(true);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
            @Override
            public View getInfoWindow(Marker marker) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {
                View v = getLayoutInflater().inflate(R.layout.marker_buble_custom, null);

                // Getting the position from the marker
                TextView txtName = (TextView) v.findViewById(R.id.txt_name);
                TextView txtDistance = (TextView) v.findViewById(R.id.txt_distance);
                TextView txtAddress = (TextView) v.findViewById(R.id.txt_address);

                LatLng curLatlng = currLocationMarker.getPosition();
                Location locationCurrent = new Location("");
                locationCurrent.setLatitude(curLatlng.latitude);
                locationCurrent.setLongitude(curLatlng.longitude);

                LatLng markerLatlng = marker.getPosition();
                Location locationMarker = new Location("");
                locationMarker.setLatitude(markerLatlng.latitude);
                locationMarker.setLongitude(markerLatlng.longitude);

                float distance = locationCurrent.distanceTo(locationMarker) / 1000;

                txtName.setText(marker.getTitle());
                txtDistance.setText(String.format("%.02f", distance) + " KM");
                txtAddress.setText(marker.getSnippet());

                // Returning the view containing InfoWindow contents
                return v;
            }
        });
        mMap.setOnMarkerClickListener(this);

        enableMyLocation();
        buildGoogleApiClient();
        mGoogleApiClient.connect();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode != LOCATION_PERMISSION_REQUEST_CODE) {
            return;
        }

        if (PermissionUtils.isPermissionGranted(permissions, grantResults,
                android.Manifest.permission.ACCESS_FINE_LOCATION)) {
            // Enable the my location layer if the permission has been granted.
            enableMyLocation();
        } else {
            // Display the missing permission error dialog when the fragments resume.
            mPermissionDenied = true;
        }
    }

    @Override
    public boolean onMyLocationButtonClick() {
        return false;
    }

    private void showMissingPermissionError() {
        PermissionUtils.PermissionDeniedDialog
                .newInstance(true).show(getSupportFragmentManager(), "dialog");
    }

    @Override
    public void onLocationChanged(Location location) {
        if (currLocationMarker != null) {
            currLocationMarker.remove();
        }
        currentLocation = String.valueOf("-6.9377838,107.5969585");
//        currentLocation = String.valueOf(location.getLatitude()) + "," + String.valueOf(location.getLongitude());
        latLng = new LatLng(-6.9377838, 107.5969585);
//        latLng = new LatLng(location.getLatitude(), location.getLongitude());
        setMarker(latLng);

//        //zoom to current position:
//        CameraPosition cameraPosition = new CameraPosition.Builder().target(latLng).zoom(18).build();
//
//        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission to access the location is missing.
            PermissionUtils.requestPermission(this, LOCATION_PERMISSION_REQUEST_CODE,
                    android.Manifest.permission.ACCESS_FINE_LOCATION, true);
        }
        Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mLastLocation != null) {
            //place marker at current position
            //mGoogleMap.clear();
            latLng = new LatLng(-6.9377838, 107.5969585);
//            latLng = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
//            currentLocation = String.valueOf(mLastLocation.getLatitude()) + "," + String.valueOf(mLastLocation.getLongitude());
//            -6.9377838,107.5969585
            currentLocation = String.valueOf("-6.9377838,107.5969585");
            CameraPosition cameraPosition = new CameraPosition.Builder().target(latLng).zoom(18).build();
//
            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            setMarker(latLng);
        }

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(5000); //5 seconds
        mLocationRequest.setFastestInterval(3000); //3 seconds
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        //mLocationRequest.setSmallestDisplacement(0.1F); //1/10 meter

        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void showProgressDialogIndicator() {
        showDialog();
    }

    @Override
    public void onFailed(String message) {
        dismissDialog();
        Toast.makeText(mContext, message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onSuccess(ArrayList<Place> places) {
        dismissDialog();
        List<Marker> markers = new ArrayList<>();
        this.placeArrayList = places;

        for (int i = 0; i < places.size(); i++) {

            String name = places.get(i).getName();
            System.out.println("name : " + name);

            LatLng latLng = new LatLng(places.get(i).getPlaceGeometry().getLocation().getLat(),
                    places.get(i).getPlaceGeometry().getLocation().getLon());

            Marker marker = mMap.addMarker(new MarkerOptions().position(latLng).title(name)
                    .snippet(places.get(i).getVicinity())
                    .icon(BitmapDescriptorFactory.fromResource(images)));
            markers.add(marker);

        }

        markers.size();
        System.out.println("MARKER SIZE : " + markers.size());

    }

    private void setMarker(LatLng latLng) {
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("Current Position");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
        currLocationMarker = mMap.addMarker(markerOptions);
    }

    private void setMarkerPlaces(LatLng latLng, String name, int icon) {
        System.out.println("CREATE MARKER");
        mMap.addMarker(new MarkerOptions().position(latLng).title(name)
                .icon(BitmapDescriptorFactory.fromResource(icon)));
    }

    private BottomSheetBehavior.BottomSheetCallback sheetCallback = new BottomSheetBehavior.BottomSheetCallback() {
        @Override
        public void onStateChanged(@NonNull View bottomSheet, int newState) {
            if (newState == BottomSheetBehavior.STATE_DRAGGING) {
//                mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            }
        }

        @Override
        public void onSlide(@NonNull View bottomSheet, float slideOffset) {

        }
    };

    @OnClick(R.id.btn_atm)
    void atmListener() {
        TYPE = TYPE_ATM;
        images = R.drawable.ic_type_atm;
        nearbyPresenter.getNearbyPlaces(currentLocation, "500", TYPE_ATM);
    }

    @OnClick(R.id.btn_hospital)
    void hospitalListener() {
        TYPE = TYPE_HOSPITAL;
        images = R.drawable.ic_type_hospital;
        nearbyPresenter.getNearbyPlaces(currentLocation, "500", TYPE_HOSPITAL);
    }

    @OnClick(R.id.btn_gas)
    void gasListener() {
        TYPE = TYPE_GAS;
        images = R.drawable.ic_type_gas;
        nearbyPresenter.getNearbyPlaces(currentLocation, "500", TYPE_GAS);
    }

    @OnClick(R.id.btn_restaurant)
    void restaurantListener() {
        TYPE = TYPE_RESTAURANT;
        images = R.drawable.ic_type_restaurant;
        nearbyPresenter.getNearbyPlaces(currentLocation, "500", TYPE_RESTAURANT);
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        marker.showInfoWindow();
        return false;
    }


}
