package launchingscreens;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.backendless.exceptions.BackendlessFault;
import com.kitchenbazaar.DashBoard;
import com.kitchenbazaar.R;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import common.AppController;
import common.Common;
import interfaces.WebApiResponseCallback;


/**
 * Created by ashish.kumar on 04-07-2018.
 */

public class Login  extends Activity implements View.OnClickListener, WebApiResponseCallback {
    @BindView(R.id.login_btn)
    Button login;
    @BindView(R.id.signup)
    common.Bold_TextView signUp;
    @BindView(R.id.forgetPassword)
    ImageView forgetPassword;
    @BindView(R.id.emailId)
    EditText emailId;
    @BindView(R.id.password)
    EditText password;
    @BindView(R.id.checkbox)
    CheckBox rememberme;
    AppController controller;
    @BindView(R.id.progressbar)
    ProgressBar progressbar;
    boolean isCalledFromCart=false;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        controller = (AppController) getApplicationContext();
        ButterKnife.bind(this);
        forgetPassword.setOnClickListener(this);
        signUp.setOnClickListener(this);
        login.setOnClickListener(this);
        rememberme.setChecked(true);
        isCalledFromCart=getIntent().getBooleanExtra("isCalledFromCart",false);
        if (controller.getRememberId().length() > 0) {
            emailId.setText(controller.getRememberId());
            password.setText(controller.getRememberpassword());
            emailId.setSelection(controller.getRememberId().length() );
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.signup:
                startActivity(new Intent(this, SignUp.class));

                break;
            case R.id.forgetPassword:
                startActivity(new Intent(this, ForgetPassword.class));

                break;
            case R.id.login_btn:
                if (isAllFieldsValidated()) {
                    if (util.Utils.isNetworkAvailable(Login.this)) {
                        login();
                    }
                }
                break;
        }

    }

    public boolean isAllFieldsValidated() {
        boolean status = false;
        if (controller.getValidation().isPhoneNumberValid(emailId)) {
            if (password.length() > 3) {
                status = true;
            }
        }
        return status;
    }

    public void disableAll() {
        emailId.setEnabled(false);
        password.setEnabled(false);
        login.setVisibility(View.GONE);
    }

    public void enableAll() {
        emailId.setEnabled(true);
        password.setEnabled(true);
        login.setVisibility(View.VISIBLE);
    }
    public void login() {
        disableAll();
        progressbar.setVisibility(View.VISIBLE);
        progressbar.bringToFront();
//        Backendless.UserService.login(emailId.getText().toString(), password.getText().toString(), new AsyncCallback<BackendlessUser>() {
//            public void handleResponse(BackendlessUser user) {
//                // user has been logged in
//                Toast.makeText(Login.this, "Logged in successfully.", Toast.LENGTH_SHORT).show();
//                progressbar.setVisibility(View.GONE);
//                controller.setUserLoggedIn(true);
//                controller.setUserProfile(user);
//                if (rememberme.isChecked()) {
//                    controller.setRememberId(emailId.getText().toString(), password.getText().toString());
//                } else {
//                    controller.setRememberId("", "");
//                }
////                Intent resultIntent = new Intent();
////                setResult(Activity.RESULT_OK, resultIntent);
////                finish();
//                startActivity(new Intent(Login.this, DashBoard.class));
        controller.getWebApiCall().postDataWithHeader(Common.loginUrl,Common.loginKeys,new String[]{emailId.getText().toString(),password.getText().toString()},this);
            }

            public void handleFault(BackendlessFault fault) {
                // login failed, to get the error code call fault.getCode()

            }


    @Override
    public void onSucess(final String value) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(Login.this, "Logged in successfully.", Toast.LENGTH_SHORT).show();
                progressbar.setVisibility(View.GONE);
                controller.setUserLoggedIn(true);
                try {
                    controller.setUserProfile1(new JSONObject(value));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (rememberme.isChecked()) {
                    controller.setRememberId(emailId.getText().toString(), password.getText().toString());
                } else {
                    controller.setRememberId("", "");
                }
                if (isCalledFromCart) {
                    handleResult();
                } else {
                    startActivity(new Intent(Login.this, DashBoard.class));
                }
            }
        });
    }

    @Override
    public void onError(final String value) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(Login.this, "Invalid user name or password", Toast.LENGTH_SHORT).show();
                progressbar.setVisibility(View.GONE);
                enableAll();
            }
        });
    }

    public void handleResult()
    {
        Intent data = new Intent();
        setResult(RESULT_OK, data);
        finish();
    }

    }

