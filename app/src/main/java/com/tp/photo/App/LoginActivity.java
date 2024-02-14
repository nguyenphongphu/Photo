package com.tp.photo.App;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import com.tp.photo.Api.ApiInterface;
import com.tp.photo.Model.User;
import com.tp.photo.R;
import com.tp.photo.Utility.Constants;
import com.tp.photo.Utility.PreferenceManager;
import com.tp.photo.databinding.ActivityLoginBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG="LoginActivity";
    private ActivityLoginBinding binding;
    private PreferenceManager preferenceManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferenceManager=new PreferenceManager(getApplicationContext());
        //check user load mainActivity
        if(preferenceManager.getBoolean(Constants.KEY_IS_SIGNED_IN)){
            Intent intent=new Intent(getApplicationContext(),MainActivity.class);
            startActivity(intent);
            finish();
        }
        binding=ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        OnClick();
    }
    private void OnClick(){
        binding.textCreateNewAccount.setOnClickListener(v->
                startActivity(new Intent(getApplicationContext(),RegisterActivity.class)));
        binding.buttonSignIn.setOnClickListener(v->{
            if(isValidSignInDetalis()){
                SignIn();
            }
        });
    }
    private void SignIn(){
        //loading(true);
        String email= binding.inputAccount.getText().toString().trim();
        String password=binding.inputPassword.getText().toString().trim();
        ApiInterface.API_INTERFACE.login(email,password).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                User user = response.body();
                if (user != null) {
                    preferenceManager.putBoolean(Constants.KEY_IS_SIGNED_IN, true);
                    preferenceManager.putString(Constants.KEY_USER_ID, user.getUser_id());
                    preferenceManager.putString(Constants.KEY_NAME, user.getUsername());
                    preferenceManager.putString(Constants.KEY_EMAIL, user.getEmail());
                    preferenceManager.putString(Constants.KEY_IMAGE, user.getImageuser());
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                } else {
                    Log.d(TAG, "onResponse: " + response.body());
                }

            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

            }
        });



    }
    private void showToat( String message){
        Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT).show();;
    }
    private Boolean isValidSignInDetalis(){
        if(binding.inputAccount.getText().toString().trim().isEmpty()){
            showToat("Enter email");
            return false;
        }else if(!Patterns.EMAIL_ADDRESS.matcher(binding.inputAccount.getText().toString()).matches()){
            showToat("Enter valid email");
            return false;
        }else if(binding.inputPassword.getText().toString().trim().isEmpty()){
            showToat("Enter Password");
            return false;
        }else{
            return true;
        }
    }
    private void loading(Boolean isloading ){
        if(isloading){
            binding.buttonSignIn.setVisibility(View.INVISIBLE);
            binding.progressbar.setVisibility(View.VISIBLE);
        }else{
            binding.buttonSignIn.setVisibility(View.VISIBLE);
            binding.progressbar.setVisibility(View.INVISIBLE);
        }
    }
}