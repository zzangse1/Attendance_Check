package com.zzangse.attendance_check.request;

import androidx.annotation.Nullable;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class GroupRequest extends StringRequest {
    private final static String URL = "http://zzangse.dothome.co.kr/Group_insert.php";
    private Map<String, String> map;

    public GroupRequest(String userID, String groupName, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);

        map = new HashMap<>();
        map.put("userID",userID);
        map.put("groupName", groupName);
    }

    @Nullable
    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return map;
    }
}