package util;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;
import android.widget.Toast;

import com.kitchenbazaar.DashBoard;
import com.kitchenbazaar.R;

import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Locale;


/**
 * Created by ashish.kumar on 06-07-2018.
 */

public class Utils {
    static String strAdd = "";

    public static boolean isNetworkAvailable(Activity act) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) act.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        if ((activeNetworkInfo != null) && (activeNetworkInfo.isConnected())) {
            return true;
        } else {
            Toast.makeText(act, "Internet Unavailable", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    public static double distance(double lat1, double lon1, double lat2, double lon2, String sr) {


        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        if (sr.equals("K")) {
            dist = dist * 1.609344;
        } else if (sr.equals("N")) {
            dist = dist * 0.8684;
        }
        return (dist);
    }

    public static double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    public static double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }

    public static String getCompleteAddressString(final Activity act, final double LATITUDE, final double LONGITUDE) {
        List<Address> list;
        Geocoder geocoder = new Geocoder(act, Locale.getDefault());
        try {
            list = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);
            strAdd = list.get(0).getSubAdminArea() + "," + list.get(0).getPostalCode();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return strAdd;

    }

    public static void goToDashboard(Activity act) {
        Intent intent = new Intent(act, DashBoard.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        act.startActivity(intent);
    }

    public static String getOrderStatusString(String typ) {
        String value = "";
        int type = Integer.parseInt(typ);
        switch (type) {
            case 1:
                value = "Pending Acceptance from storeowner";
                break;
            case 2:
                value = "Accepted By Store Owner Preparing your order";
                break;
            case 3:
                value = "Your Order is out for delivery,Will be delivered in 2 hours";
                break;
            case 4:
                value = "Cancelled";
                break;
            case 5:
                value = "Your Order has been delivered";
                break;
        }
        return value;
    }

    public static int getColor(String typ) {
        int value = -1;
        int type = Integer.parseInt(typ);
        switch (type) {
            case 1:
                value = R.color.red;
                break;
            case 2:
                value = R.color.yellow;
                break;
            case 3:
                value = R.color.skyblue;
                break;
            case 4:
                value = R.color.red;
                break;
            case 5:
                value = R.color.green;
                break;
        }
        return value;
    }

    public static String getDiscount(Double origionalPrice, Double OfferPrice) {
        long discount = (Math.round(100 - (OfferPrice / origionalPrice) * 100));
        if (discount == 0) {
            return "";
        } else {
            return discount + " % " + System.getProperty("line.separator") + "OFF";
        }
    }

    public static String getAppVersion(Activity activity) {
        String versionName = "";
        int versionCode = -1;
        try {
            PackageInfo packageInfo = activity.getPackageManager().getPackageInfo(activity.getPackageName(), 0);
            versionName = packageInfo.versionName;
            versionCode = packageInfo.versionCode;

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionName;
    }

    public static void displayPromptForEnablingGPS(
            final Activity activity) {
        final AlertDialog.Builder builder =
                new AlertDialog.Builder(activity);
        final String action = Settings.ACTION_LOCATION_SOURCE_SETTINGS;
        final String message = "Your Gps is not enabled, DO you want to enable Gps ?";

        builder.setMessage(message)
                .setPositiveButton("Ok",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface d, int id) {
                                activity.startActivityForResult(new Intent(action), 2);
                                d.dismiss();
                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface d, int id) {
                                activity.finish();
                            }
                        });
        builder.create().show();
        ;
    }

    public static boolean getStatus(String value, Activity act) {
        try {
            JSONObject jsonObject = new JSONObject(value);
            String message=jsonObject.getString("message");
            if((!message.equalsIgnoreCase("null"))) {
                Toast.makeText(act,message , Toast.LENGTH_SHORT).show();
            }
            return jsonObject.getBoolean("Status");
        } catch (Exception ex) {
            ex.fillInStackTrace();
        }
        return false;
    }
    public static String getValue(String value,String key) {
        try {
            JSONObject jsonObject = new JSONObject(value);

            return jsonObject.getString(key);
        } catch (Exception ex) {
            ex.fillInStackTrace();
        }
        return "";
    }
}
