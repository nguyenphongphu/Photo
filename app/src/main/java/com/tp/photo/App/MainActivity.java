package com.tp.photo.App;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;

import android.os.Bundle;
import android.widget.Toast;

import com.tp.photo.R;
import com.tp.photo.Utility.PreferenceManager;
import com.tp.photo.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private PreferenceManager preferenceManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferenceManager =new PreferenceManager(getApplicationContext());
        binding=ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        SetOnClick();
    }
    private void SetOnClick(){
        binding.imageOut.setOnClickListener(v -> {
            preferenceManager.clear();
            startActivity((new Intent(getApplicationContext(), LoginActivity.class)));
            finish();
        });
        binding.imageAdd.setOnClickListener(v -> {

        });
    }


}