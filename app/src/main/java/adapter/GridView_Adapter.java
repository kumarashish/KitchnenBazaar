package adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.kitchenbazaar.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


import interfaces.OnCategorySelected;
import model.CategoryModel;

/**
 * Created by ashish.kumar on 04-07-2018.
 */

public class GridView_Adapter extends BaseAdapter {
    private Activity mContext;
    ArrayList<CategoryModel> list;
LayoutInflater inflater;
OnCategorySelected callback;
    // Constructor
    public GridView_Adapter(Activity c, ArrayList<CategoryModel> list) {
        mContext = c;
        this.list =list;
        inflater= (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
callback=(OnCategorySelected)c;
    }

    public int getCount() {


        return list.size();
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        final CategoryModel model = list.get(position);
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.grid_row, null);
            holder.image = (ImageView) convertView.findViewById(R.id.image);
            holder.name = (TextView) convertView.findViewById(R.id.name);
            holder.progressBar = (ProgressBar) convertView.findViewById(R.id.progress);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        convertView.setTag(holder);
        final ProgressBar bar = holder.progressBar;
        final ImageView image = holder.image;
        bar.setVisibility(View.GONE);
        if (model.getCategoryImage().length() > 0) {
            Picasso.with(mContext).load(model.getCategoryImage()).placeholder(R.drawable.no_image).into(holder.image);
        }
        holder.name.setText(model.getCategoryName());
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callback.onCategoryClicked(model);
            }
        });
        return convertView;
    }
    public class ViewHolder{
        ImageView image;
        TextView name;
        ProgressBar progressBar;
    }
}
