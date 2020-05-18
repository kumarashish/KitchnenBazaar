package com.kitchenbazaar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
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
import java.util.Locale;
import java.util.Map;

import adapter.ProductListAdapter;
import butterknife.BindView;
import butterknife.ButterKnife;
import common.AppController;
import common.Common;

import interfaces.OnQuantityClick;
import model.ProductModel;
import util.Utils;

/**
 * Created by ashish.kumar on 10-07-2018.
 */

public class ProductList extends Activity implements View.OnClickListener, OnQuantityClick{
    String CategoryId;
    String CategoryName="";
    AppController controller;
    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.productList)
    ListView productList;
    @BindView(R.id.category)
    TextView category;
    @BindView(R.id.progressbar)
    ProgressBar progressbar;
    ArrayList<ProductModel> productLst=new ArrayList<>();
    @BindView(R.id.cart_item_count)
    TextView badgeCount;
    @BindView(R.id.myCart)
    View myCart;
    @BindView(R.id.amount)
    TextView amount;
    @BindView(R.id.search)
    EditText search;
    ProductListAdapter adapter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.productlist);
        controller=(AppController)getApplicationContext();
        ButterKnife.bind(this);
        back.setOnClickListener(this);
        myCart.setOnClickListener(this);
        CategoryId=getIntent().getStringExtra(Common.categoryId);
        CategoryName=getIntent().getStringExtra(Common.categoryName);
        category.setText( CategoryName);
        if (Utils.isNetworkAvailable(ProductList.this)) {
            getData();
        }
        if (controller.getMyCart().size() > 0) {
            badgeCount.setVisibility(View.VISIBLE);
            amount.setText("Rs "+getTotalCost());
            badgeCount.setText(Integer.toString(controller.getMyCartItemCount()));
        }
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String text = search.getText().toString().toLowerCase(Locale.getDefault());
                if(adapter!=null) {
                    adapter.filter(text);
                }
            }
        });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                onBackPressed();
                break;
            case R.id.myCart:
                startActivity(new Intent(ProductList.this,MyCart.class));
                break;
        }

    }
    public void getData() {
        progressbar.setVisibility(View.VISIBLE);
        Thread T = new Thread(new Runnable() {
            @Override
            public void run() {
                String query =  ""+Common.categoryId+"='"+CategoryId+"'";
                final IDataStore<Map> contactStorage = Backendless.Data.of(Common.productsTable);
                DataQueryBuilder queryBuilder = DataQueryBuilder.create();
                queryBuilder.setWhereClause(query);
                queryBuilder.setPageSize(100);
                contactStorage.find( queryBuilder, new AsyncCallback<List<Map>>()
                {
                    @Override
                    public void handleResponse(final List<Map>productListt )
                    {
                        productLst.clear();
                        for(int i=0;i<productListt.size();i++)
                        {
                            Map productModel=productListt.get(i);
                            productLst.add(new ProductModel(productModel));
                        }

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                progressbar.setVisibility(View.GONE);
                                for (int i = 0; i < productLst.size(); i++) {
                                    if (controller.isItemPresent(productLst.get(i).getProductId())) {

                                        productLst.get(i).setQuantity(controller.getProductAddedQuantity(productLst.get(i).getProductId()));

                                    }
                                }
                                adapter=new ProductListAdapter(ProductList.this, productLst);
                               productList.setAdapter(adapter);
                               search.setEnabled(true);
                               search.setFocusable(true);
                               search.setFocusableInTouchMode(true);
                            }
                        });

                        Log.i( "MYAPP", "Retrieved " +productListt .size() + " objects" );
                    }

                    @Override
                    public void handleFault( BackendlessFault fault )
                    {  progressbar.setVisibility(View.GONE);
                        Log.e( "MYAPP", "Server reported an error " + fault );
                    }
                } );
            }
        });
        T.start();
    }

    @Override
    public void onIncreaseQuantity(final ProductModel model,int position) {
        if(model.getQuantity()==0) {
            productLst.get(position).increaseQuantity();
        }
        controller.upDateMyCart(model, 1);
        if (controller.getMyCart().size() > 0) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    amount.setText("Rs "+getTotalCost());
                    badgeCount.setVisibility(View.VISIBLE);
                    badgeCount.setText(Integer.toString(controller.getMyCartItemCount()));
               adapter.notifyDataSetChanged();
                }
            });
        }
    }

    @Override
    public void onDecreaseQuantity(ProductModel model,int position) {
        controller.upDateMyCart(model, 2);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (controller.getMyCart().size() > 0) {
                    badgeCount.setVisibility(View.VISIBLE);
                    badgeCount.setText(Integer.toString(controller.getMyCartItemCount()));
                    amount.setText("Rs "+getTotalCost());
                } else {
                    badgeCount.setVisibility(View.GONE);
                    badgeCount.setText("");
                    amount.setText("");
                }
              adapter.notifyDataSetChanged();
            }
        });
    }
    public int getTotalCost()
    {
        int value=0;
        for(int i=0;i< controller.getMyCart().size();i++)
        {
            value+=Math.round(Float.parseFloat(controller.getMyCart().get(i).getOfferPrice())*controller.getMyCart().get(i).getQuantity());
        }
        return value;
    }
}

