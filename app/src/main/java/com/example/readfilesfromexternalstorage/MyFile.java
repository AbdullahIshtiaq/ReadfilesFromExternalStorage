package com.example.readfilesfromexternalstorage;
import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;

public class MyFile implements Serializable {

    private File file;
    private boolean isSelected;
    private String dataType;

    public MyFile(File file, boolean isSelected, String dataType) {
        this.file = file;
        this.isSelected = isSelected;
        this.dataType = dataType;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }
}

