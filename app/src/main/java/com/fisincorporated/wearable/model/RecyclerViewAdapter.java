package com.fisincorporated.wearable.model;

import android.databinding.ObservableArrayList;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public abstract class RecyclerViewAdapter<ITEM_T, VIEW_MODEL_T extends ItemViewModel<ITEM_T>>
        extends RecyclerView.Adapter<RecyclerViewAdapter.ItemViewHolder<ITEM_T, VIEW_MODEL_T>> {

    protected final ObservableArrayList<ITEM_T> items;

    public RecyclerViewAdapter() {

        items = new ObservableArrayList<>();
    }

    @Override
    public final void onBindViewHolder(ItemViewHolder<ITEM_T, VIEW_MODEL_T> holder, int position) {
        holder.setItem(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class ItemViewHolder<T, VT extends ItemViewModel<T>>
            extends RecyclerView.ViewHolder {

        protected final VT viewModel;
        private final ViewDataBinding binding;

        public ItemViewHolder(View itemView, ViewDataBinding binding, VT viewModel) {
            super(itemView);
            this.binding = binding;
            this.viewModel = viewModel;
        }

        void setItem(T item) {
            viewModel.setItem(item);
            binding.executePendingBindings();
        }
    }
}