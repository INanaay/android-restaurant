package ca.ulaval.ima.mp.ui.restaurantDetails;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
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
import ca.ulaval.ima.mp.Review;
import ca.ulaval.ima.mp.ui.restorantList.RestaurantListAdapter;
import ca.ulaval.ima.mp.ui.reviewList.ReviewActivity;
import ca.ulaval.ima.mp.ui.reviewList.ReviewAdapter;

public class DetailsRestaurant extends AppCompatActivity {
    private TextView restoName;
    private TextView restoNumberReview;
    private TextView restoType;
    private TextView restoTelNumber;
    private TextView restoDistance;
    private TextView restoLink;
    private TextView mon;
    private TextView tue;
    private TextView wed;
    private TextView thur;
    private TextView fri;
    private TextView sat;
    private TextView sun;
    private ImageView restoImg;
    private Callback _restaurantDetailsCallback;
    private ListView listView;
    private GoogleMap mMap;
    ReviewAdapter reviewAdapter;
    private TextView nb_review;
    private Button reviewButton;

    private void initView(final JSONObject data) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Picasso.get().load(data.getString("image")).into(restoImg);
                        restoName.setText(data.getString("name"));
                        JSONArray cuisine = data.getJSONArray("cuisine");
                        JSONObject type = cuisine.getJSONObject(0);
                        restoType.setText(type.getString("name"));
                        restoNumberReview.setText("( ".concat(data.getString("review_count")).concat(" )"));
                        JSONArray openingHours = data.getJSONArray("opening_hours");
                        sun.setText(openingHours.getJSONObject(0).getString("opening_hour") + " à " + openingHours.getJSONObject(0).getString("closing_hour"));
                        mon.setText(openingHours.getJSONObject(1).getString("opening_hour") + " à " + openingHours.getJSONObject(1).getString("closing_hour"));
                        tue.setText(openingHours.getJSONObject(2).getString("opening_hour") + " à " + openingHours.getJSONObject(2).getString("closing_hour"));
                        wed.setText(openingHours.getJSONObject(3).getString("opening_hour") + " à " + openingHours.getJSONObject(3).getString("closing_hour"));
                        thur.setText(openingHours.getJSONObject(4).getString("opening_hour") + " à " + openingHours.getJSONObject(4).getString("closing_hour"));
                        fri.setText(openingHours.getJSONObject(5).getString("opening_hour") + " à " + openingHours.getJSONObject(5).getString("closing_hour"));
                        sat.setText(openingHours.getJSONObject(6).getString("opening_hour") + " à " + openingHours.getJSONObject(6).getString("closing_hour"));
                        restoLink.setText(data.getString("website"));
                        restoDistance.setText(data.getString("distance"));
                        nb_review.setText(data.getString("review_count"));
                        JSONArray review_data = data.getJSONArray("reviews");
                        for (int i = 0; i < review_data.length(); i++) {
                            JSONObject object = review_data.getJSONObject(i);
                            final String first_name = object.getJSONObject("creator").getString("first_name");
                            final String last_name = object.getJSONObject("creator").getString("last_name");
                            Review review = new Review(first_name, last_name, object.getInt("stars"), object.getString("image"), object.getString("comment"), object.getString("date"));
                            reviewAdapter.add(review);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_restaurant);
        this.getSupportActionBar().hide();
        Intent intent = getIntent();
        final String restaurantId = intent.getStringExtra("id");
        final String latitude = intent.getStringExtra("latitude");
        final String longitude = intent.getStringExtra("longitude");
        listView = (ListView) findViewById(R.id.rv_list2);
        reviewAdapter = new ReviewAdapter(getApplicationContext(), R.layout.adapter_view_layout);
        listView.setAdapter(reviewAdapter);
        restoName = findViewById(R.id.txt_title);
        restoType = findViewById(R.id.txt_type);
        restoNumberReview = findViewById(R.id.textcount);
        restoTelNumber = findViewById(R.id.textcall);
        restoImg = findViewById(R.id.img);
        restoLink = findViewById(R.id.textlink);
        restoDistance = findViewById(R.id.textdistance);
        nb_review = findViewById(R.id.nb_reviewdetail);
        mon = findViewById(R.id.t11);
        tue = findViewById(R.id.t22);
        wed = findViewById(R.id.t33);
        thur = findViewById(R.id.t44);
        fri = findViewById(R.id.t55);
        sat = findViewById(R.id.t66);
        sun = findViewById(R.id.t77);
        reviewButton = findViewById(R.id.rv_bottom_btn);

        reviewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(DetailsRestaurant.this, ReviewActivity.class);
                intent.putExtra("id", restaurantId);
                startActivity(intent);
            }
        });
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
                        JSONObject jsonResponse = new JSONObject(response.body().string());
                        JSONObject jsonContent = new JSONObject(jsonResponse.getString("content"));
                        initView(jsonContent);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        try {
            ApiManager.getInstance().getRestaurantDetails(Integer.parseInt(restaurantId), _restaurantDetailsCallback);
        } catch (Exception e) {
            Log.d("Error", "Can't mke the Api call");
        }
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapFragmentThumb);
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                mMap = googleMap;
                LatLng marker = new LatLng(Double.parseDouble(latitude), Double.parseDouble(longitude));
                Marker mapMarker = mMap.addMarker(new MarkerOptions().position(marker).icon(bitmapDescriptorFromVector(DetailsRestaurant.this, R.drawable.ic_map_marker)));
            }
        });
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
