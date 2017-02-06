package com.nestia.android.testitemtouchhelp;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

/**
 * Created by chenxinying on 17/1/9
 */

public class SimpleItemTouchHelperCallback extends ItemTouchHelper.Callback {

    private ImageAdapter mAdapter;

    public SimpleItemTouchHelperCallback(ImageAdapter adapter) {
        mAdapter = adapter;
    }

    @Override
    public boolean isLongPressDragEnabled() {
        return false;
    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
        return makeMovementFlags(dragFlags, 0);
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder source, RecyclerView.ViewHolder target) {
        mAdapter.onItemMove(recyclerView,source, target);
        return true;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
    }

    interface ItemTouchHelpAdapter {
        /**
         * Called when an item has been dragged far enough to trigger a move. This is called every time
         * an item is shifted, and not at the end of a "drop" event.
         *
         * @param recyclerView
         * @param source The start position of the moved item.
         * @param target   Then end position of the moved item.
         * @see RecyclerView#getAdapterPositionFor(RecyclerView.ViewHolder)
         * @see RecyclerView.ViewHolder#getAdapterPosition()
         */
        void onItemMove(RecyclerView recyclerView, RecyclerView.ViewHolder source, RecyclerView.ViewHolder target);
    }

    interface OnStartDragListener {
        /**
         * Called when a view is requesting a start of a drag.
         *
         * @param viewHolder The holder of the view to drag.
         */
        void onStartDrag(RecyclerView.ViewHolder viewHolder);
    }
}
