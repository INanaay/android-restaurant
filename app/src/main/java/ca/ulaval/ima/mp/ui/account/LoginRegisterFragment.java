package ca.ulaval.ima.mp.ui.account;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.google.android.material.snackbar.Snackbar;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

import ca.ulaval.ima.mp.ApiManager;
import ca.ulaval.ima.mp.MainActivity;
import ca.ulaval.ima.mp.R;

public class LoginRegisterFragment extends Fragment {

    public interface ILoginRegisterListener {
        void login(String email, String password);
    }

    private boolean isLogin = true;
    private View view;
    private Callback _onLoginCallback;
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
        if (isLogin) {
            setLoginView(view);
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

    private void setLoginView(View view) {
        TextView text = view.findViewById(R.id.login_text);
        text.setText(R.string.login);

        final EditText emailInput = view.findViewById(R.id.email_login_textinput);
        emailInput.setHint("Courriel");

        final EditText passwordInput = view.findViewById(R.id.passord_login_textinput);
        passwordInput.setHint("Mot de passe");

        Button loginButton = view.findViewById(R.id.login_button);
        loginButton.setText(R.string.loginButton);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailInput.getText().toString();
                String password = passwordInput.getText().toString();

                if (email.isEmpty() || password.isEmpty()) {
                    showSnackbar("One of the fields is empty");
                }
                else {
                    ApiManager.getInstance().login(email, password, _onLoginCallback);
                }

            }
        });

        _onLoginCallback = new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                showSnackbar("Probleme de connexion.");
            }

            @Override
            public void onResponse(Response response) throws IOException {
                Log.i("Response Login", response.body().string());
            }
        };
    }

    private void showSnackbar(String message) {
        Snackbar snackBar = Snackbar.make(view.findViewById(R.id.login_container),
                message, Snackbar.LENGTH_LONG);
        snackBar.show();
    }


}
