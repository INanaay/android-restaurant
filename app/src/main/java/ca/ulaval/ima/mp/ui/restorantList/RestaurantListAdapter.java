package ca.ulaval.ima.mp.ui.restorantList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ca.ulaval.ima.mp.R;

public class RestaurantListAdapter extends RecyclerView.Adapter<RestaurantListAdapter.ViewHolder> {
    private List<String> mData;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;

    // data is passed into the constructor
    RestaurantListAdapter(Context context, List<String> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
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
        String animal = mData.get(position);
        holder.thumbnailRestoName.setText(animal);
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mData.size();
    }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView thumbnailRestoName;
        TextView thumbnailRestoDistance;
        ImageView thumbnailImage;
        ImageView thumbnailStar1;
        ImageView thumbnailStar2;
        ImageView thumbnailStar3;
        ImageView thumbnailStar4;
        ImageView thumbnailStar5;

        ViewHolder(View itemView) {
            super(itemView);
            thumbnailImage = itemView.findViewById(R.id.thumbnails_resto_image);
            thumbnailRestoName = itemView.findViewById(R.id.thumbnails_resto_name);
            thumbnailRestoDistance = itemView.findViewById(R.id.thumbnails_resto_distance);
            thumbnailStar1 = itemView.findViewById(R.id.thumbnails_resto_star_1);
            thumbnailStar2 = itemView.findViewById(R.id.thumbnails_resto_star_2);
            thumbnailStar3 = itemView.findViewById(R.id.thumbnails_resto_star_3);
            thumbnailStar4 = itemView.findViewById(R.id.thumbnails_resto_star_4);
            thumbnailStar5 = itemView.findViewById(R.id.thumbnails_resto_star_5);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // convenience method for getting data at click position
    String getItem(int id) {
        return mData.get(id);
    }

    // allows clicks events to be caught
    void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}
