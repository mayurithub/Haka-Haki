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
import com.mayurit.hakahaki.Model.ProjectModel;
import com.mayurit.hakahaki.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Krevilraj on 2/25/2018.
 */

public class ProjectListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<ProjectModel> mList;
    Context context;

    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 0;

    private boolean loading;
    private OnLoadMoreListener onLoadMoreListener;
    private Context ctx;
    int holderPosition = 0;
    private SparseBooleanArray itemStateArray = new SparseBooleanArray();
    public ProjectListAdapter(List<ProjectModel> mList) {
        this.mList = mList;
    }

    public ProjectListAdapter(Context context, List<ProjectModel> mList, RecyclerView view) {
        this.mList = mList;
        ctx = context;
        lastItemViewDetector(view);
    }

    private OnItemClickListener mOnItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(View view, ProjectModel obj, int position);
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mOnItemClickListener = mItemClickListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder vh;
        if (viewType == VIEW_ITEM) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_category_news, parent, false);
            vh = new OriginalViewHolder(v);
        } else {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_loading, parent, false);
            vh = new ProgressViewHolder(v);
        }
        return vh;

    }

  /*  @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_category_news, parent, false);
        context = parent.getContext();
        return new MyViewHolder(itemView);
    }*/


    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder1, final int position) {
        if (holder1 instanceof OriginalViewHolder) {
            final ProjectModel mData = mList.get(position);
            final OriginalViewHolder holder = (OriginalViewHolder) holder1;

            holder.mTitle.setText(mData.getPostTitle());
            holder.mDescription.setText(mData.getPost_excerpt());
            holder.mDate.setText(mData.getPostDate());
            if(!mData.getImageId().equals("")){
                holder.mAltImage.setVisibility(View.INVISIBLE);
                Glide.with(ctx).load(mData.getImageId()).into(holder.mThumbImage);
            }


        } else {
            ((ProgressViewHolder) holder1).progressBar.setIndeterminate(true);
        }
        holderPosition++;
    }
    /*@Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final ProjectModel mData = mList.get(position);
        holder.mTitle.setText(mData.getPostTitle());
        holder.mDescription.setText(mData.getPostExcerpt());
        holder.mDate.setText(mData.getPostDate());
        if(!mData.getImageId().equals("")){
            holder.mAltImage.setVisibility(View.INVISIBLE);
            Glide.with(context).load(mData.getImageId()).into(holder.mThumbImage);
        }

    }*/

    @Override
    public int getItemCount() {
        return mList.size();
    }

       @Override
    public int getItemViewType(int position) {
        return this.mList.get(position) != null ? VIEW_ITEM : VIEW_PROG;
    }

    public void insertData(List<ProjectModel> items) {
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


    public void setListData(List<ProjectModel> items){
        this.mList = items;
        notifyDataSetChanged();
    }

    public void resetListData() {
        this.mList = new ArrayList<>();
        notifyDataSetChanged();
    }

    public class OriginalViewHolder extends RecyclerView.ViewHolder {

        public TextView mTitle,mDescription,mDate;
        public ImageView mAltImage, mThumbImage;


        public OriginalViewHolder(View view) {
            super(view);

            mTitle = (TextView) view.findViewById(R.id.txt_title);
            mDescription = (TextView) view.findViewById(R.id.txt_description);
            mDate = (TextView) view.findViewById(R.id.txt_date);
            mAltImage = (ImageView) view.findViewById(R.id.img_alt_thumb);
            mThumbImage = (ImageView) view.findViewById(R.id.img_thumb);
        }

    }

    public void setFilter(List<ProjectModel> fModels) {
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
