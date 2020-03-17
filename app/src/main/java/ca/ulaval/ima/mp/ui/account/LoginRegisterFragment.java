package ca.ulaval.ima.mp.ui.account;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import ca.ulaval.ima.mp.MainActivity;
import ca.ulaval.ima.mp.R;

public class LoginRegisterFragment extends Fragment {
    private boolean isLogin = true;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MainActivity csActivity      = (MainActivity)getActivity();
        csActivity.getSupportActionBar().hide();

        View view = inflater.inflate(R.layout.fragment_login, container, false);
        if (isLogin) {
            setLoginView(view);
        }

        return view;
    }

    private void setLoginView(View view) {
        TextView text = view.findViewById(R.id.login_text);
        text.setText(R.string.login);

        EditText emailInput = view.findViewById(R.id.email_login_textinput);
        emailInput.setHint("Courriel");

        EditText passwordInput = view.findViewById(R.id.passord_login_textinput);
        passwordInput.setHint("Mot de passe");


    }
}
