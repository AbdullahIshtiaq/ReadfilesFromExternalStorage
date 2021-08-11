package com.example.readfilesfromexternalstorage;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "favourite_files_table")
public class FavouriteFiles implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private int id;

    //@ColumnInfo(name = "filename")
    private String Filename;

    //@ColumnInfo(name = "filepath")
    private String FilePath;

    private String dataType;

    public FavouriteFiles(){

    }

    public FavouriteFiles(String filename, String filePath, String dataType) {
        Filename = filename;
        FilePath = filePath;
        this.dataType = dataType;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFilename() {
        return Filename;
    }

    public void setFilename(String filename) {
        Filename = filename;
    }

    public String getFilePath() {
        return FilePath;
    }

    public void setFilePath(String filePath) {
        FilePath = filePath;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }
}
