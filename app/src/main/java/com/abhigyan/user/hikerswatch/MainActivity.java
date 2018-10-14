package com.abhigyan.user.hikerswatch;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;


import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    LocationManager locationManager;
    LocationListener locationListener;
    TextView altText,lonText,latText, addressText;


    public void updateLocationInfo(Location location)// to log the location info
    {
        Log.i("Location Information-", location.toString());
        String latlng= "  Latitude- "+(float) location.getLatitude();
        String latlng2="  Longitude- "+(float)location.getLongitude();
        String altitude="  Altitude- "+(float)location.getAltitude();
        latText.setText(latlng);
        lonText.setText(latlng2);
        altText.setText(altitude);

        Geocoder geocoder= new Geocoder(getApplicationContext(), Locale.getDefault());

        try {
            List<Address> listAddress= geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);

            if(listAddress!=null && listAddress.size()>0)
            {
                Log.i("Location Info-", listAddress.get(0).toString());
                String countryName= listAddress.get(0).getCountryName();
                String stateName= listAddress.get(0).getAdminArea();
                String locality= listAddress.get(0).getFeatureName();
                String  countryCode= listAddress.get(0).getCountryCode();

                String str="  Address- "+locality+" ,"+listAddress.get(0).getSubAdminArea()+",\n                 "+stateName+",\n                 "+countryName+",\n                 "+countryCode+".";

                addressText.setText(str);

            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }



    }
    //xyz
    public void startListening()
    {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            //locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
            locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                startListening();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toast.makeText(this, "PLEASE TURN ON GPS FOR LOCATION!", Toast.LENGTH_LONG).show();

        altText=  findViewById(R.id.altText);
        lonText=  findViewById(R.id.lonText);
        latText=  findViewById(R.id.latText);
        addressText= findViewById(R.id.addressText);


        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

                updateLocationInfo(location);
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };


        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        } else {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

            Location location= locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            if(location!=null)
            {
                updateLocationInfo(location);
            }

        }


    }
}







