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
import android.widget.Button;
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
import ca.ulaval.ima.mp.ui.reviewList.ReviewActivity;

public class DetailsRestaurant extends AppCompatActivity {
    private int restaurantId;
    TextView restoName;
    TextView restoCategory;
    TextView restoNumberReview;
    ImageView restoImage;
    JSONObject resto;
    private Callback _restaurantDetailsCallback;

    private void initView() {
        try {
            Picasso.get().load(resto.getString("image")).into(restoImage);
            restoName.setText(resto.getString("name"));
            JSONArray cuisine = resto.getJSONArray("cuisine");
            JSONObject type = cuisine.getJSONObject(0);
            restoCategory.setText(type.getString("name"));
            restoNumberReview.setText("( ".concat(resto.getString("review_count")).concat(" )"));
            restaurantId = Integer.parseInt(resto.getString("id"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_restaurant);
        Intent intent = getIntent();
        String extra = intent.getStringExtra("id");
        String latitude = intent.getStringExtra("latitude");
        String longitude = intent.getStringExtra("longitude");
        Log.d("zednzeiof", extra + "   " + latitude);
        Button button = findViewById(R.id.rv_btn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent review = new Intent(getApplicationContext(), ReviewActivity.class);
                review.putExtra("id", "1");
                startActivity(review);
            }
        });
        /* try {
            resto = new JSONObject(extra);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        restoImage = findViewById(R.id.resto_detail_image);
        restoName = findViewById(R.id.resto_detail_name);
        restoCategory = findViewById(R.id.resto_detail_category);
        restoNumberReview = findViewById(R.id.resto_detail_review_number);
        initView();
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
                    Log.d("salut la compagnie", response.body().string());
                }
            }
        };
        ApiManager.getInstance().getRestaurantDetails(restaurantId, _restaurantDetailsCallback);
        try {
            Picasso.get().load(resto.getString("image")).into(restoImage);
            restoName.setText(resto.getString("name"));
            JSONArray cuisine = resto.getJSONArray("cuisine");
            JSONObject type = cuisine.getJSONObject(0);
            restoCategory.setText(type.getString("name"));
            restoNumberReview.setText("( ".concat(resto.getString("review_count")).concat(" )"));
        } catch (JSONException e) {
            e.printStackTrace();
        }*/
    }
}
