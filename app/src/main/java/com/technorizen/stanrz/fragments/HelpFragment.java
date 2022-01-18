package com.technorizen.stanrz.fragments;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.technorizen.stanrz.R;
import com.technorizen.stanrz.adapters.HelpAdapter;
import com.technorizen.stanrz.databinding.FragmentHelpBinding;
import com.technorizen.stanrz.models.SuccessResGetHelp;
import com.technorizen.stanrz.retrofit.ApiClient;
import com.technorizen.stanrz.retrofit.StanrzInterface;
import com.technorizen.stanrz.utility.DataManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.technorizen.stanrz.retrofit.Constant.showToast;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HelpFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HelpFragment extends Fragment {

    FragmentHelpBinding binding;

    private StanrzInterface apiInterface;

    private ArrayList<SuccessResGetHelp.Result> helpList = new ArrayList<>();

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HelpFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HelpFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HelpFragment newInstance(String param1, String param2) {
        HelpFragment fragment = new HelpFragment();
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
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_help, container, false);

        apiInterface = ApiClient.getClient().create(StanrzInterface.class);

        binding.header.imgHeader.setOnClickListener(view ->
                {
                    getActivity().onBackPressed();
                }
                );
        binding.header.tvHeader.setText(getString(R.string.help));
        getHelp();
        return binding.getRoot();
    }

    public void getHelp()
    {
            DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));

            Map<String, String> map = new HashMap<>();

            Call<SuccessResGetHelp> call = apiInterface.getHelp(map);

            call.enqueue(new Callback<SuccessResGetHelp>() {
                @Override
                public void onResponse(Call<SuccessResGetHelp> call, Response<SuccessResGetHelp> response) {

                    DataManager.getInstance().hideProgressMessage();

                    try {

                        SuccessResGetHelp data = response.body();
                        if (data.status.equals("1")) {

                            helpList.clear();
                            helpList.addAll(data.getResult());

                            binding.rvYou.setHasFixedSize(true);
                            binding.rvYou.setLayoutManager(new LinearLayoutManager(getActivity()));
                            binding.rvYou.setAdapter(new HelpAdapter(getActivity(),helpList));

                        } else {
                            showToast(getActivity(), data.message);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<SuccessResGetHelp> call, Throwable t) {

                    call.cancel();
                    DataManager.getInstance().hideProgressMessage();

                }
            });

        }

}