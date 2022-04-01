package com.technorizen.stanrz.fragments;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatButton;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.technorizen.stanrz.R;
import com.technorizen.stanrz.activites.StoryDetailActivity;
import com.technorizen.stanrz.adapters.IamStanAdapter;
import com.technorizen.stanrz.adapters.StanrzAdapter;
import com.technorizen.stanrz.adapters.StoriesAdapter;
import com.technorizen.stanrz.adapters.UploadsAdapter;
import com.technorizen.stanrz.databinding.FragmentOtherUserDetailBinding;
import com.technorizen.stanrz.models.Result;
import com.technorizen.stanrz.models.SuccessResAddFollowing;
import com.technorizen.stanrz.models.SuccessResAddLike;
import com.technorizen.stanrz.models.SuccessResAddSubscription;
import com.technorizen.stanrz.models.SuccessResGetOtherUsers;
import com.technorizen.stanrz.models.SuccessResGetStanrzOf;
import com.technorizen.stanrz.models.SuccessResGetStories;
import com.technorizen.stanrz.models.SuccessResGetUploadedVideos;
import com.technorizen.stanrz.models.SuccessResGetUploads;
import com.technorizen.stanrz.models.SuccessResReportUser;
import com.technorizen.stanrz.models.UserPost;
import com.technorizen.stanrz.retrofit.ApiClient;
import com.technorizen.stanrz.retrofit.NetworkAvailablity;
import com.technorizen.stanrz.retrofit.StanrzInterface;
import com.technorizen.stanrz.utility.BottomSheet;
import com.technorizen.stanrz.utility.DataManager;
import com.technorizen.stanrz.utility.ReportInterface;
import com.technorizen.stanrz.utility.SharedPreferenceUtility;
import com.technorizen.stanrz.utility.ShowStory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.technorizen.stanrz.retrofit.Constant.USER_ID;
import static com.technorizen.stanrz.retrofit.Constant.showToast;
import static com.technorizen.stanrz.utility.Util.makeTextViewResizable;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NewOtherUserDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewOtherUserDetailFragment extends Fragment implements ShowStory {

    FragmentOtherUserDetailBinding binding;
    private SuccessResGetOtherUsers.Result otherUsersDetail;
    private ArrayList<SuccessResGetUploads.Result> uploadsList = new ArrayList<>();
    private ArrayList<SuccessResGetStories.Result> storyList = new ArrayList<>();

    private ArrayList<SuccessResGetStanrzOf.Result> stanrzOfList = new ArrayList<>();
    private ArrayList<SuccessResGetStanrzOf.Result> topStanrzList = new ArrayList<>();

    private SuccessResGetUploadedVideos successResGetUploadedVideosItem;
    private SuccessResAddSubscription.Result description;
    private UploadsAdapter videoAdapter;
    private String enableDisable;
    private  BottomSheet bottomSheetFragment;
    private StanrzInterface apiInterface;
    private String otherUserId = "";
    private ArrayList<Result> myVideos = new ArrayList<>();
    private boolean subscribed = false;
    private ArrayList<SuccessResGetUploadedVideos.Result> uploadedVideos = new ArrayList<>();
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    public NewOtherUserDetailFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment OtherUserDetailFragment.
     */

    // TODO: Rename and change types and number of parameters
    public static NewOtherUserDetailFragment newInstance(String param1, String param2) {
        NewOtherUserDetailFragment fragment = new NewOtherUserDetailFragment();
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
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_other_user_detail, container, false);
        apiInterface = ApiClient.getClient().create(StanrzInterface.class);
        Bundle bundle = this.getArguments();
        if (bundle!=null)
        {
            otherUserId = bundle.getString("otherUser");
            if (NetworkAvailablity.getInstance(getActivity()).checkNetworkStatus()) {
                getOtherUserProfile(otherUserId);
                getUploadedImagesVideos(otherUserId);
                getStanrzOf();
                getDescription();
                getTopStanrz();
            } else {
                Toast.makeText(getActivity(), getResources().getString(R.string.msg_noInternet), Toast.LENGTH_SHORT).show();
            }
            allClicks();
        }

        apiInterface = ApiClient.getClient().create(StanrzInterface.class);

        binding.header.imgHeader.setOnClickListener(v -> getActivity().onBackPressed());

        binding.ivOption.setOnClickListener(view ->
                {
                    showOption();
                }
        );

        binding.btnFollow.setOnClickListener(v ->
                {
                    addFollow();
                }
                );
        binding.btnFollowing.setOnClickListener(v ->
                {
                    unfollowDialog();
                }
                );
        binding.btnChat.setOnClickListener(v ->
                {
                    Bundle bundle1 = new Bundle();
                    bundle1.putString("name",otherUsersDetail.getUsername());
                    bundle1.putString("id",otherUsersDetail.getId());
                    bundle1.putString("image",otherUsersDetail.getImage());
                    Navigation.findNavController(v).navigate(R.id.action_newOtherUserDetailFragment_to_one2OneChatFragment,bundle1);
                }
                );
        return binding.getRoot();
    }

    public void getDescription()
    {

        DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));
        Map<String,String> map = new HashMap<>();
        map.put("user_id",otherUserId);
        Call<SuccessResAddSubscription> call = apiInterface.getDescription(map);
        call.enqueue(new Callback<SuccessResAddSubscription>() {
            @Override
            public void onResponse(Call<SuccessResAddSubscription> call, Response<SuccessResAddSubscription> response) {

                DataManager.getInstance().hideProgressMessage();
                try {
                    SuccessResAddSubscription data = response.body();
                    Log.e("data",data.status);
                    if (data.status.equals("1")) {
                        String dataResponse = new Gson().toJson(response.body());
                        showToast(getActivity(), data.message);
                        description = data.getResult();
                        if(!description.getDescription().equalsIgnoreCase(""))
                        {
                            binding.tvDescription.setText(description.getDescription());
                        }
                        else
                        {
                            binding.tvDescription.setText(otherUsersDetail.getUsername()+" "+getString(R.string.fan_club_1));
                        }
                    } else if (data.status.equals("0")) {
                        showToast(getActivity(), data.message);
                        binding.tvDescription.setText(otherUsersDetail.getUsername()+" "+getString(R.string.fan_club_1));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<SuccessResAddSubscription> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }
        });
    }

    private void unfollowDialog()
    {
        AppCompatButton btnCancel,btnRemove;
        TextView tvName,tvRemoveText;
        CircleImageView ivProfile;
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().getAttributes().windowAnimations = android.R.style.Widget_Material_ListPopupWindow;
        dialog.setContentView(R.layout.unfollow_diaog);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = dialog.getWindow();
        lp.copyFrom(window.getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);
        btnCancel = dialog.findViewById(R.id.btnCancel);
        btnRemove = dialog.findViewById(R.id.btnRemove);
        tvName = dialog.findViewById(R.id.tvName);
        tvRemoveText = dialog.findViewById(R.id.tvRemoveText);
        ivProfile = dialog.findViewById(R.id.ivOtherUserProfile);

        Glide
                .with(getActivity())
                .load(otherUsersDetail.getImage())
                .centerCrop()
                .placeholder(R.drawable.ic_user)
                .into(ivProfile);

        btnCancel.setOnClickListener(v ->
                {
                    dialog.dismiss();
                }
                );

        btnRemove.setOnClickListener(v ->
                {
                    addFollow();
                    dialog.dismiss();
                }
                );
        tvName.setText(otherUsersDetail.getUsername());
        tvRemoveText.setText("Stanrz won't tell "+otherUsersDetail.getUsername()+"they were removed from your following");
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    private void getOtherUserProfile(String otherUser) {
        String userId = SharedPreferenceUtility.getInstance(getContext()).getString(USER_ID);
        DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));
        Map<String,String> map = new HashMap<>();
        map.put("user_id",otherUser);
        map.put("login_id",userId);

        Call<SuccessResGetOtherUsers> call = apiInterface.getOtherProfile(map);

        call.enqueue(new Callback<SuccessResGetOtherUsers>() {
            @Override
            public void onResponse(Call<SuccessResGetOtherUsers> call, Response<SuccessResGetOtherUsers> response) {
                DataManager.getInstance().hideProgressMessage();
                try {
                    SuccessResGetOtherUsers data = response.body();
                    otherUsersDetail = data.getResult();
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
            public void onFailure(Call<SuccessResGetOtherUsers> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }
        });
    }

    private void allClicks() {
        binding.tvMore.setOnClickListener(view ->
                {
                    Bundle bundle = new Bundle();
                    bundle.putString("id",otherUsersDetail.getId());
                    Navigation.findNavController(view).navigate(R.id.action_newOtherUserDetailFragment_to_seeMoreFragment,bundle);
                }
        );

         binding.tvIamSeeMore.setOnClickListener(view ->
                {
                    Bundle bundle = new Bundle();
                    bundle.putString("id",otherUsersDetail.getId());
                    Navigation.findNavController(view).navigate(R.id.action_newOtherUserDetailFragment_to_iamStanSeeMoreFragment,bundle);
                }
                );

        binding.tvJoin.setOnClickListener(v ->
                {
                    Bundle bundle = new Bundle();
                    bundle.putString("id",otherUserId);
                    Navigation.findNavController(v).navigate(R.id.action_newOtherUserDetailFragment_to_joinVipMembershipFragment,bundle);
                }
        );

        binding.cvUploads.setOnClickListener(v ->
                {
                    getUploadedImagesVideos(otherUserId);
                    binding.rvUploads.setVisibility(View.VISIBLE);
                    binding.rvStories.setVisibility(View.GONE);
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
                    getUploadedVideos(otherUserId);
                    binding.rvUploads.setVisibility(View.VISIBLE);
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
                   if(subscribed)
                   {
                       binding.rvUploads.setVisibility(View.VISIBLE);
                       binding.llVip.setVisibility(View.GONE);
                       binding.rvStories.setVisibility(View.VISIBLE);
                       getFanPosts(otherUserId);
                       getStories(otherUserId);
                   }
                   else
                   {
                       binding.rvUploads.setVisibility(View.GONE);
                       binding.llVip.setVisibility(View.VISIBLE);
                   }
                    binding.tvVip.setBackgroundResource(R.drawable.ic_header_bg);
                    binding.tvVip.setTextColor(getResources().getColor(R.color.white));
                    binding.tvImage.setBackgroundResource(0);
                    binding.tvImage.setTextColor(getResources().getColor(R.color.black));
                    binding.tvVideo.setBackgroundResource(0);
                    binding.tvVideo.setTextColor(getResources().getColor(R.color.black));
                }
        );

/*
        binding.cvFanPost.setOnClickListener(v ->
                {
                    binding.tvFan.setBackgroundResource(R.drawable.ic_header_bg);
                    binding.tvFan.setTextColor(getResources().getColor(R.color.white));
                    binding.tvImage.setBackgroundResource(0);
                    binding.tvImage.setTextColor(getResources().getColor(R.color.black));
                    binding.tvVideo.setBackgroundResource(0);
                    binding.tvVideo.setTextColor(getResources().getColor(R.color.black));
                    binding.tvVip.setBackgroundResource(0);
                    binding.tvVip.setTextColor(getResources().getColor(R.color.black));
                }
        );*/

        binding.llFollowers.setOnClickListener(v ->
                {
                    Bundle bundle = new Bundle();
                    bundle.putString("name",otherUsersDetail.getUsername());
                    bundle.putString("id",otherUsersDetail.getId());
                    bundle.putString("Goto","1");
                    Navigation.findNavController(v).navigate(R.id.action_otherUserDetailFragment_to_otherUserFollowersFollowingFragment,bundle);
                }
        );

        binding.llFollowing.setOnClickListener(v ->
                {
                    Bundle bundle = new Bundle();
                    bundle.putString("name",otherUsersDetail.getUsername());
                    bundle.putString("id",otherUsersDetail.getId());
                    bundle.putString("Goto","2");
                    Navigation.findNavController(v).navigate(R.id.action_otherUserDetailFragment_to_otherUserFollowersFollowingFragment,bundle);
                }
        );
    }

    private void setProfileDetails()
    {
        Glide
                .with(getActivity())
                .load(otherUsersDetail.getImage())
                .centerCrop()
                .placeholder(R.drawable.ic_user)
                .into(binding.ivProfile);
        binding.tvName.setText(otherUsersDetail.getUsername());
        binding.tvName.setText(otherUsersDetail.getUsername());

        Glide
                .with(getActivity())
                .load(otherUsersDetail.getBgImage())
                .centerCrop()
                .into(binding.ivCoverPhoto);

        binding.tvName.setText(otherUsersDetail.getUsername());
        binding.tvPosts.setText(otherUsersDetail.getTotalPost()+"");
        binding.tvFollower.setText(otherUsersDetail.getTotalFollowers()+"");
        binding.tvFollowing.setText(otherUsersDetail.getTotalFollowing()+"");
        binding.header.tvHeader.setText(otherUsersDetail.getUsername());

        enableDisable = otherUsersDetail.getStanrzOf();

        if(enableDisable.equalsIgnoreCase("1"))
        {

         binding.rvIamStanrz.setVisibility(View.VISIBLE);
         binding.iamStanrzOf.setVisibility(View.VISIBLE);
         binding.tvIamSeeMore.setVisibility(View.VISIBLE);
        }
        else
        {
            binding.rvIamStanrz.setVisibility(View.GONE);
            binding.iamStanrzOf.setVisibility(View.GONE);
            binding.tvIamSeeMore.setVisibility(View.GONE);
        }

        binding.tvMyTopStanrz.setText(otherUsersDetail.getUsername()+" "+getString(R.string.daily_top_10_stanrz));
        binding.iamStanrzOf.setText(otherUsersDetail.getUsername()+" "+getString(R.string.i_am_a_stanrz_of));
//        if (otherUsersDetail.getFanClub().equalsIgnoreCase("1"))
//        {
//            binding.tvJoin.setVisibility(View.VISIBLE);
//        }
//        else
//        {
//            binding.tvJoin.setVisibility(View.GONE);
//        }
        if(otherUsersDetail.getSubscriber().equalsIgnoreCase("Subscribed"))
        {
            subscribed = true;
        }
        else
        {
            subscribed = false;
        }

        if(otherUsersDetail.getFollow().equalsIgnoreCase("Following"))
        {
            binding.btnFollowing.setVisibility(View.VISIBLE);
            binding.btnFollow.setVisibility(View.GONE);
        }
        else
        {
            binding.btnFollowing.setVisibility(View.GONE);
            binding.btnFollow.setVisibility(View.VISIBLE);
        }

        if(otherUsersDetail.getVerified().equalsIgnoreCase("Verified"))
        {
            binding.verifiedTick.setVisibility(View.VISIBLE);
        }else
        {
            binding.verifiedTick.setVisibility(View.GONE);
        }

        if(otherUsersDetail.getOpenFanClub().equalsIgnoreCase("done"))
        {
            binding.cvVip.setVisibility(View.VISIBLE);
        }else
        {
            binding.cvVip.setVisibility(View.GONE);
        }


//        if(otherUsersDetail.getFanClub().equalsIgnoreCase("0"))
//        {
//            binding.tvJoin.setVisibility(View.GONE);
//        }else
//        {
//            binding.tvJoin.setVisibility(View.VISIBLE);
//        }

        String bio = otherUsersDetail.getBio();

//        if(bio.contains("\n"))
//        {
//            bio = bio.replace("\n","<br>");
//        }

        binding.tvBio.setText(bio);

        if(otherUsersDetail.getBio().equalsIgnoreCase(""))
        {
            binding.tvBio.setVisibility(View.GONE);
        }
        else
        {

            binding.tvBio.post(new Runnable() {
                @Override
                public void run() {
                    int lineCount = binding.tvBio.getLineCount();
                    // Use lineCount here
                    if (lineCount>4)
                    {
                        makeTextViewResizable(binding.tvBio, 4, getString(R.string.see_more), true);
                    }
                }
            });

            binding.tvBio.setVisibility(View.VISIBLE);
        }
        binding.tvWebsite.setText(otherUsersDetail.getWebsite());
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

        if(otherUsersDetail.getWebsite().equalsIgnoreCase(""))
        {
            binding.tvWebsite.setVisibility(View.GONE);
        }
        else
        {
            binding.tvWebsite.setVisibility(View.VISIBLE);
        }
    }

    public void addFollow()
    {

        String userId = SharedPreferenceUtility.getInstance(getContext()).getString(USER_ID);
        DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));
        Map<String,String> map = new HashMap<>();
        map.put("user_id",userId);
        map.put("follower_id",otherUserId);
        Call<SuccessResAddFollowing> call = apiInterface.addFollowing(map);

        call.enqueue(new Callback<SuccessResAddFollowing>() {
            @Override
            public void onResponse(Call<SuccessResAddFollowing> call, Response<SuccessResAddFollowing> response) {
                DataManager.getInstance().hideProgressMessage();
                getOtherUserProfile(otherUserId);
                try {
                    SuccessResAddFollowing data = response.body();
                    Log.e("data",data.status);
                    showToast(getActivity(), data.result);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<SuccessResAddFollowing> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }
        });
    }

    private void getUploadedImagesVideos(String otherUserId) {

        String userId = SharedPreferenceUtility.getInstance(getContext()).getString(USER_ID);
        DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));
        Map<String,String> map = new HashMap<>();
        map.put("user_id",userId);
        map.put("other_id",otherUserId);

        Call<SuccessResGetUploadedVideos> call = apiInterface.getOtherUploadedImageVideos(map);

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
                        myVideos.clear();
                        videoAdapter = new UploadsAdapter(getActivity(),myVideos,"newother",successResGetUploadedVideosItem);
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

    private void getUploadedVideos(String otherUserId) {
        String userId = SharedPreferenceUtility.getInstance(getContext()).getString(USER_ID);
        DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));
        Map<String,String> map = new HashMap<>();
        map.put("user_id",userId);
        map.put("other_id",otherUserId);
        Call<SuccessResGetUploadedVideos> call = apiInterface.getOtherUserVideos(map);
        call.enqueue(new Callback<SuccessResGetUploadedVideos>() {
            @Override
            public void onResponse(Call<SuccessResGetUploadedVideos> call, Response<SuccessResGetUploadedVideos> response) {

                DataManager.getInstance().hideProgressMessage();

                try {
                    SuccessResGetUploadedVideos data = response.body();
                    successResGetUploadedVideosItem = data;
                    uploadedVideos.clear();
                    uploadedVideos.addAll(data.getResult());
                    setVideoList();
                    Log.e("data",data.status);
                    if (data.status.equals("1")) {
                        String dataResponse = new Gson().toJson(response.body());
                        Log.e("MapMap", "EDIT PROFILE RESPONSE" + dataResponse);
                    } else if (data.status.equals("0")) {
                        showToast(getActivity(), data.message);
                        uploadsList.clear();
                        myVideos.clear();
                        videoAdapter = new UploadsAdapter(getActivity(),myVideos,"newother",successResGetUploadedVideosItem);
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
        videoAdapter = new UploadsAdapter(getActivity(),myVideos,"newother",successResGetUploadedVideosItem);
        binding.rvUploads.setHasFixedSize(true);
        binding.rvUploads.setLayoutManager(new GridLayoutManager(getActivity(),3));
        binding.rvUploads.setAdapter(videoAdapter);
    }

    private void getFanPosts(String otherUserId) {

        String userId = SharedPreferenceUtility.getInstance(getContext()).getString(USER_ID);
        DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));
        Map<String,String> map = new HashMap<>();
        map.put("user_id",otherUserId);
        map.put("fan_id",userId);

        Call<SuccessResGetUploadedVideos> call = apiInterface.getFanPosts(map);

        call.enqueue(new Callback<SuccessResGetUploadedVideos>() {
            @Override
            public void onResponse(Call<SuccessResGetUploadedVideos> call, Response<SuccessResGetUploadedVideos> response) {

                DataManager.getInstance().hideProgressMessage();

                try {
                    SuccessResGetUploadedVideos data = response.body();

                    successResGetUploadedVideosItem = data;

                    uploadedVideos.clear();
                    uploadedVideos.addAll(data.getResult());

                    setVideoList();

                    Log.e("data",data.status);
                    if (data.status.equals("1")) {
                        String dataResponse = new Gson().toJson(response.body());
                        Log.e("MapMap", "EDIT PROFILE RESPONSE" + dataResponse);
                    } else if (data.status.equals("0")) {
                        showToast(getActivity(), data.message);
                        uploadsList.clear();
                        myVideos.clear();
                        videoAdapter = new UploadsAdapter(getActivity(),myVideos,"newother",successResGetUploadedVideosItem);

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

    private void getTopStanrz() {

        DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));
        Map<String,String> map = new HashMap<>();
        map.put("user_id",otherUserId);
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
                        binding.rvStanrz.setAdapter(new StanrzAdapter(getActivity(),topStanrzList,"newother"));

                    } else if (data.status.equals("0")) {
                        showToast(getActivity(), data.message);
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

        DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));
        Map<String,String> map = new HashMap<>();
        map.put("user_id",otherUserId);
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
                        binding.rvIamStanrz.setAdapter(new IamStanAdapter(getActivity(),stanrzOfList,"newother"));

                    } else if (data.status.equals("0")) {
                        showToast(getActivity(), data.message);
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

    private void getStories(String userIId) {

        DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));
        Map<String,String> map = new HashMap<>();
        map.put("user_id",userIId);

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
                        binding.rvStories.setAdapter(new StoriesAdapter(getActivity(),storyList,NewOtherUserDetailFragment.this));

                    } else if (data.status.equals("0")) {

                        storyList.clear();
                        binding.rvStories.setHasFixedSize(true);
                        binding.rvStories.setLayoutManager(new LinearLayoutManager(getActivity()));
                        binding.rvStories.setAdapter(new StoriesAdapter(getActivity(),storyList,NewOtherUserDetailFragment.this));
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
            Navigation.findNavController(v).navigate(R.id.action_newOtherUserDetailFragment_to_myStoriesFragment);
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

    public void showOption()
    {
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().getAttributes().windowAnimations = android.R.style.Widget_Material_ListPopupWindow;
        dialog.setContentView(R.layout.dialog_show_user_options);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = dialog.getWindow();
        lp.copyFrom(window.getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);

        TextView tvShare = dialog.findViewById(R.id.tvShare);
        TextView tvReport = dialog.findViewById(R.id.tvReport);
        TextView tvBlock = dialog.findViewById(R.id.tvBlock);

        tvBlock.setOnClickListener(view ->
                {
                    dialog.dismiss();
                    blockUser(otherUserId);
                }
        );

        tvShare.setOnClickListener(v1 ->
                {
                    dialog.dismiss();
                    String shareBody = "Username :"+otherUsersDetail.getUsername()+"\n\n Profile :"+otherUsersDetail.getImage();
                    Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                    sharingIntent.setType("text/plain");
                    sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject Here");
                    sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                    getActivity().startActivity(Intent.createChooser(sharingIntent,getActivity().getResources().getString(R.string.share_using)));

//                    Uri imageUri = Uri.parse("android.resource://" + getPackageName()
//                            + "/drawable/" + "ic_launcher");
//                    Intent shareIntent = new Intent();
//                    shareIntent.setAction(Intent.ACTION_SEND);
//                    shareIntent.putExtra(Intent.EXTRA_TEXT, "Hello");
//                    shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
//                    shareIntent.setType("image/jpeg");
//                    shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//                    startActivity(Intent.createChooser(shareIntent, "send"));

                }
        );

        tvReport.setOnClickListener(v1 ->
                {

                    dialog.dismiss();
                    bottomSheetFragment= new BottomSheet(getActivity(), new ReportInterface() {
                        @Override
                        public void onReport(String content,String userId) {
                            reportUser(otherUserId,content);
                        }

                        @Override
                        public void deleteChat(String userId) {
                        }

                        @Override
                        public void blockUser(String userId,String other) {
                        }

                    });

                    bottomSheetFragment.show(getActivity().getSupportFragmentManager(),"ModalBottomSheet");
                }
        );

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    public void blockUser(String otherId)
    {

        String userId = SharedPreferenceUtility.getInstance(getContext()).getString(USER_ID);
        DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));
        Map<String,String> map = new HashMap<>();
        map.put("user_id",userId);
        map.put("block_user_id",otherId);

        Call<SuccessResAddLike> call = apiInterface.blockUser(map);

        call.enqueue(new Callback<SuccessResAddLike>() {
            @Override
            public void onResponse(Call<SuccessResAddLike> call, Response<SuccessResAddLike> response) {

                DataManager.getInstance().hideProgressMessage();
                try {
                    SuccessResAddLike data = response.body();
                    Log.e("data",data.status);

                    getActivity().onBackPressed();

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


    public void reportUser(String id,String report)
    {
        String userId = SharedPreferenceUtility.getInstance(getContext()).getString(USER_ID);
        DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));
        Map<String,String> map = new HashMap<>();
        map.put("user_id",userId);
        map.put("report_user_id",id);
        map.put("report_reson",report);

        Call<SuccessResReportUser> call = apiInterface.reportUser(map);

        call.enqueue(new Callback<SuccessResReportUser>() {
            @Override
            public void onResponse(Call<SuccessResReportUser> call, Response<SuccessResReportUser> response) {

                DataManager.getInstance().hideProgressMessage();
                try {
                    SuccessResReportUser data = response.body();
                    Log.e("data",data.status);
                    showToast(getActivity(), data.message);
                    bottomSheetFragment.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<SuccessResReportUser> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }
        });
    }



}