package com.technorizen.stanrz.fragments;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.media.tv.TvView;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import java.io.ByteArrayOutputStream;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.braintreepayments.cardform.OnCardFormSubmitListener;
import com.braintreepayments.cardform.view.CardForm;
import com.bumptech.glide.Glide;
import com.erikagtierrez.multiple_media_picker.Gallery;
import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.gson.Gson;
import com.iamkdblue.videocompressor.VideoCompress;
import com.technorizen.stanrz.BuildConfig;
import com.technorizen.stanrz.R;
import com.technorizen.stanrz.adapters.OtherUserFollowingAdapter;
import com.technorizen.stanrz.adapters.SearchAdapter;
import com.technorizen.stanrz.adapters.SearchTagPeopleAdapter;
import com.technorizen.stanrz.adapters.StoriesAdapter;
import com.technorizen.stanrz.adapters.TaggedPeopleAdapter;
import com.technorizen.stanrz.adapters.UploadPostsAdapter;
import com.technorizen.stanrz.databinding.FragmentAddPostBinding;
import com.technorizen.stanrz.models.SuccessResGetFollowings;
import com.technorizen.stanrz.models.SuccessResGetStories;
import com.technorizen.stanrz.models.SuccessResGetUser;
import com.technorizen.stanrz.models.SuccessResGetVideos;
import com.technorizen.stanrz.models.SuccessResProfileData;
import com.technorizen.stanrz.models.SuccessResUploadCoverImage;
import com.technorizen.stanrz.models.SuccessResUploadPost;
import com.technorizen.stanrz.models.SuccessResUploadStory;
import com.technorizen.stanrz.retrofit.ApiClient;
import com.technorizen.stanrz.retrofit.NetworkAvailablity;
import com.technorizen.stanrz.retrofit.StanrzInterface;
import com.technorizen.stanrz.utility.DataManager;
import com.technorizen.stanrz.utility.RealPathUtil;
import com.technorizen.stanrz.utility.SharedPreferenceUtility;
import com.technorizen.stanrz.utility.TaggedUserId;
import com.technorizen.stanrz.utility.Util;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;
import static android.media.tv.TvTrackInfo.TYPE_VIDEO;
import static android.os.Environment.getExternalStoragePublicDirectory;
import static com.technorizen.stanrz.retrofit.Constant.USER_ID;
import static com.technorizen.stanrz.retrofit.Constant.showToast;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddPostFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddPostFragment extends Fragment {

    public static final String FILE_PROVIDER_AUTHORITY = ".silicompressor.provider";
    private static final int REQUEST_TAKE_CAMERA_PHOTO = 1;
    private static final int MY_PERMISSIONS_REQUEST_WRITE_STORAGE = 1;
    private static final int MY_PERMISSIONS_REQUEST_WRITE_STORAGE_VID = 2;
    private static final int REQUEST_TAKE_VIDEO = 200;
    private static final int TYPE_IMAGE = 1;
    private static final int TYPE_VIDEO = 2;
    private SuccessResProfileData.Result userDetail;
    String mCurrentPhotoPath;
    Uri capturedUri = null;
    Uri compressUri = null;
    ImageView imageView;
    TextView picDescription;
    private ImageView videoImageView;
    LinearLayout compressionMsg;

    RecyclerView rvUsers;

    private List<SuccessResGetUser.Result> usersList = new LinkedList<>();
    private List<SuccessResGetUser.Result> usersAddedList = new LinkedList<>();
    private ArrayList<SuccessResGetUser.Result> newUsersList = new ArrayList<>();

    private ArrayList<SuccessResGetUser.Result> taggedUsersList = new ArrayList<>();

    private String nsfwStatus = "No";

    private  String destPath = "";

    private Dialog dialog;

    private Dialog tagDialog;

    private String openFanClub = "";

    private StanrzInterface apiInterface;

    String str_image_path = "";

    String str_video_path = "";

    private static final int REQUEST_CAMERA = 191;

    private static final int SELECT_FILE = 2;

    private Uri uriSavedImage;
    private Uri uriSavedVideo;

    private boolean haveStory = false;

    private String strSuperLikes;

    private String storyID = "";

    RadioButton radioButton;

    FragmentAddPostBinding binding;

    private static final int MY_PERMISSION_CONSTANT = 5;

    static final int REQUEST_VIDEO_CAPTURE = 125;

    private String strType = "Public";

    private String strCaption = "";

    public static int OPEN_MEDIA_PICKER = 10;

    public static int IMAGE_PICKER_SELECT = 20;

    private static final String ARG_PARAM1 = "param1";

    private static final String ARG_PARAM2 = "param2";

    private String mParam1;

    private String mParam2;

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

    public AddPostFragment() {
        // Required empty public constructor
    }

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

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_post, container, false);

        apiInterface = ApiClient.getClient().create(StanrzInterface.class);

        checkPermisssionForReadStorage();

        binding.tvUploadImage.setOnClickListener(v ->
                {
                    if (checkPermisssionForReadStorage())
                    {
//                        File dirtostoreFile = new File(Environment.getExternalStorageDirectory() + "/Stanrz/Images/");
//                        if (!dirtostoreFile.exists()) {
//                            dirtostoreFile.mkdirs();
//                        }
//                        String timestr = DataManager.getInstance().convertDateToString(Calendar.getInstance().getTimeInMillis());
//                        File tostoreFile = new File(Environment.getExternalStorageDirectory() + "/Stanrz/Images/" + "IMG_" + timestr + ".jpg");
//                        str_image_path = tostoreFile.getPath();
//                        uriSavedImage = FileProvider.getUriForFile(getActivity(),
//                                BuildConfig.APPLICATION_ID + ".provider",
//                                tostoreFile);
//                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                        intent.putExtra(MediaStore.EXTRA_OUTPUT, uriSavedImage);
//                        startActivityForResult(intent, REQUEST_CAMERA);

                        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        if (cameraIntent.resolveActivity(getActivity().getPackageManager()) != null)
                            startActivityForResult(cameraIntent, REQUEST_CAMERA);

                    }
                    else
                    {
                        Toast.makeText(getActivity(),"Please allow all permissions.",Toast.LENGTH_SHORT).show();
                    }
                }
        );

        binding.tvUploadVideo.setOnClickListener(v ->
                {

                    if (checkPermisssionForReadStorage())
                    {
//                        File dirtostoreFile = new File(Environment.getExternalStorageDirectory() + "/Stanrz/Videos/");
//                        if (!dirtostoreFile.exists()) {
//                            dirtostoreFile.mkdirs();
//                        }
//                        String timestr = DataManager.getInstance().convertDateToString(Calendar.getInstance().getTimeInMillis());
//                        File tostoreFile = new File(Environment.getExternalStorageDirectory() + "/Stanrz/Videos/" + "Vid_" + timestr + ".mp4");
//                        str_video_path = tostoreFile.getPath();
//                        uriSavedVideo = FileProvider.getUriForFile(getActivity(),
//                                BuildConfig.APPLICATION_ID + ".provider",
//                                tostoreFile);
//
//                        Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
//                        takeVideoIntent.putExtra(MediaStore.EXTRA_OUTPUT, uriSavedVideo);
//                        if (takeVideoIntent.resolveActivity(getActivity().getPackageManager()) != null) {
//                            startActivityForResult(takeVideoIntent, REQUEST_VIDEO_CAPTURE);
//                        }

                       Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                      takeVideoIntent.putExtra(MediaStore.EXTRA_OUTPUT, uriSavedVideo);

                    if (takeVideoIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                        startActivityForResult(takeVideoIntent, REQUEST_VIDEO_CAPTURE);
                    }
                    }
                    else
                    {
                        Toast.makeText(getActivity(),"Please allow all permissions.",Toast.LENGTH_SHORT).show();
                    }
                }
        );

        binding.tvGallary.setOnClickListener(v ->
                {

                    if (checkPermisssionForReadStorage())
                    {
                        showImageSelection();
                    }
                    else
                    {
                        Toast.makeText(getActivity(),"Please allow all permissions.",Toast.LENGTH_SHORT).show();
                    }
                }
        );

        if (NetworkAvailablity.getInstance(getActivity()).checkNetworkStatus()) {
            getProfile();
            getStories();
            getAllUsers();
        } else {
            Toast.makeText(getActivity(), getResources().getString(R.string.msg_noInternet), Toast.LENGTH_SHORT).show();
        }
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

    public void showSelection() {

        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().getAttributes().windowAnimations = android.R.style.Widget_Material_ListPopupWindow;
        dialog.setContentView(R.layout.dialog_upload_post);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        dialog.setCancelable(false);
        Window window = dialog.getWindow();
        lp.copyFrom(window.getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);

        TextView tvImage = dialog.findViewById(R.id.tvUploadImage);
        TextView tvVideo = dialog.findViewById(R.id.tvUploadVideo);
        TextView tvGalary = dialog.findViewById(R.id.tvGallary);

        tvImage.setOnClickListener(v ->
                {

                    dialog.dismiss();

                    File dirtostoreFile = new File(Environment.getExternalStorageDirectory() + "/Stanrz/Images/");

                    if (!dirtostoreFile.exists()) {
                        dirtostoreFile.mkdirs();
                    }
                    String timestr = DataManager.getInstance().convertDateToString(Calendar.getInstance().getTimeInMillis());
                    File tostoreFile = new File(Environment.getExternalStorageDirectory() + "/Stanrz/Images/" + "IMG_" + timestr + ".jpg");
                    str_image_path = tostoreFile.getPath();
                    uriSavedImage = FileProvider.getUriForFile(getActivity(),
                            BuildConfig.APPLICATION_ID + ".provider",
                            tostoreFile);
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, uriSavedImage);
                    startActivityForResult(intent, REQUEST_CAMERA);

                }
        );

        tvVideo.setOnClickListener(v ->
                {

//                    File dirtostoreFile = new File(Environment.getExternalStorageDirectory() + "/Stanrz/Videos/");
//                    if (!dirtostoreFile.exists()) {
//                        dirtostoreFile.mkdirs();
//                    }
//                    String timestr = DataManager.getInstance().convertDateToString(Calendar.getInstance().getTimeInMillis());
//                    File tostoreFile = new File(Environment.getExternalStorageDirectory() + "/Stanrz/Videos/" + "Vid_" + timestr + ".mp4");
//                    str_video_path = tostoreFile.getPath();
//                    uriSavedVideo = FileProvider.getUriForFile(getActivity(),
//                            BuildConfig.APPLICATION_ID + ".provider",
//                            tostoreFile);
//
//                    Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
//                    takeVideoIntent.putExtra(MediaStore.EXTRA_OUTPUT, uriSavedVideo);
//
//                    if (takeVideoIntent.resolveActivity(getActivity().getPackageManager()) != null) {
//                        startActivityForResult(takeVideoIntent, REQUEST_VIDEO_CAPTURE);
//                    }
//                    dialog.dismiss();

//                    Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
//                    if (takeVideoIntent.resolveActivity(getActivity().getPackageManager()) != null) {
//                        startActivityForResult(takeVideoIntent, REQUEST_VIDEO_CAPTURE);
//                    }

                    Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                    takeVideoIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    takeVideoIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                    takeVideoIntent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
                    if (takeVideoIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                        try {

                            takeVideoIntent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 10);
                            takeVideoIntent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
                            capturedUri = FileProvider.getUriForFile(getActivity(),
                                    getActivity().getPackageName() + FILE_PROVIDER_AUTHORITY,
                                    createMediaFile(TYPE_VIDEO));

                            takeVideoIntent.putExtra(MediaStore.EXTRA_OUTPUT, capturedUri);
                            Log.d("LOG_TAG", "VideoUri: " + capturedUri.toString());
                            startActivityForResult(takeVideoIntent, REQUEST_TAKE_VIDEO);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        dialog.dismiss();

                    }

                }
        );

        tvGalary.setOnClickListener(v ->
                {
                    dialog.dismiss();
                    showImageSelection();

                }
        );

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }


    private File createMediaFile(int type) throws IOException {

        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String fileName = (type == TYPE_IMAGE) ? "JPEG_" + timeStamp + "_" : "VID_" + timeStamp + "_";
        File storageDir = getActivity().getExternalFilesDir(
                type == TYPE_IMAGE ? Environment.DIRECTORY_PICTURES : Environment.DIRECTORY_MOVIES);
        File file = File.createTempFile(
                fileName,  /* prefix */
                type == TYPE_IMAGE ? ".jpg" : ".mp4",         /* suffix */
                storageDir      /* directory */
        );

        // Get the path of the file created
        mCurrentPhotoPath = file.getAbsolutePath();
        Log.d("LOG_TAG", "mCurrentPhotoPath: " + mCurrentPhotoPath);
        return file;
    }

    public void showImageSelection() {

        if (checkPermisssionForReadStorage()) {
            Intent intent = new Intent(getActivity(), Gallery.class);
            // Set the title
            intent.putExtra("title", "Select media");
            // Mode 1 for both images and videos selection, 2 for images only and 3 for videos!
            intent.putExtra("mode", 1);
            intent.putExtra("maxSelection", 3); // Optional
            startActivityForResult(intent, OPEN_MEDIA_PICKER);
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

                ArrayList<String> selectionResult = data.getStringArrayListExtra("result");

                int size = selectionResult.size();

                if (selectionResult.size() > 0) {

                    String selectedMediaUri = selectionResult.get(0);
                    if (selectedMediaUri.endsWith("jpg") || selectedMediaUri.endsWith("jpeg") || selectedMediaUri.endsWith("png")) {

                        ArrayList<String> newSelectionResult = new ArrayList<>();

                        for (String selected: selectionResult)
                        {
                            Uri selectedImage = Uri.fromFile(new File(selected));
                            Bitmap bitmapNew = null;
                            try {
                                bitmapNew = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedImage);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            Bitmap bitmap = BITMAP_RE_SIZER(bitmapNew, bitmapNew.getWidth(), bitmapNew.getHeight());
                            Uri tempUri = getImageUri(getActivity(), bitmap);
                            String image = RealPathUtil.getRealPath(getActivity(), tempUri);
                            newSelectionResult.add(image);
                        }
                        fullScreenDialog(newSelectionResult, true);
                    } else {

                      /*  File f = new File(selectionResult.get(0));
                        long length = f.length();
                        Log.d(TAG, "onActivityResult: " + f.length());
                        fullScreenDialog(selectionResult, false);*/

                        String VideoPath =  RealPathUtil.getRealPath(getActivity(), Uri.fromFile(new File(selectionResult.get(0))));

                        compressVideo(VideoPath);
                    }
                }
                Toast.makeText(getActivity(), "" + size, Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == REQUEST_CAMERA && resultCode == RESULT_OK) {

                try {
                    if (data != null) {
                        Bundle extras = data.getExtras();
                        Bitmap bitmapNew = (Bitmap) extras.get("data");
                        Bitmap imageBitmap = BITMAP_RE_SIZER(bitmapNew, bitmapNew.getWidth(), bitmapNew.getHeight());
                        Uri tempUri = getImageUri(getActivity(), imageBitmap);

                        String image = RealPathUtil.getRealPath(getActivity(), tempUri);
                        str_image_path = image;
                        ArrayList<String> selectionResult = new ArrayList<>();
                        selectionResult.add(str_image_path);
                        fullScreenDialog(selectionResult, true);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

        } else if (requestCode == REQUEST_VIDEO_CAPTURE && resultCode == RESULT_OK) {


            Uri videoUri = data.getData();
//            videoView.setVideoURI(videoUri);

            String videPath = RealPathUtil.getRealPath(getActivity(), videoUri);


            compressVideo(videPath);

           /* DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));

            File f = new File(str_video_path);
            Log.d("TAG", "onActivityResult: Desti"+f.length());

            destPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath()+ File.separator + "VID_" + new SimpleDateFormat("yyyyMMdd_HHmmss", getLocale()).format(new Date()) + ".mp4";

            VideoCompress.compressVideoLow(str_video_path, destPath, new VideoCompress.CompressListener() {
                @Override
                public void onStart() {
                 *//*   tv_indicator.setText("Compressing..." + "\n"
                            + "Start at: " + new SimpleDateFormat("HH:mm:ss", getLocale()).format(new Date()));
                    pb_compress.setVisibility(View.VISIBLE);
                    startTime = System.currentTimeMillis();
                    *//*
                    Util.writeFile(getActivity(), "Start at: " + new SimpleDateFormat("HH:mm:ss", getLocale()).format(new Date()) + "\n");
                }

                @Override
                public void onSuccess(String compressVideoPath) {
                   *//*  endTime = System.currentTimeMillis();
                    Util.writeFile(getActivity(), "End at: " + new SimpleDateFormat("HH:mm:ss", getLocale()).format(new Date()) + "\n");
                    Util.writeFile(getActivity(), "Total: " + ((endTime - startTime)/1000) + "s" + "\n");
                    Util.writeFile(getActivity());
*//*
                    File f = new File(destPath);
                    Log.d("TAG", "onActivityResult: Desti"+f.length());

                    DataManager.getInstance().hideProgressMessage();

                    ArrayList<String> selectionResult = new ArrayList<>();
                    selectionResult.add(destPath);

                    File file = new File(destPath);

                    long length = file.length();

                    Log.d(TAG, "onActivityResult: " + file.length());

                           fullScreenDialog(selectionResult, false);
                }

                @Override
                public void onFail() {
                  *//*  tv_indicator.setText("Compress Failed!");
                    pb_compress.setVisibility(View.INVISIBLE);
                    endTime = System.currentTimeMillis();*//*

                    DataManager.getInstance().hideProgressMessage();

                    Util.writeFile(getActivity(), "Failed Compress!!!" + new SimpleDateFormat("HH:mm:ss", getLocale()).format(new Date()));
                }

                @Override
                public void onProgress(float percent) {
//                    tv_progress.setText(String.valueOf(percent) + "%");
                }
            });*/

        }

    }

    public void compressVideo(String imagePath)
    {
        DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));

        File f = new File(imagePath);
        Log.d("TAG", "onActivityResult: Desti"+f.length());

        destPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath()+ File.separator + "VID_" + new SimpleDateFormat("yyyyMMdd_HHmmss", getLocale()).format(new Date()) + ".mp4";

        VideoCompress.compressVideoLow(imagePath, destPath, new VideoCompress.CompressListener() {
            @Override
            public void onStart() {
                 /*   tv_indicator.setText("Compressing..." + "\n"
                            + "Start at: " + new SimpleDateFormat("HH:mm:ss", getLocale()).format(new Date()));
                    pb_compress.setVisibility(View.VISIBLE);
                    startTime = System.currentTimeMillis();
                    */
                Util.writeFile(getActivity(), "Start at: " + new SimpleDateFormat("HH:mm:ss", getLocale()).format(new Date()) + "\n");
            }

            @Override
            public void onSuccess(String compressVideoPath) {

                File f = new File(destPath);
                Log.d("TAG", "onActivityResult: Desti"+f.length());

                DataManager.getInstance().hideProgressMessage();

                ArrayList<String> selectionResult = new ArrayList<>();
                selectionResult.add(destPath);

                File file = new File(destPath);

                long length = file.length();

                Log.d(TAG, "onActivityResult: " + file.length());

                fullScreenDialog(selectionResult, false);
            }

            @Override
            public void onFail() {
                  /*  tv_indicator.setText("Compress Failed!");
                    pb_compress.setVisibility(View.INVISIBLE);
                    endTime = System.currentTimeMillis();*/

                DataManager.getInstance().hideProgressMessage();

                Util.writeFile(getActivity(), "Failed Compress!!!" + new SimpleDateFormat("HH:mm:ss", getLocale()).format(new Date()));
            }

            @Override
            public void onProgress(float percent) {
//                    tv_progress.setText(String.valueOf(percent) + "%");
            }
        });
    }
    int unlockWith = 0;
    int story = 0;
    int nsfw = 0;

    RecyclerView rvTagPeople;

    TaggedPeopleAdapter taggedPeopleAdapter;

    private void fullScreenDialog(ArrayList<String> selectionResult, boolean from) {

        dialog = new Dialog(getActivity(), WindowManager.LayoutParams.MATCH_PARENT);

        usersList = new LinkedList<>();
        usersAddedList = new LinkedList<>();
        usersList.clear();
        usersAddedList.clear();

        dialog.setContentView(R.layout.upload_post_dialog);

        AppCompatButton btnSubmit = dialog.findViewById(R.id.btnSubmit);

        EditText etCaption = dialog.findViewById(R.id.etCaption);

        RecyclerView rvUploading;

        rvTagPeople = dialog.findViewById(R.id.rvTagPeople);

        RadioButton radioNSFW = dialog.findViewById(R.id.radioNSFW);

        RadioButton radioUnlockWith = dialog.findViewById(R.id.radioUnloackWIth);

        RadioButton radioStory = dialog.findViewById(R.id.radioStory);

        rvUploading = dialog.findViewById(R.id.rvimages_videos);

        EditText etsuperLikes = dialog.findViewById(R.id.etUnlockAmount);

        MaterialCheckBox checkBoxPublic = dialog.findViewById(R.id.checkboxPublic);

        MaterialCheckBox checkBoxFanClub = dialog.findViewById(R.id.checkboxFanClub);
//        TextView tvTagPeople = dialog.findViewById(R.id.tvTagPeople);

        ImageView ivBack;

        RecyclerView rvTagComment = dialog.findViewById(R.id.rvTagComment);

//        TaggedUserId taggedUserId = new TaggedUserId() {
//            @Override
//            public void taggedId(int position) {
//                usersAddedList.remove(position);
//                taggedPeopleAdapter.notifyDataSetChanged();
//
//            }
//        };

        taggedPeopleAdapter = new TaggedPeopleAdapter(getActivity(), usersAddedList, true, new TaggedUserId() {
            @Override
            public void taggedId(int position) {
                usersAddedList.remove(position);
                taggedPeopleAdapter.notifyDataSetChanged();
            }
        });

        rvTagPeople.setHasFixedSize(true);
        rvTagPeople.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));

        rvTagPeople.setAdapter(taggedPeopleAdapter);

        etsuperLikes.setEnabled(false);

        radioUnlockWith.setOnClickListener(view ->
                {
                  if(unlockWith==1)
                  {
                       radioUnlockWith.setChecked(false);
                       etsuperLikes.setEnabled(false);
                       etsuperLikes.setText("");
                       unlockWith = 0;
                  }
                  else
                  {
//                      tvTagPeople.setVisibility(View.VISIBLE);
                      radioUnlockWith.setChecked(true);
                      etsuperLikes.setEnabled(true);
                      unlockWith = 1;
                      radioStory.setChecked(false);
                      story = 0;
                  }
                }
                );

        radioStory.setOnClickListener(view ->
                {
                    if(story == 0)
                    {
//                        tvTagPeople.setVisibility(View.GONE);
                        radioStory.setChecked(true);
                        story = 1;
                        radioNSFW.setChecked(false);
                        radioUnlockWith.setChecked(false);
                        etsuperLikes.setEnabled(false);
                        nsfw = 0;
                        unlockWith = 0;
                    }
                    else
                    {
                        radioStory.setChecked(false);
                        story = 0;
                    }
                }
                );

        radioNSFW.setOnClickListener(view ->
                {

                    if(nsfw == 0)
                    {
                        radioNSFW.setChecked(true);
//                        tvTagPeople.setVisibility(View.VISIBLE);
                        nsfw = 1;
                        radioStory.setChecked(false);
                        story = 0;
                    }
                    else
                    {
                        radioNSFW.setChecked(false);
                        nsfw = 0;
                    }
                }
        );

        ivBack = dialog.findViewById(R.id.img_header);

        rvUploading.setHasFixedSize(true);

        rvUploading.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));

        rvUploading.setAdapter(new UploadPostsAdapter(getActivity(), selectionResult));


        if(openFanClub.equalsIgnoreCase("done"))
        {
            checkBoxFanClub.setVisibility(View.VISIBLE);
        }
        else
        {
            checkBoxFanClub.setVisibility(View.GONE);
        }

        etCaption.addTextChangedListener(new TextWatcher()
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

                strCaption = etCaption.getText().toString();
                String text = editable.toString();
                Pattern p = Pattern.compile("[@][a-zA-Z0-9-.]+");
                Matcher m = p.matcher(text);
                int cursorPosition = etCaption.getSelectionStart();
                ArrayList<SuccessResGetUser.Result> resultArrayList = new ArrayList<>();
                rvTagComment.setHasFixedSize(true);
                rvTagComment.setLayoutManager(new LinearLayoutManager(getActivity()));
                rvTagComment.setAdapter(new SearchTagPeopleAdapter(getActivity(), resultArrayList, true, new TaggedUserId() {
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
                        for (SuccessResGetUser.Result result:newUsersList)
                        {
                            if(result.getUsername().contains(text.substring(s, e)))
                            {
                                resultArrayList.add(result);
                            }
                        }
                        rvTagComment.setHasFixedSize(true);
                        rvTagComment.setLayoutManager(new LinearLayoutManager(getActivity()));
                        rvTagComment.setAdapter(new SearchTagPeopleAdapter(getActivity(), resultArrayList, true, new TaggedUserId() {
                            @Override
                            public void taggedId(int position) {

                                int i=strCaption.length();

                                i--;

                                while (i>=0)
                                {
                                    Character character= strCaption.charAt(i);
                                    if(character.compareTo('@')==0)
                                    {
                                        int z= i+1;
                                        strCaption =strCaption.substring(0,z);
                                        break;
                                    }
                                    i--;
                                }

                                strCaption = strCaption + resultArrayList.get(position).getUsername();

                                etCaption.setText(strCaption);

                                etCaption.setSelection(etCaption.getText().length());

                                resultArrayList.clear();
                                rvTagComment.setHasFixedSize(true);
                                rvTagComment.setLayoutManager(new LinearLayoutManager(getActivity()));
                                rvTagComment.setAdapter(new SearchTagPeopleAdapter(getActivity(), resultArrayList, true, new TaggedUserId() {
                                    @Override
                                    public void taggedId(int position) {

                                    }
                                }));
                            }
                        }));
                        break;
                    }
                }
            }
        });

        btnSubmit.setOnClickListener(v ->
                {
                    strCaption = etCaption.getText().toString().trim();

                    if (from) {

                        if(checkBoxPublic.isChecked())
                        {
                            strSuperLikes = etsuperLikes.getText().toString();
                            uploadPost(selectionResult);
                        }else if(checkBoxFanClub.isChecked())
                        {
                            if(radioStory.isChecked())
                            {
                                if (haveStory) {
                                updateStory(selectionResult, true, "Image");
                            } else {
                                uploadStory(selectionResult, true, "Image");
                            }
                            }
                            else
                            {
                                if (radioNSFW.isChecked())
                                {
                                    nsfwStatus = "Yes";
                                }

                                strSuperLikes = etsuperLikes.getText().toString();
                                uploadPost(selectionResult);
                            }
                        }

//                        if (text.equalsIgnoreCase("Story")) {
//                            if (haveStory) {
//                                updateStory(selectionResult, true, "Image");
//                            } else {
//                                uploadStory(selectionResult, true, "Image");
//                            }
//                        } else if (text.equalsIgnoreCase("Unlock With")) {
//                            strSuperLikes = etsuperLikes.getText().toString();
//                            uploadPost(selectionResult);
//                        }

                    } else {

                        if(checkBoxPublic.isChecked())
                        {

                            strSuperLikes = etsuperLikes.getText().toString();
                            uploadVideoPost(selectionResult);

                        }else if(checkBoxFanClub.isChecked())
                        {

                            if(radioStory.isChecked())
                            {
                                if (haveStory) {
                                updateStory(selectionResult, false, "Video");
                            } else {
                                uploadStory(selectionResult, false, "Video");
                            }

                            }
                            else
                            {

                                if (radioNSFW.isChecked())
                                {
                                    nsfwStatus = "Yes";
                                }

                                strSuperLikes = etsuperLikes.getText().toString();
                                uploadVideoPost(selectionResult);
                            }

                        }

//                        if (text.equalsIgnoreCase("Story")) {
//
//                            if (haveStory) {
//                                updateStory(selectionResult, false, "Video");
//                            } else {
//                                uploadStory(selectionResult, false, "Video");
//                            }
//                        } else if (text.equalsIgnoreCase("Unlock With")) {
//                            strSuperLikes = etsuperLikes.getText().toString();
//                            uploadVideoPost(selectionResult);
//                            dialog.show();
//                        }



                    }
                }
        );

        ivBack.setOnClickListener(v ->
                {
                    dialog.dismiss();
                }
        );

        checkBoxPublic.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    checkBoxFanClub.setChecked(false);
                    strType = "Public";

                    radioNSFW.setVisibility(View.GONE);
                    radioStory.setVisibility(View.GONE);
                } else {
                    checkBoxFanClub.setChecked(true);
                    strType = "Fan";
                }
            }
        });

        checkBoxFanClub.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    radioNSFW.setVisibility(View.VISIBLE);
                    radioStory.setVisibility(View.VISIBLE);
                    checkBoxPublic.setChecked(false);
                    strType = "Fan";
                } else {
                    checkBoxPublic.setChecked(true);
                    strType = "Public";
                }
            }
        });

        dialog.show();
    }

    public void showTagPeopleDialog()
    {

        tagDialog = new Dialog(getActivity(), WindowManager.LayoutParams.MATCH_PARENT);

        tagDialog.setContentView(R.layout.fragment_search_users);

        rvUsers = tagDialog.findViewById(R.id.rvUsers);
        ImageView ivBack;
        ivBack = tagDialog.findViewById(R.id.ivBack);
        ivBack.setOnClickListener(v ->
                {
                    tagDialog.dismiss();
                }
        );
        EditText etSearch = tagDialog.findViewById(R.id.etSearch);

        etSearch.requestFocus();

        etSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {

                getUsers(cs.toString());

            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {

            }

            @Override
            public void afterTextChanged(Editable arg0) {

            }
        });

        tagDialog.show();

    }

    private void getUsers(String title) {

        String userId=  SharedPreferenceUtility.getInstance(getActivity()).getString(USER_ID);
        Map<String,String> map = new HashMap<>();
        map.put("user_id",userId);
        map.put("search",title);

        Call<SuccessResGetUser> call = apiInterface.getUsers(map);

        call.enqueue(new Callback<SuccessResGetUser>() {
            @Override
            public void onResponse(Call<SuccessResGetUser> call, Response<SuccessResGetUser> response) {

                DataManager.getInstance().hideProgressMessage();

                try {
                    SuccessResGetUser data = response.body();
                    Log.e("data",data.status);
                    if (data.status.equals("1")) {
                        String dataResponse = new Gson().toJson(response.body());
                        usersList.clear();

                        usersList.addAll(data.getResult());

                        rvUsers.setHasFixedSize(true);
                        rvUsers.setLayoutManager(new LinearLayoutManager(getActivity()));
                        rvUsers.setAdapter(new SearchTagPeopleAdapter(getActivity(), usersList, true, new TaggedUserId() {
                            @Override
                            public void taggedId(int position) {

                                usersAddedList.add(usersList.get(position));
                                taggedPeopleAdapter.notifyDataSetChanged();
                                tagDialog.dismiss();

                            }
                        }));

                    } else if (data.status.equals("0")) {
                        showToast(getActivity(), data.message);
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

    public void uploadPost(ArrayList<String> imagesVideosPathList) {

        taggedUsersList.clear();

        String text = strCaption;

        SpannableStringBuilder ss = new SpannableStringBuilder(text);

        Pattern p = Pattern.compile("[@][a-zA-Z0-9-.]+");

        Matcher m = p.matcher(ss);

        while(m.find())
        {
            final int s = m.start() + 1; // add 1 to omit the "@" tag
            final int e = m.end();

            int i=0;

            for (SuccessResGetUser.Result result:newUsersList)
            {
                if(result.getUsername().equalsIgnoreCase(text.substring(s, e)))
                {

                    taggedUsersList.add(result);

                }

                i++;
            }
        }

        String strTagUsers = "";

        String strTaggedUserId = "";

        for (SuccessResGetUser.Result result:taggedUsersList)
        {
            if(strCaption.contains(result.getUsername()))
            {
                strTaggedUserId = strTaggedUserId + result.getId()+",";
            }

        }

        if (strTaggedUserId.endsWith(","))
        {
            strTaggedUserId = strTaggedUserId.substring(0, strTaggedUserId.length() - 1);
        }

        String strUserId = SharedPreferenceUtility.getInstance(getContext()).getString(USER_ID);

        DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));

        List<MultipartBody.Part> filePartList = new LinkedList<>();

        for (int i = 0; i < imagesVideosPathList.size(); i++) {
            File file = DataManager.getInstance().saveBitmapToFile(new File(imagesVideosPathList.get(i)));
            filePartList.add(MultipartBody.Part.createFormData("image[]", file.getName(), RequestBody.create(MediaType.parse("image[]/*"), file)));
        }
        RequestBody userId = RequestBody.create(MediaType.parse("text/plain"), strUserId);
        RequestBody caption = RequestBody.create(MediaType.parse("text/plain"), strCaption);
        RequestBody comment = RequestBody.create(MediaType.parse("text/plain"), "Test");
        RequestBody type = RequestBody.create(MediaType.parse("text/plain"), strType);
        RequestBody nsfw = RequestBody.create(MediaType.parse("text/plain"), nsfwStatus);
        RequestBody tagUser = RequestBody.create(MediaType.parse("text/plain"), strTaggedUserId);
        RequestBody superLikes = RequestBody.create(MediaType.parse("text/plain"), strSuperLikes);

        Call<SuccessResUploadPost> loginCall = apiInterface.uploadPost(userId, caption, comment, type,nsfw, superLikes, tagUser,filePartList);
        loginCall.enqueue(new Callback<SuccessResUploadPost>() {
            @Override
            public void onResponse(Call<SuccessResUploadPost> call, Response<SuccessResUploadPost> response) {
                DataManager.getInstance().hideProgressMessage();
                try {

                    SuccessResUploadPost data = response.body();

                    Log.e("data", data.status);
                    if (data.status.equals("1")) {
                        String dataResponse = new Gson().toJson(response.body());
                        Log.e("MapMap", "EDIT PROFILE RESPONSE" + dataResponse);
                        showToast(getActivity(), data.message);

                        dialog.dismiss();

                    } else if (data.status.equals("0")) {
                        showToast(getActivity(), data.message);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e(TAG, "Test Response :" + response.body());
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

    public void uploadVideoPost(ArrayList<String> imagesVideosPathList) {

        taggedUsersList.clear();

        String text = strCaption;

        SpannableStringBuilder ss = new SpannableStringBuilder(text);

        Pattern p = Pattern.compile("[@][a-zA-Z0-9-.]+");

        Matcher m = p.matcher(ss);

        while(m.find())
        {
            final int s = m.start() + 1; // add 1 to omit the "@" tag
            final int e = m.end();

            int i=0;

            for (SuccessResGetUser.Result result:newUsersList)
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
            if(strCaption.contains(result.getUsername()))
            {
                strTaggedUserId = strTaggedUserId + result.getId()+",";
            }

        }

        if (strTaggedUserId.endsWith(","))
        {
            strTaggedUserId = strTaggedUserId.substring(0, strTaggedUserId.length() - 1);
        }

        String strUserId = SharedPreferenceUtility.getInstance(getContext()).getString(USER_ID);

        List<MultipartBody.Part> filePartList = new LinkedList<>();

        for (int i = 0; i < imagesVideosPathList.size(); i++) {
            //       File file = DataManager.getInstance().saveBitmapToFile(new File(imagesVideosPathList.get(i)));
            Log.d(TAG, "uploadVideoPost: " + imagesVideosPathList.get(i).getBytes());
            File f = new File(imagesVideosPathList.get(i));
            filePartList.add(MultipartBody.Part.createFormData("video[]", f.getName(), RequestBody.create(MediaType.parse("video[]/*"), f)));
        }

        DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));
        RequestBody userId = RequestBody.create(MediaType.parse("text/plain"), strUserId);
        RequestBody caption = RequestBody.create(MediaType.parse("text/plain"), strCaption);
        RequestBody comment = RequestBody.create(MediaType.parse("text/plain"), "Test");
        RequestBody type = RequestBody.create(MediaType.parse("text/plain"), strType);
        RequestBody nsfw = RequestBody.create(MediaType.parse("text/plain"), nsfwStatus);
        RequestBody tagUsers = RequestBody.create(MediaType.parse("text/plain"), strTaggedUserId);
        RequestBody unlockSuperlikes = RequestBody.create(MediaType.parse("text/plain"), strSuperLikes);

        Call<SuccessResGetVideos> loginCall = apiInterface.uploadVideos(userId, caption, comment, type,nsfw, unlockSuperlikes,tagUsers, filePartList);
        loginCall.enqueue(new Callback<SuccessResGetVideos>() {
            @Override
            public void onResponse(Call<SuccessResGetVideos> call, Response<SuccessResGetVideos> response) {
                DataManager.getInstance().hideProgressMessage();
                try {

                    SuccessResGetVideos data = response.body();

                    Log.d(TAG, "onResponse: "+data.message);

//                    setSellerData();
                    Log.e("data", data.status);
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
                    Log.e(TAG, "Test Response :" + response.body());
                    dialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<SuccessResGetVideos> call, Throwable t) {
                call.cancel();
                Log.d(TAG, "onResponse: "+t.toString());
                DataManager.getInstance().hideProgressMessage();
                dialog.dismiss();
            }
        });
    }

    public void uploadStory(ArrayList<String> imagesVideosPathList, boolean image, String type1) {

        String strUserId = SharedPreferenceUtility.getInstance(getContext()).getString(USER_ID);

        DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));

        List<MultipartBody.Part> filePartList = new LinkedList<>();

        if (image) {
            for (int i = 0; i < imagesVideosPathList.size(); i++) {
                File file = DataManager.getInstance().saveBitmapToFile(new File(imagesVideosPathList.get(i)));
                filePartList.add(MultipartBody.Part.createFormData("image[]", file.getName(), RequestBody.create(MediaType.parse("image[]/*"), file)));
            }
        } else {
            for (int i = 0; i < imagesVideosPathList.size(); i++) {
                //       File file = DataManager.getInstance().saveBitmapToFile(new File(imagesVideosPathList.get(i)));
                File file = new File(imagesVideosPathList.get(i));
                filePartList.add(MultipartBody.Part.createFormData("image[]", file.getName(), RequestBody.create(MediaType.parse("image[]/*"), file)));
            }
        }

        RequestBody userId = RequestBody.create(MediaType.parse("text/plain"), strUserId);
        RequestBody caption = RequestBody.create(MediaType.parse("text/plain"), strCaption);
        RequestBody type = RequestBody.create(MediaType.parse("text/plain"), type1);

        Call<SuccessResUploadStory> loginCall = apiInterface.uploadStory(userId, caption, type, filePartList);

        loginCall.enqueue(new Callback<SuccessResUploadStory>() {
            @Override
            public void onResponse(Call<SuccessResUploadStory> call, Response<SuccessResUploadStory> response) {
                DataManager.getInstance().hideProgressMessage();
                try {

                    SuccessResUploadStory data = response.body();
                    Log.e("data", data.status);
                    if (data.status.equals("1")) {
                        String dataResponse = new Gson().toJson(response.body());
                        Log.e("MapMap", "EDIT PROFILE RESPONSE" + dataResponse);
                        showToast(getActivity(), data.message);
                        dialog.dismiss();
                    } else if (data.status.equals("0")) {
                        showToast(getActivity(), data.message);

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e(TAG, "Test Response :" + response.body());
                    dialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<SuccessResUploadStory> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
                dialog.dismiss();
            }
        });

    }


    private Locale getLocale() {
        Configuration config = getResources().getConfiguration();
        Locale sysLocale = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            sysLocale = getSystemLocale(config);
        } else {
            sysLocale = getSystemLocaleLegacy(config);
        }

        return sysLocale;
    }

    @SuppressWarnings("deprecation")
    public static Locale getSystemLocaleLegacy(Configuration config){
        return config.locale;
    }

    @TargetApi(Build.VERSION_CODES.N)
    public static Locale getSystemLocale(Configuration config){
        return config.getLocales().get(0);
    }

    private void getStories() {
        String userId = SharedPreferenceUtility.getInstance(getContext()).getString(USER_ID);
        DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));
        Map<String, String> map = new HashMap<>();
        map.put("user_id", userId);
        Call<SuccessResGetStories> call = apiInterface.getStories(map);

        call.enqueue(new Callback<SuccessResGetStories>() {
            @Override
            public void onResponse(Call<SuccessResGetStories> call, Response<SuccessResGetStories> response) {

                DataManager.getInstance().hideProgressMessage();

                try {
                    SuccessResGetStories data = response.body();

                    Log.e("data", data.status);
                    if (data.status.equals("1")) {
                        String dataResponse = new Gson().toJson(response.body());
                        Log.e("MapMap", "EDIT PROFILE RESPONSE" + dataResponse);

                        haveStory = true;
                        storyID = data.getResult().get(0).getId();

                    } else if (data.status.equals("0")) {
                        //    showToast(getActivity(), data.message);
                        haveStory = false;

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

    public void updateStory(ArrayList<String> imagesVideosPathList, boolean image, String type1) {

        DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));

        List<MultipartBody.Part> filePartList = new LinkedList<>();

        if (image) {
            for (int i = 0; i < imagesVideosPathList.size(); i++) {
                File file = DataManager.getInstance().saveBitmapToFile(new File(imagesVideosPathList.get(i)));
                filePartList.add(MultipartBody.Part.createFormData("image[]", file.getName(), RequestBody.create(MediaType.parse("image[]/*"), file)));
            }
        } else {
            for (int i = 0; i < imagesVideosPathList.size(); i++) {
                //       File file = DataManager.getInstance().saveBitmapToFile(new File(imagesVideosPathList.get(i)));
                File file = new File(imagesVideosPathList.get(i));
                filePartList.add(MultipartBody.Part.createFormData("image[]", file.getName(), RequestBody.create(MediaType.parse("image[]/*"), file)));
            }
        }

        RequestBody userId = RequestBody.create(MediaType.parse("text/plain"), storyID);
        RequestBody type = RequestBody.create(MediaType.parse("text/plain"), type1);

        Call<SuccessResUploadStory> loginCall = apiInterface.updateStory(userId, type, filePartList);

        loginCall.enqueue(new Callback<SuccessResUploadStory>() {
            @Override
            public void onResponse(Call<SuccessResUploadStory> call, Response<SuccessResUploadStory> response) {
                DataManager.getInstance().hideProgressMessage();
                try {

                    SuccessResUploadStory data = response.body();

                    Log.e("data", data.status);
                    if (data.status.equals("1")) {
                        String dataResponse = new Gson().toJson(response.body());
                        Log.e("MapMap", "EDIT PROFILE RESPONSE" + dataResponse);
                        showToast(getActivity(), data.message);
                        dialog.dismiss();

                    } else if (data.status.equals("0")) {
                        showToast(getActivity(), data.message);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e(TAG, "Test Response :" + response.body());
                    dialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<SuccessResUploadStory> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
                dialog.dismiss();
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
                    Log.e("data",data.status);
                    if (data.status.equals("1")) {
                        String dataResponse = new Gson().toJson(response.body());
                        Log.e("MapMap", "EDIT PROFILE RESPONSE" + dataResponse);
                        userDetail = data.getResult();
                        openFanClub = userDetail.getOpenFanClub();

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
                        newUsersList.clear();
                        newUsersList.addAll(data.getResult());
                    } else if (data.status.equals("0")) {
                        showToast(getActivity(), data.message);
                        newUsersList.clear();
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

