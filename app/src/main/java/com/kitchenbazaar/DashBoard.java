package com.kitchenbazaar;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.tabs.TabLayout;
import androidx.viewpager.widget.ViewPager;
import android.util.Log;
import android.view.View;
import com.google.android.material.navigation.NavigationView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.IDataStore;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.DataQueryBuilder;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import adapter.GridView_Adapter;
import adapter.ViewPagerAdapter;
import common.AppController;
import common.Common;
import interfaces.OnCategorySelected;
import launchingscreens.Login;
import launchingscreens.SignUp;
import model.CategoryModel;
import util.Utils;

public class DashBoard extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,View.OnClickListener, OnCategorySelected {
AppController controller;
int i=0;
    ViewPager viewPager;
    GridView todays_deal,trendingCategory,category;
    RelativeLayout user_loggedin;
    LinearLayout user_notloggedin;
    Button signUp_btn,login_btn;
    common.Bold_TextView userName;
    common.DetailsCustomTextView location;
    NavigationView navigationView;
    ArrayList<CategoryModel> catList=new ArrayList<>();
    ArrayList<CategoryModel> trendList=new ArrayList<>();
     ImageView editLocation;
    ProgressBar progress;
    ImageView profilePic;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        View logo = getLayoutInflater().inflate(R.layout.header, null);
        location=( common.DetailsCustomTextView )logo.findViewById(R.id.location);
        editLocation=(ImageView) logo.findViewById(R.id.edit_location);
        toolbar.addView(logo);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        controller=(AppController)getApplicationContext();

        View dashBoard = (View) findViewById(R.id.app_bar_dash_board);
        View contentDahboard = (View) dashBoard.findViewById(R.id.contentDashboard);
        progress=(ProgressBar) contentDahboard.findViewById(R.id.progress);
         viewPager = (ViewPager) contentDahboard.findViewById(R.id.ViewPager);
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(this);
        viewPager.setAdapter(viewPagerAdapter);
        TabLayout tabLayout = (TabLayout) contentDahboard.findViewById(R.id.indicator);
        todays_deal=(GridView) contentDahboard.findViewById(R.id.todays_deal);
        trendingCategory=(GridView) contentDahboard.findViewById(R.id.trending_category);
        category=(GridView) contentDahboard.findViewById(R.id.category);
//        category.setAdapter(new GridView_Adapter(DashBoard.this,6));
//        trendingCategory.setAdapter(new GridView_Adapter(DashBoard.this,5));
//        todays_deal.setAdapter(new GridView_Adapter(DashBoard.this,6));
        tabLayout.setupWithViewPager(viewPager, true);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

         navigationView = (NavigationView) findViewById(R.id.nav_view);
        View header=navigationView.inflateHeaderView(R.layout.nav_header_dash_board);
        profilePic=(ImageView)header.findViewById(R.id.profilePic);
        user_loggedin=(RelativeLayout)header.findViewById(R.id.user_loggedin);
        user_notloggedin=(LinearLayout)header.findViewById(R.id.user_notloggedin);
         signUp_btn=(Button)header.findViewById(R.id.signUp_btn);
        login_btn=(Button)header.findViewById(R.id. login_btn);;
         userName=(common.Bold_TextView)header.findViewById(R.id. userName);
        if (controller.isUserLoggedIn()) {
            user_loggedin.setVisibility(View.VISIBLE);
            user_notloggedin.setVisibility(View.GONE);
            userName.setText(controller.getUserProfil().getName());
            if ((controller.getUserProfil().getProfilePic() != null) &&(controller.getUserProfil().getProfilePic().length()>0)){
                Picasso.with(DashBoard.this).load(controller.getUserProfil().getProfilePic()).placeholder(R.drawable.default_icon).into(profilePic);
            }
        } else {
            user_notloggedin.setVisibility(View.VISIBLE);
            user_loggedin.setVisibility(View.GONE);

        }
        signUp_btn.setOnClickListener(this);
        login_btn.setOnClickListener(this);
        navigationView.setNavigationItemSelectedListener(this);
        MyTimerTask yourTask = new MyTimerTask();
        Timer t = new Timer();

        t.scheduleAtFixedRate(yourTask, 0, 10000);  // 10 sec interval

        if (controller.isUserLoggedIn()) {
            navigationView.inflateMenu(R.menu.activity_dash_board_drawer);
        } else {
            navigationView.inflateMenu(R.menu.common);
        }
        editLocation.setOnClickListener(this);
        checkData();
    }

public void checkData()
{  location.setText(controller.getAddress());
    if (Utils.distance(controller.getCurrentLocation().getLatitude(), controller.getCurrentLocation().getLongitude(), Common.storeLat, Common.storeLon, "K") < 10000.0) {

        getData();
        getTrending();
    }else{
        showLocationAlert();
    }
}


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }



    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.profile) {
            startActivityForResult(new Intent(this,MyProfile.class),5);
        }  else if (id == R.id.order) {
            startActivity(new Intent(this,CurrentOrder.class));

        } else if (id == R.id.history) {
            startActivity(new Intent(this,History.class));
        }  else if (id == R.id.contact_us) {
            startActivity(new Intent(this,ContactUs.class));

        } else if (id == R.id.cart) {
            startActivity(new Intent(this,MyCart.class));

        }else if (id == R.id.logout) {
            showAlert();

        }
        else if (id == R.id.changePassword) {
            startActivityForResult(new Intent(this,ChangePassword.class),1);

        }
        else if (id == R.id.delivery_address) {
            startActivity(new Intent(this,DeliveryAddress.class));

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.login_btn:
                startActivityForResult(new Intent(DashBoard.this, Login.class),1);
                break;
            case R.id.signUp_btn:
                startActivityForResult(new Intent(DashBoard.this, SignUp.class),2);

                break;
            case R.id.edit_location:
                startActivityForResult(new Intent(DashBoard.this,LocationSearch.class),3);
                break;
        }
    }

    @Override
    public void onCategoryClicked(final CategoryModel model) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Intent in=new Intent(DashBoard.this,ProductList.class);
                in.putExtra(Common.categoryId,model.getCategoryId());
                in.putExtra(Common.categoryName,model.getCategoryName());
                startActivity(in);

            }
        });


    }
    public void showAlert() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.logout);
        final Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Button no = (Button) dialog.findViewById(R.id.no);
        Button yes = (Button) dialog.findViewById(R.id.yes);
        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
            }
        });
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                controller.setLogout();
                updateheader();
                Toast.makeText(DashBoard.this,"Logged out sucessfully",Toast.LENGTH_SHORT).show();
                dialog.cancel();
            }
        });
        dialog.show();
    }
    class MyTimerTask extends TimerTask {
        public void run() {
            // do whatever you want in hereif(i==3)
            {
                i=0;

            }

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    viewPager.setCurrentItem(i);
                    i++;
                }
            });

        }
    }
public void updateheader()
{   navigationView.getMenu().clear(); //clear old inflated items.
    if (controller.isUserLoggedIn()) {
        user_loggedin.setVisibility(View.VISIBLE);
        user_notloggedin.setVisibility(View.GONE);
        userName.setText(controller.getUserProfil().getName());
        navigationView.inflateMenu(R.menu.activity_dash_board_drawer);
    } else {
        user_notloggedin.setVisibility(View.VISIBLE);
        user_loggedin.setVisibility(View.GONE);
        navigationView.inflateMenu(R.menu.common);

    }
}
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if((requestCode==1)&&( resultCode==RESULT_OK))
        {
              updateheader();
        }else if ((requestCode==2)&&( resultCode==RESULT_OK))
        {
            updateheader();
        }else if((requestCode==3)||(requestCode==4))
        {
            checkData();
        } else if ((requestCode == 5)) {
            if ((controller.getUserProfil().getProfilePic() != null) &&(controller.getUserProfil().getProfilePic().length()>0)) {
                Picasso.with(DashBoard.this).load(controller.getUserProfil().getProfilePic()).placeholder(R.drawable.default_icon).into(profilePic);
            }
            }

    }
    public void getData() {
        progress.setVisibility(View.VISIBLE);
        progress.bringToFront();
        Thread T = new Thread(new Runnable() {
            @Override
            public void run() {
                String query =  ""+Common.categoryColumn+"="+Common.category;
                IDataStore<Map> contactStorage = Backendless.Data.of(Common.categoryTable);
                DataQueryBuilder queryBuilder = DataQueryBuilder.create();
                queryBuilder.setWhereClause(query);
                queryBuilder.setPageSize(100);
                contactStorage.find( queryBuilder, new AsyncCallback<List<Map>>()
                {
                    @Override
                    public void handleResponse( List<Map>categoryList )
                    {   catList.clear();
                        for(int i=0;i<categoryList.size();i++)
                        {
                            Map category=categoryList.get(i);
                            catList.add(new CategoryModel(category));
                        }

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                progress.setVisibility(View.GONE);
                                category.setAdapter(new GridView_Adapter(DashBoard.this, catList));
                            }
                        });

                        Log.i( "MYAPP", "Retrieved " +categoryList .size() + " objects" );
                    }

                    @Override
                    public void handleFault( BackendlessFault fault )
                    {
                        Log.e( "MYAPP", "Server reported an error " + fault );
                    }
                } );
            }
        });
        T.start();
    }
    public void getTrending() {

        Thread T = new Thread(new Runnable() {
            @Override
            public void run() {
                String query =  ""+Common.categoryColumn+"="+Common.trendingCategory;
                IDataStore<Map> contactStorage = Backendless.Data.of(Common.categoryTable);
                DataQueryBuilder queryBuilder = DataQueryBuilder.create();
                queryBuilder.setWhereClause(query);
                queryBuilder.setPageSize(100);
                contactStorage.find( queryBuilder, new AsyncCallback<List<Map>>()
                {
                    @Override
                    public void handleResponse( List<Map>categoryList )
                    {trendList.clear();
                        for(int i=0;i<categoryList.size();i++)
                        {
                            Map category=categoryList.get(i);
                            trendList.add(new CategoryModel(category));
                        }

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                trendingCategory.setAdapter(new GridView_Adapter(DashBoard.this, trendList));
                            }
                        });

                        Log.i( "MYAPP", "Retrieved " +categoryList .size() + " objects" );
                    }

                    @Override
                    public void handleFault( BackendlessFault fault )
                    {
                        Log.e( "MYAPP", "Server reported an error " + fault );
                    }
                } );
            }
        });
        T.start();
    }

    public void showLocationAlert() {

       final BottomSheetDialog mBottomSheetDialog = new BottomSheetDialog(DashBoard.this);
        View sheetView = getLayoutInflater().inflate(R.layout.update_location_alert, null);
        mBottomSheetDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        mBottomSheetDialog.setContentView(sheetView);
        mBottomSheetDialog.setCancelable(false);
        Button update = (Button) sheetView.findViewById(R.id.update);
        Button close = (Button) sheetView.findViewById(R.id.close);
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivityForResult(new Intent(DashBoard.this, LocationSearch.class), 4);
                mBottomSheetDialog.cancel();
            }
        });
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();
            }
        });
        FrameLayout bottomSheet = (FrameLayout) mBottomSheetDialog.getWindow().findViewById(com.google.android.material.R.id.design_bottom_sheet);
        bottomSheet.setBackgroundResource(R.drawable.alert_bg);
        mBottomSheetDialog.show();
    }



}
