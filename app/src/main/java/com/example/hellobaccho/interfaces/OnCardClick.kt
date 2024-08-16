package com.example.hellobaccho.interfaces

interface OnCardClick<T> {
    fun onClick(data: T, clickType: String)

    fun onCardClick(data: T, clickType: String, index: Int) {}
}