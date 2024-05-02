package com.zzangse.attendance_check.request;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.HashMap;
import java.util.Map;

public class TestRequest  {
    private Context context;
    private static final String BASE_URL = "http://zzangse.dothome.co.kr/LoadGroupName.php";

    public TestRequest(Context context) {
        this.context = context;
    }
    public void TestRequest(final String userID, final VolleyCallback callback) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, BASE_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            callback.onSuccess(jsonArray);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            callback.onError("JSON parsing error");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        callback.onError("Volley error: " + error.getMessage());
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("userID", userID);
                return params;
            }
        };

        // 요청 큐에 추가
        Volley.newRequestQueue(context).add(stringRequest);
    }

    public interface VolleyCallback {
        void onSuccess(JSONArray result);
        void onError(String errorMessage);
    }
}