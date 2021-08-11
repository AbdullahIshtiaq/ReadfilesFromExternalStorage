package com.example.readfilesfromexternalstorage;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.readfilesfromexternalstorage.databinding.ActivityAudioBinding;
import com.example.readfilesfromexternalstorage.databinding.ActivityImageBinding;

import java.io.File;
import java.util.ArrayList;

public class AudioActivity extends AppCompatActivity {

    private ArrayList<MyFile> audioArray = new ArrayList<MyFile>();
    private ArrayList<File> selectedItemsArray = new ArrayList<>();
    private ArrayList<FavouriteFiles> myFavouriteFilesArray = new ArrayList<FavouriteFiles>();
    private ActivityAudioBinding binding;
    private View view;
    private GeneralRecyclerViewAdapter adapter;
    private RoomDB database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio);

        binding = ActivityAudioBinding.inflate(getLayoutInflater());
        view = binding.getRoot();
        setContentView(view);

        audioArray = (ArrayList<MyFile>)getIntent().getSerializableExtra("AudioArray");

        LinearLayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        binding.recViewAudio.setLayoutManager(mLayoutManager);
        GeneralRecyclerViewAdapter.OnAdapterItemClickListener listener = new GeneralRecyclerViewAdapter.OnAdapterItemClickListener() {
            @Override
            public void onItemClicked(String text) {
                binding.titleTVAudio.setText(text);
            }
            @Override
            public void setFavouriteVisibility(int visibility){
                if (visibility == View.VISIBLE) {
                    binding.linearLayoutAudios.setVisibility(View.VISIBLE);
                } else {
                    binding.linearLayoutAudios.setVisibility(View.GONE);
                }
            };
        };
        adapter = new GeneralRecyclerViewAdapter(audioArray, AudioActivity.this,
                listener,selectedItemsArray);
        binding.recViewAudio.setAdapter(adapter);

        binding.linearLayoutAudios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (binding.linearLayoutAudios.getVisibility() == View.VISIBLE) {
                    database = RoomDB.getInstance(AudioActivity.this);

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

                    binding.linearLayoutAudios.setVisibility(View.GONE);
                    selectedItemsArray.clear();
                    binding.titleTVAudio.setText("Audios");
                    adapter.update();
                    binding.recViewAudio.setAdapter(adapter);
                }
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //startActivity(new Intent(AudioActivity.this, MainActivity.class));
        //finish();
    }
}