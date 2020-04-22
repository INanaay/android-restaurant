package ca.ulaval.ima.mp.ui.restorantList;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Parcelable;
import android.telecom.Call;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.okhttp.Response;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import ca.ulaval.ima.mp.MainActivity;
import ca.ulaval.ima.mp.R;
import ca.ulaval.ima.mp.ui.restaurantDetails.DetailsRestaurant;

public class RestaurantListAdapter extends RecyclerView.Adapter<RestaurantListAdapter.ViewHolder> {
    private JSONArray restoArray;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private int idClick;
    private Context mContext;


    // data is passed into the constructor
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    RestaurantListAdapter(Context context, String response) {
        this.mInflater = LayoutInflater.from(context);
        this.mContext = context;
        try {
            JSONObject jsonResponse = new JSONObject(response);
            JSONObject jsonContent = new JSONObject(jsonResponse.getString("content"));
            this.restoArray = jsonContent.getJSONArray("results");
        } catch (Exception e) {
            Log.d("crash", e.toString());
        }

    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.restorant_list_item, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        //String animal = mData.get(position);
        try {
            JSONObject resto = this.restoArray.getJSONObject(position);
            holder.restoId = resto.getString("id");
            holder.thumbnailRestoName.setText(resto.getString("name"));
            holder.thumbnailReviewNumber.setText("( ".concat(resto.getString("review_count")).concat(" )"));
            JSONArray cuisine = resto.getJSONArray("cuisine");
            JSONObject type = cuisine.getJSONObject(0);
            holder.thumbnailRestoCategory.setText(type.getString("name"));
            Picasso.get().load(resto.getString("image")).into(holder.thumbnailImage);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return restoArray.length();
    }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        String restoId;
        TextView thumbnailRestoName;
        TextView thumbnailRestoDistance;
        TextView thumbnailRestoCategory;
        ImageView thumbnailImage;
        ImageView thumbnailStar1;
        ImageView thumbnailStar2;
        ImageView thumbnailStar3;
        ImageView thumbnailStar4;
        ImageView thumbnailStar5;
        TextView thumbnailReviewNumber;

        ViewHolder(View itemView) {
            super(itemView);
            thumbnailImage = itemView.findViewById(R.id.thumbnails_resto_image);
            thumbnailRestoName = itemView.findViewById(R.id.thumbnails_resto_name);
            thumbnailRestoDistance = itemView.findViewById(R.id.thumbnails_resto_distance);
            thumbnailRestoCategory = itemView.findViewById(R.id.thumbnails_resto_category);
            thumbnailStar1 = itemView.findViewById(R.id.thumbnails_resto_star_1);
            thumbnailStar2 = itemView.findViewById(R.id.thumbnails_resto_star_2);
            thumbnailStar3 = itemView.findViewById(R.id.thumbnails_resto_star_3);
            thumbnailStar4 = itemView.findViewById(R.id.thumbnails_resto_star_4);
            thumbnailStar5 = itemView.findViewById(R.id.thumbnails_resto_star_5);
            thumbnailReviewNumber = itemView.findViewById(R.id.thumbnail_review_number);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) {
                Log.d("passe", restoId);

                ((MainActivity)mContext).navigateToRestaurantDetails(restoId, "0", "0");
            }
        }
    }

    /* // convenience method for getting data at click position
    String getItem(int id) {
        return jsonContent.get(id);
    } */

    // allows clicks events to be caught
    void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}
