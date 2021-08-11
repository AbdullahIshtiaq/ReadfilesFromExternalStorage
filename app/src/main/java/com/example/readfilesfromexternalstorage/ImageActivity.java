package com.example.readfilesfromexternalstorage;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.readfilesfromexternalstorage.databinding.ActivityImageBinding;

import java.io.File;
import java.util.ArrayList;

public class ImageActivity extends AppCompatActivity {

    private ArrayList<MyFile> imageArray = new ArrayList<MyFile>();
    private ArrayList<File> selectedItemsArray = new ArrayList<File>();
    private ArrayList<FavouriteFiles> myFavouriteFilesArray = new ArrayList<FavouriteFiles>();
    private ActivityImageBinding binding;
    private View view;
    private GeneralRecyclerViewAdapter adapter;
    private RoomDB database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);

        binding = ActivityImageBinding.inflate(getLayoutInflater());
        view = binding.getRoot();
        setContentView(view);

        binding.titleTVImage.setText("Images");

        imageArray = (ArrayList<MyFile>) getIntent().getSerializableExtra("ImageArray");

        LinearLayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        binding.recViewImage.setLayoutManager(mLayoutManager);
        GeneralRecyclerViewAdapter.OnAdapterItemClickListener listener =
                new GeneralRecyclerViewAdapter.OnAdapterItemClickListener() {
                    @Override
                    public void onItemClicked(String text) {
                        binding.titleTVImage.setText(text);
                    }

                    @Override
                    public void setFavouriteVisibility(int visibility) {
                        if (visibility == View.VISIBLE) {
                            binding.linearLayoutImages.setVisibility(View.VISIBLE);
                        } else {
                            binding.linearLayoutImages.setVisibility(View.GONE);
                        }
                    }
                };
        adapter = new GeneralRecyclerViewAdapter(imageArray, ImageActivity.this,
                listener, selectedItemsArray);
        binding.recViewImage.setAdapter(adapter);


        binding.linearLayoutImages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (binding.linearLayoutImages.getVisibility() == View.VISIBLE) {
                    database = RoomDB.getInstance(ImageActivity.this);

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

                    binding.linearLayoutImages.setVisibility(View.GONE);
                    selectedItemsArray.clear();
                    binding.titleTVImage.setText("Images");
                    adapter.update();
                    binding.recViewImage.setAdapter(adapter);
                }
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //startActivity(new Intent(ImageActivity.this, MainActivity.class));
        //finish();
    }
}