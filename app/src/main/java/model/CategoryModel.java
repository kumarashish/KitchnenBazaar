package model;

import java.util.Map;

import common.Common;

/**
 * Created by ashish.kumar on 06-07-2018.
 */

public class CategoryModel {
    String categoryId;
    String categoryName;
    int categoryType;
    String categoryImage;
public CategoryModel(Map map){
    categoryId= (String)map.get("Id");
    categoryName= (String) map.get("CategoryName");
    categoryImage= (String) map.get("CategoryImage");
    categoryType= (int)map.get("CategoryType");


}
    public void setCategoryId(int categoryId) {
        categoryId = categoryId;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public void setCategoryType(int categoryType) {
        this.categoryType = categoryType;
    }

    public void setCategoryImage(String categoryImage) {
        this.categoryImage = categoryImage;
    }

    public String getCategoryImage() {
        return categoryImage;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public int getCategoryType() {
        return categoryType;
    }

    public String getCategoryName() {
        return categoryName;
    }
}
