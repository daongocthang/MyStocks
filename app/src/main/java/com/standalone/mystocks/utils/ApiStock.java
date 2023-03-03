package com.standalone.mystocks.utils;

import android.content.Context;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.standalone.mystocks.models.DataStock;
import com.standalone.mystocks.requests.HttpVolley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ApiStock {

    private static final String TAG = ApiStock.class.getSimpleName();
    private final Context context;


    public ApiStock(Context context) {
        this.context = context;
    }

    public void requestAllStocks(OnResponseListener<List<DataStock>> responseListener) {
        String url = "https://iboard.ssi.com.vn/dchart/api/1.1/defaultAllStocks";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    Log.e(TAG, "AllStocks: " + response.toString());
                    JSONArray data = response.getJSONArray("data");
                    List<DataStock> dataResponse = new ArrayList<>();
                    for (int i = 0; i < data.length(); i++) {
                        JSONObject object = data.getJSONObject(i);
                        if (object.getString("type").equals("s")) {
                            DataStock dataStock = new DataStock();
                            dataStock.setStockNo(object.getString("stockNo"));
                            dataStock.setSymbol(object.getString("code"));
                            dataStock.setShortName(object.getString("clientName"));

                            dataResponse.add(dataStock);
                        }
                    }
                    responseListener.onResponse(dataResponse);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "JsonObjectRequest onErrorResponse: " + error.getMessage());
            }
        });

        HttpVolley.getInstance(context).getRequestQueue().add(request);
    }

    public void requestMatchedPrice(String[] stocks, OnResponseListener<HashMap<String, Double>> responseListener) {
        String url = "https://wgateway-iboard.ssi.com.vn/graphql";

        String body = "{" +
                "    'operationName': 'stockRealtimesByIds'," +
                "    'variables': {" +
                "        'ids': [" + String.join(",", stocks) + "]" +
                "    }," +
                "    'query': 'query stockRealtimesByIds($ids: [String!]) {\\n  stockRealtimesByIds(ids: $ids) {\\n    stockNo\\n    stockSymbol\\n    refPrice\\n    matchedPrice\\n  }\\n}\\n'" +
                "}";

        try {
            JSONObject jsonBody = new JSONObject(body.replace("'", "\""));
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, jsonBody, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        JSONArray data = response.getJSONObject("data").getJSONArray("stockRealtimesByIds");
                        Log.e(TAG, "StockRealTIme: " + data.toString());
                        HashMap<String, Double> dataResponse = new HashMap<>();
                        for (int i = 0; i < data.length(); i++) {
                            JSONObject object = data.getJSONObject(i);
                            for (String s : stocks) {
                                double last;
                                if (s.equals(object.getString("stockNo"))) {
                                    last = object.getDouble("matchedPrice");
                                    if (last == 0) {
                                        last = object.getDouble("refPrice");
                                    }
                                    dataResponse.put(s, last / 1000);
                                }
                            }
                        }

                        responseListener.onResponse(dataResponse);


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e(TAG, "JsonObjectRequest onErrorResponse: " + error.getMessage());
                    responseListener.onError();
                }
            });

            request.setRetryPolicy(new DefaultRetryPolicy(
                    DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
            ));

            HttpVolley.getInstance(context).getRequestQueue().add(request);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public interface OnResponseListener<T> {
        void onResponse(T t);

        void onError();
    }
}
