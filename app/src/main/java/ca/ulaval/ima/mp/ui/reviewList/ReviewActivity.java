package ca.ulaval.ima.mp.ui.reviewList;

import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import ca.ulaval.ima.mp.ApiManager;
import ca.ulaval.ima.mp.R;
import ca.ulaval.ima.mp.Review;

public class ReviewActivity extends AppCompatActivity {

    ReviewAdapter reviewAdapter;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.review_list);
        listView = (ListView) findViewById(R.id.rv_list2);
        reviewAdapter = new ReviewAdapter(getApplicationContext(), R.layout.adapter_view_layout);
        listView.setAdapter(reviewAdapter);
        final String RestaurantID = getIntent().getStringExtra("id");

        ApiManager.getInstance().getRestaurantReview(RestaurantID, new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {

            }
            @Override
            public void onResponse(Response response) throws IOException {
                if (response.code() != 200) {
                    Log.d("Debug review list", response.message());
                }
                else {
                    String my_response = response.body().string();
                    try {
                        JSONObject jsonObject = new JSONObject(my_response);
                        final JSONObject content = new JSONObject(jsonObject.getString("content"));
                        JSONArray jsonArray = content.getJSONArray("results");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            final JSONObject object = jsonArray.getJSONObject(i);
                            System.out.println(object.toString());
                            final String first_name = object.getJSONObject("creator").getString("first_name");
                            final String last_name = object.getJSONObject("creator").getString("last_name");
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        Review review = new Review(first_name, last_name, object.getString("stars"), object.getString("image"), object.getString("comment"), object.getString("date"));
                                        reviewAdapter.add(review);
                                    }
                                    catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
}
