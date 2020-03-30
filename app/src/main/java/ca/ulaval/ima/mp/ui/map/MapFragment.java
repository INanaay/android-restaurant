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

import com.google.android.gms.common.api.Api;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.snackbar.Snackbar;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import ca.ulaval.ima.mp.ApiManager;
import ca.ulaval.ima.mp.MainActivity;
import ca.ulaval.ima.mp.R;
import ca.ulaval.ima.mp.Restaurant;

import static android.content.Context.LOCATION_SERVICE;

public class MapFragment extends Fragment {
    private Callback _getRestaurantsCallback;
    private View view;
    private GoogleMap mMap;
    LocationManager _locationManager;
    Location _location;
    ArrayList<Restaurant> _restaurantsList;

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
        _restaurantsList = new ArrayList<>();

        _getRestaurantsCallback = new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                showSnackbar("Une erreur est survenue.");
            }

            @Override
            public void onResponse(Response response) throws IOException {
                onGetRestaurantSuccess(response);
            }
        };



        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.mapFragment);
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                mMap = googleMap;
                setLocation();
                if (_location == null) {
                    showSnackbar("Couldn't find your location.");
                    return;
                }

                ApiManager.getInstance().getCloseRestaurants(_location, _getRestaurantsCallback);


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

    private void showSnackbar(String message) {
        Snackbar.make( view, message, Snackbar.LENGTH_SHORT)
                .show();
    }

    private void onGetRestaurantSuccess(Response response) throws IOException {
        try {
            JSONObject jsonResponse = new JSONObject(response.body().string());
            JSONObject jsonContent = new JSONObject(jsonResponse.getString("content"));
            JSONArray array = jsonContent.getJSONArray("results");

            for (int i = 0; i < array.length(); i++) {
                Log.i("JSON", array.getString(i));
                JSONObject jsonObject = new JSONObject(array.getString(i));
                final String id = jsonObject.getString("id");
                final String name = jsonObject.getString("name");

                JSONArray kitchenArray = jsonObject.getJSONArray("cuisine");
                JSONObject kitchenJson = new JSONObject(kitchenArray.getString(0));
                final String kitchenId = kitchenJson.getString("id");
                final String kitchen = kitchenJson.getString("name");
                final String reviewCount = jsonObject.getString("review_count");
                final String reviewAverage = jsonObject.getString("review_average");
                final String image =  jsonObject.getString("image");
                JSONObject locationJson = new JSONObject(jsonObject.getString("location"));
                final String latitude = locationJson.getString("latitude");
                final String longitutde = locationJson.getString("longitude");

                Location location = new Location("me");
                location.setLatitude(Double.parseDouble(latitude));
                location.setLongitude(Double.parseDouble(longitutde));

                final Restaurant restaurant = new Restaurant(id, name, location, reviewCount,
                        reviewAverage, image, kitchenId, kitchen);

                view.post(new Runnable() {
                    @Override
                    public void run() {
                        showRestaurant(restaurant.get_location(), restaurant.get_name());

                    }
                });

                _restaurantsList.add(restaurant);
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void showRestaurant(Location location, String name) {

            LatLng marker = new LatLng(location.getLatitude(), location.getLongitude());
            mMap.addMarker(new MarkerOptions().position(marker).title(name));
    }
}
