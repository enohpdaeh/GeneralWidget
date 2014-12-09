package net.yakkuru.generalwidget.app;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.util.Log;
import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

/**
 * Created by takuya on 14/11/10.
 */
public class MyWidgetIntentReceiver extends BroadcastReceiver {

    //private final String URL_API = "http://createjson.herokuapp.com/JSON";
    private final String URL_API = "http://createjson-dev.herokuapp.com/JSON";
    private static String sTelop;
    private static String sCity;
    private static String sDeltaTemp;
    private static String sDeltaLight;
    private static String sNow;
    private static String sOdakyuTime;
    private static String sOdakyuStatus;
    /** HTTPリクエスト管理Queue */
    private RequestQueue mQueue;


    @Override
    public void onReceive(Context context, Intent intent) {
        // HTTPリクエスト管理Queueを生成
        mQueue = Volley.newRequestQueue(context);

        // リクエスト実行
        mQueue.add(new JsonObjectRequest(Method.GET, URL_API, null, new Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                // --------------------------------------------
                // JSONObjectのパース、List、Viewへの追加等
                // --------------------------------------------
                // ログ出力
                Log.d("temakishiki", "response : " + response.toString());

                try {
                    JSONArray forecasts = response.getJSONArray("weather");
                    for (int i = 0; i < forecasts.length(); i++) {
                        // 予報情報を取得
                        JSONObject forecast = forecasts.getJSONObject(i);
                        // 日付
                        if(!forecast.isNull("0")) {
                            sNow = forecast.getString("0");
                        }
                        // 予報
                        if(!forecast.isNull("1")) {
                            sTelop = forecast.getString("1");
                        }
                        //
                        if(!forecast.isNull("2")) {
                            sCity = forecast.getString("2");
                        }
                        if(!forecast.isNull("3")) {
                            sDeltaLight = forecast.getString("3");
                        }
                        if(!forecast.isNull("4")) {
                            sDeltaTemp = forecast.getString("4");
                        }
                        if(!forecast.isNull("5")) {
                            sOdakyuTime = forecast.getString("5");
                        }
                        if(!forecast.isNull("6")) {
                            sOdakyuStatus = forecast.getString("6");
                        }
                    }
                } catch (JSONException e) {
                    Log.e("temakishiki", e.getMessage());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // --------------------------------------------
                // エラー処理 error.networkResponseで確認
                // --------------------------------------------
                if (error.networkResponse != null) {
                    Log.e("temakishiki", "エラー : " + error.networkResponse.toString());
                }
            }
        }));

        if (intent.getAction().equals("UPDATE_WIDGET")) {
            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
            //2行目
            remoteViews.setTextViewText(R.id.title, sNow);
            remoteViews.setTextViewText(R.id.secondtext, sTelop);
            remoteViews.setTextViewText(R.id.thirdtext, sCity);
            remoteViews.setTextViewText(R.id.forthtext, sDeltaLight);
            remoteViews.setTextViewText(R.id.fifthtext, sDeltaTemp);
            remoteViews.setTextViewText(R.id.sixthtext, sOdakyuTime);
            remoteViews.setTextViewText(R.id.seventhtext, sOdakyuStatus);


            // もう一回クリックイベントを登録(毎回登録しないと上手く動かず)
            remoteViews.setOnClickPendingIntent(R.id.button, WidgetProvider.clickButton(context));

            WidgetProvider.pushWidgetUpdate(context.getApplicationContext(), remoteViews);
        }
    }
}
