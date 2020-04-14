package ca.ulaval.ima.mp.ui.reviewRestaurant;

import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import ca.ulaval.ima.mp.R;
import ca.ulaval.ima.mp.ui.reviewRestaurant.ReviewRestaurantFragment.OnListFragmentInteractionListener;
import ca.ulaval.ima.mp.ui.reviewRestaurant.dummy.ReviewDummyContent.DummyItem;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link DummyItem} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyReviewRestaurantRecyclerViewAdapter extends RecyclerView.Adapter<MyReviewRestaurantRecyclerViewAdapter.ViewHolder> {

    private final List<DummyItem> mValues;
    private final OnListFragmentInteractionListener mListener;

    public MyReviewRestaurantRecyclerViewAdapter(List<DummyItem> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_review_restaurant, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mFirstName.setText(mValues.get(position).first_name);
        holder.mLastName.setText(mValues.get(position).last_name);
        holder.mStars.setText(mValues.get(position).star);
        Picasso.get().load(mValues.get(position).img).into(holder.mImageView);
        holder.mComment.setText(mValues.get(position).comment);
        holder.mDate.setText(mValues.get(position).date);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mFirstName;
        public final TextView mLastName;
        public final TextView mStars;
        public final ImageView mImageView;
        public final TextView mComment;
        public final TextView mDate;
        public DummyItem mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mFirstName = (TextView) view.findViewById(R.id.rv_firstname);
            mLastName = (TextView) view.findViewById(R.id.rv_lastname);
            mStars = (TextView) view.findViewById(R.id.rv_stars);
            mImageView = (ImageView) view.findViewById(R.id.rv_img);
            mComment = (TextView) view.findViewById(R.id.rv_comment);
            mDate = (TextView) view.findViewById(R.id.rv_date);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mFirstName.getText() + mLastName.getText() + "'";
        }
    }
}
