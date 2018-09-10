package com.example.test.h5;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.test.R;

public class AndroidandH5Activity extends AppCompatActivity {
private EditText editText;
    private Button button;
    private WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_androidand_h5);
        findView();
        setListener();
    }

    private void setListener() {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s=editText.getText().toString().trim();
                webView.loadUrl("javascript:call("+"'"+s+"'"+")");

            }
        });
    }

    class JavaInterface{
        @JavascriptInterface
        public void showToast(){
            Toast.makeText(AndroidandH5Activity.this, "js调用了showToast方法", Toast.LENGTH_SHORT).show();
        }

    }

    private void findView() {
        editText= (EditText) findViewById(R.id.username);
        button= (Button) findViewById(R.id.btn);
        webView= (WebView) findViewById(R.id.webview);
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        webView.setWebChromeClient(new WebChromeClient());
        webView.addJavascriptInterface(new JavaInterface(),"android");
        webView.loadUrl("file:///android_asset/a.html");

    }
}
