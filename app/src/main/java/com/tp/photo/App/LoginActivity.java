package com.tp.photo.App;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.tp.photo.R;
import com.tp.photo.Utility.Constants;
import com.tp.photo.Utility.PreferenceManager;
import com.tp.photo.Utility.VolleySingleton;
import com.tp.photo.databinding.ActivityLoginBinding;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

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
        loading(true);
        final String email = binding.inputAccount.getText().toString().trim();
        final String password = binding.inputPassword.getText().toString().trim();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.KEY_URL+"/login", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray= jsonObject.getJSONArray("data");
                    JSONObject success=new JSONObject(jsonArray.getString(0));
                    Log.i(TAG, "onResponse: "+success);
                    if(success.length()>0){
                        preferenceManager.putBoolean(Constants.KEY_IS_SIGNED_IN,true);
                        preferenceManager.putString(Constants.KEY_USER_ID,success.getString(Constants.KEY_USER_ID));
                        preferenceManager.putString(Constants.KEY_NAME,success.getString(Constants.KEY_NAME));
                        preferenceManager.putString(Constants.KEY_EMAIL,success.getString(Constants.KEY_EMAIL));
                        Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }
                }catch (Exception e){
                    Log.i("", "eroo: "+e);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }
        ){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String,String>();
                params.put("email", email);
                params.put("password", password);
                return params;
            }
        };
        VolleySingleton.getInstance(getApplicationContext()).getRequestQueue().add(stringRequest);
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