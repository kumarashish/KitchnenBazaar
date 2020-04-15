package launchingscreens;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.kitchenbazaar.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import common.AppController;

import util.Utils;
import util.Validation;

/**
 * Created by ashish.kumar on 04-07-2018.
 */

public class SignUp  extends Activity implements View.OnClickListener{
    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.Name)
    EditText Name;
    @BindView(R.id.emailId)
    EditText emailId;
    @BindView(R.id.mobile)
    EditText mobile;
    @BindView(R.id.password)
    EditText password;
    @BindView(R.id.address)
    EditText address;
    @BindView(R.id.submit_btn)
    Button submit_btn;
    @BindView(R.id.progressbar)
    ProgressBar progressbar;
    AppController controller;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);
        ButterKnife.bind(this);
        back.setOnClickListener(this);
        submit_btn.setOnClickListener(this);
        controller=(AppController)getApplicationContext();

    }

    public void disableAll() {
        Name.setEnabled(false);

        emailId.setEnabled(false);

        mobile.setEnabled(false);

        password.setEnabled(false);

        address.setEnabled(false);

        submit_btn.setVisibility(View.GONE);
    }

    public void enableAll() {
        Name.setEnabled(true);

        emailId.setEnabled(true);

        mobile.setEnabled(true);

        password.setEnabled(true);

        address.setEnabled(true);

        submit_btn.setVisibility(View.VISIBLE);
    }
    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.back:
                onBackPressed();
                break;
            case R.id.submit_btn:
                if(isAllFieldsValidated())
                {
                    if(Utils.isNetworkAvailable(SignUp.this))
                    {
                        callRegister();
                    }
                }

                break;
        }
    }

    public void callRegister() {
        disableAll();
        progressbar.setVisibility(View.VISIBLE);
        progressbar.bringToFront();
        BackendlessUser user = new BackendlessUser();
        user.setEmail(emailId.getText().toString());
        user.setPassword(password.getText().toString());
        user.setProperty("name", Name.getText().toString());
        user.setProperty("phoneNumber", mobile.getText().toString());
        user.setProperty("Address", address.getText().toString());


        Backendless.UserService.register(user, new AsyncCallback<BackendlessUser>() {
            @Override
            public void handleResponse(BackendlessUser response) {
                progressbar.setVisibility(View.GONE);
                Toast.makeText(SignUp.this, "Registered Successfully.", Toast.LENGTH_LONG).show();
                controller.setUserLoggedIn(true);
                controller.setUserProfile(response);
                Intent resultIntent = new Intent();
                setResult(Activity.RESULT_OK, resultIntent);
                finish();

            }

            @Override
            public void handleFault(BackendlessFault fault) {
                progressbar.setVisibility(View.GONE);
                Toast.makeText(SignUp.this, fault.getMessage(), Toast.LENGTH_LONG).show();
                enableAll();
            }
        });

    }

    public boolean isAllFieldsValidated() {
        Validation validation = controller.getValidation();
        boolean status = false;
        if (validation.isNotNull(Name, "Name")) {
            if (validation.isEmailIdValid(emailId)) {
                if (validation.isPhoneNumberValid(mobile)) {
                    if (validation.isAddressValid(address)) {
                        if (validation.isPasswordValid(password)) {
                            status = true;
                        }
                    }
                }
            }
        }
        return status;
    }
}