package com.technorizen.stanrz.fragments;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.technorizen.stanrz.R;
import com.technorizen.stanrz.adapters.FollowingAdapter;
import com.technorizen.stanrz.adapters.YouAdapter;
import com.technorizen.stanrz.databinding.FragmentNotificationBinding;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NotificationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NotificationFragment extends Fragment {

    FragmentNotificationBinding binding;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public NotificationFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NotificationFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NotificationFragment newInstance(String param1, String param2) {
        NotificationFragment fragment = new NotificationFragment();
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

        binding= DataBindingUtil.inflate(inflater,R.layout.fragment_notification, container, false);


        binding.rvYou.setHasFixedSize(true);
        binding.rvYou.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.rvYou.setAdapter(new YouAdapter(getActivity()));

       /* binding.rvFollowing.setHasFixedSize(true);
        binding.rvFollowing.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.rvFollowing.setAdapter(new FollowingAdapter(getActivity()));
*/

        binding.tabLayoutEventDay.addTab(binding.tabLayoutEventDay.newTab().setText(R.string.following));
        binding.tabLayoutEventDay.addTab(binding.tabLayoutEventDay.newTab().setText(R.string.you));
        binding.tabLayoutEventDay.setTabGravity(TabLayout.GRAVITY_FILL);
        binding.tabLayoutEventDay.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int currentTabSelected= tab.getPosition();
                if(currentTabSelected==0)
                {
                    //Go for Today

                    binding.rvFollowing.setVisibility(View.VISIBLE);
                    binding.rvYou.setVisibility(View.GONE);

                }else if(currentTabSelected==1)
                {
                    //Go for Upcoming
                    binding.rvFollowing.setVisibility(View.GONE);
                    binding.rvYou.setVisibility(View.VISIBLE);

                }
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

    /*    binding.rvFollowing.setHasFixedSize(true);
        binding.rvFollowing.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.rvFollowing.setAdapter(new FollowingAdapter(getActivity()));
*/
        return binding.getRoot();
    }
}