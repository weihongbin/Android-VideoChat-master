package com.nercms;

import android.app.Activity;
import android.content.Intent;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.nercms.ryong21.AudioTest;
import com.nercms.ryong21.VideoChatActivityDemo;

public class MainActivity extends Activity {
    EditText editText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editText = (EditText) findViewById(R.id.editText);
    }

    public void doStart(View v) {
        String ip = editText.getText().toString();
        Intent intent = new Intent(this, AudioTest.class);
        intent.putExtra("remote_ip", ip);
        intent.putExtra("remote_port", 19888);
        startActivity(intent);

    }


}
