package com.example.cibushub.BE;

public class PictureFile {
    private String base64Image;
    private long size;
    private String name;


    public PictureFile() {

    }

    public PictureFile(String base64Image, long size, String name) {
        this.base64Image = base64Image;
        this.size = size;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBase64Image() {
        return base64Image;
    }

    public void setBase64Image(String base64Image) {
        this.base64Image = base64Image;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }
}
