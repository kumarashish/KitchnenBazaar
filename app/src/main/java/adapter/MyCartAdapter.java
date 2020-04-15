package adapter;

import android.app.Activity;
import android.content.Context;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.StrikethroughSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.kitchenbazaar.R;

import java.util.ArrayList;

import common.AppController;

import model.ProductModel;
import util.Utils;

/**
 * Created by ashish.kumar on 10-07-2018.
 */

public class MyCartAdapter extends BaseAdapter {
    Activity activity;
    ArrayList<ProductModel> list;
    LayoutInflater inflater;
    AppController controller;
    public MyCartAdapter(  Activity activity,ArrayList<ProductModel> list)
    {
        this.activity=activity;
        this.list=list;
        inflater= (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return  i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder=null;
        final ProductModel model=list.get(i);
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.mycart_row, null);
            holder.productImage = (ImageView) view.findViewById(R.id.productImage);
            holder.productName = (TextView) view.findViewById(R.id.productName);
            holder.quantity = (TextView) view.findViewById(R.id.quantity);
            holder.mrp = (TextView) view.findViewById(R.id.mrp);
            holder.offerPrice = (TextView) view.findViewById(R.id.offerPrice);
            holder.discount = (TextView) view.findViewById(R.id.discount);


        } else {
            holder = (ViewHolder) view.getTag();
        }
        String discount= Utils.getDiscount(Double.parseDouble(model.getMRP()),Double.parseDouble(model.getOfferPrice()));
        if (discount.length() > 0) {
            holder.discount.setVisibility(View.VISIBLE);
            holder.discount.setText(discount);
        } else {
            holder.discount.setVisibility(View.GONE);
        }
        if(model.getMRP().equalsIgnoreCase(model.getOfferPrice()))
        {
            holder.mrp.setText("MRP: Rs "+model.getMRP());
            holder.mrp.setTextColor(activity.getResources().getColor(R.color.black_color));
            holder.offerPrice.setText("Rs "+model.getOfferPrice() +" * "+model.getQuantity()+" = "+getTotal(Float.parseFloat(model.getOfferPrice() ),model.getQuantity()));

        }else{
            holder.mrp.setText("MRP: Rs "+model.getMRP());
            SpannableString spannable = new SpannableString( holder.mrp.getText().toString());
            spannable.setSpan(new StrikethroughSpan(), 4,  holder.mrp.getText().length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            holder.mrp.setText(spannable);
            holder.offerPrice.setText("Rs "+model.getOfferPrice() +" * "+model.getQuantity()+" = "+getTotal(Float.parseFloat(model.getOfferPrice() ),model.getQuantity()));
        }
        holder.productName.setText(model.getProductName().toUpperCase());
        holder.quantity.setText("Quantity: "+model.getQuantity() );

        view.setTag(holder);
        return view;
    }
    public class ViewHolder {
        common.Bold_TextView quantityValue;
        ImageView  productImage;
        TextView   productName,  mrp,  offerPrice, quantity,discount;
    }

    public int getTotal(float price, int quantity) {
        return Math.round(price * quantity);
    }
}
