package com.kitchenbazaar;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.annotation.Nullable;

import com.backendless.IDataStore;
import com.backendless.persistence.DataQueryBuilder;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import adapter.AddressAdapter;
import adapter.AlertAddressAdapter;
import adapter.MyCartAdapter;
import butterknife.BindView;
import butterknife.ButterKnife;
import common.AppController;
import common.Common;
import interfaces.WebApiResponseCallback;
import launchingscreens.Login;
import model.DeliveryAddressModel;
import model.OrderModel;
import model.ProductModel;
import model.UserProfile;
import network.WebApiCall;
import util.Utils;

/**
 * Created by ashish.kumar on 10-07-2018.
 */

public class MyCart extends Activity implements View.OnClickListener, WebApiResponseCallback {
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
    ProgressDialog progressDialog;
    ArrayList<DeliveryAddressModel>addressListItems=new ArrayList<>();
    DeliveryAddressModel model;
    String selectedSlot="";
    Dialog dialog;

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
        inilizeProgressDialog();
        progressDialog.show();
        getAddressList();


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
                    if(addressListItems.size()>0) {
                        showAddressPopup();
                    }else {
                        startActivityForResult(new Intent(MyCart.this,DeliveryAddress.class),34);
                    }

                }else{
                    Intent in=new Intent(MyCart.this, Login.class);
                    in.putExtra("isCalledFromCart",true);
                    startActivityForResult(in,34);
                }
                break;
        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if((requestCode==34)&&(resultCode==RESULT_OK))
        {   progressDialog.show();
            getAddressList();
        }else {
            if (resultCode == RESULT_OK) {
                showAddressPopup();
            }
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

    public void placeOrder(String address,String pincode,String deliveryDate,String slot) {
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
        map.put("deliverydate", deliveryDate);
        map.put("deliveryslot",slot);
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
        FrameLayout bottomSheet = (FrameLayout) mBottomSheetDialog.getWindow().findViewById(com.google.android.material.R.id.design_bottom_sheet);
        bottomSheet.setBackgroundResource(R.drawable.alert_bg);
        mBottomSheetDialog.show();
    }

    public void showAddressPopup()
    {
        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.address_alert);
        final Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);

        ListView address = (ListView) dialog.findViewById(R.id.addressList);
        final Button close=(Button) dialog.findViewById(R.id.close) ;
        address.setAdapter(new AlertAddressAdapter(MyCart.this,addressListItems));
        address.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                model = addressListItems.get(position);
                isDeliveryAvailable(model.getPinCode());

            }
        });
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             dialog.cancel();

            }
        });
        dialog.show();
    }

    public void showAlert() {

        final BottomSheetDialog mBottomSheetDialog = new BottomSheetDialog(MyCart.this);
        View sheetView = getLayoutInflater().inflate(R.layout.checkout_popup, null);
        mBottomSheetDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        mBottomSheetDialog.setContentView(sheetView);
        mBottomSheetDialog.setCancelable(false);

        TextView name=(TextView)sheetView.findViewById(R.id.address_name) ;
        TextView address=(TextView)sheetView.findViewById(R.id.address) ;
        TextView address2=(TextView)sheetView.findViewById(R.id.addressline2) ;
        final Button close=(Button) sheetView.findViewById(R.id.close) ;
        final Button date=(Button) sheetView.findViewById(R.id.date) ;
        final Button submit=(Button) sheetView.findViewById(R.id.submit) ;
        final Button slot1=(Button) sheetView.findViewById(R.id.slot1) ;
        final Button slot2=(Button)sheetView.findViewById(R.id.slot2) ;
        final Button slot3=(Button)sheetView.findViewById(R.id.slot3) ;
        final Button slot4=(Button)sheetView.findViewById(R.id.slot4) ;
         name.setText(model.getName());
         address.setText(model.getAddressLine1());
         address2.setText(model.getAddressLine2()+", "+model.getPinCode());
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        String str = sdf.format(new Date());

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              mBottomSheetDialog.cancel();

            }
        });
         date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker(date);

            }
        });
        slot1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                slot1.setBackgroundResource(R.color.grey);
                slot2.setBackground(getDrawable(R.drawable.rect));
                slot3.setBackground(getDrawable(R.drawable.rect));
                slot4.setBackground(getDrawable(R.drawable.rect));
                selectedSlot=slot1.getText().toString();
            }
        });
        slot2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                slot1.setBackground(getDrawable(R.drawable.rect));
                slot2.setBackgroundResource(R.color.grey);
                slot3.setBackground(getDrawable(R.drawable.rect));
                slot4.setBackground(getDrawable(R.drawable.rect));
                selectedSlot=slot2.getText().toString();
            }
        });slot3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                slot1.setBackground(getDrawable(R.drawable.rect));
                slot2.setBackground(getDrawable(R.drawable.rect));
                slot3.setBackgroundResource(R.color.grey);
                slot4.setBackground(getDrawable(R.drawable.rect));
                selectedSlot=slot3.getText().toString();
            }
        });
        slot4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                slot1.setBackground(getDrawable(R.drawable.rect));
                slot2.setBackground(getDrawable(R.drawable.rect));
                slot3.setBackground(getDrawable(R.drawable.rect));
                slot4.setBackgroundResource(R.color.grey);
                selectedSlot=slot4.getText().toString();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if((model!=null)&&(date.getText().length()>0)&&(selectedSlot.length()>0)){
                    progressBar.setVisibility(View.VISIBLE);
                    placeOrder(model.getAddressLine1()+" "+model.getAddressLine2(),model.getPinCode(),date.getText().toString(),selectedSlot);
                    mBottomSheetDialog.cancel();

                }else{
                    if(model==null)
                    {
                        Toast.makeText(MyCart.this,"Please select address",Toast.LENGTH_SHORT).show();
                    } else if(date.getText().length()==0)
                    {
                        Toast.makeText(MyCart.this,"Please select delivery date",Toast.LENGTH_SHORT).show();
                    }
                    else if(selectedSlot.length()==0)
                    {
                        Toast.makeText(MyCart.this,"Please select slot",Toast.LENGTH_SHORT).show();
                    }
                }
                }

        });
        FrameLayout bottomSheet = (FrameLayout) mBottomSheetDialog.getWindow().findViewById(com.google.android.material.R.id.design_bottom_sheet);
        bottomSheet.setBackgroundResource(R.drawable.alert_bg);
        mBottomSheetDialog.show();
    }


    public void isDeliveryAvailable(final String pincode)
    { dialog.cancel();
       progressDialog.show();
       controller.getWebApiCall().postDataWithHeader(Common.getValidatePinCodeUrl,Common.pincodeKeys,new String[]{pincode},this);
    }


    public void getAddressList() {

        Thread T = new Thread(new Runnable() {
            @Override
            public void run() {
                String query =  ""+Common.userIdKey+" = '"+controller.getUserProfil().getUserId()+"'";
                IDataStore<Map> contactStorage = Backendless.Data.of(Common.deliveryAddressTable);
                DataQueryBuilder queryBuilder = DataQueryBuilder.create();
                queryBuilder.setWhereClause(query);
                queryBuilder.setPageSize(100);
                contactStorage.find( queryBuilder, new AsyncCallback<List<Map>>()
                {
                    @Override
                    public void handleResponse(final List<Map>addressList )
                    { addressListItems.clear();
                        if(addressList.size()>0) {
                            for (int i = 0; i < addressList.size(); i++) {
                                Map address = addressList.get(i);
                                addressListItems.add(new DeliveryAddressModel(address));
                            }


                        }

                        Log.i( "MYAPP", "Retrieved " +addressList.size() + " objects" );
                        progressDialog.cancel();

                    }

                    @Override
                    public void handleFault( BackendlessFault fault )
                    {
                        Log.e( "MYAPP", "Server reported an error " + fault );
                        progressDialog.cancel();
                    }
                } );
            }
        });
        T.start();
    }
    public void inilizeProgressDialog()
    {
        progressDialog = new ProgressDialog(this);
        progressDialog .setProgressStyle(ProgressDialog.STYLE_SPINNER);
    }

    public void showDatePicker(final Button btn)
    {
        final Calendar cldr = Calendar.getInstance();
        int day = cldr.get(Calendar.DAY_OF_MONTH);
        int month = cldr.get(Calendar.MONTH);
        int year = cldr.get(Calendar.YEAR);
        // date picker dialog
        final DatePickerDialog picker = new DatePickerDialog(MyCart.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        btn.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);

                    }
                }, year, month, day);
        picker.getDatePicker().setMinDate(System.currentTimeMillis()+86400000);
        picker.show();
    }

    @Override
    public void onSucess(final String value) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressDialog.cancel();
                if(Utils.getStatus(value,MyCart.this))
                {

                    showAlert();
                }else {
                    if(dialog!=null) {
                        dialog.show();
                    }
                }

            }
        });
    }

    @Override
    public void onError(final String value) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressDialog.cancel();
              Toast.makeText(MyCart.this,Utils.getValue(value,"message"),Toast.LENGTH_SHORT).show();
              if(dialog!=null) {
                  dialog.show();
              }
            }
        });
    }
}



