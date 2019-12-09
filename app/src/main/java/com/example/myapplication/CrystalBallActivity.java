package com.example.myapplication;

import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;

import java.util.Arrays;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.OnClick;

/**
 * 描述：
 *
 * @author Created by heiduo
 * @time Created on 2019/11/18
 */
public class CrystalBallActivity extends WDActivity {
    @BindView(R.id.webView_policy)
    WebView webView;
    @BindView(R.id.btWebView)
    Button btWeb;

    private String loadUrl = "https://www.wangyinpeng.com";
    private int click = 0;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

        }
    };

    @Override
    protected int getLayoutId() {
        return R.layout.activity_policy;
    }

    @Override
    protected void initView() {
        webView.setVisibility(View.VISIBLE);
        WebSettings webSettings = webView.getSettings();
        webSettings.setUseWideViewPort(true);
        //自适应屏幕
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        //关闭缓存
        webSettings.setAppCacheEnabled(false);
        //设置编码格式
        webSettings.setDefaultTextEncodingName("utf-8");

        webSettings.setAllowFileAccess(true);
//        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        webSettings.setSupportZoom(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setSupportMultipleWindows(true);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setPluginState(WebSettings.PluginState.ON);// 可以使用插件
        webSettings.setGeolocationEnabled(true);
        webView.setBackgroundColor(0);//这点可以防止有白边
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);//不加上，会显示白边

        // 设置可以访问文件
        webSettings.setAllowFileAccess(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setDisplayZoomControls(false);
        webSettings.setAppCacheMaxSize(Long.MAX_VALUE);
        webSettings.setAppCachePath(getDir("appcache", 0).getPath());
        webSettings.setDatabasePath(getDir("databases", 0).getPath());
        webSettings.setGeolocationDatabasePath(getDir("geolocation", 0)
                .getPath());
        webSettings.setPluginState(WebSettings.PluginState.ON_DEMAND);
//        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        String mUserAgent = webSettings.getUserAgentString();
        webSettings.setUserAgentString(mUserAgent + " App/AppName");
        syncCookie();
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
//        webSettings.setDisplayZoomControls(false);
        webSettings.setLoadsImagesAutomatically(true);
        // 开启 DOM storage API 功能
        webSettings.setDomStorageEnabled(true);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            CookieManager.getInstance().setAcceptThirdPartyCookies(webView, true);

//        webView.setScrollBarStyle(WebView.SCROLLBARS_INSIDE_OVERLAY);
        webView.setHorizontalScrollBarEnabled(false);
        webView.setHorizontalFadingEdgeEnabled(false);
        webView.setVerticalFadingEdgeEnabled(false);

        webView.requestFocus();

        /*webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP){
            CookieManager.getInstance().setAcceptThirdPartyCookies(webView,true);
        }*/

        //如果不设置WebViewClient，请求会跳转系统浏览器

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                //该方法在Build.VERSION_CODES.LOLLIPOP以前有效，从Build.VERSION_CODES.LOLLIPOP起，建议使用shouldOverrideUrlLoading(WebView, WebResourceRequest)} instead
                //返回false，意味着请求过程里，不管有多少次的跳转请求（即新的请求地址），均交给webView自己处理，这也是此方法的默认处理
                //返回true，说明你自己想根据url，做新的跳转，比如在判断url符合条件的情况下，我想让webView加载http://ask.csdn.net/questions/178242

//                if (url.toString().contains("sina.cn")){
//                    view.loadUrl("http://ask.csdn.net/questions/178242");
//                    return true;
//                }

                view.loadUrl(url);
                return true;
//                return false;
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                //返回false，意味着请求过程里，不管有多少次的跳转请求（即新的请求地址），均交给webView自己处理，这也是此方法的默认处理
                //返回true，说明你自己想根据url，做新的跳转，比如在判断url符合条件的情况下，我想让webView加载http://ask.csdn.net/questions/178242
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                    if (request.getUrl().toString().contains("sina.cn")){
//                        view.loadUrl("http://ask.csdn.net/questions/178242");
//                        return true;
//                    }else{
//                        view.loadUrl();
//                    }
                    if (null!=request.getUrl().toString()&&!"".equals(request.getUrl().toString())){
                        view.loadUrl(request.getUrl().toString());
                        return true;
                    }
                }
                return false;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);

            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {

                super.onReceivedError(view, request, error);
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                // 不要使用super，否则有些手机访问不了，因为包含了一条 handler.cancel()  
                super.onReceivedSslError(view, handler, error);
                // 接受所有网站的证书，忽略SSL错误，执行访问网页  
                handler.proceed();
            }
        });
        webView.setWebChromeClient(new WebChromeClient() {
            /*@Override
            public boolean onCreateWindow(WebView view, boolean isDialog, boolean isUserGesture, Message resultMsg) {
                WebView newWebView = new WebView(CrystalBallActivity.this);
                view.addView(newWebView);
                WebView.WebViewTransport transport = (WebView.WebViewTransport) resultMsg.obj;
                transport.setWebView(newWebView);
                resultMsg.sendToTarget();
                newWebView.setWebViewClient(new WebViewClient() {
                    @Override
                    public boolean shouldOverrideUrlLoading(WebView view, String url) {
//去掉注释使用系统浏览器打开
//                    Intent browserIntent = new Intent(Intent.ACTION_VIEW);
//                    browserIntent.setData(Uri.parse(url));
//                    startActivity(browserIntent);
                        view.loadUrl(url);
                        return true;
                    }
                });
                return true;
            }*/

            @Override
            public void onProgressChanged(WebView webView, int i) {
                super.onProgressChanged(webView, i);
            }

            @Override
            public boolean onJsConfirm(WebView view, String url, String message, android.webkit.JsResult result) {
                return super.onJsConfirm(view, url, message, result);
            }

            @Override
            public void onShowCustomView(View view, CustomViewCallback callback) {
                super.onShowCustomView(view, callback);
            }

            @Override
            public void onHideCustomView() {
            }

            @Override
            public boolean onShowFileChooser(WebView webView, android.webkit.ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
                return super.onShowFileChooser(webView, filePathCallback, fileChooserParams);
            }

            @Override
            public boolean onJsAlert(WebView view, String url, String message, android.webkit.JsResult result) {
                return super.onJsAlert(view, url, message, result);
            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
            }
        });

        try {
            int type = getIntent().getIntExtra("key_type", 0);
            if (type != 0) {
                loadUrl = getIntent().getStringExtra("url");

                if (!loadUrl.contains("https://") && !loadUrl.contains("http://")) {
                    loadUrl = "https://" + loadUrl;
                }
                webSettings.setSupportMultipleWindows(true);

            } else {
            }

            Logger.d(TAG, "url:" + loadUrl);
            if (null != loadUrl && !"".equals(loadUrl)) {
                webView.post(() -> {
                    webView.loadUrl(loadUrl);
//                    webView.loadData("<html>" + "<iframe src=\"https://blog.csdn.net/weixin_45574883/rss/list\" width=\"100%\" height=\"99%\"></iframe>" + "</html>", "text/html", "utf-8");
                });
//                webView.loadUrl(loadUrl);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        webView.setOnKeyListener((v, keyCode, event) -> false);

    }

    private void syncCookie() {
        CookieSyncManager.createInstance(this);
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);
        CookieSyncManager.getInstance().sync();
    }

    //TODO 前进后退修改

    @OnClick({R.id.webView_policy, R.id.btWebView})
    void onFeedbackClick(View view) {
        switch (view.getId()) {
            case R.id.webView_policy:
                if (webView.canGoForward()) {
                    webView.goForward();
                }
                break;
            case R.id.btWebView:
                switch (click % 4) {
                    case 0:
                        webView.loadUrl(loadUrl);
                        break;
                    case 1:
                        webView.loadUrl("https://support.qq.com/product/82781");
                        break;
                    case 2:
                        webView.loadUrl("https://www.jianshu.com");
                        break;
                    case 3:
                        webView.loadUrl("https://www.aicaring.com");
                        break;
                    default:
                        break;
                }
                click++;

                break;
            default:
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && webView.canGoBack()) {
            webView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
