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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.review_activity);
        final ListView listView = findViewById(R.id.rv_list);
        final String RestaurantID = getIntent().getStringExtra("id");
        final TextView nb_review = findViewById(R.id.nb_eval);

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
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    nb_review.setText(content.getString("count"));
                                }
                                catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                        JSONArray jsonArray = jsonObject.getJSONArray("content");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);
                            JSONArray jsonArrayResults = object.getJSONArray("results");
                            for (int j = 0; j < jsonArrayResults.length(); j++) {
                                JSONObject results = jsonArrayResults.getJSONObject(j);
                                final String first_name = results.getJSONObject("creator").getString("first_name");
                                final String last_name = results.getJSONObject("creator").getString("last_name");
                                Review review = new Review(first_name, last_name, results.getString("stars"), results.getString("image"), results.getString("comment"), results.getString("date"));
                                ArrayList<Review> reviewArrayList = new ArrayList<>();
                                reviewArrayList.add(review);
                                ReviewListAdapter adapter = new ReviewListAdapter(getApplicationContext(), R.layout.adapter_view_layout, reviewArrayList);
                                listView.setAdapter(adapter);
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
}
