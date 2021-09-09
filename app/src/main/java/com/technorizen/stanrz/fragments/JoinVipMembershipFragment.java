package com.technorizen.stanrz.fragments;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.technorizen.stanrz.R;
import com.technorizen.stanrz.adapters.HistoryAdapter;
import com.technorizen.stanrz.databinding.FragmentJoinVipMembershipBinding;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link JoinVipMembershipFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class JoinVipMembershipFragment extends Fragment {

    FragmentJoinVipMembershipBinding binding;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public JoinVipMembershipFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment JoinVipMembershipFragment.
     */

    // TODO: Rename and change types and number of parameters
    public static JoinVipMembershipFragment newInstance(String param1, String param2) {
        JoinVipMembershipFragment fragment = new JoinVipMembershipFragment();
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
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_join_vip_membership, container, false);

        binding.header.imgHeader.setOnClickListener(v -> getActivity().onBackPressed());
        binding.header.tvHeader.setText(R.string.join);


        return binding.getRoot();    }
}