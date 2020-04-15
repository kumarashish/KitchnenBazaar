package com.kitchenbazaar;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

import adapter.OrderItemAdapter;
import butterknife.BindView;
import butterknife.ButterKnife;
import common.AppController;
import common.Common;
import model.OrderItemModel;
import util.Utils;

/**
 * Created by ashish.kumar on 18-07-2018.
 */

public class OrderDetails  extends Activity implements View.OnClickListener{
    AppController controller;
    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.productList)
    ListView orderList;
    @BindView(R.id.heading)
    TextView heading;
    @BindView(R.id.progressbar)
    ProgressBar progress;
    @BindView(R.id.noItem)
    TextView noItem;
    @BindView(R.id.toalView)
    LinearLayout totalView;
    @BindView(R.id.totalCost)
    TextView total;
    ArrayList<OrderItemModel> list = new ArrayList<>();
    String orderId = null;
    String totalValue;
    @BindView(R.id.swiperefresh)
    SwipeRefreshLayout layout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.orders);
        controller = (AppController) getApplicationContext();
        ButterKnife.bind(this);
        totalView.setVisibility(View.VISIBLE);
        heading.setText("Order Details");
        layout.setEnabled(false);
        orderId = getIntent().getStringExtra("orderId");
        totalValue= getIntent().getStringExtra("total");
        total.setText("Rs "+totalValue);
        back.setOnClickListener(this);
        if (Utils.isNetworkAvailable(OrderDetails.this)) {
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
                String query = "" + Common.orderId + " like '" + orderId + "'";
                IDataStore<Map> contactStorage = Backendless.Data.of(Common.orderDetails);
                DataQueryBuilder queryBuilder = DataQueryBuilder.create();
                queryBuilder.setWhereClause(query);
                queryBuilder.setPageSize(100);
                contactStorage.find(queryBuilder, new AsyncCallback<List<Map>>() {
                    @Override
                    public void handleResponse(List<Map> categoryList) {
                        list.clear();
                        for (int i = 0; i < categoryList.size(); i++) {
                            Map category = categoryList.get(i);
                            list.add(new OrderItemModel(category));

                        }

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                progress.setVisibility(View.GONE);
                                if (list.size() > 0) {
                                    orderList.setVisibility(View.VISIBLE);
                                    noItem.setVisibility(View.GONE);
                                     orderList.setAdapter(new OrderItemAdapter(OrderDetails.this, list));
                                } else {
                                    orderList.setVisibility(View.GONE);
                                    noItem.setVisibility(View.VISIBLE);
                                }
                            }
                        });
                        Log.i("MYAPP", "Retrieved " + categoryList.size() + " objects");
                    }

                    @Override
                    public void handleFault(BackendlessFault fault) {
                        progress.setVisibility(View.GONE);
                        Log.e("MYAPP", "Server reported an error " + fault);
                    }
                });
            }
        });
        T.start();
    }


}