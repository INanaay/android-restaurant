package ca.ulaval.ima.mp.ui.reviewList;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Toast;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import ca.ulaval.ima.mp.ApiManager;
import ca.ulaval.ima.mp.R;

public class FormReviewActivity extends AppCompatActivity {

    private String restaurant_id;
    private RatingBar ratingBar;
    private EditText editText;
    private String comment;
    private float stars;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_review);
        getSupportActionBar().hide();
        ImageView imageView = (ImageView) findViewById(R.id.back_btn);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        restaurant_id = getIntent().getStringExtra("id");
        Button button = findViewById(R.id.ev_submit);
        ratingBar = findViewById(R.id.ev_ratingcard);
        editText = findViewById(R.id.ev_mycommentaire);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stars = ratingBar.getRating();
                comment = editText.getText().toString();

                if (stars > 0 && comment.length() > 0) {
                    SendMyReviewWithoutPicture();
                } else {
                    h.sendEmptyMessage(1);
                }
            }
        });


    }

    Handler h = new Handler() {
        public void handleMessage(Message msg) {
            if(msg.what == 0) {
                Toast.makeText(getApplicationContext(), "Review publié", Toast.LENGTH_SHORT).show();
            } else if (msg.what == 1) {
                Toast.makeText(getApplicationContext(), "Veuillez remplir le formulaire", Toast.LENGTH_SHORT).show();
            } else if (msg.what == 2) {
                Toast.makeText(getApplicationContext(), "La publication a échoué", Toast.LENGTH_SHORT).show();
            }
        }
    };

    private void SendMyReviewWithoutPicture() {
        JSONObject content = new JSONObject();
        try {
            content.put("restaurant_id", restaurant_id);
            content.put("stars", stars);
            content.put("comment", comment);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        ApiManager.getInstance().postReviewWithoutPicture(content.toString(), new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                h.sendEmptyMessage(2);
            }

            @Override
            public void onResponse(Response response) throws IOException {
                h.sendEmptyMessage(0);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }
}
