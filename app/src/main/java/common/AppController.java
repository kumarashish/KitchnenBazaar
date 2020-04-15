package common;

import android.app.Application;
import android.graphics.Typeface;
import android.location.Location;
import android.preference.PreferenceManager;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONObject;

import java.util.ArrayList;

import interfaces.WebApiResponseCallback;
import model.ProductModel;
import model.UserProfile;
import network.WebApiCall;
import util.PrefManager;
import util.Validation;

/**
 * Created by ashish.kumar on 03-07-2018.
 */

public class AppController  extends Application{
    String address="";
    Location currentLocation=null;
    Validation validation=null;
    PrefManager prefManager;
    ArrayList<ProductModel> myCart=new ArrayList<>();
    WebApiCall webApiCall;
    Typeface bold,normal;
    @Override
    public void onCreate() {
        super.onCreate();
        validation=new Validation(this);
        normal = Typeface.createFromAsset(getApplicationContext().getAssets(), "font.ttf");
        bold= Typeface.createFromAsset(getApplicationContext().getAssets(), "bold.ttf");
        Backendless.initApp(this, Defaults.APPID, Defaults.APIKEY);
        prefManager=new   PrefManager(this);
        webApiCall=new WebApiCall(this);
    }

    public void setAddress(String address, Location loc) {
        this.address = address;
        this.currentLocation=loc;
    }
    public void setCurrentAddress(String address) {
        this.address = address;

    }

    public void setLocation(LatLng loc) {
       currentLocation.setLatitude(loc.latitude);
       currentLocation.setLongitude(loc.longitude);
    }
    public String getAddress() {
        return address;
    }

    public Location getCurrentLocation() {
        return currentLocation;
    }

    public Validation getValidation() {
        return validation;
    }

    public void setUserProfile(BackendlessUser user) {
        prefManager.setUserProfile(user);
    }

    public UserProfile getUserProfil() {
        return prefManager.getUserProfile();
    }

    public boolean isUserLoggedIn() {
        return prefManager.isUserLoggedIn();
    }

    public void setUserLoggedIn(boolean isloggedIn) {
        prefManager.setUserLoggedIn(isloggedIn);
    }

    public void setRememberId(String remId, String pass) {
        prefManager.setRememberId(remId, pass);
    }

    public String getRememberId() {
        return prefManager.getRememberId();
    }

    public String getRememberpassword() {
        return prefManager.getRememberpassword();
    }
    public void setLogout()
    {
        setUserLoggedIn(false);
    }

    public ArrayList<ProductModel> getMyCart() {
        return myCart;
    }

    public void clearMyCart() {
        myCart.clear();
    }
    public int getMyCartItemCount() {
        int count=0;
        {
            for(int i=0;i<myCart.size();i++)
            {
                count+=myCart.get(i).getQuantity();
            }
        }
        return count;
    }

    public void upDateMyCart(ProductModel model, int type) {
        switch (type) {
            case 1:
                if (isItemPresent(model.getProductId())) {
                    manageQuatity(model.getProductId(), type);
                } else {

                    myCart.add(model);
                }
                break;
            case 2:
                if (isItemPresent(model.getProductId())) {
                    manageQuatity(model.getProductId(),type);
                }
                break;
        }
    }

    public boolean isItemPresent(String  id) {
        boolean status = false;
        for (int i = 0; i < myCart.size(); i++) {
            if (id.equalsIgnoreCase(myCart.get(i).getProductId().trim())) {
                status = true;
                break;
            }
        }
        return status;
    }

    public int getProductAddedQuantity(String id) {
        int quantity = 0;
        for (int i = 0; i < myCart.size(); i++) {
            if (id.equalsIgnoreCase(myCart.get(i).getProductId().trim())) {
                quantity = myCart.get(i).getQuantity();
                break;
            }
        }
        return quantity;
    }

    public void manageQuatity(String id, int type) {
        for (int i = 0; i < myCart.size(); i++) {
            if (id.equalsIgnoreCase(myCart.get(i).getProductId().trim())) {
                if (type == 1) {
                    myCart.get(i).increaseQuantity();
                } else {
                    myCart.get(i).decreaseQuantity();
                    if (myCart.get(i).getQuantity() == 0) {
                        myCart.remove(i);
                    }
                }
                break;
            }
        }

    }

    public Typeface getBold() {
        return bold;
    }

    public Typeface getNormal() {
        return normal;
    }

    public WebApiCall getWebApiCall() {
        return webApiCall;
    }
}
