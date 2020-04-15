package model;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Map;

/**
 * Created by ashish.kumar on 18-07-2018.
 */

public class CurrentOrderModel {
    java.util.Date date;
    String amount;
    Integer totalItemCount;
    String orderId;
    String address;
    String orderstatus;
    boolean isPaymentDone;
    String storeOwnerMessage="";
    String customermessage ="";

    public CurrentOrderModel(Map map)
    {
        date=  (java.util.Date)map.get("created");
        amount= (String) map.get("totalamount");
        totalItemCount= (Integer) map.get("TotalItemsCount");
        orderId= (String) map.get("objectId");
        address=(String) map.get("address")+", "+(String) map.get("pincode");
        orderstatus=(String) map.get("orderstatus");
        isPaymentDone=(Boolean) map.get("isPaymentDone");
        try{
            storeOwnerMessage=(String) map.get("store_owner_message");
        }catch (Exception ex)
        {
            ex.fillInStackTrace();
        }
        try{
            customermessage=(String) map.get("customermessage");
        }catch (Exception ex)
        {
            ex.fillInStackTrace();
        }
    }

    public String getCustomermessage() {
        return customermessage;
    }

    public String getStoreOwnerMessage() {
        return storeOwnerMessage;
    }

    public boolean isPaymentDone() {
        return isPaymentDone;
    }

    public String getOrderId() {
        return orderId;
    }

    public String getDate() {
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");

        //to convert Date to String, use format method of SimpleDateFormat class.
        String strDate = dateFormat.format(date);
        return strDate;
    }

    public String getAmount() {
        return amount;
    }

    public int getTotalItemCount() {
        return totalItemCount;
    }

    public String getAddress() {
        return address;
    }

    public String getOrderstatus() {
        return orderstatus;
    }
}
