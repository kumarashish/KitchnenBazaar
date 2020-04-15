package com.kitchenbazaar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.backendless.Backendless;
import com.backendless.IDataStore;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.DataQueryBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import adapter.OrderAdapter;
import butterknife.BindView;
import butterknife.ButterKnife;
import common.AppController;
import common.Common;
import interfaces.OrderDetailsCallBack;
import model.CurrentOrderModel;
import util.Utils;

/**
 * Created by ashish.kumar on 16-07-2018.
 */

public class History extends Activity implements View.OnClickListener, OrderDetailsCallBack {
    AppController controller;
    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.productList)
    ListView productList;
    @BindView(R.id.heading)
    TextView heading;
    @BindView(R.id.progressbar)
    ProgressBar progress;
    ArrayList<CurrentOrderModel> list=new ArrayList<>();
    @BindView(R.id.swiperefresh)
    SwipeRefreshLayout layout;
    @BindView(R.id.noItem)
    TextView noItem;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.orders);
        controller = (AppController) getApplicationContext();
        ButterKnife.bind(this);
        heading.setText("Completed Orders");
        back.setOnClickListener(this);
        layout.setEnabled(false);
        if(Utils.isNetworkAvailable(History.this))
        {
            getData();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                onBackPressed();
                break;
        }

    }
    public void getData() {
        progress.setVisibility(View.VISIBLE);
        Thread T = new Thread(new Runnable() {
            @Override
            public void run() {
                String query =  ""+ Common.userIdKey+" like '"+controller.getUserProfil().getUserId()+"' and ("+ Common.orderstatus+" like '"+Common.cancelledOrderStatus+"' or "+ Common.orderstatus+" like '"+Common.deliveredOrderStatus+"')";
                IDataStore<Map> contactStorage = Backendless.Data.of(Common.orderTable);
                DataQueryBuilder queryBuilder = DataQueryBuilder.create();
                queryBuilder.setWhereClause(query);
                queryBuilder.setSortBy("created DESC");
                queryBuilder.setPageSize(100);
                contactStorage.find( queryBuilder, new AsyncCallback<List<Map>>()
                {
                    @Override
                    public void handleResponse( List<Map>categoryList )
                    {
                        progress.setVisibility(View.GONE);
                        list.clear();
                        for (int i = 0; i < categoryList.size(); i++) {
                            Map category = categoryList.get(i);
                            list.add(new CurrentOrderModel(category));
                        }

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                progress.setVisibility(View.GONE);
                                if(list.size()>0) {
                                    productList.setVisibility(View.VISIBLE);
                                    noItem.setVisibility(View.GONE);
                                    productList.setAdapter(new OrderAdapter(History.this, list));
                                }else{
                                    productList.setVisibility(View.GONE);
                                    noItem.setVisibility(View.VISIBLE);
                                }
                            }
                        });
                    }

                    @Override
                    public void handleFault( BackendlessFault fault )
                    {  progress.setVisibility(View.GONE);
                        Log.e( "MYAPP", "Server reported an error " + fault );
                        productList.setVisibility(View.GONE);
                        noItem.setVisibility(View.VISIBLE);
                    }
                } );
            }
        });
        T.start();
    }

    @Override
    public void onItemClick(final CurrentOrderModel orderModel) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Intent in = new Intent(History.this, OrderDetails.class);
                in.putExtra("orderId", orderModel.getOrderId());
                in.putExtra("total",orderModel.getAmount());
                startActivity(in);
            }
        });
    }

    @Override
    public void cancelOrderCallBack(CurrentOrderModel model, int status) {

    }
}