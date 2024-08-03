package com.zzangse.attendance_check.request;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class ShowIDRequest extends StringRequest {
    private final static String URL = "http://zzangse.store/find_account/ShowID.php";

    private Map<String, String> map;


    public ShowIDRequest(String identifier, String userEmail, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);
        map = new HashMap<>();
        map.put("userName", identifier);
        map.put("userEmail", userEmail);


    }

    @Override
    protected Map<String, String> getParams() {
        return map;
    }

}
