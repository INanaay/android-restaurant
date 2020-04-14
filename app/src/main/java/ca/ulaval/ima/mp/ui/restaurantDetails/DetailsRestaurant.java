package ca.ulaval.ima.mp.ui.restaurantDetails;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import ca.ulaval.ima.mp.ApiManager;
import ca.ulaval.ima.mp.R;
import ca.ulaval.ima.mp.ui.restorantList.RestaurantListAdapter;

public class DetailsRestaurant extends AppCompatActivity {
    private String restaurantId;
    private TextView restoName;
    /*TextView restoCategory = findViewById(R.id.txt_type);
    TextView restoNumberReview = findViewById(R.id.textcount);
    ImageView restoImage = findViewById(R.id.img);*/
    String resto;
    private Callback _restaurantDetailsCallback;

    private void initView(JSONObject data) {
        try {
            //Picasso.get().load(resto.getString("image")).into(restoImage);
            restoName.setText(data.getString("name"));
            /* JSONArray cuisine = resto.getJSONArray("cuisine");
            JSONObject type = cuisine.getJSONObject(0);
            restoCategory.setText(type.getString("name"));
            restoNumberReview.setText("( ".concat(resto.getString("review_count")).concat(" )"));*/
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_restaurant);
        Intent intent = getIntent();
        restaurantId = intent.getStringExtra("id");
        restoName = findViewById(R.id.txt_title);
        String latitude = intent.getStringExtra("latitude");
        String longitude = intent.getStringExtra("longitude");
        _restaurantDetailsCallback = new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                Log.d("Debug", "Can't get details list");
            }

            @Override
            public void onResponse(Response response) throws IOException {
                if (response.code() != 200) {
                    Log.d("debug resto list", response.message());
                }
                else {
                    try {
                        resto = response.body().string();
                        JSONObject jsonResponse = new JSONObject(resto);
                        Log.d("response de la vie", jsonResponse.toString());
                        JSONObject jsonContent = new JSONObject(jsonResponse.getString("content"));
                        initView(jsonContent);
                    } catch (Exception e) {
                        Log.d("Error qui fais chier ", e.toString());
                    }
                }
            }
        };
        try {
            ApiManager.getInstance().getRestaurantDetails(Integer.parseInt(restaurantId), _restaurantDetailsCallback);
        } catch (Exception e) {
            Log.d("Error", "Can't mke the Api call");
        }
    }
}
