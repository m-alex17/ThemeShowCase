package com.alex.themeshowcase.ui;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.alex.themeshowcase.util.ThemeManager;

public abstract class BaseActivity extends AppCompatActivity implements ThemeManager.ThemeObserver {
    private ThemeManager themeManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        themeManager = ThemeManager.getInstance();
        themeManager.applyTheme(this);
        super.onCreate(savedInstanceState);
        themeManager.addObserver(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (themeManager != null) {
            themeManager.removeObserver(this);
        }
    }

    @Override
    public void onThemeChanged(ThemeManager.AppTheme theme) {
        recreate();
    }
}
