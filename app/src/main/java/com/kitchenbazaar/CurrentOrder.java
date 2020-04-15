package com.kitchenbazaar;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.IDataStore;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.DataQueryBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import adapter.OrderAdapter;
import butterknife.BindView;
import butterknife.ButterKnife;
import common.AppController;
import common.Common;
import interfaces.OrderDetailsCallBack;
import model.CurrentOrderModel;
import network.WebApiCall;
import util.Utils;

/**
 * Created by ashish.kumar on 16-07-2018.
 */

public class CurrentOrder extends Activity implements View.OnClickListener , OrderDetailsCallBack {
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
    @BindView(R.id.swiperefresh)
    SwipeRefreshLayout layout;
ArrayList<CurrentOrderModel> list=new ArrayList<>();
    CurrentOrderModel model=null;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.orders);
        controller = (AppController) getApplicationContext();
        ButterKnife.bind(this);
        heading.setText("Current Orders");
        back.setOnClickListener(this);
        layout.setEnabled(false);
        if(Utils.isNetworkAvailable(CurrentOrder.this))
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
    public void cancelOrderPopUp() {

        final BottomSheetDialog mBottomSheetDialog = new BottomSheetDialog(CurrentOrder.this);
        View sheetView = getLayoutInflater().inflate(R.layout.cancel_order_popup, null);
        mBottomSheetDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        mBottomSheetDialog.setContentView(sheetView);
        mBottomSheetDialog.setCancelable(false);
        final EditText message = (EditText) sheetView.findViewById(R.id.address);

        Button submit = (Button) sheetView.findViewById(R.id.submit);
        submit.setTypeface(controller.getNormal());

        message.setTypeface(controller.getNormal());
        Button close = (Button) sheetView.findViewById(R.id.close);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mBottomSheetDialog.cancel();
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (message.getText().length() > 10) {

                    progress.setVisibility(View.VISIBLE);
                    cancelOrder(model.getOrderId(),message.getText().toString(),4);
                    mBottomSheetDialog.cancel();
                    orderList.setVisibility(View.GONE);

                } else {
                    if (message.getText().length() == 0) {
                        Toast.makeText(CurrentOrder.this, "Please enter reson for cancellation", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(CurrentOrder.this, "Please enter minimum 10 characters", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        FrameLayout bottomSheet = (FrameLayout) mBottomSheetDialog.getWindow().findViewById(android.support.design.R.id.design_bottom_sheet);
        bottomSheet.setBackgroundResource(R.drawable.alert_bg);
        mBottomSheetDialog.show();
    }

    public void getData() {
        progress.setVisibility(View.VISIBLE);
        Thread T = new Thread(new Runnable() {
            @Override
            public void run() {
                String query = "" + Common.userIdKey + " like '" + controller.getUserProfil().getUserId() + "' and (" + Common.orderstatus + " like '" + Common.pendingOrderStatus + "' or " + Common.orderstatus + " like '" + Common.acceptedOrderStatus + "' or " + Common.orderstatus + " like '" + Common.outForDelivery + "')";
                IDataStore<Map> contactStorage = Backendless.Data.of(Common.orderTable);
                DataQueryBuilder queryBuilder = DataQueryBuilder.create();

                queryBuilder.setWhereClause(query);
                queryBuilder.setSortBy("created DESC");
                queryBuilder.setPageSize(100);
                contactStorage.find(queryBuilder, new AsyncCallback<List<Map>>() {
                    @Override
                    public void handleResponse(List<Map> categoryList) {
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
                                    orderList.setVisibility(View.VISIBLE);
                                    noItem.setVisibility(View.GONE);
                                    orderList.setAdapter(new OrderAdapter(CurrentOrder.this, list));
                                }else{
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

    @Override
    public void onItemClick(final CurrentOrderModel orderModel) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Intent in = new Intent(CurrentOrder.this, OrderDetails.class);
                in.putExtra("orderId", orderModel.getOrderId());
                in.putExtra("total",orderModel.getAmount());
                startActivity(in);
            }
        });
    }

    @Override
    public void cancelOrderCallBack(CurrentOrderModel model, int status) {
        this.model = model;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                cancelOrderPopUp();
            }
        });
    }

    public void cancelOrder(final String orderId, final String reason, final int status) {
        progress.setVisibility(View.VISIBLE);
        orderList.setVisibility(View.GONE);
        Thread T = new Thread(new Runnable() {
            @Override
            public void run() {
                Map<String, Object> map = new HashMap<>();
                map.put("orderstatus", Integer.toString(status));
                map.put("customermessage", reason);
                String query = "" + Common.objectId + "= '" + orderId + "'";
                Backendless.Data.of(Common.orderTable).update(query, map, new AsyncCallback<Integer>() {
                    @Override
                    public void handleResponse(Integer response) {
                        progress.setVisibility(View.GONE);
                        Toast.makeText(CurrentOrder.this, "Your Order has been Cancelled.Please Go to Completed Order Section for details", Toast.LENGTH_SHORT).show();
                        getData();
                        new WebApiCall(CurrentOrder.this).getData(Common.getSendSMSUrl("8005300408", "Customer have cancelled order because " +reason));
                    }

                    @Override
                    public void handleFault(BackendlessFault fault) {
                        Log.e("MYAPP", "Server reported an error " + fault);
                    }
                });
            }
        });
        T.start();
    }
}