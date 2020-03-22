package ca.ulaval.ima.mp.ui.account;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.google.android.material.snackbar.Snackbar;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import ca.ulaval.ima.mp.ApiManager;
import ca.ulaval.ima.mp.MainActivity;
import ca.ulaval.ima.mp.R;

public class LoginRegisterFragment extends Fragment {

    public interface ILoginRegisterListener {
        void login(String email, String password);
    }

    private boolean _isLogin = true;
    private View view;
    private Callback _onLoginCallback;
    private Callback _onRegisterCallback;
    private TextView _loginText;
    private EditText _emailInput;
    private EditText _passwordInput;
    private Button  _loginButton;
    private TextView _changeFragmentText1;
    private TextView _changeFragmentText2;
    private LinearLayout _changeFragmentButton;
    public ILoginRegisterListener callback;


    public void setILoginRegisterListener(ILoginRegisterListener callback) {
        this.callback = callback;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MainActivity csActivity      = (MainActivity)getActivity();
        csActivity.getSupportActionBar().hide();

        view = inflater.inflate(R.layout.fragment_login, container, false);
        _loginText = view.findViewById(R.id.login_text);
        _emailInput = view.findViewById(R.id.email_login_textinput);
        _passwordInput = view.findViewById(R.id.passord_login_textinput);
        _loginButton = view.findViewById(R.id.login_button);
        _changeFragmentText1 = view.findViewById(R.id.change_login_fragment_text1);
        _changeFragmentText2 = view.findViewById(R.id.change_login_fragment_text2);
        _changeFragmentButton = view.findViewById(R.id.change_login_fragment_button);

        _onLoginCallback = new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                showSnackbar("Probleme de création.");
            }

            @Override
            public void onResponse(Response response) throws IOException {
                if (response.code() != 200) {
                    showSnackbar(response.message());
                }
                else {
                    showSnackbar("Connecté.");
                    getTokenFromResponse(response);
                }
            }
        };

        _onRegisterCallback = new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                showSnackbar("Probleme de connexion.");
            }

            @Override
            public void onResponse(Response response) throws IOException {
                if (response.code() == 409) {
                    showSnackbar("L'utilisateur existe déjà.");
                }
                else if (response.code() != 200) {
                    showSnackbar(response.message());
                }
                else {
                    showSnackbar("Enregistré avec succès.");
                    getTokenFromResponse(response);
                }
            }
        };

        if (_isLogin) {
            setLoginView();
        }

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ILoginRegisterListener) {
            this.setILoginRegisterListener((ILoginRegisterListener) context);
        }
    }

    private boolean checkInputs(String email, String password) {
        if (email.isEmpty() || password.isEmpty()) {
            showSnackbar("One of the fields is empty");
            return false;
        }
        return true;
    }

    private void setRegisterView() {
        _loginText.setText(R.string.register);
        _loginButton.setText("M’INSCRIRE");

        _changeFragmentText1.setText("Déjà abonnée?");
        _changeFragmentText2.setText("Me connecter");

        _loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = _emailInput.getText().toString();
                String password = _passwordInput.getText().toString();

                if (checkInputs(email, password))
                    ApiManager.getInstance().register(email, password, _onRegisterCallback);

            }
        });

        _changeFragmentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _isLogin = !_isLogin;
                setLoginView();
            }
        });



    }

    private void setLoginView() {

        _loginText.setText(R.string.login);
        _loginButton.setText(R.string.loginButton);

        _changeFragmentText1.setText("Nouveau avec Kungry?");
        _changeFragmentText2.setText("M’inscrire");


        _loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = _emailInput.getText().toString();
                String password = _passwordInput.getText().toString();

               if (checkInputs(email, password))
                    ApiManager.getInstance().login(email, password, _onLoginCallback);

            }
        });

        _changeFragmentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _isLogin = !_isLogin;
                setRegisterView();
            }
        });


    }

    private void showSnackbar(String message) {
        Snackbar snackBar = Snackbar.make(view.findViewById(R.id.login_container),
                message, Snackbar.LENGTH_LONG);
        snackBar.show();
    }

    private String getTokenFromResponse(Response response) throws IOException {
        try {
            JSONObject jsonResponse = new JSONObject(response.body().string());
            JSONObject jsonContent = new JSONObject(jsonResponse.getString("content"));
            String token = jsonContent.getString("access_token");

            ApiManager.getInstance().setToken(token, getContext());

            } catch (JSONException e) {
            e.printStackTrace();
        }
        return "";
    }
}
