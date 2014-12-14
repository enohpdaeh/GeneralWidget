package net.yakkuru.generalwidget.app;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

public class WidgetProvider extends AppWidgetProvider {

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

        // ウィジェットレイアウトの初期化
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
        // ボタンイベントを登録
        remoteViews.setOnClickPendingIntent(R.id.button, clickButton(context));

        // テキストフィールドに"初期画面"と表示
        remoteViews.setTextViewText(R.id.firsttext, "First Line");

        // テキストフィールドに"初期画面"と表示
        remoteViews.setTextViewText(R.id.secondtext, "Second Line");
        remoteViews.setTextViewText(R.id.thirdtext, "Third Line");
        remoteViews.setTextViewText(R.id.forthtext, "Forth Line");
        remoteViews.setTextViewText(R.id.fifthtext, "Fifth Line");
        remoteViews.setTextViewText(R.id.eighthtext, "eighth Line");

        // アップデートメソッド呼び出し
        pushWidgetUpdate(context, remoteViews);
    }

    public static PendingIntent clickButton(Context context) {
        // initiate widget update request
        Intent intent = new Intent();
        intent.setAction("UPDATE_WIDGET");
        return PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    // アップデート
    public static void pushWidgetUpdate(Context context, RemoteViews remoteViews) {
        ComponentName myWidget = new ComponentName(context, WidgetProvider.class);
        AppWidgetManager manager = AppWidgetManager.getInstance(context);
        Intent intent = new Intent();
        intent.setAction("UPDATE_WIDGET");
        manager.updateAppWidget(myWidget, remoteViews);
    }
}
