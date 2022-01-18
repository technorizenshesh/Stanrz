package com.technorizen.stanrz.fragments;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

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
import com.technorizen.stanrz.databinding.FragmentApplyForFanAccountBinding;
import com.technorizen.stanrz.models.SuccessResApplyForFanClub;
import com.technorizen.stanrz.models.SuccessResGetPackages;
import com.technorizen.stanrz.models.SuccessResUpdateProfile;
import com.technorizen.stanrz.retrofit.ApiClient;
import com.technorizen.stanrz.retrofit.StanrzInterface;
import com.technorizen.stanrz.utility.DataManager;
import com.technorizen.stanrz.utility.RealPathUtil;
import com.technorizen.stanrz.utility.SharedPreferenceUtility;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
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
import static android.content.ContentValues.TAG;
import static com.technorizen.stanrz.retrofit.Constant.USER_ID;
import static com.technorizen.stanrz.retrofit.Constant.showToast;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ApplyForFanAccountFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ApplyForFanAccountFragment extends Fragment {

    FragmentApplyForFanAccountBinding binding;
    private static final int MY_PERMISSION_CONSTANT = 5;

    StanrzInterface apiInterface;

//    private static final int REQUEST_CAMERA = 1;

    private static final int REQUEST_CAMERA_PHOTO_ID = 11;
    private static final int REQUEST_CAMERA_PHOTO_ID_WITH = 12;

    private static final int SELECT_PHOTO_ID = 7;
    private static final int SELECT_PHOTO_ID_WITH = 8;

    private Uri uriSavedImage;

    private String strDescription = "Test";

    String str_image_path = "";
    String str_image_photoId_path = "";

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ApplyForFanAccountFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ApplyForFanAccountFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ApplyForFanAccountFragment newInstance(String param1, String param2) {
        ApplyForFanAccountFragment fragment = new ApplyForFanAccountFragment();
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

    public Bitmap BITMAP_RE_SIZER(Bitmap bitmap, int newWidth, int newHeight) {
        Bitmap scaledBitmap = Bitmap.createBitmap(newWidth, newHeight, Bitmap.Config.ARGB_8888);

        float ratioX = newWidth / (float) bitmap.getWidth();
        float ratioY = newHeight / (float) bitmap.getHeight();
        float middleX = newWidth / 2.0f;
        float middleY = newHeight / 2.0f;

        Matrix scaleMatrix = new Matrix();
        scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);

        Canvas canvas = new Canvas(scaledBitmap);
        canvas.setMatrix(scaleMatrix);
        canvas.drawBitmap(bitmap, middleX - bitmap.getWidth() / 2, middleY - bitmap.getHeight() / 2, new Paint(Paint.FILTER_BITMAP_FLAG));

        return scaledBitmap;

    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title_" + System.currentTimeMillis(), null);
        return Uri.parse(path);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_apply_for_fan_account, container, false);

        apiInterface = ApiClient.getClient().create(StanrzInterface.class);

        binding.header.tvHeader.setText(R.string.apply_for_verified_user);

        binding.header.imgHeader.setOnClickListener(view -> getActivity().onBackPressed());

        binding.tvPhotoId.setOnClickListener(v ->
                {
                    if(checkPermisssionForReadStorage())
                        showImageSelection(true);
                }
                );

        binding.tvPhotoIDWith.setOnClickListener(v ->
                {
                    if(checkPermisssionForReadStorage())
                        showImageSelection(false);
                }
        );

        binding.btnSubmit.setOnClickListener(v ->
                {
                    if(isValid())
                    {
                        apply();
                    }
                }
                );

        return binding.getRoot();
    }


    private boolean isValid() {
        if (str_image_path.equalsIgnoreCase("")) {
            showToast(getActivity(),"Please Upload Photo Id Image");
            return false;
        } else if (str_image_photoId_path.equalsIgnoreCase("")) {
            showToast(getActivity(),"Please Upload Picture With Photo Id");
            return false;
        }
        return true;
    }


    public void showImageSelection(boolean from) {

        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().getAttributes().windowAnimations = android.R.style.Widget_Material_ListPopupWindow;
        dialog.setContentView(R.layout.dialog_show_image_selection);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = dialog.getWindow();
        lp.copyFrom(window.getAttributes());
        //This makes the dialog take up the full width
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);
        LinearLayout layoutCamera = (LinearLayout) dialog.findViewById(R.id.layoutCemera);
        LinearLayout layoutGallary = (LinearLayout) dialog.findViewById(R.id.layoutGallary);
        layoutCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                dialog.cancel();
                if(from)
                {
                    openCamera(from);
                }
                else
                {
                    openCamera(from);
                }
            }
        });
        layoutGallary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                dialog.cancel();
                if(from)
                {
                    getPhotoFromGallary(from);
                }
                else
                {
                    getPhotoFromGallary(from);
                }
            }
        });
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    private void getPhotoFromGallary(boolean from) {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        if(from)
        {
            startActivityForResult(Intent.createChooser(intent, "Select Image"), SELECT_PHOTO_ID);
        }
        else
        {
            startActivityForResult(Intent.createChooser(intent, "Select Image"), SELECT_PHOTO_ID_WITH);
        }
    }

    private void openCamera(boolean from) {
//
//        File dirtostoreFile = new File(Environment.getExternalStorageDirectory() + "/Stanrz/Images/");
//
//        if (!dirtostoreFile.exists()) {
//            dirtostoreFile.mkdirs();
//        }
//
//        String timestr = DataManager.getInstance().convertDateToString(Calendar.getInstance().getTimeInMillis());
//
//        File tostoreFile = new File(Environment.getExternalStorageDirectory() + "/Stanrz/Images/" + "IMG_" + timestr + ".jpg");
//
//        if(from)
//        {
//            str_image_path = tostoreFile.getPath();
//        }
//        else
//        {
//            str_image_photoId_path = tostoreFile.getPath();
//        }
//
//        uriSavedImage = FileProvider.getUriForFile(getActivity(),
//                BuildConfig.APPLICATION_ID + ".provider",
//                tostoreFile);
//
//        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        intent.putExtra(MediaStore.EXTRA_OUTPUT, uriSavedImage);

        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if(from)
        {


            if (cameraIntent.resolveActivity(getActivity().getPackageManager()) != null)
                startActivityForResult(cameraIntent, REQUEST_CAMERA_PHOTO_ID);

        }
        else
        {

            if (cameraIntent.resolveActivity(getActivity().getPackageManager()) != null)
                startActivityForResult(cameraIntent, REQUEST_CAMERA_PHOTO_ID_WITH);

        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            Log.e("Result_code", requestCode + "");
            if (requestCode == SELECT_PHOTO_ID) {
//                str_image_path = DataManager.getInstance().getRealPathFromURI(getActivity(), data.getData());
//                Glide.with(getActivity())
//                        .load(str_image_path)
//                        .centerCrop()
//                        .into(binding.ivPhotoID);

                try {
                    Uri selectedImage = data.getData();
                    Bitmap bitmapNew = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedImage);
                    Bitmap bitmap = BITMAP_RE_SIZER(bitmapNew, bitmapNew.getWidth(), bitmapNew.getHeight());
                    Glide.with(getActivity())
                            .load(selectedImage)
                            .centerCrop()
                            .into(binding.ivPhotoID);
                    Uri tempUri = getImageUri(getActivity(), bitmap);
                    String image = RealPathUtil.getRealPath(getActivity(), tempUri);
                    str_image_path = image;

                } catch (IOException e) {
                    Log.i("TAG", "Some exception " + e);
                }

            }else if (requestCode == SELECT_PHOTO_ID_WITH) {


//                str_image_photoId_path = DataManager.getInstance().getRealPathFromURI(getActivity(), data.getData());
//                Glide.with(getActivity())
//                        .load(str_image_photoId_path)
//                        .centerCrop()
//                        .into(binding.ivProfile);

                try {
                    Uri selectedImage = data.getData();
                    Bitmap bitmapNew = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedImage);
                    Bitmap bitmap = BITMAP_RE_SIZER(bitmapNew, bitmapNew.getWidth(), bitmapNew.getHeight());
                    Glide.with(getActivity())
                            .load(selectedImage)
                            .centerCrop()
                            .into(binding.ivProfile);
                    Uri tempUri = getImageUri(getActivity(), bitmap);
                    String image = RealPathUtil.getRealPath(getActivity(), tempUri);
                    str_image_photoId_path = image;

                } catch (IOException e) {
                    Log.i("TAG", "Some exception " + e);
                }


            } else if (requestCode == REQUEST_CAMERA_PHOTO_ID) {
//                Glide.with(getActivity())
//                        .load(str_image_path)
//                        .centerCrop()
//                        .into(binding.ivPhotoID);

                try {
                    if (data != null) {
                        Bundle extras = data.getExtras();
                        Bitmap bitmapNew = (Bitmap) extras.get("data");
                        Bitmap imageBitmap = BITMAP_RE_SIZER(bitmapNew, bitmapNew.getWidth(), bitmapNew.getHeight());

                        Glide.with(getActivity())
                                .load(imageBitmap)
                                .centerCrop()
                                .into(binding.ivPhotoID);

                        Uri tempUri = getImageUri(getActivity(), imageBitmap);
                        String image = RealPathUtil.getRealPath(getActivity(), tempUri);
                        str_image_path = image;

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }else if (requestCode == REQUEST_CAMERA_PHOTO_ID_WITH) {
//                Glide.with(getActivity())
//                        .load(str_image_photoId_path)
//                        .centerCrop()
//                        .into(binding.ivProfile);

                try {
                    if (data != null) {
                        Bundle extras = data.getExtras();
                        Bitmap bitmapNew = (Bitmap) extras.get("data");
                        Bitmap imageBitmap = BITMAP_RE_SIZER(bitmapNew, bitmapNew.getWidth(), bitmapNew.getHeight());

                        Glide.with(getActivity())
                                .load(imageBitmap)
                                .centerCrop()
                                .into(binding.ivProfile);

                        Uri tempUri = getImageUri(getActivity(), imageBitmap);
                        String image = RealPathUtil.getRealPath(getActivity(), tempUri);
                        str_image_photoId_path = image;

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        }
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

                requestPermissions(
                        new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSION_CONSTANT);

            } else {

                //explain("Please Allow Location Permission");
                // No explanation needed, we can request the permission.
                requestPermissions(
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

            case 1000: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

/*
                    Log.e("Latittude====", gpsTracker.getLatitude() + "");
                    strLat = Double.toString(gpsTracker.getLatitude()) ;
                    strLng = Double.toString(gpsTracker.getLongitude()) ;
*/

//                    if (isContinue) {
//                        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//                            // TODO: Consider calling
//                            //    ActivityCompat#requestPermissions
//                            // here to request the missing permissions, and then overriding
//                            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//                            //                                          int[] grantResults)
//                            // to handle the case where the user grants the permission. See the documentation
//                            // for ActivityCompat#requestPermissions for more details.
//                            return;
//                        }
//                        mFusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);
//                    } else {
//                        Log.e("Latittude====", gpsTracker.getLatitude() + "");
//
//                        strLat = Double.toString(gpsTracker.getLatitude()) ;
//                        strLng = Double.toString(gpsTracker.getLongitude()) ;
//                    }
                } else {
                    Toast.makeText(getActivity(), "Permission denied", Toast.LENGTH_SHORT).show();
                }
                break;
            }

            case MY_PERMISSION_CONSTANT: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0) {
                    boolean camera = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean read_external_storage = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    boolean write_external_storage = grantResults[2] == PackageManager.PERMISSION_GRANTED;
                    if (camera && read_external_storage && write_external_storage) {
                      //  showImageSelection();
                    } else {
                        Toast.makeText(getActivity(), getResources().getString(R.string.permission_denied_boo), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getActivity(), getResources().getString(R.string.permission_denied_boo), Toast.LENGTH_SHORT).show();
                }
                break;
            }
        }
    }

    public void apply()
    {
        String userID = SharedPreferenceUtility.getInstance(getContext()).getString(USER_ID);
        DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));

        MultipartBody.Part filePart;
        if (!str_image_path.equalsIgnoreCase("")) {

            File file = DataManager.getInstance().saveBitmapToFile(new File(str_image_path));
            if(file!=null)
            {
                filePart = MultipartBody.Part.createFormData("photo_id", file.getName(), RequestBody.create(MediaType.parse("photo_id/*"), file));
            }
            else
            {
                filePart = null;
            }

        } else {
            RequestBody attachmentEmpty = RequestBody.create(MediaType.parse("text/plain"), "");
            filePart = MultipartBody.Part.createFormData("attachment", "", attachmentEmpty);
        }

        MultipartBody.Part filePartPhotoID;
        if (!str_image_photoId_path.equalsIgnoreCase("")) {
            File file = DataManager.getInstance().saveBitmapToFile(new File(str_image_photoId_path));
            if(file!=null)
            {
                filePartPhotoID = MultipartBody.Part.createFormData("selefie_with_photo_id", file.getName(), RequestBody.create(MediaType.parse("selefie_with_photo_id/*"), file));
            }
            else
            {
                filePartPhotoID = null;
            }

        } else {
            RequestBody attachmentEmpty = RequestBody.create(MediaType.parse("text/plain"), "");
            filePartPhotoID = MultipartBody.Part.createFormData("attachment", "", attachmentEmpty);
        }

        RequestBody userId = RequestBody.create(MediaType.parse("text/plain"), userID);
        RequestBody desc = RequestBody.create(MediaType.parse("text/plain"), strDescription);

        Call<SuccessResApplyForFanClub> loginCall = apiInterface.applyVipMembership(userId,desc,filePart,filePartPhotoID);
        loginCall.enqueue(new Callback<SuccessResApplyForFanClub>() {
            @Override
            public void onResponse(Call<SuccessResApplyForFanClub> call, Response<SuccessResApplyForFanClub> response) {
                DataManager.getInstance().hideProgressMessage();
                try {

                    SuccessResApplyForFanClub data = response.body();
                    String responseString = new Gson().toJson(response.body());
                    Log.e(TAG,"Test Response :"+responseString);
                    getActivity().onBackPressed();

                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e(TAG,"Test Response :"+response.body());
                }
            }

            @Override
            public void onFailure(Call<SuccessResApplyForFanClub> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
                showToast(getActivity(),"Data added successfully");
                getActivity().onBackPressed();
            }
        });
    }

}