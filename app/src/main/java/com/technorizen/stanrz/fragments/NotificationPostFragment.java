package com.technorizen.stanrz.fragments;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.technorizen.stanrz.R;
import com.technorizen.stanrz.adapters.PostsAdapter;
import com.technorizen.stanrz.databinding.FragmentNotificationPostBinding;
import com.technorizen.stanrz.models.Result;
import com.technorizen.stanrz.models.SuccessResAddLike;
import com.technorizen.stanrz.models.SuccessResAddSuperLike;
import com.technorizen.stanrz.models.SuccessResDeletePost;
import com.technorizen.stanrz.models.SuccessResGetUploadedVideos;
import com.technorizen.stanrz.models.SuccessResGetUser;
import com.technorizen.stanrz.models.SuccessResReportUser;
import com.technorizen.stanrz.models.SuccessResSavePost;
import com.technorizen.stanrz.models.SuccessResUnlockNsfw;
import com.technorizen.stanrz.models.UserPost;
import com.technorizen.stanrz.retrofit.ApiClient;
import com.technorizen.stanrz.retrofit.NetworkAvailablity;
import com.technorizen.stanrz.retrofit.StanrzInterface;
import com.technorizen.stanrz.utility.AddLike;
import com.technorizen.stanrz.utility.BottomSheet;
import com.technorizen.stanrz.utility.DataManager;
import com.technorizen.stanrz.utility.ReportInterface;
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
 * Use the {@link NotificationPostFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NotificationPostFragment extends Fragment implements AddLike {

    private StanrzInterface apiInterface;
    FragmentNotificationPostBinding binding;
    private ArrayList<SuccessResGetUploadedVideos.Result> uploadedVideos = new ArrayList<>();
    private PostsAdapter postsAdapter;
    private ArrayList<SuccessResGetUser.Result> usersList = new ArrayList<>();
    private ArrayList<Result> myVideos = new ArrayList<>();

    private  BottomSheet bottomSheetFragment;
    private String postId = "";

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public NotificationPostFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NotificationPostFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NotificationPostFragment newInstance(String param1, String param2) {
        NotificationPostFragment fragment = new NotificationPostFragment();
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

        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_notification_post, container, false);

        binding.header.imgHeader.setOnClickListener(view ->
                {
                    getActivity().onBackPressed();
                }
                );

        binding.header.tvHeader.setText(R.string.post);

        apiInterface = ApiClient.getClient().create(StanrzInterface.class);

        Bundle bundle = this.getArguments();

        if (bundle!=null)
        {
            postId = bundle.getString("key");
            getAllUsers();
        }

        postsAdapter = new PostsAdapter(getActivity(),myVideos,NotificationPostFragment.this,"notification",usersList);
        binding.rvPosts.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.rvPosts.setAdapter(postsAdapter);

        if (NetworkAvailablity.getInstance(getActivity()).checkNetworkStatus()) {

            getAllUsers();

        } else {
            Toast.makeText(getActivity(), getResources().getString(R.string.msg_noInternet), Toast.LENGTH_SHORT).show();
        }
        return binding.getRoot();
    }

    private void getUploadedImagesVideos() {

        String userId = SharedPreferenceUtility.getInstance(getContext()).getString(USER_ID);
        DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));
        Map<String,String> map = new HashMap<>();
        map.put("user_id",userId);
        map.put("post_id",postId);

        Call<SuccessResGetUploadedVideos> call = apiInterface.getPostById(map);

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
                        setVideoList();

                    } else if (data.status.equals("0")) {
                        showToast(getActivity(), data.message);
                        if(uploadedVideos.size()==0)
                        {
                            uploadedVideos.clear();
                            myVideos.clear();
                            postsAdapter.notifyDataSetChanged();
                        }else
                        {
                            setVideoList();
                        }
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

            if(userPostArrayList.size()>0)
            {
                myVideos.add(myVideoModel);
            }
        }
        postsAdapter.notifyDataSetChanged();
        addScrollListener();
    }

    protected final void addScrollListener() {
        binding.rvPosts.clearOnScrollListeners();
        binding.rvPosts.addOnScrollListener((RecyclerView.OnScrollListener)(new RecyclerView.OnScrollListener() {
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

                if (binding.rvPosts != null) {
                    RecyclerView.LayoutManager var10000 = binding.rvPosts.getLayoutManager();
                    LinearLayoutManager layoutManager = (LinearLayoutManager)var10000;
                    int firstPosition = layoutManager.findFirstVisibleItemPosition();
                    int lastPosition = layoutManager.findLastVisibleItemPosition();
                    Rect rvRect = new Rect();
                    binding.rvPosts.getGlobalVisibleRect(rvRect);
                    int i = firstPosition;
                    int var9 = lastPosition;
                    if (firstPosition <= lastPosition) {
                        while(true) {
                            Rect rowRect = new Rect();
                            View var14 = layoutManager.findViewByPosition(i);

                            var14.getGlobalVisibleRect(rowRect);
                            boolean percentFirst = false;
                            int visibleHeightFirst;
                            int var15;
                            View var10001;
                            if (rowRect.bottom >= rvRect.bottom) {
                                visibleHeightFirst = rvRect.bottom - rowRect.top;
                                var15 = visibleHeightFirst * 100;
                                var10001 = layoutManager.findViewByPosition(i);
                                var15 /= var10001.getHeight();
                            } else {
                                visibleHeightFirst = rowRect.bottom - rvRect.top;
                                var15 = visibleHeightFirst * 100;
                                var10001 = layoutManager.findViewByPosition(i);
                                var15 /= var10001.getHeight();
                            }

                            int percentFirstx = var15;

                            if (percentFirstx > 70 && !(myVideos.get(i)).isPlaying()) {
                                myVideos.get(i).setVisiblePercent(percentFirstx);
                                myVideos.get(i).setPlaying(true);
                                percentFirst = true;
                                postsAdapter.playVideo(i);

                            } else if (percentFirstx < 50) {
                                myVideos.get(i).setVisiblePercent(percentFirstx);
                                myVideos.get(i).setPlaying(false);
                                postsAdapter.stopVideo(i);
                            }

                            if (i == var9) {
                                break;
                            }
                            ++i;
                        }
                    }
                }
            }
        }));
    }

    private void getAllUsers()
    {

        DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));
        Map<String,String> map = new HashMap<>();

        Call<SuccessResGetUser> call = apiInterface.getAll(map);

        call.enqueue(new Callback<SuccessResGetUser>() {
            @Override
            public void onResponse(Call<SuccessResGetUser> call, Response<SuccessResGetUser> response) {

                DataManager.getInstance().hideProgressMessage();

                try {
                    SuccessResGetUser data = response.body();
                    Log.e("data",data.status);
                    if (data.status.equals("1")) {
                        String dataResponse = new Gson().toJson(response.body());
                        Log.e("MapMap", "EDIT PROFILE RESPONSE" + dataResponse);
                        usersList.clear();
                        usersList.addAll(data.getResult());
                        getUploadedImagesVideos();
                    } else if (data.status.equals("0")) {
                        showToast(getActivity(), data.message);
                        usersList.clear();
                        getUploadedImagesVideos();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<SuccessResGetUser> call, Throwable t) {
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
        Call<SuccessResAddLike> call = apiInterface.addLike(map);
        call.enqueue(new Callback<SuccessResAddLike>() {
            @Override
            public void onResponse(Call<SuccessResAddLike> call, Response<SuccessResAddLike> response) {

                DataManager.getInstance().hideProgressMessage();
                try {
                    SuccessResAddLike data = response.body();
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
        Navigation.findNavController(v).navigate(R.id.action_notificationPostFragment_to_addCommentFragment,bundle);
    }

    @Override
    public void bottomSheet(View param1, String postID, boolean isUser, int position, Result result) {

        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().getAttributes().windowAnimations = android.R.style.Widget_Material_ListPopupWindow;
        dialog.setContentView(R.layout.dialog_show_options);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = dialog.getWindow();
        lp.copyFrom(window.getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);

        TextView tvDelete = dialog.findViewById(R.id.tvDelete);
        TextView tvShare = dialog.findViewById(R.id.tvShare);
        TextView tvReport = dialog.findViewById(R.id.tvReport);
        TextView tvSave = dialog.findViewById(R.id.tvSave);

        if(result.getSaved().equalsIgnoreCase("0"))
        {
            tvSave.setText(R.string.save);
        }
        else
        {
            tvSave.setText(R.string.unsave);
        }

        if(isUser)
        {
            tvDelete.setVisibility(View.VISIBLE);
            tvReport.setVisibility(View.GONE);
        }
        else
        {
            tvReport.setVisibility(View.VISIBLE);
            tvDelete.setVisibility(View.GONE);
        }

        tvDelete.setOnClickListener(v1 ->
                {

                    dialog.dismiss();
                    new AlertDialog.Builder(getActivity())
                            .setTitle("Delete Post")
                            .setMessage("Are you sure you want to delete Post?")

                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {

                                    deletePost(postID,position);

                                }
                            })
                            .setNegativeButton(android.R.string.no, null)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                }
        );

        tvSave.setOnClickListener(v1 ->
                {
                    dialog.dismiss();

                    if(result.getSaved().equalsIgnoreCase("0"))
                    {
                        tvSave.setText(R.string.unsave);
                        result.setSaved("1");
                    }
                    else
                    {
                        tvSave.setText(R.string.save);
                        result.setSaved("0");
                    }

                    savePost(postID,"");

                }
        );

        tvShare.setOnClickListener(v1 ->
                {

                    dialog.dismiss();
                    String shareBody = "User :"+result.getUserName()+"\n\n Posted :"+result.getUserPost();
                    Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                    sharingIntent.setType("text/plain");
                    sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject Here");
                    sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                    getActivity().startActivity(Intent.createChooser(sharingIntent,getActivity().getResources().getString(R.string.share_using)));
                }
        );

        tvReport.setOnClickListener(v1 ->
                {

                    dialog.dismiss();

                    bottomSheetFragment= new BottomSheet(getActivity(), new ReportInterface() {
                        @Override
                        public void onReport(String content,String userId) {
                            reportUser(postID,content);
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



    private void deletePost(String postId,int position)
    {

        String userId = SharedPreferenceUtility.getInstance(getContext()).getString(USER_ID);
        DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));
        Map<String,String> map = new HashMap<>();
        map.put("user_id",userId);
        map.put("post_id",postId);

        Call<SuccessResDeletePost> call = apiInterface.deletePost(map);

        call.enqueue(new Callback<SuccessResDeletePost>() {
            @Override
            public void onResponse(Call<SuccessResDeletePost> call, Response<SuccessResDeletePost> response) {
                DataManager.getInstance().hideProgressMessage();
                try {
                    SuccessResDeletePost data = response.body();
                    Log.e("data",data.status);
                    if (data.status.equals("1")) {
                        String dataResponse = new Gson().toJson(response.body());
                        Log.e("MapMap", "EDIT PROFILE RESPONSE" + dataResponse);
                        showToast(getActivity(), data.message);
                        myVideos.remove(position);
                        postsAdapter.notifyDataSetChanged();

                    } else if (data.status.equals("0")) {
                        showToast(getActivity(), data.message);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<SuccessResDeletePost> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }
        });
    }


    @Override
    public void addSuperLikes(View v, String postId, String uploaderId, String quantity) {

        String userId = SharedPreferenceUtility.getInstance(getContext()).getString(USER_ID);
        DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));
        Map<String,String> map = new HashMap<>();
        map.put("user_id",userId);
        map.put("post_id",postId);
        map.put("superlike",quantity);

        Call<SuccessResAddSuperLike> call = apiInterface.addSuperLike(map);

        call.enqueue(new Callback<SuccessResAddSuperLike>() {
            @Override
            public void onResponse(Call<SuccessResAddSuperLike> call, Response<SuccessResAddSuperLike> response) {

                DataManager.getInstance().hideProgressMessage();
                try {
                    SuccessResAddSuperLike data = response.body();
                    Log.e("data",data.status);
                    showToast(getActivity(), data.message);
//                    getFanImagesVideos();
                    getUploadedImagesVideos();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<SuccessResAddSuperLike> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }
        });


    }

    @Override
    public void unlockNsfw(View v, String postId, String uploaderId, String quantity) {

        String userId = SharedPreferenceUtility.getInstance(getContext()).getString(USER_ID);
        DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));
        Map<String,String> map = new HashMap<>();
        map.put("user_id",userId);
        map.put("post_id",postId);
        map.put("nsfw","No");

        Call<SuccessResUnlockNsfw> call = apiInterface.unlockNSFW(map);

        call.enqueue(new Callback<SuccessResUnlockNsfw>() {
            @Override
            public void onResponse(Call<SuccessResUnlockNsfw> call, Response<SuccessResUnlockNsfw> response) {

                DataManager.getInstance().hideProgressMessage();
                try {
                    SuccessResUnlockNsfw data = response.body();
                    Log.e("data",data.status);
                    showToast(getActivity(), data.message);
                    getUploadedImagesVideos();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<SuccessResUnlockNsfw> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }
        });

    }

    public void reportUser(String postId,String report)
    {
        String userId = SharedPreferenceUtility.getInstance(getContext()).getString(USER_ID);
        DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));
        Map<String,String> map = new HashMap<>();
        map.put("user_id",userId);
        map.put("post_id",postId);
        map.put("report",report);

        Call<SuccessResReportUser> call = apiInterface.reportPost(map);

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

    public void savePost(String postId,String report)
    {
        String userId = SharedPreferenceUtility.getInstance(getContext()).getString(USER_ID);
        DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));
        Map<String,String> map = new HashMap<>();
        map.put("user_id",userId);
        map.put("post_id",postId);

        Call<SuccessResSavePost> call = apiInterface.savePost(map);

        call.enqueue(new Callback<SuccessResSavePost>() {
            @Override
            public void onResponse(Call<SuccessResSavePost> call, Response<SuccessResSavePost> response) {

                DataManager.getInstance().hideProgressMessage();
                try {
                    SuccessResSavePost data = response.body();
                    Log.e("data",data.status);
                    showToast(getActivity(), data.message);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<SuccessResSavePost> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }
        });
    }

}