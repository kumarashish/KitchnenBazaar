package launchingscreens;

import android.app.Activity;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.kitchenbazaar.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import common.AppController;
import common.Common;
import interfaces.WebApiResponseCallback;


/**
 * Created by ashish.kumar on 05-07-2018.
 */

public class ForgetPassword extends Activity implements View.OnClickListener, WebApiResponseCallback {
    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.emailId)
    EditText emailId;
    @BindView(R.id.submit_btn)
    Button submit;
    AppController controller;
    @BindView(R.id.progressbar)
    ProgressBar progressbar;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forget_password);
        controller=(AppController)getApplicationContext();
        ButterKnife.bind(this);
        back.setOnClickListener(this);
        submit.setOnClickListener(this);
        if(controller.getRememberId().length()>0)
        {
            emailId.setText(controller.getRememberId());
            emailId.setSelection(controller.getRememberId().length());
        }
    }

    @Override
    public void onClick(View view) {
switch (view.getId())
{
    case R.id.back:
        onBackPressed();
        break;
    case R.id.submit_btn:
        if(controller.getValidation().isPhoneNumberValid(emailId))
        {
            if(util.Utils.isNetworkAvailable(ForgetPassword.this))
            {
                resetPassword();
            }
        }
        break;
}
    }
    public void disableAll() {
        emailId.setEnabled(false);
        submit.setVisibility(View.GONE);
    }

    public void enableAll() {
        emailId.setEnabled(true);
        submit.setVisibility(View.VISIBLE);
    }

    public void resetPassword() {
        progressbar.setVisibility(View.VISIBLE);
        disableAll();
        progressbar.bringToFront();
        controller.getWebApiCall().postDataWithHeader(Common.forgetPasswordUrl,Common.forgetPasswordKeys,new String[]{emailId.getText().toString()},this);
//        Backendless.UserService.restorePassword(emailId.getText().toString(), new AsyncCallback<Void>() {
//            public void handleResponse(Void response) {
//                progressbar.setVisibility(View.GONE);
//                Toast.makeText(ForgetPassword.this, "Your password has been reset and new password has been send to ur email id", Toast.LENGTH_SHORT).show();
//                finish();
//            }
//
//            public void handleFault(BackendlessFault fault) {
//                progressbar.setVisibility(View.GONE);
//                Toast.makeText(ForgetPassword.this, fault.getMessage(), Toast.LENGTH_SHORT).show();
//                enableAll();
//            }
//        });
    }

    @Override
    public void onSucess(final String value) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressbar.setVisibility(View.GONE);
                Toast.makeText(ForgetPassword.this, "Your password has been send to ur mobile number", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    @Override
    public void onError(final String value) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(ForgetPassword.this, value, Toast.LENGTH_SHORT).show();
                progressbar.setVisibility(View.GONE);
                enableAll();
            }
        });
    }
}
