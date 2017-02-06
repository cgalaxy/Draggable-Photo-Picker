package com.nestia.android.testitemtouchhelp;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.widget.Toast;
import java.util.ArrayList;

import me.iwf.photopicker.PhotoPicker;

public class MainActivity extends Activity implements SimpleItemTouchHelperCallback.OnStartDragListener {
    private ArrayList<String> imgList;
    private ImageBean imageBean;
    private ImageAdapter mAdapter;
    private ItemTouchHelper mItemTouchHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 4);
        RecyclerView rv = (RecyclerView) findViewById(R.id.rv);
        imgList = new ArrayList<>();
        imageBean = new ImageBean(imgList, 9);
        mAdapter = new ImageAdapter(this, imageBean, this);
        rv.setHasFixedSize(true);
        rv.setAdapter(mAdapter);
        rv.setLayoutManager(gridLayoutManager);
        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(mAdapter);
        mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(rv);

    }


    @Override
    public boolean shouldShowRequestPermissionRationale(@NonNull String permission) {
        switch (permission) {
            case Manifest.permission.READ_EXTERNAL_STORAGE:
            case Manifest.permission.CAMERA:
                // No need to explain to user as it is obvious
                return false;
            default:
                return true;
        }
    }

    /**
     * apply for take photo result
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            openPhotoPickerPage();
        } else {
            //如果是编辑,没有点开选择图片,点击选择图片,弹出permission
            Toast.makeText(this, "No read storage permission! Cannot perform the action.", Toast.LENGTH_SHORT).show();
        }
    }

    private void openPhotoPickerPage() {
        PhotoPicker.builder()
                .setPhotoCount(imageBean.getMaxImgNum())
                .setGridColumnCount(4)
                .setSelected((ArrayList<String>) mAdapter.getImages())
                .start(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == PhotoPicker.REQUEST_CODE) {
                if (null != data) {
                    ArrayList<String> photos =
                            data.getStringArrayListExtra(PhotoPicker.KEY_SELECTED_PHOTOS);
                    mAdapter.setImages(photos);
                }
            }
        }
    }

    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        mItemTouchHelper.startDrag(viewHolder);
    }
}
