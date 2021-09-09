package com.technorizen.stanrz.fragments;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.appcompat.widget.AppCompatButton;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.technorizen.stanrz.R;
import com.technorizen.stanrz.adapters.IamStanAdapter;
import com.technorizen.stanrz.adapters.StanrzAdapter;
import com.technorizen.stanrz.adapters.UploadsAdapter;
import com.technorizen.stanrz.databinding.FragmentOtherUserDetailBinding;
import com.technorizen.stanrz.models.SuccessResAddFollowing;
import com.technorizen.stanrz.models.SuccessResGetOtherUsers;
import com.technorizen.stanrz.models.SuccessResGetUploads;
import com.technorizen.stanrz.retrofit.ApiClient;
import com.technorizen.stanrz.retrofit.StanrzInterface;
import com.technorizen.stanrz.utility.DataManager;
import com.technorizen.stanrz.utility.SharedPreferenceUtility;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.technorizen.stanrz.retrofit.Constant.USER_ID;
import static com.technorizen.stanrz.retrofit.Constant.showToast;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link OtherUserDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OtherUserDetailFragment extends Fragment {

    FragmentOtherUserDetailBinding binding;
    private SuccessResGetOtherUsers.Result otherUsersDetail;
    private ArrayList<SuccessResGetUploads.Result> uploadsList = new ArrayList<>();

    private StanrzInterface apiInterface;
    private String otherUserId = "";

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public OtherUserDetailFragment() {
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
    public static OtherUserDetailFragment newInstance(String param1, String param2) {
        OtherUserDetailFragment fragment = new OtherUserDetailFragment();
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
            getOtherUserProfile(otherUserId);
            getUploadedImagesVideos(otherUserId);
            allClicks();
        }

        apiInterface = ApiClient.getClient().create(StanrzInterface.class);

        binding.header.imgHeader.setOnClickListener(v -> getActivity().onBackPressed());

        binding.rvStanrz.setHasFixedSize(true);
        binding.rvStanrz.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false));
        binding.rvStanrz.setAdapter(new StanrzAdapter(getActivity()));

    /*    binding.rvUploads.setHasFixedSize(true);
        binding.rvUploads.setLayoutManager(new GridLayoutManager(getActivity(),3));
        binding.rvUploads.setAdapter(new UploadsAdapter(getActivity()));
*/
        binding.rvIamStanrz.setHasFixedSize(true);
        binding.rvIamStanrz.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false));
        binding.rvIamStanrz.setAdapter(new IamStanAdapter(getActivity()));

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

        return binding.getRoot();
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
        //This makes the dialog take up the full width
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

      /*  RequestBody email = RequestBody.create(MediaType.parse("text/plain"),strEmail);
        RequestBody password = RequestBody.create(MediaType.parse("text/plain"), strPassword);
        RequestBody registerID = RequestBody.create(MediaType.parse("text/plain"),deviceToken);
*/
//        Call<SuccessResSignIn> call = apiInterface.login(email,password,registerID);
        Call<SuccessResGetOtherUsers> call = apiInterface.getOtherProfile(map);

        call.enqueue(new Callback<SuccessResGetOtherUsers>() {
            @Override
            public void onResponse(Call<SuccessResGetOtherUsers> call, Response<SuccessResGetOtherUsers> response) {

                DataManager.getInstance().hideProgressMessage();

                try {
                    SuccessResGetOtherUsers data = response.body();
                    otherUsersDetail = data.getResult();
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
            public void onFailure(Call<SuccessResGetOtherUsers> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }
        });
    }

    private void allClicks() {

        binding.tvJoin.setOnClickListener(v ->
                {
                //    Navigation.findNavController(v).navigate(R.id.action_navigation_profile_to_joinVipMembershipFragment);

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
        binding.tvBio.setText(otherUsersDetail.getBio());

        binding.header.tvHeader.setText(otherUsersDetail.getUsername());

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


//        str_image_path = sellerDetail.getImage();
    }

    public void addFollow()
    {

        String userId = SharedPreferenceUtility.getInstance(getContext()).getString(USER_ID);
        DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));
        Map<String,String> map = new HashMap<>();
        map.put("user_id",userId);
        map.put("follower_id",otherUserId);

      /*  RequestBody email = RequestBody.create(MediaType.parse("text/plain"),strEmail);
        RequestBody password = RequestBody.create(MediaType.parse("text/plain"), strPassword);
        RequestBody registerID = RequestBody.create(MediaType.parse("text/plain"),deviceToken);
*/
//        Call<SuccessResSignIn> call = apiInterface.login(email,password,registerID);
        Call<SuccessResAddFollowing> call = apiInterface.addFollowing(map);

        call.enqueue(new Callback<SuccessResAddFollowing>() {
            @Override
            public void onResponse(Call<SuccessResAddFollowing> call, Response<SuccessResAddFollowing> response) {

                DataManager.getInstance().hideProgressMessage();
                getOtherUserProfile(otherUserId);

                try {
                    SuccessResAddFollowing data = response.body();
//                    setSellerData();
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

    private void getUploadedImagesVideos(String otherUser) {

        String userId = SharedPreferenceUtility.getInstance(getContext()).getString(USER_ID);

        DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));
        Map<String,String> map = new HashMap<>();
        map.put("user_id",userId);
        map.put("other_id",otherUser);

      /*  RequestBody email = RequestBody.create(MediaType.parse("text/plain"),strEmail);
        RequestBody password = RequestBody.create(MediaType.parse("text/plain"), strPassword);
        RequestBody registerID = RequestBody.create(MediaType.parse("text/plain"),deviceToken);
*/
//        Call<SuccessResSignIn> call = apiInterface.login(email,password,registerID);
        Call<SuccessResGetUploads> call = apiInterface.getOtherUploadedImageVideos(map);

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
                    binding.rvUploads.setAdapter(new UploadsAdapter(getActivity(),uploadsList,false));

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
                        binding.rvUploads.setAdapter(new UploadsAdapter(getActivity(),uploadsList,false));
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