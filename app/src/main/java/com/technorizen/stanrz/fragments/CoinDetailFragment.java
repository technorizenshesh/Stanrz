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
import com.technorizen.stanrz.databinding.FragmentCoinDetailBinding;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CoinDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CoinDetailFragment extends Fragment {

    FragmentCoinDetailBinding binding;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public CoinDetailFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CoinDetailFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CoinDetailFragment newInstance(String param1, String param2) {
        CoinDetailFragment fragment = new CoinDetailFragment();
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

        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_coin_detail, container, false);

        binding.header.imgHeader.setOnClickListener(v -> getActivity().onBackPressed());
        binding.header.tvHeader.setText(R.string.stanrz_wallet);

        setAllClick();

        binding.rvHistory.setHasFixedSize(true);
        binding.rvHistory.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.rvHistory.setAdapter(new HistoryAdapter(getActivity()));

        return binding.getRoot();
    }

    public void setAllClick()
    {
        binding.cvHistory.setOnClickListener(v ->
                {
                    binding.llHistory.setVisibility(View.VISIBLE);
                    binding.rlExcahngeSuperlikes.setVisibility(View.GONE);
                    binding.rlWithdrawCash.setVisibility(View.GONE);
                    binding.rlWithdrawCoins.setVisibility(View.GONE);
                }
                );

        binding.cvExcahngeCoins.setOnClickListener(v ->
                {
                    binding.llHistory.setVisibility(View.GONE);
                    binding.rlExcahngeSuperlikes.setVisibility(View.VISIBLE);
                    binding.rlWithdrawCash.setVisibility(View.GONE);
                    binding.rlWithdrawCoins.setVisibility(View.GONE);
                }
        );


        binding.cvWithdrawCoins.setOnClickListener(v ->
                {
                    binding.llHistory.setVisibility(View.GONE);
                    binding.rlExcahngeSuperlikes.setVisibility(View.GONE);
                    binding.rlWithdrawCash.setVisibility(View.GONE);
                    binding.rlWithdrawCoins.setVisibility(View.VISIBLE);
                }
        );


        binding.cvWithdrawCash.setOnClickListener(v ->
                {
                    binding.llHistory.setVisibility(View.GONE);
                    binding.rlExcahngeSuperlikes.setVisibility(View.GONE);
                    binding.rlWithdrawCash.setVisibility(View.VISIBLE);
                    binding.rlWithdrawCoins.setVisibility(View.GONE);
                }
        );


    }

}