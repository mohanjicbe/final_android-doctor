package com.orane.docassist;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.flurry.android.FlurryAgent;

import com.orane.docassist.Model.Model;
import com.orane.docassist.Network.Detector;
import com.orane.docassist.Network.JSONParser;
import com.orane.docassist.R;
import com.orane.docassist.attachment_view.GridViewActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


import me.drakeet.materialdialog.MaterialDialog;

public class ConsHistoryView extends AppCompatActivity {

    private Toolbar toolbar;
    TextView tv_filename, tv_ext, cons_tit, tvdate, tvtime, tvpatientgeo, tvpatient, tvcasedets, tvattached, tvconsdate, tvconsmode, tvconsstatus;
    Button btnwrite_notes;
    ImageView consult_mode_img;
    public String file_full_url, attach_file_text, extension, z_host_token_text, z_host_id_text, video_cons_id, cons_view_type, err_text, note_time_text, note_text, url;
    public String edt_notes_text, str_response, call_status, notes_jsonobj_text, pres_jsonobj_text, files_jsonobj_text, filename, file_title, file_ext, appr_id, patient_id, str_cons_type, cons_id, pgeo, patient_name, cons_date, cons_time, cons_type, cons_case_dets, cons_status, presc;
    JSONObject json_resp_notes, json_notes, jsonobj_files, consview_jsonobj, appt_jsonobj;
    View vi_files;
    ImageView file_image;
    LinearLayout files_full_layout, files_layout, notes_layout;
    ScrollView full_layout;
    ProgressBar progressBar;
    TextView tv_notes;

    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs";
    public static final String Login_Status = "Login_Status_key";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (new Detector().isTablet(getApplicationContext())) {
            setContentView(R.layout.consviewdetail);
        } else {
            setContentView(R.layout.consviewdetail);
        }

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        FlurryAgent.onPageView();

        //------------ Object Creations -------------------------------
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle("Consultation Room");
        }

        //------------ Object Creations ------------------------------
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.app_color));
        }

        //---------------------- Object Creation -----------------------
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        full_layout = (ScrollView) findViewById(R.id.full_LinearLayout);
        files_full_layout = (LinearLayout) findViewById(R.id.files_full_layout);
        files_layout = (LinearLayout) findViewById(R.id.files_layout);
        notes_layout = (LinearLayout) findViewById(R.id.notes_layout);

        cons_tit = (TextView) findViewById(R.id.cons_tit);
        tvdate = (TextView) findViewById(R.id.tvdate);
        tvtime = (TextView) findViewById(R.id.tvtime);
        btnwrite_notes = (Button) findViewById(R.id.btnwrite_notes);
        consult_mode_img = (ImageView) findViewById(R.id.consult_mode_img);
        tvpatientgeo = (TextView) findViewById(R.id.tvpatientgeo);
        tvpatient = (TextView) findViewById(R.id.tvpatient);
        tvcasedets = (TextView) findViewById(R.id.tvcasedets);
        tvattached = (TextView) findViewById(R.id.tvattached);
        tvconsdate = (TextView) findViewById(R.id.tvconsdate);
        tvconsmode = (TextView) findViewById(R.id.tvconsmode);
        tvconsstatus = (TextView) findViewById(R.id.tvconsstatus);
        tv_notes = (TextView) findViewById(R.id.tv_notes);
        tv_filename = (TextView) findViewById(R.id.tv_filename);

        files_full_layout.setVisibility(View.GONE);
        notes_layout.setVisibility(View.GONE);

        try {
            Intent intent = getIntent();
            cons_id = intent.getStringExtra("consid");
            cons_view_type = intent.getStringExtra("cons_view_type");
            //----------------------------------------------------------------
            System.out.println("cons_id------------" + cons_id);
            System.out.println("Cons_view_type------------" + cons_view_type);
        } catch (Exception ee) {
            ee.printStackTrace();
        }

        //---------------------- Object Creation -----------------------
        btnwrite_notes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final MaterialDialog alert = new MaterialDialog(ConsHistoryView.this);
                View view = LayoutInflater.from(ConsHistoryView.this).inflate(R.layout.write_notes_pres, null);
                alert.setView(view);
                alert.setTitle("Writing Notes");

                final TextView tv_notes = (TextView) view.findViewById(R.id.edt_notes);
                alert.setCanceledOnTouchOutside(false);
                alert.setPositiveButton("Save", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        edt_notes_text = tv_notes.getText().toString();

                        if (edt_notes_text.equals("")) {
                            tv_notes.setError("Notes cannot be empty..!");
                        } else {

                            try {
                                json_notes = new JSONObject();
                                json_notes.put("user_id", (Model.id));
                                json_notes.put("note_type", "2");
                                json_notes.put("id", cons_id);
                                json_notes.put("note", edt_notes_text);

                                System.out.println("json_notes---" + json_notes.toString());

                                new JSON_Write_Notes().execute(json_notes);

                                //----------- Flurry -------------------------------------------------
                                Map<String, String> articleParams = new HashMap<String, String>();
                                articleParams.put("android.doc.cons_id", cons_id);
                                articleParams.put("android.doc.notes", edt_notes_text);
                                FlurryAgent.logEvent("android.doc.Cosultation_Notes_Entry", articleParams);
                                //----------- Flurry -------------------------------------------------

                            } catch (Exception e2) {
                                e2.printStackTrace();
                            }
                        }

                        alert.dismiss();
                    }
                });

                alert.setNegativeButton("Cancel", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alert.dismiss();
                    }
                });
                alert.show();


            }
        });

        if (cons_id != null && !cons_id.isEmpty() && !cons_id.equals("null") && !cons_id.equals("")) {
            if ((Model.id) != null && !(Model.id).isEmpty() && !(Model.id).equals("null") && !(Model.id).equals("")) {
                //------------------------- Url Pass -------------------------
                url = Model.BASE_URL + "sapp/jsonviewconsult4doc?user_id=" + (Model.id) + "&id=" + cons_id + "&token=" + Model.token;
                System.out.println("Consultation View url------" + url);
                new JSON_ViewCons().execute(url);
                //------------------------- Url Pass -------------------------
            } else {
                Toast.makeText(getApplicationContext(), "Sorry..! Something went wrong. Go back and try again..", Toast.LENGTH_LONG).show();
                ask_logout();
            }

        } else {
            Toast.makeText(getApplicationContext(), "Sorry..! Something went wrong. Go back and try again..", Toast.LENGTH_LONG).show();
            ask_logout();
        }
    }


    public void ask_logout() {

        //---------------- Dialog------------------------------------------------------------------
        final MaterialDialog alert = new MaterialDialog(ConsHistoryView.this);
        alert.setTitle("Oops!");
        alert.setMessage("Something went wrong. You need to logout and login again to proceed.");
        alert.setCanceledOnTouchOutside(false);
        alert.setPositiveButton("OK", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.dismiss();
                //============================================================
                sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putString(Login_Status, "0");
                editor.apply();
                //============================================================
                finishAffinity();
                Intent i = new Intent(ConsHistoryView.this, LoginActivity.class);
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


    public void onClick(View v) {

        try {
            TextView tv_filename = (TextView) v.findViewById(R.id.tv_filename);
            String file_name = tv_filename.getText().toString();
            System.out.println("str_filename-------" + file_name);

            Intent i = new Intent(getApplicationContext(), GridViewActivity.class);
            i.putExtra("filetxt", file_name);
            startActivity(i);
            overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class JSON_ViewCons extends AsyncTask<String, Void, Boolean> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressBar.setVisibility(View.VISIBLE);
            full_layout.setVisibility(View.GONE);
        }

        @Override
        protected Boolean doInBackground(String... urls) {

            try {
                str_response = new JSONParser().getJSONString(urls[0]);
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }

            return false;
        }

        protected void onPostExecute(Boolean result) {

            try {

                System.out.println("str_response--------------" + str_response);
                consview_jsonobj = new JSONObject(str_response);

                if (consview_jsonobj.has("token_status")) {
                    String token_status = consview_jsonobj.getString("token_status");
                    if (token_status.equals("0")) {

                        //============================================================
                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        editor.putString(Login_Status, "0");
                        editor.apply();
                        //============================================================

                        finishAffinity();
                        Intent intent = new Intent(ConsHistoryView.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                } else {

                    appt_jsonobj = consview_jsonobj.getJSONObject("appt");

                    //------------- get Appt Details ------------------------------------------------
                    appr_id = appt_jsonobj.getString("id");
                    patient_id = appt_jsonobj.getString("patient_id");
                    patient_name = appt_jsonobj.getString("patient_name");
                    pgeo = appt_jsonobj.getString("patient_geo");
                    cons_date = appt_jsonobj.getString("consult_date");
                    cons_time = appt_jsonobj.getString("consult_time");
                    cons_type = appt_jsonobj.getString("consult_type");
                    str_cons_type = appt_jsonobj.getString("str_consult_type");
                    cons_case_dets = appt_jsonobj.getString("case");
                    cons_status = appt_jsonobj.getString("str_status");
                    //------------- get Appt Details ------------------------------------------------

                    System.out.println("appr_id--------" + appr_id);

                    tvpatient.setText(patient_name);
                    tvpatientgeo.setText(pgeo);
                    tvdate.setText(cons_date);
                    tvtime.setText(cons_time);
                    tvconsmode.setText(str_cons_type);
                    tvcasedets.setText(Html.fromHtml(cons_case_dets));
                    tvconsstatus.setText(cons_status);

                    //-----------------------------------------------------------------------------------------------
                    if (str_cons_type.equals("Phone Consultation")) {
                        consult_mode_img.setImageResource(R.mipmap.phone_cons_ico_color);
                        cons_tit.setText("Phone Consultation Schedule");
                    } else if (str_cons_type.equals("Video Consultation")) {
                        consult_mode_img.setImageResource(R.mipmap.video_cons_ico_color);
                        cons_tit.setText("Video Consultation Schedule");
                    } else if (str_cons_type.equals("Direct Visit")) {
                        consult_mode_img.setImageResource(R.mipmap.direct_cons_ico);
                        cons_tit.setText("Direct Consultation Schedule");
                    } else {
                        consult_mode_img.setImageResource(R.mipmap.direct_cons_ico);
                        cons_tit.setText("Consultation Schedule");
                    }
                    //-----------------------------------------------------------------------------------------------

                    if (consview_jsonobj.has("files")) {
                        files_jsonobj_text = consview_jsonobj.getString("files");
                    }
                    if (consview_jsonobj.has("notes")) {
                        notes_jsonobj_text = consview_jsonobj.getString("notes");
                    }
                    if (consview_jsonobj.has("prescription")) {
                        pres_jsonobj_text = consview_jsonobj.getString("prescription");
                        if (pres_jsonobj_text.length() > 2) {
                            notes_layout.setVisibility(View.VISIBLE);
                            tv_notes.setText(Html.fromHtml(pres_jsonobj_text));
                        } else {
                            notes_layout.setVisibility(View.GONE);
                        }
                    }
                    //---------------- Files ---------------------------------------

                    files_layout.removeAllViews();

                    if ((files_jsonobj_text.length()) > 2) {

                        files_full_layout.setVisibility(View.VISIBLE);
                        JSONArray jarray_files = consview_jsonobj.getJSONArray("files");

                        System.out.println("jsonobj_items------" + jarray_files.toString());
                        System.out.println("jarray_files.length()------" + jarray_files.length());

                        tvattached.setText("Attached " + jarray_files.length() + " File(s)");

                        attach_file_text = "";

                        for (int j = 0; j < jarray_files.length(); j++) {

                            jsonobj_files = jarray_files.getJSONObject(j);
                            System.out.println("jsonobj_files------" + j + " ----" + jsonobj_files.toString());

                            filename = jsonobj_files.getString("filename");
                            file_title = jsonobj_files.getString("file_title");
                            file_ext = jsonobj_files.getString("ext");
                            file_full_url = jsonobj_files.getString("url");

                            System.out.println("filename--------" + filename);
                            System.out.println("file_title--------" + file_title);
                            System.out.println("file_ext--------" + file_ext);
                            System.out.println("file_full_url--------" + file_full_url);

                       /* vi_files = getLayoutInflater().inflate(R.layout.attached_file_list, null);
                        file_image = (ImageView) vi_files.findViewById(R.id.file_image);
                        tv_filename = (TextView) vi_files.findViewById(R.id.tv_filename);
                        tv_ext = (TextView) vi_files.findViewById(R.id.tv_ext);
                        //tv_userid = (TextView) vi_files.findViewById(R.id.tv_userid);

                        tv_filename.setText(filename + "." + file_ext);
                        tv_ext.setText(file_ext);*/

                            //------------------------ File Attached Text --------------------------------
                            if (attach_file_text.equals("")) {
                                attach_file_text = file_full_url;
                                System.out.println("attach_file_text-------" + attach_file_text);
                            } else {
                                attach_file_text = attach_file_text + "###" + file_full_url;
                                System.out.println("attach_file_text-------" + attach_file_text);
                            }
                            //------------------------ File Attached Text --------------------------------

                            //files_layout.addView(vi_files);
                        }

                        files_layout.setVisibility(View.GONE);
                        //tv_filename.setText(attach_file_text);
                        tv_filename.setText(files_jsonobj_text);
                    } else {
                        files_full_layout.setVisibility(View.GONE);
                    }
                    //-------------------------------------------------------
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            progressBar.setVisibility(View.GONE);
            full_layout.setVisibility(View.VISIBLE);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
            return true;
        }

        if (id == R.id.notify) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (cons_id != null && !cons_id.isEmpty() && !cons_id.equals("null") && !cons_id.equals("")) {
            if ((Model.id) != null && !(Model.id).isEmpty() && !(Model.id).equals("null") && !(Model.id).equals("")) {

                //------------------------- Url Pass -------------------------
                url = Model.BASE_URL + "sapp/jsonviewconsult4doc?user_id=" + (Model.id) + "&id=" + cons_id + "&token=" + Model.token;
                System.out.println("Cond View url------" + url);
                new JSON_ViewCons().execute(url);
                //------------------------- Url Pass -------------------------

            } else {
                Toast.makeText(getApplicationContext(), "Sorry..! Something went wrong. Go back and try again..", Toast.LENGTH_LONG).show();
                ask_logout();
            }

        } else {
            Toast.makeText(getApplicationContext(), "Sorry..! Something went wrong. Go back and try again..", Toast.LENGTH_LONG).show();
            ask_logout();
        }
    }

    private class JSON_Write_Notes extends AsyncTask<JSONObject, Void, Boolean> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            dialog = new ProgressDialog(ConsHistoryView.this);
            dialog.setMessage("Saving Notes. Please wait..");
            //dialog.setTitle("");
            dialog.show();
            dialog.setCancelable(false);
        }

        @Override
        protected Boolean doInBackground(JSONObject... urls) {
            try {
                JSONParser jParser = new JSONParser();
                json_resp_notes = jParser.JSON_POST(urls[0], "writenotes");

                System.out.println("JSON_Write_Notes---------------" + json_resp_notes.toString());

                return true;

            } catch (Exception e) {
                e.printStackTrace();
            }

            dialog.dismiss();

            return false;
        }

        protected void onPostExecute(Boolean result) {

            try {

                if (json_resp_notes.has("token_status")) {
                    String token_status = json_resp_notes.getString("token_status");
                    if (token_status.equals("0")) {

                        //============================================================
                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        editor.putString(Login_Status, "0");
                        editor.apply();
                        //============================================================

                        finishAffinity();
                        Intent intent = new Intent(ConsHistoryView.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }

                } else {

                    String status_txt = json_resp_notes.getString("status");

                    if (status_txt.equals("1")) {
                        say_success();

                        String old_text = tv_notes.getText().toString();

                        System.out.println("old_text--------" + old_text);
                        System.out.println("edt_notes_text--------" + edt_notes_text);

                        tv_notes.setText(Html.fromHtml(old_text + "\n\n" + edt_notes_text));

                        System.out.println(Html.fromHtml("Notes-----------" + old_text + "\n\n" + edt_notes_text));

                    } else {
                        say_failed();
                        //Toast.makeText(getApplicationContext(), "Sending Failed", Toast.LENGTH_SHORT).show();
                    }
                }

                dialog.dismiss();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void say_success() {
        Toast.makeText(getApplicationContext(), "Notes has been saved successfully", Toast.LENGTH_SHORT).show();
    }

    public void say_failed() {
        Toast.makeText(getApplicationContext(), "Notes saved failed", Toast.LENGTH_SHORT).show();
    }
}
