package com.example.readfilesfromexternalstorage;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;

import com.example.readfilesfromexternalstorage.databinding.ActivityMainBinding;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements Serializable {

    private ActivityMainBinding binding;
    private View view;

    private ArrayList<MyFile> imageArray = new ArrayList<MyFile>();
    private ArrayList<MyFile> textArray = new ArrayList<MyFile>();
    private ArrayList<MyFile> videoArray = new ArrayList<MyFile>();
    private ArrayList<MyFile> audioArray = new ArrayList<MyFile>();
    private ArrayList<ImageFolder> galleryFolders = new ArrayList<ImageFolder>();

    private Handler mainHandler = new Handler();
    private Thread thread = null;

    private boolean scanningComplete = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        view = binding.getRoot();
        setContentView(view);

        binding.progressBarMain.setVisibility(View.VISIBLE);

        Log.wtf("46", "Main Activity");

        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                getAllDir(Environment.getExternalStorageDirectory());
                galleryFolders = getPicturePaths();
                scanningComplete = true;
                mainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        binding.scanningTVMain.setVisibility(View.GONE);
                        binding.progressBarMain.setVisibility(View.GONE);
                        binding.folderTVMain.setText("" + galleryFolders.size());
                    }
                });

                thread.interrupt();
            }
        });
        thread.start();

        binding.imgBtnMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (scanningComplete) {
                    Intent imgIntent = new Intent(MainActivity.this, ImageActivity.class);
                    imgIntent.putExtra("ImageArray", imageArray);
                    startActivity(imgIntent);
                    //finish();
                }
            }
        });

        binding.docBtnMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (scanningComplete) {
                    Intent docIntent = new Intent(MainActivity.this, DocumentActivity.class);
                    docIntent.putExtra("TextArray", textArray);
                    startActivity(docIntent);
                    //finish();
                }
            }
        });

        binding.audioBtnMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (scanningComplete) {
                    Intent audioIntent = new Intent(MainActivity.this, AudioActivity.class);
                    audioIntent.putExtra("AudioArray", audioArray);
                    startActivity(audioIntent);
                    //finish();
                }

            }
        });

        binding.videoBtnMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (scanningComplete) {
                    Intent videoIntent = new Intent(MainActivity.this, VideoActivity.class);
                    videoIntent.putExtra("VideoArray", videoArray);
                    startActivity(videoIntent);
                    //finish();
                }

            }
        });

        binding.galleryBtnMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (scanningComplete) {
                    Intent galleryIntent = new Intent(MainActivity.this, GalleryActivity.class);
                    galleryIntent.putExtra("GalleryFolderArray", galleryFolders);
                    startActivity(galleryIntent);
                    //finish();
                }
            }
        });

        binding.favouriteIVMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (scanningComplete) {
                    Intent galleryIntent = new Intent(MainActivity.this, FavouriteActivity.class);
                    startActivity(galleryIntent);
                    //finish();
                }
            }
        });
    }

    private void getAllDir(File dir) {
        File listFile[] = dir.listFiles();
        if (listFile != null) {
            // Log.wtf("File Length: ", "51:" + listFile.length);
            boolean check = false;
            for (int i = 0; i < listFile.length; i++) {
                if (listFile[i].isDirectory()) {
                    //Log.wtf("Files", "Dir Name 51:" + listFile[i].getName()));
                    getAllDir(listFile[i]);
                } else {
                    if (listFile[i].getName().endsWith(".jpg") || listFile[i].getName().endsWith(".png")
                            || listFile[i].getName().endsWith(".jpeg") || listFile[i].getName().endsWith(".bmp")) {
                        //Log.wtf("Files", "FileName 55:" + listFile[i].getName() + "Path: " + listFile[i].getPath());
                        MyFile obj = new MyFile(listFile[i], false, "Images");
                        imageArray.add(obj);
                        mainHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                binding.imageTVMain.setText("" + imageArray.size());
                            }
                        });
                    }
                    if (listFile[i].getName().endsWith(".txt") || listFile[i].getName().endsWith(".pdf")
                            || listFile[i].getName().endsWith(".doc") || listFile[i].getName().endsWith(".docx")
                            || listFile[i].getName().endsWith(".xml")) {
                        //Log.wtf("Files", "FileName 55:" + listFile[i].getName() + "Path: " + listFile[i].getPath());
                        MyFile obj = new MyFile(listFile[i], false, "Documents");
                        textArray.add(obj);
                        mainHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                binding.docTVMain.setText("" + textArray.size());
                            }
                        });
                    }
                    if (listFile[i].getName().endsWith(".mp3") || listFile[i].getName().endsWith(".wav")) {
                        //Log.wtf("Files", "FileName 55:" + listFile[i].getName() + "Path: " + listFile[i].getPath());
                        MyFile obj = new MyFile(listFile[i], false, "Audios");
                        audioArray.add(obj);
                        mainHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                binding.audioTVMain.setText("" + audioArray.size());
                            }
                        });
                    }
                    if (listFile[i].getName().endsWith(".mp4") || listFile[i].getName().endsWith(".mkv")
                            || listFile[i].getName().endsWith(".wmv") || listFile[i].getName().endsWith(".mov")) {
                        //Log.wtf("Files", "FileName 55:" + listFile[i].getName() + "Path: " + listFile[i].getPath());
                        MyFile obj = new MyFile(listFile[i], false, "Videos");
                        videoArray.add(obj);
                        mainHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                binding.videoTVMain.setText("" + videoArray.size());
                            }
                        });
                    }
                    //Log.wtf("Files", "FileName 62:" + listFile[i].getName());
                }
            }
        }
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
//        for (int i = 0; i < picFolders.size(); i++) {
//            Log.d("picture folders", picFolders.get(i).getFolderName() + " and path = " + picFolders.get(i).getPath() + " " + picFolders.get(i).getNumberOfPics());
//        }

        //reverse order ArrayList
        ArrayList<ImageFolder> reverseFolders = new ArrayList<>();
        for (int i = picFolders.size() - 1; i > reverseFolders.size() - 1; i--) {
            reverseFolders.add(picFolders.get(i));
        }

        return picFolders;
    }

}