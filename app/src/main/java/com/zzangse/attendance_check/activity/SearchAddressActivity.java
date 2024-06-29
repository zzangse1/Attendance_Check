package com.zzangse.attendance_check.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.zzangse.attendance_check.databinding.ActivitySearchAddressBinding;

public class SearchAddressActivity extends AppCompatActivity {
    private ActivitySearchAddressBinding binding;
    private Handler handler;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initWebView();
        handler = new Handler();
    }

    private void initView() {
        binding = ActivitySearchAddressBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());

    }

    private void initWebView() {
        binding.webviewLayout.getSettings().setJavaScriptEnabled(true);
        binding.webviewLayout.addJavascriptInterface(new BridgeInterface(), "Android");
        binding.webviewLayout.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                binding.webviewLayout.loadUrl("javascript:sample2_execDaumPostcode();");
            }
        });
        binding.webviewLayout.loadUrl("http://zzangse.store/html/index.html");
    }

    private class BridgeInterface {
        @JavascriptInterface
        public void processDATA(String data) {
            // 카카오 주소 검색 API 결과 값을 전달받음. (from javascript)
            Intent intent = new Intent();
            intent.putExtra("data", data);
            setResult(RESULT_OK, intent);
            finish();
        }
    }
}
