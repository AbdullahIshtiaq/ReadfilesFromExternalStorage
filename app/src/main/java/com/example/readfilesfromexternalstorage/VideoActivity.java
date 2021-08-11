package com.example.readfilesfromexternalstorage;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.readfilesfromexternalstorage.databinding.ActivityImageBinding;
import com.example.readfilesfromexternalstorage.databinding.ActivityVideoBinding;

import java.io.File;
import java.util.ArrayList;

public class VideoActivity extends AppCompatActivity {

    private ArrayList<MyFile> videoArray = new ArrayList<MyFile>();
    private ArrayList<File> selectedItemsArray = new ArrayList<>();
    private ArrayList<FavouriteFiles> myFavouriteFilesArray = new ArrayList<FavouriteFiles>();
    private ActivityVideoBinding binding;
    private View view;
    private GeneralRecyclerViewAdapter adapter;
    private RoomDB database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        binding = ActivityVideoBinding.inflate(getLayoutInflater());
        view = binding.getRoot();
        setContentView(view);

        videoArray = (ArrayList<MyFile>)getIntent().getSerializableExtra("VideoArray");

        LinearLayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        binding.recViewVideo.setLayoutManager(mLayoutManager);
        GeneralRecyclerViewAdapter.OnAdapterItemClickListener listener = new GeneralRecyclerViewAdapter.OnAdapterItemClickListener() {
            @Override
            public void onItemClicked(String text) {
                binding.titleTvVideo.setText(text);
            }

            @Override
            public void setFavouriteVisibility(int visibility){
                if (visibility == View.VISIBLE) {
                    binding.linearLayoutVideos.setVisibility(View.VISIBLE);
                } else {
                    binding.linearLayoutVideos.setVisibility(View.GONE);
                }

            };
        };
        adapter = new GeneralRecyclerViewAdapter(videoArray, VideoActivity.this,
                listener,selectedItemsArray);
        binding.recViewVideo.setAdapter(adapter);

        binding.linearLayoutVideos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (binding.linearLayoutVideos.getVisibility() == View.VISIBLE) {
                    database = RoomDB.getInstance(VideoActivity.this);

                    myFavouriteFilesArray = (ArrayList<FavouriteFiles>) database.favouriteFilesDao().getAllFavouriteFiles();


                    for (int i = 0; i < selectedItemsArray.size(); i++) {

                        boolean found = false;
                        for (int j = 0; j < myFavouriteFilesArray.size(); j++) {

                            if (selectedItemsArray.get(i).getPath()
                                    .contentEquals(myFavouriteFilesArray.get(j).getFilePath())) {
                                found = true;
                                break;
                            }
                        }
                        if (!found) {
                            String dataType = "";
                            if (selectedItemsArray.get(i).getName().endsWith(".jpg") || selectedItemsArray.get(i).getName().endsWith(".png")
                                    || selectedItemsArray.get(i).getName().endsWith(".jpeg") || selectedItemsArray.get(i).getName().endsWith(".bmp")) {
                                dataType="Images";
                            }
                            if (selectedItemsArray.get(i).getName().endsWith(".txt") || selectedItemsArray.get(i).getName().endsWith(".pdf")
                                    || selectedItemsArray.get(i).getName().endsWith(".doc") || selectedItemsArray.get(i).getName().endsWith(".docx")
                                    || selectedItemsArray.get(i).getName().endsWith(".xml")) {
                                dataType="Documents";
                            }
                            if (selectedItemsArray.get(i).getName().endsWith(".mp3") || selectedItemsArray.get(i).getName().endsWith(".wav")) {
                                dataType="Audios";
                            }
                            if (selectedItemsArray.get(i).getName().endsWith(".mp4") || selectedItemsArray.get(i).getName().endsWith(".mkv")
                                    || selectedItemsArray.get(i).getName().endsWith(".wmv") || selectedItemsArray.get(i).getName().endsWith(".mov")) {
                                dataType="Videos";
                            }

                            FavouriteFiles obj = new FavouriteFiles(selectedItemsArray.get(i).getName()
                                    , selectedItemsArray.get(i).getPath(), dataType);

                            Log.wtf("81: ", "Adding " + selectedItemsArray.get(i).getName());
                            database.favouriteFilesDao().insert(obj);
                        }

                    }

                    binding.linearLayoutVideos.setVisibility(View.GONE);
                    selectedItemsArray.clear();
                    binding.titleTvVideo.setText("Videos");
                    adapter.update();
                    binding.recViewVideo.setAdapter(adapter);
                }
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //startActivity(new Intent(VideoActivity.this, MainActivity.class));
        //finish();
    }
}