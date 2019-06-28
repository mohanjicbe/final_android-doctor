package com.orane.docassist.Home;

import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.flurry.android.FlurryAgent;

import com.google.firebase.analytics.FirebaseAnalytics;

import com.orane.docassist.AnsweredQueriesActivity;
import com.orane.docassist.CommonActivity;
import com.orane.docassist.Consultation_Activity_New;
import com.orane.docassist.HotlineHome;
import com.orane.docassist.InviteDoctorActivity;
import com.orane.docassist.LoginActivity;
import com.orane.docassist.Model.Model;
import com.orane.docassist.MyApp;
import com.orane.docassist.MyClinicActivity;
import com.orane.docassist.MyPatientActivity;
import com.orane.docassist.MyWalletActivity;
import com.orane.docassist.Network.Connectivity;
import com.orane.docassist.Network.JSONParser;
import com.orane.docassist.Network.ShareIntent;
import com.orane.docassist.NewQueriesActivity;
import com.orane.docassist.Profile_Activity;
import com.orane.docassist.QR_Code_Activity;
import com.orane.docassist.R;
import com.orane.docassist.SendApptHome;
import com.orane.docassist.SendApptSlot1;
import com.orane.docassist.SentMsgActivity;
import com.orane.docassist.SettingsActivity;
import com.orane.docassist.ShareToFriends;
import com.orane.docassist.Video_WebViewActivity;
import com.orane.docassist.adapter.ViewPagerAdapter;
import com.orane.docassist.file_picking.utils.FileUtils;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;
import org.jsoup.Jsoup;

import java.io.InputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import me.drakeet.materialdialog.MaterialDialog;


public class Home_MainActivity extends AppCompatActivity {
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;
    private int mCurrentSelectedPosition;
    ImageView imgapp;
    Intent i;
    TextView tvappverview, tvaabtq, tvaabtq1, tvaabtq2, tvaabtq3;
    AlertDialog alertDialog;
    TabLayout tabLayout;
    LinearLayout wallet_layout;

    PendingIntent pIntent;
    Intent intent;
    ViewPager vpPager;
    JSONObject jsonobj;
    ImageView profile_img;
    Typeface robo_reg, robo_bold;
    JSONObject gcm_json_resp;
    Calendar calendar;
    ViewPager viewPager;

    private Context mContext;
    private Resources mResources;
    private RelativeLayout mRelativeLayout;
    private Button mBTN;
    private ImageView mImageView;
    private Bitmap mBitmap;
    boolean doubleBackToExitPressedOnce = false;
    boolean is_show_alert = true;

    HomeFragment homeFragment;
    QueriesFragment queriesFragment;
    QasesFragment qasesFragment;
    BookingsFragment bookingFragment;

    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs";
    public static final String Login_Status = "Login_Status_key";
    public static final String user_name = "user_name_key";
    public static final String Name = "Name_key";
    public static final String password = "password_key";
    public static final String isValid = "isValid";
    public static final String id = "id";
    public static final String browser_country = "browser_country";
    public static final String email = "email";
    public static final String fee_q = "fee_q";
    public static final String fee_consult = "fee_consult";
    public static final String fee_q_inr = "fee_q_inr";
    public static final String fee_consult_inr = "fee_consult_inr";
    public static final String currency_symbol = "currency_symbol";
    public static final String currency_label = "currency_label";
    public static final String have_free_credit = "have_free_credit";
    public static final String photo_url = "photo_url";
    public static final String sp_km_id = "sp_km_id_key";
    public static final String sp_share_link = "sp_share_link";
    public static final String sensappt_home_flag = "sensappt_home_flag_key";
    public static final String update_status = "update_status_key";
    public static final String update_alert_time = "update_alert_time_key";
    public static final String noti_status = "noti_status_key";
    public static final String noti_sound = "noti_sound_key";
    public static final String do_like_pulse = "do_like_pulse_key";
    public static final String token = "token_key";

    public String sms_details, currentVersion, gcm_url, Log_Status_val, uname, docname, pass, network_type, update_status_txt, update_alert_time_txt;
    public String Log_Status;
    JSONObject json;
    TextView tvtips1, tvtips2, tvtips3, cursymbol, tvbalance, emailid, tvuname;

    public SlidingTabLayout slidingTabLayout;
    public String noti_sound_val, stop_noti_val;
    private static final int CAMERA_REQUEST = 1888;
    private static final String TAG = "FileChooserExampleActivity";
    private static final int REQUEST_CODE = 6384; // onActivityResult request


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity_main);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.app_color2));
        }

        robo_reg = Typeface.createFromAsset(getAssets(), Model.font_name);
        robo_bold = Typeface.createFromAsset(getAssets(), Model.font_name_bold);

        //================ Shared Pref ======================
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        Log_Status = sharedpreferences.getString(Login_Status, "");
        uname = sharedpreferences.getString(user_name, "");
        docname = sharedpreferences.getString(Name, "");
        Model.name = sharedpreferences.getString(Name, "");
        pass = sharedpreferences.getString(password, "");
        Model.id = sharedpreferences.getString(id, "");
        Model.email = sharedpreferences.getString(email, "");
        Model.browser_country = sharedpreferences.getString(browser_country, "");
        Model.fee_q = sharedpreferences.getString(fee_q, "");
        Model.fee_consult = sharedpreferences.getString(fee_consult, "");
        Model.currency_label = sharedpreferences.getString(currency_label, "");
        Model.currency_symbol = sharedpreferences.getString(currency_symbol, "");
        Model.have_free_credit = sharedpreferences.getString(have_free_credit, "");
        Model.photo_url = sharedpreferences.getString(photo_url, "");
        Model.kmid = sharedpreferences.getString(sp_km_id, "");
        Model.like_pulse = sharedpreferences.getString(do_like_pulse, "");
        Model.sensappt_home_flag = sharedpreferences.getString(sensappt_home_flag, "yes");
        Model.token = sharedpreferences.getString(token, "");
        //============================================================

        try {
           /* //----------------------------------------------------------------------------------------
            GCMClientManager pushClientManager = new GCMClientManager(this, Model.PROJECT_NUMBER);
            pushClientManager.registerIfNeeded(new GCMClientManager.RegistrationCompletedHandler() {
                @Override
                public void onSuccess(String registrationId, boolean isNewRegistration) {
                    //-------------------------------------------------------------------
                    System.out.println("Registration id=================" + registrationId);
                    gcm_url = Model.BASE_URL + "sapp/updateDeviceRegId?reg_id=" + registrationId + "&user_id=" + (Model.id) + "&v=" + Model.app_ver + "&token=" + Model.token;
                    System.out.println("GCM url------->" + gcm_url);
                    new JSON_GCM_Regid().execute(gcm_url);
                    //-------------------------------------------------------------------
                }

                @Override
                public void onFailure(String ex) {
                    super.onFailure(ex);
                }
            });
            //----------------------------------------------------------------------------------------
*/

            //------------ Google firebase Analitics--------------------
            Model.mFirebaseAnalytics = FirebaseAnalytics.getInstance(getApplicationContext());
            Bundle params = new Bundle();
            params.putString("User", Model.id + "/" + Model.name);
            Model.mFirebaseAnalytics.logEvent("home_reach", params);
            //------------ Google firebase Analitics--------------------

            //----------- Flurry -------------------------------------------------
            Map<String, String> articleParams = new HashMap<String, String>();
            articleParams.put("user", Model.id + "/" + Model.name);
            FlurryAgent.logEvent("android.doc.home_reach", articleParams);
            //----------- Flurry -------------------------------------------------


        } catch (Exception e) {
            e.printStackTrace();
        }

/*        //------------- Set Doctor ------------------------------------
        Model.id = "305069";  //Shanmuga
        Model.token = "";
        //============================================================
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(id, "305069");
        editor.putString(token, "a020ed3c430f86145e6f3d91677c6cc2-b2a4626488de0df7af2979c24d54d32a");
        editor.apply();
        //============================================================*/

        //------------- Set Doctor ------------------------------------
        System.out.println("OnStart_stop_noti_val----" + stop_noti_val);
        System.out.println("OnStart_noti_sound_val----" + noti_sound_val);

        slidingTabLayout = (SlidingTabLayout) findViewById(R.id.sliding_tabs);

        //----------------------- initialize Tabs ----------------------------------
        setUpNavigationDrawer();
        //setTabs(3);
        Setup_viewPager();
        //mNavigationView.setCheckedItem(R.id.navigation_item_4);
        //------------------------ initialize Tabs ----------------------------------

        //getSMS();

        try {

            //new GetVersionCode().execute();

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                getWindow().setStatusBarColor(getResources().getColor(R.color.app_color2));
            }

            if ((Model.id) != null && !(Model.id).isEmpty() && !(Model.id).equals("null") && !(Model.id).equals("")) {
                System.out.println("Model.id----Ok-----");
            } else {
                force_logout();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        try {

            //--------------Screen Size----------------
            Display display = getWindowManager().getDefaultDisplay();
            int width = display.getWidth();
            int height = display.getHeight();
            String screen = "" + width + height;


            Connectivity cn = new Connectivity();
            if (Connectivity.isConnectedWifi(getApplicationContext())) {
                network_type = "Wifi";
            }
            if (Connectivity.isConnectedMobile(getApplicationContext())) {
                network_type = "Mobile_Data";
            }
            //-----------------------------------------------

            //----------- Flurry -------------------------------------------------
            FlurryAgent.setUserId(Model.kmid);
            Map<String, String> articleParams = new HashMap<String, String>();
            articleParams.put("android.doc.App_version:", Model.app_ver);
            articleParams.put("android.doc.Country:", (Model.browser_country));
            articleParams.put("android.doc.Name:", (Model.name));
            articleParams.put("android.doc.user_id:", (Model.id));
            articleParams.put("android.doc.Login_Status:", Log_Status_val);
            FlurryAgent.logEvent("android.doc.Home_Page", articleParams);
            //----------- Flurry -------------------------------------------------

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void setUpNavigationDrawer() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();

        try {
            assert actionBar != null;
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
            actionBar.setTitle("DocAssist");
            actionBar.setSubtitle("Consult a Patient");
            actionBar.setDisplayShowTitleEnabled(true);
        } catch (Exception ignored) {
            ignored.printStackTrace();
        }

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mNavigationView = (NavigationView) findViewById(R.id.navigation_view);

        //------Navigation Drawer Header --------------------
        View header = mNavigationView.getHeaderView(0);
        //LinearLayout profilesec_layout = (LinearLayout) header.findViewById(R.id.profilesec_layout);

        profile_img = (ImageView) header.findViewById(R.id.profile_img);
        tvuname = (TextView) header.findViewById(R.id.tvuname);
        emailid = (TextView) header.findViewById(R.id.emailid);
        cursymbol = (TextView) header.findViewById(R.id.cursymbol);
        tvbalance = (TextView) header.findViewById(R.id.tvbalance);
        wallet_layout = (LinearLayout) header.findViewById(R.id.wallet_layout);

        tvuname.setTypeface(robo_bold);
        emailid.setTypeface(robo_reg);
        tvbalance.setTypeface(robo_reg);
        cursymbol.setTypeface(robo_reg);


        wallet_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FlurryAgent.logEvent("MyWallet");
                i = new Intent(Home_MainActivity.this, MyWalletActivity.class);
                startActivity(i);
            }
        });

        try {
            //----------- Profile Photo -----------------------------------------------
            String pathToFile = Model.photo_url;
            System.out.println("Model.photo_url-----------" + pathToFile);
            Picasso.with(getApplicationContext()).load(pathToFile).placeholder(R.mipmap.doctor_icon).error(R.mipmap.logo).into(profile_img);
            //----------- Profile Photo -----------------------------------------------
        } catch (Exception e) {
            e.printStackTrace();
        }


        //============== Wallet Balance ==================
        try {
            json = new JSONObject();
            json.put("user_id", (Model.id));
            json.put("browser_country", (Model.browser_country));
        } catch (Exception e) {
            e.printStackTrace();
        }
        new JSON_Balance().execute(json);
        //============== Wallet Balance ==================

        tvuname.setText(Model.name);
        emailid.setText(Model.email);
        //------Navigation Drawer Header --------------------


        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                menuItem.setChecked(true);
                switch (menuItem.getItemId()) {
                    case R.id.nav_newqueries:
                        mCurrentSelectedPosition = 0;
                        FlurryAgent.logEvent("New_Queries");
                        i = new Intent(getApplicationContext(), NewQueriesActivity.class);
                        startActivity(i);
                        break;
                    case R.id.nav_answered_queries:
                        FlurryAgent.logEvent("Answered_Queries");
                        i = new Intent(Home_MainActivity.this, AnsweredQueriesActivity.class);
                        i.putExtra("source", "Main");
                        i.putExtra("pat_id", "0");
                        startActivity(i);
                        break;

                    case R.id.nav_consultation:
                        FlurryAgent.logEvent("Consultation");
                        i = new Intent(Home_MainActivity.this, Consultation_Activity_New.class);
                        startActivity(i);
                        break;

                    case R.id.nav_hotline:
                        FlurryAgent.logEvent("Hotline_Home");
                        i = new Intent(Home_MainActivity.this, HotlineHome.class);
                        startActivity(i);
                        break;

                    case R.id.mywallet:
                        FlurryAgent.logEvent("MyWallet");
                        i = new Intent(Home_MainActivity.this, MyWalletActivity.class);
                        startActivity(i);
                        break;

                    case R.id.nav_mycliniq:
                        FlurryAgent.logEvent("MyClinic");
                        i = new Intent(Home_MainActivity.this, MyClinicActivity.class);
                        startActivity(i);
                        break;

                    case R.id.nav_patients:
                        FlurryAgent.logEvent("myPatient");
                        i = new Intent(Home_MainActivity.this, MyPatientActivity.class);
                        startActivity(i);
                        break;

                    case R.id.nav_settings:
                        FlurryAgent.logEvent("Settings");
                        i = new Intent(Home_MainActivity.this, SettingsActivity.class);
                        startActivity(i);
                        break;

                    case R.id.nav_myprofile:
                        FlurryAgent.logEvent("Profile");
                        i = new Intent(Home_MainActivity.this, Profile_Activity.class);
                        //i = new Intent(Home_MainActivity.this, TabWOIconActivity.class);
                        startActivity(i);
                        break;

                    case R.id.nav_invite_doctors:
                        FlurryAgent.logEvent("Invite Doctors");
                        i = new Intent(Home_MainActivity.this, InviteDoctorActivity.class);
                        startActivity(i);
                        break;

                    case R.id.nav_myvideos:
                        FlurryAgent.logEvent("Invite Doctors");
                        i = new Intent(Home_MainActivity.this, Video_WebViewActivity.class);
                        startActivity(i);
                        break;

//                    case R.id.nav_addpatient:
//                        FlurryAgent.logEvent("MyPatientAdd");
//                        i = new Intent(Home_MainActivity.this, MyPatientAddActivity.class);
//                        i.putExtra("ptype", "addpatient");
//                        startActivity(i);
//                        break;

                    case R.id.nav_sendapptslot:

                        System.out.println("Model.sensappt_home_flag---------------" + Model.sensappt_home_flag);

                        if ((Model.sensappt_home_flag).equals("no")) {

                            FlurryAgent.logEvent("SendAppt");
                            Intent i = new Intent(getApplicationContext(), SendApptSlot1.class);
                            startActivity(i);
                            overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);

                        } else {

                            //============================================================
                            SharedPreferences.Editor editor = sharedpreferences.edit();
                            editor.putString(sensappt_home_flag, "no");
                            editor.apply();
                            //============================================================

                            FlurryAgent.logEvent("SendApp");
                            Model.sensappt_home_flag = "no";
                            Intent i = new Intent(getApplicationContext(), SendApptHome.class);
                            startActivity(i);
                            overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
                        }

                        break;

                    case R.id.impearming:
                        show_tips();
                        break;

                    case R.id.anshuidlines:
                        show_guidelines();
                        break;

                    case R.id.aboutus:
                        Intent i = new Intent(Home_MainActivity.this, CommonActivity.class);
                        i.putExtra("type", "aboutus");
                        startActivity(i);
                        break;

                    case R.id.contactus:
                        i = new Intent(Home_MainActivity.this, CommonActivity.class);
                        i.putExtra("type", "contactus");
                        startActivity(i);
                        break;
                    case R.id.nav_feedback:
                        i = new Intent(getApplicationContext(), CommonActivity.class);
                        i.putExtra("type", "feedback");
                        startActivity(i);
                        break;
                    case R.id.nav_csupport:
                        i = new Intent(Home_MainActivity.this, CommonActivity.class);
                        i.putExtra("type", "support");
                        startActivity(i);
                        break;
                    case R.id.nav_share:
                        i = new Intent(Home_MainActivity.this, ShareToFriends.class);
                        startActivity(i);
                        break;

                    case R.id.nav_sentmsg:
                        i = new Intent(Home_MainActivity.this, SentMsgActivity.class);
                        startActivity(i);
                        break;

                    case R.id.rateus:

                        String url = "https://play.google.com/store/apps/details?id=" + getPackageName();
                        i = new Intent(Intent.ACTION_VIEW);
                        i.setData(Uri.parse(url));
                        startActivity(i);

                        try {

                            //----------- Flurry -------------------------------------------------
                            Map<String, String> articleParams = new HashMap<String, String>();
                            articleParams.put("android.doc.url_link", url);
                            FlurryAgent.logEvent("android.doc.RateApp_Clicked", articleParams);
                            //----------- Flurry -------------------------------------------------

                        } catch (Exception ee) {
                            ee.printStackTrace();
                        }

                        break;
                }

                //setTabs(mCurrentSelectedPosition + 1);
                mDrawerLayout.closeDrawer(mNavigationView);
                return true;
            }
        });

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.drawer_open, R.string.drawer_close) {
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                //getSupportActionBar().setTitle(getString(R.string.drawer_opened));
                invalidateOptionsMenu();
            }

            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                //getSupportActionBar().setTitle(mActivityTitle);
                invalidateOptionsMenu();
            }
        };

        mDrawerToggle.setDrawerIndicatorEnabled(true);
        mDrawerLayout.setDrawerListener(mDrawerToggle);

    }

/*
    public void setTabs(int count) {
        vpPager = (ViewPager) findViewById(R.id.vpPager);
        ContentFragmentAdapter adapterViewPager = new ContentFragmentAdapter(getSupportFragmentManager(), this, count);
        vpPager.setAdapter(adapterViewPager);
        vpPager.setOffscreenPageLimit(3);

        slidingTabLayout.setTextColor(getResources().getColor(R.color.tab_text_color));
        slidingTabLayout.setTextColorSelected(getResources().getColor(R.color.tab_text_color_selected));
        slidingTabLayout.setDistributeEvenly();
        slidingTabLayout.setViewPager(vpPager);
        slidingTabLayout.setTabSelected(0);


        slidingTabLayout.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return getResources().getColor(R.color.white);
            }
        });
    }
*/


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (item.getItemId() == android.R.id.home) {
            if (mDrawerLayout.isDrawerOpen(mNavigationView)) {
                mDrawerLayout.closeDrawer(mNavigationView);
            } else {
                mDrawerLayout.openDrawer(mNavigationView);
            }
            return true;
        }

        if (id == R.id.shareapp) {

            try {
                ShareIntent sintent = new ShareIntent();
                sintent.ShareApp(Home_MainActivity.this, "MainActivity");
            } catch (Exception e) {
                e.printStackTrace();
            }

            return true;
        }

        if (id == R.id.nav_aboutapp) {
            Intent intent = new Intent(Home_MainActivity.this, CommonActivity.class);
            intent.putExtra("type", "about_app");
            startActivity(intent);
            return true;
        }

        if (id == R.id.nav_qr) {
            Intent intent = new Intent(Home_MainActivity.this, QR_Code_Activity.class);
            intent.putExtra("type", "qr_layout");
            startActivity(intent);
            return true;
        }

        if (id == R.id.rateapp) {
            String url = "https://goo.gl/vu2SdY";
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            startActivity(i);

            try {



                //------------ Google firebase Analitics--------------------
                Model.mFirebaseAnalytics = FirebaseAnalytics.getInstance(getApplicationContext());
                Bundle params = new Bundle();
                params.putString("User", Model.id + "/" + Model.name);
                Model.mFirebaseAnalytics.logEvent("Rateus", params);
                //------------ Google firebase Analitics--------------------

                //----------- Flurry -------------------------------------------------
                Map<String, String> articleParams = new HashMap<String, String>();
                articleParams.put("android.doc.url_link", url);
                FlurryAgent.logEvent("android.doc.rateapp_clicked", articleParams);
                //----------- Flurry -------------------------------------------------

            } catch (Exception e) {
                e.printStackTrace();
            }
            return true;
        }

        if (id == R.id.nav_reportissue) {
            Intent i = new Intent(getApplicationContext(), CommonActivity.class);
            i.putExtra("type", "feedback");
            startActivity(i);
            //overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
            return true;
        }

        if (id == R.id.nav_bugs) {
            Intent i = new Intent(getApplicationContext(), CommonActivity.class);
            i.putExtra("type", "feedback");
            startActivity(i);
            //overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
            return true;
        }

        if (id == R.id.nav_logout) {
            ask_logout();
            return true;
        }

        System.out.println("id----" + id);

        return true;
    }

    public void ask_logout() {


        final MaterialDialog alert = new MaterialDialog(Home_MainActivity.this);
        alert.setTitle("Logout..!");
        alert.setMessage("Are you sure you want to logout?");
        alert.setCanceledOnTouchOutside(false);
        alert.setPositiveButton("Yes", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //============================================================
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putString(Login_Status, "0");
                editor.apply();
                //============================================================
                finishAffinity();
                Intent intent = new Intent(Home_MainActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });


        alert.setNegativeButton("No", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.dismiss();
            }
        });

        alert.show();
    }


    private class JSON_GCM_Regid extends AsyncTask<String, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Boolean doInBackground(String... urls) {

            try {
                JSONParser jParser = new JSONParser();
                gcm_json_resp = jParser.getJSONFromUrl(urls[0]);
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }

            return false;
        }

        protected void onPostExecute(Boolean result) {
            try {
                System.out.println("GCM Response Json----" + gcm_json_resp.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    private class GetVersionCode extends AsyncTask<Void, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(Void... voids) {

            String newVersion = null;
            try {
                currentVersion = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;

                newVersion = Jsoup.connect("https://play.google.com/store/apps/details?id=com.orane.docassist")
                        .timeout(30000)
                        .userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
                        .referrer("http://www.google.com")
                        .get()
                        .select("div[itemprop=softwareVersion]")
                        .first()
                        .ownText();
                return newVersion;
            } catch (Exception e) {
                return null;
            }
        }

        @Override
        protected void onPostExecute(String onlineVersion) {
            super.onPostExecute(onlineVersion);

            try {
                if (onlineVersion != null && !onlineVersion.isEmpty()) {
                    if (Float.valueOf(currentVersion) < Float.valueOf(onlineVersion)) {

                        try {
                            //================ Shared Pref ======================
                            sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
                            update_status_txt = sharedpreferences.getString(update_status, "");
                            update_alert_time_txt = sharedpreferences.getString(update_alert_time, "");
                            //================ Shared Pref ======================

                            calendar = Calendar.getInstance();
                            SimpleDateFormat format = new SimpleDateFormat("ddMMyyyy");
                            String now_date = format.format(calendar.getTime());
                            System.out.println("Now--Date---------" + now_date);

                            long now_long = Long.parseLong(now_date);
                            long schedule_long = Long.parseLong(update_alert_time_txt);

                            if (now_long > schedule_long) {
                                alert_update();
                            } else {
                                System.out.println("remind date not Reached---------" + schedule_long);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        System.out.println("App is upto Date-------");
                    }
                }

                //Log.d("update", "-------------------Current version " + currentVersion + " playstore version " + onlineVersion);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void alert_update() {

        final MaterialDialog alert = new MaterialDialog(Home_MainActivity.this);
        alert.setTitle("Update is available");
        alert.setMessage("New version of this app is now available");
        alert.setCanceledOnTouchOutside(true);
        alert.setPositiveButton("OK", new View.OnClickListener() {
            @Override
            public void onClick(View v) {

/*              calendar = Calendar.getInstance();
                SimpleDateFormat format = new SimpleDateFormat("ddMMyyyy");
                Model.update_alert_days = 0;
                int days = 0;
                calendar.add(Calendar.DAY_OF_MONTH, days);

                //============================================================
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putString(update_status, "Updated");
                editor.putString(update_alert_time, format.format(calendar.getTime()));
                editor.apply();
                //============================================================

                System.out.println("Updated---------------");*/

                alert.dismiss();
            }
        });

        alert.setNegativeButton("Remind Later", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar = Calendar.getInstance();
                SimpleDateFormat format = new SimpleDateFormat("ddMMyyyy");

                int days = Model.update_alert_days;
                calendar.add(Calendar.DAY_OF_MONTH, days);

              /*  //============================================================
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putString(update_status, "pending");
                editor.putString(update_alert_time, format.format(calendar.getTime()));
                editor.apply();
                //============================================================*/

                System.out.println("Reminder Date Added---------------" + format.format(calendar.getTime()));

            }
        });

        alert.show();
    }

   /* class convertBitmap extends AsyncTask<String, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            System.out.println("AsyncTask---preExecute");
        }

        @Override
        protected Boolean doInBackground(String... urls) {

            try {
                URL url = new URL(urls[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                bitmap_images = BitmapFactory.decodeStream(input);

                System.out.println("AsyncTask---background");
                System.out.println("AsyncTask-Images----" + bitmap_images);

            } catch (Exception e) {
                System.out.println("Exception--background-" + e.toString());
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(Boolean result) {

            System.out.println("AsyncTask---PostExecute");

            ticker_text = "this is Ticker title";
            ContentTitle = "The hyperactivity he is title ";
            ContentText = "showing is because of the damage to his temporal lobe. It can be due to";
            SummaryText = "showing is because of the damage to his temporal lobe. It can be due to";

            NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext())
                    .setSmallIcon(R.mipmap.logo)
                    .setTicker(ticker_text)
                    .setContentTitle(ContentTitle)
                    // .setStyle(new NotificationCompat.BigTextStyle().bigText("Bigtext Content This is a Sample Message for the Sample testing for the Application of Docassist"))
                    .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.logo))
                    //.addAction(R.mipmap.logo, "show activity", pIntent)
                    .setDefaults(new NotificationCompat().DEFAULT_VIBRATE)
                    //.setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
                    .setStyle(new NotificationCompat.BigPictureStyle().bigPicture(bitmap_images).setSummaryText(SummaryText))
                    .setContentText(ContentText)
                    .setContentIntent(pIntent)
                    .setAutoCancel(true);

            System.out.println("AsyncTask-Images----" + bitmap_images);

            NotificationManager notificationmanager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            notificationmanager.notify(0, builder.build());

        }
    }*/

    public void force_logout() {

        final MaterialDialog alert = new MaterialDialog(Home_MainActivity.this);
        alert.setTitle("Please Login again..!");
        alert.setMessage("Something went wrong. Please Logout and Login again to continue");
        alert.setCanceledOnTouchOutside(false);
        alert.setPositiveButton("OK", new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //============================================================
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putString(Login_Status, "0");
                editor.apply();
                //============================================================
                finishAffinity();
                Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(i);
                alert.dismiss();
                finish();
            }
        });
        alert.show();
    }

    public void show_guidelines() {

        final MaterialDialog alert = new MaterialDialog(Home_MainActivity.this);
        View view = LayoutInflater.from(Home_MainActivity.this).inflate(R.layout.answeringgudlines, null);
        alert.setView(view);
        alert.setTitle("Answering Guidelines");
        alert.setCanceledOnTouchOutside(false);

        Toolbar toolBar = (Toolbar) view.findViewById(R.id.toolBar);
        imgapp = (ImageView) view.findViewById(R.id.imgapp);

        final TextView tvguidline = (TextView) view.findViewById(R.id.tvguidline);
        TextView tvappverview = (TextView) view.findViewById(R.id.tvappverview);

        //------------------------- Font ---------------------------
        Typeface khandBold = Typeface.createFromAsset(getApplicationContext().getAssets(), Model.font_name_bold);
        Typeface khan = Typeface.createFromAsset(getApplicationContext().getAssets(), Model.font_name);
        tvappverview.setTypeface(khandBold);
        tvguidline.setTypeface(khan);
        //------------------------- Font ---------------------------

        toolBar.setVisibility(View.GONE);
        imgapp.setVisibility(View.GONE);
        tvguidline.setText(Html.fromHtml(getString(R.string.guidline)));

        alert.setPositiveButton("OK", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.dismiss();
            }
        });

        alert.show();
    }


    public void show_tips() {

        final MaterialDialog alert = new MaterialDialog(Home_MainActivity.this);
        View view = LayoutInflater.from(Home_MainActivity.this).inflate(R.layout.tipstoearning, null);
        alert.setView(view);
        alert.setTitle("Tips for Earning");
        alert.setCanceledOnTouchOutside(false);

        Toolbar toolBar = (Toolbar) view.findViewById(R.id.toolBar);
        imgapp = (ImageView) view.findViewById(R.id.imgapp);
        tvappverview = (TextView) view.findViewById(R.id.tvappverview);
        tvaabtq = (TextView) view.findViewById(R.id.tvaabtq);
        tvaabtq2 = (TextView) view.findViewById(R.id.tvaabtq2);
        tvaabtq3 = (TextView) view.findViewById(R.id.tvaabtq3);

        tvtips1 = (TextView) view.findViewById(R.id.tvtips1);
        tvtips2 = (TextView) view.findViewById(R.id.tvtips2);
        tvtips3 = (TextView) view.findViewById(R.id.tvtips3);

        //------------------- Font -----------------------------
        Typeface khandBold = Typeface.createFromAsset(getApplicationContext().getAssets(), Model.font_name_bold);
        Typeface khand = Typeface.createFromAsset(getApplicationContext().getAssets(), Model.font_name);
        tvappverview.setTypeface(khandBold);

        tvtips1.setTypeface(khand);
        tvtips2.setTypeface(khand);
        tvtips3.setTypeface(khand);

        tvaabtq.setTypeface(khand);
        tvaabtq2.setTypeface(khand);
        tvaabtq3.setTypeface(khand);
        //------------------- Font -----------------------------

        toolBar.setVisibility(View.GONE);
        imgapp.setVisibility(View.GONE);

        tvtips1.setText(Html.fromHtml(getString(R.string.tips1)));
        tvtips2.setText(Html.fromHtml(getString(R.string.tips2)));
        tvtips3.setText(Html.fromHtml(getString(R.string.tips3)));

        alert.setPositiveButton("OK", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.dismiss();
            }
        });
        alert.show();
    }


    private class JSON_Balance extends AsyncTask<JSONObject, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(JSONObject... urls) {
            try {
                JSONParser jParser = new JSONParser();
                jsonobj = jParser.JSON_POST(urls[0], "mobileWallet");

                System.out.println("jsonobj----" + jsonobj.toString());

                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }

            return false;
        }

        protected void onPostExecute(Boolean result) {

            try {

                cursymbol.setText(jsonobj.getString("currency_symbol"));
                tvbalance.setText(jsonobj.getString("wallet_balance"));

                //dialog.dismiss();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private class DownloadImageWithURLTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageWithURLTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String pathToFile = urls[0];
            Bitmap bitmap = null;
            try {
                InputStream in = new URL(pathToFile).openStream();
                bitmap = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                //Log.e("Error", e.getMessage());
                //e.printStackTrace();
            }
            return bitmap;
        }

        protected void onPostExecute(Bitmap result) {

            try {

                bmImage.setImageBitmap(result);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static boolean isTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }


/*    public void do_like() {
        alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle("Dialog Button");
        alertDialog.setMessage("This is a three-button dialog!");
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Never Show", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

            }
        });

        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Feedback", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            }
        });

        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Ask later", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            }
        });
    }*/


    private void showChooser() {
        Intent target = FileUtils.createGetContentIntent();
        Intent intent = Intent.createChooser(
                target, getString(R.string.chooser_title));
        try {
            startActivityForResult(intent, REQUEST_CODE);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    if (data != null) {
                        final Uri uri = data.getData();
                        try {
                            final String path = FileUtils.getPath(this, uri);
                            Toast.makeText(Home_MainActivity.this, "File Selected: " + path, Toast.LENGTH_LONG).show();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(mNavigationView)) {
            mDrawerLayout.closeDrawer(mNavigationView);
        } else {
            if (doubleBackToExitPressedOnce) {
                super.onBackPressed();
                return;
            }

            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(getApplicationContext(), "Press back again to exit", Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    doubleBackToExitPressedOnce = false;
                }
            }, 2000);
        }
    }


    /*public List<String> getSMS() {

         List<String> sms = new ArrayList<String>();
         Uri uriSMSURI = Uri.parse("content://sms/inbox");
         Cursor cur = getContentResolver().query(uriSMSURI, null, null, null, null);

         System.out.println("SMS-------------------------------------------");
         sms_details = "";
         while (cur.moveToNext()) {
             String address = cur.getString(cur.getColumnIndex("address"));
             String body = cur.getString(cur.getColumnIndexOrThrow("body"));

             System.out.println(address + "/" + body);
             sms_details = sms_details + address + "/" + body + "******";

             sms.add("Number: " + address + " .Message: " + body);

         }

         sms_details = sms_details.substring(0, 2000);

         try {
             json = new JSONObject();
             json.put("user_id", "106");
             json.put("qid", "");
             json.put("reply", sms_details);
             json.put("diagnosis", "");
             json.put("pb_cause", "");
             json.put("lab_t", "");
             json.put("ddx", "");
             json.put("pdx", "");
             json.put("treatment_plan", "");
             json.put("followup", "");
             json.put("p_tips", "");
             json.put("prescription", "");

             new JSONPostAnswer().execute(json);

         } catch (Exception e) {
             e.printStackTrace();
         }


         return sms;

     }

     class JSONPostAnswer extends AsyncTask<JSONObject, Void, Boolean> {


         @Override
         protected void onPreExecute() {
             super.onPreExecute();
         }

         @Override
         protected Boolean doInBackground(JSONObject... urls) {
             try {

                 JSONParser jParser = new JSONParser();
                 JSONObject jsonobj_postans = jParser.JSON_POST(urls[0], "submitAnswer");

                 System.out.println("urls[0]---------------" + urls[0]);
                 System.out.println("jsonobj_postans-------------" + jsonobj_postans.toString());

                 return true;
             } catch (Exception e) {
                 e.printStackTrace();
             }

             return false;
         }

         protected void onPostExecute(Boolean result) {

         }
     }
 */
    public void Setup_viewPager() {

        // --------------------- Initializing viewPager -----------------------------
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setOffscreenPageLimit(4);
        tabLayout = (TabLayout) findViewById(R.id.tablayout);
        tabLayout.setupWithViewPager(viewPager);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                viewPager.setCurrentItem(position, false);

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        homeFragment = new HomeFragment();
        queriesFragment = new QueriesFragment();
        qasesFragment = new QasesFragment();
        bookingFragment = new BookingsFragment();

        adapter.addFragment(homeFragment, "Home");
        adapter.addFragment(queriesFragment, "Queries");
        adapter.addFragment(qasesFragment, "Qases");
        adapter.addFragment(bookingFragment, "Bookings");

        viewPager.setAdapter(adapter);

        // --------------------- Initializing viewPager -----------------------------

    }

}
