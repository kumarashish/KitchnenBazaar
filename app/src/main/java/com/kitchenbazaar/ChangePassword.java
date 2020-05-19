package com.kitchenbazaar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import common.AppController;
import common.Common;
import interfaces.WebApiResponseCallback;
import launchingscreens.Login;
import util.Utils;

public class ChangePassword extends Activity implements View.OnClickListener, WebApiResponseCallback {
    AppController controller;
    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.oldpassword)
    EditText oldPassword;
    @BindView(R.id.newpassword)
    EditText newPassword;
    @BindView(R.id.confirmpassword)
    EditText confirmPassword;
    @BindView(R.id.submit_btn)
    Button submit;
    @BindView(R.id.progressbar)
    ProgressBar progressBar;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_password);
        controller = (AppController) getApplicationContext();
        ButterKnife.bind(this);
        back.setOnClickListener(this);
        submit.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                onBackPressed();
                break;
            case R.id.submit_btn:
                if(isAllFieldsVAlidated())
                {
                    if(Utils.isNetworkAvailable(ChangePassword.this))
                    {  submit.setVisibility(View.GONE);
                       progressBar.setVisibility(View.VISIBLE);
                        controller.getWebApiCall().postDataWithHeader(Common.changePassword,Common.changePasswordKeys,new String[]{controller.getUserProfil().getUserId(),oldPassword.getText().toString(),newPassword.getText().toString()},this);
                    }
                }
                break;
        }

    }

    public boolean isAllFieldsVAlidated() {
        if (oldPassword.getText().length() > 0) {
            if (newPassword.getText().length() > 0) {
                if (confirmPassword.getText().length() > 0) {
                    if ((newPassword.getText().toString()).equals(confirmPassword.getText().toString())) {
                        return true;
                    } else {
                        Toast.makeText(ChangePassword.this, "New Password and confirm password mus be same.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(ChangePassword.this, "Please confirm password.", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(ChangePassword.this, "Please enter new password.", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(ChangePassword.this, "Please enter old password.", Toast.LENGTH_SHORT).show();
        }
        return false;
    }


    @Override
    public void onSucess(final String value) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
              if(Utils.getStatus(value,ChangePassword.this))
              {
                  controller.setLogout();
                  Intent in=new Intent();
                  setResult(RESULT_OK, in);
                  finish();
              }
            }
        });

    }

    @Override
    public void onError(final String value) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                submit.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
               Utils.getStatus(value,ChangePassword.this);

            }
        });
    }
}