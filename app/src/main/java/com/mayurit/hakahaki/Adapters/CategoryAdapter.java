package com.mayurit.hakahaki.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.mayurit.hakahaki.Model.CategoryModel;
import com.mayurit.hakahaki.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Krevilraj on 2/25/2018.
 */

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.MyViewHolder> {
    private List<CategoryModel> mList;
    Context context;

    public CategoryAdapter(List<CategoryModel> mList) {
        this.mList = mList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_category_list, parent, false);
        context = parent.getContext();
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final CategoryModel mData = mList.get(position);
        holder.mTitle.setText(mData.getName());

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView mTitle;

        public MyViewHolder(View view) {
            super(view);
            mTitle = (TextView) view.findViewById(R.id.txt_category_name);
        }
    }

    public void setListData(List<CategoryModel> items){
        this.mList = items;
        notifyDataSetChanged();
    }

    public void resetListData() {
        this.mList = new ArrayList<>();
        notifyDataSetChanged();
    }

}
