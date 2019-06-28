package com.orane.docassist;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.flurry.android.FlurryAgent;


import com.google.firebase.analytics.FirebaseAnalytics;
import com.orane.docassist.Model.Model;
import com.orane.docassist.Network.Detector;
import com.orane.docassist.Network.JSONParser;
import com.orane.docassist.R;
import com.orane.docassist.attachment_view.GridViewActivity;
import com.orane.docassist.fileattach_library.DefaultCallback;
import com.orane.docassist.fileattach_library.EasyImage;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;


import dmax.dialog.SpotsDialog;
import me.drakeet.materialdialog.MaterialDialog;
import pl.tajchert.nammu.Nammu;
import pl.tajchert.nammu.PermissionCallback;

import static com.zipow.videobox.confapp.ConfMgr.getApplicationContext;

public class NewQueryViewActivity extends AppCompatActivity {

    Typeface noto_reg, noto_bold;
    Switch followup_switch;
    Button btn_notanswer;
    View pred_vi;
    String strHtml_text;
    InputStream is = null;
    int serverResponseCode = 0;

    public String selectedPath, selectedfilename, local_url, last_upload_file, upLoadServerUri, attach_qid, attach_status, attach_file_url, attach_filename, attach_id, upload_response, patient_id, jio_query, track_id_val, str_response, file_full_url, attach_file_text, extension, file_name_only, title_text, fields_text, qtype, query_price_text, pat_from_text, check_enable_ffollowup, answering_status, opt_freefollow, enable_freefollow, prescribe_status, isEnablefFollowup, qansby, qcanianswer, html_file_str, followupcode, qitems, regards, msg_text, msg_ext_text, files_text, class_text, time_text;
    public String file_user_id, feedback_text, file_doctype, file_file, file_ext, rating_text, pat_feedback_text, feedback_id_text, reply_text;
    public String serverResponseMessage, contentAsString, answer_files_text, diagnose_prediction_text, age_txt, report_response, arr_feedback_text, gender_txt, age_gender_txt, prescribe_value, enable_prescription_val, lab_t_text, ddx_text, pdx_text, treatment_plan_text, followup_text, p_tips_text;
    JSONObject json_response_obj, feedback_json, jsonobj_postans, jsonon_titem, json_gender, jsonobj_canisnaswer, jsonobj, jsonobj_items, jsonobj_files;
    LinearLayout answer_display_layout, query_display_layout, main_data_layout, doctor_reply_section, feedback_section, extra_layout, ans_extra_layout, extra_hwlayout, extra_ans_layout, button_layout, answer_layout, netcheck_layout, nolayout, layout_attachfile, myLayout, answer_files_layout, answer_layout_attachfile, files_layout;
    public View vi_ans, vi, vi_files;
    TextView tv_attach_url, tv_attach_id, tv_answer_filename, tv_pred_slno, tv_pred_name, tv_pred_profname, tv_pred_icd, tv_pred_rank, tv_etitle, tv_replytext, tv_valuetext, tv_keytext, tv_title, tv_extra, tv_ext, tv_userid, tv_gender, tvattached, tv_filename, tv_pat_name, tv_pat_place, tv_query, tvt_morecomp, tv_morecomp, tvt_prevhist, tv_prevhist, tvt_curmedi, tv_curmedi, tvt_pastmedi, tv_pastmedi, tvt_labtest, tv_labtest, tv_datetime;
    TextView tv_patfeedback, tvtips1, tvtips2, tvtips3, tv_ext_title, tv_ext_desc, tv_answer, tvt_probcause, tv_probcause, tvt_invdone, tv_invdone, tvt_diffdiag, tv_diffdiag, tvt_probdiag, tv_probdiag, tvt_tratplan, tv_tratplan, tvt_prevmeasure, tv_prevmeasure, tvt_follup, tv_follup, tv_datetimeans;
    ScrollView scrollview;
    StringBuilder total;
    Button btn_write_pres, btn_feedbacksubmit;
    ImageView file_image, imgapp;
    WebView query_webview, webview_answer;
    ProgressBar progressBar;
    Button btn_reload, btn_ansquery;
    long startTime;
    ImageView thumb_img;
    View vi_hw, vi_hw_tit, vi_hw_full, vi_ans_ext;
    Typeface font_reg, font_bold;
    Map<String, String> extra_ans_map = new HashMap<String, String>();
    Map<String, String> extra_query_map = new HashMap<String, String>();
    public JSONObject json_fields, jsonobj_hwextra, text_jsonobj, draft_json, json, jsonobj_ad;
    public JSONArray jarray_hw;
    View vi_ext;
    Button btn_done, btn_upload_pres;
    public String type, ent_ans, current_qid, answer_txt, allow_answer, params, ans_isvalid, ans_status;
    EditText edt_answer;
    LinearLayout days_layout, file_list, prediction_inner_layout, prediction_layout, extra_hw_title, extra_hw_details, prescribe_layout, show_more, ans_more_dets, show_less;
    TextView tvprivate, tvclear, tv_anstit, tv_show;
    EditText prescribe_text, diagnosis, pb_cause, lab_t, ddx, pdx, treatment_plan, p_tips, followup;
    Switch reminder_switch;
    EditText edit_remind_days;

    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs";
    public static final String Login_Status = "Login_Status_key";
    public static final String user_name = "user_name_key";
    public static final String password = "password_key";
    public static final String Name = "Name_key";
    public static final String bcountry = "bcountry_key";
    public static final String photo_url = "photo_url";
    public static final String token = "token_key";
    FrameLayout ad_layout;
    ImageView home_ad, ad_close;
    RelativeLayout ad_close_layout;

    //LinearLayout prescribe_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.query_new_view_detail);

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        //------------ Object Creations -------------------------------
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle("");

            TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
            Typeface khandBold = Typeface.createFromAsset(getApplicationContext().getAssets(), Model.font_name_bold);
            mTitle.setTypeface(khandBold);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.app_color));
        }
        //-----------------------------------------------------------------------

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        allow_answer = "no";

        btn_upload_pres = (Button) findViewById(R.id.btn_upload_pres);

        file_list = (LinearLayout) findViewById(R.id.file_list);

        ad_layout = (FrameLayout) findViewById(R.id.ad_layout);
        home_ad = (ImageView) findViewById(R.id.home_ad);
        ad_close = (ImageView) findViewById(R.id.ad_close);
        ad_close_layout = (RelativeLayout) findViewById(R.id.ad_close_layout);

        prescribe_text = (EditText) findViewById(R.id.prescribe_text);
        edit_remind_days = (EditText) findViewById(R.id.edit_remind_days);
        reminder_switch = (Switch) findViewById(R.id.reminder_switch);
        prescribe_layout = (LinearLayout) findViewById(R.id.prescribe_layout);
        days_layout = (LinearLayout) findViewById(R.id.days_layout);
        extra_ans_layout = (LinearLayout) findViewById(R.id.extra_ans_layout);
        btn_notanswer = (Button) findViewById(R.id.btn_notanswer);
        followup_switch = (Switch) findViewById(R.id.followup_switch);
        btn_ansquery = (Button) findViewById(R.id.btn_ansquery);
        btn_reload = (Button) findViewById(R.id.btn_reload);
        button_layout = (LinearLayout) findViewById(R.id.button_layout);
        netcheck_layout = (LinearLayout) findViewById(R.id.netcheck_layout);
        nolayout = (LinearLayout) findViewById(R.id.nolayout);
        myLayout = (LinearLayout) findViewById(R.id.parent_qalayout);
        scrollview = (ScrollView) findViewById(R.id.scrollview);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        answer_layout = (LinearLayout) findViewById(R.id.answer_layout);
        show_more = (LinearLayout) findViewById(R.id.show_more);
        ans_more_dets = (LinearLayout) findViewById(R.id.ans_more_dets);
        diagnosis = (EditText) findViewById(R.id.diagnosis);
        pb_cause = (EditText) findViewById(R.id.pb_cause);
        lab_t = (EditText) findViewById(R.id.lab_t);
        ddx = (EditText) findViewById(R.id.ddx);
        pdx = (EditText) findViewById(R.id.pdx);
        treatment_plan = (EditText) findViewById(R.id.treatment_plan);
        p_tips = (EditText) findViewById(R.id.p_tips);
        followup = (EditText) findViewById(R.id.followup);
        btn_done = (Button) findViewById(R.id.btn_done);
        tvclear = (TextView) findViewById(R.id.tvclear);
        tvprivate = (TextView) findViewById(R.id.tvprivate);
        show_less = (LinearLayout) findViewById(R.id.show_less);
        edt_answer = (EditText) findViewById(R.id.edt_answer);
        tv_anstit = (TextView) findViewById(R.id.tv_anstit);
        tv_show = (TextView) findViewById(R.id.tv_show);
        btn_write_pres = (Button) findViewById(R.id.btn_write_pres);

        if (new Detector().isTablet(getApplicationContext())) {
            tv_anstit.setTextSize(18);
            edt_answer.setTextSize(20);
            pb_cause.setTextSize(20);
            lab_t.setTextSize(20);
            ddx.setTextSize(20);
            pdx.setTextSize(20);
            treatment_plan.setTextSize(20);
            p_tips.setTextSize(20);
            followup.setTextSize(20);
            btn_done.setTextSize(20);
            btn_notanswer.setTextSize(20);
            tv_show.setTextSize(20);
            tvprivate.setTextSize(20);
            diagnosis.setTextSize(20);
        }

        //opt_ffollowup = (CheckBox) findViewById(R.id.opt_ffollowup);

        if (new Detector().isTablet(getApplicationContext())) {
            btn_ansquery.setHeight(75);
            btn_ansquery.setTextSize(25);
        }

        try {

            Intent intent = getIntent();
            followupcode = intent.getStringExtra("followupcode");
            query_price_text = intent.getStringExtra("str_price");
            pat_from_text = intent.getStringExtra("pat_location");
            qtype = intent.getStringExtra("qtype");

            System.out.println("Get intent followupcode----" + followupcode);
            System.out.println("Get intent query_price_text----" + query_price_text);
            System.out.println("Get intent pat_from_text----" + pat_from_text);

            //-----------------------------------------------------
            if (qtype.equals("new_query")) {
                btn_ansquery.setVisibility(View.VISIBLE);
                System.out.println("qtype--new q-" + qtype);
            } else if (qtype.equals("answered_query")) {
                btn_ansquery.setVisibility(View.GONE);
                System.out.println("qtype--asnwered q-" + qtype);
            }
            //-----------------------------------------------------

        } catch (Exception e) {
            e.printStackTrace();
        }

        edt_answer.setText(Model.query_typed);


        ad_close_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ad_layout.setVisibility(View.GONE);

                //--------------------Ad- Close--------
                String ad_url = Model.BASE_URL + "/sapp/closeAd?user_id=" + Model.id + "&track_id=" + track_id_val + "&token=" + Model.token;
                System.out.println("ad_url----------" + ad_url);
                new JSON_Close().execute(ad_url);
                //---------------------Ad- Close-------
            }
        });

        reminder_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    days_layout.setVisibility(View.VISIBLE);
                } else {
                    days_layout.setVisibility(View.GONE);
                }
            }
        });


        btn_notanswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showNotAnswerAlert();
            }
        });

        btn_upload_pres.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                attach_dialog();
            }
        });

        btn_write_pres.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(NewQueryViewActivity.this, Prescriptions_Activity.class);
                i.putExtra("qid", current_qid);
                i.putExtra("patient_id", patient_id);
                startActivity(i);
                //finish();
            }
        });


        btn_ansquery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isInternetOn()) {
                    try {
                        if (current_qid != null && !current_qid.isEmpty() && !current_qid.equals("null") && !current_qid.equals("")) {
                            if ((Model.id) != null && !(Model.id).isEmpty() && !(Model.id).equals("null") && !(Model.id).equals("")) {
                                try {
                                    //------------------------ Can I answer ----------------
                                    String url = Model.BASE_URL + "sapp/canIAnswer?user_id=" + (Model.id) + "&qid=" + current_qid + "&token=" + Model.token;
                                    System.out.println("canIAnswer url------" + url);
                                    new JSON_canianswer().execute(url);
                                    //------------------------ Can I answer ----------------

                                } catch (Exception e) {
                                    e.printStackTrace();

                                    go_back_msg();
                                }
                            } else {


                                go_back_msg();
                            }
                        } else {

                            go_back_msg();
                        }
                    } catch (Exception e) {

                    }
                } else {
                    Toast.makeText(getApplicationContext(), "No internet connection. please try again..!", Toast.LENGTH_SHORT).show();
                }
            }
        });


        btn_reload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isInternetOn()) {
                    fullprocess();
                } else {
                    Toast.makeText(getApplicationContext(), "No internet connection. please try again..!", Toast.LENGTH_SHORT).show();

                    progressBar.setVisibility(View.GONE);
                    scrollview.setVisibility(View.GONE);
                    nolayout.setVisibility(View.GONE);
                    netcheck_layout.setVisibility(View.VISIBLE);
                    button_layout.setVisibility(View.GONE);
                    btn_ansquery.setVisibility(View.GONE);
                }
            }
        });


        if (isInternetOn()) {
            fullprocess();
        } else {

            Toast.makeText(getApplicationContext(), "No internet connection. please try again..!", Toast.LENGTH_SHORT).show();

            progressBar.setVisibility(View.GONE);
            scrollview.setVisibility(View.GONE);
            nolayout.setVisibility(View.GONE);
            netcheck_layout.setVisibility(View.VISIBLE);
            button_layout.setVisibility(View.GONE);
            btn_ansquery.setVisibility(View.GONE);
        }

        tvprivate.setText(Html.fromHtml("Private diagnosis (<b>Won't be displayed to the patient</b>)"));

        edt_answer.addTextChangedListener(edt_answerWatcher);


        btn_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isInternetOn()) {

                    ent_ans = edt_answer.getText().toString();
                    prescribe_value = prescribe_text.getText().toString();

                    if (!ent_ans.equals("")) {
                        if (prescribe_status.equals("1")) {
                            if (!(prescribe_value.equals(""))) {
                                submit_answer();
                            } else {
                                prescribe_text.setError("Prescription cannot be Empty");
                                prescribe_text.requestFocus();
                            }
                        } else {
                            submit_answer();
                        }
                    } else {
                        edt_answer.setError("Answer cannot be Empty");
                        edt_answer.requestFocus();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "No internet connection. Please try again..", Toast.LENGTH_SHORT).show();
                }
            }
        });


        int myNum = 0;
        try {
            myNum = Integer.parseInt((query_price_text));
            if (myNum >= 30) {
                ans_more_dets.setVisibility(View.VISIBLE);
                show_more.setVisibility(View.GONE);
            } else {
                ans_more_dets.setVisibility(View.GONE);
                show_more.setVisibility(View.VISIBLE);
            }
        } catch (NumberFormatException nfe) {

            //System.out.println("Could not parse " + nfe);
        }

        show_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ans_more_dets.setVisibility(View.VISIBLE);
                show_more.setVisibility(View.GONE);
            }
        });

        show_less.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ans_more_dets.setVisibility(View.GONE);
                show_more.setVisibility(View.VISIBLE);
            }
        });


        tvclear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                edt_answer.setText("");
                Model.query_typed = "";
            }
        });

        System.out.println("isEnablefFollowup----------" + isEnablefFollowup);

        followup_switch.setVisibility(View.VISIBLE);
        followup_switch.setChecked(true);
    }

    public void onClick(View v) {

        if (isInternetOn()) {

            try {

                TextView tv_filename = (TextView) v.findViewById(R.id.tv_filename);
                String file_name = tv_filename.getText().toString();
                System.out.println("str_filename-------" + file_name);

                Intent i = new Intent(getApplicationContext(), GridViewActivity.class);
                //Intent i = new Intent(getApplicationContext(), Attachment_List_Activity.class);
                //Intent i = new Intent(getApplicationContext(), Attached_List_Activity.class);
                //i.putExtra("filetxt", files_text);
                i.putExtra("filetxt", file_name);
                startActivity(i);
                overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);


            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(getApplicationContext(), "Internet is not connected.", Toast.LENGTH_SHORT).show();
        }
    }


    public void onClickFileOpen(View v) {

        try {

            View parent = (View) v.getParent();

            TextView tv_filename = (TextView) v.findViewById(R.id.tv_answer_filename);

            String file_name = tv_filename.getText().toString();

            System.out.println("Clicked str_filename-------" + file_name);

            Intent i = new Intent(getApplicationContext(), GridViewActivity.class);
            i.putExtra("filetxt", file_name);
            startActivity(i);
            overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private class JSON_canianswer extends AsyncTask<String, Void, Boolean> {

        AlertDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new SpotsDialog(NewQueryViewActivity.this);
            dialog.setTitle("Picking this Query., please wait");
            dialog.show();
            dialog.setCancelable(false);
        }

        @Override
        protected Boolean doInBackground(String... urls) {
            try {
                str_response = new JSONParser().getJSONString(urls[0]);
                System.out.println("str_response--------------" + str_response);
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return false;
        }

        protected void onPostExecute(Boolean result) {

            try {

                jsonobj_canisnaswer = new JSONObject(str_response);
 /*
                //------------ Tracker ------------------------
                MyApp.tracker().send(new HitBuilders.EventBuilder()
                        .setCategory("Query_View")
                        .setAction("Answering_Status=" + str_response)
                        .build());
                //------------ Tracker ------------------------*/

                if (jsonobj_canisnaswer.has("token_status")) {
                    String token_status = jsonobj_canisnaswer.getString("token_status");
                    if (token_status.equals("0")) {

                        //============================================================
                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        editor.putString(Login_Status, "0");
                        editor.apply();
                        //============================================================

                        finishAffinity();
                        Intent intent = new Intent(NewQueryViewActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                } else {

                    answering_status = jsonobj_canisnaswer.getString("status");

                    opt_freefollow = jsonobj_canisnaswer.getString("opt_freefollow");
                    enable_freefollow = jsonobj_canisnaswer.getString("enable_freefollow");

                    //----------------------------------------------
                    if ((opt_freefollow).equals("1")) {
                        check_enable_ffollowup = "Yes";
                        isEnablefFollowup = "Yes";
                        followup_switch.setVisibility(View.VISIBLE);
                        followup_switch.setChecked(true);
                        System.out.println("Opt_freefollowup_--------------1");
                    } else {

                        check_enable_ffollowup = "No";
                        isEnablefFollowup = "No";

                        followup_switch.setVisibility(View.GONE);
                        followup_switch.setChecked(false);
                    }
                    //----------------------------------------------

                    //----------------------------------------------
                    if ((answering_status).equals("0")) {
                        Toast.makeText(getApplicationContext(), "Sorry.! Another doctor has picked. You are not allowed to answer this query.", Toast.LENGTH_LONG).show();
                    } else {
                        button_layout.setVisibility(View.VISIBLE);
                        answer_layout.setVisibility(View.VISIBLE);
                        btn_ansquery.setVisibility(View.GONE);
                        scrollview.fullScroll(ScrollView.FOCUS_DOWN);
                    }
                    try {

                        //----------- Flurry -------------------------------------------------
                        Map<String, String> articleParams = new HashMap<String, String>();
                        articleParams.put("android.doc.qid", current_qid);
                        articleParams.put("android.doc.answering_status", answering_status);
                        articleParams.put("android.doc.check_enable_free_followup", check_enable_ffollowup);
                        FlurryAgent.logEvent("android.doc.Can_Answer_Status", articleParams);
                        //----------- Flurry -------------------------------------------------

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    dialog.cancel();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    public void fullprocess() {

        //------------ Object Creations -------------------------------
        try {
            if ((Model.id) != null && !(Model.id).isEmpty() && !(Model.id).equals("null") && !(Model.id).equals("")) {
                if (followupcode != null && !followupcode.isEmpty() && !followupcode.equals("null") && !followupcode.equals("")) {

                    //----------------------------------------------------
                    String full_url = Model.BASE_URL + "sapp/jsonviewquery4doc?followupcode=" + followupcode + "&user_id=" + (Model.id) + "&token=" + Model.token + "&enc=1&isAFiles=1";
                    System.out.println("New_Query_full_url-------------" + full_url);
                    new JSON_QueryView().execute(full_url);
                    //----------------------------------------------------

                    scrollview.fullScroll(ScrollView.FOCUS_DOWN);

                } else {
                    go_back_msg();
                }
            } else {
                go_back_msg();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        //------------ Object Creations -------------------------------
    }

    class JSON_QueryView extends AsyncTask<String, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressBar.setVisibility(View.VISIBLE);
            scrollview.setVisibility(View.GONE);
            nolayout.setVisibility(View.GONE);
            netcheck_layout.setVisibility(View.GONE);
            button_layout.setVisibility(View.GONE);
            btn_ansquery.setVisibility(View.GONE);

            startTime = System.currentTimeMillis();

        }

        @Override
        protected Boolean doInBackground(String... urls) {

            try {
                str_response = new JSONParser().getJSONString(urls[0]);
                System.out.println("str_response--------------" + str_response);

                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }

            return false;
        }

        protected void onPostExecute(Boolean result) {

            myLayout.removeAllViews();

            try {

                jsonobj = new JSONObject(str_response);

                if (jsonobj.has("token_status")) {
                    String token_status = jsonobj.getString("token_status");
                    if (token_status.equals("0")) {

                        //============================================================
                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        editor.putString(Login_Status, "0");
                        editor.apply();
                        //============================================================

                        finishAffinity();
                        Intent intent = new Intent(NewQueryViewActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }

                } else {

                    patient_id = jsonobj.getString("patient_id");
                    qansby = jsonobj.getString("askedby_name");
                    qcanianswer = jsonobj.getString("cananswer");
                    current_qid = jsonobj.getString("qid");
                    qitems = jsonobj.getString("items");

                    if (jsonobj.has("diagnose_prediction")) {
                        diagnose_prediction_text = jsonobj.getString("diagnose_prediction");
                        System.out.println("diagnose_prediction_text=============== " + diagnose_prediction_text);
                    } else {
                        diagnose_prediction_text = "";
                    }

                    System.out.println("diagnose_prediction_text=============== " + diagnose_prediction_text);
                    System.out.println("patient_id=============== " + patient_id);


                    if (jsonobj.has("jio_eo")) {
                        jio_query = jsonobj.getString("jio_eo");
                        System.out.println("This is Jio Query-----------------");

                        ans_more_dets.setVisibility(View.VISIBLE);
                        show_more.setVisibility(View.GONE);

                    } else {
                        jio_query = "0";
                        System.out.println("This is Not jio Query-----------------");
                    }


                    //------------ Get Array -----------------------------------
                    String array_text = jsonobj.getString("ans_ext_expansion");
                    String array_text_query = jsonobj.getString("query_ext_expansion");

                    System.out.println("array_text_ans-----" + array_text);
                    System.out.println("array_text_query-----" + array_text_query);

                    //-------------- query Key -----------------------------
                    JSONObject query_arrayJSONObj = new JSONObject(array_text_query);
                    Iterator<String> query_arrayiterator = query_arrayJSONObj.keys();
                    while (query_arrayiterator.hasNext()) {
                        String query_key = query_arrayiterator.next();
                        String value_of_key = query_arrayJSONObj.optString(query_key);

                        System.out.println("query_key------------------" + query_key);
                        System.out.println("value_of_key------------------" + value_of_key);

                        extra_query_map.put(query_key, value_of_key);
                    }
                    //-------------- query Key -----------------------------

                    //-------------- Ans Key -----------------------------
                    JSONObject ans_arrayJSONObj = new JSONObject(array_text);
                    Iterator<String> ans_arrayiterator = ans_arrayJSONObj.keys();
                    while (ans_arrayiterator.hasNext()) {
                        String ans_key = ans_arrayiterator.next();
                        String ans_value_of_key = ans_arrayJSONObj.optString(ans_key);

                        System.out.println("ans_key------------------" + ans_key);
                        System.out.println("ans_value_of_key------------------" + ans_value_of_key);

                        extra_ans_map.put(ans_key, ans_value_of_key);
                    }
                    //-------------- Ans Key -----------------------------

                    //------------ Get Array -----------------------------------
                    long elapsedTime = System.currentTimeMillis() - startTime;
                    System.out.println("Total elapsed http request/response time in milliseconds: " + elapsedTime);

                    try {

                        //----------- Flurry -------------------------------------------------
                        Map<String, String> articleParams = new HashMap<String, String>();
                        articleParams.put("android.doc.qid", "" + current_qid);
                        articleParams.put("android.doc.userid", "" + (Model.id));
                        articleParams.put("android.doc.milliseconds", "" + elapsedTime);
                        FlurryAgent.logEvent("android.doc.Query_View", articleParams);
                        //----------- Flurry -------------------------------------------------

                    } catch (Exception er) {
                        er.printStackTrace();
                    }


                    if (jsonobj.has("prescribe")) {
                        prescribe_status = jsonobj.getString("prescribe");
                        if (prescribe_status.equals("1")) {
/*                            extra_ans_layout.setVisibility(View.GONE);
                            prescribe_layout.setVisibility(View.VISIBLE);*/
                        }
                        if (prescribe_status.equals("0")) {
                          /*   extra_ans_layout.setVisibility(View.VISIBLE);
                            prescribe_layout.setVisibility(View.GONE);*/
                        }

                    } else {
                        prescribe_status = "0";
                    }


                    if (qitems.length() > 2) {

                        try {
                            JSONArray jarray = jsonobj.getJSONArray("items");
                            System.out.println("Items jarray.length------" + jarray.length());

                            for (int i = 0; i < jarray.length(); i++) {
                                jsonobj_items = jarray.getJSONObject(i);
                                System.out.println("jsonobj_Items------" + jsonobj_items.toString());
                                class_text = jsonobj_items.getString("class");

                                if (jsonobj_items.has("enable_prescription")) {
                                    enable_prescription_val = jsonobj_items.getString("enable_prescription");
                                } else {
                                    enable_prescription_val = "0";
                                }

                                System.out.println("class_text------" + class_text);

                                if (class_text.equals("bubbledRight")) {

                                    if (new Detector().isTablet(getApplicationContext())) {
                                        vi = getLayoutInflater().inflate(R.layout.query_thread_view, null);
                                    } else {
                                        vi = getLayoutInflater().inflate(R.layout.query_thread_view, null);
                                    }

                                    /*
                                     * Subscribe on Namma Coimbatore youtube channel for videos
                                     * */
                                    query_display_layout = (LinearLayout) vi.findViewById(R.id.query_display_layout);
                                    query_webview = (WebView) vi.findViewById(R.id.query_webview);

                                    extra_hwlayout = (LinearLayout) vi.findViewById(R.id.extra_hwlayout);
                                    extra_hw_details = (LinearLayout) vi.findViewById(R.id.extra_hw_details);
                                    extra_hw_title = (LinearLayout) vi.findViewById(R.id.extra_hw_title);
                                    prediction_layout = (LinearLayout) vi.findViewById(R.id.prediction_layout);
                                    prediction_inner_layout = (LinearLayout) vi.findViewById(R.id.prediction_inner_layout);

                                    extra_layout = (LinearLayout) vi.findViewById(R.id.extra_layout);
                                    tv_gender = (TextView) vi.findViewById(R.id.tv_gender);
                                    ImageView img_preview = (ImageView) vi.findViewById(R.id.img_preview);
                                    tvattached = (TextView) vi.findViewById(R.id.tvattached);
                                    files_layout = (LinearLayout) vi.findViewById(R.id.files_layout);
                                    layout_attachfile = (LinearLayout) vi.findViewById(R.id.layout_attachfile);
                                    tv_pat_name = (TextView) vi.findViewById(R.id.tv_pat_name);
                                    tv_pat_place = (TextView) vi.findViewById(R.id.tv_pat_place);
                                    tv_query = (TextView) vi.findViewById(R.id.tvquery);
                                    tvt_morecomp = (TextView) vi.findViewById(R.id.tvt_morecomp);
                                    tv_morecomp = (TextView) vi.findViewById(R.id.tv_morecomp);
                                    tvt_prevhist = (TextView) vi.findViewById(R.id.tvt_prevhist);
                                    tv_prevhist = (TextView) vi.findViewById(R.id.tv_prevhist);
                                    tvt_curmedi = (TextView) vi.findViewById(R.id.tvt_curmedi);
                                    tv_curmedi = (TextView) vi.findViewById(R.id.tv_curmedi);
                                    tvt_pastmedi = (TextView) vi.findViewById(R.id.tvt_pastmedi);
                                    tv_pastmedi = (TextView) vi.findViewById(R.id.tv_pastmedi);
                                    tvt_labtest = (TextView) vi.findViewById(R.id.tvt_labtest);
                                    tv_labtest = (TextView) vi.findViewById(R.id.tv_labtest);
                                    tv_datetime = (TextView) vi.findViewById(R.id.tv_datetime);
                                    tv_filename = (TextView) vi.findViewById(R.id.tv_filename);

                                    font_reg = Typeface.createFromAsset(getAssets(), Model.font_name);
                                    font_bold = Typeface.createFromAsset(getAssets(), Model.font_name_bold);

                                    tv_pat_name.setTypeface(font_bold);
                                    tv_pat_place.setTypeface(font_reg);
                                    //tv_gender.setTypeface(font_reg);

                                    tv_query.setTypeface(font_reg);

                                    layout_attachfile.setVisibility(View.GONE);

                                    msg_text = jsonobj_items.getString("msg");
                                    msg_ext_text = jsonobj_items.getString("msg_ext");
                                    files_text = jsonobj_items.getString("files");
                                    class_text = jsonobj_items.getString("class");
                                    time_text = jsonobj_items.getString("time");

                                    if (jsonobj_items.has("strHtml")) {
                                        strHtml_text = jsonobj_items.getString("strHtml");

                                        query_display_layout.setVisibility(View.GONE);
                                        query_webview.setVisibility(View.VISIBLE);

                                        query_webview.getSettings().setJavaScriptEnabled(true);
                                        query_webview.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);
                                        query_webview.loadDataWithBaseURL("", strHtml_text, "text/html", "UTF-8", "");
                                    } else {

                                        query_display_layout.setVisibility(View.VISIBLE);
                                        query_webview.setVisibility(View.GONE);
                                    }


                                    // strHtml_text = jsonobj_items.getString("strHtml");

                                    //--------------------------------------------------------
                                    query_webview.getSettings().setJavaScriptEnabled(true);
                                    query_webview.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);
                                    query_webview.loadData(strHtml_text, "text/html; charset=utf-8", "UTF-8");
                                    //--------------------------------------------------------

                                    //----------------diagnose_prediction_text-----------------------------------------
                                    System.out.println("diagnose_prediction_text-------------" + diagnose_prediction_text);

                                    try {
                                        if (diagnose_prediction_text.length() > 5) {

                                            prediction_layout.setVisibility(View.VISIBLE);

                                            JSONArray jpred_array = new JSONArray(diagnose_prediction_text);
                                            System.out.println("Items jarray.length------" + jpred_array.length());

                                            for (int p = 0; p < jpred_array.length(); p++) {
                                                jsonobj_items = jpred_array.getJSONObject(p);
                                                System.out.println("Pred jsonobj_Items------" + jsonobj_items.toString());

                                                String pred_name_text = jsonobj_items.getString("name");
                                                String prof_name_text = jsonobj_items.getString("prof_name");
                                                String icd_text = jsonobj_items.getString("icd");
                                                String icd_name_text = jsonobj_items.getString("icd_name");
                                                String rank_text = jsonobj_items.getString("rank");

                                                pred_vi = getLayoutInflater().inflate(R.layout.prediction_row, null);
                                                tv_pred_slno = (TextView) pred_vi.findViewById(R.id.tv_pred_slno);
                                                tv_pred_name = (TextView) pred_vi.findViewById(R.id.tv_pred_name);
                                                tv_pred_profname = (TextView) pred_vi.findViewById(R.id.tv_pred_profname);
                                                tv_pred_icd = (TextView) pred_vi.findViewById(R.id.tv_pred_icd);
                                                tv_pred_rank = (TextView) pred_vi.findViewById(R.id.tv_pred_rank);

                                                tv_pred_slno.setText("" + (p + 1) + ". ");
                                                tv_pred_name.setText(pred_name_text);
                                                tv_pred_profname.setText("(" + prof_name_text + ")");
                                                tv_pred_icd.setText(icd_text + " " + icd_name_text);
                                                tv_pred_rank.setText("Rank : " + rank_text);

                                                prediction_inner_layout.addView(pred_vi);

                                            }
                                        } else {
                                            prediction_layout.setVisibility(View.GONE);
                                            System.out.println("No Prediction--------------------");
                                        }

                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    //----------------diagnose_prediction_text-----------------------------------------


                                    //-------------------------- Age Gender ------------------------------------------------
                                    if (jsonobj_items.has("age_gender")) {
                                        age_gender_txt = jsonobj_items.getString("age_gender");
                                        if (age_gender_txt.length() > 2) {
                                            json_gender = new JSONObject(age_gender_txt);

                                            if (json_gender.has("age")) {
                                                age_txt = json_gender.getString("age");
                                            }
                                            if (json_gender.has("gender")) {
                                                gender_txt = json_gender.getString("gender");
                                            }

                                            System.out.println("age_txt------" + age_txt);
                                            System.out.println("gender_txt------" + gender_txt);

                                            if (age_txt != null && !age_txt.isEmpty() && !age_txt.equals("null") && !age_txt.equals("")) {
                                                tv_gender.setText(Html.fromHtml("<b>Age: </b>" + age_txt + ", <b>Gender: </b>" + gender_txt));
                                            } else {
                                                tv_gender.setVisibility(View.GONE);
                                            }

                                            //-----------------------Extra HW --------------------------------------
                                            if (json_gender.has("extra")) {
                                                //extra_txt = json_gender.getString("extra");

                                                JSONArray jarr = json_gender.getJSONArray("extra");
                                                System.out.println("jarr Length------" + jarr.length());
                                                System.out.println("jarr------" + jarr.toString());

                                                for (int j = 0; j < jarr.length(); j++) {
                                                    jsonobj_hwextra = jarr.getJSONObject(j);
                                                    System.out.println("jsonobj_hwextra------" + j + " ----" + jsonobj_hwextra.toString());


                                                    title_text = jsonobj_hwextra.getString("title");
                                                    fields_text = jsonobj_hwextra.getString("fields");

                                                    //--------------------- Title--------------------------------
                                                    if (new Detector().isTablet(getApplicationContext())) {
                                                        vi_hw_full = getLayoutInflater().inflate(R.layout.tab_query_view_extra_full, null);
                                                    } else {
                                                        vi_hw_full = getLayoutInflater().inflate(R.layout.query_view_extra_full, null);
                                                    }


                                                    tv_etitle = (TextView) vi_hw_full.findViewById(R.id.tv_etitle);
                                                    main_data_layout = (LinearLayout) vi_hw_full.findViewById(R.id.main_data_layout);
                                                    tv_etitle.setText(Html.fromHtml("<b>" + title_text + "</b>"));
                                                    System.out.println("title_text-------------------->" + title_text);
                                                    //extra_hw_title.addView(vi_hw_tit);
                                                    //--------------------- Title--------------------------------

                                                    tv_etitle.setText(Html.fromHtml("<b>" + title_text + "</b>"));


                                                    json_fields = new JSONObject(fields_text);

                                                    for (int f = 1; f <= 10; f++) {

                                                        String s = "" + f;
                                                        if (json_fields.has("" + s)) {

                                                            String thread = json_fields.getString("" + s);
                                                            System.out.println("thread-----" + thread);

                                                            jsonon_titem = new JSONObject(thread);
                                                            System.out.println("jsonon_titem------" + jsonon_titem.toString());
                                                            //String jsonon_titem.getString("");

                                                            Iterator<String> iter = jsonon_titem.keys();
                                                            while (iter.hasNext()) {
                                                                String key = iter.next();
                                                                System.out.println("key-----" + key);

                                                                try {
                                                                    Object value = jsonon_titem.get(key);
                                                                    System.out.println("key_values=======" + value.toString());

                                                                    if (new Detector().isTablet(getApplicationContext())) {
                                                                        vi_hw = getLayoutInflater().inflate(R.layout.tab_query_view_extra_details, null);
                                                                    } else {
                                                                        vi_hw = getLayoutInflater().inflate(R.layout.query_view_extra_details, null);
                                                                    }

                                                                    //main_data_layout = (LinearLayout) vi_hw.findViewById(R.id.main_data_layout);
                                                                    //tv_etitle = (TextView) vi_hw.findViewById(R.id.tv_etitle);
                                                                    tv_keytext = (TextView) vi_hw.findViewById(R.id.tv_keytext);
                                                                    tv_valuetext = (TextView) vi_hw.findViewById(R.id.tv_valuetext);

                                                                    //tv_keytext.setText(Html.fromHtml("<b>" + key + "</b>"));
                                                                    tv_keytext.setText(key + ": ");
                                                                    tv_valuetext.setText(value.toString());

                                                                    main_data_layout.addView(vi_hw);


                                                                } catch (JSONException e) {
                                                                    System.out.println("Exep----" + e.toString());
                                                                }
                                                            }
                                                        }
                                                    }

                                                    extra_hw_details.addView(vi_hw_full);
                                                }
                                            }
                                            //-----------------------Extra HW --------------------------------------
                                        }
                                    }
                                    //-------------------------- Age Gender ---------------------------------------------

                                    try {
                                        System.out.println("msg_text------" + msg_text);
                                        System.out.println("msg_ext_text------" + msg_ext_text);
                                        System.out.println("files_text------" + files_text);
                                        System.out.println("class_text------" + class_text);
                                        System.out.println("time_text------" + time_text);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }

                                    noto_reg = Typeface.createFromAsset(getAssets(), Model.font_name);
                                    noto_bold = Typeface.createFromAsset(getAssets(), Model.font_name_bold);

                                    Typeface robo_reg = Typeface.createFromAsset(getAssets(), Model.font_name);
                                    Typeface noto_bold = Typeface.createFromAsset(getAssets(), Model.font_name_bold);

                                    tv_query.setTypeface(noto_reg);
                                    tv_pat_name.setTypeface(noto_bold);
                                    tv_pat_place.setTypeface(robo_reg);
                                    tv_datetime.setTypeface(noto_reg);

                                    tv_query.setAutoLinkMask(Linkify.ALL);
                                    tv_query.setText(Html.fromHtml(msg_text));
                                    tv_datetime.setText(time_text);

                                    if (qansby != null && !qansby.isEmpty() && !qansby.equals("") && !qansby.equals("null")) {
                                        tv_pat_name.setText(Html.fromHtml("<b>" + qansby + "</b>"));
                                    } else {
                                        tv_pat_name.setVisibility(View.GONE);
                                    }

                                    if (pat_from_text != null && !pat_from_text.isEmpty() && !pat_from_text.equals("") && !pat_from_text.equals("null")) {
                                        tv_pat_place.setText(pat_from_text);
                                    } else {
                                        tv_pat_place.setVisibility(View.GONE);
                                    }


                                    if ((msg_ext_text.length()) > 2) {

                                        //-------------- Dynamic Key -----------------------------
                                        JSONObject categoryJSONObj = new JSONObject(msg_ext_text);
                                        System.out.println("question_ext-----" + msg_ext_text);

                                        Iterator<String> iterator = categoryJSONObj.keys();
                                        while (iterator.hasNext()) {
                                            String key = iterator.next();
                                            String value_of_key = categoryJSONObj.optString(key);

                                            //Log.i("TAG", "key:" + key + "--Value::" + categoryJSONObj.optString(key));
                                            System.out.println("Key------------------" + key);
                                            System.out.println("Value------------------" + value_of_key);

                                            if (!value_of_key.equals("")) {

                                                vi_ext = getLayoutInflater().inflate(R.layout.extra_answer_row, null);
                                                tv_ext_title = (TextView) vi_ext.findViewById(R.id.tv_ext_title);
                                                tv_ext_desc = (TextView) vi_ext.findViewById(R.id.tv_ext_desc);

                                                String GET_KEY = extra_query_map.get(key);
                                                System.out.println("keykeykey---------------" + key);
                                                System.out.println("GET_KEY---------------" + GET_KEY);


                                                tv_ext_title.setText(GET_KEY);
                                                tv_ext_desc.setText(Html.fromHtml(categoryJSONObj.optString(key)));

                                                tv_ext_desc.setMovementMethod(LinkMovementMethod.getInstance());

                                                extra_layout.addView(vi_ext);
                                            }


                                        }


                                    }

                                    //---------------- Files ---------------------------------------
                                    if ((files_text.length()) > 2) {

                                        layout_attachfile.setVisibility(View.VISIBLE);

                                        System.out.println("files_text------" + files_text);
                                        JSONArray jarray_files = jsonobj_items.getJSONArray("files");
                                        System.out.println("jsonobj_items------" + jarray_files.toString());
                                        System.out.println("jarray_files.length()------" + jarray_files.length());

                                        tvattached.setText("Attachment:   " + jarray_files.length() + " File(s)");

                                        html_file_str = "";
                                        attach_file_text = "";

                                        for (int j = 0; j < jarray_files.length(); j++) {
                                            jsonobj_files = jarray_files.getJSONObject(j);

                                            System.out.println("jsonobj_files------" + j + " ----" + jsonobj_files.toString());

                                            file_user_id = jsonobj_files.getString("user_id");
                                            file_doctype = jsonobj_files.getString("doctype");
                                            file_file = jsonobj_files.getString("file");
                                            file_ext = jsonobj_files.getString("ext");
                                            file_full_url = jsonobj_files.getString("url");

                                            //------------------------ File Attached Text --------------------------------
                                            if (attach_file_text.equals("")) {
                                                attach_file_text = file_full_url;
                                                System.out.println("attach_file_text-------" + attach_file_text);
                                            } else {
                                                attach_file_text = attach_file_text + "###" + file_full_url;
                                                System.out.println("attach_file_text-------" + attach_file_text);
                                            }
                                            //------------------------ File Attached Text --------------------------------

                                            System.out.println("file_user_id--------" + file_user_id);
                                            System.out.println("file_doctype--------" + file_doctype);
                                            System.out.println("filename--------" + file_file);
                                            System.out.println("file_ext--------" + file_ext);

                                        }

                                        files_layout.setVisibility(View.GONE);
                                        //tv_filename.setText(attach_file_text);
                                        tv_filename.setText(files_text);

                                    } else {
                                        layout_attachfile.setVisibility(View.GONE);
                                    }
                                    //---------------- Files---------------------------------------

                                    myLayout.addView(vi);
                                }

                                //-------------------------- Bubble Left --------------------------------------------------
                                if (class_text.equals("bubbledLeft")) {

                                    if (new Detector().isTablet(getApplicationContext())) {
                                        vi_ans = getLayoutInflater().inflate(R.layout.query_thread_view_answer, null);
                                    } else {
                                        vi_ans = getLayoutInflater().inflate(R.layout.query_thread_view_answer, null);
                                    }


                                    webview_answer = (WebView) vi_ans.findViewById(R.id.webview_answer);
                                    answer_display_layout = (LinearLayout) vi_ans.findViewById(R.id.answer_display_layout);

                                    tv_answer_filename = (TextView) vi_ans.findViewById(R.id.tv_answer_filename);
                                    ans_extra_layout = (LinearLayout) vi_ans.findViewById(R.id.ans_extra_layout);
                                    tv_answer = (TextView) vi_ans.findViewById(R.id.tvanswer);
                                    tvt_probcause = (TextView) vi_ans.findViewById(R.id.tvt_probcause);
                                    tv_probcause = (TextView) vi_ans.findViewById(R.id.tv_probcause);
                                    tvt_invdone = (TextView) vi_ans.findViewById(R.id.tvt_invdone);
                                    tv_invdone = (TextView) vi_ans.findViewById(R.id.tv_invdone);
                                    tvt_diffdiag = (TextView) vi_ans.findViewById(R.id.tvt_diffdiag);
                                    tv_diffdiag = (TextView) vi_ans.findViewById(R.id.tv_diffdiag);
                                    tvt_probdiag = (TextView) vi_ans.findViewById(R.id.tvt_probdiag);
                                    tv_probdiag = (TextView) vi_ans.findViewById(R.id.tv_probdiag);
                                    tvt_tratplan = (TextView) vi_ans.findViewById(R.id.tvt_tratplan);
                                    tv_tratplan = (TextView) vi_ans.findViewById(R.id.tv_tratplan);
                                    tvt_prevmeasure = (TextView) vi_ans.findViewById(R.id.tvt_prevmeasure);
                                    tv_prevmeasure = (TextView) vi_ans.findViewById(R.id.tv_prevmeasure);
                                    tvt_follup = (TextView) vi_ans.findViewById(R.id.tvt_follup);
                                    tv_follup = (TextView) vi_ans.findViewById(R.id.tv_follup);
                                    tv_datetimeans = (TextView) vi_ans.findViewById(R.id.tv_datetime);
                                    tv_patfeedback = (TextView) vi_ans.findViewById(R.id.tv_patfeedback);
                                    feedback_section = (LinearLayout) vi_ans.findViewById(R.id.feedback_section);
                                    tv_replytext = (TextView) vi_ans.findViewById(R.id.tv_replytext);
                                    doctor_reply_section = (LinearLayout) vi_ans.findViewById(R.id.doctor_reply_section);
                                    btn_feedbacksubmit = (Button) vi_ans.findViewById(R.id.btn_feedbacksubmit);

                                    answer_layout_attachfile = (LinearLayout) vi_ans.findViewById(R.id.answer_layout_attachfile);
                                    answer_files_layout = (LinearLayout) vi_ans.findViewById(R.id.answer_files_layout);


                                    msg_text = jsonobj_items.getString("msg");
                                    msg_ext_text = jsonobj_items.getString("msg_ext");
                                    class_text = jsonobj_items.getString("class");
                                    regards = jsonobj_items.getString("regards");
                                    time_text = jsonobj_items.getString("time");

                                    answer_files_text = jsonobj_items.getString("files");
                                    System.out.println("answer_files_text_Getting--------" + answer_files_text);

                                    if (jsonobj_items.has("strHtml")) {
                                        strHtml_text = jsonobj_items.getString("strHtml");

                                        answer_display_layout.setVisibility(View.GONE);
                                        webview_answer.setVisibility(View.VISIBLE);

                                        webview_answer.getSettings().setJavaScriptEnabled(true);
                                        webview_answer.setBackgroundColor(Color.TRANSPARENT);
                                        webview_answer.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);
                                        webview_answer.loadDataWithBaseURL("", strHtml_text, "text/html", "UTF-8", "");

                                    } else {

                                        answer_display_layout.setVisibility(View.VISIBLE);
                                        webview_answer.setVisibility(View.GONE);
                                    }


                                    arr_feedback_text = jsonobj_items.getString("arr_feedback");

                                    System.out.println("arr_feedback_text---------" + arr_feedback_text);


                                    //--------------- patient Feedback ------------------------------
                                    if (jsonobj_items.has("arr_feedback")) {
                                        System.out.println("Yes JSOn Array--------------------------------");
                                        arr_feedback_text = jsonobj_items.getString("arr_feedback");

                                        if (arr_feedback_text.length() > 2) {

                                            feedback_section.setVisibility(View.VISIBLE);
                                            JSONObject jobject = new JSONObject(arr_feedback_text);
                                            System.out.println("arr_feedback_text---------" + arr_feedback_text);

                                            rating_text = jobject.getString("rating");
                                            pat_feedback_text = jobject.getString("feedback");
                                            feedback_id_text = jobject.getString("feedback_id");
                                            reply_text = jobject.getString("reply");


                                            tv_patfeedback.setText(pat_feedback_text);

                                            if (reply_text != null && !reply_text.isEmpty() && !reply_text.equals("null") && !reply_text.equals("")) {
                                                tv_replytext.setText(reply_text);
                                                doctor_reply_section.setVisibility(View.VISIBLE);
                                                btn_feedbacksubmit.setVisibility(View.GONE);
                                            } else {
                                                doctor_reply_section.setVisibility(View.GONE);
                                                btn_feedbacksubmit.setVisibility(View.VISIBLE);
                                            }

                                            feedback_section.setVisibility(View.VISIBLE);

                                        } else {
                                            feedback_section.setVisibility(View.GONE);
                                        }

                                    } else {
                                        System.out.println("No JSOn Array--------------------------------");
                                        feedback_section.setVisibility(View.GONE);
                                    }
                                    //--------------- patient Feedback ------------------------------


                                    btn_feedbacksubmit.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            showFeedbackDialog();
                                        }
                                    });


                                    font_reg = Typeface.createFromAsset(getAssets(), Model.font_name);
                                    font_bold = Typeface.createFromAsset(getAssets(), Model.font_name_bold);

                                    tv_answer.setTypeface(font_reg);

                                    tv_answer.setAutoLinkMask(Linkify.ALL);

                                    tv_answer.setText(Html.fromHtml(msg_text));
                                    tv_datetimeans.setText(time_text);

                                    System.out.println("msg_text------" + msg_text);
                                    System.out.println("msg_ext_text------" + msg_ext_text);
                                    System.out.println("class_text------" + class_text);
                                    System.out.println("class_text------" + class_text);
                                    System.out.println("time_text------" + time_text);

                                    //---------------- Msg Extra ---------------------------------------
                                    if ((msg_ext_text.length()) > 2) {


                                        ans_extra_layout.removeAllViews();
                                        //-------------- Dynamic Key -----------------------------
                                        JSONObject categoryJSONObj = new JSONObject(msg_ext_text);
                                        Iterator<String> iterator = categoryJSONObj.keys();
                                        while (iterator.hasNext()) {
                                            String key = iterator.next();
                                            String value_of_key = categoryJSONObj.optString(key);

                                            System.out.println("Key------------------" + key);
                                            System.out.println("Value------------------" + value_of_key);

                                            if (!value_of_key.equals("")) {

                                                vi_ans_ext = getLayoutInflater().inflate(R.layout.extra_answer_row, null);
                                                TextView tv_ext_title = (TextView) vi_ans_ext.findViewById(R.id.tv_ext_title);
                                                tv_ext_desc = (TextView) vi_ans_ext.findViewById(R.id.tv_ext_desc);

                                                tv_ext_title.setText(extra_ans_map.get(key));
                                                tv_ext_desc.setText(Html.fromHtml(categoryJSONObj.optString(key)));
                                                tv_ext_desc.setMovementMethod(LinkMovementMethod.getInstance());


                                                ans_extra_layout.addView(vi_ans_ext);
                                            }

                                        }
                                        //-------------- Dynamic Key -----------------------------

                                    }


                                    //---------------- Files ---------------------------------------
                                    System.out.println("answer_files_text$$$$$$$$$$$" + answer_files_text);

                                    if ((answer_files_text.length()) > 2) {

                                        answer_layout_attachfile.setVisibility(View.VISIBLE);

                                        System.out.println("answer_files_text------" + answer_files_text);
                                        JSONArray jarray_files = jsonobj_items.getJSONArray("files");
                                        System.out.println("jsonobj_items------" + jarray_files.toString());
                                        System.out.println("jarray_files.length()------" + jarray_files.length());

                                        tvattached.setText("Attachment:   " + jarray_files.length() + " File(s)");

                                        html_file_str = "";
                                        attach_file_text = "";

                                        for (int j = 0; j < jarray_files.length(); j++) {
                                            jsonobj_files = jarray_files.getJSONObject(j);

                                            System.out.println("jsonobj_files------" + j + " ----" + jsonobj_files.toString());

                                            file_user_id = jsonobj_files.getString("user_id");
                                            file_doctype = jsonobj_files.getString("doctype");
                                            file_file = jsonobj_files.getString("file");
                                            file_ext = jsonobj_files.getString("ext");
                                            file_full_url = jsonobj_files.getString("url");

                                            //------------------------ File Attached Text --------------------------------
                                            if (attach_file_text.equals("")) {
                                                attach_file_text = file_full_url;
                                                System.out.println("attach_file_text-------" + attach_file_text);
                                            } else {
                                                attach_file_text = attach_file_text + "###" + file_full_url;
                                                System.out.println("attach_file_text-------" + attach_file_text);
                                            }
                                            //------------------------ File Attached Text --------------------------------

                                            System.out.println("file_user_id--------" + file_user_id);
                                            System.out.println("file_doctype--------" + file_doctype);
                                            System.out.println("filename--------" + file_file);
                                            System.out.println("file_ext--------" + file_ext);

                                        }

                                        answer_files_layout.setVisibility(View.GONE);
                                        //tv_filename.setText(attach_file_text);

                                        tv_answer_filename.setText(answer_files_text);

                                    } else {
                                        answer_layout_attachfile.setVisibility(View.GONE);
                                    }
                                    //---------------- Files---------------------------------------


                                    myLayout.addView(vi_ans);


                                }
                                //---------------- Files-------------------------------------------------------------
                            }

                            if (enable_prescription_val.equals("1")) {
                                prescribe_layout.setVisibility(View.VISIBLE);
                            } else {
                                prescribe_layout.setVisibility(View.GONE);
                            }

                        } catch (Exception e) {

                            System.out.println("Exception---1---" + e.toString());
                            e.printStackTrace();

                            nolayout.setVisibility(View.VISIBLE);
                            scrollview.setVisibility(View.GONE);
                            progressBar.setVisibility(View.GONE);
                            netcheck_layout.setVisibility(View.GONE);
                            button_layout.setVisibility(View.GONE);
                            btn_ansquery.setVisibility(View.GONE);

                        }
                    } else {

                        nolayout.setVisibility(View.VISIBLE);
                        scrollview.setVisibility(View.GONE);
                        progressBar.setVisibility(View.GONE);
                        netcheck_layout.setVisibility(View.GONE);
                        button_layout.setVisibility(View.GONE);
                        btn_ansquery.setVisibility(View.GONE);
                    }


                    //------------------ Fetch Ad ----------------------------------------------
                    if ((Model.browser_country).equals("IN")) {
                        //--------------------Ad---------
                        Model.home_ad_flag = "false";
                        String ad_url = Model.BASE_URL + "/sapp/fetchAd?user_id=" + Model.id + "&browser_country=" + Model.browser_country + "&qid=" + current_qid + "page_src=4";
                        System.out.println("ad_url----------" + ad_url);
                        new JSON_Ad().execute(ad_url);
                        //---------------------Ad--------
                    } else {
                        System.out.println("Model.browser_country--------------" + Model.browser_country);
                    }
                    //=============================================================================
                }
            } catch (Exception e) {
                System.out.println("Exception---3---" + e.toString());
                e.printStackTrace();
                nolayout.setVisibility(View.VISIBLE);
                scrollview.setVisibility(View.GONE);
                progressBar.setVisibility(View.GONE);
                netcheck_layout.setVisibility(View.GONE);
                button_layout.setVisibility(View.GONE);
                btn_ansquery.setVisibility(View.GONE);
            }

            progressBar.setVisibility(View.GONE);
            scrollview.setVisibility(View.VISIBLE);
            nolayout.setVisibility(View.GONE);
            netcheck_layout.setVisibility(View.GONE);
            button_layout.setVisibility(View.VISIBLE);

            //scrollview.fullScroll(ScrollView.FOCUS_DOWN);

            //-----------------------------------------------------
            if (qtype.equals("new_query")) {
                btn_ansquery.setVisibility(View.VISIBLE);
                System.out.println("qtype--II new q-" + qtype);
            } else if (qtype.equals("answered_query")) {
                btn_ansquery.setVisibility(View.GONE);
                System.out.println("qtype--II asnwered q-" + qtype);
            }
            //-----------------------------------------------------

            long elapsedTime = System.currentTimeMillis() - startTime;
            System.out.println("Total elapsed http request/response time in milliseconds: " + elapsedTime);

            //------- Scrolling Bottom ===============================
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    scrollview.fullScroll(ScrollView.FOCUS_DOWN);
                }
            };
            scrollview.post(runnable);
            //------- Scrolling Bottom ===============================
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.query_view_answer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
            return true;
        }

        if (id == R.id.nav_ans_guidelines) {
            show_guidelines();
            return true;
        }

        if (id == R.id.nav_earnmore) {
            show_tips();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public final boolean isInternetOn() {

        ConnectivityManager connec = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);

        if (connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTED ||
                connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTED) {

            return true;

        } else if (
                connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.DISCONNECTED ||
                        connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.DISCONNECTED) {

            return false;
        }
        return false;
    }


    class JSONPostAnswer extends AsyncTask<JSONObject, Void, Boolean> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(NewQueryViewActivity.this);
            dialog.setMessage("Submitting, please wait");
            dialog.show();
            dialog.setCancelable(false);

            startTime = System.currentTimeMillis();
        }

        @Override
        protected Boolean doInBackground(JSONObject... urls) {
            try {

                JSONParser jParser = new JSONParser();
                jsonobj_postans = jParser.JSON_POST(urls[0], "submitAnswer");

                System.out.println("urls[0]---------------" + urls[0]);
                System.out.println("jsonobj_postans-------------" + jsonobj_postans.toString());

                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }

            return false;
        }

        protected void onPostExecute(Boolean result) {

            try {
                //jsonobj_postans = new JSONObject(str_response);

                if (jsonobj_postans.has("token_status")) {
                    String token_status = jsonobj_postans.getString("token_status");
                    if (token_status.equals("0")) {

                        //============================================================
                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        editor.putString(Login_Status, "0");
                        editor.apply();
                        //============================================================

                        finishAffinity();
                        Intent intent = new Intent(NewQueryViewActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }

                } else {

                    ans_isvalid = jsonobj_postans.getString("isValid");
                    ans_status = jsonobj_postans.getString("status");

                    if (ans_status != null && !ans_status.isEmpty() && !ans_status.equals("null") && !ans_status.equals("")) {

                        //--------------------------------------------------------------------
                        if ((ans_status).equals("1")) {

                            long elapsedTime = System.currentTimeMillis() - startTime;
                            System.out.println("Total elapsed http request/response time in milliseconds: " + elapsedTime);


                            //----------- Flurry -------------------------------------------------
                            Map<String, String> articleParams = new HashMap<String, String>();
                            articleParams.put("Qid:", current_qid);
                            articleParams.put("User_Id:", (Model.id));
                            articleParams.put("elapsedTime:", "" + elapsedTime);
                            FlurryAgent.logEvent("android.doc.Answer_Submit_Success", articleParams);
                            //----------- Flurry -------------------------------------------------

                            alertdia("Answer Submitted Successfully");
                        } else {


                            //----------- Flurry -------------------------------------------------
                            Map<String, String> articleParams = new HashMap<String, String>();
                            articleParams.put("Qid:", current_qid);
                            articleParams.put("User_Id:", (Model.id));
                            FlurryAgent.logEvent("android.doc.Answer_Submit_error", articleParams);
                            //----------- Flurry -------------------------------------------------


                            alertdia("Submitting Answer Failed");
                        }
                        //--------------------------------------------------------------------

                    } else {
                        alertdia("Submitting Answer Failed.!");
                    }
                }

                dialog.cancel();


            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void alertdia(String diamsg) {
        final Context context = this;
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder
                .setMessage(diamsg)
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        ((android.os.ResultReceiver) getIntent().getParcelableExtra("finisher")).send(1, new Bundle());

                        //Intent i = new Intent(NewQueryViewActivity.this, NewQueriesActivity.class);
                        //startActivity(i);
                        finish();

                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }


    private final TextWatcher edt_answerWatcher = new TextWatcher() {
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {

            Model.query_typed = "" + s;
            Integer slength = s.length();
            Integer rema = slength % 50;
            System.out.println("slength------------------" + slength);
            System.out.println("remainder------------------" + rema);
            if (rema == 0) {
                try {
                    draft_json = new JSONObject();
                    draft_json.put("user_id", Model.id);
                    draft_json.put("answer", s);
                    draft_json.put("question_id", current_qid);
                    draft_json.put("answered_by", (Model.id));
                    System.out.println("draft_json------------------" + draft_json);

                    new JSON_Draft().execute(draft_json);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        public void afterTextChanged(Editable s) {

        }
    };


    class JSON_Draft extends AsyncTask<JSONObject, Void, Boolean> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(JSONObject... urls) {

            try {
                JSONParser jParser = new JSONParser();
                text_jsonobj = jParser.JSON_POST(urls[0], "saveqanswer");
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return false;
        }

        protected void onPostExecute(Boolean result) {

        }
    }


    class JSON_notanswer extends AsyncTask<String, Void, Boolean> {
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(NewQueryViewActivity.this);
            dialog.setMessage("Query is releasing. Please wait..");
            dialog.show();
            dialog.setCancelable(false);
        }

        @Override
        protected Boolean doInBackground(String... urls) {

            try {
                str_response = new JSONParser().getJSONString(urls[0]);
                System.out.println("str_response--------------" + str_response);

                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return false;
        }

        protected void onPostExecute(Boolean result) {


            try {
                try {

                } catch (Exception e) {
                    e.printStackTrace();
                }

                dialog.dismiss();
                finish();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    private void showNotAnswerAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(NewQueryViewActivity.this);
        builder.setTitle("Confirmation");
        builder.setMessage("If you do not want to answer this, Query will be released to answer other doctors. \n\n Do you want to release this query?");

        String positiveText = "Release it";
        builder.setPositiveButton(positiveText,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        //---------------------------------------------------
                        String url = Model.BASE_URL + "sapp/releaseQuery?qid=" + current_qid;
                        System.out.println("Not Answering url-------------" + url);
                        new JSON_notanswer().execute(url);
                        //---------------------------------------------------
                    }
                });

        String negativeText = "Cancel";
        builder.setNegativeButton(negativeText,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        AlertDialog dialog = builder.create();
        // display dialog
        dialog.show();
    }

    public void submit_answer() {

        try {

            if ((Model.id) != null && !(Model.id).isEmpty() && !(Model.id).equals("null") && !(Model.id).equals("")) {
                if (current_qid != null && !current_qid.isEmpty() && !current_qid.equals("null") && !current_qid.equals("")) {

                    try {
                        answer_txt = URLEncoder.encode((edt_answer.getText().toString()), "UTF-8");

                        Model.query_typed = "";

                        String diag_text = diagnosis.getText().toString();

                        String pb_cause_text = pb_cause.getText().toString();
                        String lab_t_text = lab_t.getText().toString();
                        String ddx_text = ddx.getText().toString();
                        String pdx_text = pdx.getText().toString();
                        String treatment_plan_text = treatment_plan.getText().toString();
                        String p_tips_text = p_tips.getText().toString();
                        String followup_text = followup.getText().toString();

                        String prescribe_text_text = prescribe_text.getText().toString();

                        //---------------Jio Query Check-----------------------------------
                        if (jio_query.equals("1")) {
                            if (pb_cause_text != null && !pb_cause_text.isEmpty() && !pb_cause_text.equals("null") && !pb_cause_text.equals("")) {
                                if (lab_t_text != null && !lab_t_text.isEmpty() && !lab_t_text.equals("null") && !lab_t_text.equals("")) {
                                    if (ddx_text != null && !ddx_text.isEmpty() && !ddx_text.equals("null") && !ddx_text.equals("")) {
                                        if (pdx_text != null && !pdx_text.isEmpty() && !pdx_text.equals("null") && !pdx_text.equals("")) {
                                            if (treatment_plan_text != null && !treatment_plan_text.isEmpty() && !treatment_plan_text.equals("null") && !treatment_plan_text.equals("")) {
                                                if (p_tips_text != null && !p_tips_text.isEmpty() && !p_tips_text.equals("null") && !p_tips_text.equals("")) {

                                                    send_answer();

                                                } else {
                                                    p_tips.setError("Preventive measures cannot be empty");
                                                    Toast.makeText(getApplicationContext(), "Preventive measures cannot be empty", Toast.LENGTH_SHORT).show();
                                                }
                                            } else {
                                                treatment_plan.setError("Treatment plan cannot be empty");
                                                Toast.makeText(getApplicationContext(), "Treatment plan cannot be empty", Toast.LENGTH_SHORT).show();
                                            }
                                        } else {
                                            pdx.setError("Probable diagnosis cannot be empty");
                                            Toast.makeText(getApplicationContext(), "Probable diagnosis cannot be empty", Toast.LENGTH_SHORT).show();
                                        }
                                    } else {
                                        ddx.setError("Differential diagnosis cannot be empty");
                                        Toast.makeText(getApplicationContext(), "Differential diagnosis cannot be empty", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    lab_t.setError("Investigations cannot be empty");
                                    Toast.makeText(getApplicationContext(), "Investigations cannot be empty", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                pb_cause.setError("Probable causes cannot be empty");
                                Toast.makeText(getApplicationContext(), "Probable causes cannot be empty", Toast.LENGTH_SHORT).show();
                            }
                        } else {

                            send_answer();
                        }

                        //---------------Jio Query Check-----------------------------------


                    } catch (Exception e) {


                        go_back_msg();

                        e.printStackTrace();
                    }

                } else {

                    go_back_msg();
                }
            } else {

                go_back_msg();
            }

        } catch (Exception e) {

            try {
                System.out.println("Exception-----3--" + e.toString());


            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
    }

    public void go_back_msg() {


        //---------------- Dialog------------------------------------------------------------------
        final MaterialDialog alert = new MaterialDialog(NewQueryViewActivity.this);
        alert.setTitle("Oops.!");
        alert.setMessage("Oops.! Something went wrong. Please go back and Try again.");
        alert.setCanceledOnTouchOutside(false);
        alert.setPositiveButton("OK", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.dismiss();
                finish();
            }
        });

                          /*  alert.setNegativeButton("No", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    alert.dismiss();
                                }
                            });*/
        alert.show();
        //-----------------Dialog-----------------------------------------------------------------
    }

    @Override
    public void onResume() {
        super.onResume();

        System.out.println("Resume current_qid---" + current_qid);
        System.out.println("Resume Model.id---" + Model.id);

        try {
            if (current_qid != null && !current_qid.isEmpty() && !current_qid.equals("null") && !current_qid.equals("")) {
                if ((Model.id) != null && !(Model.id).isEmpty() && !(Model.id).equals("null") && !(Model.id).equals("")) {
                    try {

                        String url = Model.BASE_URL + "sapp/canIAnswer?user_id=" + (Model.id) + "&qid=" + current_qid + "&token=" + Model.token;
                        System.out.println("canIAnswer url------" + url);

                        new Resume_canianswer().execute(url);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Resume Error---" + e.toString());
            e.printStackTrace();
        }
    }


    public void query_release_check() {



        //---------------- Dialog------------------------------------------------------------------
        final MaterialDialog alert = new MaterialDialog(NewQueryViewActivity.this);
        alert.setTitle("Oops!");
        alert.setMessage("This query has been released. Please check your dashboard for new query.");
        alert.setCanceledOnTouchOutside(false);
        alert.setPositiveButton("OK", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.dismiss();
                Intent i = new Intent(NewQueryViewActivity.this, NewQueriesActivity.class);
                startActivity(i);
                finish();
            }
        });

                          /*  alert.setNegativeButton("No", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    alert.dismiss();
                                }
                            });*/
        alert.show();
        //-----------------Dialog-----------------------------------------------------------------


    }

    class Resume_canianswer extends AsyncTask<String, Void, Boolean> {

        AlertDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new SpotsDialog(NewQueryViewActivity.this);
            dialog.setCanceledOnTouchOutside(false);
            dialog.setTitle("Checking Query Status., please wait");
            dialog.show();
            dialog.setCancelable(false);

        }

        @Override
        protected Boolean doInBackground(String... urls) {
            try {
                str_response = new JSONParser().getJSONString(urls[0]);
                System.out.println("str_response--------------" + str_response);
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return false;
        }

        protected void onPostExecute(Boolean result) {
            try {

                jsonobj_canisnaswer = new JSONObject(str_response);

                if (jsonobj_canisnaswer.has("token_status")) {
                    String token_status = jsonobj_canisnaswer.getString("token_status");
                    if (token_status.equals("0")) {

                        //============================================================
                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        editor.putString(Login_Status, "0");
                        editor.apply();
                        //============================================================

                        finishAffinity();
                        Intent intent = new Intent(NewQueryViewActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }

                } else {

                    answering_status = jsonobj_canisnaswer.getString("status");
                    opt_freefollow = jsonobj_canisnaswer.getString("opt_freefollow"); // 1 or 0
                    enable_freefollow = jsonobj_canisnaswer.getString("enable_freefollow");  // on or off

                    System.out.println("answering_status---- " + answering_status);
                    System.out.println("opt_freefollow---- " + opt_freefollow);
                    System.out.println("enable_freefollow---- " + enable_freefollow);

                    try {

                        //----------- Flurry -------------------------------------------------
                        Map<String, String> articleParams = new HashMap<String, String>();
                        articleParams.put("android.doc.Qid", current_qid);
                        articleParams.put("android.doc.answering_status", answering_status);
                        articleParams.put("android.doc.check_enable_free_followup", check_enable_ffollowup);
                        FlurryAgent.logEvent("android.doc.Resume_Query_Status", articleParams);
                        //----------- Flurry -------------------------------------------------

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    if ((answering_status).equals("0")) {
                        Toast.makeText(getApplicationContext(), "Sorry.! Another doctor has picked. You are not allowed to answer this query.", Toast.LENGTH_LONG).show();
                        query_release_check();
                    }
                }

                dialog.cancel();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    public void show_guidelines() {

        final MaterialDialog alert = new MaterialDialog(NewQueryViewActivity.this);
        View view = LayoutInflater.from(NewQueryViewActivity.this).inflate(R.layout.answeringgudlines, null);
        alert.setView(view);
        alert.setTitle("Answering Guidelines");
        alert.setCanceledOnTouchOutside(false);

        Toolbar toolBar = (Toolbar) view.findViewById(R.id.toolBar);
        imgapp = (ImageView) view.findViewById(R.id.imgapp);
        final TextView tvguidline = (TextView) view.findViewById(R.id.tvguidline);

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

        final MaterialDialog alert = new MaterialDialog(NewQueryViewActivity.this);
        View view = LayoutInflater.from(NewQueryViewActivity.this).inflate(R.layout.tipstoearning, null);
        alert.setView(view);
        alert.setTitle("Tips for Earning");
        alert.setCanceledOnTouchOutside(false);

        Toolbar toolBar = (Toolbar) view.findViewById(R.id.toolBar);
        imgapp = (ImageView) view.findViewById(R.id.imgapp);
        tvtips1 = (TextView) view.findViewById(R.id.tvtips1);
        tvtips2 = (TextView) view.findViewById(R.id.tvtips2);
        tvtips3 = (TextView) view.findViewById(R.id.tvtips3);

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


    private class JSON_Ad extends AsyncTask<String, Void, Boolean> {

        ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

/*            pd = new ProgressDialog(NewQueryViewActivity.this);
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
                    if (str_status.equals("1")) {
                        ad_layout.setVisibility(View.VISIBLE);
                        String ad_img_path = jsonobj_ad.getString("img");
                        track_id_val = jsonobj_ad.getString("track_id");

                        Picasso.with(NewQueryViewActivity.this).load(ad_img_path).placeholder(R.mipmap.ad_placeholder).error(R.mipmap.ad_placeholder).into(home_ad);
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


    public void showFeedbackDialog() {

        try {
            final MaterialDialog alert = new MaterialDialog(NewQueryViewActivity.this);
            View view = LayoutInflater.from(NewQueryViewActivity.this).inflate(R.layout.ask_feedback, null);
            alert.setView(view);

            alert.setTitle("Reply to feedback");

            final EditText edt_coupon = (EditText) view.findViewById(R.id.edt_coupon);

            alert.setCanceledOnTouchOutside(false);
            alert.setPositiveButton("OK", new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    feedback_text = edt_coupon.getText().toString();

                    if (!(feedback_text.equals(""))) {

                        try {
                            feedback_json = new JSONObject();
                            feedback_json.put("feedback_id", feedback_id_text);
                            feedback_json.put("user_id", (Model.id));
                            feedback_json.put("qid", current_qid);
                            feedback_json.put("reply", feedback_text);

                            System.out.println("feedback_json----------" + feedback_json.toString());

                            new JSON_post_feedback().execute(feedback_json);

                            alert.dismiss();

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        edt_coupon.setError("Feedback cannot be empty");
                    }
                }
            });

            alert.setNegativeButton("CANCEL", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alert.dismiss();
                }
            });
            alert.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class JSON_post_feedback extends AsyncTask<JSONObject, Void, Boolean> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(NewQueryViewActivity.this);
            dialog.setMessage("please wait..");
            dialog.show();
            dialog.setCancelable(false);
        }

        @Override
        protected Boolean doInBackground(JSONObject... urls) {
            try {
                JSONParser jParser = new JSONParser();
                json_response_obj = jParser.JSON_POST(urls[0], "reply_to_feedback");

                System.out.println("Feedback URL---------------" + urls[0]);
                System.out.println("json_response_obj-----------" + json_response_obj.toString());

                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }

            return false;
        }

        protected void onPostExecute(Boolean result) {
            try {

                report_response = json_response_obj.getString("status");
                System.out.println("report_response--------------" + report_response);

                if (report_response.equals("1")) {
                    System.out.println("response-POSTIVE-------------");
                    tv_replytext.setText(feedback_text);
                    doctor_reply_section.setVisibility(View.VISIBLE);
                    btn_feedbacksubmit.setVisibility(View.GONE);
                } else {
                    System.out.println("response-NEGATIVE-------------");
                    //Toast.makeText(getApplicationContext(), report_response, Toast.LENGTH_LONG).show();
                }

                dialog.cancel();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void send_answer() {

        try {
            json = new JSONObject();
            json.put("user_id", (Model.id));
            json.put("qid", current_qid);
            json.put("reply", answer_txt);
            json.put("diagnosis", (diagnosis.getText().toString()));
            json.put("pb_cause", (pb_cause.getText().toString()));
            json.put("lab_t", (lab_t.getText().toString()));
            json.put("ddx", (ddx.getText().toString()));
            json.put("pdx", (pdx.getText().toString()));
            json.put("treatment_plan", (treatment_plan.getText().toString()));
            json.put("followup", (followup.getText().toString()));
            json.put("p_tips", (p_tips.getText().toString()));
            json.put("prescription", (prescribe_text.getText().toString()));

            if (reminder_switch.isChecked()) {
                json.put("follow_remind_days", (edit_remind_days.getText().toString()));
            }

            System.out.println("json------------" + json.toString());

       /*     //------------ Tracker ------------------------
            MyApp.tracker().send(new HitBuilders.EventBuilder()
                    .setCategory("Query_View")
                    .setAction("Answering_Status=" + str_response)
                    .build());
            //------------ Tracker ------------------------
*/

            if (followup_switch.getVisibility() == View.VISIBLE) {
                //------------------------------------------------------
                if (followup_switch.isChecked()) {
                    json.put("enable_freefollow", "on");
                    System.out.println("parameter with-------on");
                } else {
                    json.put("enable_freefollow", "off");
                    System.out.println("parameter with-------off");
                }
                //------------------------------------------------------
            } else {

                json.put("enable_freefollow", enable_freefollow);

                System.out.println("enable_freefollow-------" + enable_freefollow);

 /*
                if ((enable_freefollow).equals("on")) {
                    json.put("enable_freefollow", "on");
                    System.out.println("parameter with-------on");
                    //------------------------------------------------------
                } else {
                    System.out.println("visi-else part------FALSE");
                    json.put("enable_freefollow", "off");
                    System.out.println("parameter with-------off");
                    //------------------------------------------------------
                }*/
            }

            new JSONPostAnswer().execute(json);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void attach_dialog() {
        List<String> mAnimals = new ArrayList<String>();
        mAnimals.add("Take Photo");
        mAnimals.add("Browse Files");

        final CharSequence[] Animals = mAnimals.toArray(new String[mAnimals.size()]);
        android.support.v7.app.AlertDialog.Builder dialogBuilder = new android.support.v7.app.AlertDialog.Builder(this);
        dialogBuilder.setTitle("Attach Files/Images");
        dialogBuilder.setItems(Animals, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                String selectedText = Animals[item].toString();
                System.out.println("selectedText---" + selectedText);

                if (selectedText.equals("Take Photo")) {

                    int permissionCheck = ContextCompat.checkSelfPermission(NewQueryViewActivity.this, Manifest.permission.CAMERA);
                    if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
                        EasyImage.openCamera(NewQueryViewActivity.this, 0);
                    } else {
                        Nammu.askForPermission(NewQueryViewActivity.this, Manifest.permission.CAMERA, new PermissionCallback() {
                            @Override
                            public void permissionGranted() {
                                EasyImage.openCamera(NewQueryViewActivity.this, 0);
                            }

                            @Override
                            public void permissionRefused() {

                            }
                        });
                    }

                } else {
                    //showChooser();

                    int permissionCheck = ContextCompat.checkSelfPermission(NewQueryViewActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
                    if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
                        EasyImage.openDocuments(NewQueryViewActivity.this, 0);
                    } else {
                        Nammu.askForPermission(NewQueryViewActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE, new PermissionCallback() {
                            @Override
                            public void permissionGranted() {
                                EasyImage.openDocuments(NewQueryViewActivity.this, 0);
                            }

                            @Override
                            public void permissionRefused() {

                            }
                        });
                    }

                }
            }
        });
        android.support.v7.app.AlertDialog alertDialogObject = dialogBuilder.create();
        alertDialogObject.show();
    }


    private class AsyncTask_fileupload extends AsyncTask<String, Void, Boolean> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            try {
                dialog = new ProgressDialog(NewQueryViewActivity.this);
                dialog.setMessage("Uploading. Please wait...");
                dialog.show();
                dialog.setCancelable(false);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        protected Boolean doInBackground(String... urls) {

            try {
                upload_response = upload_file(urls[0]);
                System.out.println("upload_response---------" + upload_response);

                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }

            return false;
        }

        protected void onPostExecute(Boolean result) {

            try {
                JSONObject jObj = new JSONObject(upload_response);

                attach_qid = jObj.getString("qid");
                attach_status = jObj.getString("status");
                attach_file_url = jObj.getString("url");
                attach_filename = jObj.getString("filename");
                attach_id = jObj.getString("attach_id");


                //------------ Google firebase Analitics--------------------
                Model.mFirebaseAnalytics = FirebaseAnalytics.getInstance(getApplicationContext());
                Bundle params = new Bundle();
                params.putString("attach_qid", attach_qid);
                params.putString("attach_status", attach_status);
                params.putString("attach_file_url", attach_file_url);
                params.putString("attach_filename", attach_filename);
                params.putString("attach_id", attach_id);
                Model.mFirebaseAnalytics.logEvent("AskQuery2_File_Upload", params);
                //------------ Google firebase Analitics--------------------

                System.out.println("attach_qid-------" + attach_qid);
                System.out.println("attach_status-------" + attach_status);
                System.out.println("attach_file_url-------" + attach_file_url);
                System.out.println("attach_filename-------" + attach_filename);
                System.out.println("attach_attach_id-------" + attach_id);
                System.out.println("last_upload_file--------------" + last_upload_file);

                if (!(last_upload_file).equals("")) {

                    //------------------------------------
                    LayoutInflater layoutInflater = (LayoutInflater) getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    final View addView = layoutInflater.inflate(R.layout.ans_upload_file_list, null);

                    TextView tv_quest = (TextView) addView.findViewById(R.id.tv_quest);
                    ImageView close_button = (ImageView) addView.findViewById(R.id.close_button);
                    thumb_img = (ImageView) addView.findViewById(R.id.imageView4);
                    tv_attach_url = (TextView) addView.findViewById(R.id.tv_attach_url);
                    tv_attach_id = (TextView) addView.findViewById(R.id.tv_attach_id);

                    tv_quest.setText(last_upload_file);
                    tv_attach_id.setText(attach_id);
                    tv_attach_url.setText(attach_file_url);
                    thumb_img.setImageBitmap(BitmapFactory.decodeFile(local_url));

                    System.out.println("Model.upload_files-----------" + (last_upload_file));
                    System.out.println("Model.attach_qid-----------" + (attach_qid));
                    System.out.println("Model.attach_id-----------" + (attach_id));
                    System.out.println("Model.attach_file_url-----------" + (attach_file_url));

                    close_button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            View parent = (View) v.getParent();
                            //View grand_parent = (View)parent.getParent();

                            tv_attach_id = (TextView) parent.findViewById(R.id.tv_attach_id);
                            String attid = tv_attach_id.getText().toString();

                            //---------------------------
                            String url = Model.BASE_URL + "/sapp/removeAnsAttachment?user_id=" + (Model.id) + "&attach_id=" + attid + "&token=" + Model.token;
                            System.out.println("Remover Attach url-------------" + url);
                            new JSON_remove_file().execute(url);
                            //---------------------------

                            System.out.println("Removed attach_id-----------" + attid);
                            ((LinearLayout) addView.getParent()).removeView(addView);
                        }
                    });

                    thumb_img.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //preview_image(local_url);
                        }
                    });

                    file_list.addView(addView);
                    //------------------------------------
                }

                last_upload_file = "";

            } catch (Exception e) {
                e.printStackTrace();
            }

            dialog.cancel();
        }
    }


    public String upload_file(String fullpath) {

        String fpath_filename = fullpath.substring(fullpath.lastIndexOf("/") + 1);

        local_url = fullpath;

        System.out.println("fpath-------" + fullpath);
        System.out.println("fpath_filename---------" + fpath_filename);

        last_upload_file = fpath_filename;

        HttpURLConnection conn = null;
        DataOutputStream dos = null;

        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        int maxBufferSize = 1024 * 1024;
        File sourceFile = new File(fullpath);

        if (!sourceFile.isFile()) {
            System.out.println("Source File not exist :" + fullpath);
            return "";
        } else {

            try {
                upLoadServerUri = Model.BASE_URL + "/sapp/ansUpload?user_id=" + (Model.id) + "&qid=" + (current_qid) + "&token=" + Model.token;
                System.out.println("upLoadServerUri---------------------" + upLoadServerUri);

                FileInputStream fileInputStream = new FileInputStream(fullpath);
                System.out.println("fullpath---------------------------------" + fullpath);
                URL url = new URL(upLoadServerUri);

                conn = (HttpURLConnection) url.openConnection();
                conn.setDoInput(true);
                conn.setDoOutput(true);
                conn.setUseCaches(false);
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Connection", "Keep-Alive");
                conn.setRequestProperty("ENCTYPE", "multipart/form-data");
                conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                conn.setRequestProperty("uploaded_file", fullpath);

                dos = new DataOutputStream(conn.getOutputStream());
                dos.writeBytes(twoHyphens + boundary + lineEnd);
                dos.writeBytes("Content-Disposition: form-data; name=\"file\";filename=\"" + fullpath + "\"" + lineEnd);
                dos.writeBytes(lineEnd);

                bytesAvailable = fileInputStream.available();
                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                buffer = new byte[bufferSize];
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                while (bytesRead > 0) {
                    dos.write(buffer, 0, bufferSize);
                    bytesAvailable = fileInputStream.available();
                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);
                }

                dos.writeBytes(lineEnd);
                dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

                serverResponseCode = conn.getResponseCode();
                serverResponseMessage = conn.getResponseMessage();

                int response = conn.getResponseCode();
                System.out.println("response-------" + response);
                is = conn.getInputStream();
                contentAsString = convertInputStreamToString(is);
                System.out.println("Upload File Response-----------------" + contentAsString);

                fileInputStream.close();
                dos.flush();
                dos.close();

            } catch (MalformedURLException ex) {
                ex.printStackTrace();

                runOnUiThread(new Runnable() {
                    public void run() {
                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
            }

            return contentAsString;
        }
    }

    public String convertInputStreamToString(InputStream stream) throws IOException {

        try {
            BufferedReader r = new BufferedReader(new InputStreamReader(stream, "UTF-8"));
            total = new StringBuilder();
            String line;
            while ((line = r.readLine()) != null) {
                total.append(line).append('\n');
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return total.toString();

    }


    public static void dumpIntent(Intent i) {

        Bundle bundle = i.getExtras();
        if (bundle != null) {
            Set<String> keys = bundle.keySet();
            Iterator<String> it = keys.iterator();
            while (it.hasNext()) {
                String key = it.next();
                System.out.println("Data------>" + "[" + key + "=" + bundle.get(key) + "]");
            }
        }
    }


    public static String intentToString(Intent intent) {
        if (intent == null) {
            return null;
        }

        return intent.toString() + " " + bundleToString(intent.getExtras());
    }

    public static String bundleToString(Bundle bundle) {
        StringBuilder out = new StringBuilder("Bundle[");

        if (bundle == null) {
            out.append("null");
        } else {
            boolean first = true;
            for (String key : bundle.keySet()) {
                if (!first) {
                    out.append(", ");
                }

                out.append(key).append('=');

                Object value = bundle.get(key);

                if (value instanceof int[]) {
                    out.append(Arrays.toString((int[]) value));
                } else if (value instanceof byte[]) {
                    out.append(Arrays.toString((byte[]) value));
                } else if (value instanceof boolean[]) {
                    out.append(Arrays.toString((boolean[]) value));
                } else if (value instanceof short[]) {
                    out.append(Arrays.toString((short[]) value));
                } else if (value instanceof long[]) {
                    out.append(Arrays.toString((long[]) value));
                } else if (value instanceof float[]) {
                    out.append(Arrays.toString((float[]) value));
                } else if (value instanceof double[]) {
                    out.append(Arrays.toString((double[]) value));
                } else if (value instanceof String[]) {
                    out.append(Arrays.toString((String[]) value));
                } else if (value instanceof CharSequence[]) {
                    out.append(Arrays.toString((CharSequence[]) value));
                } else if (value instanceof Parcelable[]) {
                    out.append(Arrays.toString((Parcelable[]) value));
                } else if (value instanceof Bundle) {
                    out.append(bundleToString((Bundle) value));
                } else {
                    out.append(value);
                }

                first = false;
            }
        }

        out.append("]");
        return out.toString();
    }


    public String getPath(Uri uri) {

        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Nammu.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        EasyImage.handleActivityResult(requestCode, resultCode, data, this, new DefaultCallback() {
            @Override
            public void onImagePickerError(Exception e, EasyImage.ImageSource source, int type) {
                e.printStackTrace();
            }

            @Override
            public void onImagesPicked(List<File> imageFiles, EasyImage.ImageSource source, int type) {
                onPhotosReturned(imageFiles);
                System.out.println("Selected file------------" + source.toString());

            }

            @Override
            public void onCanceled(EasyImage.ImageSource source, int type) {
                if (source == EasyImage.ImageSource.CAMERA) {
                    File photoFile = EasyImage.lastlyTakenButCanceledPhoto(NewQueryViewActivity.this);
                    if (photoFile != null) photoFile.delete();
                }
            }
        });
    }

    private void onPhotosReturned(List<File> returnedPhotos) {

        //photos.addAll(returnedPhotos);

        for (int i = 0; i < returnedPhotos.size(); i++) {
            System.out.println(returnedPhotos.get(i));

            System.out.println("File Name------------------" + (returnedPhotos.get(i)).getName());

            selectedPath = (returnedPhotos.get(i).toString());
            selectedfilename = (returnedPhotos.get(i)).getName();

            //----------- Flurry -------------------------------------------------
            Map<String, String> articleParams = new HashMap<String, String>();
            articleParams.put("android.patient.Qid", (attach_qid));
            articleParams.put("android.patient.attach_file_path", selectedPath);
            articleParams.put("android.patient.attach_filename", selectedfilename);
            FlurryAgent.logEvent("android.patient.Attach_Take_Photo", articleParams);
            //----------- Flurry -------------------------------------------------

            //------------ Google firebase Analitics--------------------
            Model.mFirebaseAnalytics = FirebaseAnalytics.getInstance(getApplicationContext());
            Bundle params = new Bundle();
            params.putString("User", Model.id);
            params.putString("Qid", attach_qid);
            params.putString("attach_file_path", selectedPath);
            params.putString("attach_filename", selectedfilename);
            Model.mFirebaseAnalytics.logEvent("Attach_Files", params);
            //------------ Google firebase Analitics--------------------

            new AsyncTask_fileupload().execute(selectedPath);

        }
    }

    @Override
    protected void onDestroy() {
        EasyImage.clearConfiguration(this);
        super.onDestroy();
    }


    private class JSON_remove_file extends AsyncTask<String, Void, Boolean> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            dialog = new ProgressDialog(NewQueryViewActivity.this);
            dialog.setMessage("Please wait..");
            dialog.show();
            dialog.setCancelable(false);
        }

        @Override
        protected Boolean doInBackground(String... urls) {

            try {

                (new JSONParser()).getJSONFromUrl(urls[0]);

                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }

            return false;
        }

        protected void onPostExecute(Boolean result) {

            dialog.dismiss();

        }
    }
}
