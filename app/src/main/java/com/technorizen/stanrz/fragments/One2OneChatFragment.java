package com.technorizen.stanrz.fragments;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.erikagtierrez.multiple_media_picker.Gallery;
import com.google.gson.Gson;
import com.technorizen.stanrz.R;
import com.technorizen.stanrz.adapters.ChatAdapter;
import com.technorizen.stanrz.adapters.One2OneChatAdapter;
import com.technorizen.stanrz.databinding.FragmentOne2OneChatBinding;
import com.technorizen.stanrz.models.SuccessResGetChat;
import com.technorizen.stanrz.models.SuccessResInsertChat;
import com.technorizen.stanrz.models.SuccessResUploadPost;
import com.technorizen.stanrz.retrofit.ApiClient;
import com.technorizen.stanrz.retrofit.NetworkAvailablity;
import com.technorizen.stanrz.retrofit.StanrzInterface;
import com.technorizen.stanrz.utility.DataManager;
import com.technorizen.stanrz.utility.SharedPreferenceUtility;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;
import static com.technorizen.stanrz.retrofit.Constant.USER_ID;
import static com.technorizen.stanrz.retrofit.Constant.showToast;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link One2OneChatFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class One2OneChatFragment extends Fragment {

    FragmentOne2OneChatBinding binding;

    StanrzInterface apiInterface;

    static final int REQUEST_VIDEO_CAPTURE = 125;

    String str_image_path="";
    String str_video_path="";
    ChatAdapter chatAdapter;

    public static int OPEN_MEDIA_PICKER = 10;

    private static final int REQUEST_CAMERA = 191;

    private String name = "",id = "", image ="",strChatMessage = "";

    List<SuccessResGetChat.Result> chatList = new LinkedList<>();

    Timer timer = new Timer();

    private static final int MY_PERMISSION_CONSTANT = 5;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public One2OneChatFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment One2OneChatFragment.
     */

    // TODO: Rename and change types and number of parameters
    public static One2OneChatFragment newInstance(String param1, String param2) {
        One2OneChatFragment fragment = new One2OneChatFragment();
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

        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_one2_one_chat, container, false);

        apiInterface = ApiClient.getClient().create(StanrzInterface.class);

        Bundle bundle = this.getArguments();

        if (bundle!=null)
        {
             name = bundle.getString("name");
             id = bundle.getString("id");
             image = bundle.getString("image");
         }

        binding.imgHeader.setOnClickListener(v -> getActivity().onBackPressed());

        Glide
                .with(getActivity())
                .load(image)
                .centerCrop()
                .into(binding.ivProfile);

        binding.tvHeader.setText(name);

        String userId = SharedPreferenceUtility.getInstance(getContext()).getString(USER_ID);

        chatAdapter = new ChatAdapter(getContext(),chatList,userId);
        binding.rvMessageItem.setHasFixedSize(true);
        binding.rvMessageItem.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.rvMessageItem.setAdapter(chatAdapter);

        if (NetworkAvailablity.getInstance(getActivity()).checkNetworkStatus()) {
            getChat();
        } else {
            Toast.makeText(getActivity(), getResources().getString(R.string.msg_noInternet), Toast.LENGTH_SHORT).show();
        }

        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if(isLastVisible()){
                    getChat();
                }
            }
        },0,5000);

        binding.ivSendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                strChatMessage = binding.etChat.getText().toString();

                if(!strChatMessage.equals(""))
                {
                    uploadImageVideoPost("","text");
                }
            }
        });

        binding.ivPicker.setOnClickListener(v ->
                {
                    if(checkPermisssionForReadStorage())
                    {
                        Intent intent= new Intent(getActivity(), Gallery.class);
                        // Set the title
                        intent.putExtra("title","Select media");
                        // Mode 1 for both images and videos selection, 2 for images only and 3 for videos!
                        intent.putExtra("mode",1);
                        intent.putExtra("maxSelection",1); // Optional
                        startActivityForResult(intent,OPEN_MEDIA_PICKER);
                    }
                }
                );

        return binding.getRoot();
    }

    public boolean checkPermisssionForReadStorage() {
        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED

                ||

                ContextCompat.checkSelfPermission(getActivity(),
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED
                ||

                ContextCompat.checkSelfPermission(getActivity(),
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED
        ) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.CAMERA)

                    ||

                    ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                            Manifest.permission.READ_EXTERNAL_STORAGE)
                    ||
                    ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                            Manifest.permission.WRITE_EXTERNAL_STORAGE)

            ) {
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSION_CONSTANT);
            } else {
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSION_CONSTANT);
            }
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {

            case MY_PERMISSION_CONSTANT: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0) {
                    boolean camera = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean read_external_storage = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    boolean write_external_storage = grantResults[2] == PackageManager.PERMISSION_GRANTED;
                    if (camera && read_external_storage && write_external_storage) {
                        showImageSelection();
                    } else {
                        Toast.makeText(getActivity(), getResources().getString(R.string.permission_denied_boo), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getActivity(), getResources().getString(R.string.permission_denied_boo), Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    public void showImageSelection()
    {

        if(checkPermisssionForReadStorage())
        {
            Intent intent= new Intent(getActivity(), Gallery.class);
            // Set the title
            intent.putExtra("title","Select media");
            // Mode 1 for both images and videos selection, 2 for images only and 3 for videos!
            intent.putExtra("mode",1);
            intent.putExtra("maxSelection",3); // Optional
            startActivityForResult(intent,OPEN_MEDIA_PICKER);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        timer.cancel();
    }

    private boolean isLastVisible() {

        if (chatList != null && chatList.size() != 0) {
            LinearLayoutManager layoutManager = ((LinearLayoutManager) binding.rvMessageItem.getLayoutManager());
            int pos = layoutManager.findLastCompletelyVisibleItemPosition();
            int numItems = binding.rvMessageItem.getAdapter().getItemCount();
            return (pos >= numItems - 1);
        }
        return false;
    }

    private void getChat() {

        String userId = SharedPreferenceUtility.getInstance(getContext()).getString(USER_ID);

        Map<String,String> map = new HashMap<>();
        map.put("sender_id",userId);
        map.put("receiver_id",id);

        Call<SuccessResGetChat> call = apiInterface.getChat(map);
        call.enqueue(new Callback<SuccessResGetChat>() {
            @Override
            public void onResponse(Call<SuccessResGetChat> call, Response<SuccessResGetChat> response) {

                DataManager.getInstance().hideProgressMessage();
                try {
                    SuccessResGetChat data = response.body();
                    Log.e("data",data.status);
                    if (data.status.equals("1")) {

                        String dataResponse = new Gson().toJson(response.body());
                        chatList.clear();
                        chatList.addAll(data.getResult());
                        binding.rvMessageItem.setLayoutManager(new LinearLayoutManager(getContext()));
                        binding.rvMessageItem.setAdapter(chatAdapter);
                        binding.rvMessageItem.scrollToPosition(chatList.size()-1);

                    } else if (data.status.equals("0")) {
                        showToast(getActivity(), data.message);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<SuccessResGetChat> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == OPEN_MEDIA_PICKER) {

            if (resultCode == RESULT_OK && data != null) {
                ArrayList<String> selectionResult=data.getStringArrayListExtra("result");

                int size = selectionResult.size();

                if(selectionResult.size()>0)
                {
                    String selectedMediaUri = selectionResult.get(0);
                    if(selectedMediaUri.endsWith("jpg") || selectedMediaUri.endsWith("jpeg") || selectedMediaUri.endsWith("png")){

                        uploadImageVideoPost(selectionResult.get(0),"image");

                    } else
                    {
                        uploadImageVideoPost(selectionResult.get(0),"video");
                    }

                }

                Toast.makeText(getActivity(),""+size,Toast.LENGTH_SHORT).show();

            }
        }else if (requestCode == REQUEST_CAMERA && resultCode == RESULT_OK) {

            ArrayList<String> selectionResult= new ArrayList<>();
            selectionResult.add(str_image_path);

        }else if (requestCode == REQUEST_VIDEO_CAPTURE && resultCode == RESULT_OK) {
            ArrayList<String> selectionResult= new ArrayList<>();
            selectionResult.add(str_video_path);
        }
    }

    public void uploadImageVideoPost(String imageVideoPath,String type1)
    {

        String strUserId = SharedPreferenceUtility.getInstance(getContext()).getString(USER_ID);

        DataManager.getInstance().showProgressMessage(getActivity(),getString(R.string.please_wait));

        MultipartBody.Part filePart;

        if (!imageVideoPath.equalsIgnoreCase("")) {
            File file ;
            if(type1.equalsIgnoreCase("image"))
            {
                file = DataManager.getInstance().saveBitmapToFile(new File(imageVideoPath));
            }
            else
            {
                file = new File(imageVideoPath);
            }
            if(file!=null)
            {
                filePart = MultipartBody.Part.createFormData("image_video", file.getName(), RequestBody.create(MediaType.parse("image_video/*"), file));
            }
            else
            {
                filePart = null;
            }
        } else {
            RequestBody attachmentEmpty = RequestBody.create(MediaType.parse("text/plain"), "");
            filePart = MultipartBody.Part.createFormData("attachment", "", attachmentEmpty);
        }

        RequestBody senderId = RequestBody.create(MediaType.parse("text/plain"), strUserId);
        RequestBody receiverId = RequestBody.create(MediaType.parse("text/plain"), id);
        RequestBody messageText = RequestBody.create(MediaType.parse("text/plain"), strChatMessage);
        RequestBody type = RequestBody.create(MediaType.parse("text/plain"), type1);
        RequestBody caption = RequestBody.create(MediaType.parse("text/plain"), "");

        Call<SuccessResInsertChat> loginCall = apiInterface.insertImageVideoChat(senderId,receiverId,messageText,type,caption,filePart);
        loginCall.enqueue(new Callback<SuccessResInsertChat>() {
            @Override
            public void onResponse(Call<SuccessResInsertChat> call, Response<SuccessResInsertChat> response) {
                DataManager.getInstance().hideProgressMessage();
                try {

                    SuccessResInsertChat data = response.body();
                    Log.e("data",data.status);
                    if (data.status.equals("1")) {
                        String dataResponse = new Gson().toJson(response.body());
                        Log.e("MapMap", "EDIT PROFILE RESPONSE" + dataResponse);
                        showToast(getActivity(), data.message);
                        binding.etChat.setText("");
                        getChat();
                    } else if (data.status.equals("0")) {
                        showToast(getActivity(), data.message);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e(TAG,"Test Response :"+response.body());
                }
            }

            @Override
            public void onFailure(Call<SuccessResInsertChat> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
                getChat();
            }
        });
    }

}