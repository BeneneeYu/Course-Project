package com.photoSharing.entity;

/**
 * @program: Project
 * @description: 图片收藏
 * @author: Shen Zhengyu
 * @create: 2020-07-13 15:15
 **/
public class travelimagefavor {
    private int FavorID;
    private int UID;
    private int ImageID;

    public int getFavorID() {
        return FavorID;
    }

    public void setFavorID(int favorID) {
        FavorID = favorID;
    }

    public int getUID() {
        return UID;
    }

    public void setUID(int UID) {
        this.UID = UID;
    }

    public int getImageID() {
        return ImageID;
    }

    public void setImageID(int imageID) {
        ImageID = imageID;
    }

    @Override
    public String toString() {
        return "travelimagefavor{" +
                "FavorID=" + FavorID +
                ", UID=" + UID +
                ", ImageID=" + ImageID +
                '}';
    }
}
