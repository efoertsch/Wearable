package com.fisincorporated.wearable.model;

public abstract class ItemViewModel<ITEM_T> extends ViewModel {

    public ItemViewModel() {
        super(null);
    }

    public abstract void setItem(ITEM_T item);
}