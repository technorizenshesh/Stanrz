package com.technorizen.stanrz.fragments;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSmoothScroller;

import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.technorizen.stanrz.R;
import com.technorizen.stanrz.adapters.CommentAdapter;
import com.technorizen.stanrz.adapters.FollowingAdapter;
import com.technorizen.stanrz.adapters.SearchTagPeopleAdapter;
import com.technorizen.stanrz.databinding.FragmentAddCommentBinding;
import com.technorizen.stanrz.models.SuccessResAddComment;
import com.technorizen.stanrz.models.SuccessResDeleteComment;
import com.technorizen.stanrz.models.SuccessResDeletePost;
import com.technorizen.stanrz.models.SuccessResGetAll;
import com.technorizen.stanrz.models.SuccessResGetComment;
import com.technorizen.stanrz.models.SuccessResGetFollowings;
import com.technorizen.stanrz.models.SuccessResGetUser;
import com.technorizen.stanrz.models.SuccessResProfileData;
import com.technorizen.stanrz.retrofit.ApiClient;
import com.technorizen.stanrz.retrofit.StanrzInterface;
import com.technorizen.stanrz.utility.DataManager;
import com.technorizen.stanrz.utility.DeleteComment;
import com.technorizen.stanrz.utility.SharedPreferenceUtility;
import com.technorizen.stanrz.utility.TaggedUserId;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.technorizen.stanrz.activites.LoginActivity.TAG;
import static com.technorizen.stanrz.retrofit.Constant.USER_ID;
import static com.technorizen.stanrz.retrofit.Constant.showToast;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddCommentFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddCommentFragment extends Fragment {

    FragmentAddCommentBinding binding;

    private StanrzInterface apiInterface;
    private SuccessResProfileData.Result userDetail;
    private ArrayList<SuccessResGetUser.Result> taggedUserList = new ArrayList<>();

    private ArrayList<SuccessResGetUser.Result> usersList = new ArrayList<>();

    private List<SuccessResGetComment.Result> commentList = new LinkedList<>();

    private String emoji1 = "", emoji2= "",emoji3="",emoji4="";

    private String strPostID ="";

    private String strComment = "";

    Timer timer = new Timer();

    private ArrayList<SuccessResGetUser.Result> taggedUsersList = new ArrayList<>();

    private CommentAdapter commentAdapter;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AddCommentFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddCommentFragment.
     */

    // TODO: Rename and change types and number of parameters
    public static AddCommentFragment newInstance(String param1, String param2) {
        AddCommentFragment fragment = new AddCommentFragment();
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

        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_add_comment, container, false);

        apiInterface  = ApiClient.getClient().create(StanrzInterface.class);

        Bundle bundle = this.getArguments();

        getAllUsers();
        getProfile();

        if (bundle!=null)
        {
            strPostID = bundle.getString("postID");
        }

        binding.imgHeader.setOnClickListener(v -> getActivity().onBackPressed());

        setCommentEmojis();

        binding.tvSendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                strComment = binding.etComment.getText().toString();
                if(!strComment.equals(""))
                {
                    strComment = encodeEmoji(binding.etComment.getText().toString());
                    addComment();
                }
            }
        });

        commentAdapter = new CommentAdapter(getActivity(),commentList,usersList, new DeleteComment() {
            @Override
            public void deleteComment(String userId, String commentId) {

                deletePost(userId,commentId);

            }
        });

        binding.rvComments.setHasFixedSize(true);
        binding.rvComments.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.rvComments.setAdapter(commentAdapter);

        binding.etComment.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {

            }
            @Override
            public void afterTextChanged(Editable editable)
            {

                strComment = binding.etComment.getText().toString();
                String text = editable.toString();
                Pattern p = Pattern.compile("[@][a-zA-Z0-9-.]+");
                Matcher m = p.matcher(text);
                int cursorPosition = binding.etComment.getSelectionStart();
                ArrayList<SuccessResGetUser.Result> resultArrayList = new ArrayList<>();
                resultArrayList.clear();
                binding.rvTagComment.setVisibility(View.GONE);
                binding.rvTagComment.setLayoutManager(new LinearLayoutManager(getActivity()));
                binding.rvTagComment.setAdapter(new SearchTagPeopleAdapter(getActivity(), resultArrayList, true, new TaggedUserId() {
                    @Override
                    public void taggedId(int position) {

                    }
                }));

                while(m.find())
                {
                    if (cursorPosition >= m.start() && cursorPosition <= m.end())
                    {
                        final int s = m.start() + 1; // add 1 to ommit the "@" tag
                        final int e = m.end();
                        for (SuccessResGetUser.Result result:usersList)
                    {
                       if(result.getUsername().contains(text.substring(s, e)))
                       {
                           binding.rvTagComment.setVisibility(View.VISIBLE);
                           resultArrayList.add(result);
                       }
                    }

                        binding.rvTagComment.setLayoutManager(new LinearLayoutManager(getActivity()));
                        binding.rvTagComment.setAdapter(new SearchTagPeopleAdapter(getActivity(), resultArrayList, true, new TaggedUserId() {
                            @Override
                            public void taggedId(int position) {

                                int i=strComment.length();

                                i--;

                                while (i>=0)
                                {
                                    Character character= strComment.charAt(i);
                                    if(character.compareTo('@')==0)
                                    {
                                        int z= i+1;
                                        strComment =strComment.substring(0,z);
                                        break;
                                    }
                                    i--;
                                }

                                strComment = strComment + resultArrayList.get(position).getUsername();

                                binding.etComment.setText(strComment);

                                binding.etComment.setSelection(binding.etComment.getText().length());

                                resultArrayList.clear();
                                binding.rvTagComment.setLayoutManager(new LinearLayoutManager(getActivity()));
                                binding.rvTagComment.setAdapter(new SearchTagPeopleAdapter(getActivity(), resultArrayList, true, new TaggedUserId() {
                                    @Override
                                    public void taggedId(int position) {

                                    }
                                }));
                            }
                        }));
                        break;
                    }
                    else
                    {
                        resultArrayList.clear();
                        binding.rvTagComment.setLayoutManager(new LinearLayoutManager(getActivity()));
                        binding.rvTagComment.setAdapter(new SearchTagPeopleAdapter(getActivity(), resultArrayList, true, new TaggedUserId() {
                            @Override
                            public void taggedId(int position) {

                            }
                        }));
                    }
                }
            }
        });
        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void setCommentEmojis()
    {

        int unicode1 = 0x1F60A;
        int unicode2 = 0x1F60D;
        int unicode3 = 0x1F525;
        int unicode4 = 0x1F618;

         emoji1 = getEmojiByUnicode(unicode1);

         emoji2 = getEmojiByUnicode(unicode2);

         emoji3 = getEmojiByUnicode(unicode3);

         emoji4 = getEmojiByUnicode(unicode4);

        binding.tvEmoji1.setText(emoji1);
        binding.tvEmoji2.setText(emoji2);
        binding.tvEmoji3.setText(emoji3);
        binding.tvEmoji4.setText(emoji4);

        binding.tvEmoji1.setOnClickListener(v ->
                {
                    strComment = strComment+" "+emoji1;
                    binding.etComment.setText(strComment);
                }
                );

        binding.tvEmoji2.setOnClickListener(v ->
                {
                    strComment = strComment+" "+emoji2;
                    binding.etComment.setText(strComment);
                }
        );

        binding.tvEmoji3.setOnClickListener(v ->
                {
                    strComment = strComment+" "+emoji3;
                    binding.etComment.setText(strComment);
                }
        );

        binding.tvEmoji4.setOnClickListener(v ->
                {
                    strComment = strComment+" "+emoji4;
                    binding.etComment.setText(strComment);
                }
        );
    }

    private void getComment() {

        DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));
        Map<String,String> map = new HashMap<>();
        map.put("post_id",strPostID);

        Call<SuccessResGetComment> call = apiInterface.getComments(map);

        call.enqueue(new Callback<SuccessResGetComment>() {
            @Override
            public void onResponse(Call<SuccessResGetComment> call, Response<SuccessResGetComment> response) {
                DataManager.getInstance().hideProgressMessage();
                try {
                    SuccessResGetComment data = response.body();
                    Log.e("data",data.status);
                    if (data.status.equals("1")) {
                        String dataResponse = new Gson().toJson(response.body());
                        Log.e("MapMap", "EDIT PROFILE RESPONSE" + dataResponse);
                        strComment = "";
                        commentList.clear();
                        commentList.addAll(data.getResult());
                        commentAdapter.notifyDataSetChanged();
                    } else if (data.status.equals("0")) {
                        commentList.clear();
                        commentAdapter.notifyDataSetChanged();
                        showToast(getActivity(),data.message);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<SuccessResGetComment> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }
        });
    }

    public String getEmojiByUnicode(int unicode){
        return new String(Character.toChars(unicode));
    }

    private void addComment() {

        taggedUsersList.clear();

        String text = binding.etComment.getText().toString();

        SpannableStringBuilder ss = new SpannableStringBuilder(text);

        Pattern p = Pattern.compile("[@][a-zA-Z0-9-.]+");

        Matcher m = p.matcher(ss);

        while(m.find())
        {
            final int s = m.start() + 1; // add 1 to omit the "@" tag
            final int e = m.end();

            int i=0;

            for (SuccessResGetUser.Result result:usersList)
            {
                if(result.getUsername().equalsIgnoreCase(text.substring(s, e)))
                {

//                    resultArrayList.add(result);

                    taggedUsersList.add(result);

                }
                i++;
            }
        }

        String strTaggedUserId = "";

        for (SuccessResGetUser.Result result:taggedUsersList)
        {
            if(strComment.contains(result.getUsername()))
            {
                strTaggedUserId = strTaggedUserId + result.getId()+",";
            }

        }

        if (strTaggedUserId.endsWith(","))
        {
            strTaggedUserId = strTaggedUserId.substring(0, strTaggedUserId.length() - 1);
        }

        String userId = SharedPreferenceUtility.getInstance(getContext()).getString(USER_ID);
        DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));
        Map<String,String> map = new HashMap<>();
        map.put("user_id",userId);
        map.put("post_id",strPostID);
        map.put("comment",strComment);
        map.put("taguser",strTaggedUserId);
        Call<ResponseBody> call = apiInterface.addComment(map);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                DataManager.getInstance().hideProgressMessage();

                try {
//                    SuccessResAddComment data = response.body();

                    JSONObject jsonObject = new JSONObject(response.body().string());

                    String data = jsonObject.getString("status");

                    String message = jsonObject.getString("message");

                    if (data.equals("1")) {

                        String dataResponse = new Gson().toJson(response.body());

                        Log.e("MapMap", "EDIT PROFILE RESPONSE" + dataResponse);

                        binding.etComment.setText("");

                        getComment();

                    } else if (data.equals("0")) {

                        showToast(getActivity(),message);
                    }
                } catch (Exception e) {

                    binding.etComment.setText("");
                    getComment();
                    Log.d(TAG, "onResponse: "+e);
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
                binding.etComment.setText("");
                getComment();
            }
        });
    }

    private void getAllUsers()
    {

        String userId = SharedPreferenceUtility.getInstance(getContext()).getString(USER_ID);
        DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));
        Map<String,String> map = new HashMap<>();
        map.put("user_id", userId);
        Call<SuccessResGetUser> call = apiInterface.getAllNew(map);

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
                        getComment();
                    } else if (data.status.equals("0")) {
                        showToast(getActivity(), data.message);
                        usersList.clear();
                        getComment();
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
    public void onPause() {
        super.onPause();
        timer.cancel();
    }

    public static String encodeEmoji (String message) {
        try {
            return URLEncoder.encode(message,
                    "UTF-8");
        } catch (UnsupportedEncodingException e) {
            return message;
        }
    }

    private void deletePost(String postId,String commentId)
    {

        String userId = SharedPreferenceUtility.getInstance(getContext()).getString(USER_ID);
        DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));
        Map<String,String> map = new HashMap<>();
        map.put("user_id",postId);
        map.put("comment_id",commentId);

        Call<SuccessResDeleteComment> call = apiInterface.deleteComment(map);

        call.enqueue(new Callback<SuccessResDeleteComment>() {
            @Override
            public void onResponse(Call<SuccessResDeleteComment> call, Response<SuccessResDeleteComment> response) {
                DataManager.getInstance().hideProgressMessage();
                try {
                    SuccessResDeleteComment data = response.body();
                    Log.e("data",data.status);
                    if (data.status.equals("1")) {
                        String dataResponse = new Gson().toJson(response.body());
                        Log.e("MapMap", "EDIT PROFILE RESPONSE" + dataResponse);
                        showToast(getActivity(), data.message);
                      getComment();

                    } else if (data.status.equals("0")) {
                        showToast(getActivity(), data.message);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<SuccessResDeleteComment> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }
        });
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

                        Glide
                                .with(getActivity())
                                .load(userDetail.getImage())
                                .centerCrop()
                                .into(binding.ivUserProfile);


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


}