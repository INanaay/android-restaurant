package ca.ulaval.ima.mp;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.util.Log;

import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.HttpUrl;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;

import static android.content.Context.MODE_PRIVATE;

public class ApiManager extends OkHttpClient{

    private String _url = "https://kungry.ca/api/v1";
    private String _clientId = "STO4WED2NTDDxjLs8ODios5M15HwsrRlydsMa1t0";
    private String _clientSecret = "YOVWGpjSnHd5AYDxGBR2CIB09ZYM1OPJGnH3ijkKwrUMVvwLprUmLf6fxku06ClUKTAEl5AeZN36V9QYBYvTtrLMrtUtXVuXOGWleQGYyApC2a469l36TdlXFqAG1tpK";
    private SharedPreferences _pref;
    private SharedPreferences.Editor _editor;

    private static final ApiManager ourInstance = new ApiManager();

    public static ApiManager getInstance() {
        return ourInstance;
    }

    public void setToken(String token, Context context) {

        _pref = context.getSharedPreferences("prefs", context.MODE_PRIVATE);
        _editor = _pref.edit();

        _editor.putString("token", token);
        _editor.commit();
    }

    public String getToken() {
        return _pref.getString("token", null);
    }

    private ApiManager() {
    }


    public void register(String email, String password, Callback callback) {
        String content = "{\"email\":\"" + email + "\","
                + "\"password\":\"" + password + "\","
                + "\"first_name\":\"" + "Nathan" + "\","
                + "\"last_name\":\"" + "Lebon" + "\","
                + "\"client_id\":\"" + _clientId + "\","
                + "\"client_secret\":\"" + _clientSecret + "\""
                + "}";

        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(JSON, content);

        Request request = new Request.Builder()
                .url(_url + "/account")
                .post(body)
                .build();

        newCall(request).enqueue(callback);
    }

    public void login(String email, String password, Callback callback) {
        Log.i("Login", "tryikng");
        String content = "{\"email\":\"" + email + "\","
                + "\"password\":\"" + password + "\","
                + "\"first_name\":\"" + "Nathan" + "\","
                + "\"last_name\":\"" + "Lebon" + "\","
                + "\"client_id\":\"" + _clientId + "\","
                + "\"client_secret\":\"" + _clientSecret + "\""
                + "}";

        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(JSON, content);

        Request request = new Request.Builder()
                .url(_url + "/account/login")
                .post(body)
                .build();

        newCall(request).enqueue(callback);
    }

    public void getRestaurantDetails(int restaurantId, Callback callback) {
        HttpUrl.Builder urlBuilder = HttpUrl.parse(_url + "/restaurant/" + restaurantId).newBuilder();
        String url = urlBuilder.build().toString();

        Request request = new Request.Builder()
                .url(url)
                .build();

        newCall(request).enqueue(callback);
    }

    public void getRestaurantReview(String restaurantId, Callback callback) {
        HttpUrl.Builder urlBuilder = HttpUrl.parse(_url + "/restaurant/" + restaurantId + "/reviews/").newBuilder();
        String url = urlBuilder.build().toString();

        Request request = new Request.Builder()
                .url(url)
                .build();
        newCall(request).enqueue(callback);
    }

    public void postReviewWithoutPicture(String content, Callback callback) {
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(JSON, content);
        final Request request = new Request.Builder()
                .url(_url + "/review/")
                .post(body)
                .addHeader("Authorization", getToken())
                .build();

        newCall(request).enqueue(callback);
    }

    public void getCloseRestaurants(Location _location, Callback callback) {

        HttpUrl.Builder urlBuilder = HttpUrl.parse(_url + "/restaurant").newBuilder();

        //urlBuilder.addQueryParameter("latitude", String.valueOf(location.getLatitude()));
        //urlBuilder.addQueryParameter("longitude", String.valueOf(location.getLongitude()));
        //urlBuilder.addQueryParameter("radius", "30");

        String url = urlBuilder.build().toString();

        Request request = new Request.Builder()
                .url(url)
                .build();

        newCall(request).enqueue(callback);
    }
}
