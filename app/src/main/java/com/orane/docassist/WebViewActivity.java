package com.orane.docassist;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;
import com.github.barteksc.pdfviewer.listener.OnPageErrorListener;
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle;

import com.orane.docassist.Model.Model;
import com.orane.docassist.R;
import com.shockwave.pdfium.PdfDocument;

import java.io.File;
import java.util.List;


public class WebViewActivity extends AppCompatActivity implements OnPageChangeListener, OnLoadCompleteListener,
        OnPageErrorListener {

    public String url, type;
    WebView webView;
    ImageView close_button;
    PDFView pdfView;
    private static final String TAG = WebViewActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.terms_consitions);

        final ProgressBar progress = (ProgressBar) findViewById(R.id.progress);

        //------ getting Values ---------------------------
        Intent intent = getIntent();
        url = intent.getStringExtra("url");
        type = intent.getStringExtra("type");
        System.out.println("url-----" + url);
        //------ getting Values ---------------------------

        //------------ Object Creations -------------------------------
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle("");

            TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
            close_button = (ImageView) toolbar.findViewById(R.id.close_button);
            Typeface khandBold = Typeface.createFromAsset(getApplicationContext().getAssets(), Model.font_name_bold);
            mTitle.setTypeface(khandBold);
            mTitle.setText(type);
        }


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.app_color2));
        }
        //------------ Object Creations -------------------------------
        webView = (WebView) findViewById(R.id.webview);
        pdfView = (PDFView) findViewById(R.id.pdfView);


        try {

            if (type.equals("PDF View")) {
                System.out.println("PDF View SECTION-------------");
                pdfView.setVisibility(View.VISIBLE);
                webView.setVisibility(View.GONE);

                Uri uri = Uri.parse(url);
                pdfView.fromUri(uri)
                        .defaultPage(1)
                        .onPageChange(this)
                        .enableAnnotationRendering(true)
                        .onLoad(this)
                        .scrollHandle(new DefaultScrollHandle(this))
                        .spacing(10) // in dp
                        .onPageError(this)
                        .load();

            } else {
                System.out.println("WEB View SECTION-------------");

                pdfView.setVisibility(View.GONE);
                webView.setVisibility(View.VISIBLE);

                webView.getSettings().setJavaScriptEnabled(true);
                webView.getSettings().setBuiltInZoomControls(true);
                webView.setInitialScale(0);
                webView.getSettings().setLoadWithOverviewMode(true);
                webView.getSettings().setUseWideViewPort(true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }



        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(final WebView view, final String url) {
                return false;
            }

            @Override
            public void onPageStarted(final WebView view, final String url, final Bitmap favicon) {
                progress.setVisibility(View.VISIBLE);
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(final WebView view, final String url) {
                progress.setVisibility(View.GONE);
                super.onPageFinished(view, url);
            }
        });

        close_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new AlertDialog.Builder(WebViewActivity.this)
                        //.setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Exit!")
                        .setMessage("Are you sure you want to close?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                try {
                                    if (type.equals("Attachment View") || type.equals("PDF View")) {

                                        String[] split = url.split(":/");
                                        String firstSubString = split[0];
                                        String secondSubString = split[1];

                                        System.out.println("firstSubString----------" + firstSubString);
                                        System.out.println("secondSubString----------" + secondSubString);

                                        File file = new File(secondSubString);
                                        boolean deleted = file.delete();
                                        System.out.println("url----------" + url);
                                        System.out.println("deleted----------" + deleted);
                                    }

                                    finish();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }

                        })
                        .setNegativeButton("No", null)
                        .show();
            }
        });


        webView.loadUrl(url);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == android.R.id.home) {
            if (webView.canGoBack()) {
                webView.goBack();
            } else {
                new AlertDialog.Builder(this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Exit!")
                        .setMessage("Are you sure you want to close?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                try {
                                    if (type.equals("Attachment View") || type.equals("PDF View")) {

                                        String[] split = url.split(":/");
                                        String firstSubString = split[0];
                                        String secondSubString = split[1];

                                        System.out.println("firstSubString----------" + firstSubString);
                                        System.out.println("secondSubString----------" + secondSubString);

                                        File file = new File(secondSubString);
                                        boolean deleted = file.delete();
                                        System.out.println("url----------" + url);
                                        System.out.println("deleted----------" + deleted);
                                    }

                                    finish();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }

                        })
                        .setNegativeButton("No", null)
                        .show();
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            new AlertDialog.Builder(this)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle("Exit!")
                    .setMessage("Are you sure you want to close?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            try {
                                if (type.equals("Attachment View") || type.equals("PDF View")) {

                                    String[] split = url.split(":/");
                                    String firstSubString = split[0];
                                    String secondSubString = split[1];

                                    System.out.println("firstSubString----------" + firstSubString);
                                    System.out.println("secondSubString----------" + secondSubString);

                                    File file = new File(secondSubString);
                                    boolean deleted = file.delete();

                                    System.out.println("url----------" + url);
                                    System.out.println("deleted----------" + deleted);
                                }

                                finish();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                    })
                    .setNegativeButton("No", null)
                    .show();
        }
    }

    public void onResult(int resultCode, Intent intent) {
        if (resultCode == RESULT_OK) {
            // uri = intent.getData();
            //  displayFromUri(uri);
        }
    }

    @Override
    public void onPageChanged(int page, int pageCount) {
        // pageNumber = page;
        // setTitle(String.format("%s %s / %s", pdfFileName, page + 1, pageCount));
    }

    public String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                    System.out.println("result---------" + result);
                }
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
        }
        if (result == null) {
            result = uri.getLastPathSegment();
        }
        return result;
    }

    @Override
    public void loadComplete(int nbPages) {

        PdfDocument.Meta meta = pdfView.getDocumentMeta();
        Log.e(TAG, "title = " + meta.getTitle());
        Log.e(TAG, "author = " + meta.getAuthor());
        Log.e(TAG, "subject = " + meta.getSubject());
        Log.e(TAG, "keywords = " + meta.getKeywords());
        Log.e(TAG, "creator = " + meta.getCreator());
        Log.e(TAG, "producer = " + meta.getProducer());
        Log.e(TAG, "creationDate = " + meta.getCreationDate());
        Log.e(TAG, "modDate = " + meta.getModDate());

        printBookmarksTree(pdfView.getTableOfContents(), "-");

    }

    public void printBookmarksTree(List<PdfDocument.Bookmark> tree, String sep) {
        for (PdfDocument.Bookmark b : tree) {

            // Log.e(TAG, String.format("%s %s, p %d", sep, b.getTitle(), b.getPageIdx()));

            if (b.hasChildren()) {
                printBookmarksTree(b.getChildren(), sep + "-");
            }
        }
    }


    @Override
    public void onPageError(int page, Throwable t) {
    }


}