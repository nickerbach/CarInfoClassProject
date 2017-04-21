package edu.chapman.lowe121.carinfo.api_wrappers;

import android.util.Log;

import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;

public class EdmundsAPI {

    public static String FrontQuarter = "FQ", RearQuarter = "RQ", ForwardInterior = "I", RearInterior = "RI",
            SideView = "S", Cargo = "CARGO", Engine = "E", Wheel = "WHEEL", Dash = "D", CenterConsole = "CC",
            Front = "F", Rear = "R", InteriorSpotlight = "DETAIL", Emblem = "B", OtherExterior = "O",
            OtherProfile = "PROFILE";
    private static String TAG = "EdmundsAPI";

    //Multiple keys for use limit
    //private static String apiKey = "&api_key=dg5rj2zrwv3rjy6udqygv7ze";
    //private static String apiKey = "&api_key=2b5t6ffbjmymdjyjvvfa78w7";
    //private static String apiKey = "&api_key=aakb9z93ht64zqu3egr9subu";
    //private static String apiKey = "&api_key=33xyne6uyaddnpa9maxdpbtc";
    private static String apiKey = "&api_key=urkh3kww2zyn5cyxawxhac9n";

    private static String baseUrl = "https://api.edmunds.com/api/";

    public static void ListAllCars(Callback onFinish) {


        String method = "vehicle/v2/makes?";

        String queryParams = "view=basic&fmt=json";

        String fullUrl = baseUrl + method + queryParams + apiKey;

        OkHttpClient client = new OkHttpClient();

        Request req = new Request.Builder()
                .url(fullUrl)
                .build();

        client.newCall(req).enqueue(onFinish);

        Log.d(TAG, "ListAllCars: " + fullUrl);
    }

    public static void getModelDetails(String make, String model, String year, Callback onFinish) {
        String method = "vehicle/v2/";

        make = make.replace(" ", "");

        model = model.replace(" ", "");

        String queryParams = make + "/" + model + "/" + year + "/styles?view=full&fmt=json";

        String fullUrl = baseUrl + method + queryParams + apiKey;

        OkHttpClient client = new OkHttpClient();

        Request req = new Request.Builder()
                .url(fullUrl)
                .build();

        client.newCall(req).enqueue(onFinish);

        Log.d(TAG, "getModelDetails: " + fullUrl);
    }

    public static void getPictures(String pictureType, String make, String model, int year, Callback onFinish) {
        String method = "media/v2/";

        model = model.replace(" ", "");

        make = make.replace(" ", "");

        String queryParams = make + "/" + model + "/" + year + "/photos?shottype=" + pictureType + "&view=basic&fmt=json";

        String fullUrl = baseUrl + method + queryParams + apiKey;

        OkHttpClient client = new OkHttpClient();

        Request req = new Request.Builder()
                .url(fullUrl)
                .build();

        client.newCall(req).enqueue(onFinish);

        Log.d(TAG, "getPictures: " + fullUrl);
    }
}
