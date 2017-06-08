package com.example.myqrcode;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button:
                Intent intent1 = new Intent(this, ScanQRCodeActivity.class); startActivity(intent1); break;
            case R.id.button2:
                Intent intent2 = new Intent(this, BuildQRCodeActivity.class); startActivity(intent2); break;
            default: break;
        }
    }
}
