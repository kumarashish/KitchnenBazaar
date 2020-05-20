package adapter;

import android.content.Context;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.kitchenbazaar.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import model.BannerModel;


/**
 * Created by ashish.kumar on 04-07-2018.
 */

public class ViewPagerAdapter extends PagerAdapter {

    private Context context;
    private LayoutInflater layoutInflater;
    private ArrayList<BannerModel>bannerlist;

    public ViewPagerAdapter(Context context,ArrayList<BannerModel>bannerlist) {
        this.context = context;
        this.bannerlist=bannerlist;
    }

    @Override
    public int getCount() {
        return bannerlist.size();//
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {

        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.custom_layout, null);
        ImageView imageView = (ImageView) view.findViewById(R.id.imageView);
        Picasso.with(context).load(bannerlist.get(position).getImgStr()).placeholder(R.drawable.loading).into(imageView);
        ViewPager vp = (ViewPager) container;
        vp.addView(view, 0);
        return view;

    }
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {

        ViewPager vp = (ViewPager) container;
        View view = (View) object;
        vp.removeView(view);

    }
}