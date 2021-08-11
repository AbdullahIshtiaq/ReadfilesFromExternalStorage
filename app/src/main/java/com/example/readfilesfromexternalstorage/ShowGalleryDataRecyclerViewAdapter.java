package com.example.readfilesfromexternalstorage;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.io.File;
import java.util.List;

public class ShowGalleryDataRecyclerViewAdapter extends RecyclerView.Adapter<ShowGalleryDataRecyclerViewAdapter.ViewHolder> {

    private List<File> mFiles;
    private Context context;

    public ShowGalleryDataRecyclerViewAdapter(List<File> mFiles, Context context) {
        this.mFiles = mFiles;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.show_gallery_data_recycler_item, parent,
                false);
        return new ShowGalleryDataRecyclerViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setFileName(mFiles.get(position).getName().toString());
        String path = mFiles.get(position).getPath().toString();
        String name = mFiles.get(position).getName().toString();
        File file = mFiles.get(position);

        if (name.endsWith(".mp4")) {
            holder.relativeLayout.setVisibility(View.VISIBLE);
        }else {
            holder.relativeLayout.setVisibility(View.GONE);
        }
        Glide.with(context)
                .load(path)
                .into(holder.imageView);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.wtf("onClick 45: ", "Path: " + path);

                Uri URI = FileProvider.getUriForFile(context, context.getApplicationContext()
                        .getPackageName() + ".provider", file);

                Intent openImgIntent = new Intent(Intent.ACTION_VIEW);
                openImgIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                if (name.endsWith(".mp4")) {
                    openImgIntent.setDataAndType(URI, "video/*");
                }else{
                    openImgIntent.setDataAndType(URI, "image/*");
                }
                try {
                    context.startActivity(openImgIntent);
                } catch (ActivityNotFoundException e) {
                    Log.wtf("68: ", e.getMessage());
                }
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
        public RelativeLayout relativeLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            fileName = itemView.findViewById(R.id.filenameTV_ShowFolderDataItem);
            imageView = itemView.findViewById(R.id.imageView_ShowFolderDataItem);
            relativeLayout = itemView.findViewById(R.id.relativeLayout_ShowDataItem);
        }

        public void setFileName(String fileName) {
            Log.wtf("FileName 91: ", fileName);
            this.fileName.setText("" + fileName);
        }

    }
}
