package com.zgt.driver;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ClipData;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.webkit.GeolocationPermissions;
import android.webkit.JavascriptInterface;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.hdgq.locationlib.LocationOpenApi;
import com.hdgq.locationlib.entity.ShippingNoteInfo;
import com.hdgq.locationlib.listener.OnResultListener;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.zgt.driver.model.AnyEventType;
import com.zgt.driver.model.StartLocation;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


public class MainActivity extends AppCompatActivity  {
    WebView webView;
    private long exitTime = 0;
    List<StartLocation> startLocation;
    ShippingNoteInfo[] startLocations;
    private ValueCallback<Uri> uploadMessage;
    private ValueCallback<Uri[]> uploadMessageAboveL;
    private final static int FILE_CHOOSER_RESULT_CODE = 10000;
    private double lat = -1;
    private double lon = -1;
    public String tag = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        webView = findViewById(R.id.web_view);
        initPermission();
        EventBus.getDefault().register(this);
        WebSettings settings = webView.getSettings();
        settings.setDatabaseEnabled(true);
        String dir = this.getApplicationContext().getDir("database", Context.MODE_PRIVATE).getPath();
        settings.setGeolocationDatabasePath(dir);
        settings.setGeolocationEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setBuiltInZoomControls(false);
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        webView.getSettings().setBlockNetworkImage(false);//解决图片不显示
        webView.setWebChromeClient(new WebChromeClient() {

            // For Android < 3.0
            public void openFileChooser(ValueCallback<Uri> valueCallback) {
                uploadMessage = valueCallback;
                openImageChooserActivity();
            }

            // For Android  >= 3.0
            public void openFileChooser(ValueCallback valueCallback, String acceptType) {
                uploadMessage = valueCallback;
                openImageChooserActivity();
            }

            //For Android  >= 4.1
            public void openFileChooser(ValueCallback<Uri> valueCallback, String acceptType, String capture) {
                uploadMessage = valueCallback;
                openImageChooserActivity();
            }

            // For Android >= 5.0
            @Override
            public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
                uploadMessageAboveL = filePathCallback;
                openImageChooserActivity();
                return true;
            }
        });
        webView.loadUrl("http://212.64.72.2:98/index.html");
        settings.setJavaScriptEnabled(true);
        webView.addJavascriptInterface(new JSHook(), "android");
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
//        webView.setWebChromeClient(new WebChromeClient() {
//            @Override
//            public void onReceivedIcon(WebView view, Bitmap icon) {
//                super.onReceivedIcon(view, icon);
//
//            }
//
//            @Override
//            public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
//                callback.invoke(origin, true, false);
//                super.onGeolocationPermissionsShowPrompt(origin, callback);
//            }
//        });

        LocationOpenApi.init(this, "com.zgt.driver", "088f5e3b5cc04abfb777bcacccc99be0dcdc0824eaf64525bf12a84667bc79030128889eacf54256a1dac44960997416"
                , "360008", "debug", new OnResultListener() {
                    @Override
                    public void onSuccess() {
                        Toast.makeText(MainActivity.this, "定位上传初始化成功", Toast.LENGTH_SHORT).show();
//
                    }

                    @Override
                    public void onFailure(String s, String s1) {
                        Toast.makeText(MainActivity.this, "定位上传初始化失败", Toast.LENGTH_SHORT).show();
                    }
                });

    }


    private void initPermission() {
        //检查权限
        String[] permissions = CheckPermissionUtils.checkPermission(this);
        if (permissions.length == 0) {
            //权限都申请了
            //是否登录
        } else {
            //申请权限
            ActivityCompat.requestPermissions(this, permissions, 100);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getAction() == KeyEvent.ACTION_DOWN) {
            // System.currentTimeMillis()无论何时调用，肯定大于2000
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                AppManager.getAppManager().AppExit(this, false);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Subscribe
    public void onEventMainThread(AnyEventType event) {
        lat  = event.getLat();
        lon = event.getLon();
        Log.e("AmapError", "event"+event.getLat());
        upLoadLocation(lat+"",lon+"");
    }
    private void upLoadLocation(String s, String s1) {
        OkGo.<String>post("http://212.64.72.2:8080/wuche2/appUser/saveLocationLog")
                .tag(this)
                .cacheKey("cachePostKey")
                .cacheMode(CacheMode.NO_CACHE)
                .params("lgt", s1)
                .params("lat", s)
                .params("driver_id", startLocation.get(0).getDriver_id())
                .params("vehicle_id", startLocation.get(0).getVehicle_id())
                .params("shipid", startLocation.get(0).getShipid())
                .params("shiptype", 2)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {

                        Log.e("AmapError", "上传成功");
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        Log.e("AmapError", "上传失败");
                    }
                });
    }


    public class JSHook {
        @JavascriptInterface
        public void showToast(final String json) {
            Log.d(tag, "JS发送过来的登录类型" + json);
            startLocation = JSON.parseArray(json, StartLocation.class);
            //做判断是那种类型调用哪个登录方法
            if ("startLocation".equals(startLocation.get(0).getType())) {
                startAlarm();
                ShippingNoteInfo shippingNoteInfo = new ShippingNoteInfo();
                shippingNoteInfo.setShippingNoteNumber(startLocation.get(0).getShippingNoteNumber());
                shippingNoteInfo.setSerialNumber(startLocation.get(0).getSerialNumber());
                shippingNoteInfo.setStartCountrySubdivisionCode(startLocation.get(0).getStartCountrySubdivisionCode());
                shippingNoteInfo.setEndCountrySubdivisionCode(startLocation.get(0).getEndCountrySubdivisionCode());
                startLocations = new ShippingNoteInfo[]{shippingNoteInfo};
                LocationOpenApi.start(MainActivity.this, startLocations, new OnResultListener() {
                    @Override
                    public void onSuccess() {
                        Toast.makeText(MainActivity.this, "定位上传成功", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(String s, String s1) {
                        Toast.makeText(MainActivity.this, "定位上传失败", Toast.LENGTH_SHORT).show();
                    }
                });
            } else if ("stopLocation".equals(startLocation.get(0).getType())) {
                ShippingNoteInfo shippingNoteInfo = new ShippingNoteInfo();
                shippingNoteInfo.setShippingNoteNumber(startLocation.get(0).getShippingNoteNumber());
                shippingNoteInfo.setSerialNumber(startLocation.get(0).getSerialNumber());
                shippingNoteInfo.setStartCountrySubdivisionCode(startLocation.get(0).getStartCountrySubdivisionCode());
                shippingNoteInfo.setEndCountrySubdivisionCode(startLocation.get(0).getEndCountrySubdivisionCode());
                startLocations = new ShippingNoteInfo[]{shippingNoteInfo};
                LocationOpenApi.stop(MainActivity.this, startLocations, new OnResultListener() {
                    @Override
                    public void onSuccess() {
                        Toast.makeText(MainActivity.this, "定位上传已停止", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(String s, String s1) {
                        Toast.makeText(MainActivity.this, "定位上传停止失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
    }

    private void openImageChooserActivity() {
        Intent i = new Intent(Intent.ACTION_GET_CONTENT);
        i.addCategory(Intent.CATEGORY_OPENABLE);
        i.setType("image/*");
        startActivityForResult(Intent.createChooser(i, "Image Chooser"), FILE_CHOOSER_RESULT_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == FILE_CHOOSER_RESULT_CODE) {
            if (null == uploadMessage && null == uploadMessageAboveL) return;
            Uri result = data == null || resultCode != RESULT_OK ? null : data.getData();
            if (uploadMessageAboveL != null) {
                onActivityResultAboveL(requestCode, resultCode, data);
            } else if (uploadMessage != null) {
                uploadMessage.onReceiveValue(result);
                uploadMessage = null;
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void onActivityResultAboveL(int requestCode, int resultCode, Intent intent) {
        if (requestCode != FILE_CHOOSER_RESULT_CODE || uploadMessageAboveL == null)
            return;
        Uri[] results = null;
        if (resultCode == Activity.RESULT_OK) {
            if (intent != null) {
                String dataString = intent.getDataString();
                ClipData clipData = intent.getClipData();
                if (clipData != null) {
                    results = new Uri[clipData.getItemCount()];
                    for (int i = 0; i < clipData.getItemCount(); i++) {
                        ClipData.Item item = clipData.getItemAt(i);
                        results[i] = item.getUri();
                    }
                }
                if (dataString != null)
                    results = new Uri[]{Uri.parse(dataString)};
            }
        }
        uploadMessageAboveL.onReceiveValue(results);
        uploadMessageAboveL = null;
    }

    public static Uri getImageContentUri(Context context, String filePath) {//File imageFile
        //String filePath = imageFile.getAbsolutePath();//根据文件来获取路径
        Cursor cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Images.Media._ID}, MediaStore.Images.Media.DATA + "=? ",
                new String[]{filePath}, null);
        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(cursor.getColumnIndex(MediaStore.MediaColumns._ID));
            Uri baseUri = Uri.parse("content://media/external/images/media");
            return Uri.withAppendedPath(baseUri, "" + id);
        } else {
            if (TextUtils.isEmpty(filePath)) {//imageFile.exists()判断文件存不存在
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.DATA, filePath);
                return context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            } else {
                return null;
            }
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
    public void startAlarm() {
        //首先获得系统服务
        AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        //设置闹钟的意图，我这里是去调用一个服务，该服务功能就是获取位置并且上传
        Intent intent = new Intent(this, LocationService.class);
        PendingIntent pendSender = PendingIntent.getService(this, 0, intent, 0);
        am.cancel(pendSender);
        //AlarmManager.RTC_WAKEUP ;这个参数表示系统会唤醒进程；设置的间隔时间是1分钟
        am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 60 * 1000, pendSender);
    }
}
