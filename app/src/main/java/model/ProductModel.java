package model;

import java.util.Map;

/**
 * Created by ashish.kumar on 09-07-2018.
 */

public class ProductModel {
    String ProductName;
    String CategoryName;
    String  Category_Id;
    String Product_Image;
    String OfferPrice;
    String MRP;
    String productId;
    int quantity=0;
    public ProductModel(Map map){
       // CategoryName = (String) map.get("CategoryName");
        CategoryName = "";
        Category_Id= (String)map.get("Category_Id");
        ProductName= (String) map.get("ProductName");
        Product_Image= "http://www.bsmc.net.au/wp-content/uploads/No-image-available.jpg";
        OfferPrice= (String )map.get("SellingPrice");
        MRP= (String) map.get("MRP");
        productId= (String)map.get("ProductId");

    }

    public String getCategoryName() {
        return CategoryName;
    }

    public String getProductName() {
        return ProductName;
    }
public void setQuantity(int quantity)
{
    this.quantity=quantity;
}
    public String getProductId() {
        return productId;
    }

    public String getCategory_Id() {
        return Category_Id;
    }

    public String getMRP() {
        return MRP;
    }

    public String getOfferPrice() {
        return OfferPrice;
    }

    public String getProduct_Image() {
        return Product_Image;
    }

    public void increaseQuantity()
    {
        quantity=quantity+1;
    }
    public void decreaseQuantity()
    {if (quantity!=0) {
        quantity =quantity-1;
    }
    }

    public int getQuantity() {
        return quantity;
    }
}
