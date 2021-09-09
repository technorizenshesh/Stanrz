package com.technorizen.stanrz.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bolaware.viewstimerstory.Momentz;
import com.bolaware.viewstimerstory.MomentzCallback;
import com.technorizen.stanrz.R;

import org.jetbrains.annotations.NotNull;

public class StoryListFragment extends Fragment implements MomentzCallback {

    public StoryListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_story_list, container, false);
    }

    @Override
    public void done() {

    }

    @Override
    public void onNextCalled(@NotNull View view, @NotNull Momentz momentz, int i) {

    }
}