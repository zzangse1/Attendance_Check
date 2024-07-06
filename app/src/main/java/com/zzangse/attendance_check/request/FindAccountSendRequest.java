package com.zzangse.attendance_check.request;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class FindAccountSendRequest extends StringRequest {
    private final static String URL = "http://zzangse.store/find_account/RandCode.php";
    private Map<String, String> map;

    public FindAccountSendRequest(String userEmail, String randomCode, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);

        map = new HashMap<>();
        map.put("verification_code_userEmail", userEmail);
        map.put("verification_code", randomCode);
    }

    @Override
    protected Map<String, String> getParams() {
        return map;
    }

    @Override
    public Map<String, String> getHeaders() {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/x-www-form-urlencoded");
        return headers;
    }
}
