package com.kotdev.food.interfaces;

public interface ClickCallback<T> {
    void clickListener(int position, T model);
}