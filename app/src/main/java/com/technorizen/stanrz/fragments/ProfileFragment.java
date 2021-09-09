package com.technorizen.stanrz.fragments;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
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

import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.technorizen.stanrz.BuildConfig;
import com.technorizen.stanrz.R;
import com.technorizen.stanrz.adapters.IamStanAdapter;
import com.technorizen.stanrz.adapters.StanrzAdapter;
import com.technorizen.stanrz.adapters.UploadsAdapter;
import com.technorizen.stanrz.databinding.FragmentAddPostBinding;
import com.technorizen.stanrz.databinding.FragmentProfileBinding;
import com.technorizen.stanrz.models.SuccessResGetOtherUsers;
import com.technorizen.stanrz.models.SuccessResGetUploads;
import com.technorizen.stanrz.models.SuccessResProfileData;
import com.technorizen.stanrz.models.SuccessResUpdateProfile;
import com.technorizen.stanrz.retrofit.ApiClient;
import com.technorizen.stanrz.retrofit.StanrzInterface;
import com.technorizen.stanrz.utility.DataManager;
import com.technorizen.stanrz.utility.SharedPreferenceUtility;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    FragmentProfileBinding binding;

    private SuccessResProfileData.Result userDetail;

    private SuccessResGetOtherUsers.Result otherUsersDetail;

    private ArrayList<SuccessResGetUploads.Result> uploadsList = new ArrayList<>();

    private StanrzInterface apiInterface;

    String str_image_path="";
    private static final int REQUEST_CAMERA = 1;
    private static final int SELECT_FILE = 2;
    private Uri uriSavedImage;
    private static final int MY_PERMISSION_CONSTANT = 5;


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

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
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
        // Inflate the layout for this fragment

        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_profile, container, false);

        apiInterface = ApiClient.getClient().create(StanrzInterface.class);

        binding.rvStanrz.setHasFixedSize(true);
        binding.rvStanrz.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false));
        binding.rvStanrz.setAdapter(new StanrzAdapter(getActivity()));

        binding.rvIamStanrz.setHasFixedSize(true);
        binding.rvIamStanrz.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false));
        binding.rvIamStanrz.setAdapter(new IamStanAdapter(getActivity()));

            allClicks();
            getProfile();
            getUploadedImagesVideos();

        return binding.getRoot();
    }

     private void allClicks() {

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
                    binding.rvUploads.setVisibility(View.GONE);
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
                    binding.rvUploads.setVisibility(View.GONE);
                    binding.llVip.setVisibility(View.VISIBLE);

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

      /*  RequestBody email = RequestBody.create(MediaType.parse("text/plain"),strEmail);
        RequestBody password = RequestBody.create(MediaType.parse("text/plain"), strPassword);
        RequestBody registerID = RequestBody.create(MediaType.parse("text/plain"),deviceToken);
*/
//        Call<SuccessResSignIn> call = apiInterface.login(email,password,registerID);
        Call<SuccessResProfileData> call = apiInterface.getProfile(map);

        call.enqueue(new Callback<SuccessResProfileData>() {
            @Override
            public void onResponse(Call<SuccessResProfileData> call, Response<SuccessResProfileData> response) {

                DataManager.getInstance().hideProgressMessage();

                try {
                    SuccessResProfileData data = response.body();
                    userDetail = data.getResult();
//                    setSellerData();
                    Log.e("data",data.status);
                    if (data.status.equals("1")) {
                        String dataResponse = new Gson().toJson(response.body());
                        Log.e("MapMap", "EDIT PROFILE RESPONSE" + dataResponse);
                        setProfileDetails();

//                        SessionManager.writeString(RegisterAct.this, Constant.driver_id,data.result.id);
//                        App.showToast(RegisterAct.this, data.message, Toast.LENGTH_SHORT);
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
        binding.tvName.setText(userDetail.getUsername());
        binding.tvName.setText(userDetail.getUsername());

        Glide
                .with(getActivity())
                .load(userDetail.getBgImage())
                .centerCrop()
                .into(binding.ivCoverPhoto);

        binding.tvName.setText(userDetail.getUsername());

        binding.tvPosts.setText(userDetail.getTotalPost()+"");
        binding.tvFollower.setText(userDetail.getTotalFollowers()+"");
        binding.tvFollowing.setText(userDetail.getTotalFollowing()+"");
        binding.tvBio.setText(userDetail.getBio());

//        str_image_path = sellerDetail.getImage();
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

                    binding.rvUploads.setHasFixedSize(true);
                    binding.rvUploads.setLayoutManager(new GridLayoutManager(getActivity(),3));
                    binding.rvUploads.setAdapter(new UploadsAdapter(getActivity(),uploadsList,true));

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
                        binding.rvUploads.setHasFixedSize(true);
                        binding.rvUploads.setLayoutManager(new GridLayoutManager(getActivity(),3));
                        binding.rvUploads.setAdapter(new UploadsAdapter(getActivity(),uploadsList,true));
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

}