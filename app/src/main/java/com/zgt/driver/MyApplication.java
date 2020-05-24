package com.zgt.driver;

import android.app.Application;
import android.content.Context;
import android.content.Intent;

import com.hdgq.locationlib.keeplive.KeepLive;
import com.hdgq.locationlib.keeplive.config.ForegroundNotification;
import com.hdgq.locationlib.keeplive.config.ForegroundNotificationClickListener;
import com.hdgq.locationlib.keeplive.config.KeepLiveService;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        //进程保活 1. //定义前台服务的默认样式。即标题、描述和图标
        ForegroundNotification foregroundNotification = new ForegroundNotification("测试", "描述", R.mipmap.ic_launcher,
                //定义前台服务的通知点击事件
                new ForegroundNotificationClickListener() {
                    @Override
                    public void foregroundNotificationClick(Context context, Intent intent) {
                    }
                });
        //启动保活服务
        KeepLive.startWork(this, KeepLive.RunMode.ENERGY, foregroundNotification,
                //你需要保活的服务，如 socket 连接、定时任务等，建议不用匿名内部类 的方式在这里写
                new

                        KeepLiveService() {
                            /** 14. * 运行中 15. * 由于服务可能会多次自动启动，该方法可能重复调用 16. */
                            @Override
                            public void onWorking() {
                            }

                            /** 21. * 服务终止 22. * 由于服务可能会被多次终止，该方法可能重复调用，需同 onWorking 配 套使用， 23. * 如注册和注销 broadcast 24. */
                            @Override
                            public void onStop() {
                            }
                        });
    }


}
