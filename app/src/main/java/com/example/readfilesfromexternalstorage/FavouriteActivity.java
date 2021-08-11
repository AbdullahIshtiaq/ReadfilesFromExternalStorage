package com.example.readfilesfromexternalstorage;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.view.View;

import com.example.readfilesfromexternalstorage.databinding.ActivityFavouriteBinding;
import com.example.readfilesfromexternalstorage.databinding.ActivityImageBinding;

import java.io.File;
import java.util.ArrayList;

public class FavouriteActivity extends AppCompatActivity {

    private ArrayList<MyFile> favouriteArray = new ArrayList<MyFile>();
    private ArrayList<File> selectedItemsArray = new ArrayList<File>();
    private ArrayList<FavouriteFiles> myFavouriteFilesArray = new ArrayList<FavouriteFiles>();
    private ActivityFavouriteBinding binding;
    private View view;
    private GeneralRecyclerViewAdapter adapter;
    private RoomDB database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite);

        binding = ActivityFavouriteBinding.inflate(getLayoutInflater());
        view = binding.getRoot();
        setContentView(view);

        binding.titleTVFavourite.setText("Favourites");

        database = RoomDB.getInstance(FavouriteActivity.this);

        myFavouriteFilesArray = (ArrayList<FavouriteFiles>) database.favouriteFilesDao().getAllFavouriteFiles();

        for(int i=0; i<myFavouriteFilesArray.size(); i++){
            File file = new File(myFavouriteFilesArray.get(i).getFilePath());
            MyFile obj = new MyFile(file, false, myFavouriteFilesArray.get(i).getDataType());
            favouriteArray.add(obj);
        }


        LinearLayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        binding.recViewFavourite.setLayoutManager(mLayoutManager);
        GeneralRecyclerViewAdapter.OnAdapterItemClickListener listener =
                new GeneralRecyclerViewAdapter.OnAdapterItemClickListener() {
                    @Override
                    public void onItemClicked(String text) {

                    }

                    @Override
                    public void setFavouriteVisibility(int visibility) {

                    }
                };
        adapter = new GeneralRecyclerViewAdapter(favouriteArray, FavouriteActivity.this,
                listener, selectedItemsArray);
        binding.recViewFavourite.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //startActivity(new Intent(ImageActivity.this, MainActivity.class));
        //finish();
    }
}