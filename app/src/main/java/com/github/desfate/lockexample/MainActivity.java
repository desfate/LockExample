package com.github.desfate.lockexample;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.github.desfate.lockexample.lock.SynchronizedActivity;

/**
 * 这里综合一下几个常用的锁的知识
 */
public class MainActivity extends AppCompatActivity {

    Button start_synchronized;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        start_synchronized = findViewById(R.id.start_synchronized);

        start_synchronized.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, SynchronizedActivity.class));
            }
        });
    }
}