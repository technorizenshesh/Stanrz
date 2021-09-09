package com.technorizen.stanrz.fragments;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.braintreepayments.cardform.OnCardFormSubmitListener;
import com.braintreepayments.cardform.view.CardForm;
import com.erikagtierrez.multiple_media_picker.Gallery;
import com.google.gson.Gson;
import com.technorizen.stanrz.R;
import com.technorizen.stanrz.adapters.OtherUserFollowingAdapter;
import com.technorizen.stanrz.adapters.UploadPostsAdapter;
import com.technorizen.stanrz.databinding.FragmentAddPostBinding;
import com.technorizen.stanrz.models.SuccessResGetFollowings;
import com.technorizen.stanrz.models.SuccessResUploadCoverImage;
import com.technorizen.stanrz.models.SuccessResUploadPost;
import com.technorizen.stanrz.retrofit.ApiClient;
import com.technorizen.stanrz.retrofit.StanrzInterface;
import com.technorizen.stanrz.utility.DataManager;
import com.technorizen.stanrz.utility.SharedPreferenceUtility;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

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
 * Use the {@link AddPostFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddPostFragment extends Fragment {

    private Dialog dialog;

    private StanrzInterface apiInterface;

    FragmentAddPostBinding binding;
    private static final int MY_PERMISSION_CONSTANT = 5;

    public static int OPEN_MEDIA_PICKER = 10;
    public static int IMAGE_PICKER_SELECT = 20;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AddPostFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddPostFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddPostFragment newInstance(String param1, String param2) {
        AddPostFragment fragment = new AddPostFragment();
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

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_post, container, false);

        apiInterface = ApiClient.getClient().create(StanrzInterface.class);

        binding.ivPost.setOnClickListener(v -> {
            if (checkPermisssionForReadStorage())
                showImageSelection();
        });

        return binding.getRoot();

    }

    //CHECKING FOR Camera STATUS
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

            // Should we show an explanation?
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

                //explain("Please Allow Location Permission");
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSION_CONSTANT);
            }
            return false;
        } else {

            //  explain("Please Allow Location Permission");
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
                // return;
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
            intent.putExtra("mode",2);
            intent.putExtra("maxSelection",3); // Optional
            startActivityForResult(intent,OPEN_MEDIA_PICKER);
        }

      /*  Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickIntent.setType("image/* video/*");
        startActivityForResult(pickIntent, IMAGE_PICKER_SELECT);*/

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to

     /*   if (resultCode == RESULT_OK) {
            Uri selectedMediaUri = data.getData();
            if (selectedMediaUri.toString().contains("image")) {
                //handle image
            } else if (selectedMediaUri.toString().contains("video")) {
                //handle video
            }*/

        if (requestCode == OPEN_MEDIA_PICKER) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK && data != null) {
                ArrayList<String> selectionResult=data.getStringArrayListExtra("result");

                int size = selectionResult.size();


                if(selectionResult.size()>0)
                {
                    String selectedMediaUri = selectionResult.get(0);
                    if(selectedMediaUri.endsWith("jpg") || selectedMediaUri.endsWith("jpeg") || selectedMediaUri.endsWith("png")){

                        fullScreenDialog(selectionResult);

                    }

                }


                Toast.makeText(getActivity(),""+size,Toast.LENGTH_SHORT).show();

            }
        }

        }


    private void fullScreenDialog(ArrayList<String> selectionResult) {

        dialog = new Dialog(getActivity(), WindowManager.LayoutParams.MATCH_PARENT);

        dialog.setContentView(R.layout.upload_post_dialog);

        AppCompatButton btnSubmit =  dialog.findViewById(R.id.btnSubmit);

        RecyclerView rvUploading;

        rvUploading = dialog.findViewById(R.id.rvimages_videos);

        ImageView ivBack;
        ivBack = dialog.findViewById(R.id.img_header);

        rvUploading.setHasFixedSize(true);
        rvUploading.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false));
        rvUploading.setAdapter(new UploadPostsAdapter(getActivity(),selectionResult));

        btnSubmit.setOnClickListener(v ->
                {

                    uploadPost(selectionResult);

                }
        );

        ivBack.setOnClickListener(v ->
                {
                    dialog.dismiss();
                }
        );

        dialog.show();

    }

    public void uploadPost(ArrayList<String> imagesVideosPathList)
    {

        String strUserId = SharedPreferenceUtility.getInstance(getContext()).getString(USER_ID);

        DataManager.getInstance().showProgressMessage(getActivity(),getString(R.string.please_wait));

        List<MultipartBody.Part> filePartList = new LinkedList<>();

        for (int i=0;i<imagesVideosPathList.size();i++)
        {
            File file = DataManager.getInstance().saveBitmapToFile(new File(imagesVideosPathList.get(i)));
            filePartList.add(MultipartBody.Part.createFormData("image[]", file.getName(), RequestBody.create(MediaType.parse("image[]/*"), file)));
        }

        RequestBody userId = RequestBody.create(MediaType.parse("text/plain"), strUserId);
        RequestBody caption = RequestBody.create(MediaType.parse("text/plain"), "Test");
        RequestBody comment = RequestBody.create(MediaType.parse("text/plain"), "Test");

        Call<SuccessResUploadPost> loginCall = apiInterface.uploadPost(userId,caption,comment,filePartList);
        loginCall.enqueue(new Callback<SuccessResUploadPost>() {
            @Override
            public void onResponse(Call<SuccessResUploadPost> call, Response<SuccessResUploadPost> response) {
                DataManager.getInstance().hideProgressMessage();
                try {

                    SuccessResUploadPost data = response.body();
//                    setSellerData();
                    Log.e("data",data.status);
                    if (data.status.equals("1")) {
                        String dataResponse = new Gson().toJson(response.body());
                        Log.e("MapMap", "EDIT PROFILE RESPONSE" + dataResponse);
                        showToast(getActivity(), data.message);
                        dialog.dismiss();
//                        SessionManager.writeString(RegisterAct.this, Constant.driver_id,data.result.id);
//                        App.showToast(RegisterAct.this, data.message, Toast.LENGTH_SHORT);
                    } else if (data.status.equals("0")) {
                        showToast(getActivity(), data.message);

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e(TAG,"Test Response :"+response.body());
                    dialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<SuccessResUploadPost> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
                dialog.dismiss();
            }
        });

    }


}

/*

    private void requestUploadSurvey () {
        File propertyImageFile = new File(surveyModel.getPropertyImagePath());
        RequestBody propertyImage = RequestBody.create(MediaType.parse("image/*"),
                propertyImageFile);
        MultipartBody.Part propertyImagePart = MultipartBody.Part.createFormData("PropertyImage",
                propertyImageFile.getName(),
                propertyImage);

        MultipartBody.Part[] surveyImagesParts = new MultipartBody.Part[surveyModel.getPicturesList()
                .size()];

        for (int index = 0; index <
                surveyModel.getPicturesList()
                        .size(); index++) {
            Log.d(TAG,
                    "requestUploadSurvey: survey image " +
                            index +
                            "  " +
                            surveyModel.getPicturesList()
                                    .get(index)
                                    .getImagePath());
            File file = new File(surveyModel.getPicturesList()
                    .get(index)
                    .getImagePath());
            RequestBody surveyBody = RequestBody.create(MediaType.parse("image/*"),
                    file);
            surveyImagesParts[index] = MultipartBody.Part.createFormData("SurveyImage",
                    file.getName(),
                    surveyBody);
        }

        final WebServicesAPI webServicesAPI = RetrofitManager.getInstance()
                .getRetrofit()
                .create(WebServicesAPI.class);
        Call<UploadSurveyResponseModel> surveyResponse = null;
        if (surveyImagesParts != null) {
            surveyResponse = webServicesAPI.uploadSurvey(surveyImagesParts,
                    propertyImagePart,
                    draBody);
        }
        surveyResponse.enqueue(this);
    }

*/

