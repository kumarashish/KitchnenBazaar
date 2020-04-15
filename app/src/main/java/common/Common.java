package common;

/**
 * Created by ashish.kumar on 03-07-2018.
 */

public class Common {
    public static double storeLat=25.619273;
    public static double storeLon=85.106410;

    public static String name="GrocWorldName";
    public static String email="GrocWorldemail";
    public static String mobile="GrocWorldphoneNumber";
    public static String address="GrocWorldAddress";
    public static String userId="GrocWorldownerId";
    public static String sendSMSBaseUrl="https://www.ontimesms.in/Rest/AIwebservice/Bulk?";
    /*--------------------------------------------------------------*/
    public static String categoryTable="Category12";
    public static String categoryColumn="CategoryType";
    public static String categoryName="CategoryName";
    public static String categoryId="CategoryId";
    /*--------------------------------------------------------------*/
    public static String productsTable="Products";
    /*--------------------------------------------------------------*/
    public static String orderTable="Orders";
    public static String userIdKey="userId";
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
        return sendSMSBaseUrl + "user=Ashish&password=ashish@123&mobilenumber=" + mobileNumber + "&message=" + message +"&mtype=n&sid=GROCWO";
    }

}
