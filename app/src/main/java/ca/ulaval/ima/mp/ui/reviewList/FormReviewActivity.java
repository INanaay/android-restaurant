package ca.ulaval.ima.mp.ui.reviewList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
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
import okhttp3.Call;

public class FormReviewActivity extends AppCompatActivity {

    private static final int PERMISSION_REQUEST_CODE = 1;
    private String restaurant_id;
    private RatingBar ratingBar;
    private EditText editText;
    private String comment;
    private ImageView uploadimg;
    private Button submitbutton;
    private String file_path;
    private float stars;
    private static final int RESULT_LOAD_IMAGE = 200;


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
        submitbutton = findViewById(R.id.ev_submit);
        uploadimg = findViewById(R.id.ev_uploadimg);
        ratingBar = findViewById(R.id.ev_ratingcard);
        editText = findViewById(R.id.ev_mycommentaire);

        uploadimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= 23) {
                    if (checkPermission()) {
                        filePicker();
                    } else {
                        requestPermission();
                    }
                } else {
                    filePicker();
                }
            }
        });

        submitbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stars = ratingBar.getRating();
                comment = editText.getText().toString();

                if (file_path != null && stars > 0 && comment.length() > 0) {
                    SendMyReviewWithPicture();
                }
                else if (stars > 0 && comment.length() > 0) {
                    SendMyReviewWithoutPicture();
                } else {
                    h.sendEmptyMessage(1);
                }
            }
        });


    };

    private void filePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, RESULT_LOAD_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && data != null) {
            String filePath = getRealPathFromUri(data.getData(), this);
            this.file_path = filePath;
            uploadimg.setImageURI(data.getData());
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void requestPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(FormReviewActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            h.sendEmptyMessage(3);
        }
        else {
            ActivityCompat.requestPermissions(FormReviewActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(FormReviewActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    h.sendEmptyMessage(4);
                } else {
                    h.sendEmptyMessage(5);
                }
        }
    }

    public String getRealPathFromUri(Uri uri, Activity activity) {
        Cursor cursor = activity.getContentResolver().query(uri, null, null, null, null);
        if (cursor == null) {
            return uri.getPath();
        }
        else {
            cursor.moveToFirst();
            int id = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(id);
        }
    }

    Handler h = new Handler() {
        public void handleMessage(Message msg) {
            if(msg.what == 0) {
                Toast.makeText(getApplicationContext(), "Review publié", Toast.LENGTH_SHORT).show();
            } else if (msg.what == 1) {
                Toast.makeText(getApplicationContext(), "Veuillez remplir le formulaire", Toast.LENGTH_SHORT).show();
            } else if (msg.what == 2) {
                Toast.makeText(getApplicationContext(), "La publication avec une image à réussi", Toast.LENGTH_SHORT).show();
            } else if (msg.what == 3) {
                Toast.makeText(getApplicationContext(), "Donner les permissions pour l'upload de photo", Toast.LENGTH_SHORT).show();
            } else if (msg.what == 4) {
                Toast.makeText(getApplicationContext(), "Permissions accordée", Toast.LENGTH_SHORT).show();
            } else if (msg.what == 5) {
                Toast.makeText(getApplicationContext(), "Permissions refusée", Toast.LENGTH_SHORT).show();
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
                if (response.code() == 201) {
                    h.sendEmptyMessage(0);
                }
            }
        });
    }

    private void SendMyReviewWithPicture() {
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

            }
            @Override
            public void onResponse(Response response) throws IOException {
                if (response.code() == 201) {
                    String my_response = response.body().string();
                    try {
                        JSONObject jsonObject = new JSONObject(my_response);
                        JSONObject content = new JSONObject(jsonObject.getString("content"));
                        ApiManager.getInstance().postReviewWithPicture(content.getString("id"), file_path, new okhttp3.Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {
                                e.printStackTrace();
                            }

                            @Override
                            public void onResponse(Call call, okhttp3.Response response) throws IOException {
                                if (response.code() == 201) {
                                    h.sendEmptyMessage(2);
                                }
                            }
                        });
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }
}
