package news.karanke.hmp.hmpnews;

import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Created by Dexterr on 04-Feb-18.
 */

class CustomWebViewClient extends WebViewClient {
    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        view.loadUrl(url);
        return true;
    }
}
