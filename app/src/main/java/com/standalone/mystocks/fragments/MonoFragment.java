package com.standalone.mystocks.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.standalone.mystocks.interfaces.AdapterUpdateListener;

public abstract class MonoFragment extends Fragment implements AdapterUpdateListener {

    protected final int resource;

    public MonoFragment(@LayoutRes int resource) {
        this.resource = resource;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(resource, container, false);
    }
}
