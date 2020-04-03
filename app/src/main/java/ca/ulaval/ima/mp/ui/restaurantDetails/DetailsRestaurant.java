package ca.ulaval.ima.mp.ui.restaurantDetails;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import ca.ulaval.ima.mp.R;

public class DetailsRestaurant extends AppCompatActivity {
    TextView restoName;
    TextView restoCategory;
    TextView restoNumberReview;
    ImageView restoImage;
    JSONObject resto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_restaurant);
        Intent intent = getIntent();
        String extra = intent.getStringExtra("id");
        String latitude = intent.getStringExtra("latitude");
        String longitude = intent.getStringExtra("longitude");
        Log.d("zednzeiof", extra + "   " + latitude);
        /* try {
            resto = new JSONObject(extra);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        restoImage = findViewById(R.id.resto_detail_image);
        restoName = findViewById(R.id.resto_detail_name);
        restoCategory = findViewById(R.id.resto_detail_category);
        restoNumberReview = findViewById(R.id.resto_detail_review_number);
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
