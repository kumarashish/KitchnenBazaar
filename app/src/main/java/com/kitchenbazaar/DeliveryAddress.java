package com.kitchenbazaar;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.backendless.Backendless;
import com.backendless.IDataStore;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.DataQueryBuilder;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import adapter.AddressAdapter;
import adapter.GridView_Adapter;
import butterknife.BindView;
import butterknife.ButterKnife;
import common.AppController;
import common.Common;
import interfaces.OnDeleteClicked;
import model.CategoryModel;
import model.DeliveryAddressModel;
import model.OrderModel;
import model.UserProfile;
import util.Utils;

public class DeliveryAddress extends Activity implements View.OnClickListener, OnDeleteClicked {
    AppController controller;
    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.add_address)
    ImageButton add_address;
    ProgressDialog progressBar;
    @BindView(R.id.addressList)
    ListView addressListView;
    @BindView(R.id.noAddress)
    TextView noAddress;
    String nameText="",address1Text="",address2Text="",pincodeText="";
ArrayList<DeliveryAddressModel>addressListItems=new ArrayList<>();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.delivery_address);
        controller = (AppController) getApplicationContext();
        ButterKnife.bind(this);
        back.setOnClickListener(this);
        add_address.setOnClickListener(this);
        inilizeProgressDialog();
        if(Utils.isNetworkAvailable(DeliveryAddress.this))
        {   progressBar.show();
            getAddressList();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                onBackPressed();
                break;
            case R.id.add_address:
                showAddressPopup();
                break;
        }

    }

    public void onBackPressed() {
        Intent data = new Intent();
        setResult(RESULT_OK, data);
        finish();
    }
    public void showAddressPopup() {
        final BottomSheetDialog mBottomSheetDialog = new BottomSheetDialog(DeliveryAddress.this);
        View sheetView = getLayoutInflater().inflate(R.layout.payment_popup, null);
        mBottomSheetDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        mBottomSheetDialog.setContentView(sheetView);
        mBottomSheetDialog.setCancelable(false);
        final EditText address = (EditText) sheetView.findViewById(R.id.address);
        final EditText address2 = (EditText) sheetView.findViewById(R.id.addressline2);
        final EditText name = (EditText) sheetView.findViewById(R.id.address_name);
        final EditText pincode = (EditText) sheetView.findViewById(R.id.pincode);
        Button submit = (Button) sheetView.findViewById(R.id.submit);
        submit.setTypeface(controller.getNormal());
        pincode.setTypeface(controller.getNormal());
        address.setTypeface(controller.getNormal());
        address2.setTypeface(controller.getNormal());
        name.setTypeface(controller.getNormal());
        name.setVisibility(View.VISIBLE);
        address2.setVisibility(View.VISIBLE);
        name.setText(nameText);
        address.setText(address1Text);
        address2.setText(address2Text);
        pincode.setText(pincodeText);
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
                if (isAddressValidation(name, 3)) {
                    if (isAddressValidation(address, 10)) {
                        if (isAddressValidation(address2, 7)) {
                            if (isAddressValidation(pincode, 5)) {
                                mBottomSheetDialog.cancel();
                                progressBar.show();
                               // progressBar.bringToFront();
                                addAddress(name.getText().toString(),address.getText().toString(),address2.getText().toString(),pincode.getText().toString());

                            }
                        }
                    }
                }
            }
        });

        FrameLayout bottomSheet = (FrameLayout) mBottomSheetDialog.getWindow().findViewById(com.google.android.material.R.id.design_bottom_sheet);
        bottomSheet.setBackgroundResource(R.drawable.alert_bg);
        mBottomSheetDialog.show();
    }


    public boolean isAddressValidation(EditText address, int length) {
        if (address.getText().length() > length) {
            if (length == 5) {
                if (address.getText().length() == length+1) {
                    return true;
                } else {
                    Toast.makeText(DeliveryAddress.this, "Please enter valid " + address.getHint().toString(), Toast.LENGTH_SHORT).show();

                    return false;
                }
            } else {
                return true;
            }
        } else {
            if (address.getText().length() == 0) {
                Toast.makeText(DeliveryAddress.this, "Please enter " + address.getHint().toString(), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(DeliveryAddress.this, "Please enter valid " + address.getHint().toString(), Toast.LENGTH_SHORT).show();
            }
        }
        return false;
    }


    public void addAddress(String name, String address1, String address2, String pincode) {
        nameText=name;
        address1Text=address1;
        address2Text=address2;
        pincodeText=pincode;
        final UserProfile profile = controller.getUserProfil();
        HashMap map = null;
        map = new HashMap();
        map.put(Common.addressname, name);
        map.put(Common.address1, address1);
        map.put(Common.address2, address2);
        map.put(Common.pinCode, pincode);
        map.put(Common.userIdKey, profile.getUserId());

        final Map mapp = map;
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                //OrderModel order = Backendless.Persistence.save(model);

                // save object asynchronously
                Backendless.Persistence.of(Common.deliveryAddressTable).save(mapp, new AsyncCallback<Map>() {
                    public void handleResponse(Map response) {
                        Log.d("Response", "handleResponse: " + response);

                        clearText();
                        Toast.makeText(DeliveryAddress.this,"Address added sucessfully.",Toast.LENGTH_SHORT).show();
                        getAddressList();
                    }

                    public void handleFault(BackendlessFault fault) {
                        Log.d("Response", "handleResponse: " + fault);
                         progressBar.show();
                         showAddressPopup();
                         Toast.makeText(DeliveryAddress.this,"Some Error Occured ,Please retry",Toast.LENGTH_SHORT).show();
                        // an error has occurred, the error code can be retrieved with fault.getCode()
                    }
                });
            }
        });
        t.start();
        // save object synchronously

    }

    public void deleteAddress(DeliveryAddressModel model) {
        final String query = "" + Common.objectId + " = '" + model.getId() + "'";
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                //OrderModel order = Backendless.Persistence.save(model);

                // save object asynchronously
                Backendless.Persistence.of(Common.deliveryAddressTable).remove(query, new AsyncCallback<Integer>() {
                    @Override
                    public void handleResponse(Integer objectsDeleted) {
                        Log.i("MYAPP", "Server has deleted " + objectsDeleted + " objects");
                        getAddressList();
                    }

                    @Override
                    public void handleFault(BackendlessFault fault) {
                        Log.e("MYAPP", "Server reported an error - " + fault);
                    }


                });
            }
        });
        t.start();
        // save object synchronously

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

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                addressListView.setVisibility(View.VISIBLE);
                                addressListView.setAdapter(new AddressAdapter(DeliveryAddress.this, addressListItems));
                                noAddress.setVisibility(View.GONE);
                            }
                        });
                    }else{
                        addressListView.setVisibility(View.GONE);
                        noAddress.setVisibility(View.VISIBLE);
                    }

                        Log.i( "MYAPP", "Retrieved " +addressList.size() + " objects" );
                        progressBar.cancel();

                    }

                    @Override
                    public void handleFault( BackendlessFault fault )
                    {
                        Log.e( "MYAPP", "Server reported an error " + fault );
                        progressBar.cancel();
                    }
                } );
            }
        });
        T.start();
    }

    public void clearText()
    {
      nameText="";address2Text="";address1Text="";pincodeText="";
    }

    @Override
    public void onDeleteClicked(final DeliveryAddressModel address) {
     runOnUiThread(new Runnable() {
         @Override
         public void run() {
             progressBar.show();
             deleteAddress(address);
         }
     });
    }

    public void inilizeProgressDialog()
    {
         progressBar = new ProgressDialog(this);
       progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
    }
}
