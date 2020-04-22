package ca.ulaval.ima.mp;

import android.app.Fragment;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.squareup.okhttp.Response;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import ca.ulaval.ima.mp.ui.account.LoginRegisterFragment;
import ca.ulaval.ima.mp.ui.restaurantDetails.DetailsRestaurant;
import ca.ulaval.ima.mp.ui.reviewList.ProfilFragment;

public class MainActivity extends AppCompatActivity implements LoginRegisterFragment.ILoginRegisterListener, IRestaurantHandler {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("Activity", "create");
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_resto_list, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    @Override
    public void login() {
        ProfilFragment fragment = new ProfilFragment();
        getFragmentManager().beginTransaction().replace(R.id.login_container, fragment, fragment.getTag()).commit();
    }

    @Override
    public void navigateToRestaurantDetails(String id, String latitude, String longitude) {
        Intent intent= new Intent(this, DetailsRestaurant.class);
        intent.putExtra("id", id);
        intent.putExtra("latitude", latitude);
        intent.putExtra("longitude", longitude);

        startActivity(intent);
    }

    @Override
    public ArrayList<Restaurant> parseRestaurantJson(Response response) throws JSONException {
        ArrayList<Restaurant> restaurants = new ArrayList<>();
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
                final String image = jsonObject.getString("image");
                final String distance = jsonObject.getString("distance");
                JSONObject locationJson = new JSONObject(jsonObject.getString("location"));
                final String latitude = locationJson.getString("latitude");
                final String longitutde = locationJson.getString("longitude");

                Location location = new Location("me");
                location.setLatitude(Double.parseDouble(latitude));
                location.setLongitude(Double.parseDouble(longitutde));

                final Restaurant restaurant = new Restaurant(id, name, location, reviewCount,
                        reviewAverage, image, kitchenId, kitchen, distance);

                restaurants.add(restaurant);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return restaurants;
    }
}
