package adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.kitchenbazaar.R;

import java.util.ArrayList;

import common.AppController;
import common.Common;

import interfaces.OrderDetailsCallBack;
import model.CurrentOrderModel;
import util.Utils;

/**
 * Created by ashish.kumar on 18-07-2018.
 */

public class OrderAdapter extends BaseAdapter {
    Activity activity;
    ArrayList<CurrentOrderModel> list;
    LayoutInflater inflater;
    AppController controller;
    OrderDetailsCallBack callBack;
    public OrderAdapter(  Activity activity,ArrayList<CurrentOrderModel> list)
    {
        this.activity=activity;
        this.list=list;
        inflater= (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        controller=(AppController)activity.getApplicationContext();
        callBack=(OrderDetailsCallBack)activity;
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
        final CurrentOrderModel model=list.get(i);
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.order_row, null);
            holder.date = (TextView) view.findViewById(R.id.date);
            holder.amount = (TextView) view.findViewById(R.id.amount);
            holder.totalitemcount = (TextView) view.findViewById(R.id.totalItems);
            holder.address = (TextView) view.findViewById(R.id.address);
            holder.orderstatus = (TextView) view.findViewById(R.id.orderstatus);
            holder.cancelOrder = (Button) view.findViewById(R.id.cancel);
            holder.details = (View) view.findViewById(R.id.details);
            holder.ownerMessage=(TextView)view.findViewById(R.id.ownerMessage);
            holder.paymentstatus=(TextView)view.findViewById(R.id.paymentstatus);

        } else {
            holder = (ViewHolder) view.getTag();
        }
        holder.paymentstatus.setVisibility(View.GONE);
        if((Integer.parseInt(model.getOrderstatus())==2)||(Integer.parseInt(model.getOrderstatus())==3)||(Integer.parseInt(model.getOrderstatus())==4)||(Integer.parseInt(model.getOrderstatus())==5))
        {
            holder.cancelOrder.setVisibility(View.GONE);
            if ((Integer.parseInt(model.getOrderstatus()) != 1) ) {
                holder.paymentstatus.setVisibility(View.VISIBLE);
                if (model.isPaymentDone()) {
                    holder.paymentstatus.setText("Payment of Rs" + model.getAmount() + " Received By Grocworld");
                } else {
                    if(Integer.parseInt(model.getOrderstatus())!=4) {
                        holder.paymentstatus.setText("Please make payment of  Rs " + model.getAmount() + " Via paytm on 9044213970 for placed order");
                    }else{
                        holder.paymentstatus.setVisibility(View.GONE);
                    }
                }
            }


        }

        if(Integer.parseInt(model.getOrderstatus())==4)
        {if(model.getStoreOwnerMessage().length()==0)
        {
            holder.ownerMessage.setText( "You have cancelled order Reason : "+model.getCustomermessage());
        }else {
            holder.ownerMessage.setText("Store Keeper have cancelled order Reason : "+model.getStoreOwnerMessage());
        }
            holder.orderstatus.setTextColor(activity.getResources().getColor(R.color.red));
        }else{
            holder.ownerMessage.setVisibility(View.GONE);
            holder.orderstatus.setTextColor(Utils.getColor(model.getOrderstatus()));
        }


        final TextView status=holder.orderstatus;
        holder.cancelOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callBack.cancelOrderCallBack(model, Common.cancelledOrderStatus);
            }
        });
        holder.details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callBack.onItemClick(model);
            }
        });
       /// holder.date .setText("Order Date : "+model.getDate());
        holder.cancelOrder.setTypeface(controller.getNormal());
        holder.date.setText("Order Date : "+model.getDate());
        holder.amount.setText("Total Amount : Rs."+model.getAmount());
        holder.totalitemcount.setText("Total Item Count : "+model.getTotalItemCount());
        holder.address.setText("Delivery Address : "+model.getAddress());
        holder.orderstatus.setText("Status : "+ Utils.getOrderStatusString(model.getOrderstatus()));
        view.setTag(holder);
        return view;
    }
    public class ViewHolder {
       TextView date,amount,totalitemcount,orderstatus,address,ownerMessage,paymentstatus;
       Button cancelOrder;
       View details;
    }
}
