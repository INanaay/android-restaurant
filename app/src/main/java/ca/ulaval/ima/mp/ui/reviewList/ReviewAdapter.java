package ca.ulaval.ima.mp.ui.reviewList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import ca.ulaval.ima.mp.R;
import ca.ulaval.ima.mp.Review;

public class ReviewAdapter extends ArrayAdapter {

    List list = new ArrayList();

    public ReviewAdapter(Context context, int resource) {
        super(context, resource);
    }

    public void add(@Nullable Review object) {
        super.add(object);
        list.add(object);

    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Nullable
    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View row;
        ReviewHolder reviewHolder;
        row = convertView;
        if (row == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = layoutInflater.inflate(R.layout.adapter_view_layout, parent, false);
            reviewHolder = new ReviewHolder();
            reviewHolder.rv_firstname = (TextView) row.findViewById(R.id.rv_firstname);
            reviewHolder.rv_lastname = (TextView) row.findViewById(R.id.rv_lastname);
            reviewHolder.rv_comment = (TextView) row.findViewById(R.id.rv_comment);
            reviewHolder.rv_stars = (TextView) row.findViewById(R.id.rv_stars);
            reviewHolder.rv_date = (TextView) row.findViewById(R.id.rv_date);
            reviewHolder.rv_img = (ImageView) row.findViewById(R.id.rv_img);
            row.setTag(reviewHolder);
        }
        else {
            reviewHolder = (ReviewHolder) row.getTag();
        }

        Review review = (Review) this.getItem(position);
        assert review != null;
        reviewHolder.rv_firstname.setText(review.getFirst_name());
        reviewHolder.rv_lastname.setText(review.getLast_name());
        reviewHolder.rv_comment.setText(review.getComment());
        reviewHolder.rv_stars.setText(review.getNb_stars());
        reviewHolder.rv_date.setText(review.getDate());
        Picasso.get().load(review.getImg()).into(reviewHolder.rv_img);
        return row;
    }

    static class ReviewHolder {
        TextView rv_firstname, rv_lastname, rv_stars, rv_date, rv_comment;
        ImageView rv_img;
    }

}
