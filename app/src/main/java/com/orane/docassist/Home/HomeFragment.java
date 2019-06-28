
package com.orane.docassist.Home;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.ScaleAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.flurry.android.FlurryAgent;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.google.android.gms.common.api.GoogleApiClient;

import com.nineoldandroids.animation.ValueAnimator;
import com.nineoldandroids.view.ViewHelper;
import com.orane.docassist.AddNotesActivity;
import com.orane.docassist.Consultation_Activity_New;
import com.orane.docassist.HotlinePatientsActivity;
import com.orane.docassist.LoginActivity;
import com.orane.docassist.Model.Model;
import com.orane.docassist.MyClinicActivity;
import com.orane.docassist.MyPatientActivity;
import com.orane.docassist.Network.Detector;
import com.orane.docassist.Network.JSONParser;
import com.orane.docassist.NewQueriesActivity;
import com.orane.docassist.New_Main.New_MainActivity;
import com.orane.docassist.PayoutTransactions;
import com.orane.docassist.QasesHome;
import com.orane.docassist.R;
import com.orane.docassist.ReferDoctorActivity;
import com.orane.docassist.SendApptHome;
import com.orane.docassist.SendApptSlot1;
import com.orane.docassist.SettingsActivity;
import com.orane.docassist.WalletTransactions;
import com.orane.docassist.WebViewActivity;

import org.json.JSONObject;

import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.List;

import me.drakeet.materialdialog.MaterialDialog;

import static android.content.Context.NOTIFICATION_SERVICE;

public class HomeFragment extends Fragment implements ObservableScrollViewCallbacks {


    public boolean isInFront;
    JSONObject jsonobj_ad;
    LinearLayout mypat_layout, senappt_layout, logo_layout_hotline, logo_layout_refer, bottbar;
    LinearLayout refer_layout, layout_offer1, b1_layout, b2_layout, b3_layout, b4_layout, b5_layout, b6_layout;
    TextView tv_qactions, tv_ad_close, tvwelcome, tvcons, tvuname, tvqnotify, text, tvbalance, emailid;
    FrameLayout ad_layout;
    ImageView ad_close;
    int qc = 0;
    int cc = 0;
    int bc = 0;
    Bitmap bitmap;
    public String str_response, qcount, ccount, bcount, uname, pass, track_id_val, url;
    Animation myAnimation;
    ImageView btn_query, btn_cons, btn_support;
    LinearLayout myclinic_layout;
    public String sensappt_home_flag_val;
    int mFlipping = 0;

    NavigationView navigationView;
    private GoogleApiClient client;
    long startTime;
    ImageView imgapp;
    TextView tv_gridtext1, tv_gridtext2, tv_gridtext3, tv_gridtext4, tv_gridtext5, tv_gridtext6, tv_wallet_tit, tv_wallet_desc, tv_myp_tit, tv_myp_desc, tv_sendappt_desc, tv_sendappt_tit, captionTextView, captionTitleView;
    public TextView tv_icon_newq, tv_icon_newc, tv_icon_newh, tv_icon_newr;
    LinearLayout newquery_layout, cons_layout;
    RelativeLayout ad_close_layout;

    ObservableScrollView scrollable;
    Typeface font_reg, font_bold;
    JSONObject json_count;
    ImageView home_ad;
    Animation bottomUp, Upbottom;
    ScaleAnimation scaleAnimation;

    public FloatingActionMenu menuRed;
    private FloatingActionButton fab1;
    private FloatingActionButton fab2;
    private FloatingActionButton fab3;

    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs";
    public static final String Login_Status = "Login_Status_key";
    public static final String sensappt_home_flag = "sensappt_home_flag_key";


    public static HomeFragment newInstance(int pageIndex) {
        HomeFragment contentFragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putInt("pageIndex", pageIndex);
        contentFragment.setArguments(args);
        return contentFragment;
    }

    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (isTablet(getActivity())) {
            view = inflater.inflate(R.layout.home_fragment, container, false);
            System.out.println("This is TAB------------------------");
        } else {
            view = inflater.inflate(R.layout.home_fragment, container, false);
            System.out.println("This is Mobile Device------------------------");
        }

        font_reg = Typeface.createFromAsset(getActivity().getAssets(), Model.font_name);
        font_bold = Typeface.createFromAsset(getActivity().getAssets(), Model.font_name_bold);

        FlurryAgent.onPageView();
        //================ Shared Pref ======================
        sharedpreferences = getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        sensappt_home_flag_val = sharedpreferences.getString(sensappt_home_flag, "");
        //------------ Object Creations -------------------------------

        scrollable = (ObservableScrollView) view.findViewById(R.id.scrollable);
        menuRed = (FloatingActionMenu) view.findViewById(R.id.menu_red);
        fab1 = (FloatingActionButton) view.findViewById(R.id.fab1);
        fab2 = (FloatingActionButton) view.findViewById(R.id.fab2);
        fab3 = (FloatingActionButton) view.findViewById(R.id.fab3);

        cons_layout = (LinearLayout) view.findViewById(R.id.cons_layout);
        newquery_layout = (LinearLayout) view.findViewById(R.id.newquery_layout);
        tvwelcome = (TextView) view.findViewById(R.id.tvwelcome);
        tv_wallet_tit = (TextView) view.findViewById(R.id.tv_wallet_tit);
        tv_wallet_desc = (TextView) view.findViewById(R.id.tv_wallet_desc);

        btn_query = (ImageView) view.findViewById(R.id.btn_query);
        btn_cons = (ImageView) view.findViewById(R.id.btn_cons);
        btn_support = (ImageView) view.findViewById(R.id.btn_support);
        tvqnotify = (TextView) view.findViewById(R.id.tvqnotify);
        tvcons = (TextView) view.findViewById(R.id.tvcons);
        tv_myp_tit = (TextView) view.findViewById(R.id.tv_myp_tit);
        tv_myp_desc = (TextView) view.findViewById(R.id.tv_myp_desc);
        tv_sendappt_tit = (TextView) view.findViewById(R.id.tv_sendappt_tit);
        tv_sendappt_desc = (TextView) view.findViewById(R.id.tv_sendappt_desc);
        captionTextView = (TextView) view.findViewById(R.id.captionTextView);
        captionTitleView = (TextView) view.findViewById(R.id.captionTitleView);
        tv_icon_newq = (TextView) view.findViewById(R.id.tv_icon_newq);
        tv_icon_newc = (TextView) view.findViewById(R.id.tv_icon_newc);
        tv_icon_newh = (TextView) view.findViewById(R.id.tv_icon_newh);
        tv_icon_newr = (TextView) view.findViewById(R.id.tv_icon_newr);
        tv_gridtext1 = (TextView) view.findViewById(R.id.tv_gridtext1);
        tv_gridtext2 = (TextView) view.findViewById(R.id.tv_gridtext2);
        tv_gridtext3 = (TextView) view.findViewById(R.id.tv_gridtext3);
        tv_gridtext4 = (TextView) view.findViewById(R.id.tv_gridtext4);
        tv_gridtext5 = (TextView) view.findViewById(R.id.tv_gridtext5);
        tv_gridtext6 = (TextView) view.findViewById(R.id.tv_gridtext6);
        tv_qactions = (TextView) view.findViewById(R.id.tv_qactions);

        myclinic_layout = (LinearLayout) view.findViewById(R.id.myclinic_layout);
        mypat_layout = (LinearLayout) view.findViewById(R.id.mypat_layout);
        senappt_layout = (LinearLayout) view.findViewById(R.id.senappt_layout);

        logo_layout_hotline = (LinearLayout) view.findViewById(R.id.logo_layout_hotline);
        logo_layout_refer = (LinearLayout) view.findViewById(R.id.logo_layout_refer);
        layout_offer1 = (LinearLayout) view.findViewById(R.id.gl_layout);
        b1_layout = (LinearLayout) view.findViewById(R.id.b1_layout);
        b2_layout = (LinearLayout) view.findViewById(R.id.b2_layout);
        b3_layout = (LinearLayout) view.findViewById(R.id.b3_layout);
        b4_layout = (LinearLayout) view.findViewById(R.id.b4_layout);
        b5_layout = (LinearLayout) view.findViewById(R.id.b5_layout);
        b6_layout = (LinearLayout) view.findViewById(R.id.b6_layout);
        refer_layout = (LinearLayout) view.findViewById(R.id.refer_layout);

        menuRed.setClosedOnTouchOutside(true);
        fab1.setOnClickListener(clickListener);
        fab2.setOnClickListener(clickListener);
        fab3.setOnClickListener(clickListener);

        tv_wallet_tit.setTypeface(font_bold);
        tv_wallet_desc.setTypeface(font_reg);
        tv_myp_tit.setTypeface(font_bold);
        tv_myp_desc.setTypeface(font_reg);
        tv_sendappt_tit.setTypeface(font_bold);
        tv_sendappt_desc.setTypeface(font_reg);
        tv_gridtext1.setTypeface(font_reg);
        tv_gridtext2.setTypeface(font_reg);
        tv_gridtext3.setTypeface(font_reg);
        tv_gridtext4.setTypeface(font_reg);
        tv_gridtext5.setTypeface(font_reg);
        tv_gridtext6.setTypeface(font_reg);

        captionTitleView.setTypeface(font_bold);
        captionTextView.setTypeface(font_reg);

        tv_icon_newq.setTypeface(font_reg);
        tv_icon_newc.setTypeface(font_reg);
        tv_icon_newh.setTypeface(font_reg);
        tv_icon_newr.setTypeface(font_reg);
        tv_qactions.setTypeface(font_bold);

/*
        try {

            //----------- hiding Hotline for Abroad ------------------------
            if ((Model.browser_country).equals("IN")) {
                b3_layout.setVisibility(View.VISIBLE);
                refer_layout.setVisibility(View.GONE);

*//*          navigationView = (NavigationView) view.findViewById(R.id.nav_view);
                Menu nav_Menu = navigationView.getMenu();
                nav_Menu.findItem(R.id.nav_hotline).setVisible(true);*//*

            } else {
                b3_layout.setVisibility(View.GONE);
                refer_layout.setVisibility(View.VISIBLE);

*//*                navigationView = (NavigationView) view.findViewById(R.id.nav_view);
                Menu nav_Menu = navigationView.getMenu();
                nav_Menu.findItem(R.id.nav_hotline).setVisible(false);*//*
            }
            //----------- hiding Hotline for Abroad ------------------------


        } catch (Exception e) {
            e.printStackTrace();
        }*/





        if ((Model.id) != null && !(Model.id).isEmpty() && !(Model.id).equals("null") && !(Model.id).equals("")) {
            //--------------------New Query Notify-----------------------------------------------------
            String url2 = Model.BASE_URL + "sapp/getNewCount?user_id=" + (Model.id) + "&token=" + Model.token;
            System.out.println("Counts URL----------" + url2);
            new JSON_Counts().execute(url2);
            //-------------------------------------------------------------------------
        } else {
            Toast.makeText(getActivity(), "Sorry..! Something went wrong. Go back and try again..", Toast.LENGTH_LONG).show();
            //force_logout();
        }
        //------------------ Fetch Ad ----------------------------------------------

        home_ad = (ImageView) view.findViewById(R.id.home_ad);
        ad_close = (ImageView) view.findViewById(R.id.ad_close);
        ad_layout = (FrameLayout) view.findViewById(R.id.ad_layout);
        tv_ad_close = (TextView) view.findViewById(R.id.tv_ad_close);
        ad_close_layout = (RelativeLayout) view.findViewById(R.id.ad_close_layout);

        bottomUp = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_up_animation);
        Upbottom = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_down_animation);

        scaleAnimation = new ScaleAnimation(0.2f, 1f, 0.2f, 1f, ScaleAnimation.RELATIVE_TO_SELF, 0.5f,
                ScaleAnimation.RELATIVE_TO_SELF, 0.5f);
        scaleAnimation.setDuration(400);

       /* //------------------------
        ad_close_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Model.home_ad_flag = "false";

                //ad_layout.startAnimation(Upbottom);
                //ad_layout.startAnimation(scaleAnimation);
                ad_layout.setVisibility(View.GONE);

                //--------------------Ad- Close--------
                String ad_url = Model.BASE_URL + "/sapp/closeAd?user_id=" + Model.id + "&track_id=" + track_id_val + "&token=" + Model.token;
                System.out.println("ad_url----------" + ad_url);
                new JSON_Close().execute(ad_url);
                //---------------------Ad- Close-------

            }
        });

        if ((Model.home_ad_flag).equals("true")) {
            //--------------------Ad---------
            Model.home_ad_flag = "false";
            String ad_url = Model.BASE_URL + "/sapp/fetchAd?user_id=" + Model.id + "&browser_country=" + Model.browser_country + "&page_src=2&token=" + Model.token;
            System.out.println("ad_url----------" + ad_url);
            new JSON_Ad().execute(ad_url);
            //---------------------Ad--------
        }
        //------------------ Fetch Ad ----------------------------------------------*/


        layout_offer1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                System.out.println("Offer1....1");

                show_guidelines();
            }
        });


        myclinic_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), MyClinicActivity.class);
                startActivity(i);
                getActivity().overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);



            }
        });
        mypat_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), MyPatientActivity.class);
                startActivity(i);
                getActivity().overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
            }
        });
        senappt_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //================ Shared Pref ======================
                sharedpreferences = getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
                Model.sensappt_home_flag = sharedpreferences.getString(sensappt_home_flag, "yes");
                //------------ Object Creations -------------------------------

                System.out.println("Model.sensappt_home_flag---------------" + Model.sensappt_home_flag);

                if ((Model.sensappt_home_flag).equals("no")) {

                    Intent i = new Intent(getActivity(), SendApptSlot1.class);
                    startActivity(i);
                    getActivity().overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);

                } else {

                    //============================================================
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.putString(sensappt_home_flag, "no");
                    editor.apply();
                    //============================================================

                    Model.sensappt_home_flag = "no";
                    Intent i = new Intent(getActivity(), SendApptHome.class);
                    startActivity(i);
                    getActivity().overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
                }
            }
        });


        b1_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(getActivity(), NewQueriesActivity.class);
                startActivity(i);

            }
        });

        b2_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent i = new Intent(getActivity(), Consultation_Activity_New.class);
                startActivity(i);

            }
        });


        b3_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(getActivity(), New_MainActivity.class);
                startActivity(i);

            }
        });

        b4_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(getActivity(), QasesHome.class);
                startActivity(i);
            }
        });

        b5_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

/*              //------------ Kissmetrics-------------------------------


                Intent i = new Intent(getActivity(), AnsweredQueriesActivity.class);
                i.putExtra("source", "Main");
                i.putExtra("pat_id", "0");
                startActivity(i);  */

/*

                Intent i = new Intent(getActivity(), AnalyticsHome.class);
                i.putExtra("source", "Main");
                i.putExtra("pat_id", "0");
                startActivity(i);*/

                Intent i = new Intent(getActivity(), WebViewActivity.class);
                i.putExtra("url", Model.BASE_URL + "/analytics/index?user_id=" + Model.id + "&token=" + Model.token + "&web_view=true");
                i.putExtra("type", "Analytics");
                startActivity(i);

            }
        });

        b6_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(getActivity(), SettingsActivity.class);
                startActivity(i);
            }
        });

        refer_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent i = new Intent(getActivity(), ReferDoctorActivity.class);
                startActivity(i);
            }
        });

        logo_layout_refer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(getActivity(), ReferDoctorActivity.class);
                startActivity(i);
            }
        });

        scrollable.setScrollViewCallbacks(this);

        return view;

    }

    @Override
    public void onScrollChanged(int scrollY, boolean firstScroll, boolean dragging) {

    }

    @Override
    public void onDownMotionEvent() {
        System.out.println("scroll Down--------");
    }

    @Override
    public void onUpOrCancelMotionEvent(ScrollState scrollState) {
        //System.out.println("scrollState--------" + scrollState);

        if (scrollState == ScrollState.UP) {
            System.out.println("scrollDir-----UP---" + scrollState);
            //bottom_layout.animate().translationY(bottom_layout.getHeight());


            if (menuRed.isShown()) {
                //hideToolbar();
                hideBottomBar();
            }

        } else if (scrollState == ScrollState.DOWN) {
            System.out.println("scrollDir-----Down---" + scrollState);
            //bottom_layout.animate().translationY(0);


            if (menuRed.isShown()) {
                //showToolbar();
                showBottomBar();
            }
        }
    }


    //------------ Bottom Bar Hide ----------------------------------------------------------------
    private void showBottomBar() {
        moveBottomBar(0);
    }

    private void hideBottomBar() {
        moveBottomBar(menuRed.getHeight());
    }

    private void moveBottomBar(float toTranslationY) {
        if (ViewHelper.getTranslationY(menuRed) == toTranslationY) {
            return;
        }
        ValueAnimator animator = ValueAnimator.ofFloat(ViewHelper.getTranslationY(menuRed), toTranslationY).setDuration(200);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float translationY = (float) animation.getAnimatedValue();
                ViewHelper.setTranslationY(menuRed, translationY);
/*                ViewHelper.setTranslationY((View) scrollable, translationY);
                FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) ((View) scrollable).getLayoutParams();
                lp.height = (int) -translationY + getScreenHeight() - lp.topMargin;
                ((View) scrollable).requestLayout();*/
            }
        });
        animator.start();
    }
    //------------ Toolbar Hide ----------------------------------------------------------------


    private class JSON_Counts extends AsyncTask<String, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            startTime = System.currentTimeMillis();
        }

        @Override
        protected Boolean doInBackground(String... urls) {

            try {

                str_response = new JSONParser().getJSONString(urls[0]);
                System.out.println("str_response--------------" + str_response);

/*                JSONParser jParser = new JSONParser();
                JSONObject jsonoj = jParser.getJSON_URL(urls[0]);

                qcount = jsonoj.getString("new_query_count");
                ccount = jsonoj.getString("new_consult_count");
                bcount = jsonoj.getString("new_booking_count");*/

                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }

            return false;
        }

        protected void onPostExecute(Boolean result) {

            try {

                json_count = new JSONObject(str_response);
                System.out.println("json_count-------" + json_count.toString());

                if (json_count.has("token_status")) {
                    String token_status = json_count.getString("token_status");
                    if (token_status.equals("0")) {
                        //============================================================
                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        editor.putString(Login_Status, "0");
                        editor.apply();
                        //============================================================
                        getActivity().finishAffinity();
                        Intent intent = new Intent(getActivity(), LoginActivity.class);
                        startActivity(intent);
                        getActivity().finish();
                    }
                } else {

                    qcount = json_count.getString("new_query_count");
                    ccount = json_count.getString("new_consult_count");
                    bcount = json_count.getString("new_booking_count");


                    if (qcount != null && !qcount.isEmpty() && !qcount.equals("null")) {
                        if (ccount != null && !ccount.isEmpty() && !ccount.equals("null")) {
                            if (bcount != null && !bcount.isEmpty() && !bcount.equals("null")) {

                                if (result) {

                                    System.out.println("qcount-----" + qcount);
                                    System.out.println("ccount-----" + ccount);
                                    System.out.println("bcount-----" + bcount);

                                    qc = Integer.parseInt(qcount);
                                    cc = Integer.parseInt(ccount);
                                    bc = Integer.parseInt(bcount);

                                    setBadge(getActivity(), (qc + cc + bc));

                                    System.out.println("(qc + cc + bc)-----" + (qc + cc + bc));

                                    //------------------------------------------
                                    if (qc > 0) {
                                        tvqnotify.setVisibility(View.VISIBLE);
                                        tvqnotify.setText("" + qc);
                                    } else {
                                        tvqnotify.setVisibility(View.GONE);
                                    }
                                    //-----------------------------------
                                    if ((cc > 0) || (bc > 0)) {
                                        tvcons.setVisibility(View.VISIBLE);
                                        tvcons.setText("" + (cc + bc));
                                    } else {
                                        tvcons.setVisibility(View.GONE);
                                    }

                                }
                            }
                        }
                    }

                    long elapsedTime = System.currentTimeMillis() - startTime;
                    System.out.println("Total Elapsed get Count time in milliseconds: " + elapsedTime);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    public static void setBadge(Context context, int count) {
        String launcherClassName = getLauncherClassName(context);
        if (launcherClassName == null) {
            return;
        }
        Intent intent = new Intent("android.intent.action.BADGE_COUNT_UPDATE");
        intent.putExtra("badge_count", count);
        intent.putExtra("badge_count_package_name", context.getPackageName());
        intent.putExtra("badge_count_class_name", launcherClassName);
        context.sendBroadcast(intent);
    }

    public static String getLauncherClassName(Context context) {

        PackageManager pm = context.getPackageManager();

        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);

        List<ResolveInfo> resolveInfos = pm.queryIntentActivities(intent, 0);
        for (ResolveInfo resolveInfo : resolveInfos) {
            String pkgName = resolveInfo.activityInfo.applicationInfo.packageName;
            if (pkgName.equalsIgnoreCase(context.getPackageName())) {
                return resolveInfo.activityInfo.name;
            }
        }
        return null;
    }

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.fab1:

                    menuRed.close(true);
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            Intent i = new Intent(getActivity(), WalletTransactions.class);
                            startActivity(i);

                        }
                    }, 500);

                    break;
                case R.id.fab2:

                    menuRed.close(true);
                    final Handler handler2 = new Handler();
                    handler2.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent j = new Intent(getActivity(), PayoutTransactions.class);
                            startActivity(j);
                        }
                    }, 500);

                    break;
                case R.id.fab3:

                    menuRed.close(true);
                    final Handler handler3 = new Handler();
                    handler3.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent k = new Intent(getActivity(), HotlinePatientsActivity.class);
                            startActivity(k);
                        }
                    }, 500);

                    break;
            }
        }
    };

    public void show_guidelines() {

        final MaterialDialog alert = new MaterialDialog(getActivity());
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.answeringgudlines, null);
        alert.setView(view);
        alert.setTitle("Answering Guidelines");
        alert.setCanceledOnTouchOutside(false);

        Toolbar toolBar = (Toolbar) view.findViewById(R.id.toolBar);
        imgapp = (ImageView) view.findViewById(R.id.imgapp);
        final TextView tvguidline = (TextView) view.findViewById(R.id.tvguidline);
        final TextView tvappverview = (TextView) view.findViewById(R.id.tvappverview);

        if (new Detector().isTablet(getActivity())) {
            tvguidline.setTextSize(18);
            tvappverview.setTextSize(20);
        }

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

    public static boolean isTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }

    @Override
    public void onResume() {
        super.onResume();

        try {
            if (getUserVisibleHint()) {

                try {
                    if ((Model.id) != null && !(Model.id).isEmpty() && !(Model.id).equals("null") && !(Model.id).equals("")) {
                        //--------------------New Query Notify-----------------------------------------------------
                        String url2 = Model.BASE_URL + "sapp/getNewCount?user_id=" + (Model.id) + "&token=" + Model.token;
                        System.out.println("Counts URL----------" + url2);
                        new JSON_Counts().execute(url2);
                        //-------------------------------------------------------------------------
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class JSON_Ad extends AsyncTask<String, Void, Boolean> {

        ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

/*            pd = new ProgressDialog(getActivity());
            pd.setMessage("Loading Ad. Please wait...");
            pd.setCancelable(true);
            pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pd.show();*/

        }

        @Override
        protected Boolean doInBackground(String... urls) {

            try {
                JSONParser jParser = new JSONParser();
                jsonobj_ad = jParser.getJSONFromUrl(urls[0]);
                System.out.println("jsonobj_ad---------- " + jsonobj_ad.toString());

                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }

            return false;
        }

        protected void onPostExecute(Boolean result) {

            try {

                if (jsonobj_ad.has("status")) {
                    String str_status = jsonobj_ad.getString("status");
                    track_id_val = jsonobj_ad.getString("track_id");

                    if (str_status.equals("1")) {
                        String ad_img_path = jsonobj_ad.getString("img");
                        track_id_val = jsonobj_ad.getString("track_id");
                        //Picasso.with(getActivity()).load(ad_img_path).placeholder(R.mipmap.patient_placeholder).error(R.mipmap.patient_placeholder).into(home_ad);
                        new LoadImage().execute(ad_img_path);

                    } else {
                        ad_layout.setVisibility(View.GONE);
                    }
                } else {
                    ad_layout.setVisibility(View.GONE);
                }

                // pd.dismiss();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private class JSON_Close extends AsyncTask<String, Void, Boolean> {

        @Override
        protected Boolean doInBackground(String... urls) {

            try {
                JSONParser jParser = new JSONParser();
                jsonobj_ad = jParser.getJSONFromUrl(urls[0]);
                System.out.println("jsonobj_ad---------- " + jsonobj_ad.toString());

                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }

            return false;
        }
    }


    private class LoadImage extends AsyncTask<String, String, Bitmap> {

        ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
/*            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Loading Image ....");
            pDialog.show();*/

            ad_layout.setVisibility(View.GONE);

        }

        protected Bitmap doInBackground(String... args) {
            try {

                bitmap = BitmapFactory.decodeStream((InputStream) new URL(args[0]).getContent());

            } catch (Exception e) {
                e.printStackTrace();
            }
            return bitmap;
        }

        protected void onPostExecute(Bitmap image) {

            try {
                if (image != null) {
                    home_ad.setImageBitmap(image);
                    //pDialog.dismiss();

                    ad_layout.setVisibility(View.VISIBLE);
                    ad_layout.startAnimation(scaleAnimation);

/*                    //------------------ Button Animation -------------------------
                    Animation shake;
                    shake = AnimationUtils.loadAnimation(getActivity(), R.anim.shakeanim);
                    ad_layout.startAnimation(shake);
                    //------------------ Button Animation -------------------------*/

                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }


}
