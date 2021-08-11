package com.example.readfilesfromexternalstorage;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import com.example.readfilesfromexternalstorage.databinding.ActivityShowGalleryDataBinding;

import java.io.File;
import java.util.ArrayList;

public class ShowGalleryDataActivity extends AppCompatActivity {

    private ArrayList<File> myArray = new ArrayList<File>() ;
    private ActivityShowGalleryDataBinding binding;
    private View view;
    private ShowGalleryDataRecyclerViewAdapter adapter;

    private Thread thread = null;
    private Handler mainHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_gallery_data);

        binding = ActivityShowGalleryDataBinding.inflate(getLayoutInflater());
        view = binding.getRoot();
        setContentView(view);

        binding.progressBarShowFolderData.setVisibility(View.VISIBLE);

        Log.wtf("32", "ShowFolderData Activity");

        String name = getIntent().getStringExtra("Name");
        String path = getIntent().getStringExtra("Path");

        binding.titleTVShowFolderData.setText(name);

        File file = new File(path);
        getAllDir(file);

        settingAdapter();

//        thread = new Thread(new Runnable() {
//            @Override
//            public void run() {
//                getAllDir(file);
//                Log.wtf("51", "MyArray Size: "+myArray.size());
//                mainHandler.post(new Runnable() {
//                    @Override
//                    public void run() {
//                        //myArray = filterArray(path);
//                        binding.progressBarShowFolderData.setVisibility(View.GONE);
//                        binding.recViewShowFolderData.setVisibility(View.VISIBLE);
//                        settingAdapter();
//                    }
//                });
//                thread.interrupt();
//            }
//        });
//        thread.start();
//        Log.wtf("64", "Outside Thread");
    }

    private void settingAdapter() {
        LinearLayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        binding.recViewShowFolderData.setLayoutManager(mLayoutManager);
        adapter = new ShowGalleryDataRecyclerViewAdapter(myArray, ShowGalleryDataActivity.this);
        binding.recViewShowFolderData.setAdapter(adapter);
        binding.progressBarShowFolderData.setVisibility(View.GONE);
        binding.recViewShowFolderData.setVisibility(View.VISIBLE);
    }

    private void getAllDir(File dir) {
        File listFile[] = dir.listFiles();
        if (listFile != null) {
            Log.wtf("83: ", "Fetching Data");
            for (int i = 0; i < listFile.length; i++) {
                if (!listFile[i].isDirectory()) {
                    myArray.add(listFile[i]);
                }
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //startActivity(new Intent(ImageActivity.this, MainActivity.class));
        //finish();
    }
}