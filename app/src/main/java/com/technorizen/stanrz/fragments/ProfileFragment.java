package com.technorizen.stanrz.fragments;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.provider.MediaStore;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.technorizen.stanrz.BuildConfig;
import com.technorizen.stanrz.R;
import com.technorizen.stanrz.activites.StoryDetailActivity;
import com.technorizen.stanrz.adapters.IamStanAdapter;
import com.technorizen.stanrz.adapters.StanrzAdapter;
import com.technorizen.stanrz.adapters.StoriesAdapter;
import com.technorizen.stanrz.adapters.UploadsAdapter;

import com.technorizen.stanrz.databinding.FragmentProfileBinding;
import com.technorizen.stanrz.models.Result;
import com.technorizen.stanrz.models.SuccessResGetOtherUsers;
import com.technorizen.stanrz.models.SuccessResGetStanrzOf;
import com.technorizen.stanrz.models.SuccessResGetStories;
import com.technorizen.stanrz.models.SuccessResGetUploadedVideos;
import com.technorizen.stanrz.models.SuccessResGetUploads;
import com.technorizen.stanrz.models.SuccessResProfileData;
import com.technorizen.stanrz.models.SuccessResUpdateIamStanrz;
import com.technorizen.stanrz.models.SuccessResUpdateProfile;
import com.technorizen.stanrz.models.UploadedVideos;
import com.technorizen.stanrz.models.UserPost;
import com.technorizen.stanrz.retrofit.ApiClient;
import com.technorizen.stanrz.retrofit.NetworkAvailablity;
import com.technorizen.stanrz.retrofit.StanrzInterface;
import com.technorizen.stanrz.utility.DataManager;
import com.technorizen.stanrz.utility.SharedPreferenceUtility;
import com.technorizen.stanrz.utility.ShowStory;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;
import static com.technorizen.stanrz.retrofit.Constant.USER_ID;
import static com.technorizen.stanrz.retrofit.Constant.showToast;
import static com.technorizen.stanrz.utility.Util.makeTextViewResizable;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment implements ShowStory {

    FragmentProfileBinding binding;
    private SuccessResProfileData.Result userDetail;
    private ArrayList<SuccessResGetStories.Result> storyList = new ArrayList<>();
    private ArrayList<SuccessResGetStanrzOf.Result> stanrzOfList = new ArrayList<>();
    private ArrayList<SuccessResGetStanrzOf.Result> topStanrzList = new ArrayList<>();
    private UploadsAdapter videoAdapter;
    private String enableOrDisable = "";
    private ArrayList<SuccessResGetUploads.Result> uploadsList = new ArrayList<>();
    private ArrayList<Result> myVideos = new ArrayList<>();
    private ArrayList<SuccessResGetUploadedVideos.Result> uploadedVideos = new ArrayList<>();
    private SuccessResGetUploadedVideos successResGetUploadedVideosItem;
    private StanrzInterface apiInterface;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ProfileFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
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
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_profile, container, false);
        apiInterface = ApiClient.getClient().create(StanrzInterface.class);
        if (NetworkAvailablity.getInstance(getActivity()).checkNetworkStatus()) {
            getProfile();
            getTopStanrz();
            getStanrzOf();
            getStories();
            getUploadedImagesVideos();
        } else {
            Toast.makeText(getActivity(), getResources().getString(R.string.msg_noInternet), Toast.LENGTH_SHORT).show();
        }
        allClicks();
        return binding.getRoot();
    }
     private void allClicks() {
         binding.tvWebsite.setOnClickListener(view ->
                    {
                        if (binding.tvWebsite.getText().toString().startsWith("http://") || binding.tvWebsite.getText().toString().startsWith("https://"))
                        {
                            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(binding.tvWebsite.getText().toString()));
                            startActivity(browserIntent);
                        }
                        else
                        {
                            showToast(getActivity(),getString(R.string.invalid_url));
                        }
                    }
                    );
        binding.tvMore.setOnClickListener(view ->
                {
                    Bundle bundle = new Bundle();
                    bundle.putString("id",userDetail.getId());
                    Navigation.findNavController(view).navigate(R.id.action_navigation_profile_to_seeMoreFragment,bundle);
                }
                );
        binding.tvEnableDisable.setOnClickListener(view ->
                {
                     updateEnableDisable();
                }
                );
        binding.tvJoin.setOnClickListener(v ->
                {
                    Navigation.findNavController(v).navigate(R.id.action_navigation_profile_to_joinVipMembershipFragment);
                }
        );
        binding.btnManage.setOnClickListener(v ->
                {
                    Navigation.findNavController(v).navigate(R.id.action_navigation_profile_to_editVipMembershipFragment);
                }
        );
        binding.btnEdit.setOnClickListener(v ->
                {
                    Navigation.findNavController(v).navigate(R.id.action_navigation_profile_to_editProfileFragment);
                }
        );
        binding.imgSettings.setOnClickListener(v ->
                {
                    Navigation.findNavController(v).navigate(R.id.action_navigation_profile_to_settingsFragment);
                }
        );
        binding.cvUploads.setOnClickListener(v ->
                {
                    getUploadedImagesVideos();
                    binding.rvStories.setVisibility(View.GONE);
                    binding.rvUploads.setVisibility(View.VISIBLE);
                    binding.llVip.setVisibility(View.GONE);
                    binding.tvVip.setBackgroundResource(0);
                    binding.tvVip.setTextColor(getResources().getColor(R.color.black));
                    binding.tvImage.setBackgroundResource(R.drawable.ic_header_bg);
                    binding.tvImage.setTextColor(getResources().getColor(R.color.white));
                    binding.tvVideo.setBackgroundResource(0);
                    binding.tvVideo.setTextColor(getResources().getColor(R.color.black));
                }
        );
        binding.cvVideos.setOnClickListener(v ->
                {
                    getUploadedVideos();
                    binding.rvStories.setVisibility(View.GONE);
                    binding.llVip.setVisibility(View.GONE);
                    binding.tvVip.setBackgroundResource(0);
                    binding.tvVip.setTextColor(getResources().getColor(R.color.black));
                    binding.tvVideo.setBackgroundResource(R.drawable.ic_header_bg);
                    binding.tvVideo.setTextColor(getResources().getColor(R.color.white));
                    binding.tvImage.setBackgroundResource(0);
                    binding.tvImage.setTextColor(getResources().getColor(R.color.black));
                }
        );
        binding.cvVip.setOnClickListener(v ->
                {
                    getFanClubImagesVideos();
                    binding.llVip.setVisibility(View.GONE);
                    binding.rvStories.setVisibility(View.VISIBLE);
                    binding.tvVip.setBackgroundResource(R.drawable.ic_header_bg);
                    binding.tvVip.setTextColor(getResources().getColor(R.color.white));
                    binding.tvImage.setBackgroundResource(0);
                    binding.tvImage.setTextColor(getResources().getColor(R.color.black));
                    binding.tvVideo.setBackgroundResource(0);
                    binding.tvVideo.setTextColor(getResources().getColor(R.color.black));
                }
        );
        binding.ivEditCoverPhoto.setOnClickListener(v ->
                {
                    Bundle bundle = new Bundle();
                    bundle.putString("background",userDetail.getBgImage());
                    bundle.putString("username",userDetail.getUsername());
                    Navigation.findNavController(v).navigate(R.id.action_navigation_profile_to_addCoverPhotoFragment,bundle);
                }
        );
        binding.llFollowers.setOnClickListener(v ->
                {
                    Bundle bundle = new Bundle();
                    bundle.putString("name",userDetail.getUsername());
                    bundle.putString("Goto","1");
                    Navigation.findNavController(v).navigate(R.id.action_navigation_profile_to_folloersAndFollowingsFragment,bundle);
                }
                );
         binding.llFollowing.setOnClickListener(v ->
                 {
                     Bundle bundle = new Bundle();
                     bundle.putString("name",userDetail.getUsername());
                     bundle.putString("Goto","2");
                     Navigation.findNavController(v).navigate(R.id.action_navigation_profile_to_folloersAndFollowingsFragment,bundle);
                 }
         );
     }

    private void getProfile() {
        String userId = SharedPreferenceUtility.getInstance(getContext()).getString(USER_ID);
        DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));
        Map<String,String> map = new HashMap<>();
        map.put("user_id",userId);
        Call<SuccessResProfileData> call = apiInterface.getProfile(map);
        call.enqueue(new Callback<SuccessResProfileData>() {
            @Override
            public void onResponse(Call<SuccessResProfileData> call, Response<SuccessResProfileData> response) {
                DataManager.getInstance().hideProgressMessage();
                try {
                    SuccessResProfileData data = response.body();
                    userDetail = data.getResult();
                    Log.e("data",data.status);
                    if (data.status.equals("1")) {
                        String dataResponse = new Gson().toJson(response.body());
                        Log.e("MapMap", "EDIT PROFILE RESPONSE" + dataResponse);
                        setProfileDetails();
                    } else if (data.status.equals("0")) {
                        showToast(getActivity(), data.message);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(Call<SuccessResProfileData> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }
        });
    }

    private void setProfileDetails()
    {

        Glide
                .with(getActivity())
                .load(userDetail.getImage())
                .centerCrop()
                .placeholder(R.drawable.ic_user)
                .into(binding.ivProfile);
        Glide
                .with(getActivity())
                .load(userDetail.getBgImage())
                .centerCrop()
                .into(binding.ivCoverPhoto);

        binding.tvName.setText(userDetail.getUsername());

        if(userDetail.getVerified().equalsIgnoreCase("Verified"))
        {
            binding.verifiedTick.setVisibility(View.VISIBLE);
        }else
        {
            binding.verifiedTick.setVisibility(View.GONE);
        }
        binding.tvMyTopStanrz.setText(userDetail.getUsername()+" "+getString(R.string.daily_top_10_stanrz));
        binding.tvIamStanrzOf.setText(userDetail.getUsername()+" "+getString(R.string.i_am_a_stanrz_of));
        enableOrDisable = userDetail.getStanrzOf();
        if(userDetail.getOpenFanClub().equalsIgnoreCase("done"))
        {
            binding.cvVip.setVisibility(View.VISIBLE);
        }
        else
        {
            binding.cvVip.setVisibility(View.GONE);
        }
        if(userDetail.getStanrzOf().equalsIgnoreCase("1"))
        {
            binding.tvEnableDisable.setText(getString(R.string.hide_from_profile));
        }
        else
        {
            binding.tvEnableDisable.setText(getString(R.string.show_from_profile));
        }
        binding.tvPosts.setText(userDetail.getTotalPost()+"");
        binding.tvFollower.setText(userDetail.getTotalFollowers()+"");
        binding.tvFollowing.setText(userDetail.getTotalFollowing()+"");
        String bio = userDetail.getBio();
        if(bio.contains("\n"))
        {
            bio = bio.replace("\n","<br>");
        }
        binding.tvBio.setText( userDetail.getBio());
        if(userDetail.getBio().equalsIgnoreCase(""))
        {
            binding.tvBio.setVisibility(View.GONE);
        }
        else
        {
            binding.tvBio.post(new Runnable() {
                @Override
                public void run() {
                    int lineCount = binding.tvBio.getLineCount();
                    if (lineCount>4)
                    {
                        makeTextViewResizable(binding.tvBio, 4, getString(R.string.see_more), true);
                    }
                }
            });
            binding.tvBio.setVisibility(View.VISIBLE);
        }
        binding.tvWebsite.setText(userDetail.getWebsite());
        if(userDetail.getWebsite().equalsIgnoreCase(""))
        {
            binding.tvWebsite.setVisibility(View.GONE);
        }
        else
        {
            binding.tvWebsite.setVisibility(View.VISIBLE);
        }
        binding.tvUser.setText(userDetail.getUsername());
    }

    private void getUploadedImagesVideos() {

        String userId = SharedPreferenceUtility.getInstance(getContext()).getString(USER_ID);
        DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));
        Map<String,String> map = new HashMap<>();
        map.put("user_id",userId);
        Call<SuccessResGetUploadedVideos> call = apiInterface.getUploadedImageVideos(map);
        call.enqueue(new Callback<SuccessResGetUploadedVideos>() {
            @Override
            public void onResponse(Call<SuccessResGetUploadedVideos> call, Response<SuccessResGetUploadedVideos> response) {
                DataManager.getInstance().hideProgressMessage();
                try {
                    SuccessResGetUploadedVideos data = response.body();
                    Log.e("data",data.status);
                    if (data.status.equals("1")) {
                        String dataResponse = new Gson().toJson(response.body());
                        Log.e("MapMap", "EDIT PROFILE RESPONSE" + dataResponse);
                        uploadedVideos.clear();
                        uploadedVideos.addAll(data.getResult());
                        successResGetUploadedVideosItem = data;
                        setVideoList();

                    } else if (data.status.equals("0")) {
                        showToast(getActivity(), data.message);
                        uploadsList.clear();

                        myVideos.clear();

                        videoAdapter = new UploadsAdapter(getActivity(),myVideos,"profile",successResGetUploadedVideosItem);
                        binding.rvUploads.setHasFixedSize(true);
                        binding.rvUploads.setLayoutManager(new GridLayoutManager(getActivity(),3));
                        binding.rvUploads.setAdapter(videoAdapter);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(Call<SuccessResGetUploadedVideos> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }
        });
    }

    private void getUploadedVideos() {
        String userId = SharedPreferenceUtility.getInstance(getContext()).getString(USER_ID);
        DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));
        Map<String,String> map = new HashMap<>();
        map.put("user_id",userId);
        Call<SuccessResGetUploadedVideos> call = apiInterface.getVideos(map);
        call.enqueue(new Callback<SuccessResGetUploadedVideos>() {
            @Override
            public void onResponse(Call<SuccessResGetUploadedVideos> call, Response<SuccessResGetUploadedVideos> response) {
                DataManager.getInstance().hideProgressMessage();
                try {
                    SuccessResGetUploadedVideos data = response.body();
                    uploadedVideos.clear();
                    uploadedVideos.addAll(data.getResult());
                    successResGetUploadedVideosItem = data;
                    setVideoList();
                    Log.e("data",data.status);
                    if (data.status.equals("1")) {
                        String dataResponse = new Gson().toJson(response.body());
                        Log.e("MapMap", "EDIT PROFILE RESPONSE" + dataResponse);
                    } else if (data.status.equals("0")) {
                        showToast(getActivity(), data.message);
                        uploadsList.clear();
                        myVideos.clear();
                        videoAdapter = new UploadsAdapter(getActivity(),myVideos,"profile",successResGetUploadedVideosItem);
                        binding.rvUploads.setHasFixedSize(true);
                        binding.rvUploads.setLayoutManager(new GridLayoutManager(getActivity(),3));
                        binding.rvUploads.setAdapter(videoAdapter);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<SuccessResGetUploadedVideos> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }
        });
    }

    public void setVideoList()
    {
        myVideos.clear();
        Result myVideoModel;
        UserPost myVideo;
        ArrayList<UserPost> userPostArrayList =new ArrayList<>() ;
        for (SuccessResGetUploadedVideos.Result videoModel:uploadedVideos)
        {

            myVideoModel = new Result();
            myVideoModel.setId(videoModel.getId());
            myVideoModel.setUserId(videoModel.getUserId());
            myVideoModel.setDescription(videoModel.getDescription());
            myVideoModel.setComment(videoModel.getComment());
            myVideoModel.setStatus(videoModel.getStatus());
            myVideoModel.setType(videoModel.getType());
            myVideoModel.setDateTime(videoModel.getDateTime());
            myVideoModel.setUserImage(videoModel.getUserImage());
            myVideoModel.setUserName(videoModel.getUserName());
            myVideoModel.setTotalLike(videoModel.getTotalLike());
            myVideoModel.setTotalComment(videoModel.getTotalComment());
            myVideoModel.setLiked(videoModel.getLiked());
            myVideoModel.setTotalSuperLike(Integer.parseInt(videoModel.getTotalSuperLike()));
            myVideoModel.setTimeAgo(videoModel.getTimeAgo());
            myVideoModel.setUnlockWith(videoModel.getUnlockWith());
            myVideoModel.setPostIs(videoModel.getPostIs());
            myVideoModel.setSaved(videoModel.getSaved());
            myVideoModel.setNsfw(videoModel.getNsfw());
            myVideoModel.setTagUsersDetails(videoModel.getTagUsersDetails());
            myVideoModel.setUploadedAs(videoModel.getUploadedAs());

            int i=0;

            if(videoModel.getUserPost() == null)
            {
                break;
            }
            userPostArrayList = new ArrayList<>();
            while (i<videoModel.getUserPost().size())
            {
                myVideo = new UserPost();
                myVideo.setId(videoModel.getUserPost().get(i).getId());
                myVideo.setPostId(videoModel.getUserPost().get(i).getPostId());
                myVideo.setPostData(videoModel.getUserPost().get(i).getPostData());
                myVideo.setPostType(videoModel.getUserPost().get(i).getPostType());
                userPostArrayList.add(myVideo);
                i++;
            }
            myVideoModel.setUserPost(userPostArrayList);
            myVideos.add(myVideoModel);
        }
        videoAdapter = new UploadsAdapter(getActivity(),myVideos,"profile",successResGetUploadedVideosItem);
        binding.rvUploads.setHasFixedSize(true);
        binding.rvUploads.setLayoutManager(new GridLayoutManager(getActivity(),3));
        binding.rvUploads.setAdapter(videoAdapter);
    }

    private void getFanClubImagesVideos() {

        String userId = SharedPreferenceUtility.getInstance(getContext()).getString(USER_ID);
        DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));
        Map<String,String> map = new HashMap<>();
        map.put("user_id",userId);
        Call<SuccessResGetUploadedVideos> call = apiInterface.getFanClubImageVideos(map);
        call.enqueue(new Callback<SuccessResGetUploadedVideos>() {
            @Override
            public void onResponse(@NotNull Call<SuccessResGetUploadedVideos> call, Response<SuccessResGetUploadedVideos> response) {
                DataManager.getInstance().hideProgressMessage();
                try {
                    SuccessResGetUploadedVideos data = response.body();
                    Log.e("data",data.status);
                    if (data.status.equals("1")) {
                        String dataResponse = new Gson().toJson(response.body());
                        Log.e("MapMap", "EDIT PROFILE RESPONSE" + dataResponse);
                        uploadedVideos.clear();
                        uploadedVideos.addAll(data.getResult());
                        successResGetUploadedVideosItem = data;
                        setVideoList();
                    } else if (data.status.equals("0")) {
                        showToast(getActivity(), data.message);
                        uploadsList.clear();
                        myVideos.clear();
                        videoAdapter = new UploadsAdapter(getActivity(),myVideos,"profile",successResGetUploadedVideosItem);
                        binding.rvUploads.setHasFixedSize(true);
                        binding.rvUploads.setLayoutManager(new GridLayoutManager(getActivity(),3));
                        binding.rvUploads.setAdapter(videoAdapter);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(@NotNull Call<SuccessResGetUploadedVideos> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }
        });
    }

    private void getTopStanrz() {

        String userId = SharedPreferenceUtility.getInstance(getContext()).getString(USER_ID);
        DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));
        Map<String,String> map = new HashMap<>();
        map.put("user_id",userId);
        map.put("type","dailytop");
        Call<SuccessResGetStanrzOf> call = apiInterface.getTopStanrz(map);

        call.enqueue(new Callback<SuccessResGetStanrzOf>() {
            @Override
            public void onResponse(Call<SuccessResGetStanrzOf> call, Response<SuccessResGetStanrzOf> response) {
                DataManager.getInstance().hideProgressMessage();
                try {
                    SuccessResGetStanrzOf data = response.body();
                    Log.e("data",data.status);
                    if (data.status.equals("1")) {
                        String dataResponse = new Gson().toJson(response.body());
                        Log.e("MapMap", "EDIT PROFILE RESPONSE" + dataResponse);
                        topStanrzList.clear();
                        topStanrzList.addAll(data.getResult());
                        binding.rvStanrz.setHasFixedSize(true);
                        binding.rvStanrz.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false));
                        binding.rvStanrz.setAdapter(new StanrzAdapter(getActivity(),topStanrzList,"profile"));
                    } else if (data.status.equals("0")) {
                        topStanrzList.clear();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<SuccessResGetStanrzOf> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }
        });
    }

    private void getStanrzOf() {

        String userId = SharedPreferenceUtility.getInstance(getContext()).getString(USER_ID);
        DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));
        Map<String,String> map = new HashMap<>();
        map.put("user_id",userId);
        map.put("type","dailytop");
        Call<SuccessResGetStanrzOf> call = apiInterface.getStanrzOf(map);

        call.enqueue(new Callback<SuccessResGetStanrzOf>() {
            @Override
            public void onResponse(Call<SuccessResGetStanrzOf> call, Response<SuccessResGetStanrzOf> response) {

                DataManager.getInstance().hideProgressMessage();

                try {

                    SuccessResGetStanrzOf data = response.body();
                    Log.e("data",data.status);
                    if (data.status.equals("1")) {
                        String dataResponse = new Gson().toJson(response.body());
                        Log.e("MapMap", "EDIT PROFILE RESPONSE" + dataResponse);

                        stanrzOfList.clear();
                        stanrzOfList.addAll(data.getResult());
                        binding.rvIamStanrz.setHasFixedSize(true);
                        binding.rvIamStanrz.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false));
                        binding.rvIamStanrz.setAdapter(new IamStanAdapter(getActivity(),stanrzOfList,"profile"));

                    } else if (data.status.equals("0")) {
//                        showToast(getActivity(), data.message);
                        stanrzOfList.clear();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<SuccessResGetStanrzOf> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }
        });
    }

    private void updateEnableDisable() {

        String stanrz = "0";

        if(enableOrDisable.equalsIgnoreCase("0"))
        {
            stanrz = "1";
        }
        else
        {
            stanrz = "0";
        }

        String userId = SharedPreferenceUtility.getInstance(getContext()).getString(USER_ID);
        DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));
        Map<String,String> map = new HashMap<>();
        map.put("user_id",userId);
        map.put("stanrz_of",stanrz);

        Call<SuccessResUpdateIamStanrz> call = apiInterface.updateEnableDisable(map);

        call.enqueue(new Callback<SuccessResUpdateIamStanrz>() {
            @Override
            public void onResponse(Call<SuccessResUpdateIamStanrz> call, Response<SuccessResUpdateIamStanrz> response) {

                DataManager.getInstance().hideProgressMessage();

                try {
                    SuccessResUpdateIamStanrz data = response.body();
                    Log.e("data",data.status);
                    if (data.status.equals("1")) {
                        String dataResponse = new Gson().toJson(response.body());
                        Log.e("MapMap", "EDIT PROFILE RESPONSE" + dataResponse);
                        getProfile();

                    } else if (data.status.equals("0")) {
                        showToast(getActivity(), data.message);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<SuccessResUpdateIamStanrz> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }
        });
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
                        storyList.clear();
                        storyList.addAll(data.getResult());
                        binding.rvStories.setHasFixedSize(true);
                        binding.rvStories.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false));
                        binding.rvStories.setAdapter(new StoriesAdapter(getActivity(),storyList,ProfileFragment.this));

                    } else if (data.status.equals("0")) {

                        storyList.clear();
                        binding.rvStories.setHasFixedSize(true);
                        binding.rvStories.setLayoutManager(new LinearLayoutManager(getActivity()));
                        binding.rvStories.setAdapter(new StoriesAdapter(getActivity(),storyList,ProfileFragment.this));
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
    public void showStory(View v, int pos, String userId, String userImage, SuccessResGetStories.Result storyList) {
        String myUserId = SharedPreferenceUtility.getInstance(getContext()).getString(USER_ID);

        if(myUserId.equalsIgnoreCase(userId))
        {
            Navigation.findNavController(v).navigate(R.id.action_navigation_profile_to_myStoriesFragment);
        }
        else
        {
            HomeFragment.story = storyList;
            Intent intent = new Intent(getActivity(), StoryDetailActivity.class);
            Bundle bundle = new Bundle();
            intent.putExtras(bundle);
            startActivity(intent);
        }

    }
}