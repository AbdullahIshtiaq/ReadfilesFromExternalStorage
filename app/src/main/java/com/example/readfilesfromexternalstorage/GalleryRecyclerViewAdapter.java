package com.example.readfilesfromexternalstorage;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class GalleryRecyclerViewAdapter extends RecyclerView.Adapter<GalleryRecyclerViewAdapter.ViewHolder> {

    //private List<File> mFiles;
    private ArrayList<ImageFolder> mFiles;
    private Context context;


    public GalleryRecyclerViewAdapter(ArrayList<ImageFolder> mFiles, Context context) {
        this.mFiles = mFiles;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.gallery_recycler_item, parent,
                false);
        return new GalleryRecyclerViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setFileName(mFiles.get(position).getFolderName().toString());
        String path = mFiles.get(position).getPath().toString();
        String name = mFiles.get(position).getFolderName().toString();
        Log.wtf("47: ",  " Path: " + path);
        Log.wtf("48: ",  " Size: " + mFiles.get(position).getNumberOfPics());

        // Uri imgURI = FileProvider.getUriForFile(context, context.getApplicationContext()
        //        .getPackageName() + ".provider", mFiles.get(position));

        Glide.with(context)
                .load(mFiles.get(position).getFirstPic())
                .into(holder.imageView);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent folderDataIntent = new Intent(context.getApplicationContext(), ShowGalleryDataActivity.class);
                folderDataIntent.putExtra("Path", path);
                folderDataIntent.putExtra("Name", name);
                context.startActivity(folderDataIntent);
                //finish();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mFiles.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public TextView fileName;
        public ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            fileName = itemView.findViewById(R.id.filenameTV_FolderItem);
            imageView = itemView.findViewById(R.id.imageView_FolderItem);
        }

        public void setFileName(String fileName) {
            Log.wtf("FileName 91: ", fileName);
            this.fileName.setText("" + fileName);
        }

    }
}
