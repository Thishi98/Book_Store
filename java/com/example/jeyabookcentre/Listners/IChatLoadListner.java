package com.example.jeyabookcentre.Listners;

import com.example.jeyabookcentre.Models.MessageModel;

import java.util.List;

public interface IChatLoadListner {
    void onItemChatLoadSuccess(List<MessageModel> cartModelList);
    void onItemChatLoadFailed(String message);

}
