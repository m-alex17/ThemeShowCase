package com.alex.themeshowcase;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.alex.themeshowcase.ui.ActivityA;
import com.alex.themeshowcase.ui.ActivityB;
import com.alex.themeshowcase.ui.BaseActivity;
import com.alex.themeshowcase.ui.SettingsActivity;

public class MainActivity extends BaseActivity {
    private Button btnActivityA, btnActivityB, btnSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        setupClickListeners();
    }

    private void initViews() {
        btnActivityA = findViewById(R.id.btnActivityA);
        btnActivityB = findViewById(R.id.btnActivityB);
        btnSettings = findViewById(R.id.btnSettings);
    }

    private void setupClickListeners() {
        btnActivityA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ActivityA.class));
            }
        });

        btnActivityB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ActivityB.class));
            }
        });

        btnSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, SettingsActivity.class));
            }
        });
    }
}