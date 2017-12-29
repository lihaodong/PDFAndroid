package com.lihaodong.pdf.demo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.lihaodong.pdf.PDFView;
import com.lihaodong.pdf.http.HttpListener;

public class MainActivity extends AppCompatActivity implements HttpListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        PDFView pdfView = findViewById(R.id.pdfView);
        pdfView .fromUrl("http://source.yiboshi.com/80743b51d2811b05da0f20c78909b4a5.pdf","你好.pdf",this);
    }

    @Override
    public void onFailed(Exception e) {

    }

    @Override
    public void onLoading(int progress) {

    }
}
