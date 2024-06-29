package com.zzangse.attendance_check.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.zzangse.attendance_check.databinding.ActivitySearchAddressBinding;

public class SearchAddressActivity extends AppCompatActivity {
    private ActivitySearchAddressBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initWebView();
    }

    private void initView() {
        binding = ActivitySearchAddressBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }

    private void initWebView() {
        binding.webviewLayout.clearCache(true);

        binding.webviewLayout.getSettings().setJavaScriptEnabled(true);
        binding.webviewLayout.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);

        binding.webviewLayout.addJavascriptInterface(new AndroidBridge(), "Android");
        binding.webviewLayout.setWebChromeClient(new WebChromeClient());
        binding.webviewLayout.loadUrl("zzangse.store/html/daum_address.html");
    }

    private class AndroidBridge {
        @JavascriptInterface
        public void processDATA(String data) {
            // 카카오 주소 검색 api 결과
            Intent intent = new Intent();
            intent.putExtra("data", data);
            Log.d("test", data);
            setResult(RESULT_OK, intent);
        }
    }
}
