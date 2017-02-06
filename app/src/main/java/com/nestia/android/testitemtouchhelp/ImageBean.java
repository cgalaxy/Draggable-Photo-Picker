package com.nestia.android.testitemtouchhelp;


import java.util.List;

/**
 * Created by chenxinying on 17/1/7
 *
 * 默认最多选择图片9张
 */

public class ImageBean {

    public List<String> images;
    public boolean canAddImg;
    public int maxImgNum;

    public ImageBean(List<String> images, int maxImgNum) {
        this.images = images;
        this.maxImgNum = maxImgNum;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public boolean isCanAddImg() {
        return getMaxImgNum() == 0 ? images.size() < 9 : images.size() < getMaxImgNum();
    }

    public int getMaxImgNum() {
        return maxImgNum == 0 ? 9 : maxImgNum;
    }

    public void setMaxImgNum(int maxImgNum) {
        this.maxImgNum = maxImgNum;
    }
}
