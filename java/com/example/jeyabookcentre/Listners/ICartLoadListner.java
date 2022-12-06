package com.example.jeyabookcentre.Listners;

import com.example.jeyabookcentre.Models.CartModel;

import java.util.List;

public interface ICartLoadListner {
    void onItemCartLoadSuccess(List<CartModel> cartModelList);
    void onItemCartLoadFailed(String message);

}
