package com.grace.weeclik.utils;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

/**
 * com.grace.weeclik.utils
 * Created by grace on 31/05/2018.
 */
public abstract class PaginationScrollListener extends RecyclerView.OnScrollListener {

    LinearLayoutManager layoutManager;

    public PaginationScrollListener(LinearLayoutManager layoutManager) {
        this.layoutManager = layoutManager;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        int visible_item_count = layoutManager.getChildCount();
        int total_item_count = layoutManager.getItemCount();
        int first_visible_item_position = layoutManager.findFirstVisibleItemPosition();

        if (!isLoading() && !isLastPage()) {
            if ((visible_item_count + first_visible_item_position) >= total_item_count
                    && first_visible_item_position >= 0
                    && total_item_count >= getTotalPageCount()) {
                loadMoreItems();
            }
        }
    }

    protected abstract void loadMoreItems();

    protected abstract int getTotalPageCount(); /*public*/

    protected abstract boolean isLastPage(); /*public*/

    protected abstract boolean isLoading(); /*public*/
}
