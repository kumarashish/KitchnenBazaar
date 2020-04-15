package launchingscreens;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.kitchenbazaar.DashBoard;
import com.kitchenbazaar.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import common.AppController;



/**
 * Created by ashish.kumar on 04-07-2018.
 */

public class Login  extends Activity implements View.OnClickListener{
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
        if (controller.getValidation().isEmailIdValid(emailId)) {
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
        Backendless.UserService.login(emailId.getText().toString(), password.getText().toString(), new AsyncCallback<BackendlessUser>() {
            public void handleResponse(BackendlessUser user) {
                // user has been logged in
                Toast.makeText(Login.this, "Logged in successfully.", Toast.LENGTH_SHORT).show();
                progressbar.setVisibility(View.GONE);
                controller.setUserLoggedIn(true);
                controller.setUserProfile(user);
                if (rememberme.isChecked()) {
                    controller.setRememberId(emailId.getText().toString(), password.getText().toString());
                } else {
                    controller.setRememberId("", "");
                }
//                Intent resultIntent = new Intent();
//                setResult(Activity.RESULT_OK, resultIntent);
//                finish();
                startActivity(new Intent(Login.this, DashBoard.class));
            }

            public void handleFault(BackendlessFault fault) {
                // login failed, to get the error code call fault.getCode()
                Toast.makeText(Login.this, fault.getMessage(), Toast.LENGTH_SHORT).show();
                progressbar.setVisibility(View.GONE);
                enableAll();
            }


        });
    }
}
