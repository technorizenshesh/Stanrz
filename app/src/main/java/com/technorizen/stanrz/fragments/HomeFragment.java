package com.technorizen.stanrz.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.technorizen.stanrz.R;
import com.technorizen.stanrz.activites.StoryDetailActivity;
import com.technorizen.stanrz.adapters.PostsAdapter;
import com.technorizen.stanrz.adapters.StoriesAdapter;
import com.technorizen.stanrz.adapters.UploadsAdapter;
import com.technorizen.stanrz.databinding.FragmentHomeBinding;
import com.technorizen.stanrz.models.SuccessResAddLike;
import com.technorizen.stanrz.models.SuccessResGetStories;
import com.technorizen.stanrz.models.SuccessResGetUploads;
import com.technorizen.stanrz.retrofit.ApiClient;
import com.technorizen.stanrz.retrofit.StanrzInterface;
import com.technorizen.stanrz.utility.AddLike;
import com.technorizen.stanrz.utility.DataManager;
import com.technorizen.stanrz.utility.SharedPreferenceUtility;
import com.technorizen.stanrz.utility.ShowStory;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.technorizen.stanrz.retrofit.Constant.USER_ID;
import static com.technorizen.stanrz.retrofit.Constant.showToast;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment implements AddLike, ShowStory {

    FragmentHomeBinding binding;

    private ArrayList<String> arrayList = new ArrayList<>();
    private ArrayList<SuccessResGetUploads.Result> uploadsList = new ArrayList<>();
    private ArrayList<SuccessResGetStories.Result> storyList = new ArrayList<>();

    private StanrzInterface apiInterface;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */

    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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

        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_home, container, false);

        apiInterface = ApiClient.getClient().create(StanrzInterface.class);

        arrayList.add("https://myspotbh.com/Easy_shopping/uploads/images/download.png");
        arrayList.add("https://myspotbh.com/Easy_shopping/uploads/images/CATEGORY_IMG79985.png");


    /*    binding.rvPosts.setHasFixedSize(true);
        binding.rvPosts.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.rvPosts.setAdapter(new PostsAdapter(getActivity(),arrayList));
*/

        binding.llCoinDetail.setOnClickListener(v ->
                {
                    Navigation.findNavController(v).navigate(R.id.action_navigation_home_to_coinDetailFragment);

                }
                );


        binding.ivChat.setOnClickListener(v ->
                {
                    Navigation.findNavController(v).navigate(R.id.action_navigation_home_to_chatFragment);

                }
        );

        getUploadedImagesVideos();

        return binding.getRoot();
    }



    private void getUploadedImagesVideos() {

        String userId = SharedPreferenceUtility.getInstance(getContext()).getString(USER_ID);
        DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));
        Map<String,String> map = new HashMap<>();
        map.put("user_id",userId);

      /*  RequestBody email = RequestBody.create(MediaType.parse("text/plain"),strEmail);
        RequestBody password = RequestBody.create(MediaType.parse("text/plain"), strPassword);
        RequestBody registerID = RequestBody.create(MediaType.parse("text/plain"),deviceToken);
*/
//        Call<SuccessResSignIn> call = apiInterface.login(email,password,registerID);
        Call<SuccessResGetUploads> call = apiInterface.getUploadedImageVideos(map);

        call.enqueue(new Callback<SuccessResGetUploads>() {
            @Override
            public void onResponse(Call<SuccessResGetUploads> call, Response<SuccessResGetUploads> response) {

                DataManager.getInstance().hideProgressMessage();

                try {
                    SuccessResGetUploads data = response.body();

                    uploadsList.clear();
                    uploadsList.addAll(data.getResult());
                    binding.rvPosts.setHasFixedSize(true);
                    binding.rvPosts.setLayoutManager(new LinearLayoutManager(getActivity()));
                    binding.rvPosts.setAdapter(new PostsAdapter(getActivity(),uploadsList,HomeFragment.this));

//                    setSellerData();
                    Log.e("data",data.status);
                    if (data.status.equals("1")) {
                        String dataResponse = new Gson().toJson(response.body());
                        Log.e("MapMap", "EDIT PROFILE RESPONSE" + dataResponse);

//                        SessionManager.writeString(RegisterAct.this, Constant.driver_id,data.result.id);
//                        App.showToast(RegisterAct.this, data.message, Toast.LENGTH_SHORT);
                    } else if (data.status.equals("0")) {
                        showToast(getActivity(), data.message);
                        uploadsList.clear();
                        binding.rvPosts.setHasFixedSize(true);
                        binding.rvPosts.setLayoutManager(new LinearLayoutManager(getActivity()));
                        binding.rvPosts.setAdapter(new PostsAdapter(getActivity(),uploadsList,HomeFragment.this));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<SuccessResGetUploads> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }
        });
    }

    @Override
    public void addLike(String post_id, String uploader_id) {
        String userId = SharedPreferenceUtility.getInstance(getContext()).getString(USER_ID);
        DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));
        Map<String,String> map = new HashMap<>();
        map.put("user_id",userId);
        map.put("post_id",post_id);
        map.put("post_user",uploader_id);

      /*  RequestBody email = RequestBody.create(MediaType.parse("text/plain"),strEmail);
        RequestBody password = RequestBody.create(MediaType.parse("text/plain"), strPassword);
        RequestBody registerID = RequestBody.create(MediaType.parse("text/plain"),deviceToken);
*/
//        Call<SuccessResSignIn> call = apiInterface.login(email,password,registerID);
        Call<SuccessResAddLike> call = apiInterface.addLike(map);

        call.enqueue(new Callback<SuccessResAddLike>() {
            @Override
            public void onResponse(Call<SuccessResAddLike> call, Response<SuccessResAddLike> response) {

                DataManager.getInstance().hideProgressMessage();
                try {
                    SuccessResAddLike data = response.body();
//                    setSellerData();
                    Log.e("data",data.status);
                    showToast(getActivity(), data.result);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<SuccessResAddLike> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }
        });
    }

    @Override
    public void comment(View v, String postID) {
        Bundle bundle = new Bundle();
        bundle.putString("postID",postID);
        Navigation.findNavController(v).navigate(R.id.action_navigation_home_to_addCommentFragment,bundle);

    }



    private void getStories() {

        String userId = SharedPreferenceUtility.getInstance(getContext()).getString(USER_ID);
        DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));
        Map<String,String> map = new HashMap<>();
        map.put("user_id","5");

      /*  RequestBody email = RequestBody.create(MediaType.parse("text/plain"),strEmail);
        RequestBody password = RequestBody.create(MediaType.parse("text/plain"), strPassword);
        RequestBody registerID = RequestBody.create(MediaType.parse("text/plain"),deviceToken);
*/
//        Call<SuccessResSignIn> call = apiInterface.login(email,password,registerID);
        Call<SuccessResGetStories> call = apiInterface.getStories(map);

        call.enqueue(new Callback<SuccessResGetStories>() {
            @Override
            public void onResponse(Call<SuccessResGetStories> call, Response<SuccessResGetStories> response) {

                DataManager.getInstance().hideProgressMessage();

                try {
                    SuccessResGetStories data = response.body();

                    storyList.clear();
                    storyList.addAll(data.getResult());
                    binding.rvStories.setHasFixedSize(true);
                    binding.rvStories.setLayoutManager(new LinearLayoutManager(getActivity()));
                    binding.rvStories.setAdapter(new StoriesAdapter(getActivity(),storyList,HomeFragment.this));

//                    setSellerData();
                    Log.e("data",data.status);
                    if (data.status.equals("1")) {
                        String dataResponse = new Gson().toJson(response.body());
                        Log.e("MapMap", "EDIT PROFILE RESPONSE" + dataResponse);

//                        SessionManager.writeString(RegisterAct.this, Constant.driver_id,data.result.id);
//                        App.showToast(RegisterAct.this, data.message, Toast.LENGTH_SHORT);
                    } else if (data.status.equals("0")) {
                        showToast(getActivity(), data.message);
                        storyList.clear();
                        binding.rvStories.setHasFixedSize(true);
                        binding.rvStories.setLayoutManager(new LinearLayoutManager(getActivity()));
                        binding.rvStories.setAdapter(new StoriesAdapter(getActivity(),storyList,HomeFragment.this));
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
    public void showStory(int pos,String userName, String userImage, List<SuccessResGetStories.UserStory> storyList) {

        Intent intent = new Intent(getActivity(), StoryDetailActivity.class);

        Bundle bundle = new Bundle();
        bundle.putSerializable("key", (Serializable) storyList);
        intent.putExtras(bundle);

    }
}