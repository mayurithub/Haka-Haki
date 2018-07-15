package com.mayurit.hakahaki.Adapters;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mayurit.hakahaki.Helpers.Constant;
import com.mayurit.hakahaki.Model.VideoModel;
import com.mayurit.hakahaki.R;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by Krevilraj on 2/25/2018.
 */

public class VideoListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<VideoModel> mList;
    Context context;

    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 0;

    private boolean loading;
    private OnLoadMoreListener onLoadMoreListener;
    private Context ctx;
    int holderPosition = 0;
    private SparseBooleanArray itemStateArray = new SparseBooleanArray();
    public VideoListAdapter(List<VideoModel> mList) {
        this.mList = mList;
    }

    public VideoListAdapter(Context context, List<VideoModel> mList, RecyclerView view) {
        this.mList = mList;
        ctx = context;
        lastItemViewDetector(view);
    }

    private OnItemClickListener mOnItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(View view, VideoModel obj, int position);
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mOnItemClickListener = mItemClickListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder vh;
        if (viewType == VIEW_ITEM) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.video_listing, parent, false);
            vh = new OriginalViewHolder(v);
        } else {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_loading, parent, false);
            vh = new ProgressViewHolder(v);
        }
        return vh;

    }


    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder1, final int position) {
        if (holder1 instanceof OriginalViewHolder) {
            final VideoModel mData = mList.get(position);
            final OriginalViewHolder holder = (OriginalViewHolder) holder1;

            holder.video_title.setText(mData.getPostTitle());
            holder.video_description.setText(mData.getPostExcerpt());
            holder.video_date.setText(mData.getPostDate());
            if(mData.getImageId().equals("false")){
                Glide.with(ctx).load(mData.getImageId()).into(holder.video_img);
            }


        } else {
            ((ProgressViewHolder) holder1).progressBar.setIndeterminate(true);
        }
        holderPosition++;
    }
    @Override
    public int getItemCount() {
        return mList.size();
    }

       @Override
    public int getItemViewType(int position) {
        return this.mList.get(position) != null ? VIEW_ITEM : VIEW_PROG;
    }

    public void insertData(List<VideoModel> items) {
        setLoaded();
        int positionStart = getItemCount();
        int itemCount = items.size();
        this.mList.addAll(items);
        notifyItemRangeInserted(positionStart, itemCount);
    }

    public void setLoaded() {
        loading = false;
        for (int i = 0; i < getItemCount(); i++) {
            if (mList.get(i) == null) {
                mList.remove(i);
                notifyItemRemoved(i);
            }
        }
    }

    public void setLoading() {
        if (getItemCount() != 0) {
            this.mList.add(null);
            notifyItemInserted(getItemCount() - 1);
            loading = true;
        }
    }


    public void setListData(List<VideoModel> items){
        this.mList = items;
        notifyDataSetChanged();
    }

    public void resetListData() {
        this.mList = new ArrayList<>();
        notifyDataSetChanged();
    }

    public class OriginalViewHolder extends RecyclerView.ViewHolder {

        public TextView video_title,video_description,video_date;
        public ImageView video_img;


        public OriginalViewHolder(View view) {
            super(view);

            video_title = (TextView) view.findViewById(R.id.video_title);
            video_description = (TextView) view.findViewById(R.id.video_description);
            video_img = (ImageView) view.findViewById(R.id.video_img);
                     video_date=view.findViewById(R.id.video_date);

        }

    }

    public void setFilter(List<VideoModel> fModels) {
        mList = new ArrayList<>();
        mList.addAll(fModels);
        notifyDataSetChanged();
    }

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }

    public static class ProgressViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar progressBar;

        public ProgressViewHolder(View v) {
            super(v);
            progressBar = (ProgressBar) v.findViewById(R.id.progressBar1);
        }
    }

    private void lastItemViewDetector(RecyclerView recyclerView) {
        if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {
            final LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    int lastPos = layoutManager.findLastVisibleItemPosition();
                    if (!loading && lastPos == getItemCount() - 1 && onLoadMoreListener != null) {
                        if (onLoadMoreListener != null) {
                            int current_page = getItemCount() / Constant.CATEGORY_LIMIT;
                            Log.i("Bxx", "page = " + current_page);
                            onLoadMoreListener.onLoadMore(current_page);
                        }
                        loading = true;
                    }
                }
            });
        }
    }

    public interface OnLoadMoreListener {
        void onLoadMore(int current_page);
    }

}
