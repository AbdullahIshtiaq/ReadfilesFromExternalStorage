package com.example.readfilesfromexternalstorage;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;

import com.example.readfilesfromexternalstorage.databinding.ActivityGalleryBinding;

import java.util.ArrayList;

public class GalleryActivity extends AppCompatActivity {

    private ArrayList<ImageFolder> galleryFolderArray = new ArrayList<ImageFolder>();
    private ActivityGalleryBinding binding;
    private View view;
    private GalleryRecyclerViewAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        binding = ActivityGalleryBinding.inflate(getLayoutInflater());
        view = binding.getRoot();
        setContentView(view);
        binding.progressBarFolder.setVisibility(View.VISIBLE);

        Log.wtf("36", "GalleryActivity");

        galleryFolderArray = (ArrayList<ImageFolder>) getIntent().getSerializableExtra("GalleryFolderArray");

        //ArrayList<ImageFolder> folds = getPicturePaths();

        binding.progressBarFolder.setVisibility(View.GONE);
        LinearLayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        binding.recViewFolder.setLayoutManager(mLayoutManager);
        adapter = new GalleryRecyclerViewAdapter(galleryFolderArray, GalleryActivity.this);
        binding.recViewFolder.setAdapter(adapter);
    }

    private ArrayList<ImageFolder> getPicturePaths() {
        ArrayList<ImageFolder> picFolders = new ArrayList<>();
        ArrayList<String> picPaths = new ArrayList<>();
        Uri allImagesuri = android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        String[] projection = {MediaStore.Images.ImageColumns.DATA, MediaStore.Images.Media.DISPLAY_NAME,
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME, MediaStore.Images.Media.BUCKET_ID};
        Cursor cursor = this.getContentResolver().query(allImagesuri, projection, null, null, null);
        try {
            if (cursor != null) {
                cursor.moveToFirst();
            }
            do {
                ImageFolder folds = new ImageFolder();
                String name = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME));
                String folder = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME));
                String datapath = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA));

                //String folderpaths =  datapath.replace(name,"");
                String folderpaths = datapath.substring(0, datapath.lastIndexOf(folder + "/"));
                folderpaths = folderpaths + folder + "/";
                if (!picPaths.contains(folderpaths)) {
                    picPaths.add(folderpaths);

                    folds.setPath(folderpaths);
                    folds.setFolderName(folder);
                    folds.setFirstPic(datapath);//if the folder has only one picture this line helps to set it as first so as to avoid blank image in itemview
                    folds.addpics();
                    picFolders.add(folds);
                } else {
                    for (int i = 0; i < picFolders.size(); i++) {
                        if (picFolders.get(i).getPath().equals(folderpaths)) {
                            picFolders.get(i).setFirstPic(datapath);
                            picFolders.get(i).addpics();
                        }
                    }
                }
            } while (cursor.moveToNext());
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        for (int i = 0; i < picFolders.size(); i++) {
            Log.d("picture folders", picFolders.get(i).getFolderName() + " and path = " + picFolders.get(i).getPath() + " " + picFolders.get(i).getNumberOfPics());
        }

        //reverse order ArrayList
        ArrayList<ImageFolder> reverseFolders = new ArrayList<>();
        for (int i = picFolders.size() - 1; i > reverseFolders.size() - 1; i--) {
            reverseFolders.add(picFolders.get(i));
        }

        return picFolders;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //startActivity(new Intent(ImageActivity.this, MainActivity.class));
        //finish();
    }
}