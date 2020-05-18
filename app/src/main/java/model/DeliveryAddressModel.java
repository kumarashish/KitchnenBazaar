package model;

import com.kitchenbazaar.DeliveryAddress;

import java.util.Map;

import common.Common;

public class DeliveryAddressModel {
    String name;
    String addressLine1;
    String addressLine2;
    String pinCode;
    String id;

    public DeliveryAddressModel(Map map) {
        name =(String) map.get(Common.addressname);
        addressLine1 = (String)map.get(Common.address1);
        addressLine2 = (String)map.get(Common.address2);
        pinCode =(String) map.get(Common.pinCode);
        id = (String)map.get(Common.objectId);
    }
    public String getAddressLine1() {
        return addressLine1;
    }

    public String getAddressLine2() {
        return addressLine2;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPinCode() {
        return pinCode;
    }

    public void setAddressLine1(String addressLine1) {
        this.addressLine1 = addressLine1;
    }

    public void setAddressLine2(String addressLine2) {
        this.addressLine2 = addressLine2;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPinCode(String pinCode) {
        this.pinCode = pinCode;
    }
}
