package com.standalone.mystocks.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.standalone.mystocks.interfaces.FragmentController;

import java.util.ArrayList;
import java.util.List;

public abstract class MonoFragment<T> extends Fragment implements FragmentController {

    protected final int resource;
    protected List<T> itemList;

    public MonoFragment(@LayoutRes int resId) {
        this.resource = resId;
        this.itemList = new ArrayList<>();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(resource, container, false);
    }
}
