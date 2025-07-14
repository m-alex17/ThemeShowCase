package com.alex.themeshowcase;

import android.app.Application;

import com.alex.themeshowcase.util.ThemeManager;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        // Initialize ThemeManager
        ThemeManager themeManager = ThemeManager.getInstance();
        themeManager.init(this);

        // Apply saved theme globally
        themeManager.setTheme(themeManager.getCurrentTheme());
    }
}
