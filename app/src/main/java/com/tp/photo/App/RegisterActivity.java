package com.tp.photo.App;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
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
import com.tp.photo.databinding.ActivityRegisterBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {
    private static final String TAG="RegisterActivity";
    private ActivityRegisterBinding binding;
    private PreferenceManager preferenceManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferenceManager=new PreferenceManager(getApplicationContext());
        binding=ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        if(isValidSignInDetalis()){
            setOnClickregister();
        }
        setOnclickLogin();
    }
    private void setOnClickregister(){
        loading(true);
        binding.buttonSignUp.setOnClickListener(v -> {
            final String name1 = binding.RegisterName.getText().toString().trim();
            final String email1 = binding.RegisterEmail.getText().toString().trim();
            final String password1 = binding.RegisterPassword.getText().toString().trim();

            StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.KEY_URL+Constants.KEY_REGISTER,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if(response!=null) {
                                preferenceManager.putString(Constants.KEY_NAME,name1);
                                preferenceManager.putString(Constants.KEY_EMAIL,email1);
                                preferenceManager.putString(Constants.KEY_USER_ID,response);
                                AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
                                builder.setMessage(response).setTitle(R.string.dialog_title);
                                builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });
                                AlertDialog dialog = builder.create();
                                dialog.show();
                                //startActivity(new Intent(getApplicationContext(),MainActivity.class));
                            }else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
                                builder.setMessage(R.string.Chek_email).setTitle(R.string.dialog_title);
                                builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                       dialog.cancel();
                                    }
                                });
                                AlertDialog dialog = builder.create();
                                dialog.show();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(RegisterActivity.this, "Register Error!" + error.toString(), Toast.LENGTH_SHORT).show();
                        }
                    })
            {
                @Override
                protected Map<String, String> getParams()throws AuthFailureError {
                    Map<String, String> params = new HashMap<String,String>();
                    params.put(Constants.KEY_NAME, name1);
                    params.put(Constants.KEY_EMAIL, email1);
                    params.put(Constants.KEY_PASSWORD, password1);
                    return params;
                }
            };
            VolleySingleton.getInstance(getApplicationContext()).getRequestQueue().add(stringRequest);
        });

    }
    private void setOnclickLogin(){
        binding.textSginIn.setOnClickListener(v -> {
            startActivity(new Intent(getApplicationContext(),LoginActivity.class));
        });
    }
    private void showToat( String message){
        Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT).show();;
    }
    private Boolean isValidSignInDetalis(){
        if(binding.RegisterEmail.getText().toString().trim().isEmpty()){
            showToat("Enter email");
            return false;
        }else if(!Patterns.EMAIL_ADDRESS.matcher(binding.RegisterEmail.getText().toString()).matches()){
            showToat("Enter valid email");
            return false;
        }else if(binding.RegisterPassword.getText().toString().trim().isEmpty()){
            showToat("Enter Password");
            return false;
        }else if(binding.RegisterPassword.getText().toString()!= binding.RegisterConfirmPassword.getText().toString()){
            showToat("Password ");
            return false;
        }else if(binding.RegisterName.getText().toString().trim().isEmpty()){
            showToat("Enter Name");
            return false;
        }
        else {
            return true;
        }
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