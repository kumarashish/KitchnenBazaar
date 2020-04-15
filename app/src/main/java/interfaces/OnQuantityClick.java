package interfaces;

import model.ProductModel;

/**
 * Created by ashish.kumar on 10-07-2018.
 */

public interface OnQuantityClick {
    public void onIncreaseQuantity(ProductModel model, int position);
    public void onDecreaseQuantity(ProductModel model,int position);
}
