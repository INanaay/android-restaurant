package ca.ulaval.ima.mp;

import android.util.Log;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.HttpUrl;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;

public class ApiManager extends OkHttpClient{

    private String _url = "http://kungry.ca/api/v1";
    private String _clientId = "STO4WED2NTDDxjLs8ODios5M15HwsrRlydsMa1t0";
    private String _clientSecret = "YOVWGpjSnHd5AYDxGBR2CIB09ZYM1OPJGnH3ijkKwrUMVvwLprUmLf6fxku06ClUKTAEl5AeZN36V9QYBYvTtrLMrtUtXVuXOGWleQGYyApC2a469l36TdlXFqAG1tpK";

    private static final ApiManager ourInstance = new ApiManager();

    public static ApiManager getInstance() {
        return ourInstance;
    }

    private ApiManager() {
    }

    public void login(String email, String password, Callback callback) {
        Log.i("Login", "tryikng");
        String content = "{\"email\":\"" + email + "\","
                + "\"password\":\"" + password + "\""
                + "\"client_id\":\"" + _clientId + "\""
                + "\"client_secret\":\"" + _clientSecret + "\""
                + "}";

        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(JSON, content);


        Request request = new Request.Builder()
                .url(_url + "/account/login/")
                .post(body)
                .build();

        newCall(request).enqueue(callback);
    }
}
