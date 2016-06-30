package com.dakshal.hexagontesting;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class OnBoardingNew extends Activity {
    private static final int NUMBER_OF_ROWS = 3;

    /*@Bind(R.id.tvDeviceCount)
    static TextView tvDeviceCount;

    @Bind(R.id.tvRewardCount)
    static TextView tvRewardCount;*/


    //    @Bind(R.id.btnSkip)
    static Button btnSkip;


    static TextView tvDeviceCount;
    static TextView tvRewardCount;

//    private ArrayList<ConsumerProduct> consumerProducts;
//    private ArrayList<UnsupportedConsumerProduct> unsupportedProducts;

    private String TAG = "OnBoardingNew";
    public static int hexSize;

    float x1, x2, lastX, lastY;

    public static int noOfDeviceSelected = 0;

    public static String[] images = new String[]{"white_ac", "white_tv", "white_fridge", "white_washing_machine", "white_camera",
            "white_laptop", "white_mobile", "white_tablet", "white_microwave", "white_router", "white_audio_system", "white_air_purifier",
            "white_juicer_mixer",
            "white_printer",
            "white_vaccum_cleaner",
            "white_water_purifier"};

    public static ArrayList<Integer> productSubCategory = new ArrayList<>();

    private boolean isTemp;
    HashMap<String, Object> param;
    String deviceID = "";
    ArrayList<String> imageList;

    static int rewardPoints = 100;

    public static int selectedDevices[];
    /*
    *
    * there are four states for selected devices
    * States:
    *       0. not selected
    *       1. selected and changeable
    *       2. intermediate selection
    *       3. intermediate deselection
    *       4. selected and fixed not allowed to change
    * */

//    public static ArrayList<ProductSubCategory> productSubCategories;

    public static ArrayList<Integer> selected = new ArrayList<>();

    private int READ_PHONE_STATE_PERMISSION_CODE = 1;
    HexGridView gridView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

//        context = (OnBoardingNew) getApplicationContext();

        tvDeviceCount = (TextView) findViewById(R.id.tvDeviceCount);
        tvRewardCount = (TextView) findViewById(R.id.tvRewardCount);
        btnSkip = (Button) findViewById(R.id.btnSkip);

        productSubCategory.add(6);
        productSubCategory.add(7);
        productSubCategory.add(8);
        productSubCategory.add(9);
        productSubCategory.add(10);
        productSubCategory.add(11);
        productSubCategory.add(12);
        productSubCategory.add(13);
        productSubCategory.add(14);
        productSubCategory.add(15);
        productSubCategory.add(16);


//        isTemp = Hawk.get(AppConstant.IS_TEMP_CONSUMER);

//        productSubCategories = Hawk.get(AppConstant.PRODUCT_CATEGORY);

//        images = new String[productSubCategories.size()];


        selectedDevices = new int[productSubCategory.size()];

        gridView = new HexGridView(this, productSubCategory.size(), NUMBER_OF_ROWS);
//        final HexGridView gridView = new HexGridView(this, images.length, 4);

        RelativeLayout rlTest = (RelativeLayout) findViewById(R.id.rlTest);
        final HorizontalScrollView hsv = (HorizontalScrollView) findViewById(R.id.hsv);

        hsv.setHorizontalScrollBarEnabled(false);
        hsv.setVerticalScrollBarEnabled(false);
        hsv.setSmoothScrollingEnabled(true);


        rlTest.addView(gridView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        int maxScroll = hsv.getMaxScrollAmount();
        Log.d(TAG, "onCreate: " + maxScroll);
//        hsv.scrollTo(maxScroll/ 4, 0);

        Log.d(TAG, "onCreate: " + maxScroll);

        rlTest.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

//                printSamples(event);

                if (event.getAction() == MotionEvent.ACTION_UP) {
                    Log.d(TAG, "onClick: scroll distance" + hsv.getScrollX());
                    gridView.transformColor(event.getX(), event.getY());
                } else if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    Log.d(TAG, "onClick: scroll distance" + hsv.getScrollX());
                    x1 = event.getX();
                    Log.d(TAG, "x1: " + x1);
                    lastX = event.getX();
                    lastY = event.getY();
                    gridView.itemSelected(event.getX(), event.getY());
                } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
//                    x2 = event.getX();
//                    Log.d(TAG, "x2: " + x2);
//                    if (distance(x1, x2) > Utilities.instance.dpToPx(hexSize, context)) {
//                        Log.d(TAG, "onClick: scroll distance" + hsv.getScrollX());
//                        gridView.getBacktoDetault(event.getX(), event.getY());
//                    } else {
//                        Log.d(TAG, "onTouch: " + distance(x1, x2) + " hexSize: " + (hexSize / 4));
//                    }
//                    gridView.getBacktoDetault(lastX, lastY);
                } else if (event.getAction() == MotionEvent.ACTION_CANCEL) {
                    x2 = event.getX();
                    Log.d(TAG, "ACTION_CANCEL: " + x2);
//                    if (distance(x1, x2) > Utilities.instance.dpToPx(hexSize, context)) {
                    Log.d(TAG, "ACTION_CANCEL: scroll distance" + hsv.getScrollX());
                    gridView.getBacktoDetault(event.getX(), event.getY());
//                    } else {
//                        Log.d(TAG, "ACTION_CANCEL: " + distance(x1, x2) + " hexSize: " + (hexSize / 4));
//                    }
//                    gridView.getBacktoDetault(lastX, lastY);
                }

                return true;
            }
        });

//        if (isTemp) {
//            addCurrentDevice();
//        } else {
//            setUpDevices();
//        }

    }

//    private void setUpDevices() {
//        HashMap<String, Object> param = new HashMap<>();
//        param.put("IsServiceable", 0);
//        if (isTemp) {
//            param.put("TempConsumerID", servify.getTempConsumer().getTempConsumerID());
//            fetchDeviceList();
//        } else {
//            param.put("ConsumerID", servify.getConsumer().getConsumerID());
//            fetchDeviceList();
//        }
//    }
//
//    public void fetchDeviceList() {
//
//        service.getUserDevices(Utilities.instance.getAccessToken(), param, new Callback<ServifyResponse<ConsumerProductsResponse>>() {
//            @Override
//            public void success(ServifyResponse<ConsumerProductsResponse> servifyResponse, Response response) {
//
//                if (servifyResponse.isSuccess()) {
//
////                    if (isAdded()) {
//                    //Logger.t(TAG + "TOTAL ==> ").json(gson.toJson(servifyResponse.getData()));
//
//                    consumerProducts = servifyResponse.getData().getSupportedDevices();
//                    unsupportedProducts = servifyResponse.getData().getUnSupportedDevices();
//
//
//                    int total = consumerProducts.size() + unsupportedProducts.size();
//                    Log.e(TAG, "TOTAL ==>" + total);
//
//                    if (total == 0) {
//
//                    } else {
//                        for (int i = 0; i < consumerProducts.size(); i++) {
//                            if (selectedDevices[consumerProducts.get(i).getProductSubCategory().getProductSubCategoryId() - 6] != 4) {
//                                noOfDeviceSelected++;
//                                selectedDevices[consumerProducts.get(i).getProductSubCategory().getProductSubCategoryId() - 6] = 4;
//                            }
//                        }
//                        for (int i = 0; i < unsupportedProducts.size(); i++) {
//                            if (selectedDevices[unsupportedProducts.get(i).getProductSubCategory().getProductSubCategoryId() - 6] != 4) {
//                                noOfDeviceSelected++;
//                                selectedDevices[unsupportedProducts.get(i).getProductSubCategory().getProductSubCategoryId() - 6] = 4;
//                            }
//                        }
//                        gridView.addDefaultDevices();
//                    }
////                    }
//                }
//            }
//
//            @Override
//            public void failure(RetrofitError error) {
//
//            }
//        });
//        /*.getUserDevices(Utilities.instance.getAccessToken(), param).enqueue(new Callback<ServifyResponse<ConsumerProductsResponse>>() {
//            @Override
//            public void onResponse(Call<ServifyResponse<ConsumerProductsResponse>> call, Response<ServifyResponse<ConsumerProductsResponse>> response) {
//
//                ServifyResponse<ConsumerProductsResponse> servifyResponse = response.body();
//
//                if (servifyResponse.isSuccess()) {
//
//                    //Logger.t(TAG + "TOTAL ==> ").json(gson.toJson(servifyResponse.getData()));
//
////                    RealmProvider.getInstance().beginTransaction();
//
////                    RealmProvider.getInstance().where(ConsumerProductsResponse.class).findAll().deleteAllFromRealm();
////                    RealmProvider.getInstance().where(ConsumerProduct.class).findAll().deleteAllFromRealm();
////                    RealmProvider.getInstance().where(UnsupportedConsumerProduct.class).findAll().deleteAllFromRealm();
////                    RealmProvider.getInstance().copyToRealm(servifyResponse.getData());
//
////                    RealmProvider.getInstance().commitTransaction();
//
////                    RealmList<ConsumerProduct> consumerProducts = servifyResponse.getData().getSupportedDevices();
////                    RealmList<UnsupportedConsumerProduct> unsupportedProducts = servifyResponse.getData().getUnSupportedDevices();
//
//                    int total = consumerProducts.size() + unsupportedProducts.size();
//                    Log.e(TAG, "TOTAL ==>" + total);
//
//                    if (total == 0) {
//
//                    } else {
//                        for (int i = 0; i < consumerProducts.size(); i++) {
//                            if (selectedDevices[consumerProducts.get(i).getProductSubCategory().getProductSubCategoryID() - 6] != 4) {
//                                noOfDeviceSelected++;
//                                selectedDevices[consumerProducts.get(i).getProductSubCategory().getProductSubCategoryID() - 6] = 4;
//                            }
//                        }
//                        for (int i = 0; i < unsupportedProducts.size(); i++) {
//                            if (selectedDevices[unsupportedProducts.get(i).getProductSubCategory().getProductSubCategoryID() - 6] != 4) {
//                                noOfDeviceSelected++;
//                                selectedDevices[unsupportedProducts.get(i).getProductSubCategory().getProductSubCategoryID() - 6] = 4;
//                            }
//                        }
//                        gridView.addDefaultDevices();
//                    }
//                }
//            }
//
//            @Override
//            public void onFailure(Call<ServifyResponse<ConsumerProductsResponse>> call, Throwable t) {
//
//            }
//
//        });*/
//    }
//
///*    public void fetchTempConsumerProducts() {
//
//        HashMap<String, Object> param = new HashMap<>();
//        param.put("TempConsumerID", servify.getTempConsumer().getTempConsumerID());
//        param.put("IsServiceable", 0);
//
//        RestClient.instance.getApiService().getTempConsumerDevices(param).enqueue(new Callback<ServifyResponse<TempConsumerProductsResponse>>() {
//            @Override
//            public void onResponse(Call<ServifyResponse<TempConsumerProductsResponse>> call, Response<ServifyResponse<TempConsumerProductsResponse>> response) {
//
//                ServifyResponse<TempConsumerProductsResponse> servifyResponse = response.body();
//
//                consumerProducts = new ArrayList<ConsumerProduct>();
//                unsupportedProducts = new ArrayList<UnsupportedConsumerProduct>();
//                consumerProducts.addAll(servifyResponse.getData().getSupportedDevices().subList(0, servifyResponse.getData().getSupportedDevices().size()));
//                unsupportedProducts.addAll(servifyResponse.getData().getUnSupportedDevices().subList(0, servifyResponse.getData().getUnSupportedDevices().size()));
//
//                int total = consumerProducts.size() + unsupportedProducts.size();
//
//                Log.e(TAG, "TOTAL ==>" + total);
//
//                if (total == 0) {
//
//                } else {
//                    for (int i = 0; i < consumerProducts.size(); i++) {
//                        if (selectedDevices[consumerProducts.get(i).getProductSubCategory().getProductSubCategoryID() - 6] != 4) {
//                            noOfDeviceSelected++;
//                            selectedDevices[consumerProducts.get(i).getProductSubCategory().getProductSubCategoryID() - 6] = 4;
//                        }
//                    }
//                    for (int i = 0; i < unsupportedProducts.size(); i++) {
//                        if (selectedDevices[unsupportedProducts.get(i).getProductSubCategory().getProductSubCategoryID() - 6] != 4) {
//                            noOfDeviceSelected++;
//                            selectedDevices[unsupportedProducts.get(i).getProductSubCategory().getProductSubCategoryID() - 6] = 4;
//                        }
//                    }
//                    gridView.addDefaultDevices();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<ServifyResponse<TempConsumerProductsResponse>> call, Throwable t) {
//
//            }
//
//        });
//    }*/
//
//    private void addCurrentDevice() {
//
//        boolean isPrompted = Hawk.get("DefaultDevice", false);
//
//        if (!isPrompted) {
//
//            if (isTemp) {
//
//                //SubCategory : Mobile
//                //Brand :
//                String manufacturer = Build.MANUFACTURER;
//                String brandName = Build.MANUFACTURER + " " + Build.BRAND;
//
//                //Product :
//                String model = Build.MODEL;
//
//                String test = Build.PRODUCT + " Device: " + Build.DEVICE + " Model: " + Build.MODEL;
//
//                Log.d(TAG, "Brand Name " + brandName);
//                Log.d(TAG, "Product: " + test);
//                Log.d(TAG, "Serial No: " + Build.SERIAL);
//
//                TempConsumerProduct defaultDevice = new TempConsumerProduct();
//
//                int tempConsumerID = ((TempConsumer) Hawk.get(AppConstant.TEMP_CONSUMER)).getTempConsumerID();
//                param = new HashMap<>();
//                param.put("TempConsumerID", "" + tempConsumerID);
//                param.put("ProductName", Build.MODEL);
//                param.put("Brand", Build.BRAND);
//                param.put("Manufacturer", Build.MANUFACTURER);
//                param.put("Device", Build.DEVICE);
//                param.put("DownloadedDeviceUniqueKey", Build.SERIAL);
//
//                defaultDevice.setTempConsumerID(tempConsumerID);
//                defaultDevice.setProductName(Build.MODEL);
//                defaultDevice.setBrand(Build.MANUFACTURER);
//                defaultDevice.setProductUniqueID(Build.SERIAL);
////                Realm realm = Realm.getDefaultInstance();
////                realm.beginTransaction();
////                realm.copyToRealm(defaultDevice);
////                realm.commitTransaction();
////                realm.close();
//
////                param.put("IsUnderWarranty", true);
////                param.put("WarrantyTill", "2017-05-20");
//                getIMEINumber();
//            }
//
//        } else {
//            setUpDevices();
//        }
//
//    }
//
//    private String getIMEINumber() {
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            if (Utilities.instance.permissionCheck(this, Manifest.permission.READ_PHONE_STATE)) {
//                TelephonyManager tm = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
//                deviceID = tm.getDeviceId();
//                param.put("ProductUniqueID", deviceID);
//                saveDefaultDevice(param, true);
//            } else {
//                requestPermissions(new String[]{Manifest.permission.READ_PHONE_STATE}, READ_PHONE_STATE_PERMISSION_CODE);
//            }
//        } else {
//            TelephonyManager tm = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
//            deviceID = tm.getDeviceId();
//            param.put("ProductUniqueID", deviceID);
//            saveDefaultDevice(param, true);
//        }
//        return deviceID;
//    }
//
//    /*private void saveDefaultDevice(HashMap<String, Object> param, boolean isActive) {
//
//        param.put("IsActive", isActive);
//
//        Log.d(TAG, "ADD DEFAULT DEVICE " + param);
//
//        service.addDefaultDevice(param).enqueue(new Callback<ServifyResponse>() {
//            @Override
//            public void onResponse(Call<ServifyResponse> call, Response<ServifyResponse> response) {
//                ServifyResponse servifyResponse = response.body();
//
//            }
//
//            @Override
//            public void onFailure(Call<ServifyResponse> call, Throwable t) {
//            }
//        });
//    }
//    */
//    private void saveDefaultDevice(HashMap<String, Object> param, boolean isActive) {
//
//        param.put("IsActive", isActive);
//
//        Log.d(TAG, "ADD DEFAULT DEVICE " + param);
//
//        service.addDefaultDevice(param, new Callback<ServifyResponse>() {
//            @Override
//            public void success(ServifyResponse servifyResponse, Response response) {
//
//                if (servifyResponse.isSuccess()) {
//
//                    Hawk.put("DefaultDevice", true);
//
//                    Hawk.put(AppConstant.IS_DEFAULT_DEVICE_ADDED, true);
//
//                    selectedDevices[6] = 4;
//
//                    noOfDeviceSelected++;
//
//                    setUpDevices();
//
//                    gridView.addDefaultDevices();
//
//
//                    Utilities.instance.showServifyDialog(context, R.drawable.first_device, new ServifyDialogClick() {
//                        @Override
//                        public void btnClickYes() {
//
//
//                            //tvMyDevicesNumber.setText(String.valueOf(totalProducts));
//
//                            Utilities.instance.dismissServifyDialog();
//                        }
//
//                        @Override
//                        public void btnClickNo() {
//
//                            Utilities.instance.dismissServifyDialog();
//
//                        }
//
//                    }, "SUPER", "We have added your smartphone to Servify. Go ahead and add more devices.", "Sure");
//                }
//            }
//
//            @Override
//            public void failure(RetrofitError error) {
////                initFragmentData();
//
//            }
//        });
//    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode,
//                                           @NonNull String permissions[],
//                                           @NonNull int[] grantResults) {
//        if (requestCode == READ_PHONE_STATE_PERMISSION_CODE) {
//            Log.d("TAG", "onRequestPermissionsResult: " + grantResults.length);
//            if (grantResults.length == 1 &&
//                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//
//                TelephonyManager tm = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
//                deviceID = tm.getDeviceId();
//                param.put("ProductUniqueID", deviceID);
//                saveDefaultDevice(param, true);
//
//            } else {
//                deviceID = "";
//                Toast.makeText(this, "Telephony Service denied", Toast.LENGTH_SHORT).show();
//            }
//        } else {
//            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        }
//    }

    void printSamples(MotionEvent ev) {
        final int historySize = ev.getHistorySize();
        final int pointerCount = ev.getPointerCount();
        for (int h = 0; h < historySize; h++) {
            Log.d("At time %d:", "" + ev.getHistoricalEventTime(h));
            for (int p = 0; p < pointerCount; p++) {
                Log.d("pointer %d: (%f,%f)", ev.getPointerId(p) + "\t " + ev.getHistoricalX(p, h) + "\t " + ev.getHistoricalY(p, h));
            }
        }
        Log.d("At time %d:", "" + ev.getEventTime());
        for (int p = 0; p < pointerCount; p++) {
            Log.d("  pointer %d: (%f,%f)", ev.getPointerId(p) + "\t " + ev.getX(p) + "\t " + ev.getY(p));
        }
    }

    public static void setSelectedDeviceCount() {
        tvDeviceCount.setText(noOfDeviceSelected + "");
        tvRewardCount.setText(noOfDeviceSelected * rewardPoints + "");

        if (noOfDeviceSelected > 0) {
            btnSkip.setTextColor(Color.BLACK);
            btnSkip.setText("Next");
            btnSkip.setBackgroundColor(Color.TRANSPARENT);
        } else {
            btnSkip.setText("Skip");
            btnSkip.setBackgroundColor(Color.TRANSPARENT);
            btnSkip.setTextColor(Color.BLACK);
        }
    }

    private int distance(float x1, float x2) {
        return (int) Math.abs(x2 - x1);
    }


//    @OnClick(R.id.btnSkip)
//    public void next() {
//        ArrayList<Integer> productSubCategory = new ArrayList<>();
//        for (int i = 0; i < images.length; i++) {
//            if (selectedDevices[i] == 1 || selectedDevices[i] == 4) {
//                productSubCategory.add((i + 6));
//                Logger.d(images[i] + " productSubcategoryID: " + (i + 6));
//            }
//        }
//
//        if (isTemp) {
//            TempConsumer tempConsumer = Hawk.get(AppConstant.TEMP_CONSUMER, null);
//            if (tempConsumer != null) {
//                HashMap<String, Object> params = new HashMap<>();
//                params.put("TempConsumerID", tempConsumer.getTempConsumerID());
//                params.put("DeviceType", "android");
//                params.put("ProductSubCategory", productSubCategory);
//
//                service.addDeviceTempConsumerBulk(Utilities.instance.getAccessToken(), params, new Callback<ServifyResponse>() {
//                    @Override
//                    public void success(ServifyResponse servifyResponse, Response response) {
//                        if (servifyResponse.isSuccess()) {
//                            Intent mainIntent = new Intent(context, HomeActivity.class);
//                            startActivity(mainIntent);
//                            AppPreferences.setOnBoardingStatus(true);
//                            finish();
//                        } else {
//                            Toast.makeText(OnBoardingNew.this, servifyResponse.getMsg(), Toast.LENGTH_SHORT).show();
//                        }
//                    }
//
//                    @Override
//                    public void failure(RetrofitError error) {
//
//                    }
//                });
//            } else {
//                Intent mainIntent = new Intent(context, SplashScreenActivity.class);
//                startActivity(mainIntent);
//                finish();
//            }
//        } else {
//            Consumer consumer = Hawk.get(AppConstant.CONSUMER, null);
//            if (consumer != null) {
//                HashMap<String, Object> params = new HashMap<>();
//                params.put("ConsumerID", consumer.getConsumerID());
//                params.put("DeviceType", "android");
//                params.put("ProductSubCategory", productSubCategory);
//
//                service.addDeviceConsumerBulk(Utilities.instance.getAccessToken(), params, new Callback<ServifyResponse>() {
//                    @Override
//                    public void success(ServifyResponse servifyResponse, Response response) {
//                        if (servifyResponse.isSuccess()) {
//                            Intent mainIntent = new Intent(context, HomeActivity.class);
//                            startActivity(mainIntent);
//                            AppPreferences.setOnBoardingStatus(true);
//                            finish();
//                        } else {
//                            Toast.makeText(OnBoardingNew.this, servifyResponse.getMsg(), Toast.LENGTH_SHORT).show();
//                        }
//                    }
//
//                    @Override
//                    public void failure(RetrofitError error) {
//
//                    }
//                });
//            } else {
//                Intent mainIntent = new Intent(context, SplashScreenActivity.class);
//                startActivity(mainIntent);
//                finish();
//            }
//        }
//
////        Intent mainIntent = new Intent(this, HomeActivity.class);
////        startActivity(mainIntent);
////        AppPreferences.saveOnBoardingStatus(true);
////        finish();
//    }

}