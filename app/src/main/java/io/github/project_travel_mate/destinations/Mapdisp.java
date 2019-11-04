package io.github.project_travel_mate.destinations;

import android.location.Location;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnSuccessListener;

import io.github.project_travel_mate.R;

public class Mapdisp extends AppCompatActivity implements OnMapReadyCallback, TaskLoadedCallback {
    private GoogleMap mMap;
    private FusedLocationProviderClient mFlc;
    double lat;
    double lng;
    String origin;
    private MarkerOptions mPlace1, mPlace2;
    Button mGetDirection;
    private Polyline mCurrentPolyline;
    String city;
    double lati;
    double longi;
    double citylati;
    double citylongi;
    RadioButton mButton1;
    RadioButton mButton2;
    RadioButton mButton3;
    String mode;

    String ori;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_mapdisp );
        mode = "driving";
        mButton1 = (RadioButton) findViewById( R.id.radioButton );
        mButton2 = (RadioButton) findViewById( R.id.radioButton2 );
        mButton3 = (RadioButton) findViewById( R.id.radioButton3 );

        city = getIntent().getStringExtra( "city" );
        if (city.equalsIgnoreCase( "Delhi" )) {
            citylati = 28.7041;
            citylongi = 77.1025;
        }
        if (city.equalsIgnoreCase( "Bangalore" )) {
            citylati = 12.9716;
            citylongi = 77.5946;
        }
        if (city.equalsIgnoreCase( "Mumbai" )) {
            citylati = 19.0760;
            citylongi = 72.8777;
        }
        if (city.equalsIgnoreCase( "Chennai" )) {
            citylati = 13.0827;
            citylongi = 80.2707;
        }
        if (city.equalsIgnoreCase( "Pune" )) {
            citylati = 18.5204;
            citylongi = 73.8567;
        }
        if (city.equalsIgnoreCase( "Kolkata" )) {
            citylati = 22.5726;
            citylongi = 88.3639;
        }
        if (city.equalsIgnoreCase( "Hyderabad" )) {
            citylati = 17.3850;
            citylongi = 78.4867;
        }
        if (city.equalsIgnoreCase( "Jaipur" )) {
            citylati = 26.9124;
            citylongi = 75.7873;
        }
        if (city.equalsIgnoreCase( "Panjim" )) {
            citylati = 15.4909;
            citylongi = 73.8278;
        }
        if (city.equalsIgnoreCase( "Ahmedabad" )) {
            citylati = 23.0225;
            citylongi = 72.5714;
        }

        lati = getIntent().getDoubleExtra( "latitude" , 0 );
        longi = getIntent().getDoubleExtra( "longitude" , 0 );

        //Toast.makeText( this, ori, Toast.LENGTH_SHORT ).show();

        //Toast.makeText( this, city , Toast.LENGTH_SHORT ).show();
        mGetDirection = findViewById(R.id.btnGetDirection);
        mFlc = LocationServices.getFusedLocationProviderClient(this);
        mFlc.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            // Logic to handle location object
                            lat = location.getLatitude();
                            lng = location.getLongitude();
                            origin = lat + "," + lng;
                        } else
                            Toast.makeText( Mapdisp.this, "null", Toast.LENGTH_SHORT ).show();
                    }
                });
        mPlace1 = new MarkerOptions().position(new LatLng(lati, longi)).title("Location 1");
        mPlace2 = new MarkerOptions().position(new LatLng(citylati, citylongi)).title("Location 2");
        //String url = getUrl(mPlace1.getPosition(), mPlace2.getPosition(), "driving");
        mGetDirection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mButton1.isChecked())
                    mode = "walking";
                if (mButton2.isChecked())
                    mode = "cycling";
                if (mButton3.isChecked())
                    mode = "driving";
                mPlace1 = new MarkerOptions().position(new LatLng(lat, lng)).title("Location 1");
                new FetchURL(Mapdisp.this).execute(getUrl(mPlace1.getPosition(),
                        mPlace2.getPosition(), mode), mode);
            }
        });


        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.mapNearBy);
        mapFragment.getMapAsync( this );
    }
    private String getUrl(LatLng origin, LatLng dest, String directionMode) {
        // Origin of route
        String strorigin = "origin=" + origin.latitude + "," + origin.longitude;
        // Destination of route
        String strdest = "destination=" + dest.latitude + "," + dest.longitude;
        // Mode
        String mode = "mode=" + directionMode;
        // Building the parameters to the web service
        String parameters = strorigin + "&" + strdest + "&" + mode;
        // Output format
        String output = "json";

        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters + "&key=" + "AIzaSyCZkX4ptMBwaNL87lWAGgkPzy_Nv7ivJec";
        return url;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.addMarker(mPlace1);
        mMap.addMarker(mPlace2);
    }

    @Override
    public void onTaskDone(Object... values) {
        if (mCurrentPolyline != null)
            mCurrentPolyline.remove();
        mCurrentPolyline = mMap.addPolyline((PolylineOptions) values[0]);
    }
}
