package com.tp.photo.App;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Base64;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.tp.photo.Api.ApiInterface;
import com.tp.photo.Model.User;
import com.tp.photo.R;
import com.tp.photo.Utility.Constants;
import com.tp.photo.Utility.PreferenceManager;
import com.tp.photo.Utility.RealPathUtil;
import com.tp.photo.databinding.ActivityRegisterBinding;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    private static final String TAG="RegisterActivity";
    private ActivityRegisterBinding binding;
    private PreferenceManager preferenceManager;
    private String encodeImage;
    private Uri mUri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferenceManager=new PreferenceManager(getApplicationContext());
        binding=ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        try {
            setOnClickregister();
        }catch (Exception e){
            Log.d(TAG, "onCreate: 11 "+ e.toString());
        }


        setOnclickLogin();
    }
    private Boolean isValidSignInDetalis(){
        if(binding.RegisterEmail.getText()==null){
            showToat("Enter email");
            return false;
        }else if(!Patterns.EMAIL_ADDRESS.matcher(binding.RegisterEmail.getText().toString()).matches()){
            showToat("Enter valid email");
            return false;
        }else if(binding.RegisterPassword.getText().toString().trim().isEmpty()){
            showToat("Enter Password");
            return false;
        }else if(binding.RegisterPassword.getText().toString().trim()==
                binding.RegisterConfirmPassword.getText().toString().trim()){
            Log.d(TAG, "isValidSignInDetalis: "+binding.RegisterPassword.getText().toString().trim()+
                    binding.RegisterConfirmPassword.getText().toString().trim());
            showToat("Password ");
            return false;
        }else if(binding.RegisterName.getText().toString().trim().isEmpty()){
            showToat("Enter Name");
            return false;
        } else {
            return true;
        }
    }
    private void setOnClickregister(){

        binding.buttonSignUp.setOnClickListener(v -> {
            if(isValidSignInDetalis()){
                loading(true);
                final String name1 = binding.RegisterName.getText().toString().trim();
                final String email1 = binding.RegisterEmail.getText().toString().trim();
                final String password1 = binding.RegisterPassword.getText().toString().trim();
                RequestBody bodyname=RequestBody.create(MediaType.parse("multipart/form-data"),name1);
                RequestBody bodyemail=RequestBody.create(MediaType.parse("multipart/form-data"),email1);
                RequestBody bodypasswrod=RequestBody.create(MediaType.parse("multipart/form-data"),password1);
                String urlPath;
                File file = null;
                MultipartBody.Part part;
                if(mUri!=null){
                   urlPath = RealPathUtil.getRealPath(this,mUri);
                    Log.d(TAG, "setOnClickregister: "+urlPath);
                   file =new File(urlPath);
                    RequestBody bodyimage=RequestBody.create(MediaType.parse("multipart/form-data"),file);
                    part=MultipartBody.Part.createFormData(Constants.KEY_IMAGE,file.getName(),bodyimage);
                }else {

                    part=null;
                }
                ApiInterface.API_INTERFACE.register(bodyname,bodyemail,bodypasswrod,part).enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        User user = response.body();
                        if(user!=null){
                            preferenceManager.putString(Constants.KEY_NAME,name1);
                            preferenceManager.putString(Constants.KEY_EMAIL,email1);
                            preferenceManager.putString(Constants.KEY_USER_ID,user.getUser_id());
                            preferenceManager.putString(Constants.KEY_IMAGE,encodeImage);
                            startActivity(new Intent(getApplicationContext(),MainActivity.class));
                            finish();
                        }else {
                            Log.d(TAG, "onResponse: ");
                        }
                        
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {

                    }
                });
            }else {
                loading(false);
            }

        });

    }
    private String encodeImage(Bitmap bitmap){
        int previewidth =150;
        int previewheight=bitmap.getHeight()*previewidth/bitmap.getWidth();
        Bitmap previewBitmat =Bitmap.createScaledBitmap(bitmap,previewidth,previewheight,false);
        ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
        previewBitmat.compress(Bitmap.CompressFormat.JPEG,50,byteArrayOutputStream);
        byte[] bytes=byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(bytes,Base64.DEFAULT);
    }
    private final ActivityResultLauncher<Intent> pickImage= registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), result -> {
                if(result.getResultCode()==RESULT_OK){
                    if( result.getData()!= null){
                        Uri imageUri =result.getData().getData();
                        mUri=imageUri;
                        Log.d(TAG, "ActivityResultLauncher: "+result.getData().getData());
                        try {
                            InputStream inputStream= getContentResolver().openInputStream(imageUri);
                            Bitmap bitmap= BitmapFactory.decodeStream(inputStream);
                            binding.imageProfile.setImageBitmap(bitmap);
                            binding.textimage.setVisibility(View.GONE);
                            encodeImage=encodeImage(bitmap);
                            Log.i(TAG, encodeImage.toString());
                        } catch (FileNotFoundException e) {

                            e.printStackTrace();
                        }
                    }
                }
            }
    );
    private void setOnclickLogin(){
        binding.textSginIn.setOnClickListener(v -> {
            startActivity(new Intent(getApplicationContext(),LoginActivity.class));
        });
        binding.imageProfile.setOnClickListener(v->{
            Intent intent=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            pickImage.launch(intent);
        });
    }
    private void requestStoragePermission() {
        Dexter.withContext(this)
                .withPermissions(
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {

                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {

                        // check for permanent denial of any permission
                        if (report.isAnyPermissionPermanentlyDenied()) {
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
    private void showSettingsDialog() {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(RegisterActivity.this);
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
    private void openSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, 101);
    }

    private void showToat( String message){
        Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT).show();;
    }

    private void loading(Boolean isloading ){
        if(isloading){
            binding.buttonSignUp.setVisibility(View.INVISIBLE);
            binding.progressbar.setVisibility(View.VISIBLE);
        }else{
            binding.buttonSignUp.setVisibility(View.VISIBLE);
            binding.progressbar.setVisibility(View.INVISIBLE);
        }
    }
}