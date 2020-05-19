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
import android.widget.Toast;

import com.kitchenbazaar.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Locale;

import common.AppController;

import common.Common;
import interfaces.OnQuantityClick;
import model.ProductModel;
import util.Utils;

/**
 * Created by ashish.kumar on 10-07-2018.
 */

public class ProductListAdapter extends BaseAdapter{
    Activity activity;
    ArrayList<ProductModel>list;
    ArrayList<ProductModel>filteredList=new ArrayList<>();
    LayoutInflater inflater;
    OnQuantityClick callBack;
    int quantity=0;
    AppController controller;

    public ProductListAdapter(Activity activity,ArrayList<ProductModel>list)
    {
        this.list=list;
        filteredList.addAll(  this.list);
        this.activity=activity;
        inflater= (LayoutInflater)  activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        controller=(AppController)activity.getApplicationContext();
        callBack=(OnQuantityClick)activity;
    }
    @Override
    public int getCount() {
        return    filteredList.size();
    }

    @Override
    public Object getItem(int i) {
        return    filteredList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        ViewHolder holder=null;
        final ProductModel model=   filteredList.get(i);
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.product_row, null);
            holder.productImage = (ImageView) view.findViewById(R.id.productImage);
            holder.productName = (TextView) view.findViewById(R.id.productName);
            holder.quantity = (TextView) view.findViewById(R.id.quantity);
            holder.mrp = (TextView) view.findViewById(R.id.mrp);
            holder.offerPrice = (TextView) view.findViewById(R.id.offerPrice);
            holder.quantityValue = (common.Bold_TextView) view.findViewById(R.id.quantityValue);
            holder.increasequantity = (ImageView) view.findViewById(R.id.increasequantity);
            holder.decreasequantity = (ImageView) view.findViewById(R.id.decreasequantity);
            holder.discount=(common.Bold_TextView)view.findViewById(R.id.discount);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        holder.productName.setText(model.getProductName().toUpperCase());
        holder.quantity.setText("Quantity: 1");
        holder.mrp.setText("MRP: Rs "+model.getMRP());
        if(model.getMRP().equalsIgnoreCase(model.getOfferPrice()))
        {
            holder.mrp.setText("MRP: Rs "+model.getMRP());
            holder.mrp.setTextColor(activity.getResources().getColor(R.color.black_color));
            holder.offerPrice.setText("");
        }else{
            SpannableString spannable = new SpannableString( holder.mrp.getText().toString());
            spannable.setSpan(new StrikethroughSpan(), 4,  holder.mrp.getText().length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            holder.mrp.setText(spannable);
            holder.offerPrice.setText("Rs "+model.getOfferPrice());
        }
        if(model.getProduct_Image().length()>0) {
            Picasso.with(activity).load(model.getProduct_Image()).placeholder(R.drawable.no_image).into(holder.productImage);
        }
        holder.quantityValue.setText(Integer.toString(model.getQuantity()));
        String discount= Utils.getDiscount(Double.parseDouble(model.getMRP()),Double.parseDouble(model.getOfferPrice()));
        if (discount.length() > 0) {
            holder.discount.setVisibility(View.VISIBLE);
            holder.discount.setText(discount);
        } else {
            holder.discount.setVisibility(View.GONE);
        }
        holder.increasequantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callBack.onIncreaseQuantity(model,i);

            }
        });
        holder.decreasequantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(model.getQuantity()!=0)
                {
                  callBack.onDecreaseQuantity(model,i);
                }else{
                    Toast.makeText(activity,"You dont have any item in cart",Toast.LENGTH_SHORT).show();
                }
            }
        });
        view.setTag(holder);
        return view;
    }

    public class ViewHolder {
        common.Bold_TextView quantityValue,discount;
        ImageView  productImage,increasequantity,decreasequantity;
        TextView   productName,  mrp,  offerPrice, quantity;
    }
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        filteredList.clear();
        if (charText.length() == 0) {
            filteredList.addAll(   list);
        }
        else
        {
            for (ProductModel wp :list)
            {
                if (wp.getProductName().toLowerCase(Locale.getDefault()).contains(charText))
                {
                    filteredList.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }
}
