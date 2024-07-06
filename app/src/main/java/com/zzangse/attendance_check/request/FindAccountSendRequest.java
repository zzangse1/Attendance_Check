package com.zzangse.attendance_check.request;

import android.util.Log;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class FindAccountSendRequest extends StringRequest {
    private final static String URL = "http://zzangse.store/find_account/RandCode.php";
    private Map<String, String> map;

    public FindAccountSendRequest(String userID, String randomCode, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);

        map = new HashMap<>();
        map.put("userID", userID);
        map.put("randCode", randomCode);
        Log.d("FIndAccount", userID + ", " + randomCode);
    }

    @Override
    protected Map<String, String> getParams() {
        return map;
    }

}
