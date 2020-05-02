package com.kitchenbazaar;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import adapter.MyCartAdapter;
import butterknife.BindView;
import butterknife.ButterKnife;
import common.AppController;
import common.Common;
import launchingscreens.Login;
import model.OrderModel;
import model.ProductModel;
import model.UserProfile;
import network.WebApiCall;
import util.Utils;

/**
 * Created by ashish.kumar on 10-07-2018.
 */

public class MyCart extends Activity implements View.OnClickListener{
    AppController controller;
    @BindView(R.id.back)
    ImageView back;
    ArrayList<ProductModel>myCart;
    @BindView(R.id.productList)
    ListView productList;
    @BindView(R.id.noItem)
    TextView noItemView;
    @BindView(R.id.totalCost)
    TextView totalCost;
    @BindView(R.id.cancelOrder)
    Button cancelOrder;
    @BindView(R.id.placeOrder)
    Button placeOrder;
    @BindView(R.id.linearLayout)
    LinearLayout footer;
    @BindView(R.id.progressbar)
    ProgressBar progressBar;
    String customer_address;
    String customer_pincode;
    String customerName;
    String customerMobileNumber;
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mycart);
        controller=(AppController)getApplicationContext();
        ButterKnife.bind(this);
        back.setOnClickListener(this);
        myCart=controller.getMyCart();
        if(myCart.size()==0)
        {
            noItemView.setVisibility(View.VISIBLE);
            productList.setVisibility(View.GONE);
            footer.setVisibility(View.GONE);

        }else {
            productList.setVisibility(View.VISIBLE);
            footer.setVisibility(View.VISIBLE);
            noItemView.setVisibility(View.GONE);
            productList.setAdapter(new MyCartAdapter(MyCart.this, controller.getMyCart()));
        }
        cancelOrder.setOnClickListener(this);
        placeOrder.setOnClickListener(this);
        totalCost.setText("Rs "+Integer.toString(getTotalCost()));


}

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.back:
                onBackPressed();
                break;
            case R.id.cancelOrder:
                break;
            case R.id.placeOrder:
                if (controller.isUserLoggedIn()) {
                    showAddressPopup();

                }else{
                    startActivityForResult(new Intent(MyCart.this, Login.class),1);
                }
                break;
        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK)
        {
            showAddressPopup();
        }
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

    public void callSmsApi() {
        Thread T1 = new Thread(new Runnable() {
            @Override
            public void run() {
                new WebApiCall(MyCart.this).getData(Common.getSendSMSUrl(customerMobileNumber, "Thank you for placing order with "+Common.storeName+",your order will be in pending state until store owner accepts it"));

            }
        });
        T1.start();
        Thread T2 = new Thread(new Runnable() {
            @Override
            public void run() {
                new WebApiCall(MyCart.this).getData(Common.getSendSMSUrl(Common.storeNumber, "You have received one new order from "+customerName+" \nAddress: "+customer_address+", "+customer_pincode+"\n Mobile number:"+customerMobileNumber+", Please check "+Common.storeName+" admin app for order details."));

            }
        });
        T2.start();

    }

    public void updateOrderItems(String orderId) {
        //progressBar.setVisibility(View.GONE);
        for (int i = 0; i < controller.getMyCart().size(); i++) {
            insertOrderItem(controller.getMyCart().get(i), orderId);
        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                donePopUp();
            }
        });
        callSmsApi();

    }




    public int getTotal(float price, int quantity) {
        return Math.round(price * quantity);
    }
   public void insertOrderItem(ProductModel items,String orderId)
   {
       HashMap map = null;
       map = new HashMap();
       map.put("orderId",orderId);
       map.put("productname", items.getProductName());
       map.put("productprice",items.getOfferPrice());
       map.put("quantity", items.getQuantity());
       map.put("totalamount", Integer.toString(getTotal(Float.parseFloat(items.getOfferPrice()),items.getQuantity())));
       final Map mapp = map;
       Thread t = new Thread(new Runnable() {
           @Override
           public void run() {
               //OrderModel order = Backendless.Persistence.save(model);

               // save object asynchronously
               Backendless.Persistence.of(Common.orderDetails).save(mapp, new AsyncCallback<Map>() {
                   public void handleResponse(Map response) {
                       Log.d("Response", "handleResponse: " + response);

                   }

                   public void handleFault(BackendlessFault fault) {
                       Log.d("Response", "handleResponse: " + fault);
                       placeOrder.setVisibility(View.VISIBLE);
                       // an error has occurred, the error code can be retrieved with fault.getCode()
                   }
               });
           }
       });
       t.start();

   }

    public void placeOrder(String address,String pincode) {
        customer_address=address;
        customer_pincode=pincode;
        UserProfile profile = controller.getUserProfil();
        customerMobileNumber=profile.getPhoneNumber();
        customerName=profile.getName();
        HashMap map = null;
        map = new HashMap();
        map.put("name", profile.getName());
        map.put("mobile", profile.getPhoneNumber());
        map.put("address", address);
        map.put("customermessage", "");
        map.put("orderstatus", Integer.toString(Common.pendingOrderStatus));
        map.put("pincode", pincode);
        map.put("store_owner_message", "");
        map.put("totalamount", Integer.toString(getTotalCost()));
        map.put("userId", profile.getUserId());
        map.put("TotalItemsCount",controller.getMyCartItemCount());
        final Map mapp = map;
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                //OrderModel order = Backendless.Persistence.save(model);

                // save object asynchronously
                Backendless.Persistence.of(Common.orderTable).save(mapp, new AsyncCallback<Map>() {
                    public void handleResponse(Map response) {
                        Log.d("Response", "handleResponse: " + response);
                        OrderModel model1 = new OrderModel(response);
                        updateOrderItems(model1.getOrderId());
                    }

                    public void handleFault(BackendlessFault fault) {
                        Log.d("Response", "handleResponse: " + fault);
                        placeOrder.setVisibility(View.VISIBLE);
                        // an error has occurred, the error code can be retrieved with fault.getCode()
                    }
                });
            }
        });
        t.start();
        // save object synchronously

    }

    public void donePopUp() {
        progressBar.setVisibility(View.GONE);
        BottomSheetDialog mBottomSheetDialog = new BottomSheetDialog(MyCart.this);
        View sheetView = getLayoutInflater().inflate(R.layout.done_message, null);
        mBottomSheetDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        mBottomSheetDialog.setContentView(sheetView);
        mBottomSheetDialog.setCancelable(false);
        common.DetailsCustomTextView heading = (common.DetailsCustomTextView) sheetView.findViewById(R.id.heading);
        ImageView icon = (ImageView) sheetView.findViewById(R.id.icon);
        common.Bold_TextView text = (common.Bold_TextView) sheetView.findViewById(R.id.text);
        LinearLayout singleView = (LinearLayout) sheetView.findViewById(R.id.singleView);
        ImageView icon3 = (ImageView) sheetView.findViewById(R.id.icon3);
        common.Bold_TextView text3 = (common.Bold_TextView) sheetView.findViewById(R.id.text3);
        common.Bold_TextView message = (common.Bold_TextView) sheetView.findViewById(R.id.message);
        singleView.setVisibility(View.VISIBLE);
        text3.setText("Done");
        message.setText("Your Order has been placed sucessfully.");
        icon3.setImageResource(R.drawable.status_complete_icon);
        Button close = (Button) sheetView.findViewById(R.id.close);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                controller.clearMyCart();
                Utils.goToDashboard(MyCart.this);
                finish();
            }
        });
        FrameLayout bottomSheet = (FrameLayout) mBottomSheetDialog.getWindow().findViewById(android.support.design.R.id.design_bottom_sheet);
        bottomSheet.setBackgroundResource(R.drawable.alert_bg);
        mBottomSheetDialog.show();
    }

    public void showAddressPopup() {
        final BottomSheetDialog mBottomSheetDialog = new BottomSheetDialog(MyCart.this);
        View sheetView = getLayoutInflater().inflate(R.layout.payment_popup, null);
        mBottomSheetDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        mBottomSheetDialog.setContentView(sheetView);
        mBottomSheetDialog.setCancelable(false);
        final EditText address = (EditText) sheetView.findViewById(R.id.address);
        final EditText pincode = (EditText) sheetView.findViewById(R.id.pincode);
        Button submit = (Button) sheetView.findViewById(R.id.submit);
        submit.setTypeface(controller.getNormal());
        pincode.setTypeface(controller.getNormal());
        address.setTypeface(controller.getNormal());
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
                if (address.getText().length() > 10) {
                    if (pincode.getText().length() == 6) {
                        if (isDeliveryAvailable(Integer.parseInt(pincode.getText().toString()))) {
                            progressBar.setVisibility(View.VISIBLE);
                            placeOrder(address.getText().toString(), pincode.getText().toString());
                            mBottomSheetDialog.cancel();
                        } else {
                            Toast.makeText(MyCart.this, "Sorry we are not delivering to entered pincode.", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        if (pincode.getText().length() == 0) {
                            Toast.makeText(MyCart.this, "Please enter Pincode", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(MyCart.this, "Please enter valid Pincode", Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    if (address.getText().length() == 0) {
                        Toast.makeText(MyCart.this, "Please enter address", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(MyCart.this, "Please enter valid address", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        FrameLayout bottomSheet = (FrameLayout) mBottomSheetDialog.getWindow().findViewById(android.support.design.R.id.design_bottom_sheet);
        bottomSheet.setBackgroundResource(R.drawable.alert_bg);
        mBottomSheetDialog.show();
    }


    public boolean isDeliveryAvailable(int pincode)
    {
        boolean status=false;
        if((pincode==800001)||(pincode==500089)||(pincode==500008)||(pincode==209625)||(pincode==209601)||(pincode==209203))
        {
            status=true;
        }
        return status;
    }

}
