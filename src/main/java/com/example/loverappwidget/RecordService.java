package com.example.loverappwidget;

import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.RemoteViews;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by uwei on 2018/2/18.
 */

public class RecordService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        new Thread(new Runnable() {
            @Override
            public void run() {
                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        update();
                    }
                },0,1000);
            }
        }).start();

    }
    public void update(){
        String mDate = getSharedPreferences("setting", Context.MODE_PRIVATE).getString("date", "");
        String mTime = getSharedPreferences("setting", Context.MODE_PRIVATE).getString("time", "");
        String mTitle = getSharedPreferences("setting", Context.MODE_PRIVATE).getString("title", "");
        String beginTime = mDate + " " + mTime;
        Log.d("ww",beginTime);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date now = new Date();
        try {
            Date begin = df.parse(beginTime);
            long l = now.getTime() - begin.getTime();
            Log.d("ww",now+"");
            Log.d("ww",begin+"");
            long day = l / (24 * 60 * 60 * 1000);
            long hour = (l / (60 * 60 * 1000) - day * 24);
            long min = ((l / (60 * 1000)) - day * 24 * 60 - hour * 60);
            long s = (l / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
            RemoteViews views = new RemoteViews(getPackageName(), R.layout.widget_love);
            Log.d("ww",""+day+"天"+hour+"小时"+min+"分"+s+"秒");
            views.setTextViewText(R.id.record, mTitle +": " + day + "天" + hour + "小时" + min + "分" + s + "秒");
            AppWidgetManager manager = AppWidgetManager.getInstance(this);
            ComponentName componentName = new ComponentName(this, LoveRecordWidgetProvider.class);
            //调用APPWidgetManager将RemoteViews添加到ComponentName中
            manager.updateAppWidget(componentName, views);

        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        flags = START_STICKY;
        return super.onStartCommand(intent, flags, startId);
    }
}
