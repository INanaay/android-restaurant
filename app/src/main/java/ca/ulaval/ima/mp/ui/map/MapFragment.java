package ca.ulaval.ima.mp.ui.map;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.snackbar.Snackbar;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.squareup.picasso.Picasso;

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

public class MapFragment extends Fragment  {
    private Callback _getRestaurantsCallback;
    private View view;
    private GoogleMap mMap;
    LocationManager _locationManager;
    Location _location;
    ArrayList<Restaurant> _restaurantsList;
    Marker _lastClicked = null;
    BitmapDescriptor _defaultMarker;
    BitmapDescriptor _clickedMarker;
    ViewSwitcher _viewSwitcher;
    ImageView _restaurantImage;
    TextView _resturantName;
    TextView _restaurantType;

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

        _defaultMarker = bitmapDescriptorFromVector(getActivity(), R.drawable.ic_map_marker);
        _clickedMarker = bitmapDescriptorFromVector(getActivity(), R.drawable.ic_clicked_map_marker);
        _viewSwitcher = view.findViewById(R.id.mapViewSwitcher);
        _restaurantImage = view.findViewById(R.id.mapPopupImage);
        _resturantName = view.findViewById(R.id.mapPopupRestaurantName);
        _restaurantType = view.findViewById(R.id.mapPopupRestaurantType);

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

                ApiManager.getInstance().getCloseRestaurants(_getRestaurantsCallback);

                LatLng camera = new LatLng(_location.getLatitude(), _location.getLongitude());
                mMap.moveCamera(CameraUpdateFactory.newLatLng(camera));
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
        }
        _location = bestLocation;
    }

    private void showSnackbar(String message) {
        Snackbar.make( view, message, Snackbar.LENGTH_SHORT)
                .show();
    }

    private void onGetRestaurantSuccess(final Response response) throws IOException {
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
                        showRestaurant(restaurant);
                        _restaurantsList.add(restaurant);
                    }
                });


            }
            view.post(new Runnable() {
                @Override
                public void run() {
                    mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                        @Override
                        public boolean onMarkerClick(Marker marker) {
                            if (_lastClicked == null) {
                                _viewSwitcher.showNext();
                            }
                            else if (_lastClicked != null) {
                                _lastClicked.setIcon(_defaultMarker);

                            }
                            marker.setIcon(_clickedMarker);
                            _lastClicked = marker;
                            Restaurant restaurant = (Restaurant) marker.getTag();
                            Log.i("Name", restaurant.get_name());

                            _resturantName.setText(restaurant.get_name());
                            _restaurantType.setText(restaurant.get_kitchen());
                            Picasso.get().load(restaurant.get_image()).fit().centerCrop().into(_restaurantImage);

                            return false;
                        }
                    });
                }
            });


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void showRestaurant(Restaurant restaurant) {

            LatLng marker = new LatLng(restaurant.get_location().getLatitude(), restaurant.get_location().getLongitude());
            Marker mapMarker = mMap.addMarker(new MarkerOptions().position(marker).icon(bitmapDescriptorFromVector(getActivity(), R.drawable.ic_map_marker)));
            mapMarker.setTag(restaurant);
    }

    private BitmapDescriptor bitmapDescriptorFromVector(Context context, int vectorResId) {
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }
}
