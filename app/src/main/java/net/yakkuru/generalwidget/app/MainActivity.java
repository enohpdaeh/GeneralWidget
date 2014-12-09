package net.yakkuru.generalwidget.app;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.Activity;
import android.app.LauncherActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import java.util.Iterator;

public class MainActivity extends Activity {
    /** JSONデータ取得URL */
    //private final String URL_API = "http://createjson.herokuapp.com/JSON";
    private final String URL_API = "http://createjson-dev.herokuapp.com/JSON";

    /** HTTPリクエスト管理Queue */
    private RequestQueue mQueue;

    /** 地区名用テキストビュー */
    TextView txtArea;
    /** 予報表示用リストビューのアダプター */
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 地区名用のテキストビュー
        txtArea = (TextView) findViewById(R.id.txtArea);
        // 予報表示用のリストビュー
        ListView listForecast = (ListView) findViewById(R.id.listForecast);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        listForecast.setAdapter(adapter);

        // HTTPリクエスト管理Queueを生成
        mQueue = Volley.newRequestQueue(this);

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
                        Iterator it = forecast.keys();
                        while(it.hasNext()){
                            String key = (String)it.next();
                            adapter.add(forecast.getString(key));
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
    }
}
