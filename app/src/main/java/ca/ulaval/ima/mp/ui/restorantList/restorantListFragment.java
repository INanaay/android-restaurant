package ca.ulaval.ima.mp.ui.restorantList;

import android.location.Location;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.ArrayList;
import com.squareup.okhttp.Callback;

import ca.ulaval.ima.mp.ApiManager;
import ca.ulaval.ima.mp.MainActivity;
import ca.ulaval.ima.mp.R;

public class restorantListFragment extends Fragment {
    RestaurantListAdapter adapter;

    private View view;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private Callback _restaurantListCallback;

    public restorantListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void setRecyclerView(Response response) {
       getActivity().runOnUiThread(new Runnable() {
           @Override
           public void run() {

           }
       });
        // data to populate the RecyclerView with
        ArrayList<String> animalNames = new ArrayList<>();
        animalNames.add("Horse");
        animalNames.add("Cow");
        animalNames.add("Camel");
        animalNames.add("Sheep");
        animalNames.add("Goat");


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        MainActivity activity = (MainActivity)getActivity();
        activity.getSupportActionBar().hide();
        super.onCreate(savedInstanceState);
        view = inflater.inflate(R.layout.fragment_restorant_list, container, false);
        recyclerView = view.findViewById(R.id.recyclerView_resto_list);

        _restaurantListCallback = new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                Log.d("salut", "cant get restaurant list");
            }

            @Override
            public void onResponse(final Response response) throws IOException {
                if (response.code() != 200) {
                    Log.d("debug resto list", response.message());
                }
                else {
                    getActivity().runOnUiThread(new Runnable() {
                        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                        @Override
                        public void run() {
                            recyclerView.setHasFixedSize(true);
                            // use a linear layout manager
                            layoutManager = new LinearLayoutManager(getActivity());
                            recyclerView.setLayoutManager(layoutManager);
                            try {
                                adapter = new RestaurantListAdapter(getActivity(), response.body().string());
                                adapter.setClickListener(new RestaurantListAdapter.ItemClickListener() {
                                    @Override
                                    public void onItemClick(View view, int position) {
                                        Log.d("passe", String.valueOf(position));
                                    }
                                });
                                recyclerView.setAdapter(adapter);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                    setRecyclerView(response);
                }
            }
        };

        Location location = new Location("me");
        ApiManager.getInstance().getCloseRestaurants(location,_restaurantListCallback);
        return view;
    }

}