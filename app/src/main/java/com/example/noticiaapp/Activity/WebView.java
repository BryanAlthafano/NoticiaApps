package com.example.noticiaapp.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import com.example.noticiaapp.R;

public class WebView extends AppCompatActivity {

    ImageView btn_back;
    android.webkit.WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        //Button Back
        btn_back = findViewById(R.id.iv_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WebView.this, Home.class);
                startActivity(intent);
            }
        });

        //WebView
        webView = findViewById(R.id.WebView);
        Intent intent = getIntent();
        String url = intent.getStringExtra("url");
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl(url);
    }

}