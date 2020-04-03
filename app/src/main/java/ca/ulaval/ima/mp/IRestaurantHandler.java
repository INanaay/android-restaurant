package ca.ulaval.ima.mp;

import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

public interface IRestaurantHandler {
    void navigateToRestaurantDetails(String id);
    ArrayList<Restaurant> parseRestaurantJson(Response response) throws JSONException;
}
