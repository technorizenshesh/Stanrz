package com.technorizen.stanrz.fragments;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.Gson;
import com.technorizen.stanrz.R;
import com.technorizen.stanrz.adapters.UploadsAdapter;
import com.technorizen.stanrz.databinding.FragmentSavedImagesAndVideoBinding;
import com.technorizen.stanrz.models.Result;
import com.technorizen.stanrz.models.SuccessResGetOtherUsers;
import com.technorizen.stanrz.models.SuccessResGetUploadedVideos;
import com.technorizen.stanrz.models.SuccessResGetUploads;
import com.technorizen.stanrz.models.UserPost;
import com.technorizen.stanrz.retrofit.ApiClient;
import com.technorizen.stanrz.retrofit.NetworkAvailablity;
import com.technorizen.stanrz.retrofit.StanrzInterface;
import com.technorizen.stanrz.utility.DataManager;
import com.technorizen.stanrz.utility.SharedPreferenceUtility;

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
 * Use the {@link SavedImagesAndVideoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SavedImagesAndVideoFragment extends Fragment {

    FragmentSavedImagesAndVideoBinding binding;

    private StanrzInterface apiInterface;

    private UploadsAdapter videoAdapter;

    private SuccessResGetOtherUsers.Result otherUsersDetail;

    private SuccessResGetUploadedVideos successResGetUploadedVideosItem;

    private ArrayList<SuccessResGetUploads.Result> uploadsList = new ArrayList<>();

    private ArrayList<Result> myVideos = new ArrayList<>();

    private ArrayList<SuccessResGetUploadedVideos.Result> uploadedVideos = new ArrayList<>();

    private static final String ARG_PARAM1 = "param1";

    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SavedImagesAndVideoFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static SavedImagesAndVideoFragment newInstance(String param1, String param2) {
        SavedImagesAndVideoFragment fragment = new SavedImagesAndVideoFragment();
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
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_saved_images_and_video, container, false);
        apiInterface = ApiClient.getClient().create(StanrzInterface.class);
        binding.header.imgHeader.setOnClickListener(v -> getActivity().onBackPressed());
        binding.header.tvHeader.setText(R.string.saved_images_videos);
        if (NetworkAvailablity.getInstance(getActivity()).checkNetworkStatus()) {
            getSavedImagesVideos();
        } else {
            Toast.makeText(getActivity(), getResources().getString(R.string.msg_noInternet), Toast.LENGTH_SHORT).show();
        }
        return binding.getRoot();
    }

    private void getSavedImagesVideos() {

        String userId = SharedPreferenceUtility.getInstance(getContext()).getString(USER_ID);
        DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));
        Map<String,String> map = new HashMap<>();
        map.put("user_id",userId);

        Call<SuccessResGetUploadedVideos> call = apiInterface.getSavedImageVideos(map);

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
                        successResGetUploadedVideosItem = data;
                        uploadedVideos.clear();
                        uploadedVideos.addAll(data.getResult());
                        setVideoList();
                    } else if (data.status.equals("0")) {
                        showToast(getActivity(), data.message);
                        uploadsList.clear();
                        myVideos.clear();
                        videoAdapter = new UploadsAdapter(getActivity(),myVideos,"saved",successResGetUploadedVideosItem);
                        binding.rvPosts.setHasFixedSize(true);
                        binding.rvPosts.setLayoutManager(new GridLayoutManager(getActivity(),3));
                        binding.rvPosts.setAdapter(videoAdapter);
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

        videoAdapter = new UploadsAdapter(getActivity(),myVideos,"saved",successResGetUploadedVideosItem);

        binding.rvPosts.setHasFixedSize(true);
        binding.rvPosts.setLayoutManager(new GridLayoutManager(getActivity(),3));
        binding.rvPosts.setAdapter(videoAdapter);

    }

}