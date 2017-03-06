package com.wp.mvp.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.wp.mvp.bean.GirlsList;
import com.wp.tnewsapp.R;

/**
 * Created by Administrator on 2017/3/5 0005.
 */

public class GirlsAdapter extends BaseRecycleViewAdapter<GirlsList.ResultsBean>{

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        bindContext(parent.getContext());

        return new ItemViewHolder(LayoutInflater.from(getContext())
                .inflate(R.layout.item_home, parent, false));
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {

        if (holder instanceof ItemViewHolder) {
            ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
            GirlsList.ResultsBean resultsBean = listData.get(position);

            // glide加载img
            Glide.with(getContext())
                    .load(resultsBean.getUrl())
                    .centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(itemViewHolder.mImage);

        }


        super.onBindViewHolder(holder, position);
    }

    private class ItemViewHolder extends BaseViewHolder {

        ImageView mImage;

        public ItemViewHolder(View itemView) {
            super(itemView);

            mImage = $(R.id.item_home_img);
        }
    }

}
