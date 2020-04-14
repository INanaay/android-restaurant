package ca.ulaval.ima.mp.ui.reviewRestaurant;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import ca.ulaval.ima.mp.ApiManager;
import ca.ulaval.ima.mp.R;
import ca.ulaval.ima.mp.ui.reviewRestaurant.dummy.ReviewDummyContent;

public class ReviewActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.review_activity);
        final String RestaurantID = getIntent().getStringExtra("id");
        final TextView nb_review = findViewById(R.id.nb_eval);

        ApiManager.getInstance().getRestaurantReview(RestaurantID, new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {

            }

            @Override
            public void onResponse(Response response) throws IOException {
                if (response.isRedirect()) {
                    String my_response = response.body().string();
                    try {
                        JSONObject jsonObject = new JSONObject(my_response);
                        nb_review.setText(jsonObject.getString("count"));
                        JSONArray jsonArray = jsonObject.getJSONArray("content").getJSONArray(Integer.parseInt("results"));
                        ReviewDummyContent.ITEMS.clear();
                        ReviewDummyContent.ITEM_MAP.clear();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);
                            final String first_name = object.getJSONObject("creator").getString("first_name");
                            final String last_name = object.getJSONObject("creator").getString("last_name");
                            ReviewDummyContent.addItem(ReviewDummyContent.createDummyItem(object.getString("id"), first_name, last_name, object.getString("stars"), object.getString("img"), object.getString("comment"), object.getString("date")));
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    getSupportFragmentManager().beginTransaction()
                                            .replace(R.id.container, ReviewRestaurantFragment.newInstance(1))
                                            .commitNow();
                                }
                            });
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, ReviewRestaurantFragment.newInstance(1))
                    .commitNow();
        }
    }
}
