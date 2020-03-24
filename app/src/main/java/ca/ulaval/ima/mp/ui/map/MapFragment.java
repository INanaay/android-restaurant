package ca.ulaval.ima.mp.ui.map;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

import ca.ulaval.ima.mp.MainActivity;
import ca.ulaval.ima.mp.R;

import static android.content.Context.LOCATION_SERVICE;

public class MapFragment extends Fragment {
    private View view;
    private GoogleMap mMap;
    LocationManager _locationManager;
    Location _location;

    final private int REQUEST_CODE_ASK_PERMISSIONS = 123;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        _locationManager = (LocationManager) getActivity().getSystemService(LOCATION_SERVICE);

    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        MainActivity activity = (MainActivity)getActivity();
        activity.getSupportActionBar().show();

        view = inflater.inflate(R.layout.fragment_map, container, false);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.mapFragment);
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                mMap = googleMap;
                setLocation();
                if (_location == null) {
                    Snackbar.make( view,"Couldn't find your location." , Snackbar.LENGTH_SHORT)
                            .show();
                    return;
                }


                // Add a marker in Sydney and move the camera
                LatLng sydney = new LatLng(_location.getLatitude(), _location.getLongitude());
                mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
            }
        });


        return view;

    }

    private void setLocation() {
        if ( Build.VERSION.SDK_INT >= 23){
            if (ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) !=
                    PackageManager.PERMISSION_GRANTED  ){
                requestPermissions(new String[]{
                                android.Manifest.permission.ACCESS_FINE_LOCATION},
                        REQUEST_CODE_ASK_PERMISSIONS);
                return ;
            }
        }

        getLocation();


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSIONS:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getLocation();
                } else {
                    // Permission Denied
                    Snackbar.make( view,"You need to set your location permissions." , Snackbar.LENGTH_SHORT)
                            .show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @SuppressLint("MissingPermission")
    public  void getLocation(){
        _locationManager = (LocationManager)getActivity().getApplicationContext().getSystemService(LOCATION_SERVICE);
        List<String> providers = _locationManager.getProviders(true);
        Location bestLocation = null;
        for (String provider : providers) {
            Log.i("Provider", provider.toString());
            Location l = _locationManager.getLastKnownLocation(provider);
            if (l == null) {
                continue;
            }
            if (bestLocation == null || l.getAccuracy() < bestLocation.getAccuracy()) {
                // Found best last known location: %s", l);
                bestLocation = l;
            }
        }
        if (bestLocation == null) {
            Log.i("NULL", "+++++++ NULL +++++");
        }
        _location = bestLocation;
    }
}
