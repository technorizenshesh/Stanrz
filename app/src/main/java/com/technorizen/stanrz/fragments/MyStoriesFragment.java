package com.technorizen.stanrz.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.technorizen.stanrz.R;
import com.technorizen.stanrz.activites.StoryDetailActivity;
import com.technorizen.stanrz.adapters.MyStoriesAdapter;
import com.technorizen.stanrz.databinding.FragmentMyStoriesBinding;
import com.technorizen.stanrz.models.SuccessResDeleteStory;
import com.technorizen.stanrz.models.SuccessResGetStories;
import com.technorizen.stanrz.retrofit.ApiClient;
import com.technorizen.stanrz.retrofit.StanrzInterface;
import com.technorizen.stanrz.utility.DataManager;
import com.technorizen.stanrz.utility.DeleteStory;
import com.technorizen.stanrz.utility.SharedPreferenceUtility;
import com.technorizen.stanrz.utility.ShowStory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.technorizen.stanrz.retrofit.Constant.USER_ID;
import static com.technorizen.stanrz.retrofit.Constant.showToast;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MyStoriesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyStoriesFragment extends Fragment implements DeleteStory {

    private ArrayList<SuccessResGetStories.UserStory> storiesList = new ArrayList<>();

    private SuccessResGetStories.Result story;

    FragmentMyStoriesBinding binding;

    private StanrzInterface apiInterface ;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MyStoriesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MyStoriesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MyStoriesFragment newInstance(String param1, String param2) {
        MyStoriesFragment fragment = new MyStoriesFragment();
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

        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_my_stories, container, false);

        apiInterface = ApiClient.getClient().create(StanrzInterface.class);

        binding.header.imgHeader.setOnClickListener(v ->
                {
                    getActivity().onBackPressed();
                }
                );

        binding.tvViewStory.setOnClickListener(v ->
                {

                    HomeFragment.story = story;
                    Intent intent = new Intent(getActivity(), StoryDetailActivity.class);
                    Bundle bundle = new Bundle();
                    intent.putExtras(bundle);
                    startActivity(intent);

                }
                );

        getStories();

        binding.header.tvHeader.setText(R.string.my_stories);

        return binding.getRoot();
    }


    private void getStories() {

        String userId = SharedPreferenceUtility.getInstance(getContext()).getString(USER_ID);
        DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));
        Map<String,String> map = new HashMap<>();
        map.put("user_id",userId);


        Call<SuccessResGetStories> call = apiInterface.getStories(map);

        call.enqueue(new Callback<SuccessResGetStories>() {
            @Override
            public void onResponse(Call<SuccessResGetStories> call, Response<SuccessResGetStories> response) {

                DataManager.getInstance().hideProgressMessage();

                try {
                    SuccessResGetStories data = response.body();

                    Log.e("data",data.status);
                    if (data.status.equals("1")) {
                        String dataResponse = new Gson().toJson(response.body());
                        Log.e("MapMap", "EDIT PROFILE RESPONSE" + dataResponse);

                        storiesList.clear();
                        storiesList.addAll(data.getResult().get(0).getUserStory());

                        story = data.getResult().get(0);

                        binding.rvMyStories.setHasFixedSize(true);
                        binding.rvMyStories.setLayoutManager(new LinearLayoutManager(getActivity()));
                        binding.rvMyStories.setAdapter(new MyStoriesAdapter(getActivity(),storiesList,MyStoriesFragment.this::deleteStory));

                    } else if (data.status.equals("0")) {
                        showToast(getActivity(), data.message);
                        storiesList.clear();

                        binding.rvMyStories.setHasFixedSize(true);
                        binding.rvMyStories.setLayoutManager(new LinearLayoutManager(getActivity()));
                        binding.rvMyStories.setAdapter(new MyStoriesAdapter(getActivity(),storiesList,MyStoriesFragment.this::deleteStory));

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<SuccessResGetStories> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }
        });
    }

    @Override
    public void deleteStory(String id, String storyId) {

        String userId = SharedPreferenceUtility.getInstance(getContext()).getString(USER_ID);

        DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));
        Map<String, String> map = new HashMap<>();
        map.put("user_id",userId);
        map.put("id",id);
        map.put("story_id",storyId);

        Call<SuccessResDeleteStory> call = apiInterface.deleteStory(map);
        call.enqueue(new Callback<SuccessResDeleteStory>() {
            @Override
            public void onResponse(Call<SuccessResDeleteStory> call, Response<SuccessResDeleteStory> response) {

                DataManager.getInstance().hideProgressMessage();

                try {

                    SuccessResDeleteStory data = response.body();
                    if (data.status.equals("1")) {
                        showToast(getActivity(), data.message);

                        getStories();

                    } else {
                        showToast(getActivity(), data.message);

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<SuccessResDeleteStory> call, Throwable t) {

                call.cancel();
                DataManager.getInstance().hideProgressMessage();

            }
        });


    }
}