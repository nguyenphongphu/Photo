package com.tp.photo.App;

import static java.util.Collections.swap;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewDebug;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.karumi.dexter.listener.single.PermissionListener;
import com.tp.photo.Adapter.GalleryAdapter;
import com.tp.photo.Api.ApiInterface;
import com.tp.photo.Model.ListData;
import com.tp.photo.Model.Photo;
import com.tp.photo.R;
import com.tp.photo.Utility.Constants;
import com.tp.photo.Utility.PaginationScrollListener;
import com.tp.photo.Utility.selectionSort;
import com.tp.photo.Utility.PreferenceManager;
import com.tp.photo.Utility.RealPathUtil;
import com.tp.photo.databinding.ActivityMainBinding;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private PreferenceManager preferenceManager;
    //private GalleryAdapter galleryAdapter;
    private GalleryAdapter dataAdapter;
    private final String DIRECTORY = Environment.getExternalStorageDirectory().toString();
    private static final String TAG="MainActivity";
    private List<String> mFiles;
    private LinearLayoutManager linearLayoutManager;
    private boolean isLoading;
    private boolean isLastPage;
    private int currentPage=1;
    private int totalPage=5;
    private int intFile;
    private List<ListData> listData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferenceManager =new PreferenceManager(getApplicationContext());
        binding=ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        listData=new ArrayList<>();
        linearLayoutManager=new LinearLayoutManager(this);
        binding.recyclerview.setLayoutManager(linearLayoutManager);
        loadUserDetails();
        SetOnClick();
    }
    private void loadUserDetails() {
        try {

            Glide.with(this).load(preferenceManager.getString(Constants.KEY_IMAGE))
                    .placeholder(R.drawable.baseline_account_circle_24)
                    .error(R.drawable.baseline_account_circle_24)
                    .into(binding.imageAdd);
        }catch (Exception e){
            Log.d(TAG,e.toString());
        }

    }
    private void SetOnClick(){
        binding.imageOut.setOnClickListener(v -> {
            preferenceManager.clear();
            startActivity((new Intent(getApplicationContext(), LoginActivity.class)));
            finish();
        });
        binding.imageAdd.setOnClickListener(v -> {
            menu(v);
        });
        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED){
            requestStoragePermission();
        }else {
            loadData();
            Log.i(TAG, "SetOnClick: ");
        }
    }

    /**
     * Requesting multiple permissions (storage and location) at once
     * This uses multiple permission model from dexter
     * On permanent denial opens settings dialog
     */
    private void requestStoragePermission() {
        Dexter.withContext(this)
                .withPermissions(
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {

                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {

                        if (report.areAllPermissionsGranted()) {
                            loadData();
                        }
                        // check for permanent denial of any permission
                        if (report.isAnyPermissionPermanentlyDenied()) {
                            // show alert dialog navigating to Settings
                            showSettingsDialog();
                        }
                    }
                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {

                        token.continuePermissionRequest();
                    }
                }).
                withErrorListener(new PermissionRequestErrorListener() {
                    @Override
                    public void onError(DexterError error) {
                        Toast.makeText(getApplicationContext(), "Error occurred! ", Toast.LENGTH_SHORT).show();
                    }
                })
                .onSameThread()
                .check();
    }

    /**
     * Requesting camera permission
     * This uses single permission model from dexter
     * Once the permission granted, opens the camera
     * On permanent denial opens settings dialog
     */
    private void requestCameraPermission() {
        Dexter.withContext(this)
                .withPermission(Manifest.permission.CAMERA)
                .withListener(new PermissionListener() {

                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        // permission is granted
                        openCamera();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        // check for permanent denial of permission
                        if (response.isPermanentlyDenied()) {
                            showSettingsDialog();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();
    }

    /**
     * Showing Alert Dialog with Settings option
     * Navigates user to app settings
     * NOTE: Keep proper title and message depending on your app
     */
    private void showSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Need Permissions");
        builder.setMessage("This app needs permission to use this feature. You can grant them in app settings.");
        builder.setPositiveButton("GOTO SETTINGS", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                openSettings();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();

    }

    // navigating user to app settings
    private void openSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, 101);
    }

    private void openCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, 100);
    }
    private void loadData() {
       // mFiles=RealPathUtil.findFileInDirectory(DIRECTORY,new String[]{"png", "jpg","jpeg","webp","mp4"});
        mFiles = RealPathUtil.findImageFileInDirectory(DIRECTORY, new String[]{"png", "jpg","jpeg","webp","mp4"});
        intFile=mFiles.size()/5;
        for(int i=0;i<intFile;i++){
            if(mFiles.get(i)!=null){
                List<String> list1 = new ArrayList<>();
                list1.add(mFiles.get(i));
                File file = new File(mFiles.get(i));
                Date lastModDate = new Date(file.lastModified());
                mFiles.remove(i);
                for (int j = 1; j <= intFile; j++) {
                    if(mFiles.get(j)!=null){
                        File filej = new File(mFiles.get(j));
                        Date lastModDatej = new Date(filej.lastModified());
                        if (lastModDatej.getDay()==lastModDate.getDay()&&lastModDatej.getMonth()==lastModDate.getMonth()&&lastModDate.getYear()==lastModDatej.getYear()) {
                            list1.add(mFiles.get(j));
                            mFiles.remove(j);
                        }
                    }
                }
                listData.add(new ListData(new SimpleDateFormat("dd-MM").format(lastModDate), list1));
            }
        }
        dataAdapter =new GalleryAdapter(MainActivity.this,listData);
        binding.recyclerview.setAdapter(dataAdapter);
        binding.recyclerview.addOnScrollListener(new PaginationScrollListener(linearLayoutManager) {
            @Override
            public void loadMoreItem() {
                isLoading=true;
                binding.progressbar.setVisibility(View.VISIBLE);
                currentPage+=1;
                loadNextPage();
            }

            @Override
            public boolean isloading() {
                return isLoading;
            }

            @Override
            public boolean isLastPage() {
                return isLastPage;
            }
        });
        Toast.makeText(getApplicationContext(), "All permissions are granted!", Toast.LENGTH_SHORT).show();
        binding.recyclerview.setHasFixedSize(true);
        binding.progressbar.setVisibility(View.INVISIBLE);
    }
    private void loadNextPage(){
        Handler handler=new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                dataAdapter.notifyDataSetChanged();
                isLoading=false;
                binding.progressbar.setVisibility(View.GONE);
                if(currentPage==totalPage){
                    isLastPage=true;
                }
            }
        },2000);

    }
    private void loadserver( List<String> mfile){
        Log.d(TAG, "load: " +mfile.size());
        final String email = preferenceManager.getString(Constants.KEY_EMAIL);
        RequestBody bodyemail=RequestBody.create(MediaType.parse("multipart/form-data"),email);
        for(int i=0;i<mfile.size();i++)
        {
            File file=new File(mfile.get(i));
            RequestBody bodyimage=RequestBody.create(MediaType.parse("multipart/form-data"),file);
            MultipartBody.Part part=MultipartBody.Part.createFormData(Constants.KEY_DATA,file.getName(),bodyimage);
            ApiInterface.API_INTERFACE.upload(bodyemail,part).enqueue(new Callback<Photo>() {
                @Override
                public void onResponse(Call<Photo> call, Response<Photo> response) {
                    SystemClock.sleep(5000);
                }

                @Override
                public void onFailure(Call<Photo> call, Throwable t) {
                    Log.d(TAG, "onFailure: " +t);
                }
            });
        }
    }

    private void menu(View v){
        PopupMenu popupMenu=new PopupMenu(this,v);
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int i=  item.getItemId();
                if(R.id.item1==i){
                    try {
                        //loadserver(mFiles);

                    }catch (Exception e){
                        Log.d(TAG, "Upload_data: loi "+e);
                    }
                    return true;
                }
                if (R.id.item2==i){
                    Intent intent1 = new Intent(getApplicationContext(), SettingActivity.class);
                    startActivity(intent1);
                    return true;
                }
                return false;
            }
        });
        popupMenu.inflate(R.menu.menu);
        popupMenu.show();
    }

}