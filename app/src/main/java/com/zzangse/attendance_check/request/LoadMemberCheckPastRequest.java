package com.zzangse.attendance_check.request;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

public class LoadMemberCheckPastRequest {
    private Context context;
    private static final String BASE_URL = "http://zzangse.store/load_member_check_past.php";

    public LoadMemberCheckPastRequest(Context context) {
        this.context = context;
    }

    private static String getBaseUrl() {
        return BASE_URL;
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkCapabilities capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.getActiveNetwork());
            return capabilities != null && (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) || capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR));
        }
        return false;
    }

    public void sendMemberOutputRequest(String groupName, final java.util.Date infoDate, final VolleyCallback callback) {
        if (!isNetworkAvailable()) {
            callback.onError("No internet connection");
            return;
        }
        StringRequest stringRequest = new StringRequest(Request.Method.POST, getBaseUrl(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            callback.onSuccess(jsonArray);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            callback.onError("JSON parsing error" + e.getMessage());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error instanceof NoConnectionError) {
                            callback.onError("No internet connection");
                        } else if (error instanceof TimeoutError) {
                            callback.onError("Request timed out");
                        } else if (error instanceof AuthFailureError) {
                            callback.onError("Authentication failure");
                        } else if (error instanceof ServerError) {
                            callback.onError("Server error");
                        } else if (error instanceof NetworkError) {
                            callback.onError("Network error");
                        } else if (error instanceof ParseError) {
                            callback.onError("Parse error");
                        } else {
                            callback.onError("Volley error: " + error.getMessage());
                        }
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                params.put("groupName", groupName);
                params.put("infoDate", dateFormat.format(infoDate));
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
