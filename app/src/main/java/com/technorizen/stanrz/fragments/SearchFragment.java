package com.technorizen.stanrz.fragments;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.technorizen.stanrz.R;
import com.technorizen.stanrz.adapters.SearchAdapter;
import com.technorizen.stanrz.adapters.StoriesAdapter;
import com.technorizen.stanrz.adapters.UploadsAdapter;
import com.technorizen.stanrz.databinding.FragmentSearchBinding;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SearchFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchFragment extends Fragment {

    FragmentSearchBinding binding;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SearchFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SearchFragment.
     */


    // TODO: Rename and change types and number of parameters
    public static SearchFragment newInstance(String param1, String param2) {
        SearchFragment fragment = new SearchFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_search, container, false);

     /*   binding.rvItems.setHasFixedSize(true);
        binding.rvItems.setLayoutManager(new GridLayoutManager(getActivity(),3));
        binding.rvItems.setAdapter(new UploadsAdapter(getActivity()));*/
/*

        binding.rvStories.setHasFixedSize(true);
        binding.rvStories.setLayoutManager(new LinearLayoutManager(getActivity(),
        LinearLayoutManager.HORIZONTAL, false));
        binding.rvStories.setAdapter(new StoriesAdapter(getActivity()));
*/

        binding.etSearch.setInputType(InputType.TYPE_NULL);
        binding.etSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // showMyDialog();

                Navigation.findNavController(v).navigate(R.id.action_navigation_search_to_searchUsersFragment);

            }
        });
        binding.etSearch.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    // showMyDialog();
                    Navigation.findNavController(v).navigate(R.id.action_navigation_search_to_searchUsersFragment);

                }
            }
        });

        return binding.getRoot();
    }
}