package com.alex.themeshowcase.ui.fragment;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.alex.themeshowcase.util.ThemeManager;

// BaseFragment.java - For fragment support
public abstract class BaseFragment extends Fragment implements ThemeManager.ThemeObserver {
    private ThemeManager themeManager;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        themeManager = ThemeManager.getInstance();
        themeManager.addObserver(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (themeManager != null) {
            themeManager.removeObserver(this);
        }
    }

    @Override
    public void onThemeChanged(ThemeManager.AppTheme theme) {
        if (getActivity() != null) {
            getActivity().recreate();
        }
    }
}
