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

import ca.ulaval.ima.mp.R;
import ca.ulaval.ima.mp.Review;

public class ReviewListAdapter extends ArrayAdapter<Review> {
    private static final String TAG = "ReviwListAdapter";
    private Context mContext;
    int mResource;

    public ReviewListAdapter(Context context, int resource, ArrayList<Review> objects) {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        String first_name = getItem(position).getFirst_name();
        String last_name = getItem(position).getLast_name();
        String nb_stars = getItem(position).getNb_stars();
        String comment = getItem(position).getComment();
        String img = getItem(position).getImg();
        String date = getItem(position).getDate();

        Review review = new Review(first_name, last_name, nb_stars, img, comment, date);

        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResource, parent, false);

        TextView Rv_date = convertView.findViewById(R.id.rv_date);
        TextView Rv_stars = convertView.findViewById(R.id.rv_stars);
        TextView Rv_firstname = convertView.findViewById(R.id.rv_firstname);
        TextView Rv_lastname = convertView.findViewById(R.id.rv_lastname);
        TextView Rv_comment = convertView.findViewById(R.id.rv_comment);
        ImageView Rv_img = convertView.findViewById(R.id.rv_img);

        Rv_date.setText(date);
        Rv_stars.setText(nb_stars);
        Rv_firstname.setText(first_name);
        Rv_lastname.setText(last_name);
        Rv_comment.setText(comment);
        Picasso.get().load(img).into(Rv_img);

        return convertView;
    }
}
