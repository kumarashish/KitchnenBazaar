package com.kitchenbazaar;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.backendless.Backendless;
import com.backendless.IDataStore;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.DataQueryBuilder;

import java.util.List;
import java.util.Map;

import adapter.OrderItemAdapter;
import butterknife.BindView;
import butterknife.ButterKnife;
import common.AppController;
import common.Common;
import model.OrderItemModel;
import model.UserProfile;
import util.Utils;

/**
 * Created by ashish.kumar on 16-07-2018.
 */

public class MyProfile extends Activity implements View.OnClickListener {
    AppController controller;
    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id. emailId)
    EditText emailId;
    @BindView(R.id. mobile)
    EditText  mobile;
    @BindView(R.id. address)
    EditText address;
    @BindView(R.id.location)
    TextView location;
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.appversion)
    TextView appVersion;
    String userId;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);
        controller=(AppController)getApplicationContext();
        ButterKnife.bind(this);
        back.setOnClickListener(this);
        initializeAll();
    }
   public void initializeAll()
   {
       UserProfile profile=controller.getUserProfil();
       emailId.setText(profile.getEmail());
       mobile.setText(profile.getPhoneNumber());
       address.setText(profile.getAddress());
       name.setText(profile.getName());
       location.setText(controller.getAddress());
       appVersion.setText("App Version : "+Utils.getAppVersion(MyProfile.this));
   }
    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.back:
                onBackPressed();
                break;
        }

    }
}
