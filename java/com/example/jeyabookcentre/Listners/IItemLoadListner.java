package com.example.jeyabookcentre.Listners;

import com.example.jeyabookcentre.Models.Category_section;

import java.util.List;

public interface IItemLoadListner {
    void onItemLoadSuccess(List<Category_section> category_sectionList);
    void onItemLoadFailed(String message);
}
