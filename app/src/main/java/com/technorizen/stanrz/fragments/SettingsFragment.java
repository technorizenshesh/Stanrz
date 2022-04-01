package com.technorizen.stanrz.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.technorizen.stanrz.R;
import com.technorizen.stanrz.activites.LoginActivity;
import com.technorizen.stanrz.databinding.FragmentSettingsBinding;
import com.technorizen.stanrz.retrofit.Constant;
import com.technorizen.stanrz.utility.SharedPreferenceUtility;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SettingsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SettingsFragment extends Fragment {

    FragmentSettingsBinding binding;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SettingsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SettingsFragment.
     */

    // TODO: Rename and change types and number of parameters
    public static SettingsFragment newInstance(String param1, String param2) {
        SettingsFragment fragment = new SettingsFragment();
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

        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_settings, container, false);

        binding.header.imgHeader.setOnClickListener(v ->
                {
                    getActivity().onBackPressed();
                }
                );

        binding.header.tvHeader.setText(R.string.sttings);

        binding.llPassword.setOnClickListener(v ->
                {
                    Navigation.findNavController(v).navigate(R.id.action_settingsFragment_to_changePasswordFragment);
                }
                );

        binding.llSaved.setOnClickListener(v ->
                {
                    Navigation.findNavController(v).navigate(R.id.action_settingsFragment_to_savedImagesAndVideoFragment);
                }
        );

        binding.tvLogout.setOnClickListener(v ->
                {
                    SharedPreferenceUtility.getInstance(getActivity().getApplicationContext()).putBoolean(Constant.IS_USER_LOGGED_IN, false);
                    Intent intent = new Intent(getActivity(),
                    LoginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    getActivity().finish();
                }
                );

        binding.tvMySubscriptions.setOnClickListener(v ->
                {
                    Navigation.findNavController(v).navigate(R.id.action_settingsFragment_to_myPurchasedSubscriptionFragment);
                }
        );

        binding.tvMyFan.setOnClickListener(v ->
                {
                    Navigation.findNavController(v).navigate(R.id.action_settingsFragment_to_myFanSubscriberFragment);
                }
        );

        binding.tvSuggestions.setOnClickListener(v ->
                {
                    Navigation.findNavController(v).navigate(R.id.action_settingsFragment_to_friendsSuggestionFragment);
                }
        );

        binding.tvPP.setOnClickListener(v ->
                {
                    Navigation.findNavController(v).navigate(R.id.action_settingsFragment_to_privacyPolicyFragment);
                }
        );

        binding.tvAbout.setOnClickListener(v ->
                {
                    Navigation.findNavController(v).navigate(R.id.action_settingsFragment_to_aboutFragment);
                }
        );

        binding.llHelp.setOnClickListener(v ->
                {
                    Navigation.findNavController(v).navigate(R.id.action_settingsFragment_to_helpFragment);
                }
        );

        binding.llUserActivity.setOnClickListener(v ->
                {
                    Navigation.findNavController(v).navigate(R.id.action_settingsFragment_to_userActivityFragment);
                }
        );

        binding.tvNotifications.setOnClickListener(v ->
                {
                    Bundle bundle = new Bundle();

                    bundle.putString("key","hai");

                    Navigation.findNavController(v).navigate(R.id.action_settingsFragment_to_navigation_notification,bundle);
                }
                );

        binding.llSecurity.setOnClickListener(v ->
                {
                    Navigation.findNavController(v).navigate(R.id.action_settingsFragment_to_securityFragment);
                }
        );

        return binding.getRoot();
    }
}