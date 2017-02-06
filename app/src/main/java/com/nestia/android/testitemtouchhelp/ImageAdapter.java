package com.nestia.android.testitemtouchhelp;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import me.iwf.photopicker.PhotoPicker;

/**
 * Created by chenxinying on 17/1/7
 */

public class ImageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements SimpleItemTouchHelperCallback.ItemTouchHelpAdapter {
    private ImageBean imgs;
    public static final int VH_COMMEN_VIEW = 0;
    public static final int VH_LAST_VIEW = 1;
    private Activity mActivity;
    private SimpleItemTouchHelperCallback.OnStartDragListener mDragStartListener;

    public ImageAdapter(Activity mActivity, ImageBean imgs, SimpleItemTouchHelperCallback.OnStartDragListener dragStartListener) {
        this.imgs = imgs;
        this.mActivity = mActivity;
        this.mDragStartListener = dragStartListener;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VH_COMMEN_VIEW) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_first, parent, false);
            return new FirstViewHold(view);
        } else if (viewType == VH_LAST_VIEW) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_add, parent, false);
            return new LastViewHold(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof FirstViewHold) {
            ((FirstViewHold) holder).setData(imgs.getImages().get(position), position);
        }
    }

    @Override
    public int getItemCount() {
        return imgs.getImages() == null ? 1 : (imgs.getImages().size() + (imgs.isCanAddImg() ? 1 : 0));
    }

    @Override
    public int getItemViewType(int position) {
        if (imgs.getImages() == null || imgs.getImages().size() == 0) {
            return VH_LAST_VIEW;
        } else {
            if ((position == getItemCount() - 1) && imgs.isCanAddImg()) {
                return VH_LAST_VIEW;
            } else {
                return VH_COMMEN_VIEW;
            }
        }
    }

    @Override
    public void onItemMove(RecyclerView recyclerView, RecyclerView.ViewHolder source, RecyclerView.ViewHolder target) {
        int fromPosition = source.getAdapterPosition();
        int toPosition = target.getAdapterPosition();
        if (getItemViewType(fromPosition) == VH_LAST_VIEW || (getItemViewType(toPosition) == VH_LAST_VIEW)) {
            return;
        }
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(getImages(), i, i + 1);
            }
            notifyItemMoved(fromPosition, toPosition);
            if (fromPosition == 0) {
                ((FirstViewHold) source).head.setVisibility(View.INVISIBLE);
                // 把变成一个的head可见
                ((FirstViewHold) recyclerView.findViewHolderForAdapterPosition(0)).head.setVisibility(View.VISIBLE);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(getImages(), i, i - 1);
            }
            notifyItemMoved(fromPosition, toPosition);
            if (toPosition == 0) {
                ((FirstViewHold) source).head.setVisibility(View.VISIBLE);
                //把变成不是第一个的head不可见
                ((FirstViewHold) recyclerView.findViewHolderForAdapterPosition(1)).head.setVisibility(View.INVISIBLE);
            }
        }
    }

    class FirstViewHold extends RecyclerView.ViewHolder {
        public ImageView img;
        public ImageView delete;
        public TextView head;

        public FirstViewHold(View itemView) {
            super(itemView);
            img = (ImageView) itemView.findViewById(R.id.img_first);
            delete = (ImageView) itemView.findViewById(R.id.ic_delete);
            head = (TextView) itemView.findViewById(R.id.head);
            img.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (MotionEventCompat.getActionMasked(event) == MotionEvent.ACTION_DOWN) {
                        if (mDragStartListener != null) {
                            mDragStartListener.onStartDrag(FirstViewHold.this);
                        }
                    }
                    return false;
                }
            });

        }

        void setData(String imgUrl, final int position) {
            head.setVisibility(position == 0 ? View.VISIBLE : View.GONE);
            Glide.with(mActivity).load(imgUrl).into(img);
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getImages().remove(position);
                    notifyDataSetChanged();
                }
            });
        }
    }

    class LastViewHold extends RecyclerView.ViewHolder {
        ImageView img;

        public LastViewHold(View itemView) {
            super(itemView);
            img = (ImageView) itemView.findViewById(R.id.img_add);
            img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    checkReadSDPermission();
                }
            });
        }
    }

    private void checkReadSDPermission() {
        int readStoragePermissionState = ContextCompat.checkSelfPermission(mActivity, Manifest.permission.READ_EXTERNAL_STORAGE);
        boolean readStoragePermissionGranted = readStoragePermissionState != PackageManager.PERMISSION_GRANTED;
        if (readStoragePermissionGranted) {
            ActivityCompat.requestPermissions(mActivity,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    0);
        } else {
            PhotoPicker.builder()
                    .setPhotoCount(imgs.getMaxImgNum())
                    .setGridColumnCount(4)
                    .setSelected((ArrayList<String>) imgs.getImages())
                    .start(mActivity);
        }
    }

    public List<String> getImages() {
        return imgs.getImages();
    }

    public void setImages(ArrayList<String> images) {
        imgs.setImages(images);
        notifyDataSetChanged();
    }
}
