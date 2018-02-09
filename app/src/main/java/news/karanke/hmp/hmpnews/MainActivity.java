package news.karanke.hmp.hmpnews;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private static final int TIME_DELAY = 2000;
    private static long back_pressed;

    WebView webView;
    ImageView imageView;
    SwipeRefreshLayout swipeRefreshLayout;
    ViewTreeObserver.OnScrollChangedListener mOnScrollChangedListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final String url = "https://hmpnews.in/";
        swipeRefreshLayout = findViewById(R.id.mySwipeControl);
        webView = findViewById(R.id.detailView);
        imageView = findViewById(R.id.imageView);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebChromeClient(new WebChromeClient());
        webView.setWebViewClient(new CustomWebViewClient(){
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }
            //Share link on WhatsApp
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                //Open Article in App
                if(url.contains("hmpnews.in")) {
                    webView.loadUrl(url);
                }
                else {
                    Intent i = new Intent(Intent.ACTION_VIEW,Uri.parse(url));
                    startActivity(i);
                }
                //Share on WhatsApp
                if (url.startsWith("whatsapp://send")) {
                    final Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    // The following flags launch the app outside the current app
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(intent);
                    return true;
                }
                return false;
            }
            @Override
            public void onPageFinished(WebView view, String url) {
                imageView.setVisibility(View.GONE);
                webView.setVisibility(View.VISIBLE);
                swipeRefreshLayout.setRefreshing(false);
            }

    });
        webView.loadUrl(url);
        // Scroll down to reload

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                webView.reload();
            }
        });
    }

    // Exit App on Double back and go back whenever has to go back
    @Override
    public void onBackPressed() {
        if(back_pressed + TIME_DELAY > System.currentTimeMillis()) {
            super.onBackPressed();
        } else {
            Toast.makeText(this, "Press once again to exit", Toast.LENGTH_SHORT).show();
        }
        back_pressed = System.currentTimeMillis();

        if(webView.canGoBack()) {
            webView.goBack();
        }
        else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        swipeRefreshLayout.getViewTreeObserver().removeOnScrollChangedListener(mOnScrollChangedListener);
    }

    @Override
    protected void onStart() {
        super.onStart();
        swipeRefreshLayout.getViewTreeObserver().addOnScrollChangedListener(mOnScrollChangedListener = new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                if(webView.getScrollY() == 0){
                    swipeRefreshLayout.setEnabled(true);

                }
                else {
                    swipeRefreshLayout.setEnabled(false);
                }
            }
        });
    }

}
