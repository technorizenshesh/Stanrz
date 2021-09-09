package com.technorizen.stanrz.fragments;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSmoothScroller;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.technorizen.stanrz.R;
import com.technorizen.stanrz.adapters.CommentAdapter;
import com.technorizen.stanrz.databinding.FragmentAddCommentBinding;
import com.technorizen.stanrz.models.SuccessResAddComment;
import com.technorizen.stanrz.models.SuccessResGetComment;
import com.technorizen.stanrz.models.SuccessResProfileData;
import com.technorizen.stanrz.retrofit.ApiClient;
import com.technorizen.stanrz.retrofit.StanrzInterface;
import com.technorizen.stanrz.utility.DataManager;
import com.technorizen.stanrz.utility.SharedPreferenceUtility;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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

    private List<SuccessResGetComment.Result> commentList = new LinkedList<>();

    private String emoji1 = "", emoji2= "",emoji3="",emoji4="";

    private String strPostID ="";

    private String strComment = "";

    Timer timer = new Timer();

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
        // Inflate the layout for this fragment

        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_add_comment, container, false);

        apiInterface  = ApiClient.getClient().create(StanrzInterface.class);

        Bundle bundle = this.getArguments();

        if (bundle!=null)
        {
            strPostID = bundle.getString("postID");
        }
        binding.imgHeader.setOnClickListener(v -> getActivity().onBackPressed());
        getComment();

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

        binding.etComment.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                strComment = binding.etComment.getText().toString();


            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                strComment = binding.etComment.getText().toString();


            }

            @Override
            public void afterTextChanged(Editable s) {
                strComment = binding.etComment.getText().toString();

            }
        });

       /* timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {

                getComment();

            }
        },0,5000);
*/
        return binding.getRoot();
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

      /*  RequestBody email = RequestBody.create(MediaType.parse("text/plain"),strEmail);
        RequestBody password = RequestBody.create(MediaType.parse("text/plain"), strPassword);
        RequestBody registerID = RequestBody.create(MediaType.parse("text/plain"),deviceToken);
*/
//        Call<SuccessResSignIn> call = apiInterface.login(email,password,registerID);
        Call<SuccessResGetComment> call = apiInterface.getComments(map);

        call.enqueue(new Callback<SuccessResGetComment>() {
            @Override
            public void onResponse(Call<SuccessResGetComment> call, Response<SuccessResGetComment> response) {

                DataManager.getInstance().hideProgressMessage();

                try {
                    SuccessResGetComment data = response.body();
                    //                    setSellerData();
                    Log.e("data",data.status);
                    if (data.status.equals("1")) {
                        String dataResponse = new Gson().toJson(response.body());
                        Log.e("MapMap", "EDIT PROFILE RESPONSE" + dataResponse);

                        strComment = "";

                        commentList.clear();
                        commentList.addAll(data.getResult());
                        binding.rvComments.setHasFixedSize(true);
                        binding.rvComments.setLayoutManager(new LinearLayoutManager(getActivity()));
                        binding.rvComments.setAdapter(new CommentAdapter(getActivity(),commentList));

//                        SessionManager.writeString(RegisterAct.this, Constant.driver_id,data.result.id);
//                        App.showToast(RegisterAct.this, data.message, Toast.LENGTH_SHORT);
                    } else if (data.status.equals("0")) {
                //        showToast(getActivity(), data.message);
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

        String userId = SharedPreferenceUtility.getInstance(getContext()).getString(USER_ID);
        DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));
        Map<String,String> map = new HashMap<>();
        map.put("user_id",userId);
        map.put("post_id",strPostID);
        map.put("comment",strComment);

      /*  RequestBody email = RequestBody.create(MediaType.parse("text/plain"),strEmail);
        RequestBody password = RequestBody.create(MediaType.parse("text/plain"), strPassword);
        RequestBody registerID = RequestBody.create(MediaType.parse("text/plain"),deviceToken);
*/
//        Call<SuccessResSignIn> call = apiInterface.login(email,password,registerID);
        Call<SuccessResAddComment> call = apiInterface.addComment(map);

        call.enqueue(new Callback<SuccessResAddComment>() {
            @Override
            public void onResponse(Call<SuccessResAddComment> call, Response<SuccessResAddComment> response) {

                DataManager.getInstance().hideProgressMessage();

                try {
                    SuccessResAddComment data = response.body();
                    //                    setSellerData();
                    Log.e("data",data.status);
                    if (data.status.equals("1")) {
                        String dataResponse = new Gson().toJson(response.body());
                        Log.e("MapMap", "EDIT PROFILE RESPONSE" + dataResponse);

                        binding.etComment.setText("");
                        getComment();

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
            public void onFailure(Call<SuccessResAddComment> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
                binding.etComment.setText("");
                getComment();

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


}