package interfaces;

import common.Common;
import model.CurrentOrderModel;

/**
 * Created by ashish.kumar on 18-07-2018.
 */

public interface OrderDetailsCallBack {
public void    onItemClick(CurrentOrderModel orderId);
    public void   cancelOrderCallBack(CurrentOrderModel model,int status);
}
