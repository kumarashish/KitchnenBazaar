package model;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import common.Common;

/**
 * Created by ashish.kumar on 06-07-2018.
 */

public class UserProfile {
      String Name;
       String email;
       String phoneNumber;
        String Address;
        String userId;
        String profilePic;

    public UserProfile(SharedPreferences prfs) {
        this.Name = prfs.getString(Common.name, "");
        this.email = prfs.getString(Common.email, "");
        this.phoneNumber = prfs.getString(Common.mobile, "");
        this.Address = prfs.getString(Common.address, "");
        this.userId = prfs.getString(Common.userId, "");
        this.profilePic=prfs.getString(Common.profilePic,"");
    }
    public void setAddress(String address) {
        Address = address;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setName(String name) {
        Name = name;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAddress() {
        return Address;
    }

    public String getEmail() {
        return email;
    }

    public String getUserId() {
        return userId;
    }

    public String getName() {
        return Name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }
}
