package com.mphra.projekmaple;

public class ItemList {

    private String tittle;
    private String description;
    private int Thumbnail;


    public ItemList(String tittle, String description, int thumbnail){
        this.tittle = tittle;
        this.description = description;
        this.Thumbnail = thumbnail;
    }

    public String getTittle() {
        return tittle;
    }

    public void setTittle(String tittle) {
        this.tittle = tittle;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getThumbnail() {
        return Thumbnail;
    }

    public void setThumbnail(int thumbnail) {
        Thumbnail = thumbnail;
    }
}

/**code explanation
 Line 10-14 : The constructor for this class

 Line 16-38 : The getter and setter for this class
 **/
