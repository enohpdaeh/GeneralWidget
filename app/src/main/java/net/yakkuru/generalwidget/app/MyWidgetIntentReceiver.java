package net.yakkuru.generalwidget.app;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

public class MyWidgetIntentReceiver extends BroadcastReceiver {

    private final String URL_API = "http://createjson-dev.herokuapp.com/JSON";
    private static String first; //日付
    private static String second;   //予報
    private static String third;
    private static String forth;
    private static String fifth;
    private static String sixth;
    private static String seventh;
    private static String eighth;
    /** HTTPリクエスト管理Queue */
    private RequestQueue mQueue;

    @Override
    public void onReceive(Context context, Intent intent) {
        // HTTPリクエスト管理Queueを生成
        mQueue = Volley.newRequestQueue(context);
        /* タイムアウト 10000ミリ秒（10秒）*/
        int custom_timeout_ms = 10000;
        DefaultRetryPolicy policy = new DefaultRetryPolicy(custom_timeout_ms,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);

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
                    JSONArray forecasts = response.getJSONArray("info");
                    for (int i = 0; i < forecasts.length(); i++) {
                        // 情報を取得
                        JSONObject forecast = forecasts.getJSONObject(i);
                        if(!forecast.isNull("0")) {
                            first = forecast.getString("0");
                        }
                        if(!forecast.isNull("1")) {
                            second = forecast.getString("1");
                        }
                        if(!forecast.isNull("2")) {
                            third = forecast.getString("2");
                        }
                        if(!forecast.isNull("4")) {
                            forth = forecast.getString("4");
                        }
                        if(!forecast.isNull("3")) {
                            fifth = forecast.getString("3");
                        }
                        if(!forecast.isNull("5")) {
                            sixth = forecast.getString("5");
                        }
                        if(!forecast.isNull("6")) {
                            seventh = forecast.getString("6");
                        }
                        if(!forecast.isNull("7")) {
                            eighth = forecast.getString("7");
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
        })).setRetryPolicy(policy);

        if (intent.getAction().equals("UPDATE_WIDGET")) {
            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
            //2行目
            remoteViews.setTextViewText(R.id.firsttext, first);
            remoteViews.setTextViewText(R.id.secondtext, second);
            remoteViews.setTextViewText(R.id.thirdtext, third);
            remoteViews.setTextViewText(R.id.fifthtext, forth);
            remoteViews.setTextViewText(R.id.forthtext, fifth);
            remoteViews.setTextViewText(R.id.sixthtext, sixth);
            remoteViews.setTextViewText(R.id.seventhtext, seventh);
            remoteViews.setTextViewText(R.id.eighthtext, eighth);

            // もう一回クリックイベントを登録(毎回登録しないと上手く動かず)
            remoteViews.setOnClickPendingIntent(R.id.LinearLayout, WidgetProvider.clickButton(context));
            WidgetProvider.pushWidgetUpdate(context.getApplicationContext(), remoteViews);
        }
    }
}
