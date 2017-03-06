package com.wp.mvp.ui.adapter;

import android.content.Context;
import android.support.annotation.IdRes;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import static android.R.attr.id;

/**
 * Created by Administrator on 2017/3/5 0005.
 * @// TODO: 2017/3/5 0005  
 */

public  abstract class BaseRecycleViewAdapter<T> extends
        RecyclerView.Adapter<BaseRecycleViewAdapter.BaseViewHolder>{

    private ROnItemClickListener onItemClickListener;
    private ROnLongClickListener onLongClickListener;

    private Context context;
    public List<T> listData;


    public void setOnItemClickListener (ROnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void setOnLongClickListener (ROnLongClickListener onLongClickListener) {
        this.onLongClickListener = onLongClickListener;
    }

    public void setDataSources(List<T> listData){
        this.listData = listData;
    }


    /**
     * 点击事件接口
     */
    public interface ROnItemClickListener {
        void onItemClick(int position, BaseViewHolder viewHolder);
    }

    /**
     * 长按事件接口
     */
    public interface ROnLongClickListener {
        boolean onLongClick(int position, BaseViewHolder viewHolder);
    }


    @Override
    public BaseRecycleViewAdapter.BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(final BaseRecycleViewAdapter.BaseViewHolder holder, final int position) {
        holder.getParentView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onItemClickListener !=null ){
                    onItemClickListener.onItemClick(position, holder);
                }
            }
        });
        holder.getParentView().setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                return onLongClickListener !=null &&
                        onLongClickListener.onLongClick(position, holder);
            }
        });
    }

    /**
     * 设置context
     * @param context
     */
    public void bindContext(Context context){
        this.context = context;
    }

    /**
     * 获取context
     * @return
     */
    public Context getContext(){
        return this.context;
    }

    /**
     * 设置recycleView的count
     * @return
     */
    @Override
    public int getItemCount() {
        return listData == null ? 0 : listData.size();
    }

    public static class BaseViewHolder extends RecyclerView.ViewHolder{

        private View parentView;

        public BaseViewHolder(View itemView) {
            super(itemView);
            this.parentView = itemView;
        }

        public View getParentView(){
            return parentView;
        }

        @SuppressWarnings("unchecked")
        public <T extends View>T $(@IdRes int id) {
            return (T) parentView.findViewById(id);
        }

    }
}
