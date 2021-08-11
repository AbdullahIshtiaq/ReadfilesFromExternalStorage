package com.example.readfilesfromexternalstorage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import com.example.readfilesfromexternalstorage.databinding.ActivityMainBinding;
import com.example.readfilesfromexternalstorage.databinding.ActivitySplashScreenBinding;

import java.util.List;

import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

public class SplashScreenActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks {

    private ActivitySplashScreenBinding binding;
    private View view;
    Animation topAnim, bottomAnim;

    private static int SPLASH_SCREEN = 3000;
    private SharedPreferences prefs;
    private boolean permission = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash_screen);

        binding = ActivitySplashScreenBinding.inflate(getLayoutInflater());
        view = binding.getRoot();
        setContentView(view);

        topAnim = AnimationUtils.loadAnimation(this, R.anim.top_animation);
        bottomAnim = AnimationUtils.loadAnimation(this, R.anim.bottom_animation);

        binding.imageViewSplash.setAnimation(topAnim);
        binding.textViewSplash.setAnimation(bottomAnim);

        prefs = this.getSharedPreferences("MY_PREFS_NAME", this.MODE_PRIVATE);
        String check = prefs.getString("myActivity", "first_time");

        if (check.contentEquals("first_time")) {
            SharedPreferences.Editor editor = getSharedPreferences("MY_PREFS_NAME", MODE_PRIVATE).edit();
            editor.putString("myActivity", "not_first");
            editor.commit();
            Toast.makeText(SplashScreenActivity.this, "First time run", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(SplashScreenActivity.this, "Not first time", Toast.LENGTH_LONG).show();
        }

        // startActivity();

    }

    private void checkPermissions() {
        if (EasyPermissions.hasPermissions(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            Log.wtf("43", "Has Permission");
            permission = true;
        } else {
            Log.wtf("46", "Not Has Permission");
            EasyPermissions.requestPermissions(this, "App needs access to storage",
                    101, Manifest.permission.READ_EXTERNAL_STORAGE);
        }
    }

    private void startActivity() {
        Log.wtf("79", "onStartActivity");
        checkPermissions();
        if (permission) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(SplashScreenActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }, SPLASH_SCREEN);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.wtf("136", "Request Permission");
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
        Log.wtf("142", "On Permission Granted");
        if (requestCode == 101) {
            Log.wtf("144", "On Has Permission");
            permission = true;
            startActivity();
        }
    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
//        if(EasyPermissions.somePermissionPermanentlyDenied(this, perms)){
//            //Log.wtf("152", "Not Has Permission Permanently");
//
//        }else{
//            Log.wtf("156", "Not Has Permission");
//            //new AppSettingsDialog.Builder(this).build().show();
//            // Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
//        }
        new AppSettingsDialog.Builder(this).build().show();
        startActivity();
        //getAllDir(Environment.getExternalStorageDirectory());

    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.wtf("128", "onStart");
        startActivity();
    }
}