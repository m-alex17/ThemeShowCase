package com.alex.themeshowcase.util;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Build;
import android.os.PowerManager;
import android.view.View;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.ContextCompat;

import com.alex.themeshowcase.R;

import java.util.ArrayList;
import java.util.List;

public class ThemeManager {
    private static ThemeManager instance;
    private List<ThemeObserver> observers;
    private AppTheme currentTheme;
    private Context context;

    public enum AppTheme {
        LIGHT(1, "Light"),
        DARK(2, "Dark"),
        DEVICE_DEFAULT(3, "Device Default");

        private final int value;
        private final String displayName;

        AppTheme(int value, String displayName) {
            this.value = value;
            this.displayName = displayName;
        }

        public int getValue() { return value; }
        public String getDisplayName() { return displayName; }

        public static AppTheme fromValue(int value) {
            for (AppTheme theme : values()) {
                if (theme.getValue() == value) return theme;
            }
            return LIGHT;
        }
    }

    public interface ThemeObserver {
        void onThemeChanged(AppTheme theme);
    }

    private ThemeManager() {
        observers = new ArrayList<>();
        currentTheme = AppTheme.LIGHT;
    }

    public static synchronized ThemeManager getInstance() {
        if (instance == null) {
            instance = new ThemeManager();
        }
        return instance;
    }

    public void init(Context context) {
        this.context = context.getApplicationContext();
        loadSavedTheme();
    }

    public void addObserver(ThemeObserver observer) {
        if (!observers.contains(observer)) {
            observers.add(observer);
        }
    }

    public void removeObserver(ThemeObserver observer) {
        observers.remove(observer);
    }

    public void setTheme(AppTheme theme) {
        currentTheme = theme;
        saveTheme(theme);
        applyThemeGlobally(theme);
        notifyObservers(theme);
    }

    public AppTheme getCurrentTheme() {
        return currentTheme;
    }

    private void notifyObservers(AppTheme theme) {
        for (ThemeObserver observer : observers) {
            observer.onThemeChanged(theme);
        }
    }

    private void saveTheme(AppTheme theme) {
        if (context != null) {
            SharedPreferences prefs = context.getSharedPreferences("theme_prefs", Context.MODE_PRIVATE);
            prefs.edit().putInt("selected_theme", theme.getValue()).apply();
        }
    }

    private void loadSavedTheme() {
        if (context != null) {
            SharedPreferences prefs = context.getSharedPreferences("theme_prefs", Context.MODE_PRIVATE);
            int themeValue = prefs.getInt("selected_theme", AppTheme.LIGHT.getValue());
            currentTheme = AppTheme.fromValue(themeValue);
        }
    }

    public void applyTheme(Activity activity) {
        if (activity == null) return;

        switch (currentTheme) {
            case LIGHT:
                activity.setTheme(R.style.AppTheme_Light);
                setStatusBarLight(activity);
                break;
            case DARK:
                activity.setTheme(R.style.AppTheme_Dark);
                setStatusBarDark(activity);
                break;
            case DEVICE_DEFAULT:
                applyDeviceDefaultTheme(activity);
                break;
        }
    }

    private void applyThemeGlobally(AppTheme theme) {
        switch (theme) {
            case LIGHT:
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                break;
            case DARK:
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                break;
            case DEVICE_DEFAULT:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY);
                }
                break;
        }
    }

    private void applyDeviceDefaultTheme(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            // Android 10+ - Follow system theme
            boolean isSystemDark = (context.getResources().getConfiguration().uiMode
                    & Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES;

            if (isSystemDark) {
                activity.setTheme(R.style.AppTheme_Dark);
                setStatusBarDark(activity);
            } else {
                activity.setTheme(R.style.AppTheme_Light);
                setStatusBarLight(activity);
            }
        } else {
            // Android 8-9 - Use battery saver as system default
            PowerManager powerManager = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
            boolean isPowerSaveMode = powerManager != null && powerManager.isPowerSaveMode();

            if (isPowerSaveMode) {
                activity.setTheme(R.style.AppTheme_Dark);
                setStatusBarDark(activity);
            } else {
                activity.setTheme(R.style.AppTheme_Light);
                setStatusBarLight(activity);
            }
        }
    }

    private void setStatusBarLight(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            View decorView = activity.getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            activity.getWindow().setStatusBarColor(ContextCompat.getColor(activity, R.color.light_primary_dark));
        }
    }

    private void setStatusBarDark(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            View decorView = activity.getWindow().getDecorView();
            decorView.setSystemUiVisibility(0);
            activity.getWindow().setStatusBarColor(ContextCompat.getColor(activity, R.color.dark_primary_dark));
        }
    }

    public boolean isDeviceInDarkMode() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            return (context.getResources().getConfiguration().uiMode
                    & Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES;
        } else {
            PowerManager powerManager = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
            return powerManager != null && powerManager.isPowerSaveMode();
        }
    }
}
