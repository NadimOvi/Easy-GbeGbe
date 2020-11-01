package com.nadim.gbe_gbe_final.MainActivity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

import com.nadim.gbe_gbe_final.R;

public class PopularListActivity extends AppCompatActivity {

    private ProgressDialog progressDialog;
    private WebView webView;
    private int counter = 0;
    SwipeRefreshLayout refreshLayout;
    private String phoneNumber,password;

    public static final String Login_Post_En_URL = "http://www.gtrbd.net/GbegbeAdmin/OrderProduct/manualorder";
   /* public static final String Login_Post_Bn_URL = "https://www.gbegbe.com/bg";*/


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popular_list);

        phoneNumber = getIntent().getStringExtra("PhoneNumber");
        password = getIntent().getStringExtra("Password");

        webView = (WebView) findViewById(R.id.mainWebView);
        refreshLayout = findViewById(R.id.swipe);
        webView.requestFocus();
        CookieManager.getInstance().setAcceptCookie(true);
        CookieManager.getInstance().setAcceptThirdPartyCookies(webView, true);
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.flush();

        webView.clearCache(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setAppCacheEnabled(true);
        webView.getSettings().setAppCachePath(getApplicationContext().getCacheDir().getPath());
        webView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);


        webView.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);

        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        webView.getSettings().setDomStorageEnabled(true);
        /*webView.getSettings().setBlockNetworkLoads(true);*/
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        webView.getSettings().setUseWideViewPort(true);
        /*   webView.getSettings().setSavePassword(true);*/
        webView.getSettings().setSaveFormData(true);
        webView.getSettings().setEnableSmoothTransition(true);


        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

        });
        webView.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {

                if (progress < 100) {
                    progressDialog.show();
                }
                if (progress == 100) {
                    progressDialog.dismiss();
                }
            }

        });

        ConnectivityManager connectivityManager = (ConnectivityManager)
                getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo == null || !networkInfo.isConnected() || !networkInfo.isAvailable()) {

            Dialog dialog = new Dialog(this);
            dialog.setContentView(R.layout.alert_network_dialog);
            dialog.setCanceledOnTouchOutside(false);
            dialog.getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.WRAP_CONTENT);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            dialog.getWindow().getAttributes().windowAnimations = android.R.style.Animation_Dialog;
            Button btTryAgain = dialog.findViewById(R.id.bt_try_again);

            btTryAgain.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    recreate();
                }
            });
            dialog.show();
        } else {

            //URL gir "....."
            getWebview("http://www.gtrbd.net/GbegbeAdmin/OrderProduct/manualorder?phonenumber="+phoneNumber+"&password="+password);
        }

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshLayout.setRefreshing(true);
                webView.reload();
                refreshLayout.setRefreshing(false);
            }
        });
    }
    /*protected boolean loginURL_BN_Matching(String url) {
        return url.toLowerCase().contains(Login_Post_Bn_URL.toLowerCase());
    }*/
   /* protected boolean loginURL_EN_Matching(String url) {
        return url.toLowerCase().contains(Login_Post_En_URL+"?phonenumber="+phoneNumber+"&password="+password.toLowerCase());
    }*/
    public void getWebview(String myurl) {
        progressDialog = new ProgressDialog(PopularListActivity.this);


        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                progressDialog.show();
                progressDialog.setContentView(R.layout.progress_dialog);
                progressDialog.setCancelable(false);
                progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

                return super.shouldOverrideUrlLoading(view, url);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                progressDialog.dismiss();
                view.getSettings().setJavaScriptEnabled(true);
                webView.getSettings().setAppCachePath(getApplicationContext().getFilesDir().getAbsolutePath() + "/cache");
                webView.getSettings().setDatabasePath(getApplicationContext().getFilesDir().getAbsolutePath() + "/databases");
                webView.setWebViewClient(new WebViewClient() {
                    @Override
                    public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                        super.onReceivedError(view, errorCode, description, failingUrl);
                        view.loadData("<html>SOMETHING WENT WRONG!,Please Check your Internet Connection</html>", "", "");
                    }

                    @Override
                    public boolean shouldOverrideUrlLoading(WebView view, String url) {
                        view.loadUrl(url);
                        return true;
                    }

                    public void onPageFinished(WebView view, String url) {
                        CookieSyncManager.getInstance().sync();
                    }

                });


                CookieSyncManager.createInstance(webView.getContext());
                CookieSyncManager.getInstance().sync();
                super.onPageFinished(view, url);
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
               /* if (loginURL_BN_Matching(url)) {
                    try {
                        *//*postInfo();*//*
                    } catch (Exception e) {
                        Log.e("Fail 2", e.toString());
                        //At the level Exception Class handle the error in Exception Table
                        // Exception Create That Error  Object and throw it
                        //E.g: FileNotFoundException ,etc
                        e.printStackTrace();
                    }
                }else *//*if (loginURL_EN_Matching(url)) {
                    try {
                        *//*postInfo();*//*
                    } catch (Exception e) {
                        Log.e("Fail 2", e.toString());
                        //At the level Exception Class handle the error in Exception Table
                        // Exception Create That Error  Object and throw it
                        //E.g: FileNotFoundException ,etc
                        e.printStackTrace();
                    }
                }*/
                super.onPageStarted(view, url, favicon);
            }
        });


        progressDialog.show();
        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.setCancelable(false);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        webView.loadUrl(myurl);

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

}