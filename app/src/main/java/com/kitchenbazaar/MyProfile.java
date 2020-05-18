package com.kitchenbazaar;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.Nullable;

import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.dhaval2404.imagepicker.ImagePicker;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URI;

import butterknife.BindView;
import butterknife.ButterKnife;
import common.AppController;
import common.Common;
import de.hdodenhof.circleimageview.CircleImageView;
import interfaces.WebApiResponseCallback;
import model.UserProfile;
import util.Utils;

/**
 * Created by ashish.kumar on 16-07-2018.
 */

public class MyProfile extends Activity implements View.OnClickListener, WebApiResponseCallback {
    AppController controller;
    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id. emailId)
    EditText emailId;
    @BindView(R.id. mobile)
    EditText  mobile;
    @BindView(R.id. address)
    EditText address;
    @BindView(R.id.location)
    TextView location;
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.appversion)
    TextView appVersion;
    @BindView(R.id.profilePic)
    CircleImageView profilePic;
    String userId;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);
        controller=(AppController)getApplicationContext();
        ButterKnife.bind(this);
        back.setOnClickListener(this);
        profilePic.setOnClickListener(this);
        initializeAll();
    }
   public void initializeAll()
   {
       UserProfile profile=controller.getUserProfil();
       emailId.setText(profile.getEmail());
       mobile.setText(profile.getPhoneNumber());
       address.setText(profile.getAddress());
       name.setText(profile.getName());
       location.setText(controller.getAddress());
       appVersion.setText("App Version : "+Utils.getAppVersion(MyProfile.this));
   }
    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.back:
                onBackPressed();
                break;
            case R.id.profilePic:
                ImagePicker.Companion.with(this)
                        .crop()	    			//Crop image(Optional), Check Customization for more option
                        .compress(1024)			//Final image size will be less than 1 MB(Optional)
                        .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                        .start();
                break;
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
          Uri uri=data.getData();
          profilePic.setImageURI(uri);
          uploadProfilePic(uri);
        }
    }

    private String getBase64(Uri imageUri) {
        final InputStream imageStream;
        try {
            imageStream = getContentResolver().openInputStream(imageUri);
            final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            selectedImage.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] b = baos.toByteArray();
            String encImage = Base64.encodeToString(b, Base64.DEFAULT);

            return encImage;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }
//    public String getImageBase64(String imageUrl) {
//
//        // File imageFile = new File(imageUrl);
//        BitmapFactory.Options options = new BitmapFactory.Options();
//        options.inSampleSize = 8;
//        Bitmap myBitmap = BitmapFactory.decodeFile(imageUrl,options);
//        ByteArrayOutputStream baos=new ByteArrayOutputStream();
//        myBitmap.compress(Bitmap.CompressFormat.PNG,100, baos);
//        byte [] b=baos.toByteArray();
//        String temp= Base64.encodeToString(b, Base64.DEFAULT);
//        System.out.print(temp);
//        return temp;
//    }
    public void uploadProfilePic(Uri uri)
    {
        String encodedImage = getBase64(uri);
       controller.getWebApiCall().postDataWithJSON(Common.profilePicUrl,getJSONObject(encodedImage).toString(),MyProfile.this);

    }

    public JSONArray getJSONObject(String base64) {
        JSONArray jsonArray=new JSONArray();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("UploadedBy", controller.getUserProfil().getUserId());
            jsonObject.put("ImgStr", base64);
            jsonObject.put("IsProfileImage", true);
            jsonObject.put("UploadedOn", "2020-05-17T15:03:59.2296782+05:30");
            jsonArray.put(0,jsonObject);
        } catch (Exception ex) {
            ex.fillInStackTrace();
        }
        return jsonArray;
    }

    @Override
    public void onSucess(final String value) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(MyProfile.this,value,Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onError(final String value) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(MyProfile.this,value,Toast.LENGTH_SHORT).show();
            }
        });
    }
}
