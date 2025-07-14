package com.alex.themeshowcase.ui;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import com.alex.themeshowcase.R;
import com.alex.themeshowcase.util.ThemeManager;

public class SettingsActivity extends BaseActivity {
    private Toolbar toolbar;
    private RadioGroup themeRadioGroup;
    private RadioButton radioLight, radioDark, radioDeviceDefault;
    private TextView tvCurrentSystemTheme;
    private ThemeManager themeManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        themeManager = ThemeManager.getInstance();
        initViews();
//        setupToolbar();
        setupThemeSettings();
        updateUI();
    }

    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        themeRadioGroup = findViewById(R.id.themeRadioGroup);
        radioLight = findViewById(R.id.radioLight);
        radioDark = findViewById(R.id.radioDark);
        radioDeviceDefault = findViewById(R.id.radioDeviceDefault);
        tvCurrentSystemTheme = findViewById(R.id.tvCurrentSystemTheme);
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Settings");
        }
    }

    private void setupThemeSettings() {
        // Set current selection
        ThemeManager.AppTheme currentTheme = themeManager.getCurrentTheme();
        switch (currentTheme) {
            case LIGHT:
                radioLight.setChecked(true);
                break;
            case DARK:
                radioDark.setChecked(true);
                break;
            case DEVICE_DEFAULT:
                radioDeviceDefault.setChecked(true);
                break;
        }

        // Set click listener
        themeRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                ThemeManager.AppTheme selectedTheme;

                if (checkedId == R.id.radioLight) {
                    selectedTheme = ThemeManager.AppTheme.LIGHT;
                } else if (checkedId == R.id.radioDark) {
                    selectedTheme = ThemeManager.AppTheme.DARK;
                } else {
                    selectedTheme = ThemeManager.AppTheme.DEVICE_DEFAULT;
                }

                themeManager.setTheme(selectedTheme);
                updateUI();
            }
        });
    }

    private void updateUI() {
        // Update device default status
        boolean isDeviceInDarkMode = themeManager.isDeviceInDarkMode();
        String systemThemeText = "System is currently in " +
                (isDeviceInDarkMode ? "Dark" : "Light") + " mode";
        tvCurrentSystemTheme.setText(systemThemeText);

        // Show/hide system theme info based on selection
        boolean isDeviceDefaultSelected = themeManager.getCurrentTheme() == ThemeManager.AppTheme.DEVICE_DEFAULT;
        tvCurrentSystemTheme.setVisibility(isDeviceDefaultSelected ? View.VISIBLE : View.GONE);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
