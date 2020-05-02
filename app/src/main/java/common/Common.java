package common;

/**
 * Created by ashish.kumar on 03-07-2018.
 */

public class Common {
    public static double storeLat=25.619273;
    public static double storeLon=85.106410;
    public static String storeName="Kitchen Bazaar";
    public static String storeNumber="9835280392";
    public static String storeAdmin="8299600107";
    public static String name=storeName+"Name";
    public static String email=storeName+"email";
    public static String mobile=storeName+"phoneNumber";
    public static String address=storeName+"Address";
    public static String userId=storeName+"ownerId";
    public static String sendSMSBaseUrl="http://roundsms.com/api/sendhttp.php?authkey=NDlmMWM4OWIyYmF";
    /*--------------------------------------------------------------*/
    public static String categoryTable="Category";
    public static String categoryColumn="CategoryType";
    public static String categoryName="CategoryName";
    public static String categoryId="CategoryId";
    /*--------------------------------------------------------------*/
    public static String profileTable="Users";
    /*--------------------------------------------------------------*/
    public static String productsTable="Products";
    /*--------------------------------------------------------------*/
    public static String orderTable="Orders";
    public static String userIdKey="userId";
    public static String ownerIdKey="ownerId";

    public static String orderstatus="orderstatus";
    /*--------------------------------------------------------------*/
    public static String orderDetails="OrderItems";
    public static String orderId="orderId";
    public static String objectId="objectId";

    /*--------------------------------------------------------------*/
    public static int category=1;
    public static int trendingCategory=2;
    public static int pendingOrderStatus=1;
    public static int acceptedOrderStatus=2;
    public static int outForDelivery=3;
    public static int deliveredOrderStatus=5;
    public static int cancelledOrderStatus=4;
    public static String getSendSMSUrl(String mobileNumber, String message) {
        return sendSMSBaseUrl +"&mobiles="+ mobileNumber + "&message=" + message +"&sender=ROUSMS&type=1&route=2";
    }
}
