package ca.ulaval.ima.mp.ui.reviewList;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import ca.ulaval.ima.mp.ApiManager;
import ca.ulaval.ima.mp.R;

public class ProfilFragment extends Fragment {

    private static final String ARG_PARAM1= "param1";
    private static final String ARG_PARAM2= "param2";

    private String mParam1;
    private String mParam2;

    public ProfilFragment() {

    }

    public static ProfilFragment newInstance(String param1, String param2){
        ProfilFragment fragment = new ProfilFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.profil_fragment, container, false);
        final TextView firstname = (TextView) view.findViewById(R.id.cp_firstname);
        final TextView lastname = (TextView) view.findViewById(R.id.cp_lastname);
        final TextView email = (TextView) view.findViewById(R.id.cp_email);
        final TextView nb_review = (TextView) view.findViewById(R.id.cp_nbreview);

        ApiManager.getInstance().getProfilInfo(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {

            }

            @Override
            public void onResponse(Response response) throws IOException {
                if (response.isSuccessful()) {
                    String my_response = response.body().string();
                    try {
                        JSONObject jsonObject = new JSONObject(my_response);
                        JSONObject content = new JSONObject(jsonObject.getString("content"));
                        firstname.setText(content.getString("first_name"));
                        lastname.setText(content.getString("last_name"));
                        email.setText(content.getString("email"));
                        nb_review.setText(content.getString("total_review_count"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        Button button = (Button) view.findViewById(R.id.cp_bottom_btn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ApiManager.getInstance().RemoveToken();
            }
        });
        return view;
    }
}
