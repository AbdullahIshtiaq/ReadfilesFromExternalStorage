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
import java.util.ArrayList;
import java.util.List;

public class GeneralRecyclerViewAdapter extends RecyclerView.Adapter<GeneralRecyclerViewAdapter.ViewHolder> {

    private List<MyFile> mFiles;
    private Context context;
    private boolean isLongPressed = false;
    private int totalSelectedItems = 0;
    private ArrayList<File> selectedItemsArray;
    public OnAdapterItemClickListener listener;


    public GeneralRecyclerViewAdapter(List<MyFile> mFiles, Context context, OnAdapterItemClickListener listener,
                                      ArrayList<File> selectedItemsArray) {
        this.mFiles = mFiles;
        this.context = context;
        this.listener = listener;
        this.selectedItemsArray = selectedItemsArray;
    }

    interface OnAdapterItemClickListener {
        void onItemClicked(String text);

        void setFavouriteVisibility(int visibility);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.general_recycler_item, parent,
                false);
        return new GeneralRecyclerViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        int currentIndex = position;
        holder.setFileName(mFiles.get(position).getFile().getName().toString());
        String path = mFiles.get(position).getFile().getPath().toString();
        String name = mFiles.get(position).getFile().getName().toString();

        Uri myURI = FileProvider.getUriForFile(context, context.getApplicationContext()
                .getPackageName() + ".provider", mFiles.get(position).getFile());

        if (mFiles.get(currentIndex).getDataType().contentEquals("Images")) {
            holder.imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            Glide.with(context)
                    .load(path)
                    .into(holder.imageView);
        } else if (mFiles.get(currentIndex).getDataType().contentEquals("Documents")) {
            holder.imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
            if (mFiles.get(position).getFile().getName().endsWith(".pdf")) {
                Glide.with(context)
                        .load(R.drawable.ic_pdf)
                        .into(holder.imageView);
            } else {
                Glide.with(context)
                        .load(R.drawable.ic_document)
                        .into(holder.imageView);
            }

        } else if (mFiles.get(currentIndex).getDataType().contentEquals("Audios")) {
            holder.imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
            Glide.with(context)
                    .load(R.drawable.ic_audio_file)
                    .into(holder.imageView);

        } else if (mFiles.get(currentIndex).getDataType().contentEquals("Videos")) {
            holder.imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            holder.relativeLayout1.setVisibility(View.VISIBLE);
            Glide.with(context)
                    .load(path)
                    .into(holder.imageView);
        } else if (mFiles.get(currentIndex).getDataType().contentEquals("Favourites")) {

        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isLongPressed == false && totalSelectedItems == 0) {
                    Log.wtf("45", "onClick 45:");
                    Log.wtf("Path 46: ", name + " Path: " + path);

                    holder.relativeLayout1.setVisibility(View.GONE);

                    Intent myIntent = new Intent(Intent.ACTION_VIEW);
                    myIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

                    if (mFiles.get(currentIndex).getDataType().contentEquals("Images")) {

                        myIntent.setDataAndType(myURI, "image/*");

                    } else if (mFiles.get(currentIndex).getDataType().contentEquals("Documents")) {

                        String[] mimeTypes = {"application/pdf"};
                        myIntent.setDataAndType(myURI, "*/*");
                        myIntent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);

                    } else if (mFiles.get(currentIndex).getDataType().contentEquals("Audios")) {

                        myIntent.setDataAndType(myURI, "audio/*");

                    } else if (mFiles.get(currentIndex).getDataType().contentEquals("Videos")) {
                        myIntent.setDataAndType(myURI, "video/*");
                    }

                    try {
                        context.startActivity(myIntent);
                        if (mFiles.get(currentIndex).getDataType().contentEquals("Videos")) {
                            holder.relativeLayout1.setVisibility(View.VISIBLE);
                        }
                    } catch (ActivityNotFoundException e) {
                        Log.wtf("68: ", e.getMessage());
                    }
                } else {
                    Log.wtf("73: ", "In Single Click");
                    if (!mFiles.get(currentIndex).isSelected()) {
                        Log.wtf("75: ", "Select");
                        holder.relativeLayout.setVisibility(View.VISIBLE);
                        mFiles.get(currentIndex).setSelected(true);
                        selectedItemsArray.add(mFiles.get(currentIndex).getFile());
                        listener.onItemClicked("Selected: " + selectedItemsArray.size());
                        totalSelectedItems += 1;
                    } else if (mFiles.get(currentIndex).isSelected()) {
                        Log.wtf("79: ", "UnSelect");
                        holder.relativeLayout.setVisibility(View.GONE);
                        mFiles.get(currentIndex).setSelected(false);
                        removeItemFromList(mFiles.get(currentIndex).getFile());
                        listener.onItemClicked("Selected: " + selectedItemsArray.size());
                        totalSelectedItems -= 1;
                        if (totalSelectedItems == 0) {
                            listener.onItemClicked(mFiles.get(currentIndex).getDataType());
                            isLongPressed = false;
                            listener.setFavouriteVisibility(View.GONE);
                        }
                    }
                    Log.wtf("89: ", "Total Selected Items " + totalSelectedItems);
                }
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (isLongPressed == false) {
                    Log.wtf("92: ", "In Long Click");
                    //holder.relativeLayout.setVisibility(View.VISIBLE);
                    //mFiles.get(currentIndex).setSelected(true);
                    listener.setFavouriteVisibility(View.VISIBLE);
                    isLongPressed = true;
                } else if (isLongPressed == true && totalSelectedItems == 0) {
                    isLongPressed = false;
                    listener.setFavouriteVisibility(View.GONE);
                    Log.wtf("97: ", "In Else");
                }
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return mFiles.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    private void removeItemFromList(File file) {
        for (int i = 0; i < selectedItemsArray.size(); i++) {
            if (selectedItemsArray.get(i).getPath().contentEquals(file.getPath())) {
                selectedItemsArray.remove(i);
                return;
            }
        }
    }

    public void update() {
        isLongPressed = false;
        totalSelectedItems = 0;
    }


    class ViewHolder extends RecyclerView.ViewHolder {

        public TextView fileName;
        public ImageView imageView;
        public RelativeLayout relativeLayout, relativeLayout1;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            fileName = itemView.findViewById(R.id.filenameTV_ImageItem);
            imageView = itemView.findViewById(R.id.imageView_Image);
            relativeLayout = itemView.findViewById(R.id.relativeLayout_ImageItem);
            relativeLayout1 = itemView.findViewById(R.id.relativeLayout1_ImageItem);
        }

        public void setFileName(String fileName) {
            Log.wtf("FileName 91: ", fileName);
            this.fileName.setText("" + fileName);
        }

    }
}
