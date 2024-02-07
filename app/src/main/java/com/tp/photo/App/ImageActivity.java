package com.tp.photo.App;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.tp.photo.R;
import com.tp.photo.databinding.ActivityImageBinding;

public class ImageActivity extends AppCompatActivity {
    private ActivityImageBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityImageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Intent intent=this.getIntent();

        binding.text.setText(intent.getStringExtra("Image"));

    }
}