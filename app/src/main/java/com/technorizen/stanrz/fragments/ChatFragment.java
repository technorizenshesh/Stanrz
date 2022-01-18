package com.technorizen.stanrz.fragments;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.technorizen.stanrz.R;
import com.technorizen.stanrz.adapters.MessageAdapter;
import com.technorizen.stanrz.adapters.StoriesAdapter;
import com.technorizen.stanrz.adapters.UploadsAdapter;
import com.technorizen.stanrz.databinding.FragmentChatBinding;
import com.technorizen.stanrz.models.SuccessResAddLike;
import com.technorizen.stanrz.models.SuccessResGetConversation;
import com.technorizen.stanrz.models.SuccessResGetStories;
import com.technorizen.stanrz.models.SuccessResReportUser;
import com.technorizen.stanrz.retrofit.ApiClient;
import com.technorizen.stanrz.retrofit.StanrzInterface;
import com.technorizen.stanrz.utility.BottomSheet;
import com.technorizen.stanrz.utility.DataManager;
import com.technorizen.stanrz.utility.ReportInterface;
import com.technorizen.stanrz.utility.SharedPreferenceUtility;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import io.perfmark.Link;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.ContentValues.TAG;
import static com.technorizen.stanrz.retrofit.Constant.USER_ID;
import static com.technorizen.stanrz.retrofit.Constant.showToast;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ChatFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChatFragment extends Fragment implements ReportInterface {

    FragmentChatBinding binding;

    private BottomSheet bottomSheetFragment;

    private ArrayList<SuccessResGetConversation.Result> conversationList = new ArrayList<>();

    StanrzInterface apiInterface;

    private String type = "All";

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ChatFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ChatFragment.
     */

    // TODO: Rename and change types and number of parameters
    public static ChatFragment newInstance(String param1, String param2) {
        ChatFragment fragment = new ChatFragment();
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

        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_chat, container, false);

        apiInterface = ApiClient.getClient().create(StanrzInterface.class);

        binding.header.imgHeader.setOnClickListener(v ->
                {
                    getActivity().onBackPressed();
                }
                );

        binding.header.tvHeader.setText(R.string.messsages);
        getConversations();

        binding.etSearch.setInputType(InputType.TYPE_NULL);
        binding.etSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("search","my");
                Navigation.findNavController(v).navigate(R.id.action_chatFragment_to_searchUsersFragment,bundle);
            }
        });

        binding.cvAll.setOnClickListener(v ->
                {

                    type = "All";
                    getConversations();

                    binding.tvAll.setBackgroundResource(R.drawable.ic_header_bg);
                    binding.tvAll.setTextColor(getResources().getColor(R.color.white));

                    binding.tvFan.setBackgroundResource(0);
                    binding.tvFan.setTextColor(getResources().getColor(R.color.black));

                    binding.tvFollowing.setBackgroundResource(0);
                    binding.tvFollowing.setTextColor(getResources().getColor(R.color.black));

                }
        );

        binding.cvFollowing.setOnClickListener(v ->
                {

                    type = "Following";
                    getConversations();
                    binding.tvFollowing.setBackgroundResource(R.drawable.ic_header_bg);
                    binding.tvFollowing.setTextColor(getResources().getColor(R.color.white));

                    binding.tvFan.setBackgroundResource(0);
                    binding.tvFan.setTextColor(getResources().getColor(R.color.black));

                    binding.tvAll.setBackgroundResource(0);
                    binding.tvAll.setTextColor(getResources().getColor(R.color.black));

                }
        );

        binding.cvFan.setOnClickListener(v ->
                {

                    type = "Fan";

                    getConversations();

                    binding.tvFan.setBackgroundResource(R.drawable.ic_header_bg);
                    binding.tvFan.setTextColor(getResources().getColor(R.color.white));

                    binding.tvFollowing.setBackgroundResource(0);
                    binding.tvFollowing.setTextColor(getResources().getColor(R.color.black));

                    binding.tvAll.setBackgroundResource(0);
                    binding.tvAll.setTextColor(getResources().getColor(R.color.black));

                }
        );

        return binding.getRoot();
    }

    private void getConversations() {

        String userId = SharedPreferenceUtility.getInstance(getContext()).getString(USER_ID);
        DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));
        Map<String,String> map = new HashMap<>();
        map.put("receiver_id",userId);
        map.put("type",type);

        Call<SuccessResGetConversation> call = apiInterface.getConversations(map);

        call.enqueue(new Callback<SuccessResGetConversation>() {
            @Override
            public void onResponse(Call<SuccessResGetConversation> call, Response<SuccessResGetConversation> response) {

                DataManager.getInstance().hideProgressMessage();
                try {
                    SuccessResGetConversation data = response.body();

                    if (data.status.equalsIgnoreCase("1")) {
                        String dataResponse = new Gson().toJson(response.body());
                        Log.e("MapMap", "EDIT PROFILE RESPONSE" + dataResponse);
                        conversationList.clear();

                        for(SuccessResGetConversation.Result conversation:data.getResult())
                        {
                            if(conversation.getBlockUser().equalsIgnoreCase("Unblock") && conversation.getSenderAccount().equalsIgnoreCase("Active"))
                            {
                                conversationList.add(conversation);
                            }
                        }

                        binding.rvMessageItem.setHasFixedSize(true);
                        binding.rvMessageItem.setLayoutManager(new LinearLayoutManager(getActivity()));
                        binding.rvMessageItem.setAdapter(new MessageAdapter(getActivity(),conversationList,ChatFragment.this));

                    } else if (data.status.equalsIgnoreCase("0")) {
                        showToast(getActivity(), data.message);
                        conversationList.clear();
                        binding.rvMessageItem.setHasFixedSize(true);
                        binding.rvMessageItem.setLayoutManager(new LinearLayoutManager(getActivity()));
                        binding.rvMessageItem.setAdapter(new MessageAdapter(getActivity(),conversationList,ChatFragment.this));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<SuccessResGetConversation> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
                Log.d(TAG, "onFailure: "+t);

                conversationList.clear();
                binding.rvMessageItem.setHasFixedSize(true);
                binding.rvMessageItem.setLayoutManager(new LinearLayoutManager(getActivity()));
                binding.rvMessageItem.setAdapter(new MessageAdapter(getActivity(),conversationList,ChatFragment.this));

            }
        });
    }

    String otherUserId = "";

    @Override
    public void onReport(String content, String userId1) {

        String userId = SharedPreferenceUtility.getInstance(getContext()).getString(USER_ID);
        if(userId.equalsIgnoreCase(content))
        {
            otherUserId = userId1;
        }else
        {
            otherUserId = content;
        }

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

    @Override
    public void deleteChat(String userId) {

        String myUserId = SharedPreferenceUtility.getInstance(getContext()).getString(USER_ID);
        DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));
        Map<String,String> map = new HashMap<>();
        map.put("sender_id",myUserId);
        map.put("receiver_id",userId);

        Call<SuccessResAddLike> call = apiInterface.deleteChat(map);

        call.enqueue(new Callback<SuccessResAddLike>() {
            @Override
            public void onResponse(Call<SuccessResAddLike> call, Response<SuccessResAddLike> response) {

                DataManager.getInstance().hideProgressMessage();

                try {
                    SuccessResAddLike data = response.body();
                    if (data.status.equalsIgnoreCase("1")) {
                        String dataResponse = new Gson().toJson(response.body());
                        Log.e("MapMap", "EDIT PROFILE RESPONSE" + dataResponse);

                        getConversations();

                    } else if (data.status.equalsIgnoreCase("0")) {
                        showToast(getActivity(), data.result);
                             }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<SuccessResAddLike> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
                Log.d(TAG, "onFailure: "+t);
            }
        });

    }

    private String blockOtherUser = "";

    @Override
    public void blockUser(String content,String userId1) {

        String userId = SharedPreferenceUtility.getInstance(getContext()).getString(USER_ID);

        if(userId.equalsIgnoreCase(content))
        {
            blockOtherUser = userId1;
        }else
        {
            blockOtherUser = content;
        }

        blockUser(blockOtherUser);
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
                    getConversations();
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

}