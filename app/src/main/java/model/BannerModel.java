package model;

import org.json.JSONException;
import org.json.JSONObject;

public class BannerModel {

    private String bannerUrl;

    public BannerModel(JSONObject jsonObject) {
        try {
            this.bannerUrl = jsonObject.getString("SavedImagePath");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getImgStr() {
        return bannerUrl;
    }

    public void setImgStr(String imgStr) {
        this.bannerUrl = imgStr;
    }


}






