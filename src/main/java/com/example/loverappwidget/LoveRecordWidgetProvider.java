package com.example.loverappwidget;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.RemoteViews;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
/**
 * Created by uwei on 2018/2/17.
 */

public class LoveRecordWidgetProvider extends AppWidgetProvider {
    private static String mDate;
    private static String mTime;
    private static String boy_name;
    private static String girl_name;
    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {
        boy_name = context.getSharedPreferences("setting", Context.MODE_PRIVATE).getString("boy_name", "");
        girl_name = context.getSharedPreferences("setting", Context.MODE_PRIVATE).getString("girl_name", "");
        mDate = context.getSharedPreferences("setting", Context.MODE_PRIVATE).getString("date", "");
        mTime = context.getSharedPreferences("setting", Context.MODE_PRIVATE).getString("time", "");
        String widgetText = boy_name + " 与 " + girl_name;
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_love);
        views.setTextViewText(R.id.boy_girl_name, widgetText);
        String path = context.getFilesDir().getPath() + File.separator + SetActivity.IMAGE_FILE_NAME;
        InputStream is = null;
        try {
            is = new FileInputStream(path);
            Bitmap bitmap = BitmapFactory.decodeStream(is);
            views.setImageViewBitmap(R.id.widget_img, bitmap);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of the
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
        Intent intent = new Intent(context,RecordService.class);
        context.startService(intent);
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
        super.onEnabled(context);
        Intent intent = new Intent(context,RecordService.class);
        context.startService(intent);
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
        Intent intent = new Intent(context,RecordService.class);
        context.stopService(intent);

    }

    @Override
    public void onReceive(Context context, Intent intent) {
        boy_name = context.getSharedPreferences("setting", Context.MODE_PRIVATE).getString("boy_name", "");
        girl_name = context.getSharedPreferences("setting", Context.MODE_PRIVATE).getString("girl_name", "");
        super.onReceive(context, intent);
        AppWidgetManager manager = AppWidgetManager.getInstance(context);
        RemoteViews views = new RemoteViews(context.getPackageName(),R.layout.widget_love);
        views.setTextViewText(R.id.boy_girl_name,boy_name + " 与 " + girl_name);
        InputStream is = null;
        try {
            String path = context.getFilesDir().getPath() + File.separator + SetActivity.IMAGE_FILE_NAME;
            is = new FileInputStream(path);
            Bitmap bitmap = BitmapFactory.decodeStream(is);
            views.setImageViewBitmap(R.id.widget_img, bitmap);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        ComponentName componentName = new ComponentName(context,LoveRecordWidgetProvider.class);
        manager.updateAppWidget(componentName,views);
        Intent service = new Intent(context,RecordService.class);
        context.startService(service);
    }
}
