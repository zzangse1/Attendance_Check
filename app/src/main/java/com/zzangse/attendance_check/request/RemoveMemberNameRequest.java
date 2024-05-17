package com.zzangse.attendance_check.request;

import androidx.annotation.Nullable;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class RemoveMemberNameRequest extends StringRequest {
    private final static String URL = "http://zzangse.store/remove_member.php";
    private Map<String, Integer> map;

    public RemoveMemberNameRequest(int priNum, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);

        map = new HashMap<>();
        map.put("priNum", priNum);
    }

    @Nullable
    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        Map<String, String> stringMap = new HashMap<>();
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            stringMap.put(entry.getKey(), String.valueOf(entry.getValue()));
        }
        return stringMap;
    }
}
